package main;

import java.util.Map;
import java.util.HashMap;

public class Logic {
	public enum Buffs {
		HUNT_A, WARR_A, HUNT_B, MILITARY_MOD, TRADE_BASE, WARR_B, CONS_MULT, MEDI_DISSENT_MOD, PRIDE

	}

	// TODO fleshout Looks UGLY
	public enum Tech {
		COGNITIO, TEMPLE, CIRCUS, HARBOR, HOUSE1, HOUSE2, HOUSE3, AQUADUCT, UNIVERSITY, SCHOOL, WALLS, BARRACKS, ROAD, TRADE, BIO, ECON, MILITARY, CONSTRUCTION, GOVERNANCE, PHYSICS, PRODUCITON
	};

	// @formatter:off
	public enum Government {
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

		Government(int governance_required, double SP_required, String name) {
			this.governance_required = governance_required;
			this.SP_required = SP_required;
			this.name = name;
		}

		public double sp() {
			return SP_required;
		}

		public int governance() {
			return governance_required;
		}

		@Override
		public String toString() {
			return this.name;
		}

	};

	public enum Jobs {
		//@formatter:off
		IDLE("Idle"),
		HUNTERS("Hunters"),
		WARRIORS("Warriors"),
		BUILDERS("Builders"),
		SCIENTISTS("Scientists"),
		ENGINEERS("Engineers"),
		CRAFTERS("Crafters");
		
		//@formatter:on
		private final String name;

		Jobs(String name) {
			this.name = name;
		}

		public int index() {
			return this.ordinal();
		}

