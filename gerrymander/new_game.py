import random
import math
import visualizer as vs

def distance(a,b):
	return math.sqrt(math.pow(a[0]-b[0],2) + math.pow(a[1]-b[1],2))

def generate_map(w,h):
	M1 = M2 = M3 = [[0 for col in range(w)] for row in range(h)]
	points = []
	popsize = 5000
	num_red = num_blue = .48 * popsize
	num_neutral = .04 * popsize
	clustering_coef = 30
	for loop in range(clustering_coef):
		x = random.randint(0,w-1)
		y = random.randint(0,h-1)
		points.append([y,x])
	count = 0
	while count < popsize:
		a = random.randint(0,len(points)-1)
		y1,x1 = points[a]
		b = random.randint(0,len(points)-1)
		y2,x2 = points[b]
		while a == b or distance([y1,x1],[y2,x2]) > w/2:
			b = random.randint(0,len(points)-1)
			y2,x2 = points[b]			
		
		x3 = (x1 + x2) / 2
		y3 = (y1 + y2) / 2
		if M1[y3][x3] + M2[y3][x3] + M3[y3][x3] == 0:
			if num_red > 0:
				M1[y3][x3] = 1
				num_red -= 1
			elif num_neutral > 0:
				M3[y3][x3] = 1
				num_neutral -= 1	
			else:			
				M2[y3][x3] = 1
				num_blue -= 1				
			points.append([y3,x3])			
			count += 1
			
	return [M1,M2,M3]
	
M1,M2,M3 = generate_map(600,800)

vs.vis(M1,M2,M3)