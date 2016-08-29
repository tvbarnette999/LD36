package TnT.ld.ld36;//TODO TnTWizard.ld.ld36? or TnT.wizard.ld.ld36?

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;



//LOOK AT WHITEBOARD DUNCAN! it has my ideas for gui. i didnts get as far as i hoped


public class LD36 extends JFrame{
	public static LD36 theLD;
	volatile State gameState = State.MAIN;
	JPanel panel = new JPanel();
	Map map;
	VolatileImage buffer;
	Overlay bottom = new Overlay();
	Overlay right = new Overlay();
	OverlayButton treeButton = new OverlayButton("Technology Tree");
	OverlayButton clearSelection = new OverlayButton("Clear");
	OverlayButton addFootPath = new OverlayButton("Footpath");
	OverlayButton addDirtRoad = new OverlayButton("Dirt Road");
	OverlayButton addRailRoad = new OverlayButton("Railroad");
	OverlayButton addPavedRoad = new OverlayButton("Paved Road");
	OverlayButton addCatapalt = new OverlayButton("Catapalt");
	OverlayButton addAirport = new OverlayButton("Airport");

	OverlayButton addSelected = clearSelection;

	private static final DecimalFormat smallFormat = new DecimalFormat("###,###,###.##");
	private static final DecimalFormat longFormat = new DecimalFormat("###.#");
	private static final String[] small = new String[] {"m", "b", "tr", "quadr", "quint", "sext", "sept", "oct", "non", "dec"};
	private static final String[] ones = new String[] { "un", "duo", "tre", "quattuor", "quinqua", "se", "septe", "octo", "nove"};
	private static final String[] tens = new String[] { "deci", "viginti", "triginta", "quadraginta", "quinquaginta", "sexaginta", "septuaginta", "octoginta", "nonaginta"};
	private static final String[] hundreds = new String[] {"centi", "ducenti", "trecenti", "quadringenti", "quingenti", "sescenti", "septingenti", "octingenti", "nongenti"};
	private static final String[] treSpecialTens = new String[]{"", "s", "s", "s", "s", "", "", "", ""};
	private static final String[] treSpecialHundreds = new String[]{"", "", "s", "s", "s", "", "", "", ""};
	private static final String[] seSpecialTens = new String[]{"", "", "s", "s", "s", "", "", "x", ""};
	private static final String[] seSpecialHundreds = new String[]{"x", "", "s", "s", "s", "", "", "x", ""};
	private static final String[] septeNoveSpecialTens = new String[]{"n", "m", "n", "n", "n", "n", "n", "m", ""};
	private static final String[] septeNoveSpecialHundreds = new String[]{"n", "n", "n", "n", "n", "n", "n", "m", ""};
	//TODO return how to display varying sizes of money
	public static String moneyString(double x){
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
				//			thousand = log / 1000;
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
			if (One == 2) { //tre
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
			if (Ten == 2) { //tre
				s2 = treSpecialHundreds[Hundred];
			}
			if (Ten == 5) {
				s2 = seSpecialHundreds[Hundred];
			}
			if (Ten == 6 || One == 8) {
				s2 = septeNoveSpecialHundreds[Hundred];
			}
		}
		String start = " " + one + s1 + ten + s2+ hundred;
		if (endsEven(start)){
			start = start.substring(0, start.length() - 1);
		}
		return longFormat.format(x / Math.pow(10, initlog * 3 + 3)) + start + "illion";
	}
	/**
	 * Does not include o bc reasons
	 * @param s
	 * @return
	 */
	@Deprecated
	private static boolean endsEven(String s) {
		return s.length() > 0 && "aeiu".indexOf(s.charAt(s.length() - 1)) >= 0;
	}
	ActionListener addListener = new ActionListener(){
		public void actionPerformed(ActionEvent e){
			OverlayButton o = (OverlayButton) e.getSource();
			if (o==clearSelection) {
				map.clearSelection();
				return;
			}
			Road road = null;
			if (o==addFootPath) road = Road.FOOTPATH;
			if (o==addDirtRoad) road = Road.DIRT;
			if (o==addPavedRoad) road = Road.PAVED;
			if (o==addRailRoad) road = Road.RAIL;
			if (road != null) {
				System.out.println(e.getModifiers());
				if (e.getModifiers()==16 && money > road.cost * map.selectAdd[road.key]) {
					map.buildSelection(road);
					map.clearSelection();
				} else if (e.getModifiers()==4) {
					map.sellSelection(road);
					map.clearSelection();
				}
			} else if (o==addCatapalt) {

			} else if (o==addAirport) {

			}
		}
	};

