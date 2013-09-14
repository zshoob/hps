import time
import itertools
import random
	
def exactCostTable(denoms):
	table = [[100] for x in range(100)]
	for d in denoms:
		table[d] = [d]
	black = set(denoms)
	grey = set(denoms)
	while len(black) < 99:
		nextGrey = set( )	
		for pair in itertools.product(black,grey):
			pCost = sum(table[pair[0]]) + sum(table[pair[1]])
			if sum(pair) < 100 and pCost < sum(table[sum(pair)]):
				nextGrey.add(sum(pair))
				table[sum(pair)] = table[pair[0]] + table[pair[1]]
		black |= grey
		grey = nextGrey
	return table
		
def exchangeCostTable(exactCostTable):
	table = exactCostTable + [[100]]
	black = [len(entry) for entry in table]
	black[0] = 100	
	for loop in range(100):
		u = black.index(min(black))
		for v in range(1,u):
			w = u - v
			if len(table[u]) + len(table[w]) < len(table[v]):
				table[v] = table[u] + [-c for c in table[w]]
		black[u] = 100
	return table[:-1]
