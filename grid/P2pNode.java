import java.util.ArrayList;
import java.util.Iterator;


public class P2pNode implements Node {

	@Override
	public ArrayList<Splitable> getAllAreas() {
		return areas;
	}

	@Override
	public SplitPlan split(double[] p2) {
		if (! isPosIn(p2))
			return null;
		
		SplitPlan ssp = null;
		Splitable s1 = null, s2 = null; // The two areas which contains the two points
		Iterator<Splitable> it = areas.iterator();
		while (((s1 == null) || (s2 == null)) && it.hasNext()) {
			Splitable area = it.next();
			if (area.isPosIn(p))
				s1 = area;
			if (area.isPosIn(p2))
				s2 = area;
		}
		
		// Dbg.verbose(s1.toString());
		// Dbg.verbose(s2.toString());
		
		
		if (s1 == s2) { // In the same area, need to split area and then re-organize
			Dbg.verbose("The two points are in the same area.");
			ssp  = s1.split(p, p2);
		} else { // Not in the same area, just simply put the two areas into two different group.
			Dbg.verbose("The two points are in the different areas.");
			ssp = new  SplitPlan();
			ssp.addSplitable(0,  s1);
			ssp.addSplitable(1,  s2);
		}
		
		Dbg.verbose(ssp.toString());
		
		// Then add other areas into the plan, just try to balance.
		while (Math.abs(capacity - ssp.getCapacity(0) - ssp.getCapacity(1)) > 1e-6) { // Greedy algorithm
			for(Splitable area: areas) {
				
				if (ssp.contains(area))
					continue;
				
				if (ssp.isNeighbor(0, area) && ssp.isNeighbor(1, area)) { // If it's neighbor of both areas
			        if (Math.abs(ssp.getCapacity(1) + area.getCapacity() - ssp.getCapacity(0))
			                > Math.abs(ssp.getCapacity(0) + area.getCapacity() - ssp.getCapacity(1))) {
			            // Delta 1 > Delta 0, to balance, the remained should be added to part 0
			            ssp.addSplitable(0, area);
			        } else {
			            ssp.addSplitable(1, area);
			        }
				} else if (ssp.isNeighbor(0, area)) {
					ssp.addSplitable(0, area);
				} else if (ssp.isNeighbor(1, area)) {
					ssp.addSplitable(1, area);
				}
			}
		}
		
		return ssp;
	}

	@Override
	public void addArea(Splitable s) {
		areas.add(s);
		s.setNode(this);
		capacity += s.getCapacity();
	}

	
	@Override
	public void setAreas(ArrayList<Splitable> s) {
		areas = s;
		// Set node of each area and recalculate the capacity
		capacity = 0.0D;
		Iterator<Splitable> it = areas.iterator();
		while (it.hasNext()) {
			Splitable area = it.next();
			capacity += area.getCapacity();
			area.setNode(this);
		}
	}
	

	@Override
	public boolean isNeighbor(Splitable s) {
		if (areas.contains(s))
			return false; // An area belong to this node should not be treated as a neighbor.
		
		for (Splitable area: areas) {
			if (s.isNeighbor(area))
				return true;
		}
		return false;
	}

	
	@Override
	public boolean isNeighbor(Node n) {
		if (n == this)
			return false; // No one can be neighbor of itself.
		
		for (Splitable area: areas) {
			if (n.isNeighbor(area))
				return true;
		}
		return false;
	}
	

	@Override
	public double getCapacity() {
		return capacity;
	}

	
	@Override
	public double[] getKeyPos() {
		return p;
	}

	
	@Override
	public void setKeyPos(double[] pos) {
		p = pos;
	}

	
	@Override
	public boolean isPosIn(double[] pos) {
		Iterator<Splitable> it = areas.iterator();
		while (it.hasNext()) {
			if (it.next().isPosIn(pos))
				return true;
		}
		return false;
	}

	
	@Override
	public boolean isKeyPos(double[] pos) {
		int i = 0;
		boolean match = true;
		while (match & (i < pos.length)) {
			match = (Math.abs(pos[i] - p[i]) < 1e-6);
			i++;
		}
		return match;
	}

	
	@Override
	public void addItem(Item i) {
		items.add(i);
		i.setNode(this);
	}

	@Override
	public ArrayList<Item> getItems() {
		return items;
	}


	public boolean isSplited() {
		return splited;
	}

	
	public void setSplited(boolean splited) {
		this.splited = splited;
	}

	
	public Node getSplitFrom() {
		return splitFrom;
	}

	
	public void setSplitFrom(Node splitFrom) {
		this.splitFrom = splitFrom;
	}
	
	
	public String toString() {
		StringBuffer sb = new StringBuffer("Node: (" + p[0]);
        for (int i = 1; i < p.length; i++) {
            sb.append(", ").append(p[i]);
        }
        sb.append(")     Capacity: ").append(capacity).append("\n");
        
        int i = 1;
        for (Splitable s: areas) {
        	sb.append("\t Area ").append(i++).append(": ").append(s.toString()).append("\n");
        }
        
        i = 1;
        for (Item s: items) {
        	sb.append("\t Item ").append(i++).append(": ").append(s.toString()).append("\n");
        }
        
		return sb.toString();
	}


	double[] p = null;	// The key position
	ArrayList<Splitable> areas = new ArrayList<Splitable>();	// The areas in this node
	double capacity = 0.0D;	// The capacity of this node (the sum of capacity of all areas)
	ArrayList<Item> items = new ArrayList<Item>();	// The items in this node
	boolean splited = false;	// Whether current node has been split
	Node splitFrom = null;		// The node from which current node is split from
}
