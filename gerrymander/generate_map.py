import math
import random as rndm

def generate_map_inner(M,popsize,color):
	width = len(M)
	points = []	
	gran = 40
	for loop in range(gran):
		x = rndm.randint(0,width-1)
		y = rndm.randint(0,width-1)
		points.append([x,y])
	for loop in range(0,popsize):
		#if loop % ((popsize-(2*n))/5) == 0:
		#	draw(M,n)
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