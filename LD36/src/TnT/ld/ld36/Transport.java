package TnT.ld.ld36;

public class Transport {
	
	//given no upgrades, on a path of the same length, how many should each unit be able to
	//take per unit time?
	public static Transport RUNNER = new Transport(1d, 0x1F);
	public static Transport HORSE = new Transport(10, Map.CITY_BIT | Map.DIRT_ROAD_BIT | Map.PAVED_ROAD_BIT);;
	public static Transport CARRIAGE = new Transport(100, Map.DIRT_ROAD_BIT | Map.PAVED_ROAD_BIT);
	public static Transport CARAVAN = new Transport(1000, Map.DIRT_ROAD_BIT | Map.PAVED_ROAD_BIT);
	public static Transport CATAPAULT = new Transport(1000, Map.CITY_BIT);
	public static Transport CAR = new Transport(10000, Map.PAVED_ROAD_BIT);
	public static Transport TRUCK = new Transport(100000, Map.PAVED_ROAD_BIT);
	public static Transport SEMI = new Transport(1000000, Map.PAVED_ROAD_BIT);
	public static Transport PLANE = new Transport(10000000, Map.CITY_BIT);
	public static Transport JET = new Transport(100000000, Map.CITY_BIT);
	public static Transport VACUUM = new Transport(10000, 0x1F);//add vaccuum tube? 
	
	public static Transport CATAPAULT_RANGE = new Transport(20, Map.CITY_BIT);
	
	public double scalar;
	public byte bits;
	private Transport(double s, int bits){
		scalar = s;
		this.bits = (byte) bits;
		
	}
	public byte getBits(){
		return bits;
	}
	
	
}
