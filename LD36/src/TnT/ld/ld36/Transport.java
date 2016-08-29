package TnT.ld.ld36;

import java.awt.image.BufferedImage;

public class Transport {
	
	//given no upgrades, on a path of the same length, how many should each unit be able to
	//take per unit time?
	public static Transport RUNNER = new Transport("Runner", 2d, 0x1F);
	public static Transport HORSE = new Transport("Horse", 15, Map.FOOT_PATH_BIT|Map.DIRT_ROAD_BIT | Map.PAVED_ROAD_BIT);;
	public static Transport CARRIAGE = new Transport("Carriage", 500, Map.DIRT_ROAD_BIT | Map.PAVED_ROAD_BIT);
	public static Transport CARAVAN = new Transport("Caravan", 2000, Map.DIRT_ROAD_BIT | Map.PAVED_ROAD_BIT);
	public static Transport CATAPAULT = new Transport("Catapult", 2500, Map.CITY_BIT);
	public static Transport CAR = new Transport("Car", 40000, Map.PAVED_ROAD_BIT);
	public static Transport TRUCK = new Transport("Truck",500000 , Map.PAVED_ROAD_BIT);
	public static Transport SEMI = new Transport("Semi", 1000000, Map.PAVED_ROAD_BIT);
	public static Transport PLANE = new Transport("Airplane", 99000000, Map.CITY_BIT);
	public static Transport JET = new Transport("Jumbo Jet", 990000000, Map.CITY_BIT);
	public static Transport VACUUM = new Transport("Vacuum Tube", 10000, 0x1F);//add vaccuum tube? 
	public static Transport TRAIN = new Transport("Choo Choo", 1000000, Map.TRACK_BIT);
	
	public static Transport CATAPAULT_RANGE = new Transport("Catapult Range", 20, Map.CITY_BIT);
	
	// units used for pathfinding
	public static final Transport[] baseUnits = {RUNNER, HORSE, CARRIAGE, CAR, TRAIN};
	public static Transport[] currentUnits = {RUNNER, null, null, null, null, null, null};
	
	public static BufferedImage[] images = {Resources.getImage("footprint.png"), Resources.getImage("horse.png"), Resources.getImage("carriage.png"), Resources.getImage("car.png"), Resources.getImage("truck.png"), Resources.getImage("train.png") };
	
	public static final int RUNNER_TYPE = 0;
	public static final int HORSE_TYPE = 1;
	public static final int CARRIAGE_TYPE = 2;
	public static final int CAR_TYPE = 3;
	public static final int TRAIN_TYPE = 4;
	public static final int CATAPULT_TYPE = 5;
	public static final int PLANE_TYPE = 6;
	
	
	public double scalar;
	public byte bits;
	String name;
	private Transport(String name, double s, int bits){
		scalar = s;
		this.bits = (byte) bits;
		this.name = name;
	}
	public byte getBits(){
		return bits;
	}
	public String toString() {
		return name;
	}
	public static String[] debug(){
		String[] s = new String[currentUnits.length];
		for(int i =0;i < s.length;i++){
			if(currentUnits[i]!=null)s[i]=""+currentUnits[i].scalar;
		}
		return s;
	}
	
	
}
