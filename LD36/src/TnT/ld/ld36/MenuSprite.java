package TnT.ld.ld36;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class MenuSprite {
	public BufferedImage img;
	public int x = 0, y = 0;
	Point2D.Double src, dest;
	int ticks = 0;
	int maxTicks = 2000;
	public MenuSprite(Point2D.Double src, Point2D.Double dest, BufferedImage img) {
		this.src = src;
		x = (int) src.x;
		y = (int) src.y;
		this.dest = dest;
		this.img = img;
	}
	public void draw(Graphics2D g) {
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
		ticks++;
		x = (int) (src.x + (dest.x - src.x) * ((double) ticks / maxTicks));
		y = (int) (src.y + (dest.y - src.y) * ((double) ticks / maxTicks));
		// g.fillRect(x, y, 100, 100);
	}
	public boolean isDone() {
		return ticks > maxTicks || dest.distance(x, y) < 2;
	}
}
