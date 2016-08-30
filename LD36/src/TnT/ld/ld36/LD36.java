package TnT.ld.ld36;//TODO TnTWizard.ld.ld36? or TnT.wizard.ld.ld36?

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.VolatileImage;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

//LOOK AT WHITEBOARD DUNCAN! it has my ideas for gui. i didnts get as far as i hoped

public class LD36 extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8318833936997575712L;
	public static LD36 theLD;
	public static boolean debug = true;
	volatile State gameState = State.MAIN;
	JPanel panel = new JPanel();
	Map map;
	VolatileImage buffer;
	Overlay bottom = new Overlay();
	Overlay right = new Overlay();
	OverlayScrollPane sp = new OverlayScrollPane();
	public static final BufferedImage footPath = Resources.getImage("icon_footpath.png");
	public static final BufferedImage dirtRoad = Resources.getImage("icon_dirtroad.png");
	public static final BufferedImage trainTrack = Resources.getImage("icon_traintrack.png");
	public static final BufferedImage road = Resources.getImage("icon_road.png");
	public static final BufferedImage catapult = Resources.getImage("icon_catapult.png");
	public static final BufferedImage airport = Resources.getImage("icon_airport.png");
	OverlayButton treeButton = new OverlayButton("Technology Tree");
	OverlayButton clearSelection = new OverlayButton("Clear");
	OverlayButton addFootPath = new OverlayButton(footPath);
	OverlayButton addDirtRoad = new OverlayButton(dirtRoad);
	OverlayButton addRailRoad = new OverlayButton(trainTrack);
	OverlayButton addPavedRoad = new OverlayButton(road);
	OverlayButton addCatapalt = new OverlayButton(catapult);
	OverlayButton addAirport = new OverlayButton(airport);
	OverlayText footPathCost;
	OverlayText dirtRoadCost;
	OverlayText railRoadCost;
	OverlayText pavedRoadCost;
	OverlayText catapultCost;
	OverlayText airportCost;
	

	OverlayButton cityName = new OverlayButton("Name: ");
	Overlay cityPopulation = new Overlay("Pop: ");

	static ColorConvertOp grayOp = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);

	String initHelpText = "A new city was founded to take\nadvantage of your great mail system.\n"
			+ "Consider adding it to your other\ncities to generate more mail!";
	String initHelpText2 = "To connect this city, select tiles to form a path\nand then select the path type at the bottom.\nOver time you will need to upgrade!";
	boolean shownHelp = false;
	boolean boosted = true;
	OverlayButton increase = new OverlayButton("Write Mail for City") {
		/**
		 * 
		 */
		private static final long serialVersionUID = -8660650637481509055L;

		@Override
		public void mousePressed(MouseEvent e) {
			boosted = true;
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			boosted = false;
		}

		@Override
		public void mouseExited(MouseEvent e) {
			mouseReleased(e);
		}
	};

	private static final Font moneyFont = new Font("Courier", Font.BOLD, 24);

	Overlay moneyOverlay = new Overlay() {
		/**
		 * 
		 */
		private static final long serialVersionUID = -5539501003550325316L;

		@Override
		public void draw(Graphics2D g) {
			Font f = g.getFont();
			g.setFont(moneyFont);
			Color c = g.getColor();
			g.setColor(Color.black);
			String mon;
			g.drawString(mon = moneyString(money), (int) x, (int) (y + moneyFont.getSize() * 1.5));
			g.drawString(moneyString(moneyPerTick * (1000 / PHYSICS_DELAY)) + "/s",
					(int) x + Math.max((g.getFontMetrics().stringWidth(mon) / 20) * 20 + 50, 150),
					(int) (y + moneyFont.getSize() * 1.5));
			g.setColor(c);
			g.setFont(f);
		}
	};

	OverlayButton addSelected = clearSelection;

	private static final DecimalFormat smallFormat = new DecimalFormat("$###,###,###.##");
	private static final String[] small = new String[] { "m", "b", "tr", "quadr", "quint", "sext", "sept", "oct", "non",
			"dec" };
	private static final String[] ones = new String[] { "un", "duo", "tre", "quattuor", "quinqua", "se", "septe",
			"octo", "nove" };
	private static final String[] tens = new String[] { "deci", "viginti", "triginta", "quadraginta", "quinquaginta",
			"sexaginta", "septuaginta", "octoginta", "nonaginta" };
	private static final String[] hundreds = new String[] { "centi", "ducenti", "trecenti", "quadringenti", "quingenti",
			"sescenti", "septingenti", "octingenti", "nongenti" };
	private static final String[] treSpecialTens = new String[] { "", "s", "s", "s", "s", "", "", "", "" };
	private static final String[] treSpecialHundreds = new String[] { "", "", "s", "s", "s", "", "", "", "" };
	private static final String[] seSpecialTens = new String[] { "", "", "s", "s", "s", "", "", "x", "" };
	private static final String[] seSpecialHundreds = new String[] { "x", "", "s", "s", "s", "", "", "x", "" };
	private static final String[] septeNoveSpecialTens = new String[] { "n", "m", "n", "n", "n", "n", "n", "m", "" };
	private static final String[] septeNoveSpecialHundreds = new String[] { "n", "n", "n", "n", "n", "n", "n", "m",
			"" };

	public static String moneyString(double x) {
		if (x == Double.POSITIVE_INFINITY || x == Double.NaN) {
			return "$ ∞";
		}
		if (x < 1000000) {
			return smallFormat.format(x);
		}
		int log = (int) (Math.floor(Math.log10(x)) - 3) / 3;
		int initlog = log;
		int One = -1, Ten = -1, Hundred = -1;
		String hundred = "", ten = "", one = "";
		if (log <= 10) {
			one = small[log - 1];
		} else {
			if (log > 1000) {
				// thousand = log / 1000;
				log %= 1000;
			}
			if (log > 100) {
				Hundred = log / 100 - 1;
				log %= 100;
			}
			if (log > 10) {
				Ten = log / 10 - 1;
				log %= 10;
			}
			if (log > 0) {
				One = log - 1;
			}
		}
		if (One != -1) {
			one = ones[One];
		}
		if (Ten != -1) {
			ten = tens[Ten];
		}
		if (Hundred != -1) {
			hundred = hundreds[Hundred];
		}
		String s1 = "", s2 = "";
		if (Ten >= 0) {
			if (One == 2) { // tre
				s1 = treSpecialTens[Ten];
			}
			if (One == 5) {
				s1 = seSpecialTens[Ten];
			}
			if (One == 6 || One == 8) {
				s1 = septeNoveSpecialTens[Ten];
			}
		}
		if (Hundred >= 0) {
			if (Ten == 2) { // tre
				s2 = treSpecialHundreds[Hundred];
			}
			if (Ten == 5) {
				s2 = seSpecialHundreds[Hundred];
			}
			if (Ten == 6 || One == 8) {
				s2 = septeNoveSpecialHundreds[Hundred];
			}
		}
		String start = " " + one + s1 + ten + s2 + hundred;
		if (endsEven(start)) {
			start = start.substring(0, start.length() - 1);
		}
		return "$" + String.format("%1$.5f", x / Math.pow(10, initlog * 3 + 3)).substring(0, 5) + start + "illion";
	}

	/**
	 * Does not include o bc reasons
	 * 
	 * @param s
	 * @return
	 */
	@Deprecated
	private static boolean endsEven(String s) {
		return s.length() > 0 && "aeiu".indexOf(s.charAt(s.length() - 1)) >= 0;
	}

	ActionListener addListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			OverlayButton o = (OverlayButton) e.getSource();
			if (o == clearSelection) {
				map.clearSelection();
				return;
			}
			Road road = null;
			if (o == addFootPath)
				road = Road.FOOTPATH;
			if (o == addDirtRoad)
				road = Road.DIRT;
			if (o == addPavedRoad)
				road = Road.PAVED;
			if (o == addRailRoad)
				road = Road.RAIL;
			if (road != null) {
				if (!road.unlocked)
					return;
				if (e.getModifiers() == 16 && money > road.cost * map.selectAdd[road.key]) {
					money -= road.cost * map.selectAdd[road.key];
					map.buildSelection(road);
					map.clearSelection();
				} // else if (e.getModifiers()==4) {
				// map.sellSelection(road);
				// map.clearSelection();
				// }
			} else if (o == addCatapalt) {
				if (money >= Transport.CATAPULT_COST * map.selectAddCatapult) {
					money -= Transport.CATAPULT_COST * map.selectAddCatapult;
					map.buildCatapults();
					map.clearSelection();
				}
			} else if (o == addAirport) {
				if (money >= Road.AIRPORT.cost * map.selectAddAirport) {
					money -= Road.AIRPORT.cost * map.selectAddAirport;
					map.buildAirports();
					map.clearSelection();
				}
			}
		}
	};

	TechTree techTree = new TechTree();
	ArrayList<Overlay> activeOverlays = new ArrayList<Overlay>();
	public double money = 0;
	public double lifeTimeEarnings = 0;

	MouseAdapter adapter = new MouseAdapter() {
		public void mouseClicked(MouseEvent e) {
			// TODO consider ignoring game state and use overlays for menu?
			switch (gameState) {
			case MAIN:
				startGame();
				break;
			case GAME:
				// handle anything above the map here, then forward to the map
				for (Overlay o : activeOverlays) {
					if (!o.visible)
						continue;
					if (o.contains(e.getPoint())) {
						o.mouseClicked(e);
						return;
					}
				}
				map.mouseClicked(e);
			}
		}

		public void mousePressed(MouseEvent e) {
			switch (gameState) {
			case MAIN:
				break;
			case GAME:
				// handle anything above the map here, then forward to the map
				for (Overlay o : activeOverlays) {
					if (!o.visible)
						continue;
					if (o.contains(e.getPoint())) {
						o.mousePressed(e);
						return;
					}
				}
				map.mousePressed(e);
			}
		}

		public void mouseReleased(MouseEvent e) {
			switch (gameState) {
			case MAIN:
				break;
			case GAME:
				// handle anything above the map here, then forward to the map
				for (Overlay o : activeOverlays) {
					if (!o.visible)
						continue;
					if (o.contains(e.getPoint())) {
						o.mouseReleased(e);
					}
				}
			}
		}

		public void mouseMoved(MouseEvent e) {
			// System.out.println(e.getPoint());
			switch (gameState) {
			case MAIN:
				break;
			case GAME:
				// handle anything above the map here, then forward to the map
				for (Overlay o : activeOverlays) {
					if (!o.visible)
						continue;
					if (o.contains(e.getPoint())) {
						if (!o.mouseIn) {
							o.mouseIn = true;
							o.mouseEntered(new MouseEvent(panel, MouseEvent.MOUSE_ENTERED, e.getWhen(), 0, e.getX(),
									e.getY(), e.getXOnScreen(), e.getYOnScreen(), 0, false, 0));
						}
						o.mouseMoved(e);
					} else {
						if (o.mouseIn) {
							o.mouseIn = false;
							o.mouseExited(new MouseEvent(panel, MouseEvent.MOUSE_EXITED, e.getWhen(), 0, e.getX(),
									e.getY(), e.getXOnScreen(), e.getYOnScreen(), 0, false, 0));
						}
					}
				}
				// map.mouseMoved(e);
			}
		}

		public void mouseDragged(MouseEvent e) {
			switch (gameState) {
			case MAIN:
				break;
			case GAME:
				// handle anything above the map here, then forward to the map
				for (Overlay o : activeOverlays) {
					if (!o.visible)
						continue;
					if (o.contains(e.getPoint())) {
						o.mouseDragged(e);
						return;
					}
					;
				}
				map.mouseDragged(e);
			}
		}

		public void mouseWheelMoved(MouseWheelEvent e) {
			switch (gameState) {
			case MAIN:
				break;
			case GAME:
				// handle anything above the map here, then forward to the map
				for (Overlay o : activeOverlays) {
					if (!o.visible)
						continue;
					if (o.contains(e.getPoint()))
						return;
				}
				map.mouseWheelMoved(e);
			}
		}
	};

	static boolean up, down, left, rightPressed;
	KeyAdapter keyAdapter = new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_A:
			case KeyEvent.VK_LEFT:
				left = true;
				break;
			case KeyEvent.VK_W:
			case KeyEvent.VK_UP:
				up = true;
				break;
			case KeyEvent.VK_S:
			case KeyEvent.VK_DOWN:
				down = true;
				break;
			case KeyEvent.VK_D:
			case KeyEvent.VK_RIGHT:
				rightPressed = true;
				break;
			case KeyEvent.VK_SPACE:
				if (gameState == State.GAME)
					map.scrollTo(map.getTileCenter((int) (Map.MAP_WIDTH / 2), (int) (Map.MAP_HEIGHT / 2)));
				break;
			case KeyEvent.VK_ESCAPE:
				if (treeButton.callback != null && sp.visible) {
					treeButton.callback.actionPerformed(new ActionEvent(treeButton, 0, ""));
				} else {
					map.clearSelection();
				}
				break;
			}
		}

		public void keyReleased(KeyEvent e) {
			System.out.println(e.getKeyCode());
			switch (e.getKeyCode()) {
			case KeyEvent.VK_A:
			case KeyEvent.VK_LEFT:
				left = false;
				break;
			case KeyEvent.VK_W:
			case KeyEvent.VK_UP:
				up = false;
				break;
			case KeyEvent.VK_S:
			case KeyEvent.VK_DOWN:
				down = false;
				break;
			case KeyEvent.VK_D:
			case KeyEvent.VK_RIGHT:
				rightPressed = false;
				break;
			case KeyEvent.VK_PAUSE:
				if (e.isShiftDown()) {
					money *= 2;
				}
				break;
			case KeyEvent.VK_PRINTSCREEN:
				if (e.isShiftDown()) {
					generateCity();
				}
				break;
			}
		}
	};

	public static void main(String[] args) {
		theLD = new LD36();
		theLD.initGUI();
	}

	protected void generateCity() {
		City c;
		map.scrollTo(map.getTileCenter(c = map.addNewRandomCity()));
		if (!shownHelp) {
			map.addHelp(new HelpPopup(map, c.x, c.y - 1, initHelpText));
			map.addHelp(new HelpPopup(map, c.x, c.y + 1, initHelpText2));
			shownHelp = true;
		}
	}

	public void initGUI() {
		this.setTitle(GAME_NAME);
		// Fullscreen?
		panel.setPreferredSize(new Dimension(1280, 768));
		// buffer = new BufferedImage(1280, 768, BufferedImage.TYPE_4BYTE_ABGR);
		panel.setFocusable(true);
		panel.grabFocus();
		panel.addKeyListener(keyAdapter);
		panel.addMouseListener(adapter);
		panel.addMouseMotionListener(adapter);
		panel.addMouseWheelListener(adapter);
		this.add(panel);
		this.pack();
		this.setDefaultCloseOperation(3);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		buffer = panel.createVolatileImage(1280, 768);
		graphics.start();
		physics.start();
	}
	public static final String GAME_NAME = "IT'S MAIL TIIIME...";
	public static final String CLICK = "Click anywhere to continue";
	public Thread graphics = new Thread() {
		long start = System.currentTimeMillis();
		int fps = 0;
		int frames = 0;
		int tick = 0;
		ArrayList<MenuSprite> menuSprites = new ArrayList<MenuSprite>();
		Random r = new Random();
		public void run() {
			try {
				while (true) {
					Graphics2D g = buffer.createGraphics();
					g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g.setColor(Color.darkGray);
					g.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());
					g.setColor(Color.BLACK);
					switch (gameState) {
					case MAIN:
						Font f = g.getFont();
						g.setFont(f.deriveFont(42f));
						g.setColor(Color.white);
						g.drawString(GAME_NAME,(getWidth() - g.getFontMetrics().stringWidth(GAME_NAME)) / 2, 200);
						g.setFont(f.deriveFont(24f));
						g.drawString(CLICK,(getWidth() - g.getFontMetrics().stringWidth(CLICK)) / 2, 400);
						g.setFont(f);
						for (MenuSprite s : menuSprites) {
							s.draw(g);
						}
						if (tick++ % 100 == 0) {
							Point2D.Double src = new Point2D.Double(-1, -1), dest = new Point2D.Double(-1, -1);
							int s = r.nextInt(4);
							switch (s) {
							case 0:
								src = new Point2D.Double(0, r.nextInt(getHeight()));
								break;
							case 1:
								src = new Point2D.Double(getWidth(), r.nextInt(getHeight()));
								break;
							case 2:
								src = new Point2D.Double(r.nextInt(getWidth()), getHeight());
								break;
							case 3:
								src = new Point2D.Double(r.nextInt(getWidth()), 0);
								break;
							}
							int d = s;
							while (d == s || dest.distance(src) < 500) {
								d = r.nextInt(4);
								switch (d) {
								case 0:
									dest = new Point2D.Double(0, r.nextInt(getHeight()));
									break;
								case 1:
									dest = new Point2D.Double(getWidth(), r.nextInt(getHeight()));
									break;
								case 2:
									dest = new Point2D.Double(r.nextInt(getWidth()), getHeight());
									break;
								case 3:
									dest = new Point2D.Double(r.nextInt(getWidth()), 0);
									break;
								}
							}
							
							menuSprites.add(new MenuSprite(src, dest, Transport.baseUnits[r.nextInt(Transport.baseUnits.length)].img));
						}
						break;
					case GAME:
						if (sp.visible) {
							sp.draw(g);
							bottom.draw(g);
							map.graphicsStall = true;
							break;
						}
						map.draw(g);
						displayCityData();

						footPathCost.setText(moneyString(map.selectAdd[Road.FOOTPATH.key]*Road.FOOTPATH.cost));
						dirtRoadCost.setText(moneyString(map.selectAdd[Road.DIRT.key]*Road.DIRT.cost));
						pavedRoadCost.setText(moneyString(map.selectAdd[Road.PAVED.key]*Road.PAVED.cost));
						railRoadCost.setText(moneyString(map.selectAdd[Road.RAIL.key]*Road.RAIL.cost));
						airportCost.setText(moneyString(map.selectAddAirport*Road.AIRPORT.cost));
						catapultCost.setText(moneyString(map.selectAddCatapult*Transport.CATAPULT_COST));
						
						Color good = Color.green.darker().darker();
						Color bad = Color.red;
						footPathCost.setColor(map.selectAdd[Road.FOOTPATH.key]*Road.FOOTPATH.cost>money?bad:good);
						dirtRoadCost.setColor(map.selectAdd[Road.DIRT.key]*Road.DIRT.cost>money?bad:good);
						pavedRoadCost.setColor(map.selectAdd[Road.PAVED.key]*Road.PAVED.cost>money?bad:good);
						railRoadCost.setColor(map.selectAdd[Road.RAIL.key]*Road.RAIL.cost>money?bad:good);
						airportCost.setColor(map.selectAddAirport*Road.AIRPORT.cost>money?bad:good);
						catapultCost.setColor(map.selectAddCatapult*Transport.CATAPULT_COST>money?bad:good);
						
						
						// draw everything above the map
						bottom.setRect(0, buffer.getHeight() - Overlay.BOTTOM_HEIGHT, buffer.getWidth(),
								Overlay.BOTTOM_HEIGHT);
						right.setRect(buffer.getWidth() - Overlay.RIGHT_WIDTH, 0, Overlay.RIGHT_WIDTH,
								buffer.getHeight() - Overlay.BOTTOM_HEIGHT);
						
						right.draw(g);
						bottom.draw(g);
						// g.setColor(Color.DARK_GRAY);

						// bottom.setBounds(0, 5*(buffer.getHeight()/6),
						// buffer.getWidth(), buffer.getHeight()/6 +6);

						break;
					}

					g.setColor(Color.RED);
					g.drawString(fps + " fps", 10, 20);

					if (debug) {
						g.drawString(Arrays.toString(Transport.currentUnits), 200, 15);
						g.drawString(Arrays.toString(Transport.debug()), 200, 30);
						g.drawString("lit:" + City.literacy, 100, 15);
						g.drawString("lifetime: " + moneyString(lifeTimeEarnings), 100, 40);
						String s = "";
						/*
						for(int i =0;i <bottom.elements.size(); i++){
							if(bottom.elements.get(i) == null){
								System.out.println("A");
							}
							if(bottom.elements.get(i).background == null){
								System.out.println("B");
							}
							s+=bottom.elements.get(i).background.getRed()+" ";
						}
						*/
						//g.drawString(s, 400, 700);
					}

					g.dispose();
					Graphics g2 = panel.getGraphics();
					g2.drawImage(buffer, 0, 0, null);
					g2.dispose();

					frames++;
					if (System.currentTimeMillis() - start >= 1000) {
						fps = frames;
						frames = 0;
						start = System.currentTimeMillis();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
	};
	public static double mailValue = .003;
	public static double populationGrowth = 1.0001;
	public static long tick = 0;
	public static double moneyPerTick;
	public static long POP_BUMP = 1000; // every 100 s
	public Thread physics = new Thread() {
		public void run() {
			try {
				while (true) {
					if (gameState.equals(TnT.ld.ld36.State.GAME)) {
						if (map.recalcFlag)
							map.calculateAllPaths();
						double litFactor = Math.pow(City.literacy + 1, 2.75);
						// calculate rate capacities for each city
						for (int i = 0; i < map.cities.size(); i++) {
							City c = map.cities.get(i);
							c.population *= populationGrowth;
							if (tick++ % POP_BUMP == 0) {
								for (int v = 0; v < 10; v++) {
									c.population *= populationGrowth;
								}
							}
							for (int j = 0; j < c.paths.size(); j++) {
								double cap = 0;
								Path[] paths = c.paths.get(j);
								Point last = null;
								for (int t = 0; t < Transport.baseUnits.length; t++) {
									Transport current = Transport.currentUnits[t];
									if (current != null && paths[t] != null) {
										cap += current.scalar / paths[t].length();
										last = paths[t].getLast();
									}
								}
								if (last != null && map.cities.get(i).distance(last) < Transport.CATAPAULT_RANGE.scalar) {
									cap += Transport.CATAPAULT.scalar * map.cities.get(i).catapults;
								}
								c.rateCapacity.set(j, (c == selectedCity && boosted ? 1.2 : 1) * cap);
							}
							if (map.cities.get(i).airport) {
								boolean past = false;
								for (int k = 0; k < map.cities.size() - 1; k++) {
									if (k == i) {
										past = true;
									}
									if (map.cities.get(k + (past?1:0)).airport) {
										c.rateCapacity.set(k, c.rateCapacity.get(k) + (c == selectedCity && boosted ? 1.2 : 1) * Transport.PLANE.scalar / map.cities.get(i).distance(map.cities.get(k)));
									}
								}
							}
						}

						// calculate desired rate for each city
						int totalPop = 0;
						HashMap<Point, Point> done1 = new HashMap<Point, Point>();
						HashMap<Point, Point> done2 = new HashMap<Point, Point>();
						for (int i = 0; i < map.cities.size(); i++) {
							totalPop += map.cities.get(i).population;
							for (int j = 0; j < Transport.baseUnits.length
									&& Transport.currentUnits[j] != null; j++) {
								if (tick % 700 / ((j + 1) * 5) == 0) {
									ArrayList<Path[]> paths = map.cities.get(i).paths;
									Point p;
									for (int k = 0; k < paths.size(); k++) {
										try {
											p = paths.get(k)[j].getLast();
										} catch (NullPointerException e) {
											continue;
										}
										if (!(done1.containsKey(map.cities.get(i)) && done1.get(map.cities.get(i)).equals(p)) && !(done2.containsKey(map.cities.get(i)) && done2.get(map.cities.get(i)).equals(p)) ){
											done1.put(map.cities.get(i), p);
											done2.put(p, map.cities.get(i));
											map.addAnimation(new Sprite(map, map.cities.get(i), paths.get(k)[j],
												Transport.currentUnits[j].img));
										}
										// System.out.println("Made sprite for "
										// + j + " in " +
										// map.cities.get(i).name);
									}
								}
							}
						}
						for (int i = 0; i < map.cities.size(); i++) {
							City c = map.cities.get(i);
							for (int j = 0; j < c.desiredRate.size(); j++) {
								c.desiredRate.set(j,
										(c == selectedCity && boosted ? 1.2 : 1) * litFactor
												* (double) (c.population / (totalPop - c.population)
														* map.cities.get(j + (j >= i ? 1 : 0)).population));
							}
						}
						// animate sprites

						for (int i = 0; i < map.anims.size(); i++) {
							map.anims.get(i).animate();
							if (map.anims.get(i).isDone()) {
								map.anims.remove(i--);
							}
						}

						double totalMail = 0;
						for (City city : map.cities) {
							totalMail += city.getMail();
						}
						totalMail *= mailValue;
						// System.out.println("Mail: " + totalMail);
						moneyPerTick = totalMail;
						money += totalMail;
						lifeTimeEarnings += totalMail;

						if (lifeTimeEarnings > Math.pow(7, map.cities.size() * 1.5)) {
							generateCity();
						}
						// System.out.println("Money: " + moneyString(money));
					}
					try {
						Thread.sleep(PHYSICS_DELAY);
					} catch (Exception e) {
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
	};
	public static final long PHYSICS_DELAY = 10;

	public void startGame() {
		map = Map.generate();

		treeButton.setRect(10, buffer.getHeight() - 70, 200, 50);

		// /*techTree.height*/sp.height = buffer.getHeight() -
		// Overlay.BOTTOM_HEIGHT;//setRect(0, 0, buffer.getWidth(),
		// buffer.getHeight());
		// sp.width = buffer.getWidth();
		sp.setRect(0, 0, buffer.getWidth(), buffer.getHeight() - Overlay.BOTTOM_HEIGHT);
		techTree.height = sp.innerSize().getHeight();
		// System.out.println(techTree.height);
		double BY = treeButton.getY();
		double BW = 50;
		double GAP = 25;

		double BX = buffer.getWidth() - 7 * GAP - 7 * BW;
		double RX = buffer.getWidth() - Overlay.RIGHT_WIDTH;

		clearSelection.setRect(BX, BY, BW, BW);
		addFootPath.setRect(BX + BW + GAP, BY, BW, BW);
		addDirtRoad.setRect(BX + 2 * BW + 2 * GAP, BY, BW, BW);
		addCatapalt.setRect(BX + 3 * BW + 3 * GAP, BY, BW, BW);
		addRailRoad.setRect(BX + 4 * BW + 4 * GAP, BY, BW, BW);
		addPavedRoad.setRect(BX + 5 * BW + 5 * GAP, BY, BW, BW);
		addAirport.setRect(BX + 6 * BW + 6 * GAP, BY, BW, BW);
		moneyOverlay.setRect(220, BY, BX - 10, BY);
		
		double h = -10;
		BX += BW/2;
		footPathCost = new OverlayText(BX + 1*BW + 1*GAP, BY + h, "test");
		dirtRoadCost = new OverlayText(BX + 2*BW + 2*GAP, BY + h, "test");
		catapultCost = new OverlayText(BX + 3*BW + 3*GAP, BY + h, "test");
		railRoadCost = new OverlayText(BX + 4*BW + 4*GAP, BY + h, "test");
		pavedRoadCost = new OverlayText(BX + 5*BW + 5*GAP, BY + h, "test");
		airportCost = new OverlayText(BX + 6*BW + 6*GAP, BY + h, "test");

		addFootPath.setRoad(Road.FOOTPATH);
		addDirtRoad.setRoad(Road.DIRT);
		addRailRoad.setRoad(Road.RAIL);
		addPavedRoad.setRoad(Road.PAVED);
		addAirport.setRoad(Road.AIRPORT);
		addCatapalt.setCatapult();

		cityName.setRect(RX, 0, Overlay.RIGHT_WIDTH, 100);
		cityPopulation.setRect(RX, 110, Overlay.RIGHT_WIDTH, 80);
		increase.setRect(RX, buffer.getHeight() - Overlay.BOTTOM_HEIGHT - 60, Overlay.RIGHT_WIDTH, 50);
		increase.enabled = false;

		bottom.addChild(treeButton);
		treeButton.setActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (sp.visible) {
					// techTree.visible = false;
					sp.visible = false;
					// bottom.visible = true;
					right.visible = true;
					treeButton.text = "Technology Tree";
				} else {
					// techTree.visible = true;
					sp.visible = true;
					// bottom.visible = false;
					right.visible = false;
					treeButton.text = "Close";
				}
			}
		});

		bottom.addChild(clearSelection);
		bottom.addChild(addFootPath);
		bottom.addChild(addDirtRoad);
		bottom.addChild(addCatapalt);
		bottom.addChild(addRailRoad);
		bottom.addChild(addPavedRoad);
		bottom.addChild(addAirport);
		bottom.addChild(moneyOverlay);
		
		bottom.addChild(footPathCost);
		bottom.addChild(dirtRoadCost);
		bottom.addChild(catapultCost);
		bottom.addChild(railRoadCost);
		bottom.addChild(pavedRoadCost);
		bottom.addChild(airportCost);

		right.addChild(cityName);
		right.addChild(cityPopulation);
		right.addChild(increase);

		clearSelection.setActionListener(addListener);
		addFootPath.setActionListener(addListener);
		addDirtRoad.setActionListener(addListener);
		addCatapalt.setActionListener(addListener);
		addRailRoad.setActionListener(addListener);
		addPavedRoad.setActionListener(addListener);
		addAirport.setActionListener(addListener);

		activeOverlays.add(bottom);
		activeOverlays.add(right);
		activeOverlays.add(sp);

		// OverlayScrollBar ttScroll = new OverlayScrollBar();
		// ttScroll.setRect(20,600,50,15);

		// techTree.addChild(ttScroll);
		// techTree.addChild(treeButton);

		TechTree.MAX_SCROLL = (int) (techTree.width - buffer.getWidth());
//		sp.setMaxHorizontalScroll(TechTree.MAX_SCROLL);
		techTree.addChild(moneyOverlay);
		// techTree.visible = false;
		sp.visible = false;
		sp.inner = techTree;
		right.setRect(buffer.getWidth() - Overlay.RIGHT_WIDTH, 0, Overlay.RIGHT_WIDTH, buffer.getHeight() - Overlay.BOTTOM_HEIGHT);
		
		

		gameState = State.GAME;
	}

	private City selectedCity = null;

	public void setSelectedCity(City c) {
		selectedCity = c;
		increase.enabled = true;
	}

	public void displayCityData() {
		if (selectedCity == null)
			return;
		City c = selectedCity;
		cityName.setText("Name: " + c.name);
		cityPopulation.setText("Pop: " + moneyString(c.population).substring(1));
	}

}
