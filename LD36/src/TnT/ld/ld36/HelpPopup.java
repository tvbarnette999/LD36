package TnT.ld.ld36;

import java.awt.Color;
import java.awt.Graphics2D;

import java.awt.geom.Point2D;

public class HelpPopup {
	public int x, y; //the tile to display on
	public double px, py;
	public Point2D.Double p;
	public double ticks = 15; //seconds
	public String text;
	public String[] display;
	Map m;
	public HelpPopup(Map m, int x, int y, String text) {
		this.text = text;
		this.m = m;
		this.x = x;
		this.y = y;
		p = m.getTileLocation(x, y);
		px = p.x;
		py = p.y;
		display = text.split("\n");
	}
	
	public HelpPopup(Map m, int x, int y, String text, int ticks) {
		this(m, x, y, text);
		this.ticks = ticks;
	}
	
	public void draw(Graphics2D g) {
		int width = 0;
		for (String s : display) {
			width = Math.max(width, g.getFontMetrics().stringWidth(s));
		}
		width += 5;
		Color c = g.getColor();
		g.setColor(Color.lightGray);
		g.fillRect((int) px, (int) py, width, display.length * g.getFontMetrics().getHeight() + 5);
		g.setColor(Color.black);
		int fy = (int) (py + 5 + g.getFontMetrics().getHeight());
		for (String s : display) {
			g.drawString(s, (int) (px + 5), fy);
			fy += 3 * g.getFontMetrics().getHeight() / 4;
		}
		g.setColor(c);
	}
}
