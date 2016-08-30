package TnT.ld.ld36;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class OverlayText extends Overlay {
	String text = "";
	float fontSize = 15;
	Color c = Color.black;
	
	public OverlayText(double x, double y, String text) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.width = 0;
		this.height = 0;
	}
	
	public OverlayText(double x, double y) {
		this(x, y, "");
	}
	
	public void setFontSize(float size) {
		fontSize = size;
	}
	
	public void setColor(Color c) {
		this.c = c;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void draw(Graphics2D g) {
		Font fo = g.getFont();
		Color co = g.getColor();
		g.setFont(fo.deriveFont(fontSize));
		g.setColor(c);
//		System.out.println(text);
		g.drawString(text, (int) (x-g.getFontMetrics(g.getFont()).stringWidth(text)/2), (int)(y+fontSize/2));
		
		g.setFont(fo);
		g.setColor(co);
	}
	
}
