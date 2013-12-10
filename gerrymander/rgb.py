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
	
def partitions_to_rgb(M):
	colors = [[221,30,47],[235,176,53],[6,162,203],[33,133,89],[208,198,177]]
	districts = []
	msize = len(M)
	for row in range(msize):
		for col in range(msize):
			district = M[row][col]
			if not district in districts:
				districts.append(district)
	N = []
	for row in range(msize):
		r = []
		for col in range(msize):	
			color = colors[districts.index(M[row][col])]
			r.extend(color)
		N.append(r)
	return N
	
def write_partitions(M,fname):
	P = partitions_to_rgb(M)
	f = open(fname, 'wb')
	w = png.Writer(512,512)
	w.write(f, P)
	f.close()
	
def results_to_rgb(M):
	red = [221,30,47]
	blue = [6,162,203]
	msize = len(M)
	N = []
	for row in range(msize):
		r = []
		for col in range(msize):	
			if M[row][col] == 0:
				r.extend(blue)
			else:
				r.extend(red)
		N.append(r)
	return N
	
def write_results(M,fname):
	R = results_to_rgb(M)
	f = open(fname, 'wb')
	w = png.Writer(512,512)
	w.write(f, R)
	f.close()