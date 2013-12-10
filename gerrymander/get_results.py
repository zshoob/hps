import random

def get_results(P,M,k):
	msize = len(M)
	count = [[0,0] for d in range(k)]
	for row in range(msize):
		for col in range(msize):
			d = P[row][col]
			count[d][0] += M[row][col][0]
			count[d][1] += M[row][col][1]
	results = [0 for d in range(k)]
	for d in range(k):
		num_blue = float(count[d][0])
		num_red = float(count[d][1])
		prob_blue = num_blue / (num_red+num_blue)
		
		if random.random( ) < prob_blue:
			results[d] = 0
		else:
			results[d] = 1
	
	R = [[0 for col in range(msize)] for row in range(msize)]
	for row in range(msize):
		for col in range(msize):
			d = P[row][col]
			R[row][col] = results[d]
	return R				