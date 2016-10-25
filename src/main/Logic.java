
package main;

import java.util.Map.Entry;
import java.util.EnumMap;

/**
 * The logic for the CivIlde Game
 * 
 * @author Zecrus
 * @author puhrjj
 *
 */
public class Logic {
	public enum Buffs {
		HUNT_A, WARR_A, HUNT_B, MILITARY_MOD, TRADE_BASE, WARR_B, CONS_MULT, MEDI_DISSENT_MOD, PRIDE, HARBOR_MILI, HARBOR_EXPANSION, WALL_DISSENT, WALL_WARRIOR

	}

	// TODO fleshout Looks UGLY
	public enum Tech {
		TRADE;
	};

	/**
	 * The different forms of Government available to the player
	 * 
	 * @author puhrjj
	 *
	 */
	public enum Government {
		// @formatter:off

		TRIBE(0, 0, "Tribe"),
		DEM1(2, 1000, "Democracy"),
		DEM2(4, 100000, "Democracy 2"),
		THEO1(1, 1000,	"Theocracy"),
		THEO2(2, 100000, "Theocracy 2"),
		WAR1(1, 1000, "Warrior Society"),
		WAR2(2, 100000,	"Warrior Soceiety 2"),
		CONFED(4, 5000000, "Confederation"),
		FEUD(4, 5000000, "Feudalism"),
		REP(4, 5000000, "Republic"), 
		CASTE(4, 5000000, "Caste System"),
		KING(4, 5000000, "Kingdom"),
		SENATE(4, 5000000, "Senate");
		// @formatter:on

		private final int governance_required;
		private final double SP_required;
		private final String name;

		/**
		 * How to call and add a form of government
		 * 
		 * @param governance_required
		 *            - the level of research needed to unlock this form of
		 *            government
		 * @param SP_required
		 *            - the amount of SP required to unlock this form of
		 *            government
		 * @param name
		 *            - The text the player sees to associate with this form of
		 *            government
		 */
		Government(int governance_required, double SP_required, String name) {
			this.governance_required = governance_required;
			this.SP_required = SP_required;
			this.name = name;
		}

		/**
		 * @return The SP Cost to unlock this form of government
		 */
		public double sp() {
			return SP_required;
		}

		/**
		 * @return the level of research needed to unlock this form of
		 *         government
		 */
		public int governance() {
			return governance_required;
		}

		@Override
		public String toString() {
			return this.name;
		}

	};

	/**
	 * A container for the current form of government - Default:
	 * Government.TRIBE
	 * 
	 * @see Government
	 */
	private Government government = Government.TRIBE;

	/**
	 * The different ways a unit can be assigned
	 * 
	 * @author puhrjj
	 *
	 */
	public enum Jobs {
		//@formatter:off
		/**
		 * The default state for units however should generally be empty
		 */
		IDLE("Idle"),
		/**
		 * Hunters produce food either through HUNT_A 'hunting' or HUNT_B 'Farming'
		 */
		HUNTERS("Hunters"),
		/**
		 * Warriors Conquer land 
		 */
		WARRIORS("Warriors"),
		/**
		 * Builders produce BP used in the construction of Buildings
		 */
		BUILDERS("Builders"),
		/**
		 * Scientists produce SP used in researching sciences and methods
		 */
		SCIENTISTS("Scientists"),
		/** 
		 * Engineers produce EP used in developing technologies
		 */
		ENGINEERS("Engineers"),
		/**
		 * Crafters make Products which in turn generate Gold
		 */
		CRAFTERS("Crafters");
		
		//@formatter:on
		private final String name;

		/**
		 * How to call and add forms of Jobs for units
		 * 
		 * @param name
		 *            - What the player sees associated with this form of work
		 */
		Jobs(String name) {
			this.name = name;
		}

		/**
		 * Call this when using the workforce array
		 * 
		 * @return the position within the group
		 */
		public int index() {
			return this.ordinal();
		}

		@Override
		public String toString() {
			return this.name;
		}
	};

	/**
	 * A container for the available units
	 */
	private double people;

	/**
	 * A container populating how the workforce is divided
	 * 
	 * @see Jobs.index()
	 */
	private double[] workforce = new double[Jobs.values().length];

