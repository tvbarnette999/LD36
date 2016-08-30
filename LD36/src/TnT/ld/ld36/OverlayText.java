package TnT.ld.ld36;

public class OverlayText extends Overlay {
	String text = "";
	
	public OverlayText(int x, int y, String text) {
		this.text = text;
	}
	
	public OverlayText(int x, int y) {
		this(x, y, "");
	}
	
}
