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

def draw(M):
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
	plt.savefig('init_state.png')
	
def draw_partition(M,k):
	parts = {}
	width = len(M)

	for col in range(width):
		for row in range(width):
			M[col][row] *= 1

	fig = plt.figure()			
	l = [1 * i for i in range(k)]
	#plt.contourf(range(width),range(width),M,levels=range(k-1))
	plt.imshow(M,interpolation='nearest')
	plt.show()	