	public enum EngType {
		//@formatter:off
		FILLER("ordo"),
		SPEAR("Spear"),
		NET("Net"), 
		BOW("Bow"),
		BOAT("Boat"), 
		FARM("Farm"),
		CTOOLS("Construction Tools"),
		MED("Medicine"),
		TAX("Tax"),
		SIEGE("Siege"), 
		WRITE("Writing"),
		DOM("Domestication"),
		SWORD("Sword"),
		CHARIOT("Chariot"),
		ELECTRIC("Electronic"),
		VEHICLE("Vehicle"),
		OPTICS("Optics")
		
		;		
		//@formatter:on
		private String name;

		private EngType(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return this.name;
		}
	}

	public enum Engineering {
		// @formatter:off
		INSTRUMENTUM(EngType.FILLER, 0, "filler", new Science[] {}, new int[] {},  new Engineering[] { }, new Buffs[] {}, new double[] {}), 
		SPEAR1(EngType.SPEAR,2000, "Sharpened Stick", new Science[] { Science.COGNITIO }, new int[] { 0 }, new Engineering[] { INSTRUMENTUM }, new Buffs[] {Buffs.HUNT_A,Buffs.WARR_A}, new double[] {.25,.25}),
		SPEAR2(EngType.SPEAR,10000, "Stone Spear", new Science[] { Science.MILITARY }, new int[] { 1 },	new Engineering[] { SPEAR1 }, new Buffs[] {Buffs.HUNT_A,Buffs.WARR_A}, new double[] {.25,.25}),
		SPEAR3(EngType.SPEAR,50000, "Bronze Spear", new Science[] { Science.MILITARY }, new int[] { 2 }, new Engineering[] { SPEAR2 }, new Buffs[] {Buffs.WARR_A}, new double[] {.5}),
		SPEAR4(EngType.SPEAR,2000000, "Iron Spear", new Science[] { Science.MILITARY }, new int[] { 4 }, new Engineering[] { SPEAR3 }, new Buffs[] {Buffs.WARR_A}, new double[] {.5}),
		NET1(EngType.NET,10000, "Small Net", new Science[] { Science.COGNITIO }, new int[] { 0 }, new Engineering[] { INSTRUMENTUM }, new Buffs[] {Buffs.HUNT_B}, new double[] {.25}),
		NET2(EngType.NET,100000, "Large Net", new Science[] { Science.COGNITIO }, new int[] { 0 }, new Engineering[] { NET1 }, new Buffs[] {Buffs.HUNT_B}, new double[] {.25}),
		BOW1(EngType.BOW,100000, "Short Bow", new Science[] { Science.MILITARY }, new int[] { 3 }, new Engineering[] { INSTRUMENTUM }, new Buffs[] {Buffs.HUNT_B, Buffs.MILITARY_MOD}, new double[] {.25,.25}),
		BOW2(EngType.BOW,300000, "Long Bow", new Science[] { Science.MILITARY, Science.PHYSICS }, new int[] { 3, 3 }, new Engineering[] { BOW1 }, new Buffs[] {Buffs.HUNT_A,Buffs.WARR_A}, new double[] {.5,.5}),//same level of military?
		BOW3(EngType.BOW,30000000, "Compound Bow", new Science[] { Science.MILITARY, Science.PHYSICS }, new int[] { 5, 4 }, new Engineering[] { BOW2 }, new Buffs[] {Buffs.WARR_A}, new double[] {.5}),
		BOAT1(EngType.BOAT,300000, "Raft", new Science[] {Science.PHYSICS}, new int[] {3},  new Engineering[] {INSTRUMENTUM}, new Buffs[] {Buffs.TRADE_BASE, Buffs.HUNT_B}, new double[] {1,.25}), 
		BOAT2(EngType.BOAT,3000000, "Trireme", new Science[] {Science.MILITARY,Science.PHYSICS}, new int[] {3,4},  new Engineering[] {BOAT1 }, new Buffs[] {Buffs.TRADE_BASE, Buffs.WARR_B}, new double[] {1,.5}), 
		BOAT3(EngType.BOAT,3000000, "Caravel", new Science[] {Science.PHYSICS}, new int[] {4},  new Engineering[] {BOAT2 }, new Buffs[] {Buffs.TRADE_BASE, Buffs.HUNT_B}, new double[] {2,.5}), 
		BOAT4(EngType.BOAT,3000000, "Steamship", new Science[] {Science.MILITARY,Science.PHYSICS}, new int[] {3,4},  new Engineering[] {BOAT3 }, new Buffs[] {Buffs.TRADE_BASE, Buffs.WARR_B}, new double[] {2,.5}), 
		FARM1(EngType.BOAT,10000000, "Farming", new Science[] {Science.BIO}, new int[] {3},  new Engineering[] { INSTRUMENTUM}, new Buffs[] {Buffs.HUNT_A, Buffs.HUNT_B}, new double[] {1.5}), 
		FARM2(EngType.BOAT,100000000, "Plantation", new Science[] {Science.BIO}, new int[] {4},  new Engineering[] {FARM1 }, new Buffs[] {Buffs.HUNT_A, Buffs.HUNT_B}, new double[] {1.5}),
		CTOOLS1(EngType.CTOOLS,30000, "Stone Tools", new Science[] {Science.PHYSICS,Science.CONSTRUCTION}, new int[] {2,2},  new Engineering[] {INSTRUMENTUM }, new Buffs[] {Buffs.CONS_MULT}, new double[] {.5}),
		CTOOLS2(EngType.CTOOLS,500000, "Basic Metal Tools", new Science[] {Science.PHYSICS,Science.CONSTRUCTION}, new int[] {3,3},  new Engineering[] {CTOOLS1 }, new Buffs[] {Buffs.CONS_MULT}, new double[] {.5}),
		CTOOLS3(EngType.CTOOLS,500000, "Advanced Metal Tools", new Science[] {Science.PHYSICS,Science.CONSTRUCTION}, new int[] {5,5},  new Engineering[] {CTOOLS2 }, new Buffs[] {Buffs.CONS_MULT}, new double[] {1}),//Same EP Cost?
		MED1(EngType.MED,3000, "Cauterization", new Science[] {Science.BIO}, new int[] {1},  new Engineering[] {INSTRUMENTUM }, new Buffs[] {Buffs.MEDI_DISSENT_MOD}, new double[] {.97}),
		MED2(EngType.MED,30000, "Leaches", new Science[] {Science.BIO}, new int[] {2},  new Engineering[] { MED1}, new Buffs[] {Buffs.MEDI_DISSENT_MOD}, new double[] {.95}),
		MED3(EngType.MED,3000000, "Medicine", new Science[] {Science.BIO}, new int[] {4},  new Engineering[] {MED2 }, new Buffs[] {Buffs.MEDI_DISSENT_MOD}, new double[] {.9}),
		MED4(EngType.MED,300000000, "Hospitals", new Science[] {Science.BIO}, new int[] {6},  new Engineering[] {MED3 }, new Buffs[] {Buffs.MEDI_DISSENT_MOD}, new double[] {.85}),
		TAX1(EngType.TAX,1000000, "Tax Collectors", new Science[] {Science.ECON}, new int[] {3},  new Engineering[] {INSTRUMENTUM}, new Buffs[] {Buffs.PRIDE}, new double[] {0}),
		TAX2(EngType.TAX,30000000, "Mail-in Taxes", new Science[] {Science.ECON}, new int[] {4},  new Engineering[] { TAX1}, new Buffs[] {Buffs.PRIDE}, new double[] {0}),
		TAX3(EngType.TAX,1000000000, "E-Tax", new Science[] {Science.ECON}, new int[] {6},  new Engineering[] { TAX2}, new Buffs[] {Buffs.PRIDE}, new double[] {0}),
		SIEGE1(EngType.SIEGE,500000, "Balista", new Science[] {Science.MILITARY,Science.PHYSICS}, new int[] {3,4},  new Engineering[] { INSTRUMENTUM}, new Buffs[] {Buffs.WARR_B}, new double[] {.25}),
		SIEGE2(EngType.SIEGE,5000000, "Catapult", new Science[] {Science.MILITARY,Science.PHYSICS}, new int[] {3,5},  new Engineering[] { SIEGE1}, new Buffs[] {Buffs.WARR_B}, new double[] {.5}),
		SIEGE3(EngType.SIEGE,10000000, "Trebuchet", new Science[] {Science.MILITARY,Science.PHYSICS}, new int[] {4,6},  new Engineering[] {SIEGE2 }, new Buffs[] {Buffs.WARR_B}, new double[] {.5}),
		SIEGE4(EngType.SIEGE,10000000, "Cannon", new Science[] {Science.MILITARY,Science.PHYSICS}, new int[] {6,7},  new Engineering[] { SIEGE3}, new Buffs[] {Buffs.WARR_B}, new double[] {1}),
		SIEGE5(EngType.SIEGE,10000000, "Howitzer", new Science[] {Science.MILITARY,Science.PHYSICS}, new int[] {7,8},  new Engineering[] {SIEGE4 }, new Buffs[] {Buffs.WARR_B}, new double[] {1}),
		SIEGE6(EngType.SIEGE,10000000, "Rocket artillery", new Science[] {Science.MILITARY,Science.PHYSICS}, new int[] {8,9},  new Engineering[] { SIEGE5}, new Buffs[] {Buffs.WARR_B}, new double[] {1}),
		WRITE1(EngType.WRITE,10000, "Parchment", new Science[] {Science.COGNITIO}, new int[] {0},  new Engineering[] { INSTRUMENTUM}, new Buffs[] {Buffs.PRIDE}, new double[] {0}),
		WRITE2(EngType.WRITE,1000000, "Paper", new Science[] {Science.COGNITIO}, new int[] {0},  new Engineering[] { WRITE1}, new Buffs[] {Buffs.PRIDE}, new double[] {0}),
		WRITE3(EngType.WRITE,100000000, "Movable Types", new Science[] {Science.COGNITIO}, new int[] {0},  new Engineering[] { WRITE2}, new Buffs[] {Buffs.PRIDE}, new double[] {0}),
		WRITE4(EngType.WRITE,100000000, "Printing Press", new Science[] {Science.COGNITIO}, new int[] {0},  new Engineering[] { WRITE3}, new Buffs[] {Buffs.PRIDE}, new double[] {0}),
		DOM1(EngType.DOM,30000, "Livestock", new Science[] {Science.BIO}, new int[] {1},  new Engineering[] {INSTRUMENTUM }, new Buffs[] {Buffs.HUNT_B}, new double[] {.25}),
		DOM2(EngType.DOM,1000000, "Horses", new Science[] {Science.BIO}, new int[] {2},  new Engineering[] { DOM1}, new Buffs[] {Buffs.WARR_B,Buffs.CONS_MULT}, new double[] {.25,.5}),
		DOM3(EngType.DOM,30000000, "Selective Breeding", new Science[] {Science.BIO}, new int[] {3},  new Engineering[] {DOM2 }, new Buffs[] {Buffs.WARR_A, Buffs.CONS_MULT}, new double[] {.25,.5}),
		SWORD1(EngType.SWORD,100000, "Bronze Sword", new Science[] {Science.MILITARY}, new int[] {3},  new Engineering[] {INSTRUMENTUM }, new Buffs[] {Buffs.WARR_A}, new double[] {.25}),
		SWORD2(EngType.SWORD,30000000, "Iron Sword", new Science[] {Science.MILITARY}, new int[] {4},  new Engineering[] {SWORD1 }, new Buffs[] {Buffs.WARR_A}, new double[] {.25}),
		SWORD3(EngType.SWORD,1000000000, "Steel Sword", new Science[] {Science.MILITARY}, new int[] {6},  new Engineering[] {SWORD2 }, new Buffs[] {Buffs.WARR_A}, new double[] {.25}),
		CHARIOT1(EngType.CHARIOT,500000, "Simple Chariot", new Science[] {Science.MILITARY, Science.PHYSICS}, new int[] {3,3},  new Engineering[] { INSTRUMENTUM}, new Buffs[] {Buffs.WARR_B}, new double[] {.25}),
		CHARIOT2(EngType.CHARIOT,5000000, "Reinforced Chariot", new Science[] {Science.MILITARY,Science.PHYSICS}, new int[] {3,4},  new Engineering[] {CHARIOT1 }, new Buffs[] {Buffs.WARR_A}, new double[] {.25}),
		CHARIOT3(EngType.CHARIOT,10000000, "Caravan", new Science[] {Science.ECON, Science.PHYSICS}, new int[] {4,4},  new Engineering[] { CHARIOT2}, new Buffs[] {Buffs.TRADE_BASE}, new double[] {1}),
		ELECTRIC1(EngType.ELECTRIC, 30000000, "Light Bulb",new Science[] {Science.MILITARY, Science.PHYSICS}, new int[]{3,5}, new Engineering[] {INSTRUMENTUM}, new Buffs[] {Buffs.PRIDE},new double[]{0}),
		ELECTRIC2(EngType.ELECTRIC, 30000000, "Radio",new Science[] {Science.MILITARY, Science.PHYSICS}, new int[]{3,5}, new Engineering[] {ELECTRIC1}, new Buffs[] {Buffs.PRIDE},new double[]{0}),
		ELECTRIC3(EngType.ELECTRIC, 30000000, "Computer",new Science[] {Science.MILITARY, Science.PHYSICS}, new int[]{3,5}, new Engineering[] {ELECTRIC2}, new Buffs[] {Buffs.PRIDE},new double[]{0}),
		VEHICLE1(EngType.VEHICLE, 30000000, "Steam Powered Train",new Science[] {Science.PHYSICS}, new int[]{5}, new Engineering[] {INSTRUMENTUM}, new Buffs[] {Buffs.PRIDE},new double[]{0}),
		VEHICLE2(EngType.VEHICLE, 30000000, "Internal Combustion Engine",new Science[] {Science.PHYSICS}, new int[]{5}, new Engineering[] {VEHICLE1}, new Buffs[] {Buffs.PRIDE},new double[]{0}),
		VEHICLE3(EngType.VEHICLE, 30000000, "Air Craft",new Science[] {Science.PHYSICS}, new int[]{5}, new Engineering[] {VEHICLE2}, new Buffs[] {Buffs.PRIDE},new double[]{0}),
		VEHICLE4(EngType.VEHICLE, 30000000, "Rocket",new Science[] {Science.PHYSICS}, new int[]{5}, new Engineering[] {VEHICLE3}, new Buffs[] {Buffs.PRIDE},new double[]{0}),
		OPTICS1(EngType.OPTICS, 5000000, "Telescope",new Science[] {Science.MILITARY, Science.PHYSICS}, new int[]{3,4}, new Engineering[] {INSTRUMENTUM}, new Buffs[] {Buffs.PRIDE},new double[]{0}),
		OPTICS2(EngType.OPTICS, 300000000, "Camera",new Science[] {Science.MILITARY, Science.PHYSICS}, new int[]{4,6}, new Engineering[] {OPTICS1}, new Buffs[] {Buffs.PRIDE},new double[]{0}),
		OPTICS3(EngType.OPTICS, 300000000, "Cathode Ray Tube",new Science[] {Science.MILITARY, Science.PHYSICS}, new int[]{4,6}, new Engineering[] {OPTICS2}, new Buffs[] {Buffs.PRIDE},new double[]{0}),
		OPTICS4(EngType.OPTICS, 300000000, "Liquid Crystal Display",new Science[] {Science.MILITARY, Science.PHYSICS}, new int[]{4,6}, new Engineering[] {OPTICS3}, new Buffs[] {Buffs.PRIDE},new double[]{0}),
		;// @formatter:on

