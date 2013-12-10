import copy

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
      dis = int(N[row][col])
      pop[dis] += sum(M[row][col])
  return pop

def is_vary_beyond_range(pop, percentage):
  max_value = max(pop)
  min_value = min(pop)
  per = float(max_value)/float(min_value)
  return (per - 1) > percentage

def get_election_prop(M, N, k):
  width = len(N)
  red = [0] * k
  blue = [0] * k
  for row in range(width-1):
    for col in range(width-1):
      dis = int(N[row][col])
      red[dis] += M[row][col][0]
      blue[dis] += M[row][col][1]
  prop = [0]*k;
  for i in range(0, k -1):
    if(red[dis] < blue[dis]):
      prop[i] = 0
    else:
      prop[i] = 1
  return prop

def get_election_result(N, prop):
  width = len(N)
  # D = copy.deepcopy(N)
  for row in range(width-1):
    for col in range(width-1):
      dis = int(N[row][col])
      N[row][col] = prop[dis]
  return N

def write_solution(D):
  out = open('result_map.txt','w')
  width = len(D)
  for row in range(width-1):
    for col in range(width-1):
      out.write(str(D[row][col]) + ' ')
    out.write(str(D[row][-1]) + '\n')
  for col in range(width-1):
    out.write(str(D[-1][col]) + ' ')
  out.write(str(D[-1][-1]) + '\n')
  out.close( )


M = read_input_map()
N = read_input_solution()
print len(M)
pop = get_population(M, N, 5)
print len(pop)
for p in pop:
  print p
print is_vary_beyond_range(pop, 0.02)

prop = get_election_prop(M, N, 5)
D = get_election_result(N, prop)
write_solution(D)
