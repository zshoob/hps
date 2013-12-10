import generate_map as gm
import random_player as rp
import visualizer as vs
import check_contiguous as cc

k = 10
M = gm.generate_map( )
gm.write_map(M)
D = rp.generate_solution(M,k)
#vs.draw_partitions(D)
vs.draw_population(M)
#print cc.check_contiguous(D,k)