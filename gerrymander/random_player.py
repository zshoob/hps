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

def read_input( ):
	lines = []
	with open('input_map.txt','r') as ipt:
		lines = ipt.read( ).split('\n')[:-1]
	M = []
	for line in lines:
		row = [[int(col.split(',')[0]),int(col.split(',')[1])] for col in line.split(' ')]
		M.append(row)
	return M

def write_solution(D):
	out = open('solution.txt','w')
	width = len(D)
	for row in range(width-1):
		for col in range(width-1):
			out.write(str(D[row][col]) + ' ')
		out.write(str(D[row][-1]) + '\n')
	for col in range(width-1):
		out.write(str(D[-1][col]) + ' ')
	out.write(str(D[-1][-1]) + '\n')
	out.close( )	

def generate_solution(M,k):
	width = len(M)
	
	population = 0
	for row in M:
		for col in row:
			population += sum(col)
		
	dsize = int(math.floor(float(population)/float(k)))
	D = [[-1 for row in range(width)] for col in range(width)]
	count = [0 for d in range(k)]

	d = 0
	for row,col in point_seq(width):
		D[row][col] = d
		count[d] += sum(M[row][col])
		if count[d] > dsize and d < k-1:
			d += 1	
			
	return D			
		
k = 5		
M = read_input( )
D = generate_solution(M,k)
		
#vs.draw_results(M,D)
vs.draw_partitions(D)
#vs.draw_population(M)

'''
result = [[0,0] for d in range(k)]
for col in range(width):
	for row in range(width):
		d = D[col][row]
		result[d][0] += M[col][row][0]
		result[d][1] += M[col][row][1]
	
for d in range(k):
	if result[d][0] > result[d][1]:
		result[d] = 0
	else:
		result[d] = 1
	
R = [[-1 for row in range(width)] for col in range(width)]
for col in range(width):
	for row in range(width):
		d = D[col][row]
		R[col][row] = result[d]
'''