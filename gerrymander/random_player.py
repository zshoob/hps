import math, random, copy, sys
import get_results as gr
import rgb

def read_input(fname):
	'''
		Returns a 2d list giving the map's population distribution, read from @fname		
	'''
	lines = []
	with open(fname,'r') as ipt:
		lines = ipt.read( ).split('\n')[:-1]
	M = []
	for line in lines:
		row = [[int(col.split(',')[0]),int(col.split(',')[1])] for col in line.split(' ')]
		M.append(row)
	return M
	
def write_solution(P):
	'''
		Writes district partition map @p to random_player_solution.txt in the 
		local directory
	'''
	mapsize = len(P)
	out = open('random_player_solution.txt','w')
	for row in range(mapsize):
		for col in range(mapsize):
			out.write(str(P[row][col]))
			if col < mapsize-1:
				out.write(' ')
			else:
				out.write('\n')
	out.close( )	

def seed_population(n,k,mapsize):
	'''
		Returns an initial population k randomly generated centroids
		for each of n candidate solutions.
	'''
	pop = [[[0,0] for point in range(k)] for sol in range(n)]
	i = int(.4 * mapsize)
	j = mapsize - i
	for sol in range(n):
		for point in range(k):
			pop[sol][point] = [random.randint(i,j),random.randint(i,j)]
	return pop
	
def distance(a,b):
	'''
		Returns the euclidean distance between a and b.
	'''
	return math.sqrt(math.pow(a[0]-b[0],2) + math.pow(a[1]-b[1],2))	
	
def populated_areas(M):
	'''
		Returns a list of all points in population @M which 
		have a non-zero population
	'''
	pcells = []
	mapsize = len(M)
	for row in range(mapsize):
		for col in range(mapsize):
			if sum(M[row][col]) > 0:
				pcells.append([row,col])
	return pcells	
	
def evaluate(sol,M,pcells):
	'''
		Partitions population map @M into k districts based on
		the centroids in sol. Returns a fitness function, which
		is the size of the densest district divided by that of
		the sparsest.
	'''
	k = len(sol)
	count = [0 for d in range(k)]
	#for sample in range(10000):
		#row,col = pcells[random.randint(0,len(pcells)-1)]
	for row,col in pcells:
		num_people = sum(M[row][col])
		mindist = float("inf")
		d = None
		for point in range(k):
			dist = distance(sol[point],[row,col])
			if dist < mindist:
				mindist = dist
				d = point
		count[d] += num_people
	return float(max(count)) / float(min(count) + 0.0001)
	
def cross(s1,s2):
	'''
		Randomly generates an offspring of candidate solutions @s1
		and @s2 by taking centroids from each
	'''
	k = len(s1)
	s3 = [[0,0] for point in range(k)]
	for point in range(k):
		if random.randint(0,2):
			s3[point] = copy.deepcopy(s1[point])
		else:
			s3[point] = copy.deepcopy(s2[point])
	return s3
	
def sigma_generator(init_sigma,alpha):
	'''
		A generator which yields a slowly decreasing value 
		of sigma, used in the mutate function below
	'''	
	sigma = init_sigma
	while True:
		yield max(sigma,1.0)
		sigma *= alpha
	
def mutate(sol,sigma_gen):
	'''
		Randomly mutates the centroids in @sol
		@alpha: the probably that one dimension of a given centroid
				will mutate
		@mu: the mean distance centroids move when they mutate
		@sigma: the standard deviation of the distance centroids move 
				when they mutate. this will decrease by a small amount 
				each time the function is called.
	'''
	alpha = 0.5
	mu = 0
	k = len(sol)
	for point in range(k):
		for dim in range(2):
			if random.uniform(0,1) <= alpha:
				sol[point][dim] += math.ceil(random.gauss(mu,sigma_gen.next( )))
	
	
def generate_solution(M,k):	
	'''
		Evolves a list of k centroids using a genetic algorithm. 
		The k centroids give a Voronoi tesselation of 
		population map @M which 
		produces a valid district partitioning of @M.
		
		@n: the number of candidate solutions in the population
	'''	
	n = 5
	mapsize = len(M)
	pcells = populated_areas(M)	
	pop = seed_population(n,k,mapsize)
	minscore = float("inf")
	sigma_gen = sigma_generator(9,0.9999)
	score = evaluate(pop[0],M,pcells)
	while score >= 1.3:
		#print '\t' +  str(sigma_gen.next( ))
		pop = sorted(pop, key = lambda sol: evaluate(sol,M,pcells))
		score = evaluate(pop[0],M,pcells)
		#print score
		#for p in pop:
		#	print p
		if score < minscore:
			minscore = score
		pop[-1] = cross(pop[0],pop[1])
		for s in range(1,n):
			mutate(pop[s],sigma_gen)
	
	sol = pop[0]
	
	P = [[0 for col in range(mapsize)] for row in range(mapsize)]
	for row in range(mapsize):
		for col in range(mapsize):
			num_people = sum(M[row][col])
			mindist = float("inf")
			d = None
			for point in range(k):
				dist = distance(sol[point],[row,col])
				if dist < mindist:
					mindist = dist
					d = point			
			P[row][col] = d

	return P
	
def main(args):
	M = read_input('population_map.txt')
	k = int(args[0])
	P = generate_solution(M,k)
	write_solution(P)
	
if __name__ == "__main__":
	main(sys.argv[1:])	