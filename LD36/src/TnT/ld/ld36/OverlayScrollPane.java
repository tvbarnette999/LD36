package TnT.ld.ld36;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class OverlayScrollPane extends Overlay{
	Overlay hbar = new Overlay();
	Overlay inner;
	boolean hactive = false;
	int hoffset = 0;
	double hscroll = 0;
	int hmaxscroll = 0;
	public OverlayScrollPane(){
		hbar.height = 15;
		hbar.width = 50;
	}
	public void setMaxHorizontalScroll(int max){
		this.hmaxscroll = max;
		hbar.width = 50;
	}
	public void mousePressed(MouseEvent e){
		if(hbar.contains(e.getPoint())){
			hactive = true;
			hoffset = (int) (e.getX() -hbar.x);
			return;
		} else{
			inner.mousePressed(new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiers(), e.getX()+(int)hscroll, e.getY(), e.getXOnScreen(), e.getYOnScreen(), e.getClickCount(), e.isPopupTrigger(), e.getButton()));
			
		}
		
	}
	public void addChild(Overlay c){
		throw new RuntimeException("Dont Add stuff to ScrollPanes!!!! Use .inner!");
	}
	public void mouseReleased(MouseEvent e){
		if(hactive){
			hactive = false;
			return;
		} else{
			inner.mouseReleased(new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiers(), e.getX()+(int)hscroll, e.getY(), e.getXOnScreen(), e.getYOnScreen(), e.getClickCount(), e.isPopupTrigger(), e.getButton()));
			
		}
	}
	public void mouseClicked(MouseEvent e){
		if(inner!=null)inner.mouseClicked(new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiers(), e.getX()+(int)hscroll, e.getY(), e.getXOnScreen(), e.getYOnScreen(), e.getClickCount(), e.isPopupTrigger(), e.getButton()));
	}
	public void mouseDragged(MouseEvent e){
		System.out.println(hscroll+" of "+hmaxscroll+":"+(hbar.x/(this.width-hbar.width)));
		if(hactive){
			//do the stuff
			hbar.x = e.getX()-hoffset;
			if(hbar.x<0){
				hbar.x=0;
			}
			if(hbar.x>hmaxscroll-hbar.width){
				hbar.x = hmaxscroll-hbar.width;
			}
			calculateScroll();
			return;
		}
		inner.mouseDragged(e);
		
	}
	public void mouseMoved(MouseEvent e){
		inner.mouseMoved(new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiers(), e.getX()+(int)hscroll, e.getY(), e.getXOnScreen(), e.getYOnScreen(), e.getClickCount(), e.isPopupTrigger(), e.getButton()));
		
	}
	public void calculateScroll(){
		hscroll =  (hmaxscroll * (hbar.x/(this.width-hbar.width)));
	}
	public Dimension innerSize(){
		return new Dimension((int)this.width,(int)( this.height-hbar.height));
	}
	public void draw(Graphics2D g){
		
		if(LD36.rightPressed && !hactive ){
			hbar.x +=5;
			if(hbar.x > this.hmaxscroll-hbar.width){
				hbar.x = this.hmaxscroll - hbar.width;
			}
			calculateScroll();
			
		}
		else if(LD36.left && !hactive){
			hbar.x -=5;
			if(hbar.x < 0){
				hbar.x = 0;
			}
			calculateScroll();
		}
		
		Color oc = g.getColor();
		
		//hideclass oe in case it changes
		double hscroll = this.hscroll;
		double vscroll;
		g.translate(-hscroll, 0);
		super.draw(g);
//		System.out.println(height+":"+hbar.x+","+hbar.y+","+hbar.width+","+hbar.height);
		inner.draw(g);
		
		g.translate(hscroll, 0);
		g.setColor(Color.red);
		g.fill(hbar);
		g.setColor(oc);
	}
	public void setRect(int x, int y, int w, int h){
		super.setRect(x,y,w,h);
		hbar.x = 1;
		hbar.y = height-hbar.height;
	}
	
	
	
	
}
