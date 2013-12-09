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
area = width*width
k = 17

population = 0
for row in M:
	for col in row:
		population += sum(col)

D = [[-1 for row in range(width)] for col in range(width)]
count = [0 for d in range(k)]

seq = point_seq(width)
parts = []
while len(parts) < k-1:
	p = random.randint(1,area-2)
	if not p in parts:
		parts.append(p)
parts.sort( )
pcopy = [p for p in parts]
		
print pcopy
		
d = 0
last_part = True
for i in range(area):
	x,y = seq[i]
	D[x][y] = d
	count[d] += sum(M[x][y])
	if len(pcopy) > 0 and i >= pcopy[0]:
		pcopy.pop(0)
		d += 1
	'''
	elif len(pcopy) == 0 and last_part:
		last_part = False
		d += 1
	'''
	
for loop in range(1000000):
	idx = random.randint(0,len(parts)-1)
	part = parts[idx]
	x1,y1 = seq[part]
	d1 = D[x1][y1]
	x2,y2 = seq[part+1]
	d2 = D[x2][y2]	
	if count[d1] < count[d2]:
		parts[idx] += 1
		count[d1] += sum(M[x2][y2])
		count[d2] -= sum(M[x2][y2])
		D[x2][y2] = d1
	elif count[d1] > count[d2]:
		parts[idx] -= 1
		count[d1] -= sum(M[x1][y1])
		count[d2] += sum(M[x1][y1])			
		D[x1][y1] = d2		

print count
vs.draw_partitions(D)	

'''		
dsize = int(math.floor(float(population)/float(k)))
D = [[-1 for row in range(width)] for col in range(width)]
count = [0 for d in range(k)]

d = 0
for row,col in point_seq(width):
	D[row][col] = d
	count[d] += sum(M[row][col])
	if count[d] > dsize and d < k-1:
		d += 1	
'''	
	
'''	
print count
	
vs.draw_partitions(D)

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
		
vs.draw_results(M,R)
'''