		@Override
		public String toString() {
			return this.name;
		}
	};

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
		CHARIOT("Chariot");
		
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
		INSTRUMENTUM(EngType.FILLER, 0, "filler", new Tech[] {}, new int[] {},  new Engineering[] { }, new Buffs[] {}, new double[] {}), 
		SPEAR1(EngType.SPEAR,2000, "Sharpened Stick", new Tech[] { Tech.COGNITIO }, new int[] { 0 }, new Engineering[] { INSTRUMENTUM }, new Buffs[] {Buffs.HUNT_A,Buffs.WARR_A}, new double[] {.25,.25}),
		SPEAR2(EngType.SPEAR,10000, "Stone Spear", new Tech[] { Tech.MILITARY }, new int[] { 1 },	new Engineering[] { SPEAR1 }, new Buffs[] {Buffs.HUNT_A,Buffs.WARR_A}, new double[] {.25,.25}),
		SPEAR3(EngType.SPEAR,50000, "Bronze Spear", new Tech[] { Tech.MILITARY }, new int[] { 2 }, new Engineering[] { SPEAR2 }, new Buffs[] {Buffs.WARR_A}, new double[] {.5}),
		SPEAR4(EngType.SPEAR,2000000, "Iron Spear", new Tech[] { Tech.MILITARY }, new int[] { 4 }, new Engineering[] { SPEAR3 }, new Buffs[] {Buffs.WARR_A}, new double[] {.5}),
		NET1(EngType.NET,10000, "Small Net", new Tech[] { Tech.COGNITIO }, new int[] { 0 }, new Engineering[] { INSTRUMENTUM }, new Buffs[] {Buffs.HUNT_B}, new double[] {.25}),
		NET2(EngType.NET,100000, "Large Net", new Tech[] { Tech.COGNITIO }, new int[] { 0 }, new Engineering[] { NET1 }, new Buffs[] {Buffs.HUNT_B}, new double[] {.25}),
		BOW1(EngType.BOW,100000, "Short Bow", new Tech[] { Tech.MILITARY }, new int[] { 3 }, new Engineering[] { INSTRUMENTUM }, new Buffs[] {Buffs.HUNT_B, Buffs.MILITARY_MOD}, new double[] {.25,.25}),
		BOW2(EngType.BOW,300000, "Long Bow", new Tech[] { Tech.MILITARY, Tech.PHYSICS }, new int[] { 3, 3 }, new Engineering[] { BOW1 }, new Buffs[] {Buffs.HUNT_A,Buffs.WARR_A}, new double[] {.5,.5}),//same level of military?
		BOW3(EngType.BOW,300000, "Compound Bow", new Tech[] { Tech.MILITARY, Tech.PHYSICS }, new int[] { 5, 4 }, new Engineering[] { BOW2 }, new Buffs[] {Buffs.WARR_A}, new double[] {.5}),
		BOAT1(EngType.BOAT,300000, "Raft", new Tech[] {Tech.PHYSICS}, new int[] {3},  new Engineering[] {INSTRUMENTUM}, new Buffs[] {Buffs.TRADE_BASE, Buffs.HUNT_B}, new double[] {.25,.25}), 
		BOAT2(EngType.BOAT,3000000, "Trireme", new Tech[] {Tech.MILITARY,Tech.PHYSICS}, new int[] {3,4},  new Engineering[] {BOAT1 }, new Buffs[] {Buffs.TRADE_BASE, Buffs.WARR_B}, new double[] {.5,.5}), 
		FARM1(EngType.BOAT,10000000, "Farming", new Tech[] {Tech.BIO}, new int[] {3},  new Engineering[] { INSTRUMENTUM}, new Buffs[] {Buffs.HUNT_A, Buffs.HUNT_B}, new double[] {1.5}), 
		FARM2(EngType.BOAT,100000000, "Plantation", new Tech[] {Tech.BIO}, new int[] {4},  new Engineering[] {FARM1 }, new Buffs[] {Buffs.HUNT_A, Buffs.HUNT_B}, new double[] {1.5}),
		CTOOLS1(EngType.CTOOLS,30000, "Stone Tools", new Tech[] {Tech.PHYSICS,Tech.CONSTRUCTION}, new int[] {2,2},  new Engineering[] {INSTRUMENTUM }, new Buffs[] {Buffs.CONS_MULT}, new double[] {.5}),
		CTOOLS2(EngType.CTOOLS,500000, "Basic Metal Tools", new Tech[] {Tech.PHYSICS,Tech.CONSTRUCTION}, new int[] {3,3},  new Engineering[] {CTOOLS1 }, new Buffs[] {Buffs.CONS_MULT}, new double[] {.5}),
		CTOOLS3(EngType.CTOOLS,500000, "Advanced Metal Tools", new Tech[] {Tech.PHYSICS,Tech.CONSTRUCTION}, new int[] {5,5},  new Engineering[] {CTOOLS2 }, new Buffs[] {Buffs.CONS_MULT}, new double[] {1}),//Same EP Cost?
		MED1(EngType.MED,3000, "Medicine", new Tech[] {Tech.BIO}, new int[] {1},  new Engineering[] {INSTRUMENTUM }, new Buffs[] {Buffs.MEDI_DISSENT_MOD}, new double[] {-1}),
		MED2(EngType.MED,30000, "Sanitation", new Tech[] {Tech.BIO}, new int[] {2},  new Engineering[] { MED1}, new Buffs[] {Buffs.MEDI_DISSENT_MOD}, new double[] {-2}),
		MED3(EngType.MED,3000000, "Hospitals", new Tech[] {Tech.BIO}, new int[] {4},  new Engineering[] {MED2 }, new Buffs[] {Buffs.MEDI_DISSENT_MOD}, new double[] {-4}),
		TAX1(EngType.TAX,1000000, "Tax Collectors", new Tech[] {Tech.ECON}, new int[] {3},  new Engineering[] {INSTRUMENTUM}, new Buffs[] {Buffs.PRIDE}, new double[] {0}),
		TAX2(EngType.TAX,30000000, "Mail-in Taxes", new Tech[] {Tech.ECON}, new int[] {4},  new Engineering[] { TAX1}, new Buffs[] {Buffs.PRIDE}, new double[] {0}),
		TAX3(EngType.TAX,1000000000, "E-Tax", new Tech[] {Tech.ECON}, new int[] {6},  new Engineering[] { TAX2}, new Buffs[] {Buffs.PRIDE}, new double[] {0}),
		SIEGE1(EngType.SIEGE,500000, "Balista", new Tech[] {Tech.MILITARY,Tech.PHYSICS}, new int[] {3,4},  new Engineering[] { INSTRUMENTUM}, new Buffs[] {Buffs.WARR_B}, new double[] {.25}),
		SIEGE2(EngType.SIEGE,5000000, "Catapult", new Tech[] {Tech.MILITARY,Tech.PHYSICS}, new int[] {3,5},  new Engineering[] { SIEGE1}, new Buffs[] {Buffs.WARR_B}, new double[] {.5}),
		SIEGE3(EngType.SIEGE,10000000, "Trebuchet", new Tech[] {Tech.MILITARY,Tech.PHYSICS}, new int[] {4,6},  new Engineering[] {SIEGE2 }, new Buffs[] {Buffs.WARR_B}, new double[] {.5}),
		SIEGE4(EngType.SIEGE,10000000, "Cannon", new Tech[] {Tech.MILITARY,Tech.PHYSICS}, new int[] {6,7},  new Engineering[] { SIEGE3}, new Buffs[] {Buffs.WARR_B}, new double[] {1}),
		SIEGE5(EngType.SIEGE,10000000, "Howitzer", new Tech[] {Tech.MILITARY,Tech.PHYSICS}, new int[] {7,8},  new Engineering[] {SIEGE4 }, new Buffs[] {Buffs.WARR_B}, new double[] {1}),
		SIEGE6(EngType.SIEGE,10000000, "Rocket artillery", new Tech[] {Tech.MILITARY,Tech.PHYSICS}, new int[] {8,9},  new Engineering[] { SIEGE5}, new Buffs[] {Buffs.WARR_B}, new double[] {1}),
		WRITE1(EngType.WRITE,10000, "Stone", new Tech[] {Tech.COGNITIO}, new int[] {0},  new Engineering[] { INSTRUMENTUM}, new Buffs[] {Buffs.PRIDE}, new double[] {0}),
		WRITE2(EngType.WRITE,1000000, "Parchment", new Tech[] {Tech.COGNITIO}, new int[] {0},  new Engineering[] { WRITE1}, new Buffs[] {Buffs.PRIDE}, new double[] {0}),
		WRITE3(EngType.WRITE,100000000, "Books", new Tech[] {Tech.COGNITIO}, new int[] {0},  new Engineering[] { WRITE2}, new Buffs[] {Buffs.PRIDE}, new double[] {0}),
		DOM1(EngType.DOM,30000, "Livestock", new Tech[] {Tech.BIO}, new int[] {1},  new Engineering[] {INSTRUMENTUM }, new Buffs[] {Buffs.HUNT_B}, new double[] {.25}),
		DOM2(EngType.DOM,1000000, "Horses", new Tech[] {Tech.BIO}, new int[] {2},  new Engineering[] { DOM1}, new Buffs[] {Buffs.WARR_B,Buffs.CONS_MULT}, new double[] {.25,.5}),
		DOM3(EngType.DOM,30000000, "Selective Breeding", new Tech[] {Tech.BIO}, new int[] {3},  new Engineering[] {DOM2 }, new Buffs[] {Buffs.WARR_A, Buffs.CONS_MULT}, new double[] {.25,.5}),
		SWORD1(EngType.SWORD,100000, "Bronze Sword", new Tech[] {Tech.MILITARY}, new int[] {3},  new Engineering[] {INSTRUMENTUM }, new Buffs[] {Buffs.WARR_A}, new double[] {.25}),
		SWORD2(EngType.SWORD,30000000, "Iron Sword", new Tech[] {Tech.MILITARY}, new int[] {4},  new Engineering[] {SWORD1 }, new Buffs[] {Buffs.WARR_A}, new double[] {.25}),
		SWORD3(EngType.SWORD,1000000000, "Steel Sword", new Tech[] {Tech.MILITARY}, new int[] {6},  new Engineering[] {SWORD2 }, new Buffs[] {Buffs.WARR_A}, new double[] {.25}),
		CHARIOT1(EngType.CHARIOT,500000, "Simple Chariot", new Tech[] {Tech.MILITARY, Tech.PHYSICS}, new int[] {3,3},  new Engineering[] { INSTRUMENTUM}, new Buffs[] {Buffs.WARR_B}, new double[] {.25}),
		CHARIOT2(EngType.CHARIOT,5000000, "Reinforced Chariot", new Tech[] {Tech.MILITARY,Tech.PHYSICS}, new int[] {3,4},  new Engineering[] {CHARIOT1 }, new Buffs[] {Buffs.WARR_A}, new double[] {.25}),
		CHARIOT3(EngType.CHARIOT,10000000, "Caravan", new Tech[] {Tech.ECON, Tech.PHYSICS}, new int[] {4,4},  new Engineering[] { CHARIOT2}, new Buffs[] {Buffs.TRADE_BASE}, new double[] {1});
		// @formatter:on

