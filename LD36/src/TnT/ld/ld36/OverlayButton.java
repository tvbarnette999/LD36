package TnT.ld.ld36;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

public class OverlayButton extends Overlay {
	public String text = "Click ME";
	public boolean selectable = false;
	public boolean selected = false;
	public ActionListener callback = null;
	public Color enteredBackground = background.brighter();
	public Color removedBackground;
	public OverlayButton(String text, boolean selectable){
		this.background = Color.lightGray;
		this.text = text;
		this.removedBackground = background;
	}
	public OverlayButton(Image icon){
		this.img = icon;
	}
	public Image img = null;
	public OverlayButton(String text){
		this(text,false);
	}
	public OverlayButton(){
		this("", false);
	}
	public void draw(Graphics2D g){
		super.draw(g);
		if(img != null){
			g.drawImage(img,(int)x,(int)y,(int)width,(int)height,null);
			return;
		}
		Color oc = g.getColor();
		Font of = g.getFont();
		g.setColor(selected?Color.RED :Color.blue);
		g.draw(this);
		g.setColor(Color.GREEN);
		g.setFont(of.deriveFont(24f));
		g.drawString(text,(int)( x+width/2 - g.getFontMetrics().stringWidth(text)/2.0),(int)(y+ height/2.0));
		g.setColor(oc);
		g.setFont(of);
//		System.out.println("OB"+x+","+y);
	}
	
	public void mouseEntered(MouseEvent e){
		System.out.println("ENTER "+text);
		this.background = enteredBackground;
		e.consume();
	}
	public void mouseExited(MouseEvent e){
		this.background = removedBackground;
		e.consume();
	}
	public void mouseClicked(MouseEvent e){
		if(callback!=null){
			callback.actionPerformed(new ActionEvent(this, 0, "", 0, e.getModifiers()));
		}
		e.consume();
	}
	public void setActionListener(ActionListener al){
		callback = al;
	}
}
