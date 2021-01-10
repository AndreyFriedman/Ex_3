package api;

/**
 * Class Represents a weighted and directed edge between two nodes.
 */
public class EdgeData implements edge_data {
    private int src; // key of source node
    private int dest; // key of destanation node
    private double weight; // weight of the edge between them
    private int tag;
    private String info;

    /**
     *  A constructor for the class that takes src dest and weight.
     * @param src key of src
     * @param dest key of dest
     * @param weight the weight of that edge
     */
    public EdgeData(int src,int dest,double weight)
    {
        this(src,dest,weight,0,""); // using the bigger constructor but with constants
    }

    /**
     * The complete constructor for this class taking input for all the field.
     * @param src key of src
     * @param dest key of dest
     * @param weight weight of edge
     * @param tag tag for that edge
     * @param info String info about that edge
     */
    public EdgeData(int src, int dest,double weight, int tag, String info)
    {
        // set all the fields
        this.src = src;
        this.dest = dest;
        this.weight = weight;
        setTag(tag);
        setInfo(info);
    }

    /**
     * Copy constructor taking another edge and copying it.
     * @param other another edge to deep copy from
     */
    public EdgeData(EdgeData other)
    {
        this(other.src,other.dest,other.weight,other.tag,other.info);
    }

    /**
     * Copy constructor that changes just the weight of the edge .
     * @param other another edge
     * @param weight number of weight
     */
    public EdgeData(EdgeData other,double weight)
    {
        this(other.src,other.dest,weight,other.tag,other.info);

    }

    /**
     * get the src
     * @return return key of src
     */
    @Override
    public int getSrc()
    {
        return src;
    }

    /**
     * get the dest
     * @return the key of the dest
     */
    @Override
    public int getDest()
    {
        return dest;
    }

    /**
     * get the weight
     * @return the weight of this edge
     */
    @Override
    public double getWeight()
    {
        return weight;
    }

    /**
     * get the info
     * @return the info of that edge
     */
    @Override
    public String getInfo()
    {
        return info;
    }

    /**
     * set the info
     * @param s info to set
     */
    @Override
    public void setInfo(String s)
    {
        info = s;
    }

    /**
     * get the tag of this edge
     * @return the tag of this edge
     */
    @Override
    public int getTag()
    {
        return tag;
    }

    /**
     * set the tag for this edge
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t)
    {
        tag = t;
    }

    /**
     * Override the equals methode.
     * @param obj object to test equality
     * @return true if equal and false if not
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof EdgeData)
        {
            EdgeData e = (EdgeData)obj;
            // since the data inside the tag and info fields is changeable im not going to include it in this comparison
            // just for the simplicity.
            return e.src==src&&e.dest==dest&&e.weight==weight; // return true if this is true
        }
        return false; // otherwise if not
    }

    /**
     * A wrap class for the EdgeData class, representing all the important information that needs saving
     */
    public class WrapEdgeData
    {
        private int src; // taking source
        private double w; // taking weight
        private int dest; // taking destination

        /**
         * default constructor
         */
        public WrapEdgeData()
        {
            // set them right form the outer class of that object
            this.src = EdgeData.this.src;
            this.w = weight;
            this.dest = EdgeData.this.dest;
        }

        public int getSrc() {
            return src;
        }

        public double getW() {
            return w;
        }

        public int getDest() {
            return dest;
        }
    }

}
