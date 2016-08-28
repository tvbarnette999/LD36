package TnT.ld.ld36;

import java.awt.Point;
import java.awt.geom.Path2D;

public class City extends Point{
	int ID;
	String name; //preload a bunch of rando city names
	int population = 1; //double?
	boolean airport = false;
	//divide transport.scalar by path length for #/time
	public City(String name){
		this.name = name;
	}
	
	
	

}
