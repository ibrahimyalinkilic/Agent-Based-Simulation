
package stupidmodel.common;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.engine.environment.RunState;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.GridPointTranslator;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.StrictBorders;
import stupidmodel.agents.Server;


public class TestUtils {

	
	public static final int TEST_GRID_SIZE = 10;

	
	public static int tr(int i) {
		if (i < 0) {
			return i += TEST_GRID_SIZE;
		}

		if (TEST_GRID_SIZE <= i) {
			return i -= TEST_GRID_SIZE;
		}

		return i;
	}

	
	public static Context<Object> initContext() {
		final Context<Object> context = new DefaultContext<Object>();
		RunState.init().setMasterContext(context);
		return context;
	}

	
	public static Grid<Object> initEmptyGrid(final Context<Object> context) {
		return initEmptyGrid(new StrictBorders(), context);
	}

	
	public static Grid<Object> initEmptyGrid(final GridPointTranslator borders,
			final Context<Object> context) {
		final Grid<Object> grid = GridFactoryFinder.createGridFactory(null)
				.createGrid(
						Constants.GRID_ID,
						context,
						new GridBuilderParameters<Object>(borders,
								new SimpleGridAdder<Object>(), true,
								TEST_GRID_SIZE, TEST_GRID_SIZE));
		return grid;
	}

	
	public static Grid<Object> initGrid(final Context<Object> context) {
		return initGrid(new StrictBorders(), context);
	}

	
	public static Grid<Object> initGrid(final GridPointTranslator borders,
			final Context<Object> context) {
		final Grid<Object> grid = initEmptyGrid(borders, context);

		for (int i = 0; i < TEST_GRID_SIZE; ++i) {
			for (int j = 0; j < TEST_GRID_SIZE; ++j) {
				final Server cell = new Server(new CellData(i, j,
						RandomHelper.nextDouble()));

				context.add(cell);
				grid.moveTo(cell, i, j);
			}
		}

		return grid;
	}
}
