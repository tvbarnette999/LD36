package TnT.ld.ld36;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class TechTree extends Overlay{

	public static final int X_GAP = Tech.HEIGHT; //squares!
	public static final int Y_GAP = 30;
	public static int MAX_COLUMN = 0;
	public static int MAX_SCROLL = 0;
	
	double scroll = 00;
	static ArrayList<Tech> heads = new ArrayList<Tech>();
	
	//instantiate all the techs here, pass them the list of ones they affect
	static Tech sandals = new Tech("Sandals", "Increase Runner speed by 10%.", Transport.RUNNER);
	static Tech shoeLaces = new Tech("Shoe Laces", "", Transport.currentUnits, 3, Transport.TRAIN);
	static Tech shoes = new Tech("Shoes", "Increases Runner Speed by 10%.", Transport.RUNNER);
	static Tech agriculture = new Tech("Agriculture", "");
	static Tech paper = new Tech("Paper", "");
	static Tech ink = new Tech("Ink", "");
	static Tech husbandry = new Tech("Husbandry", "");
	static Tech mining = new Tech("Mining", "");
	static Tech metal = new Tech("Metal", "");
	static Tech leather = new Tech("Leather", "");
	static Tech snacks = new Tech("Snacks", "");
	static Tech exercise = new Tech("Exercise", "");
	static Tech protein = new Tech("Protein", "");
	static Tech gym = new Tech("Gym", "");
	static Tech horses = new Tech("Horses", "");
	static Tech saddles = new Tech("Saddles", "");
	static Tech saddleBags = new Tech("Saddle Bags", "");
	static Tech horseFood = new Tech("Horse Food", "");
	static Tech horseShoes = new Tech("Horseshoes", "Increases horse speed by 10%", Transport.HORSE, Transport.CARRIAGE, Transport.CARAVAN);
	static Tech wheel = new Tech("The Wheel", "");
	static Tech weights = new Tech("Weight Training", "");
	static Tech breeding = new Tech("Enhanced Breeding", "");
	static Tech dirtRoad = new Tech("Dirt Roads", "", Road.DIRT);
	
	
	static Tech olympics = new Tech("Olympics", "");
	static Tech relays = new Tech("Relays", "");
	static Tech carrot = new Tech("Carrot on a Stick", "");
	static Tech spurs = new Tech("Spurs", "Better control of horses");
	static Tech ironHorseShoes = new Tech("Iron Horse Shoes", "");
	static Tech smithing = new Tech("Smithing", "");
	static Tech smootherRoads = new Tech("Smoother Roads", "");
	static Tech ditches = new Tech("Ditches", "");
	static Tech iron = new Tech("Iron", "");
//	static Tech compass = new Tech("Compass", "");
	static Tech cartography = new Tech("Cartography", "");
	static Tech medicine = new Tech("Medicine", "");
	//static Tech literacy = new Tech("Literacy", "");
	static Tech steel = new Tech("Steel", "");
	//static Tech steelFarm = new Tech("Steel Farm Equipment", "");
	static Tech math = new Tech("Math", "");
	static Tech carriage = new Tech("Horse Drawn Carriage", "");
	static Tech metalRims = new Tech("Metal Rims", "Adds coolness, so horse goes quicker");
	static Tech lighterCarriage = new Tech("Lighter Carriage", "");
	static Tech twoHorse = new Tech("Two Horse Carriage", "");
	static Tech caravan = new Tech("Caravan", "");
	//static Tech lanterns = new Tech("Lanterns", "");
	static Tech steam = new Tech("Steam", "");
	static Tech engineering = new Tech("Engineering", "");
	static Tech catapult = new Tech("Catapult", "");
	static Tech biggerCatapult = new Tech("BIGGER Catapult", "Bigger is better");
	static Tech tension = new Tech("High Tension Rope", "");
	static Tech onager = new Tech("Onager", "");
	static Tech shaftMining = new Tech("Shaft Mining", "");
	static Tech metallurgy = new Tech("Metallurgy", "");
	static Tech engine = new Tech("Engine", "");
	static Tech railroad = new Tech("Rail Roads", "", Road.RAIL);
	static Tech modernScience = new Tech("Modern Science", "");
	static Tech steroids= new Tech("Steroids", "");
	static Tech horseSteroids = new Tech("Horse Steroids", "");
	static Tech printPress = new Tech("Printing Press", "");
	static Tech method = new Tech("Scientific Method", "");
	static Tech fossil = new Tech("Fossil Fuels", "");
	static Tech combustion = new Tech("Combustion Engine", "");
	static Tech coal = new Tech("Coal", "");
	static Tech locomotive = new Tech("Locomotive", "");
	static Tech longTrains = new Tech("Long Trains" , "");
	static Tech cowCatchers = new Tech("Cow Catchers", "");
	static Tech electricity = new Tech("Electricity", "");
	static Tech oil=  new Tech("Oil", "");
	static Tech asphault = new Tech("Asphault", "");
	static Tech paved = new Tech("Paved Road", "");
	static Tech cars = new Tech("Cars", "");
	static Tech gasoline = new Tech("Gasoline", "");
	static Tech diesel = new Tech("Diesel", "");
	static Tech jetFuel = new Tech("Jet Fuel", "");
	static Tech beams = new Tech("Steel Beams", "");
	static Tech aerodynamics = new Tech("Aerodynamics", "");
	static Tech aviation = new Tech("Aviation", "");
	static Tech airplanes = new Tech("Airplanes", "");
	static Tech airports = new Tech("Airports", "");
	
	static Tech dieselTrain = new Tech("Diesel Locomotive", "");
	static Tech trucks = new Tech("Trucks", "");
	static Tech powerLines = new Tech("Power Lines", "");
	static Tech electricTrain = new Tech("Electric Trains", "");
	static Tech bulletTrain = new Tech("Bullet Train", "");
	static Tech battery = new Tech("Batteries", "");
	static Tech semi = new Tech("Semi-Trailer Truck", "");
	static Tech electricCar = new Tech("Electric Car", "");
	
	static Tech EPA = new Tech("EPA", "");
	static Tech efficiency= new Tech("Fuel Efficiency", "");
	
	static Tech AI = new Tech("AI", "");
	
	static Tech selfCar = new Tech("Self Driving Car", "");
	static Tech selfTrain = new Tech("Self Driving Train", "");
	static Tech selfPlane = new Tech("AutoPilot", "");
	
	static Tech singularity = new Tech("Singularity", "Machines take over. ");
	//TODO research inflation. you make more monezzz
	//reseach capitalism
	//static Tech onagers = new Tech("Onagers", "Increases catapault range by 20%",  Transport.CATAPAULT_RANGE);
	
	static{
		
//add everything's row first
		saddles.row = 0;
		shoeLaces.row = 0;
		snacks.row = 0;
		exercise.row = 0;
		protein.row = 0;
		gym.row = 0;
		weights.row = 0;
		olympics.row = 0;
		relays.row = 0;
		
		agriculture.row = 1;
		paper.row = 1;
		ink.row = 1;
		leather.row = 1;
		husbandry.row = 1;
		mining.row = 1;
		metal.row = 1;
		wheel.row = 1;
		
		horses.row = 2;
		saddles.row = 2;
		saddleBags.row = 2;
		horseFood.row = 2;
		horseShoes.row = 2;
		
		breeding.row = 2;
		carrot.row = 3;
		spurs.row = 2;
		ironHorseShoes.row = 2;
		
		dirtRoad.row = 1;
		smithing.row = 2;
		carriage.row = 1;
		
		spurs.row = 3;
		ironHorseShoes.row = 3;
		
		smootherRoads.row = 2;
		ditches.row = 2;
		iron.row = 2;
		
	//	compass.row = 2;
		cartography.row = 2;
		
		metalRims.row = 1;
		lighterCarriage.row =1;
		twoHorse.row = 1;
		caravan.row = 1;
		medicine.row = 2;
		//literacy.row = 2;
		steel.row = 0;
		math.row = 2;
		engineering.row = 2;
		catapult.row = 4;
		tension.row = 4;
		biggerCatapult.row = 4;
		onager.row = 4;
		
		steam.row = 1;
		shaftMining.row = 1;
		metallurgy.row = 1;
		engine.row = 1;
		railroad.row = 0;
	//	steelFarm.row = 3;
		
		modernScience.row = 3;
		medicine.row = 3;
		steroids.row = 3;
		horseSteroids.row = 3;
		
		printPress.row = 2;
		method.row = 2;
		fossil.row = 2;
		electricity.row = 2;
		oil.row = 2;
		gasoline.row = 2;
		
		
		combustion.row = 1;
		asphault.row = 1;
		paved.row = 1;
		cars.row = 2;
		diesel.row = 1;
		powerLines.row = 1;
		battery.row = 1;
		AI.row = 1;
		
		coal.row = 0;
		locomotive.row = 0;
		longTrains.row = 0;
		cowCatchers.row= 0;
		dieselTrain.row = 0;
		bulletTrain.row = 0;
		electricTrain.row = 0;
		trucks.row = 2;
		semi.row = 2;
		electricCar.row =2;
		
		EPA.row = 3;
		efficiency.row = 3;
		jetFuel.row = 4;
		beams.row = 4;
		aerodynamics.row = 4;
		aviation.row = 4;
		airplanes.row = 4;
		airports.row = 4;
		
		
		selfPlane.row = 4;
		selfCar.row = 2;
		selfTrain.row = 0;
		
		singularity.row = 2;
		
		
		//add child only after all parents have been added to their parents!
		heads.add(sandals);
		heads.add(agriculture);
		
		paper.addParent(agriculture);
		ink.addParent(paper);
		husbandry.addParent(ink);
		leather.addParent(husbandry);
		mining.addParent(leather);
		metal.addParent(mining);
		wheel.addParent(metal);
		dirtRoad.addParent(wheel);
		
		
		
		shoes.addParent(sandals);
		shoeLaces.addParent(shoes);
		snacks.addParent(shoeLaces);
		exercise.addParent(snacks);
		protein.addParent(exercise);
		gym.addParent(protein);
		weights.addParent(gym);
		weights.addParent(metal);
		olympics.addParent(weights);
		relays.addParent(olympics);
		
		
		
		
		
		horses.addParent(husbandry);
		saddles.addParent(horses);
		saddles.addParent(leather);
		saddleBags.addParent(saddles);
		horseFood.addParent(saddleBags);
		horseShoes.addParent(horseFood);
		horseShoes.addParent(metal);
		breeding.addParent(horseShoes);
		carrot.addParent(breeding);
		
		carriage.addParent(breeding);
		carriage.addParent(dirtRoad);
		smithing.addParent(breeding);
		smithing.addParent(dirtRoad);
		
		
		
		metalRims.addParent(smithing);
		metalRims.addParent(carriage);
		lighterCarriage.addParent(metalRims);
		twoHorse.addParent(lighterCarriage);
		caravan.addParent(twoHorse);
		
		smootherRoads.addParent(smithing);
		ditches.addParent(smootherRoads);
		iron.addParent(ditches);
		//compass.addParent(iron);
		
		spurs.addParent(carrot);
		ironHorseShoes.addParent(iron);
		ironHorseShoes.addParent(spurs);
		
		caravan.addParent(iron);
		
		steam.addParent(caravan);
		shaftMining.addParent(steam);
		metallurgy.addParent(shaftMining);
		
		
//		literacy.addParent(medicine);
//		steel.addParent(literacy);
//		math.addParent(steel);
//		steelFarm.addParent(steel);
		//math.addParent(cartography);
		math.addParent(iron);

		cartography.addParent(math);//ompass);
		engineering.addParent(cartography);
		engineering.addParent(modernScience);
		catapult.addParent(math);
		biggerCatapult.addParent(catapult);
		tension.addParent(biggerCatapult);
		onager.addParent(tension);
		

		steel.addParent(metallurgy);
		engine.addParent(metallurgy);
		engine.addParent(engineering);
		railroad.addParent(engine);
		railroad.addParent(steel);
		coal.addParent(railroad);
		locomotive.addParent(coal);
		longTrains.addParent(locomotive);
		cowCatchers.addParent(longTrains);
		
		
		modernScience.addParent(math);
		medicine.addParent(modernScience);
		steroids.addParent(medicine);
		horseSteroids.addParent(steroids);
		
		
		//Train steroids?
		
		printPress.addParent(engineering);
		method.addParent(printPress);
		fossil.addParent(method);
		
		combustion.addParent(engine);
		combustion.addParent(fossil);
		electricity.addParent(fossil);
		oil.addParent(electricity);
		gasoline.addParent(oil);
		asphault.addParent(combustion);
		paved.addParent(asphault);
		cars.addParent(gasoline);
		cars.addParent(paved);
		
		diesel.addParent(gasoline);
		jetFuel.addParent(oil);
		beams.addParent(jetFuel);
		aerodynamics.addParent(beams);
		aviation.addParent(aerodynamics);
		airplanes.addParent(aviation);
		airports.addParent(airplanes);
		
		powerLines.addParent(diesel);
		
		dieselTrain.addParent(diesel);
		dieselTrain.addParent(cowCatchers);
		electricTrain.addParent(dieselTrain);
		electricTrain.addParent(powerLines);
		bulletTrain.addParent(electricTrain);
		battery.addParent(powerLines);
		trucks.addParent(cars);
		semi.addParent(trucks);
		electricCar.addParent(semi);
		electricCar.addParent(battery);
//		System.out.println(weights.depth);
		
		EPA.addParent(trucks);
		efficiency.addParent(EPA);
		
		AI.addParent(battery);
		
		selfCar.addParent(AI);
		selfTrain.addParent(AI);
		selfPlane.addParent(AI);
		selfCar.addParent(electricCar);
		selfTrain.addParent(electricTrain);
		selfPlane.addParent(airports);
		
		singularity.addParent(selfCar);
		singularity.addParent(selfTrain);
		singularity.addParent(selfPlane);
	}
	
	static{
		sandals.cost = 10;
		shoeLaces.cost = 100;
		shoes.cost = 500;
		
		agriculture.cost = 50;
		paper.cost = 100;
		ink.cost = 300;
		husbandry.cost = 1000;

		leather.cost = 2000;
		horses.cost = 5000;
		mining.cost = 8000;
		metal.cost = 10000;
		
		saddles.cost = 15000;
		saddleBags.cost = 30000000d;
	}
	
	public TechTree(){		
		//add all Techs as children
		for(Field f:this.getClass().getDeclaredFields()){
			if(f.getType().equals(Tech.class)){
				try {
					Tech t = (Tech) f.get(null);
					t.x = X_GAP + (t.depth * (X_GAP + Tech.WIDTH));
					t.y = Y_GAP + (t.row * (Y_GAP + Tech.HEIGHT));
					MAX_COLUMN = Math.max(MAX_COLUMN, t.depth);
					this.addChild(t);
					System.out.println(t.name+":"+t.x+", "+t.y);
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		}
		
		
		this.width = (MAX_COLUMN+1)*(X_GAP+Tech.WIDTH)+X_GAP;
//		height = LD36.theLD.buffer.getHeight();
//		System.out.println(elements.size());
	}
	
	long lastTime = -1;
	long current = -1;
	public void draw(Graphics2D g){

		MAX_SCROLL = (int) (this.width - LD36.theLD.buffer.getWidth());
		long current = System.nanoTime();
		if (lastTime > 0) {
			double dt = 500 * (current-lastTime) * 1e-9 ;
			if (LD36.rightPressed) scroll += dt;
			if (LD36.left) scroll -= dt;
			if(scroll<0)scroll = 0;
			if(scroll > MAX_SCROLL)scroll = MAX_SCROLL;
		}
		lastTime = current;
		
		g.translate(-scroll,0);
		super.draw(g);
		Color oc = g.getColor();
		g.setColor(Color.white);
//		for(int y = 0; y < LD36.theLD.buffer.getHeight(); y+=Tech.HEIGHT + Y_GAP){
//			g.drawLine(0, y, MAX_COLUMN*(Tech.WIDTH+X_GAP), y);
//			g.drawLine(0, y+Y_GAP, MAX_COLUMN*(Tech.WIDTH+X_GAP), y+Y_GAP);
//			
//		}
//		
//		for(int x = 0; x < MAX_COLUMN * (Tech.WIDTH+X_GAP); x+=Tech.WIDTH+X_GAP){
//			g.drawLine(x, 0, x, LD36.theLD.buffer.getHeight());
//			g.drawLine(x+X_GAP, 0,x+X_GAP , LD36.theLD.buffer.getHeight());
//			
//		}
		g.setColor(oc);
		g.translate(scroll, 0);
		
	}
	
	public void mouseMoved(MouseEvent e){
		super.mouseMoved(new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiers(), e.getX()+(int)scroll, e.getY(), e.getXOnScreen(), e.getYOnScreen(), e.getClickCount(), e.isPopupTrigger(), e.getButton()));
	}
	public void mousePressed(MouseEvent e){
		super.mousePressed(new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiers(), e.getX()+(int)scroll, e.getY(), e.getXOnScreen(), e.getYOnScreen(), e.getClickCount(), e.isPopupTrigger(), e.getButton()));
	}
	public void mouseReleased(MouseEvent e){
		super.mouseReleased(new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiers(), e.getX()+(int)scroll, e.getY(), e.getXOnScreen(), e.getYOnScreen(), e.getClickCount(), e.isPopupTrigger(), e.getButton()));
	}
	public void mouseClicked(MouseEvent e){
		super.mouseClicked(new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiers(), e.getX()+(int)scroll, e.getY(), e.getXOnScreen(), e.getYOnScreen(), e.getClickCount(), e.isPopupTrigger(), e.getButton()));
	}
	
}
