
package stupidmodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.IllegalParameterException;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.continuous.RandomCartesianAdder;
import repast.simphony.space.continuous.StrictBorders;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.WrapAroundBorders;
import repast.simphony.valueLayer.GridValueLayer;
import stupidmodel.agents.Attack;
import stupidmodel.agents.Attack.AttackSizeComparator;
import stupidmodel.agents.Server;
import stupidmodel.agents.Security;
import stupidmodel.common.CellData;
import stupidmodel.common.Constants;
import stupidmodel.common.SMUtils;
import cern.jet.random.Normal;


public class StupidModelContextBuilder extends DefaultContext<Object> implements
		ContextBuilder<Object> {

	
	@Override
	public Context<Object> build(final Context<Object> context) {
		assert (context != null);

	
		context.setId(Constants.CONTEXT_ID);

	

		final List<CellData> cellData = SMUtils
				.readDataFile(Constants.STUPID_CELL_DATA_FILE);
		
		

		final int gridSizeX = Collections.max(cellData,
				CellData.CELL_DATA_X_COMPARATOR).getX() + 1;

		final int gridSizeY = Collections.max(cellData,
				CellData.CELL_DATA_Y_COMPARATOR).getY() + 1;

	

		final ContinuousSpace<Object> space = ContinuousSpaceFactoryFinder
				.createContinuousSpaceFactory(null) 
				.createContinuousSpace(Constants.SPACE_ID, context,
						new RandomCartesianAdder<Object>(),
						
						new StrictBorders(), gridSizeX, gridSizeY);

		
		final Grid<Object> grid = GridFactoryFinder.createGridFactory(null)
				.createGrid(Constants.GRID_ID, context,
						new GridBuilderParameters<Object>(
					
								new repast.simphony.space.grid.StrictBorders(),
								
								new SimpleGridAdder<Object>(),
								
								true,
								
								gridSizeX, gridSizeY));

		
		final GridValueLayer foodValueLayer = new GridValueLayer(
				"foodValueLayer",
				true, 	
				new WrapAroundBorders(), 	
				gridSizeX, gridSizeY);

		context.addValueLayer(foodValueLayer);

		createattacks(context, space, grid);
		createsecuritys(context, space, grid); 
		createservers(context, cellData, grid, foodValueLayer);

		RunEnvironment.getInstance().endAt(Constants.DEFAULT_END_AT);
		
		return context;
		
	}

	
	private void createattacks(final Context<Object> context,
			final ContinuousSpace<Object> space, final Grid<Object> grid) {
		assert (context != null);
		assert (space != null);
		assert (grid != null);

		final Parameters parameters = RunEnvironment.getInstance()
				.getParameters();

	
		final int attackCount = ((Integer) parameters
				.getValue(Constants.PARAMETER_ID_BUG_COUNT)).intValue();

		if (attackCount < 0) {
			throw new IllegalParameterException("Parameter attackCount = "
					+ attackCount + " must be non-negative");
		}

		
		final double initialattackSizeMean = Constants.PARAMETER_INITIAL_BUG_SIZE_MEAN;
				

		final double initialattackSizeSD = Constants.PARAMETER_INITIAL_BUG_SIZE_SD;
				

		final Normal normal = RandomHelper.createNormal(initialattackSizeMean,
				initialattackSizeSD);

		
		for (int i = 0; i < attackCount; ++i) {
			final Attack attack = new Attack();
			attack.setInitialSize(normal);

			context.add(attack);
			final NdPoint pt = space.getLocation(attack);
			grid.moveTo(attack, (int) pt.getX(), (int) pt.getY());
		}
	}

	
	private void createsecuritys(final Context<Object> context,
			final ContinuousSpace<Object> space, final Grid<Object> grid) {
		assert (context != null);
		assert (space != null);
		assert (grid != null);

		final Parameters parameters = RunEnvironment.getInstance()
				.getParameters();
		final int defenderCount = ((Integer) parameters
				.getValue(Constants.DEFENDER_COUNT)).intValue();
		for (int i = 0; i < defenderCount; ++i) {
			final Security security = new Security();

			context.add(security);
			final NdPoint pt = space.getLocation(security);
			grid.moveTo(security, (int) pt.getX(), (int) pt.getY());
		}
	}

	
	private void createservers(final Context<Object> context,
			final List<CellData> cellData, final Grid<Object> grid,
			final GridValueLayer foodValueLayer) {
		assert (context != null);
		assert (cellData != null);
		assert (grid != null);
		assert (foodValueLayer != null);

		for (final CellData act : cellData) {

			final Server cell = new Server(act);
			context.add(cell); // First add it to the context
			grid.moveTo(cell, act.getX(), act.getY());
			foodValueLayer.set(cell.getFoodAvailability(), act.getX(),
					act.getY());
		}
	}

	
	@ScheduledMethod(start = 1, interval = 1, priority = 0)
	public void activateAgents() {
		final ArrayList<Attack> attackList = getAttackList();
		
		

	
		Collections.sort(attackList, new AttackSizeComparator());

		for (final Attack attack : attackList) {
			attack.step();
		}

		for (final Attack attack : attackList) {
			attack.grow();
		}

	
		for (final Attack attack : attackList) {
			attack.mortality();
		}

		
		if (0 == getAttackList().size()) {
			System.out.println("All agents dead, terminating simulation.");
			System.out.println("All attacks have been averted. Security System works great!");
			RunEnvironment.getInstance().endRun();
		}
	}

	
	protected ArrayList<Attack> getAttackList() {
		@SuppressWarnings("unchecked")
		final Iterable<Attack> attacks = RunState.getInstance().getMasterContext()
				.getObjects(Attack.class);
		final ArrayList<Attack> attackList = new ArrayList<Attack>();

		for (final Attack attack : attacks) {
			attackList.add(attack);
		}

		return attackList;
	}

	
	public int attackCount() {
		final RunState runState = RunState.getInstance();

		
		if (null == runState) {
			return 0;
		}

		@SuppressWarnings("unchecked")
		final Context<Object> masterContext = runState.getMasterContext();

		
		if (null == masterContext) {
			return 0;
		}

		return masterContext.getObjects(Attack.class).size();
	}

}
