
package stupidmodel.agents;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.IllegalParameterException;
import repast.simphony.parameter.Parameter;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import repast.simphony.util.SimUtilities;
import stupidmodel.StupidModelContextBuilder;
import stupidmodel.common.Constants;
import stupidmodel.common.SMUtils;
import cern.jet.random.Normal;
import repast.simphony.parameter.Parameters;


public class Attack {

	
	public static class AttackSizeComparator implements Comparator<Attack>,
			Serializable {

		
		private static final long serialVersionUID = 3468196663094781744L;

		
		@Override
		public int compare(final Attack o1, final Attack o2) {
			if (null == o1) {
				throw new IllegalArgumentException("Parameter o1 == null.");
			}

			if (null == o2) {
				throw new IllegalArgumentException("Parameter o2 == null.");
			}

			return -Double.compare(o1.getSize(), o2.getSize());
		}
	}

	
	private double size = 1.0;

	
	private double maxConsumptionRate = 1.0;

	
	private double survivalProbability = 0.95;

	
	public Attack() {
		super();
	}

	
	public double getSize() {
		return size;
	}

	
	public void setSize(final double size) {
		if (size < 0) {
			throw new IllegalArgumentException(String.format(
					"Parameter size = %f < 0.", size));
		}

		this.size = size;
	}

	
	@Parameter(displayName = "Attack maximum server consumption rate", usageName = "maxConsumptionRate")
	public double getMaxConsumptionRate() {
		return maxConsumptionRate;
	}

	
	public void setMaxConsumptionRate(final double maxConsumptionRate) {
		if (maxConsumptionRate < 0) {
			throw new IllegalArgumentException(String.format(
					"Parameter maxConsumptionRate = %f < 0.",
					maxConsumptionRate));
		}

		this.maxConsumptionRate = maxConsumptionRate;
	}

	
	@Parameter(displayName = "Attack survival probability", usageName = "survivalProbability")
	public double getSurvivalProbability() {
		return survivalProbability;
	}

	
	public void setSurvivalProbability(final double survivalProbability) {
		if (survivalProbability < 0.0 || 1.0 < survivalProbability) {
			throw new IllegalArgumentException(
					String.format(
							"Parameter survivalProbability=%f should be in interval [0, 1].",
							survivalProbability));
		}

		this.survivalProbability = survivalProbability;
	}

	
	public void step() {
		
		final Grid<Object> grid = SMUtils.getGrid(this);
		
		final GridPoint location = grid.getLocation(this);

		
		final Parameters parameters = RunEnvironment.getInstance()
				.getParameters();
		final int attackStrength = ((Integer) parameters
				.getValue(Constants.ATTACK_STRENGTH)).intValue();
		final List<GridCell<Attack>> AttackNeighborhood = new GridCellNgh<Attack>(grid,
				location, Attack.class, attackStrength,
				attackStrength).getNeighborhood(false);

		
		final List<GridCell<Attack>> freeCells = SMUtils
				.getFreeGridCells(AttackNeighborhood);

		
		if (freeCells.isEmpty()) {
			return;
		}

		

		SimUtilities.shuffle(freeCells, RandomHelper.getUniform());

		final List<GridCell<Server>> Servers = getServersForLocations(freeCells);
		assert (Servers.size() == freeCells.size());

		Collections.sort(Servers,
				Server.HABITAT_CELL_FOOD_AVAILABILITY_COMPARATOR);

		
		final GridCell<Server> chosenFreeCell = Servers.get(0);

		
		final GridPoint newGridPoint = chosenFreeCell.getPoint();
		grid.moveTo(this, newGridPoint.getX(), newGridPoint.getY());
	}

	
	private List<GridCell<Server>> getServersForLocations(
			final List<GridCell<Attack>> freeCells) {
		
		assert (freeCells.equals(SMUtils.getFreeGridCells(freeCells)));

		final ArrayList<GridCell<Server>> ret = new ArrayList<GridCell<Server>>();
		final Grid<Object> grid = SMUtils.getGrid(this);

		
		for (final GridCell<Attack> gridCell : freeCells) {
			final GridPoint point = gridCell.getPoint();

			
			final List<GridCell<Server>> cells = new GridCellNgh<Server>(
					grid, point, Server.class, 0, 0).getNeighborhood(true);

			
			assert (1 == cells.size());
			ret.add(cells.get(0));
		}

		return ret;
	}

	
	public void grow() {
		size += foodConsumption();

		
	}

	
	protected double foodConsumption() {
		final Server cell = getUnderlyingCell();
		final double foodAvailable = cell.getFoodAvailability();

		final double eatenFood = Math.min(maxConsumptionRate, foodAvailable);
		cell.foodConsumed(eatenFood);

		
		assert (eatenFood >= 0);
		assert (eatenFood <= maxConsumptionRate);
		assert (eatenFood <= foodAvailable);

		return eatenFood;
	}

	
	protected Server getUnderlyingCell() {
		final Grid<Object> grid = SMUtils.getGrid(this);
		final GridPoint location = grid.getLocation(this);
		final Iterable<Object> objects = grid.getObjectsAt(location.getX(),
				location.getY());

		Server ret = null;

		for (final Object object : objects) {
			if (object instanceof Server) {
				final Server cell = (Server) object;
				if (ret != null) {
					throw new IllegalStateException(
							String.format(
									"Multiple cells defined for the same position;cell 1=%s, cell 2=%s",
									ret, cell));
				}

				ret = cell;
			}
		}

		if (null == ret) {
			throw new IllegalStateException(String.format(
					"Cannot find any cells for location %s", location));
		}

		return ret;
	}

	
	public void mortality() {
		final Parameters parameters = RunEnvironment.getInstance()
				.getParameters();
		final int timeBetweenAttacks = ((Integer) parameters
				.getValue(Constants.MAX_BUG_SIZE)).intValue();
		
		if (size >= timeBetweenAttacks) {
			reproduce();
			die();
			return;
		}

		
		if (SMUtils.prob(survivalProbability)) {
			die();
		}
	}

	
	protected void reproduce() {
		
		final Parameters parameters = RunEnvironment.getInstance()
				.getParameters();
		final int timeBetweenAttacks = ((Integer) parameters
				.getValue(Constants.MAX_BUG_SIZE)).intValue();
		assert (size >= timeBetweenAttacks);

		
		@SuppressWarnings("unchecked")
		final Context<Object> context = (Context<Object>) ContextUtils
				.getContext(this);

		final Grid<Object> grid = SMUtils.getGrid(this);
		final GridPoint location = grid.getLocation(this);

		
		final int attackFreq = ((Integer) parameters
				.getValue(Constants.ATTACK_FREQUENCY)).intValue();
		if (attackFreq > 10) {
			throw new IllegalParameterException("Parameter attack Frequency = "
					+ attackFreq + " must be less than 11");
		}
		for (int i = 0; i < attackFreq; ++i) {
			
			final Attack child = new Attack();
			child.setSize(0.0);

			
			final List<GridCell<Attack>> AttackNeighborhood = new GridCellNgh<Attack>(
					grid, location, Attack.class,
					Constants.BUG_REPRODUCTION_RANGE,
					Constants.BUG_REPRODUCTION_RANGE).getNeighborhood(false);

			
			final List<GridCell<Attack>> freeCells = SMUtils
					.getFreeGridCells(AttackNeighborhood);

			
			if (freeCells.isEmpty()) {
				break;
			}

			
			final GridCell<Attack> chosenFreeCell = SMUtils
					.randomElementOf(freeCells);

		
			context.add(child);

		
			final GridPoint newGridPoint = chosenFreeCell.getPoint();
			grid.moveTo(child, newGridPoint.getX(), newGridPoint.getY());
		}
	}

	
	public void die() {
		ContextUtils.getContext(this).remove(this);
	}

	
	public void setInitialSize(final Normal normal) {
		if (null == normal) {
			throw new IllegalArgumentException("Parameter normal == null.");
		}

		
		final double initialSize = Math.max(normal.nextDouble(), 0.0);
		setSize(initialSize);
	}

	
	@Override
	public String toString() {
		
		final String location = (ContextUtils.getContext(this) != null) ? SMUtils
				.getGrid(this).getLocation(this).toString()
				: "[?, ?]";

		
		return String.format("Attack @ location %s, size=%f", location, size);
	}

}
