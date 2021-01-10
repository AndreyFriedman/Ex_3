from typing import Callable
import networkx.algorithms.components as a
from networkx.classes import graph
from DiGraph import DiGraph
from GraphAlgo import GraphAlgo
from os import listdir
from time import time 
import networkx as nx


def timefunc(func:Callable,*args)->float:
    start = time()
    func(*args)
    return time() - start

l = listdir("./Graph_no_pos")

ga = GraphAlgo()
times = {}
for file in l:
    ga.load_from_json("./Graph_no_pos/"+file)
    g = nx.DiGraph()
    for i in ga.graph.nodesdict:
        g.add_node(i)
    for i in ga.graph.nodesdict:
        for j in ga.graph.nodesdict[i].out_neighbors:
            g.add_edge(i,j,weight=ga.graph.nodesdict[i].out_neighbors[j])
    s = time()
    # try:
    #     print(nx.dijkstra_path(g,1,10))
    # except nx.NetworkXNoPath as e:
    #     print("no path",e)
    #
    #ga.shortest_path(1,10)
    # ga.connected_components()
    t = a.strongly_connected.strongly_connected_components(g)
    times[file] = time()-s
for i in times:
    print(i,times[i])









