package TnT.ld.ld36;
import java.awt.Point;
import java.util.ArrayList;
public class Path {
	ArrayList<Point> points = new ArrayList<Point>();
	//divide transport.scalar by path length for #/time
	public int length(){
		return points.size();
	}
}
