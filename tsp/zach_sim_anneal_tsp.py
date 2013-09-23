import math
import random
import matplotlib as mpl
from mpl_toolkits.mplot3d import Axes3D
import numpy as np
import matplotlib.pyplot as plt
import time
import copy
from twist import twist
from multiprocessing import Pool

num_cities = 1000

g = []
with open('travelingtest.txt') as input:
	g = input.read( ).split('\n')[:-1]
	
for i in range(len(g)):	
	g[i] = [int(x) for x in g[i].split(' ')[1:]]

g = ['zero'] + g		
		
	
def plot(path):	

	path2 = [1]
	roll = path[1]
	for i in range(num_cities):
		path2.append(roll)
		roll = path[roll]
	path = path2
	
	fig = plt.figure()
	ax = fig.gca(projection='3d')
	x = [entry[0] for entry in [g[p] for p in path]]
	y = [entry[2] for entry in [g[p] for p in path]]

	ax.plot(x,y)
	
	plt.show()	

def distance(r,s):
	a = g[r]
	b = g[s]
	return math.sqrt(math.pow(a[0]-b[0],2)+math.pow(a[1]-b[1],2)+math.pow(a[2]-b[2],2))	
	
def generate_candidate_list(num_candidates):
	candidateList = [[] for city in range(num_cities + 1)]
	for c in range(1,len(g)):
		args = [distance(c,other) for other in range(1,len(g))]
		args[c-1] = 1000000
		for loop in range(num_candidates):
			neighbor = np.argmin(args)
			candidateList[c].append(neighbor + 1)
			args[neighbor] = 1000000		
	return candidateList		
	
def length(path):
	length = 0.0
	for i in range(1,len(path)):
		length += distance(i,path[i])
	return length		
	
def greedy_path( ):
	s = set( )
	while len(s) < 3:
		s.add(random.randint(1,num_cities))
	path = list(s)
	path.append(path[0])
	for i in set(range(1,num_cities + 1)).difference(set(path)):
		m = 0
		d1 = distance(path[m],i)
		d2 = distance(path[m+1],i)
		d3 = distance(path[m],path[m+1])
		minCost = (d1 + d2) - d3
		for j in range(1,len(path)-1):
			d1 = distance(path[j],i)
			d2 = distance(path[j+1],i)
			d3 = distance(path[j],path[j+1])
			cost = (d1 + d2) - d3
			if cost < minCost:
				minCost = cost
				m = j
		path.insert(m+1,i)
	#fwd[i] gives the edge forward edge from node i. 
	#Likewise, bkw[i] gives the backward edge from node i. 		
	fwd = [0 for i in range(num_cities + 1)]
	fwd[0] = 'zero'
	bkd = [i for i in fwd]
	for i in range(1,num_cities + 1):
		fwd[path[i]] = path[i-1]
		bkd[path[i-1]] = path[i]
	return [fwd,bkd]
	
def random_path( ):
	seed = [i for i in range(1,num_cities + 1)]
	path = [0 for i in range(num_cities + 1)]
	idx = 0
	for loop in range(num_cities):
		j = random.randint(idx,len(seed)-1)
		temp = seed[j]
		seed[j] = seed[idx]
		seed[idx] = temp
		path[idx] = seed[idx]
		idx += 1
	path[-1] = path[0]
	fwd = [0 for i in range(num_cities + 1)]
	fwd[0] = 'zero'
	bkd = [i for i in fwd]
	for i in range(1,num_cities + 1):
		fwd[path[i]] = path[i-1]
		bkd[path[i-1]] = path[i]
	return [fwd,bkd]	
	
"""	
def twist(fwd,bkd,i,j):
	j_next = fwd[j]
	idx = fwd[i]
	while not idx == j_next:
		temp = fwd[idx]
		fwd[idx] = bkd[idx]
		bkd[idx] = temp
		idx = bkd[idx]
	bkd[j_next] = fwd[i]
	fwd[fwd[i]] = j_next
	fwd[i] = j
	bkd[j] = i
	return [fwd,bkd]
"""
		
def k_opt(fwd,p_length):
	i = random.randint(1,num_cities)
	j = cl[i][random.randint(0,len(cl[i])-1)]
	d1 = distance(i,fwd[i]) + distance(j,fwd[j])
	d2 = distance(i,j) + distance(fwd[i],fwd[j])
	d3 = (p_length - d1) + d2
	return [i,j,d3]

			
def P(prev_score, next_score, temperature):
    if next_score < prev_score:
        return 1.0
    else:
    	if temperature == 0.0:
    		return 0.0
        return math.exp( -abs(next_score-prev_score)/temperature )

def heat_generator(start_temp,alpha):
    t = start_temp
    while True:
        yield t
        t = alpha*t

def anneal(path,path_length,start_temp,alpha,limit):
	start = time.clock( )
	#fwd[i] gives the forward edge from node i. 
	#Likewise, bkd[i] gives the backward edge from node i. 
	fwd = path[0]
	bkd = path[1]
	current_score = length(fwd)
	best = path
	best_score = current_score
	
	heat_gen = heat_generator(start_temp,alpha)
	done = False 

	num_evals = 0
	
	for temperature in heat_gen:
	
		if time.clock( ) - start >= limit:
			done = True
			break		
		
		if num_evals % 10000 == 0:
			print str(current_score) + '\t' + str(temperature)		
		
		num_evals += 1
		
		k_tuple = k_opt(fwd,current_score)
		i = k_tuple[0]
		j = k_tuple[1]
		next_score = k_tuple[2]
		
		
		p = P(current_score,next_score,temperature)
		if random.random() < p:
			twist(fwd,bkd,i,j) #apply the 2-opt transformation
			current_score = next_score
			if current_score < best_score:
				best = [fwd,bkd]
				best_score = current_score
	
	return [best,best_score,temperature]

num_candidates = 50
alpha = 0.9999997 #rate of cooling
start_temp = 270 #initial temperature
start = time.clock( )	
cl = generate_candidate_list(num_candidates)	
p = greedy_path( )
timeleft = 115 - (time.clock( ) - start)
a = anneal(p,length(p[0]),start_temp,alpha,timeleft)
print a[1]
plot(a[0][0])

"""
start = time.clock( )	
cl = generate_candidate_list(60)	
p = greedy_path( )
timeleft = 118 - (time.clock( ) - start)
a = anneal(p,length(p[0]),270,0.9999998,timeleft)
print a[1]
plot(a[0][0])
"""

"""	
p = random_path( )
a = anneal(p,length(p[0]),200,0.9999995,30)
plot(a[0][0])
"""
