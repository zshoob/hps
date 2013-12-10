def pop_to_rgb(M):
	red = 'B0171F'
	blue = '1874CD'
	white = 'FFFFFF'
	msize = len(M)
	N = [[white for col in range(msize)] for row in range(msize)]
	for row in range(msize):
		for col in range(msize):
			if M[row][col][0] > M[row][col][1]:
				N[row][col] = blue
			elif M[row][col][0] < M[row][col][1]:
				N[row][col] = red
	return N
	
def write_pop(M,fname):
	N = pop_to_rgb(M)
	nsize = len(N)
	out = open(fname,'w')
	for row in range(nsize):
		for col in range(nsize):
			out.write(N[row][col])
			if col < nsize-1:
				out.write(',')
		if row < nsize-1:
			out.write('\n')
	out.close( )