		private double ep;
		private String name;
		private EngType type;
		private Science[] requiredScience;
		private int[] requiredScienceLvl;
		private Engineering[] requiredEng;
		private Buffs[] benefit;
		private double[] benefitVal;

		Engineering(EngType type, double epCost, String name, Science[] requiredScience, int[] requiredScienceLvl,
				Engineering[] reEngineering, Buffs[] benefit, double[] benefitVal) {
			this.ep = epCost;
			this.name = name;
			this.type = type;
			this.requiredScience = requiredScience;
			this.requiredScienceLvl = requiredScienceLvl;
			this.requiredEng = reEngineering;
			this.benefit = benefit;
			this.benefitVal = benefitVal;

		}

		public double requiredEP() {
			return this.ep;
		}

		@Override
		public String toString() {
			return this.name;
		}

		public Science[] requiredScience() {
			return this.requiredScience;
		}

		public int[] requiredScienceLvl() {
			return requiredScienceLvl;
		}

		public Engineering[] requiredEng() {
			return this.requiredEng;
		}

		public Buffs[] benefit() {
			return this.benefit;
		}

		public double[] benefitVal() {
			return this.benefitVal;
		}

		public EngType type() {
			return this.type;
		}

		public String typeToString() {
			return this.type.toString();
		}

