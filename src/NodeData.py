class NodeData:

    def __init__(self, key = 0, tag = 0):
        self.key = key
        self.tag = tag
        self.in_neighbors = {}
        self.out_neighbors = {} # dictionary of int(id) and float(weight)

    def getKey(self):
        return self.key

    def getInNeighbors(self,id: int, dic: dict): # to node from neighbors
        '''
        checking if the given node exist in out_neighbors dictionary of every element in the given dictionary
        :param id:
        :param dic:
        :return:
        '''
        for i in dic:
            for x in dic[i].out_neighbors:
                if id == x:
                    self.in_neighbors[i] = dic[i].out_neighbors[x]
        return self.in_neighbors

    def setOutNeighbors(self,id: int, weight: float): # from node to neighbors
        '''
        adding a neighbor to out_neighbors dictionary
        :param id:
        :param weight:
        :return:
        '''
        self.out_neighbors[id] = weight

    def clearOutNeighbors(self):
        '''
        clearing out_neighbors dictionary
        :return:
        '''
        self.out_neighbors.clear()
