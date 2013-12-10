import math
import random as rndm
	
def distance(a,b):
	'''
		Returns the euclidean distance between points a and b.
	'''
	return math.sqrt(math.pow(a[0]-b[0],2) + math.pow(a[1]-b[1],2))		
	
def generate_map_inner(M,popsize,color):
	'''
		This function in-place inserts @popsize people voting for
		party @color into population map @M.
		
		@alpha controls the cluster density of the population 
		distribution. as alpha decreases, clusters will tend to
		get more dense.
	'''
	width = len(M)
	points = []	
	alpha = 50
	for loop in range(alpha):
		x = rndm.randint(0,width-1)
		y = rndm.randint(0,width-1)
		points.append([x,y])
	for loop in range(0,popsize):
		l = len(points)
		a = rndm.randint(0,l-1)
		b = rndm.randint(0,l-1)
		while distance(points[a],points[b]) > .3*width:
			b = rndm.randint(0,l-1)
		midx = (points[a][0] + points[b][0]) / 2
		midy = (points[a][1] + points[b][1]) / 2			
		points.append([midx,midy])
		M[midx][midy][color] += 1
						


def generate_map( ):
	'''
		Returns a 3d-list giving a stochastic distribution of 
		equal numbers of red and blue voters. 
		
		@M: the map to be generated, where m[row][col] = 
			[blue,red], which gives the number of blue and
			red voters living at (row,col)
		@width: the width and height dimensions of the map. 
			(The map is always a square)
		@popsize: the total number of people to be distributed
			(should be even)
		@num_passes: the number of independent population clusters
			to be formed on the map, each with voters for a single color
			(should *definitely* be even)
	'''
	width = 512
	M = [[[0,0] for y in range(width)] for x in range(width)]
	popsize = int(math.pow(2,12))	
	num_passes = 4
	for color in range(num_passes):
		generate_map_inner(M,popsize/num_passes,color%2)
	return M			
	
def write_map(M):
	'''
		Writes population map @M to the local directory with the 
		name population_map.txt. All rows are separated by a newline,
		columns by a single space, and population tuples by a comma.
	'''
	out = open('population_map.txt','w')
	width = len(M)
	for row in range(width-1):
		for col in range(width-1):
			out.write(str(M[row][col][0]) + ',' + str(M[row][col][1]) + ' ')
		out.write(str(M[row][-1][0]) + ',' + str(M[row][-1][1]) + '\n')
	for col in range(width-1):
		out.write(str(M[-1][col][0]) + ',' + str(M[-1][col][1]) + ' ')
	out.write(str(M[-1][-1][0]) + ',' + str(M[-1][-1][1]) + '\n')
	out.close( )
	
def main( ):
	write_map(generate_map( ))
	
if __name__ == "__main__":
	main( )	
	
#M = vs.compress(M,12)