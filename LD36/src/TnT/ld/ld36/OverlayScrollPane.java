package TnT.ld.ld36;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

public class OverlayScrollPane extends Overlay{
	Overlay hbar = new Overlay();
	Overlay inner;
	boolean hactive = false;
	int hoffset = 0;
	int hscroll = 0;
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
		}
		super.mousePressed(e);
		
	}
	public void mouseReleased(MouseEvent e){
		if(hactive){
			hactive = false;
			return;
		}
		super.mouseReleased(e);
	}
	public void mouseDragged(MouseEvent e){
		System.out.println(e.getX());
		if(hactive){
			//do the stuff
			hbar.x = e.getX()-hoffset;
			hscroll = (int) (hmaxscroll * ((this.width-hbar.width)/hbar.x));
			return;
		}
		super.mouseDragged(e);
		
	}
	public Dimension innerSize(){
		return new Dimension((int)this.width,(int)( this.height-hbar.height));
	}
	public void draw(Graphics2D g){
		Color oc = g.getColor();
		
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
