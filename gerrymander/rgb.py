import png

def pop_to_rgb(M):
	'''
		Maps population map @M to a 2d-list @N of rgb values, for writing to .png.
		Thus, N[row][col] = red if M[row][col] has a majority of red voters, 
		or blue if a majority of blue voters, or white otherwise.
	'''
	red = [221,30,47]
	blue = [6,162,203]
	white = [245,245,220]
	msize = len(M)	
	N = [[white for col in range(msize)] for row in range(msize)]
	for row in range(msize):
		for col in range(msize):	
			if M[row][col][0] > M[row][col][1]:
				for r in range(max(0,row-1),min(msize-1,row+2)):
					for c in range(max(0,col-1),min(msize-1,col+2)):				
						N[r][c] = blue
			elif M[row][col][0] < M[row][col][1]:
				for r in range(max(0,row-1),min(msize-1,row+2)):
					for c in range(max(0,col-1),min(msize-1,col+2)):				
						N[r][c] = red
	for row in range(msize):
		N[row] = [val for color in N[row] for val in color]				
	return N
	
def write_pop(M,fname):
	'''
		@M: a population distribution map
		@P: a mapping of rgb values from @M (see comments in function 
			pop_to_rgb(M) above)
		
		Writes @P to a png file in the local directory with name @fname.
	'''
	P = pop_to_rgb(M)
	f = open(fname, 'wb')
	w = png.Writer(512,512)
	w.write(f, P)
	f.close()
	
def partitions_to_rgb(M):
	'''
		@colors: a standard set of colors for each district
		
		Maps district partition map @M to a 2d-list @N of rgb values, 
		for writing to png. Thus, N[row][col] = colors[d] if M[row][col]
		lies in district d. 
		
	'''
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
	'''
		@M: a district partition map
		@P: a mapping of rgb values from @M (see comments in function 
			partitions_to_rgb(M) above)
		
		Writes @P to a png file in the local directory with name @fname.
	'''	
	P = partitions_to_rgb(M)
	f = open(fname, 'wb')
	w = png.Writer(512,512)
	w.write(f, P)
	f.close()
	
def results_to_rgb(M):
	'''
		Maps election results map @M to a 2d-list @N of rgb values, 
		for writing to png. Thus, N[row][col] = 'blue' if (row,col) 
		lies in a district which voted blue in M, and 'red' otherwise.
	
	'''
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
	'''
		@M: an election results map
		@R: a mapping of rgb values from @M (see comments in function 
			results_to_rgb(M) above)
		
		Writes @R to a png file in the local directory with name @fname.
	'''
	R = results_to_rgb(M)
	f = open(fname, 'wb')
	w = png.Writer(512,512)
	w.write(f, R)
	f.close()