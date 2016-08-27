package TnT.ld.ld36;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Map {
	public static final int MAP_WIDTH = 1000;
	public static final int MAP_HEIGHT = 1000;
	/**Ratio of width:height of hexagon*/
	public static final double RATIO = 1.154700538;
	/**The height of a tile at max zoom*/
	public static final double MAX_HEIGHT = 100;
	public static final double MAX_WIDTH = (MAX_HEIGHT * RATIO);
	
	//Alternatively, could use first 4 bits to specify city ID, but then limits to 16 cities
	//to use, just and with the bit you want and check > 0
	public static final byte CITY_BIT = 1;
	public static final byte FOOT_PATH_BIT = 2;
	public static final byte DIRT_ROAD_BIT = 4;
	public static final byte PAVED_ROAD_BIT = 8;
	public static final byte TRACK_BIT = 16;

	
	public byte[][] data = new byte[MAP_WIDTH][MAP_HEIGHT]; //this array is done [x][y] to simplify.
	public ArrayList<Sprite> sprites = new ArrayList<Sprite>();
	public void mouseClicked(MouseEvent e){
		
	}
	public void mouseDragged(MouseEvent e){
		
	}
	public static Map generate(){
		Map m = new Map();		
		m.data[3][2] |= CITY_BIT;
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
	/**
	 * 
	 * passed the dimension of the graphics object's container.
	 * 
	 */
	public void draw(Graphics2D g, int width, int height){
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
					
					y+=y1;
					x+=x2;
					xi++;
				}
				y-=y1*2;
			}
			xi = 0;
			yi++;
		}
	}
}
