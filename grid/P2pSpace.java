public class P2pSpace implements Splitable, Cloneable {

    @Override
    public void setVertex(double[] p1, double[] p2) {
        if (p1 == null || p2 == null)
            return;
        
        this.vertex1 = new double[p1.length];
        this.vertex2 = new double[p1.length];
        
        // Make all the coordination of Vertex 2 is bigger than that of Vertex 1.
        for (int i = 0; i < vertex1.length; i++) {
            if (p1[i] < p2[i]) {
                vertex1[i] = p1[i];
                vertex2[i] = p2[i];
            } else {
                vertex1[i] = p2[i];
                vertex2[i] = p1[i];
            }
        }
        calculateCapacity();
    }
    
    
    private void calculateCapacity() {
        capacity = vertex2[0] - vertex1[0];
        for (int i = 1; i < vertex1.length; i++) {
            capacity *= vertex2[i] - vertex1[i];
        }
    }
    
    @Override
    public double[] getVertex1() {
        return vertex1;
    }
    
    @Override
    public double[] getVertex2() {
        return vertex2;
    }

    @Override
    public double getCapacity() {
        return capacity;
    }

    
    //@Override
    public int testMerge(Splitable s2) {
        double[] vp1 = s2.getVertex1();
        double[] vp2 = s2.getVertex2();
        
/*         The condition that two areas are neighbors is:
 *         1. One side are the same: (vertex1[k] = vp2[k] or vertex2[k] = vp1[k])
 *         2. The other sides overlapped: (vertex1[j] = vp1[j] and vertex2[j] = vp2[j]), for all j!=k.
 *         3. The two areas should be identical in size
 *         4. The two areas should merge into the same style how they were divided. 
 */
        int k = 0, j;
        while (k < vertex1.length) {
            // Test criteria 1
            boolean result = ((Math.abs(vertex1[k] - vp2[k]) < 1e-6) || (Math.abs(vertex2[k] - vp1[k]) < 1e-6));
            // Test criteria 2.
            j = 0;
            while (result && (j < vertex1.length)) {
                if (j != k) {
                    result = ((Math.abs(vertex1[j] - vp1[j]) < 1e-6) && (Math.abs(vertex2[j] - vp2[j]) < 1e-6));
                }
                j++;
            }
            
            // Test criteria 3
            if (result) {
            	result = Math.abs((vertex2[k] - vertex1[k]) - (vp2[k] - vp1[k])) < 1e-6;
            }
            
            // Test criteria 4
            if (result) {
            	if (Math.abs(vertex1[k] - vp2[k]) < 1e-6)
            		result = getCuts(vp1[k]) < getCuts(vp2[k]);
            	else if (Math.abs(vertex2[k] - vp1[k]) < 1e-6)
            		result = getCuts(vp1[k]) > getCuts(vp2[k]);
            }
            
            if (result)
            	return k;
            
            k++;
        }
        
        return -1;
    }
    
    
    // Return how many cuts applied on this side.
    private int getCuts(double d) {
    	int i = 0;
    	int n = (int)d;
    	
    	while (Math.abs(n - d) > 1e-6) {
    		d = d * 2;
    		n = (int)d;
    	}
    	
    	return i;
    }
    

    @Override
    public SplitPlan split(double[] p1, double[] p2) {
        boolean[] canSplit = new boolean[p1.length];
        double[] dist = new double[p1.length];
        
        for (int i = 0; i < p1.length; i++) {
            dist[i] = vertex2[i] - vertex1[i];
        }
        
        for (int i = 0; i < p1.length; i++) {
            int j = 0;
            // Firstly, check whether the distance in this dimension can be split into half.
            // If in that dimension the distance is equal or less than half of other dimension,
            // the area can not be divided from that dimension.
            canSplit[i] = true;
            while (j < p1.length) {
                if ((i != j) && (Math.abs(dist[i] - dist[j] / 2) < 1e-6)) {
                    canSplit[i] = false;
                    break;
                }
                j++;
            }
            // If can split, then try to split and find whether the two points can be located into
            // the two equal areas.
            if (canSplit[i]) {
                double middle_i = vertex1[i] + dist[i] / 2; // The middle point in the i-th dimension
                P2pSpace s1 = null;
                P2pSpace s2 = null;
                // Try to check whether the two points are separated into two evenly divided areas.
                // If yes, then split this area.
                if ((p1[i] < middle_i) && (p2[i] >= middle_i)) {
                    s1 = (P2pSpace)(this.clone());
                    s2 = (P2pSpace)(this.clone());
                    s1.update(1, i, middle_i);
                    s2.update(0, i, middle_i);
                } else if ((p2[i] < middle_i) && (p1[i] >= middle_i)) {
                    s1 = (P2pSpace)(this.clone());
                    s2 = (P2pSpace)(this.clone());
                    s2.update(1, i, middle_i);
                    s1.update(0, i, middle_i);
                }
                // If can split, then return the split plan.
                if (s1 != null) {
                    SplitPlan ssp = new SplitPlan();
                    ssp.addSplitable(0, s1);
                    ssp.addSplitable(1, s2);
                    return ssp;
                }
            }
        } // End of For, the direct split.
        
        // If cannot split directly, then try to force to split the whole into half from the first
        // divide-able dimension, and then try to recursively call this method on the area that the 
        // two points are located.
        int i = 0;
        while (!canSplit[i]) i++;         // Find the first splitable dimension.
        double middle_i = vertex1[i] + dist[i] / 2; // The middle point in the i-th dimension
        P2pSpace remained = (P2pSpace)(this.clone());    // The remained sub space
        P2pSpace ss = (P2pSpace)(this.clone());        // The sub space to be split again 
        
        if (p1[i] < middle_i) { //   p2[i] < middle_i, too
            remained.update(0, i, middle_i);
            ss.update(1, i, middle_i);
        } else {
            remained.update(1, i, middle_i);
            ss.update(0, i, middle_i);
        }
        
        // Recursively do the split on ss, and then try to merge remained part into the split plan
        // and return as the final split plan.
        SplitPlan ssp = ss.split(p1, p2);
        // Try to add the remained into the ssp. Just try to balance the capacities withint each part.
		if (ssp.isNeighbor(0, remained) && ssp.isNeighbor(1, remained)) { // If it's neighbor of both areas
	        if (Math.abs(ssp.getCapacity(1) + remained.getCapacity() - ssp.getCapacity(0))
	                > Math.abs(ssp.getCapacity(0) + remained.getCapacity() - ssp.getCapacity(1))) {
	            // Delta 1 > Delta 0, to balance, the remained should be added to part 0
	            ssp.addSplitable(0, remained);
	        } else {
	            ssp.addSplitable(1, remained);
	        }
		} else if (ssp.isNeighbor(0, remained)) {
			ssp.addSplitable(0, remained);
		} else if (ssp.isNeighbor(1, remained)) {
			ssp.addSplitable(1, remained);
		}

		return ssp;
    }

    
    @Override
    public boolean isNeighbor(Splitable s2) {
    	if (s2 == this) // No one can be neighbor of itself.
    		return false;
    	
        double[] vp1 = s2.getVertex1();
        double[] vp2 = s2.getVertex2();
        
/*         The condition that two areas are neighbors is:
 *         One side are the same: (vertex1[k] = vp2[k] or vertex2[k] = vp1[k])
 *         The other sides overlapped: (vertex1[j] < vp2[j] and vertex2[j] = vp1[j]), for all j!=k.
 */
        int k = 0, j;
        while (k < vertex1.length) {
            boolean result = ((Math.abs(vertex1[k] - vp2[k]) < 1e-6) || (Math.abs(vertex2[k] - vp1[k]) < 1e-6));
            j = 0;
            
            while (result && (j < vertex1.length)) {
                if (j != k) {
                    result = ((vertex1[j] < vp2[j]) && (vertex2[j] > vp1[j]));
                }
                j++;
            }
            if (result)
                return true;
            
            k++;
        }
        return false;
    }

    
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("Capacity: ");
        sb.append(capacity).append("\tLower point(");
        sb.append(vertex1[0]);
        for (int i = 1; i < vertex1.length; i++) {
            sb.append(", ").append(vertex1[i]);
        }
        sb.append("); Higher point(").append(vertex2[0]);
        for (int i = 1; i < vertex1.length; i++) {
            sb.append(", ").append(vertex2[i]);
        }
        sb.append(")");
        return sb.toString();
    }
    
    
    @Override
    public void update(int v, int index, double value) {
        if (v == 0) {
            vertex1[index] = value;
        } else {
            vertex2[index] = value;
        }
        calculateCapacity();
    }
    
    
    @Override
    public Object clone() {
        P2pSpace s = new P2pSpace();
        double[] p1 = new double[vertex1.length];
        double[] p2 = new double[vertex1.length];
        
        for (int i = 0; i < vertex1.length; i++) {
            p1[i] = vertex1[i];
            p2[i] = vertex2[i];
        }
        s.setVertex(p1, p2);
        return s;
    }

    
    @Override
    public Node getNode() {
    	return n;
    }

    
    @Override
    public void setNode(Node n) {
    	this.n = n;
    }
    
    
    @Override
    public boolean isPosIn(double[] pos) {
    	for (int i = 0; i < pos.length; i++) {
    		if ((pos[i] < vertex1[i]) || (pos[i] >= vertex2[i]))
    			return false;
    	}
    	return true;
    }


	double[] vertex1 = null;
    double[] vertex2 = null;
    double capacity = -1.0D;
    Node n = null;
}
