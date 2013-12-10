import math
import random as rndm
import visualizer as vs

'''
def generate_map_inner(M,popsize,color):
	width = len(M)
	points = []	
	gran = 40
	for loop in range(gran):
		x = rndm.randint(0,width-1)
		y = rndm.randint(0,width-1)
		points.append([x,y])
	for loop in range(0,popsize):
		l = len(points)
		a = rndm.randint(0,l-1)
		b = rndm.randint(0,l-1)
		while a == b:
			b = rndm.randint(0,l-1)
		midx = (points[a][0] + points[b][0]) / 2
		midy = (points[a][1] + points[b][1]) / 2			
		points.append([midx,midy])
		M[midx][midy][color] += 1
	return M

def generate_map( ):
	width = 512
	M = [[[0,0] for y in range(width)] for x in range(width)]
	popsize = int(math.pow(2,13))	
	for color in range(8):
		M = generate_map_inner(M,popsize,color%2)
	return M
'''	
	
def distance(a,b):
	return math.sqrt(math.pow(a[0]-b[0],2) + math.pow(a[1]-b[1],2))		
	
def generate_map_inner(M,popsize,color):
	width = len(M)
	points = []	
	gran = 50
	for loop in range(gran):
		x = rndm.randint(0,width-1)
		y = rndm.randint(0,width-1)
		points.append([x,y])
	for loop in range(0,popsize):
		l = len(points)
		a = rndm.randint(0,l-1)
		b = rndm.randint(0,l-1)
		while distance(points[a],points[b]) > .3*width:# or distance(points[a],points[b]) < 4:
			b = rndm.randint(0,l-1)
		midx = (points[a][0] + points[b][0]) / 2
		midy = (points[a][1] + points[b][1]) / 2			
		points.append([midx,midy])
		M[midx][midy][color] += 1
	return M
						

def generate_map( ):
	width = 512
	M = [[[0,0] for y in range(width)] for x in range(width)]
	popsize = int(math.pow(2,10))	
	for color in range(4):
		M = generate_map_inner(M,popsize,color%2)
	return M			
	
def write_map(M):
	out = open('input_map.txt','w')
	width = len(M)
	for row in range(width-1):
		for col in range(width-1):
			out.write(str(M[row][col][0]) + ',' + str(M[row][col][1]) + ' ')
		out.write(str(M[row][-1][0]) + ',' + str(M[row][-1][1]) + '\n')
	for col in range(width-1):
		out.write(str(M[-1][col][0]) + ',' + str(M[-1][col][1]) + ' ')
	out.write(str(M[-1][-1][0]) + ',' + str(M[-1][-1][1]) + '\n')
	out.close( )
	
#M = vs.compress(M,12)