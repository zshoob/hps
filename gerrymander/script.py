import generate_map as gm
import random_player as rp
import visualizer as vs
import check_contiguous as cc
import get_results as gr
import evaluate as ev
import rgb
import png

k = 5
M = gm.generate_map( )
gm.write_map(M)
#rgb.write_pop(M,'pop.png')
P = rp.generate_solution(M,k)
rp.write_solution(P)
#rgb.write_partitions(P,'partitions.png')
t = ev.evaluate('random_player',k)
print t
		
'''
rgb.write_partitions(P,'partitions.png')
R = gr.get_results(P,M,k)
rgb.write_results(R,'results.png')
'''