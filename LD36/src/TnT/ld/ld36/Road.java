package TnT.ld.ld36;

public class Road {
	public static Road FOOTPATH = new Road(1, Map.FOOT_PATH_BIT);
	public static Road DIRT = new Road(100, Map.DIRT_ROAD_BIT);
	public static Road PAVED = new Road(10000, Map.PAVED_ROAD_BIT);
	public static Road RAIL = new Road(1000000, Map.TRACK_BIT);
	public static Road AIRPORT = new Road(1000000, Map.AIRPORT_BIT);
	
	public static Road[] roads = {FOOTPATH, DIRT, PAVED, RAIL};
	static {
		for (int i = 0; i < roads.length; i++)
			roads[i].key = i;
		FOOTPATH.unlocked = true;
	}
	
	public double cost;
	public byte mask;
	public int key;
	public boolean unlocked;
	private Road(double cost, byte mask) {
		this.cost = cost;
		this.mask = mask;
	}
}
