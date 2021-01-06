from GraphAlgoInterface import GraphAlgoInterface
from DiGraph import DiGraph
from NodeData import NodeData
import numpy as np
import matplotlib.pyplot as plt
import json
import queue
import random

class GraphAlgo(GraphAlgoInterface, DiGraph):

    def __init__(self, g : DiGraph = None):
        if g :
            self.g = g
        else:
            self.g = DiGraph()

    def get_graph(self):
        '''
        returning the graph
        :return:
        '''
        return self.g

    def load_from_json(self, file_name: str):
        '''
        loading a json file
        :param file_name:
        :return:
        '''
        with open(file_name, 'r') as f:
            data = json.load(f)
            for i in data["Nodes"]:
                self.g.add_node(i["id"])
                if "pos" in i: # if we have position then save them
                    self.g.posdict[i["id"]] = i["pos"]
            for i in data["Edges"]:
                self.g.add_edge(i["src"], i["dest"], i["w"])
            return True
        return False

    def save_to_json(self, file_name: str):
        '''
        saving a file to json format
        :param file_name:
        :return:
        '''
        edges = []
        nodes = []
        with open(file_name, 'w') as json_file:
            data = {"Nodes" : "" , "Edges" : ""}
            for key in self.g.nodesdict:
                for neighbors in self.g.nodesdict[key].out_neighbors:
                    edges.append({"src" : key, "dest" : neighbors, "w" : self.g.nodesdict[key].out_neighbors[neighbors]})
            for key in self.g.nodesdict:
                if self.g.posdict == {}: # if there is no positions
                    nodes.append({"id": key})
                else: # if the nodes have positions
                    nodes.append({"id" : key, "pos" : self.g.posdict[key]})
            data["Nodes"] = nodes
            data["Edges"] = edges
            json.dump(data, json_file)
            return True
        return False

    def shortest_path(self, id1: int, id2: int):
        '''
        returning the shortest distance between id1 and id2 and the path as a list
        :param id1:
        :param id2:
        :return:
        '''
        distance = float("inf") # distance between 2 nodes
        distance_start: float # distance of every node from the start
        distance_list = [] # the list of ints that will return in the end
        nodes_queue = queue.Queue() # queue that will go through all the nodes in the graph
        distance_dic = {} # represents the distance from start to specific node
        distance_dic[id2] = -1 # if it will not change that means that there is no way from id1 to id2
        list_dic = {} # represents the list to every node from start

        if self.g.v_size() == 0 and self.g.e_size() == 0: # if the graph is empty
            return distance, distance_list
        if id1 not in self.g.nodesdict or id2 not in self.g.nodesdict: # if id1 or id2 dont exist
            return distance, distance_list
        if id1 == id2: # if id1 and id2 is the same node
            return distance, distance_list

        for i in self.g.nodesdict: # setting all the distances to the start to be -1 so you can know if you already visited this node or not
            distance_dic[i] = -1

        distance_dic[id1] = 0
        list_dic[id1] = [id1]
        nodes_queue.put(id1)

        while not nodes_queue.empty():
            n = nodes_queue.get()

            for i in self.g.nodesdict[n].out_neighbors: # going through the neighbors of n
                distance = self.g.nodesdict[n].out_neighbors[i] # getting the distance between n and i
                distance_start = distance_dic[n] + distance # getting the distance from id1 to i

                if distance_dic[i] == -1 or distance_dic[i] > distance_start:
                    distance_dic[i] = distance_start # putting the better distance from the start
                    list_dic[i] = list_dic[n] # adding the neighbor to the list
                    list_dic[i].append(i)
                    nodes_queue.put(i) # placing the neighbor to the queue

        if distance_dic[id2] == -1: # that means there is no path between id1 to id2
            distance = float("inf")
            return distance, distance_list

        # if we came to here that means that everything is fine and we have distance between id1 and id2
        distance_list.append(list_dic[id2]) # set the list that we want to return as the list that going from id1 to id2
        return distance_dic[id2], list_dic[id2]

    def connected_component(self, id1: int):
        list_to_return = [id1] # place the given node to list
        for i in self.g.nodesdict: # check if there is a path from id to i and back
            x = self.shortest_path(i,id1)
            y = self.shortest_path(id1, i)
            if not (x[0] == float("inf")) and not (y[0] == float("inf")) and i not in list_to_return: # if we have path from id to i and back and i isnt already in the list
                list_to_return.append(i) # place in the list
        return list_to_return

    def connected_components(self):
        list_to_return = []
        bol = True
        list_to_return.append(self.connected_component(0))
        for key in self.g.nodesdict:
            for j in list_to_return:
                if self.connected_component(key)[0] not in j:
                    bol = True
                else:
                    bol = False
            if bol:
                list_to_return.append(self.connected_component(key))
        return list_to_return

    def plot_graph(self):
        x_vals = [] # array with x coordinates
        y_vals = [] # array with y coordinates
        if self.g.posdict == {}: # if we dont get positions for nodes
            for i in self.g.nodesdict: # go through all the nodes and make them positions
                x_ran = random.uniform(35,36) # make a random x coordinate
                while not self.checkGoodRandomSpawning(self.g.posdict, x_ran, True): # if its too close to another one make a new coordinate
                    x_ran = random.uniform(35, 36)
                y_ran = random.uniform(32,33) # make a random y coordinate
                while not self.checkGoodRandomSpawning(self.g.posdict, y_ran, False): # if its too close to another one make a new coordinate
                    y_ran = random.uniform(35, 36)
                str_pos = str(x_ran) + "," + str(y_ran) # string of the randomed positions
                self.g.posdict[i] = str_pos # saving the position in dictionary of positions
                x_vals.append(x_ran) # place in the array
                y_vals.append(y_ran) # place in the array
        else: # if we get positions for nodes
            for i in self.g.posdict: # go through all the positions given
                first_stop = self.g.posdict[i].find(",") # find the length of the first coordinate
                second_stop = self.g.posdict[i].find(",", first_stop + 1)  # find the length of the second coordinate
                x_string = self.g.posdict[i][0 : first_stop] # save the substring that we need from a big string
                y_string = self.g.posdict[i][first_stop + 1: second_stop] # save the substring that we need from a big string
                x_vals.append(float(x_string)) # convert to float
                y_vals.append(float(y_string)) # convert to float
        plt.plot(x_vals,y_vals, "ro") # draw the nodes
        for i in self.g.nodesdict: # draw the arrows
            for x in self.g.nodesdict[i].out_neighbors:
                plt.arrow( x_vals[i], y_vals[i], x_vals[x] - x_vals[i], y_vals[x] - y_vals[i], head_width = 0.0002, width = 0.000005, length_includes_head = True)
        plt.show()

    def checkGoodRandomSpawning(self, dic: dict, pos: float, bol: bool):
        '''
        making shure that the random position that we got dont too close to another node that we already have or that they dont stay at the same place
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


