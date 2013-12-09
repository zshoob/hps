import random
import math
import visualizer as vs

def distance(a,b):
	return math.sqrt(math.pow(a[0]-b[0],2) + math.pow(a[1]-b[1],2))

def generate_map(w,h):
	''' 
		M1,M2,M3 are sparse matrices for red, blue and neutral voters, respectively						  
	'''
	M1 = M2 = M3 = [[0 for col in range(w)] for row in range(h)]
	points_list = []
	popsize = 10000
	num_red = num_blue = .48 * popsize
	num_neutral = .04 * popsize
	# a greater clustering_coef means less clustering
	clustering_coef = 70
	# seed points_list with some number of randomly generated coord points
	for loop in range(clustering_coef):
		x = random.randint(0,w-1)
		y = random.randint(0,h-1)
		points_list.append([y,x])
	# loop until popsize citizens have been created
	count = 0
	while count < popsize:
		# choose two coord points randomly from points_list
		# a greater distance_coef means more clusters
		distance_coef = .40
		a = random.randint(0,len(points_list)-1)
		y1,x1 = points_list[a]
		b = random.randint(0,len(points_list)-1)
		y2,x2 = points_list[b]
		while a == b or distance([y1,x1],[y2,x2]) > distance_coef * w:
			b = random.randint(0,len(points_list)-1)
			y2,x2 = points_list[b]			
		# generate the midpoint between the two randomly selected points
		x3 = (x1 + x2) / 2
		y3 = (y1 + y2) / 2
		# if (y3,x3) is unfilled in all three maps
		if M1[y3][x3] + M2[y3][x3] + M3[y3][x3] == 0:
			# add 1 person to one of the three maps
			if num_red > 0:
				M1[y3][x3] = 1
				num_red -= 1
			elif num_neutral > 0:
				M3[y3][x3] = 1
				num_neutral -= 1	
			else:			
				M2[y3][x3] = 1
				num_blue -= 1				
			# add the midpoint to the points list
			points_list.append([y3,x3])		
			# increment population count	
			count += 1
			
	return [M1,M2,M3]
	
M1,M2,M3 = generate_map(600,800)

vs.vis(M1,M2,M3)