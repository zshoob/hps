import generate_map as gm
import random_player as rp
import visualizer as vs

k = 5
M = gm.generate_map( )
D = rp.generate_solution(M,k)
vs.draw_partitions(D)
vs.draw_population(M)