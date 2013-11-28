import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * Create the nodes and maintain the logical relationship of the nodes. 
 * @author she
 *
 */
public class P2pNodeFactory implements NodeFactory {
    
    @Override
    public void addNode(double[] co) {
    	if (null == nodes) {
    		// Create an area which cover the whole area.
    		Splitable s = new P2pSpace();
    		double[] v1 = new double[GlobalSettings.getDimension()];
    		double[] v2 = new double[GlobalSettings.getDimension()];
    		for (int i = 0; i < v1.length; i++) {
    			v1[i] = 0.0D;
    			v2[i] = 1.0D;
    		}
    		
    		s.setVertex(v1, v2);
    		
			Dbg.verbose("I'm here");
    		// Create the first node
    		Node n = new P2pNode();
    		n.addArea(s);
    		s.setNode(n);
    		n.setKeyPos(co);
    		
    		// Add the node into the node lists. 
    		nodes = new ArrayList<Node>();
    		nodes.add(n);
    		Dbg.verbose(n.toString());
    	} else {
    		// To locate the node in which the co is in.
    		Node n = null;
    		for (Node tmp : nodes) {
    			if (tmp.isPosIn(co)) {
    				n = tmp;
    				break;
    			}
    		}
    		
    		// If the node is identical to an existing node, then do not add.
    		boolean same = true;
    		for (int i = 0; i < n.getKeyPos().length; i++) {
    			if (Math.abs(n.getKeyPos()[i] - co[i]) > 1e-6) { 
    				same  = false;
    				break;
    			}
    		}
    		if (same) {
    			System.out.println("The coordination exists, please use another coordination!");
    			return;
    		}
    		
    		// Split the node into two
    		SplitPlan ssp = n.split(co);
    		n.setAreas(ssp.getPart(0));
    		Node n2 = new P2pNode();
    		n2.setAreas(ssp.getPart(1));
    		n2.setKeyPos(co);
    		
    		// Split the items
    		Iterator<Item> it1 = n.getItems().iterator();
    		while (it1.hasNext()) {
    			Item i = it1.next();
    			if (n2.isPosIn(i.getLocation()))
    				n2.addItem(i);	// Put the items which belong to n2 into items2
    		}
    		
    		it1 = n2.getItems().iterator();
    		while (it1.hasNext()) { // Keep the items which belong to n
    			n.getItems().remove(it1.next());
    		}
    		
    		// Set the split tags:
    		n.setSplited(true);
    		n2.setSplitFrom(n);
    		nodes.add(n2);
    		Dbg.verbose(n.toString());
    		Dbg.verbose(n2.toString());
    	}
    }
    

