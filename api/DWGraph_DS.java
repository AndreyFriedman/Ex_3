package api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * This class Represents a Weighted directed graph.
 */
public class DWGraph_DS implements directed_weighted_graph
{
    private HashMap<Integer, node_data> nodes; // hashmap from keys to nodes
    private int MC; // mode count for the graph

    /**
     * A copy constructor from the wrap class of the graph
     * @param wrapDWGraph_ds wrapper graph object
     */
    public DWGraph_DS(WrapDWGraph_DS wrapDWGraph_ds)
    {
        this(); // call the basic constructor
        for (NodeData.WrapNodeData wrapNodeData:  wrapDWGraph_ds.Nodes) // place all the nodes in the hashmap
        {
            addNode(new NodeData(wrapNodeData));
        }
        for(EdgeData.WrapEdgeData wrapEdgeData: wrapDWGraph_ds.Edges) // connect them using the edges
        {
            connect(wrapEdgeData.getSrc(),wrapEdgeData.getDest(),wrapEdgeData.getW());
        }
    }

    /**
     * Basic constructor for the class creating an empty graph
     */
    public DWGraph_DS()
    {
        nodes = new HashMap<Integer, node_data>();
        MC = 0;
    }

    /**
     * A copy constructor taking another graph and deep copying it
     * @param g graph object to deep copy from
     */
    public DWGraph_DS( DWGraph_DS g)
    {
        this(); // call to basic constructor
        if (g==null) // if null terminate function
            return;
        for(node_data n: g.getV()) // copy all the nodes to the hashmap in deep copy
        {
            addNode(new NodeData(n.getKey(),n.getWeight())); // using copy constructor of NodeData
        }
        for(node_data n: g.getV()) // connect all the edges to the  nodes
        {
            for(edge_data e: g.getE(n.getKey()))
            {
                connect(e.getSrc(),e.getDest(),e.getWeight()); // using the connect methode
            }
        }
        MC = g.getMC(); // copy the MC
    }

    /**
     * A constructor using a dummy var to diffrent it from the regular constructor
     * This constructor is creating the Transpose of a graph G.
     * @param g graph object
     * @param dummyvar dummy variable for transpose graph
     */
    public DWGraph_DS(DWGraph_DS g, boolean dummyvar )
    {
        this(); // call the basic constructor
        if(g==null) // if g is null terminate function
            return;
        for(node_data n: g.getV()) // go over all vertices and add them
        {
            addNode(new NodeData(n.getKey(),n.getWeight()));
        }
        for(node_data n: g.getV()) // go over all edges and connect them in opposite way
        {
            for(edge_data e: g.getE(n.getKey()))
            {
                connect(e.getDest(),e.getSrc(),e.getWeight()); // dest --> src
            }
        }
    }

    /**
     * Function to get node_data with key from the nodes hashmap
     * @param key - the node_id
     * @return the node
     */
    @Override
    public node_data getNode(int key)
    {
        return nodes.get(key);
    }

    /**
     * function to get an edge between two nodes with src and dest keys.
     * @param src key of src
     * @param dest key of dest
     * @return an edge
     */
    @Override
    public edge_data getEdge(int src, int dest)
    {
        NodeData source = (NodeData) getNode(src);
        return source.getEdge(dest);
    }

    /**
     * function to add a node to the graph using node_data object
     * @param n the node to add
     */
    @Override
    public void addNode(node_data n)
    {
        nodes.put(n.getKey(),n);
        MC++; // add one to mode count
    }

    /**
     * Function to connect two nodes in the grpah
     * @param src - the source of the edge.
     * @param dest - the destination of the edge.
     * @param w - positive weight representing the cost (aka time, price, etc) between src-->dest.
     */
    @Override
    public void connect(int src, int dest, double w)
    {
        if (w<0) // w must be positive
            return;
        if (getNode(src) == null || getNode(dest)==null) // both nodes must be in the graph
            return;
        NodeData source = (NodeData)getNode(src); // cast into source
        source.addNi((NodeData) getNode(dest),w); // add to it as a neighbor
        MC++; //  add one to mode count
    }

    /**
     * Get a shallow copy to an array of vertices
     * @return collection of nodes
     */
    @Override
    public Collection<node_data> getV()
    {
        return nodes.values();
    }

    /**
     * get a copy array to shallow copies of Edges of specified node with key.
     * @param node_id key of node
     * @return collection of edges that go out of src
     */
    @Override
    public Collection<edge_data> getE(int node_id)
    {
        if (getNode(node_id)==null) // make sure node is in the graph
            return null;
        ArrayList<edge_data> arr = new ArrayList<edge_data>(); // create an empty arraylist
        NodeData node = (NodeData) getNode(node_id); // cast into node
        for(edge_data e:node.getNiEdges().values()) // go over all of it's edges
        {
            arr.add(e); // add it to the array
        }
        return arr; // return the array
    }

