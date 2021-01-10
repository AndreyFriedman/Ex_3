from typing import List
from GraphAlgoInterface import GraphAlgoInterface
from DiGraph import DiGraph
from NodeData import NodeData
import matplotlib.pyplot as plt
import json
import random
from copy import deepcopy

class GraphAlgo(GraphAlgoInterface, DiGraph):

    def __init__(self, g : DiGraph = None):
        self.graph = g
        self.gT = None

    def get_graph(self):
        '''
        returning the graph
        :return:
        '''
        return self.graph

    def load_from_json(self, file_name: str):
        '''
        loading a json file
        :param file_name:
        :return:
        '''
        self.graph = DiGraph() # create a graph 
        with open(file_name, 'r') as f: # open file 
            data = json.load(f) # load dict form the file json text
            for i in data["Nodes"]: # go through Nodes dict 
                self.graph.add_node(i["id"]) # add node the graph
                if "pos" in i: # if we have position then save them
                    self.graph.posdict[i["id"]] = i["pos"] # place the position from the dict in the graph
            for i in data["Edges"]: # go through edges 
                self.graph.add_edge(i["src"], i["dest"], i["w"]) # connect the edges with weight 
            return True # return true since success
        

    def save_to_json(self, file_name: str):
        '''
        saving a file to json format
        :param file_name:
        :return:
        '''
        edges = [] # init the edges and nodes 
        nodes = []
        with open(file_name, 'w') as json_file: # open file for writing 
            data = {"Nodes" : "" , "Edges" : ""} # create the dict to dump to the file 
            for key in self.graph.nodesdict: # go through nodes 
                for neighbors in self.graph.nodesdict[key].out_neighbors: # go through outedges from node 
                    # add the edges list the edge dict 
                    edges.append({"src" : key, "dest" : neighbors, "w" : self.graph.nodesdict[key].out_neighbors[neighbors]})
            for key in self.graph.nodesdict: # go through nodes 
                if self.graph.posdict == {}: # if there is no positions
                    nodes.append({"id": key})
                else: # if the nodes have positions
                    nodes.append({"id" : key, "pos" : self.graph.posdict[key]}) # add node dict to list 
            data["Nodes"] = nodes # put both edges list and nodes list in data dict 
            data["Edges"] = edges
            json.dump(data, json_file) # dump the dict to the fle 
            return True 

    def shortest_path(self, id1: int, id2: int)->tuple:
        '''
        returning the shortest distance between id1 and id2 and the path as a list
        :param id1:
        :param id2:
        :return:
        '''
        if not self.graph:
            return float('inf'),[]
        if id1 in self.graph.nodesdict and id2 in self.graph.nodesdict:
            src = self.graph.nodesdict[id1]
            dest = self.graph.nodesdict[id2]
            parent = {} # parent dictionary 
            stack = [src] # init the stack with the src
            for key in self.graph.nodesdict: # iterate over nodes and set weights to -1
                self.graph.nodesdict[key].tag = -1
            src.tag = 0 # set src weight to 0
            while(len(stack)!=0): # as long as the stack isn't empty 
                node = stack.pop() # pop a node out of the stack 
                node:NodeData
                for ni_key in node.out_neighbors: # iterate over the edges that go out of the node
                    if self.graph.nodesdict[ni_key].tag == -1: #node.neighbors[ni_key].weight == -1: #means we never reached it
                        self.graph.nodesdict[ni_key].tag = node.out_neighbors[ni_key]+node.tag
                        stack.append(self.graph.nodesdict[ni_key])
                        stack.sort(key=lambda x: x.tag,reverse=True)
                        parent[ni_key] = node.key
                    elif self.graph.nodesdict[ni_key].tag > node.out_neighbors[ni_key]+node.tag:   
                        self.graph.nodesdict[ni_key].tag = node.out_neighbors[ni_key]+node.tag
                        stack.sort(key=lambda x: x.tag,reverse=True)
                        parent[ni_key] = node.key
            # once finished the algorithm 
            # should check if we reached the dest 
            # check if dest is in parent dict
            if parent.get(dest.key) == None: # means we never reached it 
                #thus return None
                return float("inf"),[]
            else: # means we reached it so we should trace the path back 
                n = dest.key
                l = []
                while(parent[n]!=src.key):
                    l.append(n)
                    n = parent[n]
                l.append(n)
                l.append(parent[n])
                l.reverse()
                return (dest.tag,l)

    def transpose(self)->DiGraph:
        """
        This function is taking the graph and returning the tramspose graph.
        @return the transpose graph.
        """
        if(self.graph and isinstance(self.graph,DiGraph)): # check if graph is not none 
            g = DiGraph() # create a new graph 
            for node in self.graph.nodesdict.values(): # go through all nodes and add them to graph 
                g.add_node(node.key)
            for node in self.graph.nodesdict.values(): # go through all edges and connect nodes acorrding to them 
                for e in node.in_neighbors:
                    g.add_edge(node.key,e,node.in_neighbors[e]) # create the edge 
            return g # return the transpose 
        else:
            return None # return None since there is no graph 
    
    def connected_component(self, id1: int):
        """
        Determine the scc of a certain node
        @return list of the keys of this scc 
        """
        def dfs(v:int,graph:DiGraph)->None:
            """
            DFS algorithm implementation inside the scc function since it's used only here.
            @return none since it's an algorithm
            """
            s = [v]
            while len(s)>0:
                node = s.pop()
                if(graph.nodesdict[node].tag == 0):
                    graph.nodesdict[node].tag = 1
                    for n in graph.nodesdict[node].out_neighbors:
                        s.append(n)
        #######################################
        if(self.graph and isinstance(self.graph,DiGraph)): # if there is a graph and not none 
            if id1 not in self.graph.nodesdict:# if the node is not in the graph 
                return [] # empty list to know something is wrong 
            for node in self.graph.nodesdict.values(): # go over nodes 
                node.tag = 0 # put tags as 0
            dfs(id1,self.graph) # do dfs algo on the graph 
            # gather nodes that had been reached by the dfs traversal into nodes1
            nodes1 = [node.key for node in self.graph.nodesdict.values() if node.tag ==1]
            t = self.transpose() # create the tranpose graph 
            for node in t.nodesdict.values(): # go through the nodes 
                node.tag = 0 # put tags as 0 
            dfs(id1,t) # do dfs alogo on t 
            # gather all nodes that were reached in dfs 
            nodes2 = [node.key for node in t.nodesdict.values() if node.tag == 1]
            # return the intersection between the two 
            return [node for node in nodes1 if node in nodes2]
        else:
            return [] # return empty list 


    def connected_components(self)->List:
        """
        @return list of all connected components in the graph 
        """
        if(self.graph and isinstance(self.graph,DiGraph)):
            sccs = [] # list to return 
            disc = [] # discovered nodes 
            for node in self.graph.nodesdict.values(): # go through nodes 
                if(node.key not in disc): # if node not discovred yet 
                    l = self.connected_component(node.key) # find it's scc 
                    disc += l # add all this nodes to discovred ones 
                    sccs.append(l) # and add scc to sccs 
            return sccs # return all thr scc's 
        else:
            return None # since there is no graph not to confuse with the empty graph 

    
        

    def plot_graph(self):
        x_vals = [] # array with x coordinates
        y_vals = [] # array with y coordinates
        if self.graph.posdict == {}: # if we dont get positions for nodes
            for i in self.graph.nodesdict: # go through all the nodes and make them positions
                x_ran = random.uniform(35,36) # make a random x coordinate
                while not self.checkGoodRandomSpawning(self.graph.posdict, x_ran, True): # if its too close to another one make a new coordinate
                    x_ran = random.uniform(35, 36)
                y_ran = random.uniform(32,33) # make a random y coordinate
                while not self.checkGoodRandomSpawning(self.graph.posdict, y_ran, False): # if its too close to another one make a new coordinate
                    y_ran = random.uniform(32, 33)
                str_pos = str(x_ran) + "," + str(y_ran) # string of the randomed positions
                self.graph.posdict[i] = str_pos # saving the position in dictionary of positions
                x_vals.append(x_ran) # place in the array
                y_vals.append(y_ran) # place in the array
        else: # if we get positions for nodes
            for i in self.graph.posdict: # go through all the positions given
                first_stop = self.graph.posdict[i].find(",") # find the length of the first coordinate
                second_stop = self.graph.posdict[i].find(",", first_stop + 1)  # find the length of the second coordinate
                x_string = self.graph.posdict[i][0 : first_stop] # save the substring that we need from a big string
                y_string = self.graph.posdict[i][first_stop + 1: second_stop] # save the substring that we need from a big string
                x_vals.append(float(x_string)) # convert to float
                y_vals.append(float(y_string)) # convert to float
        plt.plot(x_vals,y_vals, "ro") # draw the nodes
        for i in self.graph.nodesdict: # draw the arrows
            for x in self.graph.nodesdict[i].out_neighbors:
                plt.arrow( x_vals[i], y_vals[i], x_vals[x] - x_vals[i], y_vals[x] - y_vals[i], head_width = 0.0002, width = 0.000005, length_includes_head = True)
        plt.show()

    def checkGoodRandomSpawning(self, dic: dict, pos: float, bol: bool):
        '''
        making sure that the random position that we got dont too close to another node that we already have or that they dont stay at the same place
        :param dic:
        :param pos:
        :return:
        '''
        if bol: # checking the x coordinates
            for i in dic:
                stop = dic[i].find(",")
                str_x = dic[i][0:stop]
                if float(str_x) - pos < 0.005 and float(str_x) - pos > -0.005:
                    return False
        else: # checking the y coordinates
            for i in dic:
                stop = dic[i].find(",")
                str_y = dic[i][stop + 1:]
                if float(str_y) - pos < 0.005 and float(str_y) - pos > -0.005:
                    return False
        return True


