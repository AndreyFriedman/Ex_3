package api;

import java.io.*;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import org.w3c.dom.Node;

/**
 * This class represents a set of algorithms on a graph which is given to it as an input.
 */
public class DWGraph_Algo implements dw_graph_algorithms {
    private DWGraph_DS g; //  A graph  object

    /**
     * Default constructor
     */
    public DWGraph_Algo()
    {
        init(null);
    }

    /**
     * Constructor taking a graph object
     * @param g graph object
     */
    public DWGraph_Algo(directed_weighted_graph g)
    {
        init(g); // init it to the g field in class
    }

    /**
     * setter to g field
     * @param g graph
     */
    @Override
    public void init(directed_weighted_graph g)
    {
        this.g = (DWGraph_DS)g; // cast into g
    }

    /**
     * get the graph
     * @return the graph
     */
    @Override
    public directed_weighted_graph getGraph()
    {
        return g;
    }

    /**
     * create a deep copy of g
     * @return deep copy of the graph
     */
    @Override
    public directed_weighted_graph copy()
    {
        return new DWGraph_DS(g); // return the copy constructor
    }

    /**
     * Function to determine if g graph is connected.
     * using the dfs algorithm on the graph and it's Transpose.
     * @return true if the graph is connected
     */
    @Override
    public boolean isConnected()
    {
        Iterator<node_data> iterator = g.getV().iterator(); // create an iterator
        node_data start; // take a start var
        if(iterator.hasNext()) // if there is a next node
            start = iterator.next(); // take it as start
        else
            return true; // otherwise graph is empty thus return true
        for (node_data n: g.getV()) // go over all tags and fill them with 0
        {
            n.setTag(0);
        }
        dfs(g,start); // use dfs on the grpah
        for (node_data n:g.getV())
        {
            if(n.getTag()==0) // if there is node that no one had visited
                return false;
        }
        // create the transpose graph using this constructor
        DWGraph_DS gT = new DWGraph_DS(g,true);
        iterator = gT.getV().iterator(); // iterate over it
        start = iterator.next(); // take a start
        for (node_data n: gT.getV())
        {
            n.setTag(0); // set all tags to 0
        }
        dfs(gT,start); // do dfs on it
        for (node_data n:gT.getV()) // go over all nodes
        {
            if(n.getTag()==0) // if it's 0 return false
                return false;
        }
        return true; // return true sine all tests passed
    }

    /**
     * DFS algorithm using recursion since it's simpler to write.
     * @param g graph
     * @param v a node
     */
    private void  dfs(DWGraph_DS g,node_data v)
    {
//        if(v==null) // this means that there is no node to start from thus terminate
//            return;
//        v.setTag(1); // set v as 1
//        for(edge_data e:g.getE(v.getKey())) // go over all edges
//        {
//            if(g.getNode(e.getDest()).getTag() == 0) // if I hasn't been to destination
//                dfs(g,g.getNode(e.getDest())); // do dfs on it
//        }
        Stack<node_data> s = new Stack<node_data>();
        s.push(v);
        while (!s.empty())
        {
            node_data node = s.pop();
            if (node.getTag()==0)
            {
                node.setTag(1);
                NodeData n = (NodeData)node;
                for (int i :n.getNiEdges().keySet())
                {
                    s.push(g.getNode(i));
                }
            }
        }
    }

