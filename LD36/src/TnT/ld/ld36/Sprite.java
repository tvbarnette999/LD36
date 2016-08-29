package TnT.ld.ld36;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Sprite{
	public Point2D.Double dest;
	public Point2D.Double src;
	public BufferedImage img;
	public int x, y;
	public int ticks = 0;
	public int maxTicks = 100;
	public Sprite (Point2D.Double src, Point2D.Double dest, BufferedImage img) {
		this.src = src;
		this.dest = dest;
		this.img = img;
	}
	public Sprite (Point2D.Double src, Point2D.Double dest, BufferedImage img, int maxTicks) {
		this(src, dest, img);
		this.maxTicks = maxTicks;
	}
	public void draw(Graphics2D g) {
		double angle = Math.atan2(dest.y - src.y, dest.y - src.y);
		
		AffineTransform tx = AffineTransform.getRotateInstance(angle, x, y);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

		// Drawing the rotated image at the required drawing locations
		g.drawImage(op.filter(img, null), x, y, null);
	}
	public void animate() {
		ticks++;
		x = (int) (src.x + (dest.x - src.x) * (ticks / maxTicks));
		y = (int) (src.y + (dest.y - src.y) * (ticks / maxTicks));
	}
	public boolean isDone() {
		return ticks > maxTicks;
	}
	public void tick() {
		ticks++;		
	}
}
