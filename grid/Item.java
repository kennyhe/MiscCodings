

public interface Item {

    public String getName();
    
    public void setName(String name, Hash hash);

    public Node getNode();                // Return the node this item belongs to
    
    public void setNode(Node node);
    
    public double[] getOwnerPos();
    
    public void setOwnerPos(double[] p);
    
    public double[] getLocation();
    
    public String toString();
}