		public int getIndex() {
			return this.ordinal();
		}

	}

	private EnumMap<EngType, Engineering> engTechLvls = new EnumMap<EngType, Engineering>(EngType.class);

	public enum Science {
		//@formatter:off
		COGNITIO ("filler"),
		MILITARY ("Military Science"),
		PHYSICS ("Physics"),
		GOVERNANCE ("Governance"),
		CONSTRUCTION ("Construction"),
		ECON ("Economics"),
		BIO("Biology"),
		PRODUCITON ("Produciton");
		//@formatter:on
		private String name;

		Science(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return this.name;
		}
	}

	private EnumMap<Science, Integer> scienceLevels = new EnumMap<Science, Integer>(Science.class);

	public enum Food {
		NORMAL("Normal", 1, 1), HIGHER("Higher", 1.5, .9), HIGHEST("Highest", 2, .8);
		private String name;
		private double consumption;
		private double dissent;

		Food(String name, double consumption, double dissent) {
			this.name = name;
			this.consumption = consumption;
			this.dissent = dissent;
		}

		@Override
		public String toString() {
			return this.name;
		}

		public double consumption() {
			return this.consumption;
		}

		public double dissent() {
			return this.dissent;
		}
	}

	private double food;

	public enum BuildType{
		 TEMPLE("Temple"),
		 CIRCUS("Circus"),
		 HOUSE1("Mud Huts"),
		 HOUSE2("Brick House"),
		 HOUSE3("Mansions"),
		 AQUADUCT("Aquaducts"),
		 UNIVERSITY("University"),
		 SCHOOL("School"),
		 WALLS("Wall"),
		 HARBOR("Harbor"),
		 BARRACKS("Barracks"),
		 ROAD("Road");
		private String name;
		BuildType(String name){
			this.name = name;
		}
		@Override
		public String toString() {
			return this.name;
		}
	}