    /**
     * Calculate shortest path distance
     * @param src - start node
     * @param dest - end (target) node
     * @return number of the least cost
     */
    @Override
    public double shortestPathDist(int src, int dest)
    {
        if(g==null) // if g is null return -1
            return -1;
        if(g.getNode(src)==null || g.getNode(dest)==null) // if one of the nodes not in the graph return -1
            return -1;
        PriorityQueue<NodeData> PQ = new PriorityQueue<NodeData>(g.getV().size(),(NodeData)g.getNode(src)); // create a Priority Queue
        for(node_data n:g.getV()) // go all over the nodes and set them to -1 in the weight
        {
            n.setWeight(-1);
        }
        NodeData source = (NodeData) g.getNode(src); // cast into source to  start from
        source.setWeight(0); // set source as 0
        PQ.add(source); // add it into PQ
        while(!PQ.isEmpty()) // loop while PQ isn't empty
        {
            NodeData n = PQ.poll(); // take a node out of PQ
            for (edge_data e: g.getE(n.getKey())) // go over all the edges connected to that node
            {
                node_data temp = g.getNode(e.getDest()); // get the dest node
                if(temp.getWeight()==-1)//means we never got to it
                {
                    temp.setWeight(e.getWeight()+n.getWeight()); // set it's new weight
                    PQ.add((NodeData)temp); // add it to PQ
                }
                if(temp.getWeight()>e.getWeight()+n.getWeight()) // if there is a new weight which is lighter than the current one
                {
                    temp.setWeight(e.getWeight()+n.getWeight()); // switch to that weight
                    PQ.remove((NodeData)temp); // remove that node from PQ
                    PQ.add((NodeData)temp); // add it back in
                }
            }
        }
        return g.getNode(dest).getWeight(); // return the value inside of destination node weight
    }

    /**
     * Find the shortest path between nodes in the graph
     * @param src - start node
     * @param dest - end (target) node
     * @return list of nodes as path
     */
    @Override
    public List<node_data> shortestPath(int src, int dest) {
        if(g==null) // if g is null return null
            return null;
        if(g.getNode(src)==null || g.getNode(dest)==null) // if one of the nodes isn't in the graph return null
            return null;
        if (src==dest) // if src is the dest return an empty array
            return new ArrayList<node_data>();
        HashMap<NodeData,NodeData> parent = new HashMap<NodeData,NodeData>(); // create a parent hashmap
        PriorityQueue<NodeData> PQ = new PriorityQueue<NodeData>(g.getV().size(),(NodeData)g.getNode(src)); // create a priority queue
        for(node_data n:g.getV()) // go over all nodes
        {
            n.setWeight(-1); // set their weighs to -1
        }
        NodeData source = (NodeData) g.getNode(src); // take a source
        source.setWeight(0); // set the weight to -1
        PQ.add(source); // add it to the priority queue
        while(!PQ.isEmpty()) // loop as long as queue is not empty
        {
            NodeData n = PQ.poll(); // get a node out of the queue
            for (edge_data e: g.getE(n.getKey())) // loop on all of the edges
            {
                node_data temp = g.getNode(e.getDest()); // get the neighboring node
                if(temp.getWeight()==-1)//means we never got to it
                {
                    temp.setWeight(e.getWeight()+n.getWeight());// set it's new weight
                    PQ.add((NodeData)temp); // add it to priority queue
                    parent.put((NodeData)temp,n); // add its way as from the node before it
                }
                if(temp.getWeight()>e.getWeight()+n.getWeight()) // if a new lighter way discovered update it in the same manner
                {
                    temp.setWeight(e.getWeight()+n.getWeight()); // set lighter new weight
                    PQ.remove((NodeData)temp);
                    PQ.add((NodeData)temp);
                    parent.put((NodeData)temp,n); // put new way into hashmap
                }
            }
        }
        if (!parent.containsKey(g.getNode(dest))) // if parent doesn't contains the dest node it means we never got to it thus we need to return null
            return null;
        ArrayList<node_data> ret = new ArrayList<node_data>(); // create an array
        NodeData nodeData = (NodeData) g.getNode(dest); // cast the dest into the nodedata
        while (!parent.get(nodeData).equals(g.getNode(src))) // loop until getting to src
        {
            ret.add(nodeData); // add it to array
            nodeData = parent.get(nodeData);
        }
        ret.add(nodeData); // add last node into array
        ret.add(parent.get(nodeData)); // and add the src
        Collections.reverse(ret); // reverse the way
        return ret; // return the array
    }

