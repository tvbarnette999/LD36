package TnT.ld.ld36;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class OverlayScrollPane extends Overlay{
	Overlay hbar = new Overlay();
	Overlay vbar = new Overlay();
	Overlay inner;
	boolean hactive = false;
	boolean vactive = false;
	int hoffset = 0;
	double hscroll = 0;
	int hmax = 0;
	int voffset = 0;
	double vscroll = 0;
	int vmaxscroll = 0;
	int hmin = 0;
	int vmin = 0;
	int vmax = 0;
	long lastScroll = 0;
	
		
	public OverlayScrollPane(){
		hbar.height = 15;
		hbar.width = 50;
		vbar.width = 15;
		vbar.height = 50;
		
	}
	public void setMaxHorizontalScroll(int max){
		this.hmax = max;
		hbar.width = 50;
	}
	public void setMaxVerticalScroll(int max){
		this.vmax = max;
		vbar.height = 50;
	}
	public void mousePressed(MouseEvent e){
		if(hbar.contains(e.getPoint())){
			hactive = true;
			hoffset = (int) (e.getX() -hbar.x);
			return;
		} else if (vbar.contains(e.getPoint())){
			vactive = true;
			voffset = (int)(e.getY() - hbar.y);
		}
		else{
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
		} else if(vactive){
			vactive = false;
		}
		else{
			inner.mouseReleased(new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiers(), e.getX()+(int)hscroll, e.getY(), e.getXOnScreen(), e.getYOnScreen(), e.getClickCount(), e.isPopupTrigger(), e.getButton()));
			
		}
	}
	public void mouseClicked(MouseEvent e){
		if(inner!=null)inner.mouseClicked(new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiers(), e.getX()+(int)hscroll, e.getY(), e.getXOnScreen(), e.getYOnScreen(), e.getClickCount(), e.isPopupTrigger(), e.getButton()));
	}
	public void mouseDragged(MouseEvent e){
//		System.out.println(hscroll+" of "+hmax+":"+(hbar.x/(this.width-hbar.width)));
		if(hactive){
			//do the stuff
			hbar.x = e.getX()-hoffset;
			if(hbar.x<y){
				hbar.x=y;
			}
			if(hbar.x>width-hbar.width){
				hbar.x = width-hbar.width;
			}
			calculateScroll();
			return;
		}
		if(vactive ){
			vbar.y = e.getY()-voffset;
			if(vbar.y<y){
				vbar.y=y;
			}
			if(vbar.y>height - vbar.height){
				vbar.y = height -vbar.height;
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
		hscroll =  (hmax * (hbar.x/(this.width-hbar.width)));
		vscroll =  (vmax * (vbar.y/(this.height-vbar.height)));
	}
	public Dimension innerSize(){
		return new Dimension((int)this.width,(int)( this.height-hbar.height));
	}
	public void draw(Graphics2D g){
		this.hmax = (int) (inner.width - innerSize().getWidth());
		this.vmax = (int) (inner.height - innerSize().getHeight());
		
//		System.out.println(hmax+", "+hscroll);
		if(System.currentTimeMillis() - lastScroll > 30){
			if(LD36.rightPressed && !hactive ){
				hbar.x +=5;
				if(hbar.x > width -hbar.width){
					hbar.x = width - hbar.width;
				}
				calculateScroll();
				
			}
			else if(LD36.left && !hactive){
				hbar.x -=5;
				if(hbar.x < x){
					hbar.x = x;
				}
				calculateScroll();
			}
			if(LD36.down && ! vactive){
				vbar.y+=5;
				if(vbar.y > height-vbar.height){
					vbar.y = height-vbar.height;
				}
				calculateScroll();
			} else if(LD36.up && ! vactive){
				vbar.y-=5;
				if(vbar.y < y){
					vbar.y = y;
				}
				calculateScroll();
			}
			lastScroll = System.currentTimeMillis();
		}
		
		Color oc = g.getColor();
		
		//hideclass oe in case it changes
		double hscroll = this.hscroll;
		double vscroll = this.vscroll;
		g.translate(-hscroll, -vscroll);
		super.draw(g);
//		System.out.println(height+":"+hbar.x+","+hbar.y+","+hbar.width+","+hbar.height);
		inner.draw(g);
		g.setColor(Color.black);

		g.fillRect(0,(int) hbar.y, (int)inner.width, (int) hbar.height);
		g.translate(hscroll, vscroll);
		g.setColor(Color.red);
		g.fill(hbar);
		//if(inner.height > innerSize().getHeight())g.fill(vbar);
		g.setColor(oc);
	}
	public void setRect(int x, int y, int w, int h){
		super.setRect(x,y,w,h);
		hbar.x = x+1;
		hbar.y = y+height-hbar.height;
		vbar.x =x+ width-vbar.width;
		vbar.y = y+1;
	}
	
	
	
	
}
