import math

def read_input( ):
	lines = []
	with open('input_map.txt','r') as ipt:
		lines = ipt.read( ).split('\n')[:-1]
	M = []
	for line in lines:
		row = [[int(col.split(',')[0]),int(col.split(',')[1])] for col in line.split(' ')]
		M.append(row)
	return M

def transpose(M):
	T = []
	width = 0
	for row in M:
		if len(row) > width:
			width = len(row)
	for row in range(width):
		new_row = []
		for r in M:
			if len(r) > row:
				new_row.append(r[row])
		T.append(new_row)
	return T
	
def prime_factors(k):
	factors = []
	d = 2
	while d*d <= k:
		while (k % d) == 0:
			factors.append(d)
			k /= d
		d += 1
	if k > 1:
		factors.append(k)
	return factors
    
def square_dims(n):
	a = int(math.floor(math.sqrt(n)))
	b = n - a
	return [a,b]
	
def init_pointlist(w):
	pointlist = []
	for row in range(w):
		for col in range(w):
			pointlist.append([row,col])
	return pointlist
	
def partition(pointlist,factors):
	if len(factors) == 0:
		return pointlist

for i in range(2,30):
	print square_dims(i)	