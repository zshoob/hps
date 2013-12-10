import copy
import visualizer as vs

def search(D,point,district):
	dsize = len(D)
	q = [point]
	i = 0
	while len(q) > 0:
		i += 1
		if i % 100000 == 0:
			print i
			vs.draw_partitions(D)
		r,c = q.pop(0)
		D[r][c] = (D[r][c] + 1) * -1
		for row in [r-1,r+1]:
			for col in [c-1,c+1]:
				if row >= 0 and row < dsize and col >= 0 and col < dsize:
					if D[row][col] == district:
						q.append([row,col])

def check_contiguous(D,k):
	D = copy.deepcopy(D)
	dsize = len(D)
	for iter in range(k):
		print iter
		for row in range(dsize):
			for col in range(dsize):
				if D[row][col] > -1:
					search(D,[row,col],D[row][col])
	for row in range(dsize):
		for col in range(dsize):
			if D[row][col] > -1:
				return False
	return True
	
