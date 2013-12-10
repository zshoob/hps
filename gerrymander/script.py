import generate_map as gm
import random_player as rp
import visualizer as vs
import check_contiguous as cc
import rgb

k = 5
M = gm.generate_map( )
gm.write_map(M)
#D = rp.generate_solution(M,k)
#vs.draw_partitions(D)
#vs.draw_population(M)
#rgb.write_pop(M)