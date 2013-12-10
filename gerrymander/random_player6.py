import random
import math
import copy

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
	
def temp_generator(temp,alpha):
	t = temp
	while True:
		yield t
		t *= alpha
		
def mutate(sol,alpha):
	newsol = copy.deepcopy(sol)
	centroid = random.randint(0,len(sol)-1)
	newsol[centroid][0] += math.ceil(random.gauss(0,alpha))
	newsol[centroid][1] += math.ceil(random.gauss(0,alpha))		
	return newsol
	
def anneal:
	temp_gen = temp_generator(5,0.99995)
	
		
	