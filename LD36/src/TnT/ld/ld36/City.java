package TnT.ld.ld36;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;

public class City extends Point{
	int ID;
	String name; //preload a bunch of rando city names
	double population = 1; //double?
	static double literacy = .1;
	
	boolean airport = false;
	//divide transport.scalar by path length for #/time
	public City(String name){
		this.name = name;
	}
	ArrayList<Path[]> paths = new ArrayList<Path[]>();
	ArrayList<java.lang.Double> rateCapacity = new ArrayList<java.lang.Double>();
	ArrayList<java.lang.Double> desiredRate = new ArrayList<java.lang.Double>();
	
	public void addCity() {
		paths.add(new Path[Transport.baseUnits.length]);
		rateCapacity.add(0d);
		desiredRate.add(0d);
	}
	
	public String printPaths() {
		String s = "";
		for (int i = 0; i < paths.size(); i++) {
			s += i + ": " + Arrays.toString(paths.get(i)) + '\n';
		}
		return s.trim();
	}
}
