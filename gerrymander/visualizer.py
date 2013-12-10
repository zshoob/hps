import matplotlib.pyplot as plt
from matplotlib import cm
from matplotlib import colors
import math

def point_list(M):
	l = []
	for col in range(len(M)):
		for row in range(len(M[col])):
			if sum(M[col][row]) > 0:
				l.append([col,row])
	return l	
	
def distance(a,b):
	return math.sqrt(math.pow(a[0]-b[0],2) + math.pow(a[1]-b[1],2))
	
def color(x,y,M):
	red = 0
	blue = 0

	p_list = point_list(M)
	for [col,row] in p_list:
		d = distance([x,y],[col,row])
		red += M[col][row][0] / (math.pow(d,2) + 0.00001)
		blue += M[col][row][1] / (math.pow(d,2) + 0.00001)
		
	if red > blue:
		return red
	else:
		return -1 * blue

def compress(M,factor):
	width = len(M[0])
	N = [[[0,0]	for row in range(width/factor)] for col in range(width/factor)]
	for col in range(width/factor):
		for row in range(width/factor):
			for x in range(factor):
				for y in range(factor):
					N[col][row][0] += M[col*factor + x][row*factor + y][0]
					N[col][row][1] += M[col*factor + x][row*factor + y][1]
	return N

def draw_population(M):
	'''
	input: M - a population map (given by generate_map.generate_map( ))
	
	output: writes file 'population_map.png' to the local directory, which
			shows the color of the dominant part in each area, weighted
			by population density			
	'''
	M = compress(M,8)
	width = len(M[0])
	P = [[0 for y in range(width)] for x in range(width)]
	for col in range(width):
		for row in range(width):
			#color = M[col][row][0] - M[col][row][1]
			#color = (M[col][row][0]) / (M[col][row][0] + M[col][row][1] + 0.0001)
			P[col][row] = color(col,row,M)

	fig = plt.figure()
	plt.subplots_adjust(left=0.0,right=1.0,top=1.0,bottom=0.0)
	plt.gca(frame_on=False, xticks=[], yticks=[],aspect='equal')
	#ax = fig.add_subplot(frame_on=False, xticks=[], yticks=[],aspect='equal')
	plt.contourf(range(width),range(width),P,cmap=cm.RdBu,aspect='equal')
	#plt.show( )
	plt.savefig('population_map.png')
	
def draw_partitions(M):
	'''
	input: M a partitioning of the population map, where 
			M[col][row] = n if the point (col,row) lies in 
			district n
			
	output: writes file 'partition_map.png' to the local 
			directory, in which each district is shown
			with a different color
	'''
	fig = plt.figure()			
	plt.gca(frame_on=False, xticks=[], yticks=[],aspect='equal')	
	plt.imshow(M,interpolation='nearest',cmap=cm.Set1)
	#plt.savefig('partition_map.png')
	plt.show( )	

	
def draw_results(P,R):
	'''
	input: P - a population map (given by generate_map.generate_map( ))
		   R - a map of the election results, where 
		   			R[x][y] = 0 if the point (x,y) is in a district which 
		   			voted 'blue' and R[x][y] = 1 otherwise
	output: writes file 'results_map.png' to the local directory, which shows
			the color which won each district, weighted by population size
	'''
	width = len(P)
	M = [[0 for row in range(width)] for col in range(width)]
	for col in range(width):
		for row in range(width):
			if P[col][row][0] > P[row][col][1]:
				M[col][row] = -1 * (1+sum(P[col][row]))
			else:
				M[col][row] = 1+sum(P[col][row])
	fig = plt.figure()			
	plt.gca(frame_on=False, xticks=[], yticks=[],aspect='equal')				
	#plt.contourf(range(width),range(width),R,cmap=cm.RdBu,aspect='equal')
	plt.imshow(R,interpolation='nearest',cmap=cm.RdBu)	
	plt.savefig('results_map.png')	
	
def vis(M1,M2,M3):
	w = 600
	h = 800
	for row in range(h):
		for col in range(w):
			for r in range(row-3,row+3):
				for c in range(col-3,col+3):
					try:
						M1[r][c] = M2[r][c]
						M1[r][c] = M3[r][c]
					except:
						pass
	fig = plt.figure()			
	plt.gca(frame_on=False, xticks=[], yticks=[],aspect='equal')				
	#plt.contourf(range(width),range(width),R,cmap=cm.RdBu,aspect='equal')
	plt.imshow(M1,interpolation='nearest',cmap=cm.Blues)	
	#plt.show()			
	
def vis2(M):
	M2 = [[0 for col in row] for row in M]
	for row in range(len(M)):
		for col in range(len(M)):
			if M[row][col][0] > M[row][col][1]:
				M2[row][col] = 100
			elif M[row][col][0] < M[row][col][1]:
				M2[row][col] = -100
	fig = plt.figure()			
	plt.gca(frame_on=False, xticks=[], yticks=[],aspect='equal')				
	#plt.contourf(range(width),range(width),R,cmap=cm.RdBu,aspect='equal')
	plt.imshow(M2,interpolation='nearest')
	plt.show()			