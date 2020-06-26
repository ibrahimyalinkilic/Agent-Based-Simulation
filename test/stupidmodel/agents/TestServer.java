
package stupidmodel.agents;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;

import junit.framework.Assert;

import org.junit.Test;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.engine.environment.RunState;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.TestGridCellFactory;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.valueLayer.GridValueLayer;
import stupidmodel.common.CellData;
import stupidmodel.common.Constants;


public class TestServer {

	
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidCreation() {
		new Server(null); 
	}

	
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidCoordinateX() {
		final int x = RandomHelper.nextIntFromTo(Integer.MIN_VALUE, -1);
		final int y = RandomHelper.nextIntFromTo(0, Integer.MAX_VALUE);

		new Server(new CellData(x, y, 1.0));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidCoordinateY() {
		final int x = RandomHelper.nextIntFromTo(0, Integer.MAX_VALUE);
		final int y = RandomHelper.nextIntFromTo(Integer.MIN_VALUE, -1);

		new Server(new CellData(x, y, 1.0));
	}

	
	@Test
	public void testServerCreationCorrectX() {
		final int x = RandomHelper.nextIntFromTo(0, Integer.MAX_VALUE);
		final int y = RandomHelper.nextIntFromTo(0, Integer.MAX_VALUE);

		final Server cell = new Server(new CellData(x, y, 1.0));
		Assert.assertEquals(x, cell.x);
	}

	
	@Test
	public void testServerCreationCorrectY() {
		final int x = RandomHelper.nextIntFromTo(0, Integer.MAX_VALUE);
		final int y = RandomHelper.nextIntFromTo(0, Integer.MAX_VALUE);

		final Server cell = new Server(new CellData(x, y, 1.0));
		Assert.assertEquals(y, cell.y);
	}

	
	@Test
	public void testDefaultFoodProductionRate() {
		final int x = RandomHelper.nextIntFromTo(0, Integer.MAX_VALUE);
		final int y = RandomHelper.nextIntFromTo(0, Integer.MAX_VALUE);

		final Server cell = new Server(new CellData(x, y, 0.01));
		Assert.assertEquals(0.01, cell.foodProductionRate, Constants.DELTA);
	}

	
	@Test(expected = IllegalArgumentException.class)
	public void testSetDefaultFoodProductionRateFailure() {
		final int x = RandomHelper.nextIntFromTo(0, Integer.MAX_VALUE);
		final int y = RandomHelper.nextIntFromTo(0, Integer.MAX_VALUE);
		final double wrongValue = RandomHelper.nextDoubleFromTo(
				-Double.MAX_VALUE, -Double.MIN_VALUE);

		new Server(new CellData(x, y, wrongValue));
	}

	
	@Test
	public void testSetDefaultFoodProductionRateWorks() {
		final int x = RandomHelper.nextIntFromTo(0, Integer.MAX_VALUE);
		final int y = RandomHelper.nextIntFromTo(0, Integer.MAX_VALUE);
		final double value = RandomHelper.nextDoubleFromTo(0.0,
				Double.MAX_VALUE);

		final Server cell = new Server(new CellData(x, y, value));
		Assert.assertEquals(value, cell.getFoodProductionRate(),
				Constants.DELTA);
	}

	
	@Test
	public void testDefaultGetFoodAvailability() {
		final int x = RandomHelper.nextIntFromTo(0, Integer.MAX_VALUE);
		final int y = RandomHelper.nextIntFromTo(0, Integer.MAX_VALUE);

		final Server cell = new Server(new CellData(x, y, 0.0));

		Assert.assertEquals(0.0, cell.getFoodAvailability(), Constants.DELTA);
	}

	
	@Test(expected = IllegalArgumentException.class)
	public void testSetFoodAvailabilityFailure() {
		final int x = RandomHelper.nextIntFromTo(0, Integer.MAX_VALUE);
		final int y = RandomHelper.nextIntFromTo(0, Integer.MAX_VALUE);

		final Server cell = new Server(new CellData(x, y, 1.0));

		final double wrongValue = RandomHelper.nextDoubleFromTo(
				-Double.MAX_VALUE, -Double.MIN_VALUE);

		cell.setFoodAvailability(wrongValue);
	}

	
	@Test
	public void testSetFoodAvailability() {
		final int x = RandomHelper.nextIntFromTo(0, Integer.MAX_VALUE);
		final int y = RandomHelper.nextIntFromTo(0, Integer.MAX_VALUE);

		final Server cell = new Server(new CellData(x, y, 1.0));

		final double value = RandomHelper.nextDoubleFromTo(0.0,
				Double.MAX_VALUE);

		cell.setFoodAvailability(value);
		Assert.assertEquals(value, cell.getFoodAvailability(), Constants.DELTA);
	}

	
	@Test(expected = IllegalArgumentException.class)
	public void testFoodProductionRateFailure() {
		final int x = RandomHelper.nextIntFromTo(0, Integer.MAX_VALUE);
		final int y = RandomHelper.nextIntFromTo(0, Integer.MAX_VALUE);

		final Server cell = new Server(new CellData(x, y, 1.0));

		final double wrongValue = RandomHelper.nextDoubleFromTo(
				-Double.MAX_VALUE, -Double.MIN_VALUE);

		cell.setFoodProductionRate(wrongValue);
	}

	
	@Test
	public void testSetFoodProductionRate() {
		final int x = RandomHelper.nextIntFromTo(0, Integer.MAX_VALUE);
		final int y = RandomHelper.nextIntFromTo(0, Integer.MAX_VALUE);

		final Server cell = new Server(new CellData(x, y, 1.0));

		final double value = RandomHelper.nextDoubleFromTo(0.0,
				Double.MAX_VALUE);

		cell.setFoodProductionRate(value);
		Assert.assertEquals(value, cell.getFoodProductionRate(),
				Constants.DELTA);
	}

	
	@Test(expected = IllegalStateException.class)
	public void testDefaultGrowFoodWithoutProperGridValueLayer() {
		final int x = RandomHelper.nextIntFromTo(0, Integer.MAX_VALUE);
		final int y = RandomHelper.nextIntFromTo(0, Integer.MAX_VALUE);

		final Server cell = new Server(new CellData(x, y, 1.0));

		{ 
			final Context<Object> context = new DefaultContext<Object>();

			context.add(cell);
			RunState.init().setMasterContext(context);
		}

		cell.growFood();
	}

	
	@Test
	public void testDefaultGrowFood() {
		final int x = RandomHelper.nextIntFromTo(0, Integer.MAX_VALUE);
		final int y = RandomHelper.nextIntFromTo(0, Integer.MAX_VALUE);

		final double value = RandomHelper.nextDoubleFromTo(0, Double.MAX_VALUE);
		final Server cell = new Server(new CellData(x, y, value));

		final GridValueLayer valueLayer = mock(GridValueLayer.class);
		when(valueLayer.getName()).thenReturn(Constants.FOOD_VALUE_LAYER_ID);

		{
			final Context<Object> context = new DefaultContext<Object>();

			context.add(cell);
			context.addValueLayer(valueLayer);
			RunState.init().setMasterContext(context);
		}

		final double prevFood = cell.getFoodAvailability();

		cell.growFood();

		Assert.assertEquals(prevFood + cell.getFoodProductionRate(),
				cell.getFoodAvailability(), Constants.DELTA);
	}

	
	@Test(expected = IllegalArgumentException.class)
	public void testFoodConsumedFailure() {
		final int x = RandomHelper.nextIntFromTo(0, Integer.MAX_VALUE);
		final int y = RandomHelper.nextIntFromTo(0, Integer.MAX_VALUE);

		final Server cell = new Server(new CellData(x, y, 1.0));
		final double wrongValue = RandomHelper.nextDoubleFromTo(
				-Double.MAX_VALUE, -Double.MIN_VALUE);
		cell.foodConsumed(wrongValue);
	}

	
	@Test(expected = IllegalArgumentException.class)
	public void testMoreThanAvailableFoodConsumed() {
		final int x = RandomHelper.nextIntFromTo(0, Integer.MAX_VALUE);
		final int y = RandomHelper.nextIntFromTo(0, Integer.MAX_VALUE);

		final Server cell = new Server(new CellData(x, y, 1.0));

		final double wrongValue = RandomHelper.nextDoubleFromTo(
				cell.getFoodAvailability(), Double.MAX_VALUE);
		cell.foodConsumed(wrongValue);
	}

	
	@Test
	public void testFoodConsumed() {
		final int x = RandomHelper.nextIntFromTo(0, Integer.MAX_VALUE);
		final int y = RandomHelper.nextIntFromTo(0, Integer.MAX_VALUE);

		final Server cell = new Server(new CellData(x, y, 1.0));

		final double food = RandomHelper
				.nextDoubleFromTo(0.0, Double.MAX_VALUE);

		final double consumed = RandomHelper.nextDoubleFromTo(0.0, food);

		cell.setFoodAvailability(food);
		cell.foodConsumed(consumed);

		Assert.assertEquals(food - consumed, cell.getFoodAvailability(),
				Constants.DELTA);
	}

	
	@Test
	public void testToString() {
		final int x = RandomHelper.nextIntFromTo(0, Integer.MAX_VALUE);
		final int y = RandomHelper.nextIntFromTo(0, Integer.MAX_VALUE);

		final Server cell = new Server(new CellData(x, y, 1.0));
		Assert.assertNotNull(cell.toString());
	}

	
	@Test
	public void testComparatorBasicFunctionality() {
		final ArrayList<GridCell<Server>> list = new ArrayList<GridCell<Server>>();

		final Server cell = new Server(new CellData(0, 0, 1.0));
		cell.setFoodAvailability(10);
		final GridCell<Server> gc1 = TestGridCellFactory
				.createGridCellToTest(new GridPoint(0, 0), Server.class,
						cell);
		list.add(gc1);

		final Server bell = new Server(new CellData(1, 1, 1.0));
		bell.setFoodAvailability(20);
		final GridCell<Server> gc2 = TestGridCellFactory
				.createGridCellToTest(new GridPoint(1, 1), Server.class,
						bell);
		list.add(gc2);

		Collections.sort(list,
				Server.HABITAT_CELL_FOOD_AVAILABILITY_COMPARATOR);
		Assert.assertEquals(gc2, list.get(0));
		Assert.assertEquals(gc1, list.get(1));
	}

	
	@Test
	public void testComparator() {
		final ArrayList<GridCell<Server>> list = new ArrayList<GridCell<Server>>();

		final Server cell = new Server(new CellData(0, 0, 1.0));
		cell.setFoodAvailability(15);
		final GridCell<Server> gc1 = TestGridCellFactory
				.createGridCellToTest(new GridPoint(0, 0), Server.class,
						cell);
		list.add(gc1);

		final Server bell = new Server(new CellData(1, 1, 1.0));
		bell.setFoodAvailability(10);
		final GridCell<Server> gc2 = TestGridCellFactory
				.createGridCellToTest(new GridPoint(1, 1), Server.class,
						bell);
		list.add(gc2);

		final Server dell = new Server(new CellData(2, 2, 1.0));
		dell.setFoodAvailability(20);
		final GridCell<Server> gc3 = TestGridCellFactory
				.createGridCellToTest(new GridPoint(2, 2), Server.class,
						dell);
		list.add(gc3);

		Collections.sort(list,
				Server.HABITAT_CELL_FOOD_AVAILABILITY_COMPARATOR);
		Assert.assertEquals(gc3, list.get(0));
		Assert.assertEquals(gc1, list.get(1));
		Assert.assertEquals(gc2, list.get(2));
	}

}
