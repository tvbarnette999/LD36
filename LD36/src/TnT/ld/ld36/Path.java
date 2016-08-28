package TnT.ld.ld36;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
public class Path {
	LinkedList<Point> points = new LinkedList<Point>();
	//divide transport.scalar by path length for #/time
	public int length(){
		return points.size();
	}
	public String toString() {
		String s = "";
		for (Point p : points)
			s += "("+p.x+", "+p.y+") ";
		return "[" + s.trim() + "]";
	}
}
