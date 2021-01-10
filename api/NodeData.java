package api;




import java.util.*;

/**
 * This class describes a logical node in a graph.
 */
public class NodeData implements node_data,Comparator<NodeData>
{
    private int key; // unchangeable key of the node
    private int tag;
    private double weight; // special weight for node
    private String info; // info to put to that node
    private GeoLocation location; // location of that in (x,y,z)
    private HashMap<Integer, NodeData> neighbors; // hashmap of all the neighbor
    private HashMap<Integer, EdgeData> neighborsedges; // hashamp of the edges to neighbor
    private static int numofnodes = 0; // a static variable to follow how many node instances  were created

    /**
     *  A contructor to the node class that takes a wrapNodeData instance , and builds back a
     *  node from it.
     * @param wrapNodeData wrapper of node class
     */
    public NodeData(WrapNodeData wrapNodeData)
    {
        if(wrapNodeData==null) // make sure it's not null
            return; // if so stop operation
        neighbors = new HashMap<Integer, NodeData>(); // init the neighbors hashmap
        neighborsedges = new HashMap<Integer, EdgeData>(); // init the neighbors edges hashmap
        this.key = wrapNodeData.id; // put the key back in place
        if (wrapNodeData.pos!=null)
        {
            String positions [] = wrapNodeData.pos.split(","); // parse the position String into a string array
            if(positions.length!=3) // make sure that if there are not 3 values in the array we cannot continue...
                return; // terminate operation
            // create the geolocation object using parseDouble
            location = new GeoLocation(Double.parseDouble(positions[0]),Double.parseDouble(positions[1]),Double.parseDouble(positions[2]));
        }
        setInfo(""); // set info to default nothing
        setTag(0); // set tag to default 0
        setWeight(0); // set weight to default 0
        numofnodes++; // count another node creation
    }

    /**
     * this constructor needs only a key and a weight
     * @param key key of node
     * @param weight weight for that node
     */
    public NodeData(int key,double weight)
    {
        this(key,0,weight,"",null); // call a bigger constructor but with default chosen values
    }

    /**
     *  This is the full constructor that takes everything that a node should have
     * @param key key of node
     * @param tag tag for that node
     * @param weight weight for that node
     * @param info info for that node
     * @param location location for that node
     */
    public NodeData(int key, int tag, double weight,String info, GeoLocation location)
    {
        setKey(key); // set the key in place
        setTag(tag); // set the chosen tag in place
        setWeight(weight); // set the chosen weight in place
        setInfo(info); // set the chosen info in place
        setLocation(location); // set the chosen location in place
        neighbors =new HashMap<Integer, NodeData>();  // init the neighbors hashmap
        neighborsedges = new HashMap<Integer, EdgeData>(); // init the neighbors edges  hashmap
        numofnodes++; // add one to node creation
    }

    /**
     * simply returns the key
     * @return the key of the node
     */
    @Override
    public int getKey()
    {
        return key;
    }

    /**
     * simply returns the location
     * @return the location
     */
    @Override
    public geo_location getLocation()
    {
        return this.location;
    }

    /**
     * simply sets the desired location for that node
     * @param p - new new location  (position) of this node.
     */
    @Override
    public void setLocation(geo_location p)
    {
        this.location = (GeoLocation) p; // put it in with a cast
    }

    /**
     * simply returns the weight of that node
     * @return the weight
     */
    @Override
    public double getWeight()
    {
        return this.weight;
    }

    /**
     * simply sets the weight of that node
     * @param w - the new weight
     */
    @Override
    public void setWeight(double w)
    {
        weight = w;
    }

    /**
     * simply returns the info of that node
     * @return return info for that node
     */
    @Override
    public String getInfo() {
        return info;
    }

    /**
     * sets the info of that stirng
     * @param s String of info for that node
     */
    @Override
    public void setInfo(String s)
    {
        info = s;
    }

    /**
     * simply gets the tag of that node
     * @return the tag of that node
     */
    @Override
    public int getTag() {
        return tag;
    }

    /**
     * simply set the tag of that node
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t)
    {
        tag = t;
    }

    /**
     * simply set the key of that node
     * @param key set the key of that node
     */
    private void setKey(int key) {
        this.key = key;
    }
    //todo hasNi

    /**
     * ask if two nodes are neighbors.
     * @param node another node
     * @return if two nodes are adjacent that means that there is an edge from one of them to the other.
     */
    public boolean hasNi(NodeData node)
    {
        return neighbors.containsKey(node.getKey())&&(hasEdge(node)||node.hasEdge(this));
    }
    /**
     * ask if there is an edge from this node to the other node.
     * @param node another node
     * @return true if there is an edge between them false otherwise
     */
    public boolean hasEdge(NodeData node)
    {
        return neighborsedges.containsKey(node.getKey());
    }

    /**
     * Takes another node and create an edge between them with desired weight
     * @param other another node
     * @param weight weight for the edge
     * @return
     */
    public boolean addNi(NodeData other,double weight)
    {
        if(weight<0) // if weight is negative return false since operation failed
            return false;
        if (other.getKey()==key) return false; // if we want to connect the same node to itself we abort and return false
        if(!hasNi(other)) // if they are not neighbors yet register them as ones
        {
            neighbors.put(other.getKey(),other); //add other to this list
            other.neighbors.put(getKey(),this); // add this to other list
        }
        if (hasEdge(other)) // if there is an edge between them already update weight
        {
            EdgeData edge = new EdgeData(neighborsedges.get(other.getKey()),weight); // create new edge
            neighborsedges.put(other.getKey(),edge); // put edge in the hashmap
            return true; // return true since operation suceeded
        }
        else // create a new edge with this weight between them
        {
            EdgeData edge = new EdgeData(this.getKey(),other.getKey(),weight);
            neighborsedges.put(other.getKey(),edge);
            return true;
        }
    }