    /**
     * Save the graph G in json form
     * @param file - the file name (may include a relative path).
     * @return true if saved successfully
     */
    @Override
    public boolean save(String file) {
        if (g==null) // if g is null return  false
            return false;
        try
        {
            File f = new File(file); // open file
            if (f.exists()) // if file exists
                f.delete(); // delete the file
            f.createNewFile(); // create the file to make sure nothing in it
            FileWriter fw = new FileWriter(file); // create filewriter object
            Gson  gson = new GsonBuilder().setPrettyPrinting().create();//create gson object
            DWGraph_DS.WrapDWGraph_DS temp = g.new WrapDWGraph_DS(); // wrap the graph
            String towrite = gson.toJson(temp); // get json string form the wrapped graph object
            if (f.canWrite()) // make sure can write into the file
            {
                fw.write(towrite); // write to file if allowed
                fw.close(); // close the writer
            }
            else
                return false;
            return true; // return true since success
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return false; // since failed
        }
    }

    /**
     * Load graph object from json file
     * @param file - file name of JSON file
     * @return true if loaded successfully
     */
    @Override
    public boolean load(String file) {
        try
        {
            File f = new File(file); // create a file object
            if(!f.canRead()) // make sure can read from it
                return false;
            FileReader fr = new FileReader(file); // create
            Gson gson = new Gson(); // create a gson object
            JsonReader jr = new JsonReader(fr); // create a json reader
            g = new DWGraph_DS((DWGraph_DS.WrapDWGraph_DS) gson.fromJson(jr,DWGraph_DS.WrapDWGraph_DS.class)); // convert json form into graph object using gson
            return true; // success
        }
        catch (IOException exception) {
            exception.printStackTrace();
            return false; //  failed
        }
    }
    public ArrayList<node_data> findScc(DWGraph_DS graph_ds,node_data n)
    {
        ArrayList<node_data> endlist = new ArrayList<node_data>();
        for (node_data node: graph_ds.getV()){
            node.setTag(0);
        }
        dfs(graph_ds,n);
        ArrayList<node_data> nodes1 = new ArrayList<node_data>();
        for (node_data node: graph_ds.getV()){
            if(node.getTag()==1)
                nodes1.add(node);
        }
        DWGraph_DS gT = new DWGraph_DS(graph_ds,true);
        node_data t1 = gT.getNode(n.getKey());

        for (node_data node: gT.getV()){
            node.setTag(0);
        }
        dfs(gT,t1);
        ArrayList<node_data> nodes2 = new ArrayList<node_data>();
        for (node_data node: gT.getV()){
            if(node.getTag()==1)
                nodes2.add(node);
        }
        for(node_data n1: nodes1)
        {
            for (node_data n2: nodes2)
                if(n2.getKey() == n1.getKey() )
                {
                    endlist.add(n1);
                    break;
                }
        }
        return endlist;
    }

    public ArrayList<ArrayList<node_data>> SCCS(DWGraph_DS graph_ds)
    {
        ArrayList<ArrayList<node_data>> sccs = new ArrayList<ArrayList<node_data>>();
        ArrayList<node_data> disc = new ArrayList<node_data>();
        for(node_data n:graph_ds.getV())
        {
            if(!disc.contains(n))
            {
                ArrayList<node_data> scc = findScc(graph_ds,n);
                sccs.add(scc);
                disc.addAll(scc);
            }
        }
        return sccs;
    }

    public static void main(String[] args) {

        DWGraph_Algo ga = new DWGraph_Algo();
        File fol = new File("./Graph_no_pos");
        String files [] = fol.list();
        for (String f : files)
        {
            ga.load("./Graph_no_pos/"+f);
            long start =  System.currentTimeMillis();
            ga.SCCS(ga.g);
            System.out.println(f+"  "+(System.currentTimeMillis()-start));
        }

    }

}
