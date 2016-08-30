package TnT.ld.ld36;

import java.awt.Point;
import java.util.Iterator;
import java.util.LinkedList;

public class Path {
	LinkedList<Point> points = new LinkedList<Point>();
	LinkedList<Point> reversePoints = new LinkedList<Point>();

	// divide transport.scalar by path length for #/time
	public int length() {
		return points.size();
	}

	public String toString() {
		String s = "";
		for (Point p : points)
			s += "(" + p.x + ", " + p.y + ") ";
		return "[" + s.trim() + "]";
	}

	public Point getLast() {
		try {
			return points.getLast();
		} catch (Exception e) {
			return null; // empty list
		}
	}

	public Point getFirst() {
		try {
			return points.getFirst();
		} catch (Exception e) {
			return null; // empty list
		}
	}

	public Iterator<Point> iterator() {
		return points.iterator();
	}

	public Iterator<Point> getBackIt() {
		return reversePoints.iterator();
	}
}
