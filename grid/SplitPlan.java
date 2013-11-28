import java.util.ArrayList;

/**
 * This class stores the split plan of ** Splitable ** object.
 * The current Splitable will be splited into several parts, some of them will belong to 
 * the point 1, some of them will belong to the point 2.
 * @author kenny
 *
 */
public class SplitPlan {
    
    SplitPlan() {
        part1 = new ArrayList<Splitable>();
        part2 = new ArrayList<Splitable>();
        capacity1 = 0.0D;
        capacity2 = 0.0D;
    }

    public ArrayList<Splitable> getPart(int part) {
        if (part == 0) 
            return part1;
        else
            return part2;
    }

    public void addSplitable(int part, Splitable s) {
        if (part == 0) {
            part1.add(s);
            capacity1 += s.getCapacity();
        } else {
            part2.add(s);
            capacity2 += s.getCapacity();
        }
    }
    
    public double getCapacity(int part) {
        if (part == 0) 
            return capacity1;
        else
            return capacity2;
    }
    
    
    /**
     * Check whether the area has already been in the plan.
     * @param s The area to be checked
     * @return True if it has been there, else false.
     */
    public boolean contains(Splitable s) {
         
         if (part1.contains(s) || part2.contains(s))
        	 return true;
         
         return false;
    }
    
    
    /**
     * Check whether the area is neighbor to either group of the plan.
     * @param part The group number
     * @param s The area to be checked
     * @return True if they are neighbor, else false.
     */
    public boolean isNeighbor(int part, Splitable s) {
    	 if (contains(s))
    		 return false;
    	 
         ArrayList<Splitable> al;
         if (part == 0)
        	 al = part1;
         else
        	 al = part2;

         for (Splitable a: al) {
        	 if (a.isNeighbor(s)) 
        		 return true;
         }
         
         return false;
    }
    
    
    public String toString() {
    	 StringBuffer sb = new StringBuffer("Split Plan:\n\tPart1: Capacity = ");
    	 sb.append(capacity1).append("\n");
    	 for (Splitable s:part1) {
    		 sb.append("\t\t").append(s.toString()).append("\n");
    	 }
    	 
    	 sb.append("\tPart2: Capacity = ");
    	 sb.append(capacity2).append("\n");
    	 for (Splitable s:part2) {
    		 sb.append("\t\t").append(s.toString()).append("\n");
    	 }
    	 
    	 return sb.toString();
    }


    ArrayList<Splitable>    part1, part2;
    double capacity1, capacity2;
}
