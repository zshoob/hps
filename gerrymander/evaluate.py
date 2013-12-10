import rgb
import math, random, sys

def distance(a,b):
	return math.sqrt(math.pow(a[0]-b[0],2) + math.pow(a[1]-b[1],2))	

def read_P(fname):
	P = []
	lines = []
	with open(fname,'r') as ipt:
		lines = ipt.read( ).split('\n')[:-1]
	for line in lines:
		if len(line) > 0:
			P.append([int(l) for l in line.split(' ')])
	return P
	
def read_M(fname):
	lines = []
	with open(fname,'r') as ipt:
		lines = ipt.read( ).split('\n')[:-1]
	M = []
	for line in lines:
		row = [[int(col.split(',')[0]),int(col.split(',')[1])] for col in line.split(' ')]
		M.append(row)
	return M

def row_edges(P,row,d):
	edges = []
	for col in range(len(P[row])):
		if P[row][col] == d:
			if col == 0 or P[row][col-1] != d:
				edges.append([row,col])				
			elif col == len(P[row])-1 or P[row][col+1] != d:
				edges.append([row,col])	
	return edges			

def insert_into_edge_list(edges,prevrow,nextrow,P):
	r,c = edges[0]
	d = P[r][c]
	for edge in nextrow:
		row1,col1 = edge 
		mindist = float("inf")
		minpoint = None
		for point in prevrow:
			dist = distance(edge,point)
			if dist < mindist:
				mindist = dist
				minpoint = point
		row2,col2 = minpoint
		if col1 < col2:
			edges.insert(edges.index(minpoint),edge)
		elif col1 > col2:
			edges.insert(edges.index(minpoint)+1,edge)			
		else:
			if col1 == 0 or P[row1][col1-1] != d:
				edges.insert(edges.index(minpoint),edge)
			else:
				edges.insert(edges.index(minpoint)+1,edge)							

def get_edges(P,k):
	edges = [[] for d in range(k)]
	mapsize = len(P)
	for d in range(k):
		d_edges = []
		prevrow = []
		for row in range(mapsize):
			r_edges = row_edges(P,row,d)
			if len(r_edges) > 0:
				if len(d_edges) > 0:
					insert_into_edge_list(d_edges,prevrow,r_edges,P)
				else:
					d_edges = r_edges
				prevrow = r_edges
		#edges[d] = d_edges
		edges[d] = [dim for point in d_edges for dim in point]
	return edges
	
def population_per_district(P,M,k):
	msize = len(M)
	count = [[0,0] for d in range(k)]
	for row in range(msize):
		for col in range(msize):
			d = P[row][col]
			count[d][0] += M[row][col][0]
			count[d][1] += M[row][col][1]	
	return count
	
def get_election_results(P,M,k):
	count = population_per_district(P,M,k)
	results = [0 for d in range(k)]
	for d in range(k):
		num_blue = float(count[d][0])
		num_red = float(count[d][1])
		prob_blue = num_blue / (num_red+num_blue)
		
		if random.random( ) < prob_blue:
			results[d] = 0
		else:
			results[d] = 1
	return results
	
def evaluate(player_name,k):
	P = read_P(player_name + '_solution.txt')
	M = read_M('population_map.txt')
	
	edges = get_edges(P,k)
	results = get_election_results(P,M,k)
	pop_counts = population_per_district(P,M,k)
	
	num_blue = results.count(0)
	num_red = k - num_blue
	
	out = open(player_name + '_results.txt','w')
	out.write(str(num_blue) + '\n')
	out.write(str(num_red) + '\n')	
	for d in range(k):
		out.write('{\n')
		out.write(str(pop_counts[d]) + '\n')
		out.write(str(edges[d]) + '\n')
		out.write('}\n')		
	out.close	
	
	msize = len(M)
	R = [[0 for col in range(msize)] for row in range(msize)]
	for row in range(msize):
		for col in range(msize):
			d = P[row][col]
			R[row][col] = results[d]
			
	rgb.write_results(R,player_name + '_results.png')	
	rgb.write_partitions(P,player_name + '_solution.png')
	rgb.write_pop(M,'population_map.png')
	
def main(args):
	player_name = args[0]
	k = int(args[1])
	evaluate(player_name,k)
	
if __name__ == "__main__":
	main(sys.argv[1:])	