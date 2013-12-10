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
	for sol in range(n):
		for point in range(k):
			pop[sol][point] = [random.randint(0,mapsize-1),random.randint(0,mapsize-1)]
	return pop
	
def distance(a,b):
	return math.sqrt(math.pow(a[0]-b[0],2) + math.pow(a[1]-b[1],2))	
	
def populated_areas(M):
	plist = []
	mapsize = len(M)
	for row in range(mapsize):
		for col in range(mapsize):
			if sum(M[row][col]) > 0:
				plist.append([row,col])
	return plist	
	
def evaluate(sol,M,plist):
	k = len(sol)
	count = [0 for d in range(k)]
	#for sample in range(10000):
		#row,col = plist[random.randint(0,len(plist)-1)]
	for row,col in plist:
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
	alpha = 0.6
	mu = 0
	#sigma = 9
	k = len(sol)
	for point in range(k):
		for dim in range(2):
			if random.uniform(0,1) <= alpha:
				sol[point][dim] += math.ceil(random.gauss(mu,sigma_gen.next( )))
	#return sol	
	
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
	
n = 5
k = 5
M = read_input( )
mapsize = len(M)
plist = populated_areas(M)	
pop = seed_population(n,k,mapsize)
minscore = float("inf")
sigma_gen = sigma_generator(5,0.9999)
for generation in range(200):
	print '\t' +  str(sigma_gen.next( ))
	pop = sorted(pop, key = lambda sol: evaluate(sol,M,plist))
	score = evaluate(pop[0],M,plist)
	print score
	show(pop[0],M)
	#for p in pop:
	#	print p
	if score < minscore:
		minscore = score
		#show(pop[0],M)
	offspring = cross(pop[0],pop[1])
	pop[-1] = cross(pop[0],pop[1])
	#pop[-2] = cross(pop[0],pop[1])
	#pop[-3] = cross(pop[0],pop[1])	
	for s in range(1,n):
		mutate(pop[s],sigma_gen)
	
