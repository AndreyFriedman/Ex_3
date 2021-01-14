from GraphInterface import GraphInterface
from NodeData import NodeData


class DiGraph(GraphInterface):

    def __init__(self):
        self.nodes = 0 # number of nodes in the graph
        self.edges = 0 # number of edges in the graph
        self.MC = 0 # number of made acctions
        self.nodesdict = {} # dictionary of the nodes in the graph
        self.posdict = {} # the position of every node in the graph


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
        self.nodesdict[id1].out_neighbors[id2] =  weight
        self.nodesdict[id2].in_neighbors[id1] = weight
        self.MC = self.MC + 1
        self.edges = self.edges + 1
        return True

    def add_node(self, node_id: int, pos: tuple = None):
        if node_id in self.nodesdict:
            return False
        self.nodesdict[node_id] = NodeData(node_id)
        self.MC = self.MC + 1
        self.nodes = self.nodes + 1
        return True

    def remove_node(self, node_id: int):
        if node_id not in self.nodesdict:
            return False
        for i in self.nodesdict:
            if node_id in self.nodesdict[i].out_neighbors:
                del(self.nodesdict[i].out_neighbors[node_id])
                self.edges = self.edges - 1
        for i in self.nodesdict[node_id].in_neighbors:
            del self.nodesdict[i].in_neighbors[node_id]
            self.edges-=1
        self.nodes = self.nodes - 1
        del self.nodesdict[node_id]
        self.MC+=1
        return True

    def remove_edge(self, node_id1: int, node_id2: int):
        if node_id1 not in self.nodesdict or node_id2 not in self.nodesdict:
            return False
        node1 = self.nodesdict[node_id1]
        node2 = self.nodesdict[node_id2]
        if node_id2 in node1.out_neighbors:
            del node1.out_neighbors[node_id2]
        else:
            return False
        if node_id1 in node2.in_neighbors:
            del node2.in_neighbors[node_id1]
        else:
            return False
        self.MC = self.MC + 1
        self.edges = self.edges - 1
        return True
    def __eq__(self, o: object) -> bool:
        if(isinstance(o,DiGraph)):
            for i in o.nodesdict:
                node = o.nodesdict[i]
                if isinstance(node,NodeData):
                    if (node != self.nodesdict.get(node.key)):
                        return False
                else:
                    return False
            for i in self.nodesdict:
                node = self.nodesdict[i]
                if isinstance(node,NodeData):
                    if (node != o.nodesdict.get(node.key)):
                        return False
                else:
                    return False
            return True
        else:
            return False
