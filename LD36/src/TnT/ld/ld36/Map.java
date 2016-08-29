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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Vector;

public class Map {
	public static Image grass;
	public static Image rock;
	public static Image tents;
	public static Image[] cityImages;
	static {
		grass = Resources.getImage("grass.png");
		rock = Resources.getImage("mountainpeak.png");
		tents = Resources.getImage("tents.png");
		ArrayList<Image> city = new ArrayList<Image>();
		Image img;
		int i = 0;
		try {
			while ((img = Resources.getImage("city_" + i++ + ".png")) != null) {
				city.add(img);
			}
		} catch (Exception e) {

		}
		cityImages = city.toArray(new Image[0]);
	}
	public static final int MAP_WIDTH = 100;
	public static final int MAP_HEIGHT = 100;
	/** Ratio of width:height of hexagon */
	public static final double RATIO = 1.154700538;
	/** The height of a tile at max zoom */
	public static final double MAX_HEIGHT = 100;
	public static final double MAX_WIDTH = (MAX_HEIGHT * RATIO);

	// size of the map section on the screen
	public static final int FRAME_WIDTH = 1280 - Overlay.RIGHT_WIDTH;
	public static final int FRAME_HEIGHT = 768 - Overlay.BOTTOM_HEIGHT;
	// total pixel size of the map
	public static final double MAP_PIXEL_WIDTH = MAX_WIDTH * (.75 * MAP_WIDTH + .25);
	public static final double MAP_PIXEL_HEIGHT = MAX_HEIGHT * (MAP_HEIGHT + .5);

	// Alternatively, could use first 4 bits to specify city ID, but then limits
	// to 16 cities
	// to use, just and with the bit you want and check > 0
	// If cities have everything by default, then just OR each of these with
	// CITY_BIT
	public static final byte CITY_BIT = 1;
	public static final byte FOOT_PATH_BIT = 2;
	public static final byte DIRT_ROAD_BIT = 4;
	public static final byte PAVED_ROAD_BIT = 8;
	public static final byte TRACK_BIT = 16;
	public static final byte IMPASS_BIT = 32;
	public static final byte AIRPORT_BIT = 64;

	Vector<Point> selection = new Vector<Point>();
	Vector<Sprite> anims = new Vector<Sprite>();
	int[] selectAdd = new int[Road.roads.length];
	int[] selectRemove = new int[Road.roads.length];
	int selectAddAirport, selectRemoveAirport, selectAddCatapult, selectRemoveCatapult;

	ArrayList<HelpPopup> helps = new ArrayList<HelpPopup>();

	ArrayList<City> cities = new ArrayList<City>();

	// make tile poly
	static Path2D.Double tilePoly;
	static AffineTransform tDown = AffineTransform.getTranslateInstance(0, MAX_HEIGHT);
	static {
		tilePoly = new Path2D.Double();
		tilePoly.moveTo(0, MAX_HEIGHT / 2);
		tilePoly.lineTo(MAX_WIDTH / 4, 0);
		tilePoly.lineTo(MAX_WIDTH * 3 / 4, 0);
		tilePoly.lineTo(MAX_WIDTH, MAX_HEIGHT / 2);
		tilePoly.lineTo(MAX_WIDTH * 3 / 4, MAX_HEIGHT);
		tilePoly.lineTo(MAX_WIDTH / 4, MAX_HEIGHT);
		tilePoly.closePath();
	}
	static Path2D.Double insetPoly;
	static {
		insetPoly = new Path2D.Double();
		insetPoly.moveTo(0, MAX_HEIGHT / 2);
		insetPoly.lineTo(MAX_WIDTH / 4, 0);
		insetPoly.lineTo(MAX_WIDTH * 3 / 4, 0);
		insetPoly.lineTo(MAX_WIDTH, MAX_HEIGHT / 2);
		insetPoly.lineTo(MAX_WIDTH * 3 / 4, MAX_HEIGHT);
		insetPoly.lineTo(MAX_WIDTH / 4, MAX_HEIGHT);
		insetPoly.closePath();
		insetPoly.transform(AffineTransform.getTranslateInstance(-MAX_WIDTH / 2, -MAX_HEIGHT / 2));
		insetPoly.transform(AffineTransform.getScaleInstance(.85, .85));
		insetPoly.transform(AffineTransform.getTranslateInstance(MAX_WIDTH / 2, MAX_HEIGHT / 2));
	}

