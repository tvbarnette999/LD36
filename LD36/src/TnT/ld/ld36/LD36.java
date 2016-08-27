package TnT.ld.ld36;//TODO TnTWizard.ld.ld36? or TnT.wizard.ld.ld36?

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;



//LOOK AT WHITEBOARD DUNCAN! it has my ideas for gui. i didnts get as far as i hoped


public class LD36 extends JFrame{

	State gameState = State.MAIN;
	JPanel panel = new JPanel();
	Map map;
	BufferedImage buffer;
	MouseAdapter adapter = new MouseAdapter(){
		public void mouseClicked(MouseEvent e){
			switch(gameState){
				case MAIN:
					startGame();
					break;
				case GAME:
					//handle anything above the map here, then forward to the map
					map.mouseClicked(e);
			}
		}
		
	};
	public static void main(String[] args) {
		LD36 theLD = new LD36();
		theLD.initGUI();		
	}

	public void initGUI() {
		//Fullscreen?
		panel.setPreferredSize(new Dimension(1280, 768));
		buffer = new BufferedImage(1280, 768, BufferedImage.TYPE_4BYTE_ABGR);
		panel.addMouseListener(adapter);
		panel.addMouseMotionListener(adapter);
		this.add(panel);
		this.pack();
		this.setDefaultCloseOperation(3);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
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
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());
				g.setColor(Color.BLACK);
				switch(gameState){
					case MAIN:
						g.drawString("Click To Start", 400, 400);
						break;
					case GAME:
						map.draw(g, buffer.getWidth(), buffer.getHeight());
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
	}

}
