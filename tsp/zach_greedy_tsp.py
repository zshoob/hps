import math
import random
import matplotlib as mpl
from mpl_toolkits.mplot3d import Axes3D
import numpy as np
import matplotlib.pyplot as plt
import time
import copy

g = []
with open('travelingtest.txt') as input:
	g = input.read( ).split('\n')[:-1]
	
for i in range(len(g)):	
	g[i] = [int(x) for x in g[i].split(' ')[1:]]

g = ['zero'] + g
	
def distance(r,s):
	a = g[r]
	b = g[s]
	return math.sqrt(math.pow(a[0]-b[0],2)+math.pow(a[1]-b[1],2)+math.pow(a[2]-b[2],2))
	
def length(path):
	length = 0.0
	for i in range(1,len(path)):
		length += distance(path[i],path[i-1])
	return length	
	
def plot(path):	
	
	fig = plt.figure()
	ax = fig.gca(projection='3d')
	x = [entry[0] for entry in [g[p] for p in path]]
	y = [entry[2] for entry in [g[p] for p in path]]
	z = [entry[1] for entry in [g[p] for p in path]]

	ax.plot(x,y)
	
	plt.show()	

def greedy_path():
	path = [1,2,3,1]
	for v in range(4,1001):
		m = 1
		d1 = distance(path[m],v)
		d2 = distance(path[m+1],v)
		d3 = distance(path[m],path[m+1])
		minCost = (d1 + d2) - d3
		for j in range(1,len(path)-1):
			d1 = distance(path[j],v)
			d2 = distance(path[j+1],v)
			d3 = distance(path[j],path[j+1])
			cost = (d1 + d2) - d3
			if cost < minCost:
				minCost = cost
				m = j
		path.insert(m+1,v)
	return path
	
path = greedy_path( )
print length(path)
plot(path)