	Point2D.Double scrollPoint = null;
	double scrollRate = 0;
	boolean graphicsStall;

	public byte[][] data = new byte[MAP_WIDTH][MAP_HEIGHT]; // this array is
															// done [x][y] to
															// simplify.

	public void mouseClicked(MouseEvent e) {
//		Point tile = getContainingTile(transformToMap(e.getX(), e.getY()));

	}

	Point mouseLoc = null;

	public void mousePressed(MouseEvent e) {
		mouseLoc = e.getPoint();
		mouseDragged(e);
	}

	public void mouseDragged(MouseEvent e) {
		if ((e.getModifiers() & 8) != 0) {
			if (this.isScrolling())
				return;
			// right click
			transX -= mouseLoc.x - e.getX();
			transY -= mouseLoc.y - e.getY();
			restrictScroll();
			mouseLoc = e.getPoint();
		}
		Point tile = getContainingTile(transformToMap(e.getX(), e.getY()));
		if ((e.getModifiers() & 16) != 0 && tile.x >= 0 && tile.x < MAP_WIDTH && tile.y >= 0 && tile.y < MAP_HEIGHT) {
			// left click
			// add to selection
			byte d = data[tile.x][tile.y];
			if ((d & CITY_BIT) != 0) {
				for (City c : cities) {
					if (c.getX() == tile.x && c.getY() == tile.y) {
						LD36.theLD.setSelectedCity(c);
					}
				}
			}
			if (!selection.contains(tile)) {
				selection.add(tile);
				// if not city or impassible

				if ((d & (CITY_BIT | IMPASS_BIT)) == 0) {
					for (Road r : Road.roads) {
						if ((d & r.mask) == 0)
							selectAdd[r.key]++;
						else
							selectRemove[r.key]++;
					}
				} else if ((d & CITY_BIT) != 0) {
					for (int i = 0; i < cities.size(); i++) {
						if (tile.equals(cities.get(i))) {
							if (cities.get(i).airport)
								selectRemoveAirport++;
							else
								selectAddAirport++;
							if (cities.get(i).catapults > 0)
								selectRemoveCatapult++;
							selectAddCatapult++;
						}
					}
				}
			}
		}
		if ((e.getModifiers() & 4) != 0 && tile.x >= 0 && tile.x < MAP_WIDTH && tile.y >= 0 && tile.y < MAP_HEIGHT) {
			// right click
			// remove from selection
			if (selection.contains(tile)) {
				selection.remove(tile);
				byte d = data[tile.x][tile.y];
				// if not city or impassible
				if ((d & (CITY_BIT | IMPASS_BIT)) == 0) {
					for (Road r : Road.roads) {
						if ((d & r.mask) == 0)
							selectAdd[r.key]--;
						else
							selectRemove[r.key]--;
					}
				} else if ((d & CITY_BIT) != 0) {
					for (int i = 0; i < cities.size(); i++) {
						if (tile.equals(cities.get(i))) {
							if (cities.get(i).airport)
								selectRemoveAirport--;
							else
								selectAddAirport--;
							if (cities.get(i).catapults > 0)
								selectRemoveCatapult--;
							selectAddCatapult--;
						}
					}
				}
			}
		}
		// System.out.println("selection: "+selection);
		// System.out.println("can add: "+Arrays.toString(selectAdd));
		// System.out.println("can remove: "+Arrays.toString(selectRemove));
	}

	public void clearSelection() {
		selection.clear();
		for (int i = 0; i < selectAdd.length; i++) {
			selectAdd[i] = 0;
			selectRemove[i] = 0;
		}
		selectAddAirport = 0;
		selectAddCatapult = 0;
		selectRemoveAirport = 0;
		selectRemoveCatapult = 0;
	}

	public void buildSelection(Road r) {
		for (int i = 0; i < selection.size(); i++) {
			Point tile = selection.get(i);
			if ((data[tile.x][tile.y] & (CITY_BIT | IMPASS_BIT | r.mask)) == 0)
				data[tile.x][tile.y] |= r.mask;
		}
		selectRemove[r.key] += selectAdd[r.key];
		selectAdd[r.key] = 0;
		recalcFlag = true;
	}

