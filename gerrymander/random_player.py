import generate_map as gm
import visualizer as vs
import random
import math
import itertools

def cross_border(P,point):
	width = len(P)
	center = width/2
	x,y = point
	#neighbors = [item for item in itertools.product([x-1,x+1],[y-1,y+1])]
	#neighbors = [[x-1,y],[x+1,y],[x,y-1],[x,y+1]]
	if x < center:
		if y < center:
			neighbors = [[x+1,y],[x,y+1]]
		else:
			neighbors = [[x+1,y],[x,y-1]]
	else:
		if y < center:
			neighbors = [[x-1,y],[x,y+1]]
		else:
			neighbors = [[x-1,y],[x,y-1]]
	random.shuffle(neighbors)
	for col,row in neighbors:
		if col < 0 or col >= width or row < 0 or row >= width:
			continue
		if P[x][y] != P[col][row]:
			return [col,row]		
	return None
	
def cross_border2(P,point):
	width = len(P)
	x,y = point
	points = []
	neighbors = [[x-1,y],[x+1,y],[x,y-1],[x,y+1]]
	random.shuffle(neighbors)
	for col,row in neighbors:
		if col < 0 or col >= width or row < 0 or row >= width:
			continue
		if P[x][y] != P[col][row]:
			points.append([col,row])
	return points
	
def is_thread(P,point):
	width = len(P)
	x,y = point
	count = 0
	for col in range(x-1,x+2):
		for row in range(y-1,y+2):
			if col < 0 or col >= width or row < 0 or row >= width:
				continue	
			if P[x][y] == P[col][row]:
				count += 1
	return count < 6		
	'''
	neighbors = [[x-1,y],[x+1,y],[x,y-1],[x,y+1]]
	for col,row in neighbors:
		if col < 0 or col >= width or row < 0 or row >= width:
			continue
		if P[x][y] == P[col][row]:
			return False
	return True


	for col in [x-1,x+1]:
		if col < 0 or col >= width:
			continue		
		for row in [y-1,y+1]:
			if row < 0 or row >= width:
				continue
			if P[x][y] == P[col][row]:
				return False
	return True
	'''
	
				

M = gm.generate_map( )

vs.draw(M)
print 'here'

k = 4
width = len(M)
part_size = int(math.ceil(width/k))
borders = []
count = [0 for district in range(k)]
parts = []
while len(parts) < k-1:
	part = random.randint(0,width)
	if not part in parts:
		parts.append(part)

P = [[0 for row in range(width)] for col in range(width)]
d = 0
for col in range(width):
	part_here = False
	if col in parts:
		part_here = True
		d += 1
	for row in range(width):
		P[col][row] = d
		count[d] += sum(M[col][row])
		if part_here:
			borders.append([col,row])

print count

for loop in range(10000):
	x1,y1 = borders.pop(random.randint(0,len(borders)-1))
	d1 = P[x1][y1]
	'''
	p2 = cross_border(P,[x1,y1])
	if p2:
		x2,y2 = p2
	else:
		continue
	'''
	for p2 in cross_border2(P,[x1,y1]):
		x2,y2 = p2	
		d2 = P[x2][y2]
		#if is_thread(P,[x1,y1]) or is_thread(P,[x2,y2]):
		#	borders.append([x1,y1])	
		#	continue
		if count[d1] > count[d2]:
			P[x1][y1] = d2
			count[d2] += sum(M[x1][y1])
			count[d1] -= sum(M[x1][y1])
			borders.append([x1,y1])
		else:
			P[x2][y2] = d1
			count[d1] += sum(M[x2][y2])
			count[d2] -= sum(M[x2][y2])
			borders.append([x2,y2])

print count

#vs.draw_partition(P,k)	
