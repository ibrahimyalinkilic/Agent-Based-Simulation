
package stupidmodel.common;

import stupidmodel.StupidModelContextBuilder;
import stupidmodel.agents.Attack;
import stupidmodel.agents.Security;


public final class Constants {

	
	public static final String EOL = System.getProperty("line.separator");

	
	public static final String CONTEXT_ID = "StupidModel";


	public static final String SPACE_ID = "space";

	
	public static final String GRID_ID = "grid";

	
	public static final String PARAMETER_ID_BUG_COUNT = "bugCount";

	
	public static final double PARAMETER_INITIAL_BUG_SIZE_MEAN = 0.1;

	
	public static final double PARAMETER_INITIAL_BUG_SIZE_SD = 0.03;

	
	public static final String ATTACK_STRENGTH = "attack_strength";

	
	public static final String ATTACK_FREQUENCY = "attackFrequency";

	
	public static final int BUG_REPRODUCTION_RANGE = 3;

	
	public static final String FOOD_VALUE_LAYER_ID = "foodValueLayer";

	
	public static final float GUI_CELL_SIZE = 15.0f;

	
	public static final double DELTA = 1e-6;

	
	public static final double DEFAULT_END_AT = 1000.0;

	
	public static final String STUPID_CELL_DATA_FILE = "Stupid_Cell.data";

	
	public static final int CELL_DATA_FILE_HEADER_LINES = 3;

	
	public static final String DEFENDER_COUNT = "defender_count";

	
	public static final String DEFENDER_POWER = "defender_power";

	
	public static final String MAX_BUG_SIZE = "time_between_attacks";

	
	private Constants() {
		;
	}

}
