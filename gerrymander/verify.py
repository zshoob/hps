

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


M = read_input_map()
N = read_input_solution()
print len(M)
pop = get_population(M, N, 5)
print len(pop)
for p in pop:
  print p
print is_vary_beyond_range(pop, 0.02)