	public enum Building {
		// @formatter:off
		HARBOR1("Beach", BuildType.HARBOR, 0, 0, new Science[] {Science.COGNITIO}, new int[]{0}, new Engineering[]{Engineering.BOAT1}, new Buffs[] {Buffs.HARBOR_MILI, Buffs.HARBOR_EXPANSION},new double[]{1, 0}),
		HARBOR2("Dock",  BuildType.HARBOR, 500000, .04, new Science[] {Science.COGNITIO}, new int[]{0}, new Engineering[]{Engineering.BOAT1}, new Buffs[] {Buffs.HARBOR_MILI, Buffs.HARBOR_EXPANSION},new double[]{1.1, .1}), 
		HARBOR3("Pier",  BuildType.HARBOR, 1500000, .12, new Science[] {Science.COGNITIO}, new int[]{0}, new Engineering[]{Engineering.BOAT1}, new Buffs[] {Buffs.HARBOR_MILI, Buffs.HARBOR_EXPANSION},new double[]{1.2, .25}), 
		HARBOR4("Port",  BuildType.HARBOR, 5000000, .4, new Science[] {Science.COGNITIO}, new int[]{0}, new Engineering[]{Engineering.BOAT1}, new Buffs[] {Buffs.HARBOR_MILI, Buffs.HARBOR_EXPANSION},new double[]{ 1.3, .5}),
		WALL1("Fence", BuildType.WALLS, 0, 0, new Science[] {Science.CONSTRUCTION,Science.MILITARY}, new int[]{3,2}, new Engineering[]{Engineering.INSTRUMENTUM}, new Buffs[]{Buffs.WALL_DISSENT, Buffs.WALL_WARRIOR}, new double[]{1,0}),
		WALL2("Palisade", BuildType.WALLS, 1000000, .002, new Science[] {Science.CONSTRUCTION,Science.MILITARY}, new int[]{3,2}, new Engineering[]{Engineering.INSTRUMENTUM}, new Buffs[]{Buffs.WALL_DISSENT, Buffs.WALL_WARRIOR}, new double[]{.95,2}),
		WALL3("Stone Walls", BuildType.WALLS, 3000000, .006, new Science[] {Science.CONSTRUCTION,Science.MILITARY}, new int[]{3,2}, new Engineering[]{Engineering.INSTRUMENTUM}, new Buffs[]{Buffs.WALL_DISSENT, Buffs.WALL_WARRIOR}, new double[]{.9,4}), 
		WALL4("Guard Towers", BuildType.WALLS, 10000000, .02, new Science[] {Science.CONSTRUCTION,Science.MILITARY}, new int[]{3,2}, new Engineering[]{Engineering.INSTRUMENTUM}, new Buffs[]{Buffs.WALL_DISSENT, Buffs.WALL_WARRIOR}, new double[]{.85,6}),;
//		SCHOOL
//		AQUADUCT
//		BARRACKS
//		UNI
//		TEMPLE
//		HOUSE1
//		ROAD
//		
		// @formatter:on
		
		
		
