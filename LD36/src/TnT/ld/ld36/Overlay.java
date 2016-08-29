package TnT.ld.ld36;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * Each type of overlay element is an Overlay object, and each one overrides whichever listener methods it needs calling super. 
 * When one uses the event, it needs to consume it so elements further up the hierarchy know its not actually theirs.
 * @author Thomas
 *
 */
public class Overlay extends Rectangle2D.Double implements MouseListener, MouseMotionListener{
	public static final int BOTTOM_HEIGHT = 150;
	public static final int RIGHT_WIDTH = 200;
	
	public ArrayList<Overlay> elements = new ArrayList<Overlay>();
	Color background = Color.LIGHT_GRAY;
	boolean visible = true;
	boolean mouseIn = false;
	boolean enabled = true;
	Color disabledBackground = Color.darkGray.brighter().brighter();
	public String text;
	public Overlay(String string) {
		setText(string);
	}
	public Overlay() {
		
	}
	public void draw(Graphics2D g){
		if(!visible){
			return;
		}
		Color oc = g.getColor();
		g.setColor(enabled?background:disabledBackground);
		g.fill(this);
		
		for(Overlay o : elements){
			o.draw(g);
		}
		
		if (text != null && ! (this instanceof OverlayButton)) {
			Font of = g.getFont();
			g.setColor(Color.black);
			g.setFont(of.deriveFont(24f));
			g.drawString(text,(int)( x+width/2 - g.getFontMetrics().stringWidth(text)/2.0),(int)(y+ height/2.0));
			g.setColor(oc);
			g.setFont(of);
		}
		
	}
	public void setText(String text) {
		this.text = text;
	}
	public void addChild(Overlay o){
		elements.add(o);
		
	}
	public Overlay getEventTarget(MouseEvent e){
		for(Overlay o : elements){
			if(o.contains(e.getPoint())) return o;
		}
		return null;
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("CLICK");
		Overlay target = getEventTarget(e);
		if(target != null) target.mouseClicked(e);
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		System.out.println("ENTER");
		Overlay target = getEventTarget(e);
		if(target != null) target.mouseEntered(e);
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		System.out.println("exit!");
		Overlay target = getEventTarget(e);
		if(target != null) target.mouseExited(e);
		for(Overlay o : elements){
			if(o.mouseIn){
				o.mouseIn = false;
				o.mouseExited(new MouseEvent(LD36.theLD.panel, MouseEvent.MOUSE_EXITED, e.getWhen(), 0, e.getX(), e.getY(), e.getXOnScreen(), e.getYOnScreen(), 0, false, 0));
			}
		}
	}
	@Override
	public void mousePressed(MouseEvent e) {
		Overlay target = getEventTarget(e);
		if(target != null) target.mousePressed(e);
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		Overlay target = getEventTarget(e);
		if(target != null) target.mouseReleased(e);
		
		
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		Overlay target = getEventTarget(e);
		if(target != null) target.mouseDragged(e);
		
		System.out.println("MOVE"+e.getButton());
		
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		Overlay target = getEventTarget(e);
		if(target != null){
			if(!target.mouseIn){
				target.mouseIn = true;
				target.mouseEntered(new MouseEvent(LD36.theLD.panel, MouseEvent.MOUSE_ENTERED, e.getWhen(), 0, e.getX(), e.getY(), e.getXOnScreen(), e.getYOnScreen(), 0, false, 0));
			}
			target.mouseMoved(e);
		}
		for(Overlay o : elements){
			if(o.mouseIn){
				if(target == null || target != o){
					o.mouseIn = false;
					o.mouseExited(new MouseEvent(LD36.theLD.panel, MouseEvent.MOUSE_EXITED, e.getWhen(), 0, e.getX(), e.getY(), e.getXOnScreen(), e.getYOnScreen(), 0, false, 0));
				}
			}
		}
	}
}
