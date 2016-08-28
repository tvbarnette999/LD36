package TnT.ld.ld36;

import java.util.ArrayList;

public class Tech extends OverlayButton{
	public static final int WIDTH = 350;
	public static final int HEIGHT = 120;
	
	ArrayList<Tech> parents = new ArrayList<Tech>();
	String name;
	String description;
	int cost;
	boolean researched = false;
	int x, y;//manual location/rendering?
	Transport[] targets;
	public double value;
	int depth = 0; //calculated
	int row = 0;
	public Tech(String name, String description, Transport ...targets ){
		super(name);
		this.name = name;
		this.description = description;
		this.targets = targets;
	}
	public boolean available(){
		for(int i=0; i<parents.size(); i++){
			if(!parents.get(i).researched){
				return false;
			}
		}
		return true;
	}
	public boolean canAfford(){
		return false;
	}
	public void research(){
		researched = true;
		for(Transport t : targets){
			t.scalar *= value;
		}
		
	}
	public void addParent(Tech t){
		parents.add(t);
		depth = Math.max(depth,  t.depth + 1);
	}

}
