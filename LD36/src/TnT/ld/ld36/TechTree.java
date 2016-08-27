package TnT.ld.ld36;

import java.util.ArrayList;

public class TechTree extends Overlay{

	static ArrayList<Tech> heads = new ArrayList<Tech>();
	
	//instantiate all the techs here, pass them the list of ones they affect
	static Tech shoes = new Tech("shoes", "Increases Runner Speed by 10%.", 100, Tech.ADD, .1, Transport.RUNNER);
	static Tech horseShoes = new Tech("Horseshoes", "Increases horse speed by 10%", 1000, Tech.ADD, .1, Transport.HORSE, Transport.CARRIAGE, Transport.CARAVAN);
	static Tech onagers = new Tech("Onagers", "Increases catapault range by 20%", 100000, Tech.MUL, 1.2, Transport.CATAPAULT_RANGE);
	static{
		heads.add(shoes);
		horseShoes.addParent(shoes);
	}
	
	public TechTree(){
		
	}
	
}
