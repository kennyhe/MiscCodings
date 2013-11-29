
public class P2pItem implements Item {

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name, Hash hash) {
        this.name = name;
        pos = hash.getHash(name, GlobalSettings.getDimension());
    }

    @Override
    public Node getNode() {
        return node;
    }


	@Override
	public void setNode(Node node) {
		this.node = node;
	}

	
	@Override
	public double[] getOwnerPos() {
		return owner;
	}

	@Override
	public void setOwnerPos(double[] p) {
		owner = p;
	}

	@Override
	public double[] getLocation() {
		return pos;
	}
	
	@Override
	public String toString() {
        StringBuffer sb = new StringBuffer("Owner: (");
        sb.append(owner[0]);
        for (int i = 1; i < owner.length; i++) {
            sb.append(", ").append(owner[i]);
        }
        sb.append("); Position: (").append(pos[0]);
        for (int i = 1; i < pos.length; i++) {
            sb.append(", ").append(pos[i]);
        }
        sb.append(")  Name: \"").append(name).append("\"");
        return sb.toString();
	}

	
	String name = null;
	Node node = null;
	double[] pos = null;
	double[] owner = null;
}
