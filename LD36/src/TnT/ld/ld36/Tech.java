package TnT.ld.ld36;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Tech extends OverlayButton implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6358554550558708154L;
	public static final int WIDTH = 260;
	public static final int HEIGHT = 100;

	ArrayList<Tech> parents = new ArrayList<Tech>();
	String name;
	String description;
	String[] split;
	double cost = 1;
	boolean researched = false;
	// int x, y;//manual location/rendering?
	Object[] targets;
	public double value;
	int depth = 0; // calculated
	int row = 0;

	public Tech(String name, String description, double value, Object... targets) {
		super(name);
		this.background = Color.blue;
		this.removedBackground = Color.blue;
		this.enteredBackground = Color.CYAN;
		this.name = name;
		this.description = description;
		split = this.description.split(" ");
		this.targets = targets;
		width = WIDTH;
		height = HEIGHT;
		this.enabled = false;
		this.value = value;
		setActionListener(this);
	}

	public boolean available() {
		for (int i = 0; i < parents.size(); i++) {
			if (!parents.get(i).researched) {
				return false;
			}
		}
		return true;
	}

	public boolean canAfford() {
		return LD36.theLD.money > cost;
	}

	public void research() {
		researched = true;
		Object o = null;
		for (int i = 0; i < targets.length; i++) {
			o = targets[i];
			if (o instanceof Transport) {
				((Transport) o).scalar *= value;
			}
			if (o instanceof Road) {
				((Road) o).unlocked = true;
			}
			if (o instanceof Transport[]) { // DEFINITELY UNTESTED!
				((Transport[]) o)[(int) value] = (Transport) targets[i + 1];
				i++;
				continue;
			}
			if (o == City.class) {
				City.literacy *= value;
			}
			if (o == LD36.theLD) {
				LD36.theLD.money = value;
			}
		}
		LD36.theLD.money -= cost;

	}

	public void addParent(Tech t) {
		parents.add(t);
		depth = Math.max(depth, t.depth + 1);
	}

	public int round(double d) {
		return (int) Math.round(d);
	}

	public void drawLine(Graphics2D g, double x1, double y1, double x2, double y2) {
		g.drawLine(round(x1), round(y1), round(x2), round(y2));
	}

	public void draw(Graphics2D g) {
		// System.out.println(name);

		Color oc = g.getColor();
		if (researched) {
			g.setColor(Color.BLUE);
		} else if (available()) {
			enabled = true;
			if (canAfford()) {
				g.setColor(Color.GREEN.darker().darker());
			} else {
				g.setColor(Color.RED);
			}
		} else {
			g.setColor(Color.DARK_GRAY);
		}
		g.fill(this);

		g.setColor(Color.white);

		Font of = g.getFont();
		g.setFont(of.deriveFont(20f));

		g.drawString(name, (int) (x + 5), (int) (y + 20));
		if (!researched)
			g.drawString(LD36.moneyString(cost), (int) x + 5, (int) ((int) y + height - 5));

		g.setFont(of.deriveFont(14f));
		// System.out.println(name+":"+x+","+y);
		int sx = (int) (x + 5);
		int sy = (int) (y + 40);
		FontMetrics fm = g.getFontMetrics();
		for (String s : split) {
			if (fm.stringWidth(s) >= (x + width) - sx) {
				// next line
				sy += 3.0 * fm.getHeight() / 4.0;
				sx = (int) (this.x + 5);
			}
			g.drawString(s, sx, sy);
			sx += fm.stringWidth(s + " ");
		}
		// g.drawString(description, (int)(x+5), (int)(y+30));
		// draw path to parents!
		Stroke os = g.getStroke();
		g.setStroke(new BasicStroke(4));
		for (Tech p : parents) {

			double x = this.x;
			double y = this.y;
			double row = this.row;
			double depth = this.depth - 1;
			if (row < p.row) {
				g.drawArc((int) (x - TechTree.X_GAP / 2), (int) (y + height / 2), TechTree.X_GAP, TechTree.X_GAP, 90,
						90); // R to D
				drawLine(g, x - TechTree.X_GAP / 2, y + height, x - TechTree.X_GAP / 2, y + height + TechTree.Y_GAP);
				row++;
				// go down height+gap
				for (; row < p.row; row++, y += height + TechTree.Y_GAP) {
					drawLine(g, x - TechTree.X_GAP / 2, y + height + TechTree.Y_GAP, x - TechTree.X_GAP / 2,
							y + 2 * (height + TechTree.Y_GAP));
				}
				g.drawArc((int) (x - 3 * TechTree.X_GAP / 2), (int) (y + TechTree.Y_GAP + height / 2), TechTree.X_GAP,
						TechTree.X_GAP, 270, 90); // T to L
				y += (height + TechTree.Y_GAP);
				x -= TechTree.X_GAP;
			} else if (row > p.row) {
				g.drawArc((int) (x - TechTree.X_GAP / 2), (int) (y - height / 2), TechTree.X_GAP, TechTree.X_GAP, 180,
						90); // r to u
				drawLine(g, x - TechTree.X_GAP / 2, y, x - TechTree.X_GAP / 2, y - TechTree.Y_GAP);// up
																									// gap
				row--;
				for (; row > p.row; row--, y -= height + TechTree.Y_GAP) {
					drawLine(g, x - TechTree.X_GAP / 2, y - TechTree.Y_GAP, x - TechTree.X_GAP / 2,
							y - height - 2 * TechTree.Y_GAP);
				}
				g.drawArc((int) (x - 3 * TechTree.X_GAP / 2), (int) (y - height / 2 - TechTree.Y_GAP), TechTree.X_GAP,
						TechTree.X_GAP, 0, 90);// b to l
				y -= (height + TechTree.Y_GAP);
				x -= TechTree.X_GAP;

			} else {

				drawLine(g, x, y + height / 2, x - TechTree.X_GAP, y + height / 2);
				x -= TechTree.X_GAP;
			}
			for (; depth > p.depth; depth--, x -= (TechTree.X_GAP + WIDTH)) {
				drawLine(g, x, y + height / 2, x - TechTree.X_GAP - WIDTH, y + height / 2);
			}

			if (1 == 1)
				continue; // dead code to copy paste for connections
			drawLine(g, x, y + height / 2, x - TechTree.X_GAP, y + height / 2);
			g.drawArc((int) (x - TechTree.X_GAP / 2), (int) (y + height / 2), TechTree.X_GAP, TechTree.X_GAP, 90, 90);// r
																														// to
																														// d
			drawLine(g, x - TechTree.X_GAP / 2, y + height, x - TechTree.X_GAP / 2, y + height + TechTree.Y_GAP);// down
																													// gap
			g.drawArc((int) (x - TechTree.X_GAP / 2), (int) (y - height / 2), TechTree.X_GAP, TechTree.X_GAP, 180, 90); // r
																														// to
																														// u
			drawLine(g, x - TechTree.X_GAP / 2, y, x - TechTree.X_GAP / 2, y - TechTree.Y_GAP);// up
																								// gap
			g.drawArc((int) (x - 3 * TechTree.X_GAP / 2), (int) (y - height / 2 - TechTree.Y_GAP), TechTree.X_GAP,
					TechTree.X_GAP, 0, 90);// b to l
			g.drawArc((int) (x - 3 * TechTree.X_GAP / 2), (int) (y + TechTree.Y_GAP + height / 2), TechTree.X_GAP,
					TechTree.X_GAP, 270, 90);// t to l
		}
		g.setFont(of);
		g.setStroke(os);
		g.setColor(oc);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (available() && canAfford())
			research();
	}

}
