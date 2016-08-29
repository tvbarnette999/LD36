package TnT.ld.ld36;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

public class City extends Point {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5067627801527250470L;
	int ID;
	String name; // preload a bunch of rando city names
	double population = 1; // double?
	static double literacy = .1;
	boolean airport = false;
	int catapults = 0;

	// divide transport.scalar by path length for #/time
	public City(String name) {
		this.name = name;
	}

	ArrayList<Path[]> paths = new ArrayList<Path[]>();
	ArrayList<java.lang.Double> rateCapacity = new ArrayList<java.lang.Double>();
	ArrayList<java.lang.Double> desiredRate = new ArrayList<java.lang.Double>();
	private static String[] NAMES = new String[] { "Athens", "Sparta", "Corinth", "Thebes", "Eretria", "Chalcis",
			"Syracuse", "Massalia", "Rome", "Pompeii", "Marathon", "Cumae", "Antium", "Babylon", "Teotihuacan",
			"Calixtlahuaca", "Carthage", "Petra", "Tehran" };
	private static ArrayList<String> names = new ArrayList<String>();
	Random r = new Random();

	public City() {
		if (names.size() == 0) {
			names.addAll(Arrays.asList(NAMES));
		}
		this.name = names.remove(r.nextInt(names.size()));
	}

	public void addCity() {
		paths.add(new Path[Transport.baseUnits.length]);
		rateCapacity.add(0d);
		desiredRate.add(0d);
	}

	public double getMail() {
		double tot = 0;
		Iterator<java.lang.Double> cap = rateCapacity.iterator();
		Iterator<java.lang.Double> des = desiredRate.iterator();
		while (cap.hasNext()) {
			tot += Math.min(cap.next(), des.next());
		}
		return tot;
	}

	public String printPaths() {
		String s = "";
		for (int i = 0; i < paths.size(); i++) {
			s += i + ": " + Arrays.toString(paths.get(i)) + '\n';
		}
		return s.trim();
	}

	public Point2D.Double p() {
		return new Point2D.Double(x, y);
	}
}