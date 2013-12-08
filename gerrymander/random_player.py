import generate_map as gm
import visualizer as vs
import random
import math
import itertools

def point_seq(width):
	w = width/2
	seq = []
	for layer in range(w):
		seq.extend([[layer,col] for col in range(layer,width-layer)])	
		seq.extend([[row,width-layer-1] for row in range(layer+1,width-layer)])
		seq.extend([[width-layer-1,col] for col in range(width-layer-2,layer-1,-1)])
		seq.extend([[row,layer] for row in range(width-layer-2,layer,-1)])
	return seq
		

M = gm.generate_map( )
width = len(M)
k = 17

population = 0
for row in M:
	for col in row:
		population += sum(col)
		
dsize = population/k
D = [[-1 for row in range(width)] for col in range(width)]
count = [0 for d in range(k)]

d = 0
for row,col in point_seq(width):
	D[row][col] = d
	count[d] += sum(M[row][col])
	if count[d] > dsize and d < k-1:
		d += 1

		
	
print count
	
vs.draw_partition(D,k)
