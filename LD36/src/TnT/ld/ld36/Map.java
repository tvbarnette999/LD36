package TnT.ld.ld36;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Path2D.Double;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class Map {
	public static Image grass;
	public static Image rock;
	public static Image tents;
	static{
		try {
			grass = Resources.getImage("grass.png");
			rock = Resources.getImage("mountainpeak.png");
			tents = Resources.getImage("tents.png");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static final int MAP_WIDTH = 100;
	public static final int MAP_HEIGHT = 100;
	/**Ratio of width:height of hexagon*/
	public static final double RATIO = 1.154700538;
	/**The height of a tile at max zoom*/
	public static final double MAX_HEIGHT = 100;
	public static final double MAX_WIDTH = (MAX_HEIGHT * RATIO);
	
	// size of the map section on the screen
	public static final int FRAME_WIDTH = 1280 - Overlay.RIGHT_WIDTH;
	public static final int FRAME_HEIGHT = 768 - Overlay.BOTTOM_HEIGHT;
	// total pixel size of the map
	public static final double MAP_PIXEL_WIDTH = MAX_WIDTH * (.75*MAP_WIDTH + .25);
	public static final double MAP_PIXEL_HEIGHT = MAX_HEIGHT * (MAP_HEIGHT + .5);
	
	//Alternatively, could use first 4 bits to specify city ID, but then limits to 16 cities
	//to use, just and with the bit you want and check > 0
	//If cities have everything by default, then just OR each of these with CITY_BIT
	public static final byte CITY_BIT = 1;
	public static final byte FOOT_PATH_BIT = 2;
	public static final byte DIRT_ROAD_BIT = 4;
	public static final byte PAVED_ROAD_BIT = 8;
	public static final byte TRACK_BIT = 16;
	public static final byte IMPASS_BIT = 32;
	
	Vector<Point> selection = new Vector<Point>();
	int[] selectAdd = new int[Road.roads.length];
	int[] selectRemove = new int[Road.roads.length];
	
	// make tile poly
	static Path2D.Double tilePoly;
	static AffineTransform tDown = AffineTransform.getTranslateInstance(0, MAX_HEIGHT);
	static {
		tilePoly = new Path2D.Double();
		tilePoly.moveTo(0, MAX_HEIGHT/2);
		tilePoly.lineTo(MAX_WIDTH/4, 0);
		tilePoly.lineTo(MAX_WIDTH*3/4, 0);
		tilePoly.lineTo(MAX_WIDTH, MAX_HEIGHT/2);
		tilePoly.lineTo(MAX_WIDTH*3/4, MAX_HEIGHT);
		tilePoly.lineTo(MAX_WIDTH/4, MAX_HEIGHT);
		tilePoly.closePath();
	}
	static Path2D.Double insetPoly;
	static {
		insetPoly = new Path2D.Double();
		insetPoly.moveTo(0, MAX_HEIGHT/2);
		insetPoly.lineTo(MAX_WIDTH/4, 0);
		insetPoly.lineTo(MAX_WIDTH*3/4, 0);
		insetPoly.lineTo(MAX_WIDTH, MAX_HEIGHT/2);
		insetPoly.lineTo(MAX_WIDTH*3/4, MAX_HEIGHT);
		insetPoly.lineTo(MAX_WIDTH/4, MAX_HEIGHT);
		insetPoly.closePath();
		insetPoly.transform(AffineTransform.getTranslateInstance(-MAX_WIDTH/2, -MAX_HEIGHT/2));
		insetPoly.transform(AffineTransform.getScaleInstance(.85, .85));
		insetPoly.transform(AffineTransform.getTranslateInstance(MAX_WIDTH/2, MAX_HEIGHT/2));
	}

	
	public byte[][] data = new byte[MAP_WIDTH][MAP_HEIGHT]; //this array is done [x][y] to simplify.
	public ArrayList<Sprite> sprites = new ArrayList<Sprite>();
	public void mouseClicked(MouseEvent e){
		Point tile = getContainingTile(transformToMap(e.getX(), e.getY()));
		
	}
	Point mouseLoc = null;
	public void mousePressed(MouseEvent e){
		mouseLoc = e.getPoint();
		mouseDragged(e);
	}
	public void mouseDragged(MouseEvent e) {
		if ((e.getModifiers()&8) != 0) {
			// right click
			transX -= mouseLoc.x - e.getX();
			transY -= mouseLoc.y - e.getY();
			restrictScroll();
			mouseLoc = e.getPoint();
		}
		Point tile = getContainingTile(transformToMap(e.getX(), e.getY()));
		if ((e.getModifiers()&16) != 0 && tile.x >= 0 && tile.x < MAP_WIDTH && tile.y >= 0 && tile.y < MAP_HEIGHT) {
			// left click
			// add to selection
			if (!selection.contains(tile)) {
				selection.add(tile);
				byte d = data[tile.x][tile.y];
				// if not city or impassible
				if ((d&(CITY_BIT|IMPASS_BIT))==0) {
					for (Road r : Road.roads) {
						if ((d&r.mask)==0) selectAdd[r.key]++;
						else selectRemove[r.key]++;
					}
				}
			}
		}
		if ((e.getModifiers()&4) != 0 && tile.x >= 0 && tile.x < MAP_WIDTH && tile.y >= 0 && tile.y < MAP_HEIGHT) {
			// right click
			// remove from selection
			if (selection.contains(tile)) {
				selection.remove(tile);
				byte d = data[tile.x][tile.y];
				// if not city or impassible
				if ((d&(CITY_BIT|IMPASS_BIT))==0) {
					for (Road r : Road.roads) {
						if ((d&r.mask)==0) selectAdd[r.key]--;
						else selectRemove[r.key]--;
					}
				}
			}
		}
		System.out.println("selection: "+selection);
		System.out.println("can add: "+Arrays.toString(selectAdd));
		System.out.println("can remove: "+Arrays.toString(selectRemove));
	}
	public void clearSelection() {
		selection.clear();
		for (int i = 0; i < selectAdd.length; i++) {
			selectAdd[i] = 0;
			selectRemove[i] = 0;
		}
	}
	public void mouseWheelMoved(MouseWheelEvent e) {
		double oz = zoom;
		zoom *= Math.pow(1.125, -e.getPreciseWheelRotation());
		zoom = Math.max(MIN_ZOOM, Math.min(1, zoom));
		transX = e.getX() - (e.getX() - transX) * zoom / oz;
		transY = e.getY() - (e.getY() - transY) * zoom / oz;
		restrictScroll();
	}
	public void restrictScroll() {
		transX = Math.min(0, Math.max(FRAME_WIDTH-MAP_PIXEL_WIDTH*zoom, transX));
		transY = Math.min(0, Math.max(FRAME_HEIGHT-MAP_PIXEL_HEIGHT*zoom, transY));
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
	public Point2D.Double getTileLocation(Point p) {
		return getTileLocation(p.x, p.y);
	}
	public Point2D.Double getTileLocation(int x, int y) {
		return new Point2D.Double(x * MAX_WIDTH * .75, (y + ((x&1)==1?.5:0)) * MAX_HEIGHT);
	}
	public Point2D.Double transformToMap(double x, double y) {
		return new Point2D.Double((x-transX)/zoom, (y-transY)/zoom);
	}
	public Point getContainingTile(Point2D.Double p) {
		return getContainingTile(p.x, p.y);
	}
	public Point getContainingTile(double x, double y) {
		if (x / MAX_WIDTH * 4 % 3 >= 1) {
			// middle section
			x /= MAX_WIDTH*.75;
			if (x % 2 >= 1) {
				return new Point((int) x, (int) Math.floor(y/MAX_HEIGHT-.5));
			} else {
				return new Point((int) x, (int) Math.floor(y/MAX_HEIGHT));
			}
		} else {
			// overlap section
			double xm = x % (MAX_WIDTH*.75);
			double ym = y % (MAX_HEIGHT/2);
			int xd = (int) (x/(MAX_WIDTH*.75));
			int yd = (int) (y/MAX_HEIGHT);
			double slope = Math.tan(Math.PI/3);
			if (x / (MAX_WIDTH*.75) % 2 >= 1) {
				if (y / (MAX_HEIGHT/2) % 2 >= 1) {
					if (xm*slope > MAX_HEIGHT/2-ym) return new Point(xd, yd);
					else return new Point(xd-1, yd);
				} else {
					if (xm*slope > ym) return new Point(xd, yd-1);
					else return new Point(xd-1, yd);
				}
			} else {
				if (y / (MAX_HEIGHT/2) % 2 >= 1) {
					if (xm*slope > ym) return new Point(xd, yd);
					else return new Point(xd-1, yd);
				} else {
					if (xm*slope > MAX_HEIGHT/2-ym) return new Point(xd, yd);
					else return new Point(xd-1, yd-1);
				}
			}
		}
	}
	public void drawTileImage(Graphics2D g, Image img, Point2D.Double loc) {
		g.drawImage(img, (int)loc.x-1, (int)loc.y-1, (int)MAX_WIDTH+2, (int)MAX_HEIGHT+2, null);
	}
	double zoom = 1;
	double transX = 0, transY = 0;
	static final double MIN_ZOOM = .5;
	long lastTime = -1;
	public void draw(Graphics2D g){
		long current = System.nanoTime();
		if (lastTime > 0) {
			double dt = 500 * (current-lastTime) * 1e-9;
			if (LD36.up) transY += dt;
			if (LD36.down) transY -= dt;
			if (LD36.rightPressed) transX -= dt;
			if (LD36.left) transX += dt;
			restrictScroll();
		}
		lastTime = current;
		
		final double zoom = this.zoom, transX = this.transX, transY = this.transY;
		g.translate(transX, transY);
		g.scale(zoom, zoom);
		
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(2));
		
		int startX = (int) Math.floor(-transX / zoom / (MAX_WIDTH*3/4)) - 1;
		int endX = startX + (int) Math.floor(FRAME_WIDTH / zoom / (MAX_WIDTH*3/4)) + 2;
//		if (startX < 0) startX = 0;
//		if (endX >= MAP_WIDTH) endX = MAP_WIDTH-1;
		int startY = (int) Math.floor(-transY / zoom / MAX_HEIGHT) - 1;
		int endY = startY + (int) Math.floor(FRAME_HEIGHT / zoom / MAX_HEIGHT) + 2;
//		if (startY < 0) startY = 0;
//		if (endY >= MAP_HEIGHT) endY = MAP_HEIGHT-1;
		
		Path2D.Double tile = (Double) tilePoly.clone();
		Point2D.Double loc = getTileLocation(startX, startY);
		tile.transform(AffineTransform.getTranslateInstance(loc.x, loc.y));
		double sup = MAX_HEIGHT*(startY - endY - .5);
		double lup = sup - MAX_HEIGHT;
		AffineTransform tsup = AffineTransform.getTranslateInstance(MAX_WIDTH * .75, sup);
		AffineTransform tlup = AffineTransform.getTranslateInstance(MAX_WIDTH * .75, lup);
		for (int x = startX; x <= endX; x++) {
			for (int y = startY; y <= endY; y++) {
				//TODO draw stuff in tile (corner at loc)
				if (x >= 0 && x < MAP_WIDTH && y >= 0 && y < MAP_HEIGHT) {
					if ((data[x][y]&IMPASS_BIT) != 0) {
						drawTileImage(g, rock, loc);
					} else {
						drawTileImage(g, grass, loc);
					}
					if ((data[x][y]&CITY_BIT) != 0) {
						drawTileImage(g, tents, loc);
					}
					if ((data[x][y]&DIRT_ROAD_BIT) != 0) {
						g.setColor(Color.yellow.darker().darker());
						g.fillRect((int)loc.x + 20, (int) (loc.y + 20), 20, 20);
					}
					if ((data[x][y]&FOOT_PATH_BIT) != 0) {
						g.setColor(Color.GREEN);
						g.fillRect((int)loc.x + 60, (int)loc.y + 20, 20, 20);
					}
					if ((data[x][y]&PAVED_ROAD_BIT) != 0) {
						g.setColor(Color.black);
						g.fillRect((int)loc.x + 20, (int)loc.y + 60, 20, 20);
					}
					if ((data[x][y]&TRACK_BIT) != 0) {
						g.setColor(Color.red);
						g.fillRect((int)loc.x + 60, (int)loc.y + 60, 20, 20);
					}
				} else drawTileImage(g, grass, loc);
				
				// draw tile outline
				g.draw(tile);
				tile.transform(tDown);
				loc.y += MAX_HEIGHT;
			}
			if (x%2==0) {
				tile.transform(tsup);
				loc.y += sup;
			} else {
				tile.transform(tlup);
				loc.y += lup;
			}
			loc.x += MAX_WIDTH * .75;
		}
		
		//draw selected tiles
		if (selection.size() > 0) {
			g.setColor(Color.green);
			Stroke s = g.getStroke();
			g.setStroke(new BasicStroke(5));
			
			tile = (Double) insetPoly.clone();
			loc = new Point2D.Double();
			for (int i = 0; i < selection.size(); i++) {
				Point2D.Double p = getTileLocation(selection.get(i));
				tile.transform(AffineTransform.getTranslateInstance(p.x-loc.x, p.y-loc.y));
				loc = p;
				
				g.draw(tile);
			}
			g.setStroke(s);
		}
		
		
		g.scale(1/zoom, 1/zoom);
		g.translate(-transX, -transY);
	}
}
