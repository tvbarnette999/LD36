package TnT.ld.ld36;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

public class OverlayButton extends Overlay {
	public String text = "Click ME";
	public OverlayButton(){
		this.background = Color.lightGray;
	}
	public void draw(Graphics2D g){
		super.draw(g);
		Color oc = g.getColor();
		Font of = g.getFont();
		g.setColor(Color.blue);
		g.draw(this);
		g.setColor(Color.GREEN);
		g.setFont(of.deriveFont(24f));
		g.drawString(text,(int)( x+width/2 - g.getFontMetrics().stringWidth(text)/2.0),(int)(y+ height/2.0));
		g.setColor(oc);
		g.setFont(of);
	}
	
	public void mouseEntered(MouseEvent e){
		this.background = Color.LIGHT_GRAY.brighter();
		e.consume();
	}
	public void mouseExited(MouseEvent e){
		this.background = Color.LIGHT_GRAY;
		e.consume();
	}
	public void mouseClicked(MouseEvent e){
		JOptionPane.showMessageDialog(null, "You Clicked It!");
	}
}
