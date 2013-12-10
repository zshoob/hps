

def read_input_solution():
  lines = []
  with open('solution.txt', 'r') as ipt:
    lines = ipt.read().split('\n')[:-1]
  M = []
  for line in lines:
    row = line.split(' ')
    M.append(row)
  return M

def read_input_map( ):
  lines = []
  with open('input_map.txt','r') as ipt:
    lines = ipt.read( ).split('\n')[:-1]
  M = []
  for line in lines:
    row = [[int(col.split(',')[0]),int(col.split(',')[1])] for col in line.split(' ')]
    M.append(row)
  return M
# def is_valid(M):



def get_population(M, N, k):
  width = len(N)
  pop = [0]*k
  for row in range(width-1):
    for col in range(width-1):
      dis = int(N[col][row])
      pop[dis] += sum(M[col][row])
  return pop

def is_vary_beyond_range(pop, percentage):
  max_value = max(pop)
  min_value = min(pop)
  per = float(max_value)/float(min_value)
  return (per - 1) > percentage

def get_election_result(M, N, k):
  width = len(N)
  red = [0] * k
  blue = [0] * k
  for row in range(width-1):
    for col in range(width-1):
      dis = int(N[row][col])
      red[dis] += M[row][col][0]
      blue[dis] += M[row][col][1]

  ret = [0]*k;
  for i in range(0, k -1):
    if(red[dis] < blue[dis]):
      ret[i] = 0
    else:
      ret[i] = 1
  return ret


M = read_input_map()
N = read_input_solution()
print len(M)
pop = get_population(M, N, 5)
print len(pop)
for p in pop:
  print p
print is_vary_beyond_range(pop, 0.02)

ret = get_election_result(M, N, 5)
for r in ret:
  print r