		private String name;
		private BuildType type;
		private double bp;
		private double upkeep;
		private Science[] sciReq;
		private int[] sciReqLvl;
		private Engineering[] engReq;
		private Buffs[] buffs;
		private double[] buffValues;		
		
		
		
		Building(String name, BuildType type, double bp, double upkeep, Science[] sciReq,
				int[] sciReqLvl, Engineering[] engReq, Buffs[] buffs, double[] buffValue) {
			this.name = name;
			this.type = type;
			this.bp = bp;
			this.upkeep = upkeep;
			this.sciReq = sciReq;
			this.engReq = engReq;
			this.sciReqLvl = sciReqLvl;
			this.buffs=buffs;
			this.buffValues=buffValue;

		}
		
		@Override
		public String toString() {
			return this.name;
		}

		public double bpCost() {
			return this.bp;
		}

		
		public double upkeep() {
			return this.upkeep;
		}

		
		public String typeToString() {
			return this.type.toString();
		}

		public Engineering[] requiredEng() {
			return this.engReq;
		}

		
		public Science[] requiredSci() {
			return this.sciReq;
		}

		
		public int[] requiredSciLvl() {
			return this.sciReqLvl;
		}
		public Buffs[] getBuffs(){
			return this.buffs;
		}
		public double[] getBuffVal(){
			return this.buffValues;
		}
	}
		 