    /**
     * a function to remove a node from the graph adn all of it's connection
     * @param key key of node
     * @return the object of that node
     */
    @Override
    public node_data removeNode(int key)
    {
        if (getNode(key)==null) // make sure node is in the graph
            return null;
        NodeData node = (NodeData) getNode(key); // cast into node
        for(NodeData n: node.copyOfNodes()) // go over a copy array of all of the neighboring nodes
        {
            MC += node.removeNi_complete(n); // remove the connection from them completely and add that to mode count
        }
        nodes.remove(key); // remove the node itself from the graph
        MC++; //  add one to mode count
        return node; // return the node object
    }

    /**
     * Remove an edge between two nodes
     * @param src key of src
     * @param dest key of dest
     * @return return back that edge
     */
    @Override
    public edge_data removeEdge(int src, int dest) {
        if(getNode(src)==null||getNode(dest)==null) // make sure both nodes with src and dest keys are in the graph
            return null;
        NodeData source = (NodeData)getNode(src); // cast into node
        if(source.hasEdge((NodeData) getNode(dest))) // if edge exist between them
        {
            edge_data e = source.getEdge(dest); // take the edge
            source.removeNi_oneway((NodeData)getNode(dest)); // remove the connection one way src --> dest
            MC++; // add one to mode count
            return e; // return the edge
        }
        else
            return null; // return null since edge doesn't exist between them
    }

    @Override
    public int nodeSize() {
        return nodes.size();
    }

    /**
     * gives the edge size
     * @return the amount of edges that there is int the graph
     */
    @Override
    public int edgeSize()
    {
        int s = 0; // sum of edges
        for(node_data n : getV()) // go over all nodes
        {
            s+=getE(n.getKey()).size(); // sum all edges going out from that node
        }
        return s; // return the sum of edges
    }

    /**
     * Mode count
     * @return the modification count
     */
    @Override
    public int getMC()
    {
        return MC;
    }

    /**
     *
     * @param obj object to test equality to
     * @return if the object is equal or not
     */
    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof DWGraph_DS) // make sure that it's a graph object too
        {
            DWGraph_DS other = (DWGraph_DS) obj; // cast into a graph object
            for(node_data node: other.getV()) // go over all nodes from one side
            {
                if(this.getNode(node.getKey())==null) return false; // if one node doesn't exist at other side return false
                if(!((NodeData)this.getNode(node.getKey())).equals(node)) return false; // if the mirroring node doesn't equal return false

            }
            for(node_data node: getV()) // do the same thing but on the other way around
            {
                if(other.getNode(node.getKey())==null) return false;
                if(!((NodeData)other.getNode(node.getKey())).equals(node)) return false;
            }
            return true; // return true since passed test
        }
        else
        {
            return false; // otherwise false.
        }
    }

    /**
     * Override the toString function
     * @return string representation  of the graph
     */
    public String toString()
    {
        String ret=""; // String var to return
        for (node_data n: getV()) // go over all of the nodes
        {
            NodeData t1 = (NodeData)n; // cast into t1
            ret += t1+"[ "; // start the beginning of the line
            for (edge_data e:getE(n.getKey())) // goo over all of the edges and get their nodes and add them to the string
            {
                NodeData t2 = (NodeData) getNode(e.getDest()); // cast into t2
                ret += t2; // add it to ret
            }
            ret+="]\n"; // finish the string with ]\n

        }
        return ret; // return result
    }

    /**
     * A wrap class to the graph class
     */
    public class WrapDWGraph_DS
    {
        private ArrayList<NodeData.WrapNodeData> Nodes; // wrap nodes array
        private ArrayList<EdgeData.WrapEdgeData> Edges; // wrap edges array

        /**
         * default constructor
         */
        public WrapDWGraph_DS()
        {
            Nodes = new ArrayList<NodeData.WrapNodeData>(); // init nodes array
            Edges = new ArrayList<EdgeData.WrapEdgeData>();// init edges array
            for(node_data n : getV()) // copy all nodes with their wrap into the node array
            {
                NodeData t = (NodeData) n; // cast into t
                NodeData.WrapNodeData t2 = t.new WrapNodeData(); // use the wrap
                Nodes.add(t2); // add it to array
                for(edge_data e: getE(n.getKey())) // go over all edges from that node
                {
                    EdgeData t3 = (EdgeData)e; // cast into t3
                    EdgeData.WrapEdgeData t4 = t3.new WrapEdgeData(); // put in t4 and wrap it
                    Edges.add(t4); // add to edges array
                }
            }
        }
    }
}