	public void sellSelection(Road r) {
		for (int i = 0; i < selection.size(); i++) {
			Point tile = selection.get(i);
			byte d = data[tile.x][tile.y];
			if ((d & (CITY_BIT | IMPASS_BIT)) == 0 && (d & r.mask) != 0)
				data[tile.x][tile.y] ^= r.mask;
		}
		selectAdd[r.key] += selectRemove[r.key];
		selectRemove[r.key] = 0;
		recalcFlag = true;
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		if (this.isScrolling())
			return;
		double oz = zoom;
		zoom *= Math.pow(1.125, -e.getPreciseWheelRotation());
		zoom = Math.max(MIN_ZOOM, Math.min(1, zoom));
		transX = e.getX() - (e.getX() - transX) * zoom / oz;
		transY = e.getY() - (e.getY() - transY) * zoom / oz;
		restrictScroll();
	}

	public void restrictScroll() {
		transX = Math.min(0, Math.max(FRAME_WIDTH - MAP_PIXEL_WIDTH * zoom, transX));
		transY = Math.min(0, Math.max(FRAME_HEIGHT - MAP_PIXEL_HEIGHT * zoom, transY));
	}

	public void gotoPoint(Point2D.Double p) {
		Point2D.Double off = getOffsetToCenterPoint(p.x, p.y);
		transX = off.x;
		transY = off.y;
	}

	public void scrollTo(Point2D.Double p) {
		if (this.isScrolling())
			return;
		scrollPoint = getOffsetToCenterPoint(p.x, p.y);
		scrollRate = 0;
	}

	public boolean isScrolling() {
		return scrollPoint != null;
	}

	public Point2D.Double getOffsetToCenterPoint(double x, double y) {
		double tx = FRAME_WIDTH / 2 - x * zoom;
		double ty = FRAME_HEIGHT / 2 - y * zoom;
		tx = Math.min(0, Math.max(FRAME_WIDTH - MAP_PIXEL_WIDTH * zoom, tx));
		ty = Math.min(0, Math.max(FRAME_HEIGHT - MAP_PIXEL_HEIGHT * zoom, ty));
		return new Point2D.Double(tx, ty);
	}

	public static Map generate() {
		Map m = new Map();
		m.addCity((int) (MAP_WIDTH / 2.0 - 2), (int) (MAP_HEIGHT / 2.0));
		m.cities.get(0).population = 10;
		m.addCity((int) (MAP_WIDTH / 2.0 + 2), (int) (MAP_HEIGHT / 2.0));
		Point p1 = m.cities.get(0).getLocation();
		Point p2 = m.cities.get(1).getLocation();
		while (!p1.equals(p2)) {
			p1.x += Math.signum(p2.x - p1.x);
			p1.y += Math.signum(p2.y - p1.y);
			m.setTile(p1, Map.FOOT_PATH_BIT, true);
		}
		m.gotoPoint(m.getTileCenter((int) (MAP_WIDTH / 2), (int) (MAP_HEIGHT / 2)));
		m.recalcFlag = true;
		p1 = m.cities.get(0).getLocation();
		p2 = m.cities.get(1).getLocation();
		// generate some mountain ranges
		Point p3;
		Random r = new Random();
		d: for (int i = 0; i < 15; i++) {
			p3 = new Point(r.nextInt(m.data.length), r.nextInt(m.data[0].length));
			int count = 0;
			while ((Math.random() < .965 && count++ < 20) || count < 4) {
				if (m.data[p3.x][p3.y] == 0) {
					m.data[p3.x][p3.y] |= IMPASS_BIT;
				}
				if (Math.random() < .5) {
					p3.x += Math.signum(Math.random() - .25);
				} else {
					p3.y += Math.signum(Math.random() - .25);
				}
				if (p3.x >= m.data.length || p3.x < 0 || p3.y >= m.data[0].length || p3.y < 0) {
					continue d;
				}
			}
		}
		return m;
	}

	public City addCity(int x, int y) {
		data[x][y] = 0x1f;

		// expand path arrays for all existing cities
		for (City c : cities)
			c.addCity();

		City c = new City();
		c.x = x;
		c.y = y;
		c.ID = cities.size();
		for (int i = 0; i < cities.size(); i++)
			c.addCity();
		cities.add(c);
		recalcFlag = true;
		return c;
	}

	boolean recalcFlag = true;

