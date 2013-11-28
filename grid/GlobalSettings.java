
public class GlobalSettings {
    static GlobalSettings gs = null;
    
    static public GlobalSettings getInstance() {
        init();
        return gs;
    }
    
    
    static public int getDimension() {
        init();
        return gs.dimension;
    }

    static public void setDimension(int dimension) {
        init();
        gs.dimension = dimension;
    }

    static private void init() {
        if (gs == null)
            gs = new GlobalSettings();
    }
    
    private GlobalSettings() {
        // Init default values in the default constructor.
        dimension = 2;
    }
    
    //members
    int dimension;

}
