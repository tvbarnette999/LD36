package TnT.ld.ld36;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Map {
	public static Image test;
	static{
		try {
			test = Resources.getImage("soldier.png");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static final int MAP_WIDTH = 1000;
	public static final int MAP_HEIGHT = 1000;
	/**Ratio of width:height of hexagon*/
	public static final double RATIO = 1.154700538;
	/**The height of a tile at max zoom*/
	public static final double MAX_HEIGHT = 100;
	public static final double MAX_WIDTH = (MAX_HEIGHT * RATIO);
	
	//Alternatively, could use first 4 bits to specify city ID, but then limits to 16 cities
	//to use, just and with the bit you want and check > 0
	//If cities have everything by default, then just OR each of these with CITY_BIT
	public static final byte CITY_BIT = 1;
	public static final byte FOOT_PATH_BIT = 2;
	public static final byte DIRT_ROAD_BIT = 4;
	public static final byte PAVED_ROAD_BIT = 8;
	public static final byte TRACK_BIT = 16;
	//bit for impassable?

	
	public byte[][] data = new byte[MAP_WIDTH][MAP_HEIGHT]; //this array is done [x][y] to simplify.
	public ArrayList<Sprite> sprites = new ArrayList<Sprite>();
	public void mouseClicked(MouseEvent e){
		System.out.println("map click");
	}
	Point mouseLoc = null;
	public void mousePressed(MouseEvent e){
		System.out.println("map press");
		mouseLoc = e.getPoint();
	}
	public void mouseDragged(MouseEvent e) {
		transX -= mouseLoc.x - e.getX();
		transY -= mouseLoc.y - e.getY();
		
		mouseLoc = e.getPoint();
	}
	public void mouseWheelMoved(MouseWheelEvent e) {
		System.out.println("map scroll");
		System.out.println(e.getPoint());
		double oz = zoom;
		zoom *= Math.pow(1.125, e.getPreciseWheelRotation());
		transX += e.getX() * (oz/zoom - 1);
		transY += e.getY() * (oz/zoom - 1);
	}
	public static Map generate(){
		Map m = new Map();		
		m.setTile(3,2,CITY_BIT, true);//m.data[3][2] |= CITY_BIT;
		m.data[7][6] |= CITY_BIT;
		m.data[11][3] |= CITY_BIT;
		return m;
	}
	public boolean hasCity(int x, int y){
		return (data[x][y] & CITY_BIT) > 0;
	}
	private int round(double d){
		return (int) Math.round(d);
	}
	private void drawLine(Graphics2D g, double x1, double y1, double x2, double y2){
		g.drawLine(round(x1), round(y1), round(x2), round(y2));
	}
	private boolean canUse(int x, int y, Transport t){
		return (data[x][y] & t.getBits()) > 0;
	}
	public void findPath(int x1, int y1, int x2, int y2, Transport type){
		//TODO use canUse method above for pathfinder. pass it transport type.
	}
	public void setTile(int xi, int yi, byte bit, boolean v){
		if(v)data[xi][yi] |= bit;
		else data[xi][yi] &= ~bit;
	}
	public void setTile(Point p, byte bit , boolean v){
		int xi = 0;
		int yi = 0;
		if(v)data[xi][yi] |= bit;
		else data[xi][yi] &= ~bit;
	}
	
	double zoom = 1;
	double transX = 0, transY = 0;
	/**
	 * 
	 * passed the dimension of the graphics object's container.
	 * 
	 */
	public void draw(Graphics2D g, int width, int height){
		final double zoom = this.zoom, transX = this.transX, transY = this.transY;
		g.translate(transX, transY);
		g.scale(zoom, zoom);
		
		g.setColor(Color.BLACK);
		final double x1 = MAX_WIDTH/4.0;
		final double x2 = 3 * x1;
		final double y1 = MAX_HEIGHT / 2.0;
		//TODO do this properly.
		int xi = 0;
		int yi = 0;
		for(double y = 0; y < height; y+=MAX_HEIGHT){
			for(double x = 0; x < width; ){
//				g.drawRect(x, y, MAX_WIDTH, MAX_HEIGHT);
				for(int i = 0; i < 2; i++){
					drawLine(g, x+x1, y, x+x2, y);
					drawLine(g, x+x2, y, x+MAX_WIDTH, y+y1);
					drawLine(g, x+MAX_WIDTH, y+y1, x+x2, y+MAX_HEIGHT);
					drawLine(g, x+x1,  y+MAX_HEIGHT, x+x2, y+MAX_HEIGHT);
					drawLine(g, x, y+y1, x+x1, y+MAX_HEIGHT);
					drawLine(g, x,y+ y1, x+x1, y);
					g.drawString(xi+","+yi, (int)(x+x1),(int) (y+y1));
					if(hasCity(xi,yi)){
						g.setColor(Color.RED);
						g.fillRect((int)(x+x1),(int)(y+ y1+10),20, 20);
						g.setColor(Color.BLACK);
					}
					g.drawImage(test,(int)( x+x1+10),(int)( y+5),null);
					//to display diff path types, just & with bits
					
					y+=y1;
					x+=x2;
					xi++;
				}
				y-=y1*2;
			}
			xi = 0;
			yi++;
		}
		
		g.scale(1/zoom, 1/zoom);
		g.translate(-transX, -transY);
	}
}
