import unittest
from GraphAlgo import GraphAlgo
from DiGraph import DiGraph

class TestDiGraph(unittest.TestCase):
    def __init__(self, methodName: str) -> None:
        super().__init__(methodName=methodName)
        g = GraphAlgo()
        g.load_from_json("./data/A0.json")
        self.graph = g.g

    def test_v_size(self):
        self.assertEquals(11,self.graph.v_size())
    def test_e_size(self):
        s = 0
        for i in self.graph.nodesdict:
            s+=len(self.graph.nodesdict[i].out_neighbors)
        self.assertEquals(s,self.graph.e_size())
        




if __name__ == "__main__":
    unittest.main()