package TnT.ld.ld36;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;

public class Tech extends OverlayButton{
	public static final int WIDTH = 100;//250;
	public static final int HEIGHT = 100;
	
	ArrayList<Tech> parents = new ArrayList<Tech>();
	String name;
	String description;
	int cost;
	boolean researched = false;
	//int x, y;//manual location/rendering?
	Transport[] targets;
	public double value;
	int depth = 0; //calculated
	int row = 0;
	public Tech(String name, String description, Transport ...targets ){
		super(name);
		this.background = Color.blue;
		this.removedBackground = Color.blue;
		this.enteredBackground = Color.CYAN;
		this.name = name;
		this.description = description;
		this.targets = targets;
		width = WIDTH;
		height = HEIGHT;
		this.enabled = false;
	}
	public boolean available(){
		for(int i=0; i<parents.size(); i++){
			if(!parents.get(i).researched){
				return false;
			}
		}
		return true;
	}
	public boolean canAfford(){
		return false;
	}
	public void research(){
		researched = true;
		for(Transport t : targets){
			t.scalar *= value;
		}
		
	}
	public void addParent(Tech t){
		parents.add(t);
		depth = Math.max(depth,  t.depth + 1);
	}
	public int round(double d){
		return (int) Math.round(d);
	}
	public void drawLine(Graphics2D g, double x1, double y1, double x2, double y2){
		g.drawLine(round(x1), round(y1), round(x2), round(y2));
	}
	public void draw(Graphics2D g){
		
		
		Color oc = g.getColor();
		if(available()){
			enabled = true;
			if(canAfford()){
				g.setColor(Color.GREEN);
			} else{
				g.setColor(Color.RED);
			}
		} else{
			g.setColor(Color.DARK_GRAY);
		}
		g.fill(this);
		
		g.setColor(Color.white);
		g.drawString(name, (int)(x+5), (int)(y+15));
		//draw path to parents!
		Stroke os = g.getStroke();
		g.setStroke(new BasicStroke(4));
		for(Tech p : parents){
			drawLine(g, x, y+height/2, x-TechTree.X_GAP, y+height/2);
			g.drawArc((int)(x-TechTree.X_GAP/2),(int)( y+height/2), TechTree.X_GAP, TechTree.X_GAP, 90, 90);//r to d
			drawLine(g, x-TechTree.X_GAP/2, y+height, x-TechTree.X_GAP/2, y+height+TechTree.Y_GAP);
			g.drawArc((int)(x-TechTree.X_GAP/2),(int)( y-height/2), TechTree.X_GAP, TechTree.X_GAP, 180, 90); //r to u
			drawLine(g, x-TechTree.X_GAP/2, y, x-TechTree.X_GAP/2, y-TechTree.Y_GAP);
			g.drawArc((int)(x-3*TechTree.X_GAP/2),(int)( y-height/2 -TechTree.Y_GAP), TechTree.X_GAP, TechTree.X_GAP, 0, 90);//b to l
			//g.drawArc((int)(x-3*TechTree.X_GAP/2),(int)( y+height/2 +TechTree.Y_GAP), TechTree.X_GAP, TechTree.X_GAP, 270, 359);//t to l
		}
		g.setStroke(os);
		g.setColor(oc);
	}
	
}
