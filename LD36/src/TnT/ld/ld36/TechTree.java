package TnT.ld.ld36;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class TechTree extends Overlay{

	public static final int X_GAP = 100;
	public static final int Y_GAP = 30;
	public int MAX_COLUMN = 100;
	
	static ArrayList<Tech> heads = new ArrayList<Tech>();
	
	//instantiate all the techs here, pass them the list of ones they affect
	static Tech shoes = new Tech("shoes", "Increases Runner Speed by 10%.", 100, Tech.ADD, .1, Transport.RUNNER);
	static Tech horseShoes = new Tech("Horseshoes", "Increases horse speed by 10%", 1000, Tech.ADD, .1, Transport.HORSE, Transport.CARRIAGE, Transport.CARAVAN);
	static Tech onagers = new Tech("Onagers", "Increases catapault range by 20%", 100000, Tech.MUL, 1.2, Transport.CATAPAULT_RANGE);
	static{
		heads.add(shoes);
		horseShoes.addParent(shoes);
	}
	
	public TechTree(){
		
	}
	
	public void draw(Graphics2D g){
		super.draw(g);
		Color oc = g.getColor();
		g.setColor(Color.white);
		for(int y = 0; y < LD36.theLD.buffer.getHeight(); y+=Tech.HEIGHT){
			g.drawLine(0, y, MAX_COLUMN*(Tech.WIDTH+X_GAP), y);
			g.drawLine(0, y+Y_GAP, MAX_COLUMN*(Tech.WIDTH+X_GAP), y+Y_GAP);
			
		}
		
		for(int x = 0; x < MAX_COLUMN * (Tech.WIDTH+X_GAP); x+=Tech.WIDTH){
			g.drawLine(x, 0, x, LD36.theLD.buffer.getHeight());
			g.drawLine(x+X_GAP, 0,x+X_GAP , LD36.theLD.buffer.getHeight());
			
		}
		
	}
	
}
