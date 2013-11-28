public interface Splitable {
    
	/**
	 * The following three functions are getter and setters.
	 */
	public void setVertex(double[] p1, double[] p2);

    public double[] getVertex1();

    public double[] getVertex2();

    /**
     * Get the capacity of this area. It multiply the length of all the edges
     * @return The capacity of this area
     */
    public double getCapacity();

    /**
     * Update the coordination of the area.
     * @param v 0 means update vertex1, 1 means update vertex2
     * @param index The index of the coordination
     * @param value The new value
     */
    public void update(int v, int index, double value);

    
    /**
     * To split this area into two or more sub-areas. Each split should split an area into two 
     * equal half. The algorithm runs until the two points are located into two parts which are
     * most close in capacity.
     * @param p1 The coordination of point one
     * @param p2 The coordination of point two
     * @return The plan about how to split
     */
    SplitPlan split(double[] p1, double[] p2);
    
    /**
     * To check whether this area and another area are neighbors.
     * @param s2
     * @return true if they are neighbors.
     */
    boolean isNeighbor(Splitable s2);
    
    /**
     * To find out whether this area can be merged with another one.
     * @param s2 The other area to be merged
     * @return    -1 if can not merge in all sides. A number >=0 means the two areas can be merged in this side. 
     */
    int testMerge(Splitable s2);
    
    /**
     * Get the node that this area belongs to
     */
    Node getNode();
    
    /**
     * Set the node that this area belongs to
     * @param n
     */
    void setNode(Node n);
    
    /**
     * Check whether a position is inside this area. [vertex1, vertex2)
     * @param pos The position to be tested
     * @return True if it is inside. Else false.
     */
    boolean isPosIn(double[] pos);
}
