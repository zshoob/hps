import matplotlib as mpl
from mpl_toolkits.mplot3d import Axes3D
import numpy as np
import matplotlib.pyplot as plt

nodemap = {}

with open('testgraph.txt','r') as ipt:
	g = ipt.read( ).split('\n')
	
fig = plt.figure()
ax = fig.gca()	
ax.plot([-1,-1],[-1,-1])
for line in g:
	l = line.split(',')
	if( len(l) == 3 ):
		nodemap[l[0]] = [int(l[1]),int(l[2])]
	else:
		a = nodemap[l[0]]
		b = nodemap[l[1]]
		ax.plot([a[0],b[0]],[a[1],b[1]])
	
plt.show( )