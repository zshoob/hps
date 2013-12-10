import png

'''
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
'''

def pop_to_rgb(M):
	'''
	red = '221,30,47,'
	blue = '6,162,203'
	white = '208,198,177'	
	'''
	red = [221,30,47]
	blue = [6,162,203]
	white = [255,255,255]
	msize = len(M)	
	N = []
	for row in range(msize):
		r = []
		for col in range(msize):
			if M[row][col][0] > M[row][col][1]:
				r.extend(blue)
			elif M[row][col][0] < M[row][col][1]:
				r.extend(red)
			else:
				r.extend(white)
		N.append(r)
	return N
	
def write_pop(M,fname):
	P = pop_to_rgb(M)
	f = open(fname, 'wb')
	w = png.Writer(512,512)
	w.write(f, P)
	f.close()
	
def districts_to_rgb(D):
	colors = ['DD1E2F','EBB035','06A2CB','218559','D0C6B1']
	districts = []
	dsize = len(D)
	for row in range(dsize):
		for col in range(dsize):
			district = D[row][col]
			if not district in districts:
				districts.append(district)
	D2 = [['' for col in range(dsize)] for row in range(dsize)]
	for row in range(dsize):
		for col in range(dsize):	
			color = colors[districts.index(D[row][col])]
			D2[row][col] = color
	return D2
	
def write_partition(D,fname):
	M = districts_to_rgb(D)
	msize = len(M)
	out = open(fname,'w')
	for row in range(msize):
		for col in range(msize):
			out.write(M[row][col])
			if col < msize-1:
				out.write(',')
		if row < msize-1:
			out.write('\n')
	out.close( )	