package TnT.ld.ld36;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.Iterator;

public class Sprite {
	Path p;
	public BufferedImage img;
	public int x, y;
	public int ticks = 0;
	public int maxTicks;// = 200;
	public int miniTicks = 50;
	Point2D.Double src, dest;
	Iterator<Point> it;
	int on = 0;
	Map m;

	public Sprite(Map m, Point src, Path p, BufferedImage img) {
		this.p = p;
		this.m = m;
		it = p.iterator();
		this.img = img;
		this.src = m.getTileCenter(src);
		dest = m.getTileCenter(it.next());
//		miniTicks = maxTicks / (p.length());
		maxTicks = miniTicks * p.length();
		x = (int) p.getFirst().x;
		y = (int) p.getFirst().y;
	}

	public void draw(Graphics2D g) {
		final int x = this.x, y = this.y;
		double angle = Math.atan2(dest.y - src.y, dest.x - src.x);
		int sign = 1;
		if (Math.abs(angle) > Math.PI/2) {
			sign = -1;
		}
		g.rotate(angle, x, y);
		g.scale(1, sign);
		AffineTransform tx = AffineTransform.getRotateInstance(angle, img.getWidth() / 2, img.getHeight() / 2);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

		// Drawing the rotated image at the required drawing locations
		// System.out.println(angle);
//		g.drawImage(op.filter(img, null), x, y, null);
		g.drawImage(img, x-img.getWidth()/2, sign*y-img.getHeight()/2-sign*20, null);
		g.scale(1, sign);
		g.rotate(-angle, x, y);
		// g.fillRect(x, y, 100, 100);
	}

	boolean reversed = false;
	public void animate() {
		ticks++;
		if (ticks % miniTicks == 0 && ticks > maxTicks - miniTicks && !reversed) {
			reversed = true; //only do this once
			src = dest;
			it = p.getBackIt();
//			src = m.getTileCenter(it.next());
			dest = m.getTileCenter(it.next());
		} else {
			if (ticks % miniTicks == 0 && it.hasNext()) {
				src = dest;
				dest = m.getTileCenter(it.next());
			}
		}
		x = (int) (src.x + (dest.x - src.x) * ((double) (ticks % miniTicks) / miniTicks));
		y = (int) (src.y + (dest.y - src.y) * ((double) (ticks % miniTicks) / miniTicks));
	}

	public boolean isDone() {
		return ticks > maxTicks * 2;
	}
}
