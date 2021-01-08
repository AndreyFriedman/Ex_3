from GraphInterface import GraphInterface
from NodeData import NodeData


class DiGraph(GraphInterface):

    def __init__(self, nodes = 0, edges = 0, nodesdict = {}, posdict = {}):
        self.nodes = nodes
        self.edges = edges
        self.MC = 0
        self.nodesdict = nodesdict
        self.posdict = posdict


    def v_size(self):
        return self.nodes

    def e_size(self):
        return self.edges

    def get_all_v(self):
        return self.nodesdict

    def all_in_edges_of_node(self, id1: int):
        return self.nodesdict[id1].getInNeighbors(id1, self.nodesdict)

    def all_out_edges_of_node(self, id1: int):
        return self.nodesdict[id1].out_neighbors

    def get_mc(self):
        return self.MC

    def add_edge(self, id1: int, id2: int, weight: float):
        if id1 == id2 or weight == 0:
            return False
        self.nodesdict[id1].setOutNeighbors(id2, weight)
        self.MC = self.MC + 1
        self.edges = self.edges + 1
        return True

    def add_node(self, node_id: int, pos: tuple = None):
        for i in self.nodesdict:
            if i == node_id:
                return False
        self.nodesdict[node_id] = NodeData(node_id)
        self.MC = self.MC + 1
        self.nodes = self.nodes + 1
        return True

    def remove_node(self, node_id: int):
        if node_id not in self.nodesdict:
            return False

        self.nodesdict[node_id].clearOutNeighbors()
        for i in self.nodesdict:
            if node_id in self.nodesdict[i].out_neighbors:
                del(self.nodesdict[i].out_neighbors[node_id])
                self.MC = self.MC + 1
                self.edges = self.edges - 1
        self.nodes = self.nodes - 1
        return True

    def remove_edge(self, node_id1: int, node_id2: int):
        for neigh in self.nodesdict[node_id1].out_neighbors:
            if neigh == node_id2:
                del(self.nodesdict[node_id1].out_neighbors[neigh])
                self.MC = self.MC + 1
                self.edges = self.edges - 1
                return True
        return False
