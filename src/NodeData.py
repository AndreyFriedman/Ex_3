class NodeData:

    def __init__(self, key = 0, tag = 0):
        self.key = key # store key 
        self.tag = tag # store tag
        self.in_neighbors = {} # dictionary of int(id) and float(weight)
        self.out_neighbors = {} # dictionary of int(id) and float(weight)

    def getKey(self):
        return self.key # get the key back 

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
    def __str__(self) -> str:
        return f"[{self.key}]"
    def __repr__(self) -> str:
        return self.__str__()
    def __eq__(self, o: object) -> bool:
        if(isinstance(o,NodeData)):
            if(o.key == self.key):
                for i in o.in_neighbors:
                    if self.in_neighbors.get(i) != o.in_neighbors[i]:
                        return False
                for i in self.in_neighbors:
                    if o.in_neighbors.get(i)!= self.in_neighbors[i]:
                        return False
                for i in self.out_neighbors:
                    if o.out_neighbors.get(i)!= self.out_neighbors[i]:
                        return False             
                for i in o.out_neighbors:
                    if self.out_neighbors.get(i)!= o.out_neighbors[i]:
                        return False
                return True
            else:
                return False
        else:
            return False