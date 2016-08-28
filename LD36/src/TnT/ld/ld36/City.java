package TnT.ld.ld36;

import java.awt.Point;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Arrays;

public class City extends Point{
	int ID;
	String name; //preload a bunch of rando city names
	int population = 1; //double?
	boolean airport = false;
	int x, y;
	//divide transport.scalar by path length for #/time
	public City(String name){
		this.name = name;
	}
	ArrayList<Path[]> paths = new ArrayList<Path[]>();
	ArrayList<Double> rateCapacity = new ArrayList<Double>();
	ArrayList<Double> desiredRate = new ArrayList<Double>();
	
	public void addCity() {
		paths.add(new Path[Transport.baseUnits.length]);
	}
	
	public String printPaths() {
		String s = "";
		for (int i = 0; i < paths.size(); i++) {
			s += i + ": " + Arrays.toString(paths.get(i)) + '\n';
		}
		return s.trim();
	}
}