	TechTree techTree = new TechTree();
	ArrayList<Overlay> activeOverlays = new ArrayList<Overlay>();
	public double money = 1e5;

	MouseAdapter adapter = new MouseAdapter(){
		public void mouseClicked(MouseEvent e){
			//TODO consider ignoring game state and use overlays for menu?
			switch(gameState){
			case MAIN:
				startGame();
				break;
			case GAME:
				//handle anything above the map here, then forward to the map
				for(Overlay o :activeOverlays){
					if(!o.visible) continue;
					if(o.contains(e.getPoint())){
						o.mouseClicked(e);
						return;
					}
				}
				map.mouseClicked(e);
			}
		}
		public void mousePressed(MouseEvent e){
			switch(gameState){
			case MAIN:
				break;
			case GAME:
				//handle anything above the map here, then forward to the map
				for(Overlay o :activeOverlays){
					if(!o.visible) continue;
					if(o.contains(e.getPoint())){
						o.mousePressed(e);
						return;
					}
				}
				map.mousePressed(e);
			}
		}
		public void mouseReleased(MouseEvent e){
			switch(gameState){
			case MAIN:
				break;
			case GAME:
				//handle anything above the map here, then forward to the map
				for(Overlay o :activeOverlays){
					if(!o.visible) continue;
					if(o.contains(e.getPoint())){
						o.mouseReleased(e);
					}
				}
			}
		}
		public void mouseMoved(MouseEvent e){
			//			System.out.println(e.getPoint());
			switch(gameState){
			case MAIN:
				break;
			case GAME:
				//handle anything above the map here, then forward to the map
				for(Overlay o :activeOverlays){
					if(!o.visible) continue;
					if(o.contains(e.getPoint())){
						if(!o.mouseIn){
							o.mouseIn = true;
							o.mouseEntered(new MouseEvent(panel, MouseEvent.MOUSE_ENTERED, e.getWhen(), 0, e.getX(), e.getY(), e.getXOnScreen(), e.getYOnScreen(), 0, false, 0));
						}
						o.mouseMoved(e);
					} else{
						if(o.mouseIn){
							o.mouseIn = false;
							o.mouseExited(new MouseEvent(panel, MouseEvent.MOUSE_EXITED, e.getWhen(), 0, e.getX(), e.getY(), e.getXOnScreen(), e.getYOnScreen(), 0, false, 0));
						}
					}
				}
				//					map.mouseMoved(e);
			}
		}
		public void mouseDragged(MouseEvent e){
			switch(gameState){
			case MAIN:
				break;
			case GAME:
				//handle anything above the map here, then forward to the map
				for(Overlay o :activeOverlays){
					if(!o.visible) continue;
					if(o.contains(e.getPoint())) return;
				}
				map.mouseDragged(e);
			}
		}
		public void mouseWheelMoved(MouseWheelEvent e){
			switch(gameState){
			case MAIN:
				break;
			case GAME:
				//handle anything above the map here, then forward to the map
				for(Overlay o :activeOverlays){
					if(!o.visible) continue;
					if(o.contains(e.getPoint())) return;
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
			case KeyEvent.VK_ESCAPE:
				if(treeButton.callback != null && techTree.visible)treeButton.callback.actionPerformed(new ActionEvent(treeButton, 0 ,""));
			}
		}

		public void keyReleased(KeyEvent e) {
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
			}
		}
	};

	public static void main(String[] args) {
		theLD = new LD36();
		theLD.initGUI();		
	}

