package TnT.ld.ld36;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class TechTree extends Overlay{

	public static final int X_GAP = Tech.HEIGHT; //squares!
	public static final int Y_GAP = 20;
	public static int MAX_COLUMN = 0;
	public static int MAX_SCROLL = 0;
	
	double scroll = 00;
	static ArrayList<Tech> heads = new ArrayList<Tech>();
	
	//instantiate all the techs here, pass them the list of ones they affect
	static Tech sandals = new Tech("Sandals", "Increase Runner speed by 10%.", 1.1, Transport.RUNNER);
	static Tech shoeLaces = new Tech("Shoe Laces", "Keeps runners shoes from coming off, doubling speed", 2, Transport.RUNNER);
	static Tech shoes = new Tech("Shoes", "Increases Runner Speed by 10%.", 1.1, Transport.RUNNER);
	static Tech agriculture = new Tech("Agriculture", "Better Food Increases Runner payload by 20%", 1.2, Transport.RUNNER);
	static Tech paper = new Tech("Paper", "Messages on paper weight less than stone, tripling Runner payload", 3.0, Transport.RUNNER);
	static Tech ink = new Tech("Ink", "Ink allows more people to write letters", 2, City.class);
	static Tech husbandry = new Tech("Husbandry", "Allows research into horses", 0);
	static Tech mining = new Tech("Mining", "Allows aquisition of metal",1);
	static Tech metal = new Tech("Metal", "Oooooh! Shiny!",1);
	static Tech leather = new Tech("Leather", "Better clothing, increases runner payload by 10%",1.1, Transport.RUNNER);
	static Tech snacks = new Tech("Snacks", "Runners can eat while running, increasing speed by 20%",1.2, Transport.RUNNER);
	static Tech exercise = new Tech("Exercise", "Training Runners to run faster",1.5, Transport.RUNNER);
	static Tech protein = new Tech("Protein", "Increse Runner strength and speed by 10%",1.2, Transport.RUNNER);
	static Tech gym = new Tech("Gym", "Increase runner payload by 10%", 1.1, Transport.RUNNER);
	static Tech horses = new Tech("Horses", "Horses are much faster than Runners!", Transport.HORSE_TYPE, Transport.HORSE);
	static Tech saddles = new Tech("Saddles", "Increases horse payload by 20%",1.2, Transport.HORSE);
	static Tech saddleBags = new Tech("Saddle Bags", "Double Horse payload",2, Transport.HORSE);
	static Tech horseFood = new Tech("Horse Food", "Increases horse speed by 50%", 1.5, Transport.HORSE, Transport.CARRIAGE, Transport.CARAVAN);
	static Tech horseShoes = new Tech("Horseshoes", "Increases horse speed by 10%", 1.1, Transport.HORSE, Transport.CARRIAGE, Transport.CARAVAN);
	static Tech wheel = new Tech("The Wheel", "It Rolls.", 1);
	static Tech weights = new Tech("Weight Training", "Do Runners even lift?. Increase payload by 20%", 1.2, Transport.RUNNER);
	static Tech breeding = new Tech("Enhanced Breeding", "Horses are faster and stronger",1.5, Transport.HORSE, Transport.CARAVAN );
	static Tech dirtRoad = new Tech("Dirt Roads", "Easier for all units to traverse",1, Road.DIRT);
	
	
	static Tech olympics = new Tech("Olympics", "Competitive Running!", 1.1, Transport.RUNNER );
	static Tech relays = new Tech("Relays", "Runner better at delivering things.", 1.1, Transport.RUNNER);
	static Tech carrot = new Tech("Carrot on a Stick", "Motivated horses to move faster", 1.2, Transport.HORSE, Transport.CARRIAGE, Transport.CARAVAN);
	static Tech spurs = new Tech("Spurs", "Better control of horses", 1.1, Transport.HORSE);
	static Tech ironHorseShoes = new Tech("Iron Horse Shoes", "Increase horse speed by 10%", 1.1, Transport.HORSE, Transport.CARRIAGE, Transport.CARAVAN);
	static Tech smithing = new Tech("Smithing", "Make cool stuff", 1);
	static Tech smootherRoads = new Tech("Smoother Roads", "Runners and horses dont trip.", 1.1, Transport.RUNNER, Transport.HORSE, Transport.CARAVAN, Transport.CARRIAGE);
	static Tech ditches = new Tech("Ditches", "Drier roads, Runners and horses don't get stuck in mud anymore.", 1.1, Transport.RUNNER, Transport.HORSE, Transport.CARAVAN, Transport.CARRIAGE);
	static Tech iron = new Tech("Iron", "Make better cool stuff", 1);
//	static Tech compass = new Tech("Compass", "");
	static Tech cartography = new Tech("Cartography", "Maps are good", 1);
	static Tech medicine = new Tech("Medicine", "Heal sick runners and horses, instead of shoot them!", 1.1, Transport.RUNNER, Transport.HORSE, Transport.CARAVAN, Transport.CARRIAGE);
	//static Tech literacy = new Tech("Literacy", "");
	static Tech steel = new Tech("Steel", "Stronger than iron, leads to way cooler stuff. (Like railroads)", 1);
	//static Tech steelFarm = new Tech("Steel Farm Equipment", "");
	static Tech math = new Tech("Math", "42", 1);
	static Tech carriage = new Tech("Horse Drawn Carriage", "Way more space to store stuff", Transport.CARRIAGE_TYPE, Transport.CARRIAGE);
	static Tech metalRims = new Tech("Metal Rims", "Adds coolness, so increases carriage speed by 10%", 1);
	static Tech lighterCarriage = new Tech("Lighter Carriage", "increase crriage speed by 20%", 1.2, Transport.CARRIAGE);
	static Tech twoHorse = new Tech("Two Horse Carriage", "Bouble the horses doubles the speed.", 2, Transport.CARRIAGE);
	static Tech caravan = new Tech("Caravan", "Groups of carriages carry way more", Transport.CARRIAGE_TYPE, Transport.CARAVAN);
	//static Tech lanterns = new Tech("Lanterns", "");
	static Tech steam = new Tech("Steam", "Energy source for science", 1);
	static Tech engineering = new Tech("Engineering", "Build really cool stuff", 1);
	static Tech catapult = new Tech("Catapult", "Wheeeeeeeeeeee!", 1); //TODO CATAPULTS!*******************************************************
	static Tech biggerCatapult = new Tech("BIGGER Catapult", "Bigger is better", 1.5, Transport.CATAPAULT);
	static Tech tension = new Tech("High Tension Rope", "Shoot further", 2, Transport.CATAPAULT_RANGE);
	static Tech onager = new Tech("Onager", "Bigger payload and higher distance", 2, Transport.CATAPAULT_RANGE, Transport.CATAPAULT);
	static Tech shaftMining = new Tech("Shaft Mining", "Its mining. With shafts.", 1);
	static Tech metallurgy = new Tech("Metallurgy", "Almost to railroads!", 1);
	static Tech engine = new Tech("Engine", "Make things happen", 1);
	static Tech railroad = new Tech("Rail Roads", "CHOO CHOO!!!", Transport.TRAIN_TYPE, Transport.TRAIN, Road.RAIL);
	static Tech modernScience = new Tech("Modern Science", "Science, Biatch!", 1);
	static Tech steroids= new Tech("Steroids", "Enhances performance by 300%.", 3, Transport.RUNNER);
	static Tech horseSteroids = new Tech("Horse Steroids", "Enhances horse performance by 300%", 3, Transport.HORSE, Transport.CARAVAN, Transport.CARRIAGE);
	static Tech printPress = new Tech("Printing Press", "Messages are produced way quicker", 2, City.literacy);
//	static Tech method = new Tech("Scientific Method", "", 1);
	static Tech fossil = new Tech("Fossil Fuels", "Yay pollution!", 1);
	static Tech combustion = new Tech("Combustion Engine", "Much more powerful engines", 1);
	static Tech coal = new Tech("Coal", "Better fuel for trains, increase speed by 20%",1.2, Transport.TRAIN);
	static Tech locomotive = new Tech("Locomotive", "Powerful train that can haul 50% more",1.5, Transport.TRAIN);
	static Tech longTrains = new Tech("Long Trains" , "Longer trains carry more mail",1.5, Transport.TRAIN);
	static Tech cowCatchers = new Tech("Cow Catchers", "Gets stuff out of the way, increasing speed by 10%",1.1, Transport.TRAIN);
	static Tech electricity = new Tech("Electricity", "It Glows!",1);
	static Tech oil=  new Tech("Oil", "Drill Everywhere!",1);
	static Tech asphault = new Tech("Asphault", "Way better than dirt.",1);
	static Tech paved = new Tech("Paved Road", "Unlocks Paved Roads",1);
	static Tech cars = new Tech("Cars", "Can transport way more than caravans. Need paved roads",Transport.CAR_TYPE, Transport.CAR);
	static Tech gasoline = new Tech("Gasoline", "Important fuel",1);
	static Tech diesel = new Tech("Diesel", "Better fuel",1);
	static Tech jetFuel = new Tech("Jet Fuel", "High quality fuel. Might can use it in the future.",1);
	static Tech beams = new Tech("Steel Beams", "Do they melt?",1);
	static Tech aerodynamics = new Tech("Aerodynamics", "Speeds up cars and trains by 10%.",1.1, Transport.CAR, Transport.TRAIN, Transport.SEMI, Transport.TRUCK);
	static Tech aviation = new Tech("Aviation", "Flying looks cool",1);
	static Tech airplanes = new Tech("Airplanes", "Flying is fun!",1);
	static Tech airports = new Tech("Airports", "Unlocks Airports. Can only be built in cities.",1);
	
	static Tech dieselTrain = new Tech("Diesel Locomotive", "Faster by 10%",1.1, Transport.TRAIN);
	static Tech trucks = new Tech("Trucks", "Haul twice as much as cars",Transport.CAR_TYPE, Transport.TRUCK);
	static Tech powerLines = new Tech("Power Lines", "Zap!",1);
	static Tech electricTrain = new Tech("Electric Trains", "20% more efficient",1.2, Transport.TRAIN);
	static Tech bulletTrain = new Tech("Bullet Train", "Twice as fast as electric trains",2, Transport.TRAIN);
	static Tech battery = new Tech("Batteries", "Road vehicles can run longer",1.2, Transport.CAR, Transport.TRAIN, Transport.SEMI, Transport.TRUCK);
	static Tech semi = new Tech("Semi-Trailer Truck", "Carries 10x as much as trucks", Transport.CAR_TYPE, Transport.SEMI);
	static Tech electricCar = new Tech("Electric Car", "More efficient that gas",1.2, Transport.CAR,Transport.TRUCK , Transport.SEMI);
	
	static Tech EPA = new Tech("EPA", "Demands fuel efficiency",1.2, Transport.CAR, Transport.TRUCK, Transport.SEMI);
	static Tech efficiency= new Tech("Fuel Efficiency", "Regulations require even more efficiency. ",1.2, Transport.CAR, Transport.TRUCK, Transport.SEMI);
	
	static Tech AI = new Tech("AI", "Its not self aware. Yet.",1);
	
	static Tech selfCar = new Tech("Self Driving Car", "Twice as efficient",2, Transport.CAR, Transport.TRUCK, Transport.SEMI);
	static Tech selfTrain = new Tech("Self Driving Train", "Twice as efficient",2, Transport.TRAIN);
	static Tech selfPlane = new Tech("AutoPilot", "Twice as efficient",2, Transport.PLANE);
	
	static Tech singularity = new Tech("Singularity", "Machines take over. Mail is no longer needed. Have infinite Money", java.lang.Double.POSITIVE_INFINITY, LD36.theLD);
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
//		method.row = 2;
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
		wheel.addParent(leather);
		mining.addParent(wheel);
//		mining.addParent(leather);
		metal.addParent(mining);
//		wheel.addParent(metal);
//		dirtRoad.addParent(wheel);
		dirtRoad.addParent(metal);
		
		
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
//		method.addParent(printPress);
		fossil.addParent(printPress);
		
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
		selfTrain.addParent(bulletTrain);
		selfPlane.addParent(airports);
		
		singularity.addParent(selfCar);
		singularity.addParent(selfTrain);
		singularity.addParent(selfPlane);
	}
	
	static{
		sandals.cost = 10;
		shoeLaces.cost = 750;
		shoes.cost = 30;
		snacks.cost = 1250;
		exercise.cost = 1800;
		protein.cost = 2400;
		gym.cost = 3000;
		weights.cost = 4000;
		
		wheel.cost = 2600;
		
		
		agriculture.cost = 50;
		paper.cost = 100;
		ink.cost = 300;
		husbandry.cost = 1000;

		leather.cost = 2000;
		horses.cost = 5000;
		mining.cost = 8000;
		metal.cost = 10000;
		
		saddles.cost = 10000;
		saddleBags.cost = 16000;
		horseFood.cost = 35000;
		horseShoes.cost = 40000;
		
		breeding.cost = 100000;
		
		olympics.cost = 30000;
		relays.cost = 60000;
		
		carriage.cost = 1e6;
		smithing.cost = 5e6;
		carrot.cost = 3.5e6;
		spurs.cost = 5e6;
		
		
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
