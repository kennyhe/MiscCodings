public class MaxPointsOnLine {
    java.util.ArrayList<Point> pa = new java.util.ArrayList<Point>();
    java.util.HashMap<Point, Integer> dups = new java.util.HashMap<Point, Integer>();

    public int maxPoints(Point[] points) {
        pa.clear();
        dups.clear();
        java.util.ArrayList<Intersect> intersects = new java.util.ArrayList<Intersect>();

        if (points.length <= 0)
            return 0;
        
        if (points.length == 1)
            return 1;

        // Clear duplicates
        boolean[] isDup = new boolean[points.length];
        pa.add(points[0]);
        dups.put(points[0], 1);
        for (int j = 1; j < points.length; j++) {
            for (int i = 0; i < j; i++) {
                if (! isDup[i]) {
                    Point p1 = points[i], p2 = points[j];
                    if (p1.x == p2.x && p1.y == p2.y) {
                        Integer x = dups.get(p1);
                        dups.put(p1, x + 1);
                        isDup[j] = true;
                        break;
                    }
                }
            }
            if (!isDup[j]) {
                pa.add(points[j]);
                dups.put(points[j], 1);
            }
        }
        
        if (pa.size() == 1) {
            return dups.get(pa.get(0));
        }

        // Check points with lines
        intersects.add(new Intersect(pa.get(0), pa.get(1), 0, 1));
        for (int i = 2; i < pa.size(); i++) {
            Point p = pa.get(i);
            boolean[] lined = new boolean[i];
            
            for (Intersect sect: intersects) {
                if (sect.sameLine(p)) {
                    for (int idx: sect.indice)
                        lined[idx] = true;
                    sect.add(p, i);
                }
            }
            
            for (int j = 0; j < i; j++)
                if (! lined[j])
                    intersects.add(new Intersect(pa.get(j), pa.get(i), j, i));
        }

        int max = 1, count;
        for (Intersect ij: intersects) {
            count = ij.size();
            if (count > max)
                max = count;
        }
        
        return max;
    }


class Intersect {
    
    Point start;
    int deltaX;
    int deltaY;
    int count; 
    java.util.ArrayList<Point> line = new java.util.ArrayList<Point>();
    java.util.ArrayList<Integer> indice = new java.util.ArrayList<Integer>();
    
    Intersect(Point start, Point end, int i, int j) {
        this.start = start;
        this.deltaX = start.x - end.x;
        this.deltaY  = start.y - end.y;
        line.add(start);
        line.add(end);
        indice.add(i);
        indice.add(j);
        count = dups.get(start);
        count += dups.get(end);
    }
    
    void add(Point p, int i) {
        if (! line.contains(p)) {
            line.add(p);
            indice.add(i);
            count += dups.get(p);
        }
    }
    
    int size() {
        return count;
    }
    
    boolean sameLine(Point p) {
        if ((p.x - start.x) * deltaY == (p.y - start.y) * deltaX)
            return true;
        return false;
    }
}

}