	public void initGUI() {
		//Fullscreen?
		panel.setPreferredSize(new Dimension(1280, 768));
		//		buffer = new BufferedImage(1280, 768, BufferedImage.TYPE_4BYTE_ABGR);
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

	public Thread graphics = new Thread(){
		long start = System.currentTimeMillis();
		int fps = 0;
		int frames = 0;
		public void run(){
			while(true){
				Graphics2D g = buffer.createGraphics();
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());
				g.setColor(Color.BLACK);
				switch(gameState){
				case MAIN:
					g.setColor(Color.white);
					g.drawString("Click To Start", 400, 400);
					break;
				case GAME:
					if(techTree.visible){
						techTree.draw(g);

						break;
					}
					map.draw(g);

					//draw everything above the map
					bottom.setRect(0, buffer.getHeight() - Overlay.BOTTOM_HEIGHT, buffer.getWidth(), Overlay.BOTTOM_HEIGHT);
					right.setRect(buffer.getWidth() - Overlay.RIGHT_WIDTH, 0, Overlay.RIGHT_WIDTH, buffer.getHeight() - Overlay.BOTTOM_HEIGHT);
					right.draw(g);
					bottom.draw(g);
					//	g.setColor(Color.DARK_GRAY);

					//bottom.setBounds(0, 5*(buffer.getHeight()/6), buffer.getWidth(), buffer.getHeight()/6 +6);


					break;
				}

				g.setColor(Color.RED);
				g.drawString(fps+" fps", 10, 20);

				g.dispose();
				Graphics g2 = panel.getGraphics();
				g2.drawImage(buffer, 0, 0, null);
				g2.dispose();

				frames++;
				if(System.currentTimeMillis() - start >= 1000){
					fps = frames;
					frames = 0;
					start = System.currentTimeMillis();
				}
			}
		}
	};
	public Thread physics = new Thread(){
		public void run(){
			while(true){
				if (gameState.equals(TnT.ld.ld36.State.GAME)) {
					if (map.recalcFlag) map.calculateAllPaths();

					// calculate rate capacities for each city
					for (int i = 0; i < map.cities.size(); i++) {
						City c = map.cities.get(i);
						for (int j = 0; j < c.paths.size()-1; j++) {
							double cap = 0;
							Path[] paths = c.paths.get(j);
							for (int t = 0; t < Transport.baseUnits.length; t++) {
								Transport current = Transport.currentUnits[t];
								if (current != null && paths[t] != null) {
									cap += current.scalar / paths[t].length();
								}
							}
							c.rateCapacity.set(j, cap);
						}
					}

					//calculate desired rate for each city
					int totalPop = 0;
					for (int i = 0; i < map.cities.size(); i++) totalPop += map.cities.get(i).population;
					for (int i = 0; i < map.cities.size(); i++) {
						City c = map.cities.get(i);
						for (int j = 0; j < c.desiredRate.size(); j++) {
							c.desiredRate.set(j, (double) (c.population/(totalPop-c.population)*map.cities.get(j+(j>=i?1:0)).population));
						}
					}
					System.out.println(map.cities.get(0).rateCapacity);
					System.out.println(map.cities.get(0).desiredRate);
				}
				try {
					Thread.sleep(10);
				} catch (Exception e) {}
			}
		}
	};

	public void startGame(){
		map = Map.generate();

		treeButton.setRect(10, buffer.getHeight() - 100, 200, 50);

		techTree.height = buffer.getHeight();//setRect(0, 0, buffer.getWidth(), buffer.getHeight());
		double BY = buffer.getHeight() - 100;
		double BW = 50;
		double GAP = 25;

		double BX = buffer.getWidth() - 7*GAP - 7*BW;
		clearSelection.setRect(BX, BY, BW, BW);
		addFootPath.setRect(BX+BW+GAP, BY, BW, BW);
		addDirtRoad.setRect(BX+2*BW+2*GAP, BY, BW, BW);
		addCatapalt.setRect(BX+3*BW+3*GAP, BY, BW, BW);
		addRailRoad.setRect(BX+4*BW+4*GAP,  BY, BW, BW);
		addPavedRoad.setRect(BX+5*BW+5*GAP,BY,BW,BW);
		addAirport.setRect(BX+6*BW+6*GAP, BY, BW, BW);

		bottom.addChild(treeButton);
		treeButton.setActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(techTree.visible){
					techTree.visible = false;
					bottom.visible = true;
					right.visible = true;
					treeButton.text = "Technology Tree";
					treeButton.y-=30;
				} else{
					techTree.visible = true;
					bottom.visible = false;
					right.visible = false;
					treeButton.text = "Close";
					treeButton.y+=30;
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

		clearSelection.setActionListener(addListener);
		addFootPath.setActionListener(addListener);
		addDirtRoad.setActionListener(addListener);
		addCatapalt.setActionListener(addListener);
		addRailRoad.setActionListener(addListener);
		addPavedRoad.setActionListener(addListener);
		addAirport.setActionListener(addListener);


		activeOverlays.add(bottom);
		activeOverlays.add(right);
		activeOverlays.add(techTree);

		techTree.addChild(treeButton);
		techTree.visible = false;

		gameState = State.GAME;
	}

}
