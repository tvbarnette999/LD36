package TnT.ld.ld36;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class TechTree extends Overlay{

	public static final int X_GAP = Tech.HEIGHT; //squares!
	public static final int Y_GAP = 30;
	public static int MAX_COLUMN = 0;
	public static int MAX_SCROLL = 0;
	
	double scroll = 0;
	static ArrayList<Tech> heads = new ArrayList<Tech>();
	
	//instantiate all the techs here, pass them the list of ones they affect
	static Tech sandals = new Tech("Sandals", "Increase Runner speed by 10%.", Transport.RUNNER);
	static Tech shoeLaces = new Tech("Shoe Laces", "");
	static Tech shoes = new Tech("Shoes", "Increases Runner Speed by 10%.", Transport.RUNNER);
	static Tech agriculture = new Tech("Agriculture", "");
	static Tech paper = new Tech("Paper", "");
	static Tech ink = new Tech("Ink", "");
	static Tech husbandry = new Tech("Husbandry", "");
	static Tech mining = new Tech("Mining", "");
	static Tech metal = new Tech("Metal", "");
	static Tech leather = new Tech("Leather", "");
	static Tech snacks = new Tech("Snacks", "");
	static Tech exercise = new Tech("Exercise", "");
	static Tech protein = new Tech("Protein", "");
	static Tech gym = new Tech("Gym", "");
	static Tech horses = new Tech("Horses", "");
	static Tech saddles = new Tech("Saddles", "");
	static Tech saddleBags = new Tech("Saddle Bags", "");
	static Tech horseFood = new Tech("Horse Food", "");
	static Tech horseShoes = new Tech("Horseshoes", "Increases horse speed by 10%", Transport.HORSE, Transport.CARRIAGE, Transport.CARAVAN);
	static Tech wheel = new Tech("The Wheel", "");
	static Tech weights = new Tech("Weight Training", "");
	
	
	//static Tech onagers = new Tech("Onagers", "Increases catapault range by 20%",  Transport.CATAPAULT_RANGE);
	
	static{
		
//add everything's row first
		saddles.row = 0;
		shoeLaces.row = 0;
		snacks.row = 0;
		exercise.row = 0;
		protein.row = 0;
		gym.row = 0;
		weights.row = 0;
		
		agriculture.row = 1;
		paper.row = 1;
		ink.row = 1;
		leather.row = 1;
		husbandry.row = 1;
		mining.row = 1;
		metal.row = 1;
		wheel.row = 1;
		
		horses.row = 2;
		saddles.row = 2;
		saddleBags.row = 2;
		horseFood.row = 2;
		horseShoes.row = 2;
		
		
		
		
		
		//add child only after all parents have been added to their parents!
		heads.add(sandals);
		heads.add(agriculture);
		
		paper.addParent(agriculture);
		ink.addParent(paper);
		husbandry.addParent(ink);
		leather.addParent(husbandry);
		mining.addParent(leather);
		metal.addParent(mining);
		wheel.addParent(metal);
		
		
		shoes.addParent(sandals);
		shoeLaces.addParent(shoes);
		snacks.addParent(shoeLaces);
		exercise.addParent(snacks);
		protein.addParent(exercise);
		gym.addParent(protein);
		weights.addParent(gym);
		weights.addParent(metal);
		
		
		
		
		
		horses.addParent(husbandry);
		saddles.addParent(horses);
		saddles.addParent(leather);
		saddleBags.addParent(saddles);
		horseFood.addParent(saddleBags);
		horseShoes.addParent(horseFood);
		horseShoes.addParent(metal);
		
		
		
		
		
//		System.out.println(weights.depth);
		
		
	}
	
	public TechTree(){		
		//add all Techs as children
		for(Field f:this.getClass().getDeclaredFields()){
			if(f.getType().equals(Tech.class)){
				try {
					Tech t = (Tech) f.get(null);
					t.x = X_GAP + (t.depth * (X_GAP + Tech.WIDTH));
					t.y = Y_GAP + (t.row * (Y_GAP + Tech.HEIGHT));
					MAX_COLUMN = Math.max(MAX_COLUMN, t.depth);
					this.addChild(t);
					System.out.println(t.name+":"+t.x+", "+t.y);
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		}
		
		
		this.width = (MAX_COLUMN+1)*(X_GAP+Tech.WIDTH)+X_GAP;
//		height = LD36.theLD.buffer.getHeight();
//		System.out.println(elements.size());
	}
	
	long lastTime = -1;
	long current = -1;
	public void draw(Graphics2D g){

		MAX_SCROLL = (int) (this.width - LD36.theLD.buffer.getWidth());
		long current = System.nanoTime();
		if (lastTime > 0) {
			double dt = 500 * (current-lastTime) * 1e-9 ;
			if (LD36.rightPressed) scroll += dt;
			if (LD36.left) scroll -= dt;
			if(scroll<0)scroll = 0;
			if(scroll > MAX_SCROLL)scroll = MAX_SCROLL;
		}
		lastTime = current;
		
		g.translate(-scroll,0);
		super.draw(g);
		Color oc = g.getColor();
		g.setColor(Color.white);
//		for(int y = 0; y < LD36.theLD.buffer.getHeight(); y+=Tech.HEIGHT + Y_GAP){
//			g.drawLine(0, y, MAX_COLUMN*(Tech.WIDTH+X_GAP), y);
//			g.drawLine(0, y+Y_GAP, MAX_COLUMN*(Tech.WIDTH+X_GAP), y+Y_GAP);
//			
//		}
//		
//		for(int x = 0; x < MAX_COLUMN * (Tech.WIDTH+X_GAP); x+=Tech.WIDTH+X_GAP){
//			g.drawLine(x, 0, x, LD36.theLD.buffer.getHeight());
//			g.drawLine(x+X_GAP, 0,x+X_GAP , LD36.theLD.buffer.getHeight());
//			
//		}
		g.setColor(oc);
		g.translate(scroll, 0);
		
	}
	
	public void mouseMoved(MouseEvent e){
		super.mouseMoved(new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiers(), e.getX()+(int)scroll, e.getY(), e.getXOnScreen(), e.getYOnScreen(), e.getClickCount(), e.isPopupTrigger(), e.getButton()));
	}
	public void mousePressed(MouseEvent e){
		super.mousePressed(new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiers(), e.getX()+(int)scroll, e.getY(), e.getXOnScreen(), e.getYOnScreen(), e.getClickCount(), e.isPopupTrigger(), e.getButton()));
	}
	public void mouseReleased(MouseEvent e){
		super.mouseReleased(new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiers(), e.getX()+(int)scroll, e.getY(), e.getXOnScreen(), e.getYOnScreen(), e.getClickCount(), e.isPopupTrigger(), e.getButton()));
	}
	public void mouseClicked(MouseEvent e){
		super.mouseClicked(new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiers(), e.getX()+(int)scroll, e.getY(), e.getXOnScreen(), e.getYOnScreen(), e.getClickCount(), e.isPopupTrigger(), e.getButton()));
	}
	
}
