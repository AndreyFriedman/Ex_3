import unittest
from DiGraph import DiGraph
from GraphAlgo import GraphAlgo
from os import remove


class TestGraphAlgo(unittest.TestCase):
    def __init__(self, methodName: str) -> None:
        super().__init__(methodName=methodName)
    
    def test_get_graph(self):
        ga = GraphAlgo()
        ga.load_from_json("./data/A0.json")
        self.assertEqual(ga.graph,ga.get_graph())
    def test_load_from_json(self):
        ga = GraphAlgo()
        ga.load_from_json("./data/A0.json")
        self.assertEqual(ga.graph.v_size(),11)
        self.assertEqual(ga.graph.e_size(),22)
        self.assertEqual(ga.graph.get_mc(),33)
    def test_save_to_json(self):
        ga = GraphAlgo()
        ga.load_from_json("./data/A0.json")
        g1 = ga.graph
        ga.save_to_json("./data/file.json")
        ga.load_from_json("./data/file.json")
        self.assertEqual(ga.graph,g1)
        remove("./data/file.json")
    def test_shortest_path(self):
        g = DiGraph()
        g.add_node(1)
        g.add_node(2)
        g.add_edge(1,2,2)
        g.add_node(3)
        g.add_edge(2,3,1)
        g.add_edge(1,3,5)
        ga = GraphAlgo(g)
        ga.load_from_json("./data/A0.json")
        weight,path = ga.shortest_path(1,3)
        self.assertEqual(path,[1,2,3])
    def test_connected_component(self):
        g = DiGraph()
        for i in range(5):
            g.add_node(i)
        g.add_edge(0,1,1)
        g.add_edge(1,2,1)
        g.add_edge(2,3,1)
        g.add_edge(3,0,1)
        g.add_edge(2,4,1)
        ga = GraphAlgo(g)
        self.assertEqual([0,1,2,3],ga.connected_component(0))
        self.assertEqual(ga.connected_component(0),ga.connected_component(2))
    
    def test_connected_components(self):
        g = DiGraph()
        for i in range(5):
            g.add_node(i)
        g.add_edge(0,1,1)
        g.add_edge(1,2,1)
        g.add_edge(2,3,1)
        g.add_edge(3,0,1)
        g.add_edge(2,4,1)
        ga = GraphAlgo(g)
        self.assertEqual([[0,1,2,3],[4]],ga.connected_components())
    def test_transpose(self):
        def hasedge(n1:int,n2:int,graph:DiGraph)->bool:
            """
            Check if an edge exits between n1 and n2 in graph 
            @return true if so and false if not.
            """
            keys = graph.nodesdict.keys()
            if n1 in keys and n2 in keys:
                return n2 in graph.nodesdict[n1].out_neighbors
            else:
                return False
        g = DiGraph()
        for i in range(5):
            g.add_node(i)
        g.add_edge(0,1,1)
        g.add_edge(1,2,1)
        g.add_edge(2,3,1)
        g.add_edge(3,0,1)
        g.add_edge(2,4,1)
        ga = GraphAlgo(g)
        t = ga.transpose()
        for node in t.nodesdict.values():
            for key in node.out_neighbors:
                self.assertTrue(hasedge(key,node.key,g))
    
    def test_plot_graph(self):
        ga = GraphAlgo()
        try:
            ga.load_from_json("./data/A0.json") # graph with locations
            ga.plot_graph()
            ga.load_from_json("./data/T0.json") # graph with no locations 
            ga.plot_graph()
        except Exception as e: # if fail somehow 
            print(e.__cause__)
            raise AssertionError



if __name__ == "__main__":
    unittest.main()
