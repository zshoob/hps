import generate_map as gm
import random_player as rp
import visualizer as vs
import check_contiguous as cc
import rgb
import png

k = 5
M = gm.generate_map( )
gm.write_map(M)
rgb.write_pop(M,'pop.png')


'''
p = [[255,0,0, 0,255,0, 0,0,255],
     [128,0,0, 0,128,0, 0,0,128]]
f = open('swatch.png', 'wb')
w = png.Writer(3, 2)
w.write(f, p) ; f.close()
'''

#D = rp.generate_solution(M,k)
#vs.draw_partitions(D)
#rgb.write_partition(D,'partitions.txt')