    //todo removeNi_complete

    /**
     * Remove connection completely between two nodes
     * @param node another node
     * @return the amount of connection that were removed
     */
    public int removeNi_complete(NodeData node)
    {
        if(!hasNi(node)) // if they are not neighbors
        {
            return 0; // false since it didn't go well
        }
        else
        {
            int c =0;
            if(neighbors.containsKey(node.getKey())) // if this node contains the others key in the neighbors hashmap
            {
                neighbors.remove(node.getKey()); // remove it
                neighborsedges.remove(node.getKey()); // remove the edge too
                c++;
            }
            if(node.neighbors.containsKey(getKey())) // if the other way around do the same  but reverse
            {
                node.neighbors.remove(getKey());
                node.neighborsedges.remove(getKey());
                c++;
            }
            return c;
        }
    }
    //todo removeNi_oneway this->other

    /**
     * Remove a edge in one way from this -> other
     * @param node another node
     * @return true if successfully removed way
     */
    public boolean removeNi_oneway(NodeData node)
    {
        if(hasNi(node)&&hasEdge(node)) // make sure that they are neighbors  and that there is an edge this direction
        {
            neighborsedges.remove(node.getKey()); // remove  the edge itself
            if(!hasNi(node)) // if they are not neighbors anymore
            {
                // remove neighboring bonds from them
                neighbors.remove(node.getKey());
                node.neighbors.remove(getKey());
            }
            return true; // return true since succeeded
        }
        else
            return false; // return false since operation failed
    }
    /**
     * removeNi_otherway this<-other
     * @node another node
     * @return true if successfully removed way
      */
    public boolean removeNi_otherway(NodeData node)
    {
        if(hasNi(node)&&node.hasEdge(this)) // make sure that they are neighbors and that there is an edge between them
        {
            node.neighborsedges.remove(getKey()); // remove the edge
            if(!hasNi(node)) // if they are not neighbors anymore
            {
                // remove the neighboring bond between them
                neighbors.remove(node.getKey());
                node.neighbors.remove(getKey());
            }
            return true; // operation success
        }
        else
            return false; // operation failed
    }

    /**
     * return the weight of an edge from this to other
     * @param node another node
     * @return the weight between them
     */
    public double getWeight(NodeData node)
    {
        if(hasEdge(node)) // if there is an edge between them
            return neighborsedges.get(node.getKey()).getWeight();
        else
            return -1; // symbols that there is no edge between them
    }

    /**
     * Get the edge between two nodes
     * @param node another node
     * @return the edge between them
     */
    public EdgeData getEdge(NodeData node)
    {
        return neighborsedges.get(node.getKey());
    }

    /**
     * Get the edge between nodes using the key alone
     * @param key key of the other node
     * @return edge between them
     */
    public EdgeData getEdge(int key)
    {
        return neighborsedges.get(key);
    }

    /**
     * Get the neighbors hashmap
     * @return neighbors hashmap
     */
    public HashMap<Integer,NodeData> getNi()
    {
        return neighbors;
    }

    /**
     * create an array list of all the shallow copies to the neighbors and return it
     * @return copy if neighbors
     */
    public ArrayList<NodeData> copyOfNodes()
    {
        ArrayList<NodeData> arr = new ArrayList<NodeData>(); // create arraylist
        for (NodeData n: neighbors.values()) // loop through the nodes
        {
            arr.add(n); // add them to arr
        }
        return arr; // return  result
    }

    /**
     * get the neighbors edges hashmap
     * @return hashmap of edges
     */
    public HashMap<Integer,EdgeData> getNiEdges()
    {
        return neighborsedges;
    }

    /**
     * Compare the weight of two nodes
     * @param o1 node 1
     * @param o2 node 2
     * @return comparison between their weights
     */
    @Override
    public int compare(NodeData o1, NodeData o2)
    {
        return Double.compare(o1.getWeight(),o2.getWeight());
    }

    /**
     * override the equals function but for nodes
     * @param obj another object
     * @return if they are equal or not
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof NodeData) // make sure its nodedata instance
        {
            NodeData t = (NodeData) obj; // cast into t
            if (t.key!=key) return false; // return false if keys don't match
            for(EdgeData e:t.getNiEdges().values()) // loop through edges coming out
            {
                if(!(this.getEdge(e.getDest()).equals(e))) return false; // if the edges don't match return false
            }
            for(EdgeData e : neighborsedges.values()) // do the same the other way around
            {
                if(!(t.getEdge(e.getDest()).equals(e))) return false;
            }
            // in the end return true;
            return true;
        }
        return false; // it's not even instance of nodedata return false
    }

    /**
     * override toString 
     * @return representation of node
     */
    public String toString()
    {
        return "[Node "+getKey()+"]";
    }

    /**
     * A class to wrap the data inside the NodeData class
     */
    public class WrapNodeData
    {
        private int id;
        private String pos;

        /**
         * default constructor
         */
        public WrapNodeData(){
            id = NodeData.this.key;
            if (location!=null)
                pos = location.x()+","+location.y()+","+location.z();
            else
                pos="0,0,0";
    }
    }
}










