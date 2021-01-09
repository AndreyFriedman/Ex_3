import unittest
import sys 
sys.path.append("../")
print(sys.path)
from src.GraphAlgo import GraphAlgo
from src.DiGraph import DiGraph
from src.NodeData import NodeData

def getgraph():
    g = GraphAlgo()
    g.load_from_json("./data/A0.json")
    return g.get_graph()

class TestDiGraph(unittest.TestCase):
    def __init__(self, methodName: str) -> None:
        super().__init__(methodName=methodName)

    def test_v_size(self):
        graph = getgraph()
        self.assertEqual(11,graph.v_size())
        
    def test_e_size(self):
        graph = getgraph()
        s = 0
        for i in graph.nodesdict:
            s+=len(graph.nodesdict[i].out_neighbors)
        self.assertEqual(s,graph.e_size())
            
    def test_get_all_v(self):
        g = DiGraph()
        g.add_node(1)
        g.add_node(2)
        print()
        l = {1:NodeData(1),2:NodeData(2)}
        self.assertEqual(g.get_all_v(),l)
    def test_all_in_edges_of_node(self):
        g = DiGraph()
        g.add_node(1)
        g.add_node(2)
        g.add_edge(1,2,5)
        self.assertEqual(g.all_in_edges_of_node(2),{1:5})
    def test_all_out_edges_of_node(self):
        g = DiGraph()
        g.add_node(1)
        g.add_node(2)
        g.add_edge(1,2,5)
        self.assertEqual(g.all_out_edges_of_node(1),{2:5})
    def test_get_mc(self):
        g = getgraph()
        self.assertEqual(g.edges+g.nodes,g.get_mc())
    def test_add_edge(self):
        g = getgraph()
        mc  = g.get_mc()
        edges = g.edges
        g.add_edge(1,5,3)
        self.assertEqual(mc+1,g.get_mc())
        self.assertEqual(edges+1,g.edges)
        if( g.nodesdict[1].out_neighbors.get(5)!=3):
            raise AssertionError
        if(g.nodesdict[5].in_neighbors.get(1)!=3):
            raise AssertionError
    def test_add_node(self):
        g = getgraph()
        mc  = g.get_mc()
        nodes = g.v_size()
        g.add_node(15)
        self.assertEqual(mc+1,g.get_mc())
        self.assertEqual(nodes+1,g.v_size())
        g.add_node(1)
        self.assertEqual(mc+1,g.get_mc())
        self.assertEqual(nodes+1,g.v_size())
    
    def test_remove_node(self):
        g = getgraph()
        mc  = g.get_mc()
        nodes = g.v_size()
        g.remove_node(15)       
        self.assertEqual(mc,g.get_mc())
        self.assertEqual(nodes,g.v_size())
        g.remove_node(0)
        self.assertEqual(mc+5,g.get_mc())
        self.assertEqual(nodes-1,g.v_size())
    
    def test_remove_edge(self):
        g = getgraph()
        mc  = g.get_mc()
        edges = g.edges
        self.assertFalse(g.remove_edge(1,15))
        self.assertEqual(mc,g.get_mc())
        self.assertTrue(g.remove_edge(1,0))
        self.assertEqual(edges-1,g.edges)
    def test__eq__(self):
        g1 = getgraph()
        g2 = getgraph()
        self.assertEqual(g1,g2)
        
    


            



if __name__ == "__main__":
    unittest.main()