	private EnumMap<BuildType, Building> buildings = new EnumMap<BuildType,Building>(BuildType.class);
	private EnumMap<BuildType, Double> investments = new EnumMap<BuildType,Double>(BuildType.class);

public enum Tax{
	TAX1(0),
	TAX2(3),
	TAX3(8),
	TAX4(15),
	TAX5(25),
	TAX6(40),
	TAX7(60);
	
	private double dissent;
	
	Tax(double dissent){
		this.dissent=dissent;
	}
	
	public double getDissent(){
		return this.dissent;
	}
	public int gettaxLvl(){
		return this.ordinal()+1;
	}
}

	private Tax taxLvl;

	private double food_income;
	private Food consumption;
	private double foodConsumptionMulti;
	private double foodDissentmod;

	private double[] products = new double[4];
	private double[] products_upkeep = new double[4];

	private double gold;
	private double gold_income;
	private double gold_upkeep;
	private double land_permanent;
	private double land_conquered;
	
	private double[] dissent;
	private double bp;
	private double ep;
	private double sp;

	public String message = "hello";

	/*
	 * Functions Ahead
	 * 
	 */

	public boolean setGovernment(Government gov) {
		if (gov == this.government) {
			this.message = String.format("You already use %s as a form of Government", gov.toString());
			return false;
		} else if (this.sp < gov.sp()
				|| this.scienceLevels.get(Science.GOVERNANCE).intValue() < gov.governance_required) {
			this.message = String.format("You need this much %d ep and %n have a Governance Level of %i", gov.sp(),
					gov.governance());
			return false;
		} else {
			this.sp -= gov.sp();
			this.government = gov;
			this.message = String.format("Welcome to the Age of %s", gov.toString());
			return true;
		}
	}

	public Government getGovernment() {
		return this.government;
	}

	public String getGovernmentToString() {
		return this.government.toString();
	}

