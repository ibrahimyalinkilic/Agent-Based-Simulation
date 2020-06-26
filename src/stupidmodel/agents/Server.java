
package stupidmodel.agents;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameter;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.util.ContextUtils;
import repast.simphony.valueLayer.GridValueLayer;
import stupidmodel.StupidModelContextBuilder;
import stupidmodel.common.CellData;
import stupidmodel.common.Constants;


public class Server {

	
	public static final ServerFoodAvailabilityComparator HABITAT_CELL_FOOD_AVAILABILITY_COMPARATOR = new ServerFoodAvailabilityComparator();

	
	private final static class ServerFoodAvailabilityComparator implements
			Comparator<GridCell<Server>>, Serializable {

		
		private static final long serialVersionUID = 3468196663094781744L;

		
		@Override
		public int compare(final GridCell<Server> gc1,
				final GridCell<Server> gc2) {
			// Make sure if exactly 1 Server exists
			assert (hasOneCell(gc1));
			assert (hasOneCell(gc2));

			final Server cell = gc1.items().iterator().next();
			final Server bell = gc2.items().iterator().next();
			return -Double
					.compare(cell.foodAvailability, bell.foodAvailability);
		}

		
		private boolean hasOneCell(final GridCell<Server> gc) {
			final ArrayList<Server> list = new ArrayList<Server>();
			for (final Server cell : gc.items()) {
				list.add(cell);
			}

			return (1 == list.size());
		}
	}

	
	protected double foodProductionRate = 0.1;

	
	protected double foodAvailability = 0.0;

	
	protected final int x, y;

	
	public Server(final CellData cellData) {
		if (null == cellData) {
			throw new IllegalArgumentException("Parameter cellData == null.");
		}

		this.x = cellData.getX();
		this.y = cellData.getY();
		this.foodProductionRate = cellData.getFoodProductionRate();
	}

	
	@Parameter(displayName = "Cell maximum server production rate", usageName = "foodProductionRate")
	public double getFoodProductionRate() {
		return foodProductionRate;
	}

	
	public void setFoodProductionRate(final double foodProductionRate) {
		if (foodProductionRate < 0) {
			throw new IllegalArgumentException(String.format(
					"Parameter serverProductionRate == %f < 0.",
					foodProductionRate));
		}

		this.foodProductionRate = foodProductionRate;
	}

	
	public double getFoodAvailability() {
		return foodAvailability;
	}

	
	public void setFoodAvailability(final double foodAvailability) {
		if (foodAvailability < 0) {
			throw new IllegalArgumentException(String.format(
					"Parameter serverAvailability == %f < 0.", foodAvailability));
		}

		this.foodAvailability = foodAvailability;
	}

	
	@ScheduledMethod(start = 1, interval = 1, priority = -2)
	public void growFood() {
		foodAvailability += foodProductionRate;

		final GridValueLayer foodValueLayer = (GridValueLayer) ContextUtils
				.getContext(this).getValueLayer(Constants.FOOD_VALUE_LAYER_ID);

		if (null == foodValueLayer) {
			throw new IllegalStateException(
					"Cannot locate server value layer with ID="
							+ Constants.FOOD_VALUE_LAYER_ID + ".");
		}

		foodValueLayer.set(getFoodAvailability(), x, y);
	}

	
	public void foodConsumed(final double eatenFood) {
		if (eatenFood < 0.0) {
			throw new IllegalArgumentException(String.format(
					"eatenFood == %f < 0.0", eatenFood));
		}

		if (eatenFood > foodAvailability) {
			throw new IllegalArgumentException(String.format(
					"eatenFood == %f > foodAvailability == %f", eatenFood,
					foodAvailability));
		}

		foodAvailability -= eatenFood;
	}

	
	@Override
	public String toString() {
		
		return String.format(
				"Server @ location (%d, %d), foodAvailability=%f", x, y,
				foodAvailability);
	}

}
