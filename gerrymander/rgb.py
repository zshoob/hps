import png

def distance(a,b):
	return math.sqrt(math.pow(a[0]-b[0],2) + math.pow(a[1]-b[1],2))
	
def color(point,M):
	x,y = point
	red = 0
	blue = 0

	p_list = point_list(M)
	for [col,row] in p_list:
		d = distance([x,y],[col,row])
		red += M[col][row][0] / (math.pow(d,2) + 0.00001)
		blue += M[col][row][1] / (math.pow(d,2) + 0.00001)
		
	if red > blue:
		return red
	else:
		return -1 * blue

def add_gradients(M):
	msize = len(M)
	N = [[0.0 for col in range(msize)] for row in range(msize)]
	for row in range(msize):
		for col in range(msize):
			N[row][col] = color([row,col],M)
	return N
	
#def pop_to_rgb2(M):
		

def pop_to_rgb(M):
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
	#red = [221,30,47]
	red = [3,91,101]
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