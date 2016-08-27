package TnT.ld.ld36;

import java.util.ArrayList;

public class Tech extends OverlayButton{
	public static final int WIDTH = 100;
	public static final int HEIGHT = 50;
	
	ArrayList<Tech> parents = new ArrayList<Tech>();
	String name;
	String description;
	int cost;
	boolean researched = false;
	int x, y;//manual location/rendering?
	Transport[] targets;
	public static final int ADD = 1;
	public static final int MUL = 2;
	public int op;
	public double value;
	int depth = 0; //calculated
	int row = 0;
	public Tech(String name, String description, int cost, int op, double value, Transport ...targets ){
		this.name = name;
		this.description = description;
		this.cost = cost;
		this.op = op;
		this.value = value;
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
		if(op == ADD){
			for(Transport t: targets){
				t.scalar += value;
			}
		}
		else if(op == MUL){
			for(Transport t: targets){
				t.scalar*=value;
			}
		}
		
	}
	public void addParent(Tech t){
		parents.add(t);
		depth = Math.max(depth,  t.depth);
	}

}
