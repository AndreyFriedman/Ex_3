package api;

/**
 *  The class Represents the location with x,y,z coordinates
 */
public class GeoLocation implements geo_location
{
    private double x;
    private double y;
    private double z;
    public GeoLocation()
    {
        this(0,0,0);
    }

    /**
     * geo location constructor
     * @param x coordinate
     * @param y coordinate
     * @param z coordinate
     */
    public GeoLocation(double x, double y, double z)
    {
        setX(x);
        setY(y);
        setZ(z);
    }

    /**
     * gives back the x
     * @return the x coordinate
     */
    @Override
    public double x() {
        return x;
    }

    /**
     * gives back the y
     * @return the y coordinate
     */
    @Override
    public double y() {
        return y;
    }

    /**
     * gives back the z coordinate
     * @return the z coordinate
     */
    @Override
    public double z() {
        return z;
    }

    /**
     * gives the distance between another location to this location
     * @param g another location
     * @return the distance between them
     */
    @Override
    public double distance(geo_location g) { // calculate distance between points
        return Math.sqrt(Math.pow(g.x()-x(),2)+Math.pow(g.y()-y(),2)+Math.pow(g.z()-z(),2));
    }

    /**
     * set x location
     * @param x coordinate
     */
    private void setX(double x) {
        this.x = x;
    }

    /**
     * set y location
     * @param y coordinate
     */
    private void setY(double y) {
        this.y = y;
    }

    /**
     * set z location
     * @param z coordinate
     */
    private void setZ(double z) {
        this.z = z;
    }

    /**
     * Tells if other object is equal to this location
     * @param obj another object
     * @return if they are equal
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof GeoLocation)
        {
            GeoLocation t = (GeoLocation) obj;
            return x==t.x && y == t.y && z==t.z; // if they have the same location they are equal
        }
        else // otherwise false
            return false;
    }

    /**
     * String representation of this location.
     * @return
     */
    @Override
    public String toString() {
        return x+","+y+","+z;
    }
}
