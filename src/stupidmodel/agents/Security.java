
package stupidmodel.agents;

import java.util.List;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameters;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import repast.simphony.util.SimUtilities;
import stupidmodel.common.Constants;
import stupidmodel.common.SMUtils;


public class Security {

	
	public Security() {
		super();
	}

	
	@ScheduledMethod(start = 1, interval = 1, priority = -1)
	public void hunt() {
	
		final Grid<Object> grid = SMUtils.getGrid(this);

		
		final GridPoint location = grid.getLocation(this);

		
		final Parameters parameters = RunEnvironment.getInstance()
				.getParameters();
		final int defenderPower = ((Integer) parameters
				.getValue(Constants.DEFENDER_POWER)).intValue();
		final List<GridCell<Server>> cellNeighborhood = new GridCellNgh<Server>(
				grid, location, Server.class,
				defenderPower,
				defenderPower).getNeighborhood(true);

		SimUtilities.shuffle(cellNeighborhood, RandomHelper.getUniform());

		

		for (final GridCell<Server> cell : cellNeighborhood) {
			if (hasAgent(grid, cell, Attack.class)) {
				killAttackAt(grid, cell);
				moveTo(grid, cell);
				return;
			}
		}

		
		final GridCell<Server> randomCell = SMUtils
				.randomElementOf(cellNeighborhood);

	
		if (hasAgent(grid, randomCell, Security.class)) {
			return;
		}

		moveTo(grid, randomCell);
	}

	
	private <T> boolean hasAgent(final Grid<Object> grid,
			final GridCell<Server> cell, final Class<T> clazz) {
		assert (grid != null);
		assert (cell != null);

		final List<GridCell<T>> neighborhood = new GridCellNgh<T>(grid,
				cell.getPoint(), clazz, 0, 0).getNeighborhood(true);

		assert (neighborhood != null);
		assert (1 == neighborhood.size());

		final int ctr = neighborhood.get(0).size();

		return (ctr > 0);
	}

	
	private void killAttackAt(final Grid<Object> grid,
			final GridCell<Server> cell) {
		assert (grid != null);
		assert (cell != null);
		assert (hasAgent(grid, cell, Attack.class));

		final List<GridCell<Attack>> AttacksAt = new GridCellNgh<Attack>(grid,
				cell.getPoint(), Attack.class, 0, 0).getNeighborhood(true);

		assert (1 == AttacksAt.size());
		AttacksAt.get(0).items().iterator().next().die();
	}

	
	private void moveTo(final Grid<Object> grid,
			final GridCell<Server> cell) {
		assert (grid != null);
		assert (cell != null);

		
		final GridPoint newGridPoint = cell.getPoint();
		grid.moveTo(this, newGridPoint.getX(), newGridPoint.getY());
	}

	
	@Override
	public String toString() {
		
		final String location = (ContextUtils.getContext(this) != null) ? SMUtils
				.getGrid(this).getLocation(this).toString()
				: "[?, ?]";

		
		return String.format("Security @ location %s", location);
	}

}