	public void calculateAllPaths() {
		for (int i = 0; i < cities.size(); i++) {
			City a = cities.get(i);
			for (int j = 0; j < cities.size(); j++)
				if (i != j) {
					City b = cities.get(j);
					for (int t = 0; t < Transport.baseUnits.length; t++)
						cities.get(i).paths.get(j - (j > i ? 1 : 0))[t] = findPath(a.x, a.y, b.x, b.y,
								Transport.baseUnits[t]);
				}
		}
		recalcFlag = false;
	}

	public boolean hasCity(int x, int y) {
		return (data[x][y] & CITY_BIT) > 0;
	}

	public boolean canUse(Transport type, Point tile) {
		if (tile.x < 0 || tile.x >= MAP_WIDTH || tile.y < 0 || tile.y >= MAP_HEIGHT)
			return false;
		return (data[tile.x][tile.y] & type.bits) != 0;
	}

	public Path findPath(int x1, int y1, int x2, int y2, Transport type) {
		HashSet<Point> visited = new HashSet<Point>();
		PriorityQueue<SearchNode> open = new PriorityQueue<SearchNode>(20, compare);
		Point2D.Double endLoc = getTileLocation(x2, y2);
		Point start = new Point(x1, y1);
		open.add(new SearchNode(start));
		visited.add(start);
		while (!open.isEmpty()) {
			SearchNode current = open.remove();
			for (Point n : getNeighbors(current.tile)) {
				if (n.x == x2 && n.y == y2) {
					Path path = new Path();
					path.points.add(n);
					while (current.parent != null) {
						path.points.addFirst(current.tile);
						current = current.parent;
					}
					return path;
				} else if (!visited.contains(n) && canUse(type, n)) {
					visited.add(n);
					open.add(new SearchNode(n, endLoc.distance(getTileLocation(n)), current));
				}
			}
		}
		return null;
	}

	public void setTile(int xi, int yi, byte bit, boolean v) {
		if (v)
			data[xi][yi] |= bit;
		else
			data[xi][yi] &= ~bit;
	}

	public void setTile(Point p, byte bit, boolean v) {
		setTile(p.x, p.y, bit, v);
	}

	public Point2D.Double getTileLocation(Point p) {
		return getTileLocation(p.x, p.y);
	}

	public Point2D.Double getTileLocation(int x, int y) {
		return new Point2D.Double(x * MAX_WIDTH * .75, (y + ((x & 1) == 1 ? .5 : 0)) * MAX_HEIGHT);
	}

	public Point2D.Double getTileCenter(Point p) {
		return getTileCenter(p.x, p.y);
	}

	public Point2D.Double getTileCenter(int x, int y) {
		return new Point2D.Double(x * MAX_WIDTH * .75 + MAX_WIDTH / 2,
				(y + ((x & 1) == 1 ? .5 : 0)) * MAX_HEIGHT + MAX_HEIGHT / 2);
	}

	public Point2D.Double transformToMap(double x, double y) {
		return new Point2D.Double((x - transX) / zoom, (y - transY) / zoom);
	}

	public Point[] getNeighbors(Point p) {
		return getNeighbors(p.x, p.y);
	}

	public Point[] getNeighbors(int x, int y) {
		return new Point[] { new Point(x, y + 1), new Point(x, y - 1), new Point(x - 1, y - 1 + (x & 1)),
				new Point(x + 1, y - 1 + (x & 1)), new Point(x - 1, y + (x & 1)), new Point(x + 1, y + (x & 1)) };
	}

	public Point getContainingTile(Point2D.Double p) {
		return getContainingTile(p.x, p.y);
	}

	public Point getContainingTile(double x, double y) {
		if (x / MAX_WIDTH * 4 % 3 >= 1) {
			// middle section
			x /= MAX_WIDTH * .75;
			if (x % 2 >= 1) {
				return new Point((int) x, (int) Math.floor(y / MAX_HEIGHT - .5));
			} else {
				return new Point((int) x, (int) Math.floor(y / MAX_HEIGHT));
			}
		} else {
			// overlap section
			double xm = x % (MAX_WIDTH * .75);
			double ym = y % (MAX_HEIGHT / 2);
			int xd = (int) (x / (MAX_WIDTH * .75));
			int yd = (int) (y / MAX_HEIGHT);
			double slope = Math.tan(Math.PI / 3);
			if (x / (MAX_WIDTH * .75) % 2 >= 1) {
				if (y / (MAX_HEIGHT / 2) % 2 >= 1) {
					if (xm * slope > MAX_HEIGHT / 2 - ym)
						return new Point(xd, yd);
					else
						return new Point(xd - 1, yd);
				} else {
					if (xm * slope > ym)
						return new Point(xd, yd - 1);
					else
						return new Point(xd - 1, yd);
				}
			} else {
				if (y / (MAX_HEIGHT / 2) % 2 >= 1) {
					if (xm * slope > ym)
						return new Point(xd, yd);
					else
						return new Point(xd - 1, yd);
				} else {
					if (xm * slope > MAX_HEIGHT / 2 - ym)
						return new Point(xd, yd);
					else
						return new Point(xd - 1, yd - 1);
				}
			}
		}
	}

