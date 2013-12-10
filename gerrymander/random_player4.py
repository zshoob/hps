import math
import random
import copy
import visualizer as vs

def read_input( ):
	lines = []
	with open('input_map.txt','r') as ipt:
		lines = ipt.read( ).split('\n')[:-1]
	M = []
	for line in lines:
		row = [[int(col.split(',')[0]),int(col.split(',')[1])] for col in line.split(' ')]
		M.append(row)
	return M

def seed_population(n,k,mapsize):
	pop = [[[0,0] for point in range(k)] for sol in range(n)]
	i = int(.4 * mapsize)
	j = mapsize - i
	for sol in range(n):
		for point in range(k):
			pop[sol][point] = [random.randint(i,j),random.randint(i,j)]
	return pop
	
def distance(a,b):
	return math.sqrt(math.pow(a[0]-b[0],2) + math.pow(a[1]-b[1],2))	
	
def populated_areas(M):
	pcells = []
	mapsize = len(M)
	for row in range(mapsize):
		for col in range(mapsize):
			if sum(M[row][col]) > 0:
				pcells.append([row,col])
	return pcells	
	
def evaluate(sol,M,pcells):
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
	k = len(s1)
	s3 = [[0,0] for point in range(k)]
	for point in range(k):
		if random.randint(0,2):
			s3[point] = copy.deepcopy(s1[point])
		else:
			s3[point] = copy.deepcopy(s2[point])
	return s3
	
def sigma_generator(init_sigma,alpha):
	sigma = init_sigma
	while True:
		yield max(sigma,1.0)
		sigma *= alpha
	
def mutate(sol,sigma_gen):
	alpha = 0.5
	mu = 0
	#sigma = 9
	k = len(sol)
	for point in range(k):
		for dim in range(2):
			if random.uniform(0,1) <= alpha:
				sol[point][dim] += math.ceil(random.gauss(mu,sigma_gen.next( )))
	#return sol	
	

def get_centroids(k,pcells):
	centroids = [[random.randint(0,mapsize-1),random.randint(0,mapsize)] for c in range(k)]

	for iter in range(100):
		rs = [0 for i in range(k)]
		cs = [0 for i in range(k)]
		count = [0 for i in range(k)]
		for row,col in pcells:
			mind = float("inf")
			centroid = 0
			for c in range(k):
				d = distance([row,col],centroids[c])
				if d < mind:
					mind = d
					centroid = c
			rs[centroid] += row
			cs[centroid] += col
			count[centroid] += 1
		for c in range(k):
			row = int(float(rs[c]) / float(count[c]) + 0.00001)
			col = int(float(cs[c]) / float(count[c]) + 0.00001)
			centroids[c] = [row,col]
	return centroids
	
def show(sol,M):
	mapsize = len(M)
	k = len(sol)
	count = [0 for d in range(k)]
	D = [[0 for col in range(mapsize)] for row in range(mapsize)]
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
			D[row][col] = d
			count[d] += num_people
	vs.draw_partitions(D)
	
n = 3
k = 7
M = read_input( )
mapsize = len(M)
pcells = populated_areas(M)	
pop = seed_population(n,k,mapsize)
#centroids = get_centroids(k,pcells)
#pop = [centroids for p in range(n)]
minscore = float("inf")
sigma_gen = sigma_generator(9,0.9999)
score = evaluate(pop[0],M,pcells)
while score >= 1.08:
	print '\t' +  str(sigma_gen.next( ))
	pop = sorted(pop, key = lambda sol: evaluate(sol,M,pcells))
	score = evaluate(pop[0],M,pcells)
	print [evaluate(sol,M,pcells) for sol in pop]
	#for p in pop:
	#	print p
	if score < minscore:
		minscore = score
		#show(pop[0],M)
	pop[-1] = cross(pop[0],pop[1])
	#pop[-2] = cross(pop[0],pop[1])
	#pop[-3] = cross(pop[0],pop[1])	
	for s in range(1,n):
		mutate(pop[s],sigma_gen)
	
show(pop[0],M)
