import java.util.ArrayList;


public interface NodeFactory {

    /**
     * Create a node by the coordinations
     * @param co
     */
    public void addNode(double[] co);
    
    /**
     * Remove a node by the coordinations
     * @param co
     * @return
     */
    public boolean removeNode(double[] co);
    
    /**
     * To create an item
     * @param co The position of the owner
     * @param n The name of the item
     * @return The new Item created
     */
    public Item insertItem(double[] co, String n);
    
    /**
     * Delete an item
     * @param co The position of the owner
     * @param n The name of the item
     * @return Whether the delete operation is success
     */
    public boolean deleteItem(double[] co, String n);
    
    /**
     * Find an item and return the optimal path of the nodes
     * @param co 
     * @param n
     * @return
     */
    public ArrayList<Node> findItem(double[] co, String n);
    
    /**
     * Print all nodes.
     * @return A string which represents all the nodes.
     */
    public String printNodes();
    
    /**
     * Print all items.
     * @return A string which represents all the items.
     */
    public String printItems();
}