	public void drawTileImage(Graphics2D g, Image img, Point2D.Double loc) {
		g.drawImage(img, (int) loc.x - 1, (int) loc.y - 1, (int) MAX_WIDTH + 2, (int) MAX_HEIGHT + 2, null);
	}

	double zoom = 1;
	double transX = 0, transY = 0;
	static final double MIN_ZOOM = .25;
	long lastTime = -1;

	public void draw(Graphics2D g) {
		long current = System.nanoTime();
		double dt = (current - lastTime) * 1e-9;
		if (lastTime > 0 && !graphicsStall) {
			if (this.isScrolling()) {
				System.out.println("scrolling " + transX + ",  " + transY + " to " + scrollPoint);
				double dist = scrollPoint.distance(transX, transY);
				if (dist < 1) {
					transX = scrollPoint.x;
					transY = scrollPoint.y;
					scrollPoint = null;
					scrollRate = 0;
					System.out.println("stopped");
				} else {
					scrollRate = Math.min(scrollRate + 2000 * dt, Math.min(dist * 5, 1e9));
					System.out.println(scrollRate);
					double dx = (scrollPoint.x - transX) * scrollRate / dist;
					double dy = (scrollPoint.y - transY) * scrollRate / dist;
					transX += dx * dt;
					transY += dy * dt;
				}
			} else {
				double rate = 500;
				if (LD36.up)
					transY += rate * dt;
				if (LD36.down)
					transY -= rate * dt;
				if (LD36.rightPressed)
					transX -= rate * dt;
				if (LD36.left)
					transX += rate * dt;
				restrictScroll();
			}
		} else {
			dt = 0;
		}

		// System.out.println(transX + ", " + transY);
		lastTime = current;
		graphicsStall = false;

		final double zoom = this.zoom, transX = this.transX, transY = this.transY;
		g.translate(transX, transY);
		g.scale(zoom, zoom);

		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(2));

		int startX = (int) Math.floor(-transX / zoom / (MAX_WIDTH * 3 / 4)) - 1;
		int endX = startX + (int) Math.floor(FRAME_WIDTH / zoom / (MAX_WIDTH * 3 / 4)) + 2;
		// if (startX < 0) startX = 0;
		// if (endX >= MAP_WIDTH) endX = MAP_WIDTH-1;
		int startY = (int) Math.floor(-transY / zoom / MAX_HEIGHT) - 1;
		int endY = startY + (int) Math.floor(FRAME_HEIGHT / zoom / MAX_HEIGHT) + 2;
		// if (startY < 0) startY = 0;
		// if (endY >= MAP_HEIGHT) endY = MAP_HEIGHT-1;

