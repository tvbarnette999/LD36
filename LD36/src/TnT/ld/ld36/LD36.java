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
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;



//LOOK AT WHITEBOARD DUNCAN! it has my ideas for gui. i didnts get as far as i hoped


public class LD36 extends JFrame{
	public static LD36 theLD;
	State gameState = State.MAIN;
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
	
	ActionListener addListener = new ActionListener(){
		public void actionPerformed(ActionEvent e){
			OverlayButton o = (OverlayButton) e.getSource();
			Road road = null;
			if (o==addFootPath) road = Road.FOOTPATH;
			if (o==addDirtRoad) road = Road.DIRT;
			if (o==addPavedRoad) road = Road.PAVED;
			if (o==addRailRoad) road = Road.RAIL;
			if (road != null) {
				
			} else if (o==addCatapalt) {
				
			} else if (o==addAirport) {
				
			}
		}
	};
	
	TechTree techTree = new TechTree();
	ArrayList<Overlay> activeOverlays = new ArrayList<Overlay>();
	public double money = 1e9;
	
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
				
			}
		}
	};
	
	public void startGame(){
		map = Map.generate();
		gameState = State.GAME;
		
		treeButton.setRect(10, buffer.getHeight() - 100, 200, 50);
		
		techTree.setRect(0, 0, buffer.getWidth(), buffer.getHeight());
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
		
	}

}