    @Override
    public boolean removeNode(double[] co) {
    	if (null == nodes) {
    		System.out.println("No nodes at all!");
    		return false;
    	} else if (nodes.size() == 1) { 
    		System.out.println("Only one node, can not delete!");
    		return false;
    	}
    
    	Iterator<Node> it = nodes.iterator();
    	boolean notMatch = true;
    	Node n = null;	// n will be the matched node if successful match.
    	while (notMatch && it.hasNext()) {
    		n = it.next();
    		notMatch = ! n.isKeyPos(co);
    	}
    	
    	// If the position is not a key position, just return false.
    	if (notMatch)
    		return false;
    	
    	Node n2 = null;	// The node to be merged with n1
    	// Recollect node n, and merge the areas with other areas.
    	if (n.isNeighbor(n.getSplitFrom())) {
    		// Merge the n and the node it was split from.
    		n2 = n.getSplitFrom();
    	} else { // Choose a neighbor to merge.
        	it = nodes.iterator();
        	notMatch = true;
        	while (notMatch && it.hasNext()) {
        		n2 = it.next();
        		notMatch = ! n2.isNeighbor(n);
        	}
    	}
    	
    	nodes.remove(n);
    	// Move the items to the combined node.
    	Iterator<Item> iti = n.getItems().iterator();
    	while (iti.hasNext()) {
    		n2.addItem(iti.next());
    	}
    	
    	// Merge the areas in n into n2. Try to merge all the areas which can be merged. Not the optimal solution.
    	Iterator<Splitable> s1 = n.getAllAreas().iterator();
    	Iterator<Splitable> s2;
    	while (s1.hasNext()) {
    		Splitable a1 = s1.next();
    		Splitable a2 = null;
    		// Find an area in n2, the item can merge a1
    		s2 = n2.getAllAreas().iterator();
    		int side = -1;
    		while ((side == -1) && s2.hasNext()) {
    			a2 = s2.next();
    			side = a2.testMerge(a1);
    		}
    		
    		if (side >= 0) {// Change the size of the a2
    			if (Math.abs(a1.getVertex1()[side] - a2.getVertex2()[side]) < 1e-6) {
    				a2.update(1, side, a1.getVertex2()[side]);
    			} else if (Math.abs(a1.getVertex2()[side] - a2.getVertex1()[side]) < 1e-6) {
        			a2.update(0, side, a1.getVertex1()[side]);
        		}
    		} else { // Simply add a1 into n2.
    			n2.addArea(a1);
    		}
    		
    		// If time allows, do the more merges within n2.
    		// ToDo.
    	}
    	
    	return true;
    }

    
    @Override
    public Item insertItem(double[] co, String name) {
		// If the name is identical to that of an existing item, then do not add.
		boolean same = false;
		for (Item item : items) {
			if (item.getName().equals(name)) { 
				same  = true;
				break;
			}
		}
		if (same) {
			System.out.println("The name exists, please use another name!");
			return null;
		}
    	
        Item i = new P2pItem();
        i.setName(name, new P2pHash());
        i.setOwnerPos(co);
        
        // Locate the node
		Node node = null;
		Iterator<Node> it = nodes.iterator();
		while (it.hasNext()) {
			node = it.next();
			if (node.isPosIn(i.getLocation())) {
				break;
			}
		}
        node.addItem(i);
        items.add(i);
    	return i;
    }
    
    
    @Override
    public boolean deleteItem(double[] co, String n) {
    	if (items.size() == 0) {
    		System.out.println("No items in the P2P network, can not delete.");
    		return false;
    	}
    	
		boolean notFound = true;
		Item i = null;
    	for (Item tmp : items) {
    		if (n.equals(tmp.getName())) {
    			boolean matched = true;
	    		int j = 0;
	    		while (matched && j < co.length) {
	    			if (Math.abs(co[j] - tmp.getOwnerPos()[j]) > 1e-6) {
	    				matched = false;
	    			}
	    			j++;
	    		}
	    		notFound = ! matched;
	    		if (matched) {
	    			i = tmp;
	    			break; 
	    		}
    		}
    	}
    	
    	if (notFound) {
    		System.out.println("No item with the same name and owner coordination can be found! Delete failed.");
    		return false;
    	}
    	
    	// i is the matched item.
    	i.getNode().getItems().remove(i);
    	items.remove(i);
    	
        System.out.println("Successfully deleted the item: \"" + i.getName() + "\"");
    	return true;
    }
    
    
    @Override
    public ArrayList<Node> findItem(double[] co, String name) {
    	if (items == null) {
    		System.out.println("No items in the P2P network. Please insert items.");
    		return null;
    	}
    	
    	// Locate the item
    	Iterator<Item> it = items.iterator();
    	boolean notFound = true;
    	Item i = null;
    	while (notFound && it.hasNext()) {
    		i = it.next();
    		notFound = ! name.equals(i.getName());
    	}
    	
    	if (notFound)
    		return null;
    	
    	// Locate the node that searcher point belongs to
    	Iterator<Node> it1 = nodes.iterator();
    	Node n1 = null;	// The node searcher belongs to
    	while (it1.hasNext()) {
    		n1 = it1.next();
    		if (n1.isPosIn(co))
    			break;
    	}
    	
    	Node n2 = i.getNode(); // The node item belongs to
    	
    	Dbg.verbose("Node1 ="+n1);
    	Dbg.verbose("Node2 ="+n2);
    	
    	if (n1 == n2) { // The searcher
        	ArrayList<Node> route = new ArrayList<Node>();
        	route.add(n1);
    		return route;
    	} else if (n1.isNeighbor(n2)) { // They are neighbors
        	ArrayList<Node> route = new ArrayList<Node>();
        	route.add(n1);
        	route.add(n2);
    		return route;
    	}
    	
    	// Need to use the width discovery algorithm
    	ArrayList<Node> visited = new ArrayList<Node>();
    	ArrayList<Node> recent = new ArrayList<Node>();
		Hashtable<Node, ArrayList<Node>> paths = new Hashtable<Node, ArrayList<Node>>();
		
		Dbg.verbose("Start point " + n1);
		for (Node n : nodes) {
			if (n == n1) {
				visited.add(n);
			} else if (n.isNeighbor(n1)) {
				visited.add(n);
				recent.add(n);
		    	ArrayList<Node> path = new ArrayList<Node>();
		    	path.add(n1);
		    	path.add(n);
		    	paths.put(n, path);
		    	Dbg.verbose("Visisted " + n);
			}
		}
		
    	while (true) {
        	ArrayList<Node> next = new ArrayList<Node>();
        	for (Node pre : recent) {
               	for (Node n : nodes) {
               		if ((! visited.contains(n)) && pre.isNeighbor(n)) {
        		    	Dbg.verbose("Visisted " + n);
               			visited.add(n);
               			next.add(n);
        		    	ArrayList<Node> path = new ArrayList<Node>(paths.get(pre));
        		    	path.add(n);
        		    	if (n == n2) {
        		    		return path;
        		    	}
        		    	paths.put(n, path);
                	}
        		}
        	}
        	recent = next;
    	}
    }

    
	@Override
	public String printNodes() {
		StringBuffer sb = new StringBuffer("There are ");
		sb.append(nodes.size()).append(" nodes:\n");
		for (Node n:nodes)
			sb.append(n.toString());
		
		return sb.toString();
	}
    

	@Override
	public String printItems() {
		if (items.size() == 0)
			return "There is no items now. Please insert some items.";
		
		StringBuffer sb = new StringBuffer("There are ");
		sb.append(items.size()).append(" items:\n");
		for (Item i:items)
			sb.append(i.toString()).append("\n");
		
		return sb.toString();
	}

	
	ArrayList<Item> items = new ArrayList<Item>();
    ArrayList<Node> nodes = null;
}
