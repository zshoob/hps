import random
import math

def read_input( ):
	lines = []
	with open('input_map.txt','r') as ipt:
		lines = ipt.read( ).split('\n')[:-1]
	M = []
	for line in lines:
		row = [[int(col.split(',')[0]),int(col.split(',')[1])] for col in line.split(' ')]
		M.append(row)
	return M
	
def prime_factors(k):
	factors = []
	d = 2
	while d*d <= k:
		while (k % d) == 0:
			factors.append(d)
			k /= d
		d += 1
	if k > 1:
		factors.append(k)
	return factors	
	
def populated_cells(M):
	pcells = []
	mapsize = len(M)
	for row in range(mapsize):
		for col in range(mapsize):
			if sum(M[row][col]) > 0:
				pcells.append([row,col])
	return pcells		
	
def distance(a,b):
	return math.sqrt(math.pow(a[0]-b[0],2) + math.pow(a[1]-b[1],2))		

def nearest_centroid(point,centroids):
	row,col = point
	mind = float("inf")
	centroid = 0
	for c in range(k):
		d = distance([row,col],centroids[c])
		if d < mind:
			mind = d
			centroid = c
	return centroid

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
			row = int(float(rs[c]) / (float(count[c]) + 0.0001))
			col = int(float(cs[c]) / (float(count[c]) + 0.0001))
			centroids[c] = [row,col]
	return centroids

k = 5
M = read_input( )
mapsize = len(M)
pcells = populated_cells(M)
popsize = sum([sum(M[row][row]) for row,col in pcells])
target = int(1.05 * float(popsize) / float(k)) 

centroids = get_centroids(k,pcells)
clusters = [[] for c in range(k)]
for point in pcells:
	c = nearest_centroid(point,centroids)
	clusters[c].append(point)

for i in range(k):
	neighbors = centroids[:i] + centroids[i+1:]
	while len(clusters[i]) > target:
		mind = float("inf")
		minidx = 0
		neighbor = 0
		for idx in range(clusters[i]):
			point = clusters[idx]
			nearest = nearest_neighbor(point,neighbors)
			d = distance(point,neighbors[nearest])
		

centroids = get_centroids(k,pcells)
clist = [[centroids]]