		private double ep;
		private String name;
		private EngType type;
		private Tech[] requiredTech;
		private int[] requiredTechLvl;
		private Engineering[] requiredEng;
		private Buffs[] benefit;
		private double[] benefitVal;

		Engineering(EngType type, double epCost, String name, Tech[] requiredTech, int[] requiredTechLvl,
				Engineering[] reEngineering, Buffs[] benefit, double[] benefitVal) {
			this.ep = epCost;
			this.name = name;
			this.type = type;
			this.requiredTech = requiredTech;
			this.requiredTechLvl = requiredTechLvl;
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

		public Tech[] requiredTech() {
			return this.requiredTech;
		}

		public int[] requiredTechLvl() {
			return requiredTechLvl;
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

	private Government government = Government.TRIBE;
	private Map<Tech, Integer> techLevels = new HashMap<Tech, Integer>();
	private Map<EngType, Engineering> engTechLvls = new HashMap<EngType, Engineering>();

	private double[] products = new double[4];
	private double[] products_upkeep = new double[4];

	private double gold;
	private double gold_income;
	private double gold_upkeep;
	private double food;
	private double food_income;
	private double people;
	private double[] workforce = new double[7];

	private double land_permanent;
	private double land_conquered;

	private double ep;
	private double sp;

	public String message = "hello";

	public boolean setGovernment(Government gov) {
		if (gov == this.government) {
			this.message = String.format("You already use %s as a form of Government", gov.toString());
			return false;
		} else if (this.sp < gov.sp() || this.techLevels.get(Tech.GOVERNANCE).intValue() < gov.governance_required) {
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
		} else if(!checkRequirements(eng, currentEng)){
			return false;
		
		} else {
			this.engTechLvls.put(type, eng);
			this.message = "With these tools we grow ever stronger";
			return true;
		}
		return false;
	}

	private boolean checkRequirements(Engineering eng, Engineering currentEng) {
		Engineering[] engReq = eng.requiredEng();
		Tech[] engTechReq = eng.requiredTech();
		int[] engTechLvl = eng.requiredTechLvl();
		Engineering[] currentEngReq = currentEng.requiredEng();
		Tech[] currentEngTechReq = currentEng.requiredTech();
		int[] currentEngTechLvl = currentEng.requiredTechLvl();
		
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

	public void getWorkforceToMessage() {
		StringBuilder string = new StringBuilder();
		for (Jobs j : Jobs.values()) {
			string.append(
					String.format("There are %d %s in the workforce %n", this.workforce[j.index()], j.toString()));
		}
		this.message = string.toString();
	}
}