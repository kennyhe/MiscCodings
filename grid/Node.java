import java.util.ArrayList;

public interface Node {
    
    /**
     * Get the spaces this node owns
     * @return An array list of all the areas within this node
     */
	public ArrayList<Splitable> getAllAreas();
    
	/**
	 * When adding a new node in this zone, just need to split this zone into two parts.
	 * If the two points locate in the same area, then split that area, and share the other areas
	 * with these two areas to make sure both side are balanced (also the neighborhood relationship 
	 * also need to be considered.
	 * If they are located in different areas, just split the current areas into two different group
	 * and try to balance their capacity.
	 * Due to the time limitation, the result may not be the most optimal one. Just add the areas one
	 * by one and try to balance when adding the new area into a group. And also need to consider the 
	 * 
	 * @param p The point to be added to create the new node
	 * @return	The split plan which contains the two groups of areas.
	 */
    public SplitPlan split(double[] p2);
    
    /**
     * Add an area into the current node.
     * @param s	 The area to be added.
     */
    public void addArea(Splitable s);
    
    /**
     * Update the areas in this node
     * @param as An array list which contains all the new areas.
     */
    public void setAreas(ArrayList<Splitable> s);

    /**
     * Check whether the area is neighbor of this node
     * @param s The area to be tested
     * @return	True if the area is a neighbor of this node. Else false.
     */
    public boolean isNeighbor(Splitable s);
    
    /**
     * Check whether the node is neighbor of this node
     * @param n The node to be tested
     * @return	True if the node is a neighbor of this node. Else false.
     */
    public boolean isNeighbor(Node n);
    
    /**
     * Get the capacity of this node. It is the sum of the capacities of all areas.
     * @return The capacity
     */
    public double getCapacity();
    
    
    /**
     * Getter of the key position which is used for creating this node.
     * @return
     */
    public double[] getKeyPos();
    
    /**
     * Setter of the key position which is used for creating this node.
     * @param pos The Key position
     */
    public void setKeyPos(double[] pos);
    
    
    /**
     * Check whether a position is inside this node. Just check all the areas.
     * @param pos The position to be tested
     * @return True if it is inside. Else false.
     */
    boolean isPosIn(double[] pos);
    
    
    /**
     * Check whether a position is the key position of a node.
     * @param pos The position to be tested
     * @return True if it is the key position. Else false.
     */
    boolean isKeyPos(double[] pos);
    
    
    /**
     * Add an item to the node. And it will call Item.addNote() to set the node field of the item to itself
     * @param i the item to be added.
     */
    public void addItem(Item i);
    
    /**
     * Get all the items in this node.
     * @return An array list of items in this node. If no node, then return an empty Array list.
     */
    public ArrayList<Item> getItems();
    
    
	/**
	 * Get whether this node has been splited
	 * @return the value.
	 */
    public boolean isSplited();

	/**
	 * Setter of split property
	 * @param splited
	 */
	public void setSplited(boolean splited);

	
	/**
	 * Get which Node the current node is split from.
	 * @return The node that current node is split from
	 */
	public Node getSplitFrom();

	
	/**
	 * Setter of the node which current node is split from
	 * @param splitFrom The node from which current node is split
	 */
	public void setSplitFrom(Node splitFrom);
}