	public boolean setEngineering(Engineering eng) {
		EngType type = eng.type();
		Engineering currentEng = this.engTechLvls.get(type);

		if (eng == currentEng) {
			this.message = String.format("You already have %s as a form of %s", eng.toString(), eng.typeToString());
			return false;
		} else if (eng.getIndex() < currentEng.getIndex()) {
			this.message = String.format(
					"Technology only moves forward, %nto replace %s with %s as a form of %s is a step backward",
					currentEng.toString(), eng.toString(), type.toString());
			return false;
		} else if (this.ep < eng.requiredEP()) {
			this.message = String.format(
					"You only have %d engingeering points and need %d points,%nchain some more grad students to their desks.",
					this.ep, eng.requiredEP());
		} else if (!checkRequirements(eng)) {
			return false;

		} else {
			this.engTechLvls.put(type, eng);
			this.message = "With these tools we grow ever stronger";
			return true;
		}
		return false;
	}

	private boolean checkRequirements(Engineering eng) {
		StringBuilder string = new StringBuilder();
		Engineering[] engReq = eng.requiredEng();
		Science[] engScienceReq = eng.requiredScience();

		int[] engScienceLvl = eng.requiredScienceLvl();
		int i = 0;
		boolean hasFailed = false;

		for (Science sci : engScienceReq) {
			if (this.scienceLevels.get(sci) < engScienceLvl[i]) {
				hasFailed = true;
				string.append(String.format("Not enough research in %s", sci.toString()));
			}
			i++;
		}
		for (Engineering engrec : engReq) {
			if (this.engTechLvls.get(engrec.type()).getIndex() < engrec.getIndex()) {
				hasFailed = true;
				string.append(String.format("Need to research %s", engrec.toString()));
			}
		}
		if (hasFailed) {
			this.message = string.toString();
			return false;
		} else
			return true;
	}

	public Engineering getEngineering(EngType type) {
		return this.engTechLvls.get(type);
	}

	public String getEngineeringToString(EngType type) {
		return this.engTechLvls.get(type).toString();
	}

	public void assignJobs(double[] percentWorkforce) {
		this.workforce = new double[] { 0, 0, 0, 0, 0, 0, 0 };
		double leftover = this.people;
		for (int i = 0; i < this.workforce.length && leftover > 0; i++) {
			double temp = Math.round(this.people * percentWorkforce[i]);
			leftover -= temp;
			if (leftover > 0) {
				this.workforce[i] = temp;
			} else {
				this.workforce[i] = temp + leftover;
			}
		}
		if (leftover > 0) {
			this.workforce[1] += leftover;
		}

	}

	public double[] getWorkforce() {
		return workforce;
	}

	public boolean setScienceLvl(Science sci) {
		Integer lvl = this.scienceLevels.get(sci);
		double spRequired = getSciencePrice(lvl);
		if (this.sp < spRequired) {
			this.message = String.format("To advance in %s, %d sp is needed", sci.toString(), spRequired);
			return false;

		} else {
			this.sp -= spRequired;
			this.scienceLevels.put(sci, lvl++);
			this.message = String.format("It is a new age for %s", sci.toString());
			return true;
		}

		// TODO Have bj explain the loss in Warrior C for science growth
	}

	public int getScienceLvl(Science sci) {
		return this.scienceLevels.get(sci);
	}

	public double getSciencePrice(int lvl) {
		double sum = 0;
		for (Entry<Science, Integer> sci : scienceLevels.entrySet()) {
			if (sci.getValue() >= lvl)
				sum++;
		}
		sum = Math.pow(2, sum);
		return Math.pow(10, 1 + lvl + sum);
	}

	public void getWorkforceToMessage() {
		StringBuilder string = new StringBuilder();
		for (Jobs j : Jobs.values()) {
			string.append(
					String.format("There are %d %s in the workforce %n", this.workforce[j.index()], j.toString()));
		}
		this.message = string.toString();
	}

	public boolean setFoodConsumption(Food level) {
		if (this.scienceLevels.get(Science.GOVERNANCE) < 2) {
			this.message = "Need to research Rank 2 in Governance";
			return false;
		}

		this.consumption = level;
		this.foodConsumptionMulti = level.consumption();
		this.foodDissentmod = level.dissent();
		return true;
	}

	public String getFoodConsumption() {
		return this.consumption.toString();
	}
	
	public double getDissent(){
		double dissent = 1;
		for (Double d: this.dissent){
			dissent*=d;
			
		}
		return dissent;
	}
}