		Path2D.Double tile = (Double) tilePoly.clone();
		Point2D.Double loc = getTileLocation(startX, startY);
		tile.transform(AffineTransform.getTranslateInstance(loc.x, loc.y));
		double sup = MAX_HEIGHT * (startY - endY - .5);
		double lup = sup - MAX_HEIGHT;
		AffineTransform tsup = AffineTransform.getTranslateInstance(MAX_WIDTH * .75, sup);
		AffineTransform tlup = AffineTransform.getTranslateInstance(MAX_WIDTH * .75, lup);
		for (int i = 0; i < cities.size(); i++) {
			City c = cities.get(i);
			int pop = (int) Math.max(Math.min(cityImages.length - 1, Math.log10(c.population)), 0);
			drawTileImage(g, grass, getTileLocation(c));
			drawTileImage(g, cityImages[pop], getTileLocation(c));
		}
		for (int x = startX; x <= endX; x++) {
			for (int y = startY; y <= endY; y++) {
				// TODO draw stuff in tile (corner at loc)
				d: if (x >= 0 && x < MAP_WIDTH && y >= 0 && y < MAP_HEIGHT) {

					if ((data[x][y] & IMPASS_BIT) != 0) {
						drawTileImage(g, rock, loc);
					} else {
						if ((data[x][y] & CITY_BIT) == 0) {
							drawTileImage(g, grass, loc);
						} else {
							break d;
						}
					}
					// if ((data[x][y]&CITY_BIT) != 0) {
					// drawTileImage(g, tents, loc);
					// }
					if ((data[x][y] & DIRT_ROAD_BIT) != 0) {
						g.setColor(Color.yellow.darker().darker());
						g.fillRect((int) loc.x + 20, (int) (loc.y + 20), 20, 20);
					}
					if ((data[x][y] & FOOT_PATH_BIT) != 0) {
						g.setColor(Color.GREEN);
						g.fillRect((int) loc.x + 60, (int) loc.y + 20, 20, 20);
					}
					if ((data[x][y] & PAVED_ROAD_BIT) != 0) {
						g.setColor(Color.black);
						g.fillRect((int) loc.x + 20, (int) loc.y + 60, 20, 20);
					}
					if ((data[x][y] & TRACK_BIT) != 0) {
						g.setColor(Color.red);
						g.fillRect((int) loc.x + 60, (int) loc.y + 60, 20, 20);
					}
				} else
					drawTileImage(g, grass, loc);

				// draw tile outline
				g.setColor(Color.black);
				g.draw(tile);
				tile.transform(tDown);
				loc.y += MAX_HEIGHT;
			}
			if (x % 2 == 0) {
				tile.transform(tsup);
				loc.y += sup;
			} else {
				tile.transform(tlup);
				loc.y += lup;
			}
			loc.x += MAX_WIDTH * .75;
		}

		// draw selected tiles
		if (selection.size() > 0) {
			g.setColor(Color.green);
			Stroke s = g.getStroke();
			g.setStroke(new BasicStroke(5));

			tile = (Double) insetPoly.clone();
			loc = new Point2D.Double();
			for (int i = 0; i < selection.size(); i++) {
				Point2D.Double p = getTileLocation(selection.get(i));
				tile.transform(AffineTransform.getTranslateInstance(p.x - loc.x, p.y - loc.y));
				loc = p;

				g.draw(tile);
			}
			g.setStroke(s);
		}
		for (int i = 0; i < helps.size(); i++) {
			helps.get(i).draw(g);
			helps.get(i).ticks -= dt;
			System.out.println("dt: " + dt);
			System.out.println(helps.get(i).ticks);
			if (helps.get(i).ticks < 0) {
				helps.remove(i--);
			}
		}
		for (int i = 0; i < anims.size(); i++) {
			anims.get(i).draw(g);
		}

		g.scale(1 / zoom, 1 / zoom);
		g.translate(-transX, -transY);
	}

	/**
	 * A* search node
	 */
	private class SearchNode {
		SearchNode parent;
		double weight;
		double pathLength;
		Point tile;

		/**
		 * Makes start node
		 */
		public SearchNode(Point tile) {
			this.tile = tile;
		}

		/**
		 * Search node with parent
		 */
		public SearchNode(Point tile, double dist, SearchNode parent) {
			this.tile = tile;
			pathLength = parent.pathLength + MAX_HEIGHT;
			weight = pathLength + dist;
			this.parent = parent;
		}
	}

	Comparator<SearchNode> compare = new Comparator<SearchNode>() {
		public int compare(SearchNode a, SearchNode b) {
			return (int) Math.signum(a.weight - b.weight);
		}
	};
	Random r = new Random();

	public City addNewRandomCity() {
		Point p = new Point();
		boolean b = true, bb = true;
		;
		do {
			p.x = r.nextInt(MAP_WIDTH - 4) + 2;
			p.y = r.nextInt(MAP_HEIGHT - 4) + 2;
			if (data[p.x][p.y] != 0)
				continue; // it must be on a grass tile
			b = true;
			bb = false;
			for (City c : cities) {
				if (p.distance(c) < 4) { // cannot be closer than this
					b = false;
					continue;
				}
				if (p.distance(c) < 8) { // must have at least one this close
					bb = true;
					continue;
				}
			}
		} while (!(b && bb));
		return addCity(p.x, p.y);
	}

	public void addHelp(HelpPopup helpPopup) {
		helps.add(helpPopup);
	}

	public void addAnimation(Sprite sprite) {
		anims.add(sprite);
	}
}
