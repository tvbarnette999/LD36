package TnT.ld.ld36;

public class Transport {
	
	//given no upgrades, on a path of the same length, how many should each unit be able to
	//take per unit time?
	public static Transport RUNNER = new Transport(1);
	public static Transport HORSE = new Transport(10);
	public static Transport CARRIAGE = new Transport(100);
	public static Transport CARAVAN = new Transport(1000);
	public static Transport CATAPAULT = new Transport(1000);
	public static Transport CAR = new Transport(10000);
	public static Transport TRUCK = new Transport(100000);
	public static Transport SEMI = new Transport(1000000);
	public static Transport PLANE = new Transport(10000000);
	public static Transport JET = new Transport(100000000);
	public static Transport VACUUM = new Transport(10000);
	
	public static double CATAPAULT_RANGE = 20;
	
	public double scalar;
	private Transport(double s){
		scalar = s;
	}
	
	
}
