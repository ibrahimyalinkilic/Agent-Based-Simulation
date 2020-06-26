
package stupidmodel.agents;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.engine.environment.RunState;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.collections.IndexedIterable;
import stupidmodel.agents.Attack.AttackSizeComparator;
import stupidmodel.common.CellData;
import stupidmodel.common.Constants;
import stupidmodel.common.TestUtils;


public class TestAttack {

	
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidParameterForSetSize() {
		final Attack Attack = new Attack();
		final double wrongValue = RandomHelper.nextDoubleFromTo(
				-Double.MAX_VALUE, -Double.MIN_VALUE);
		Attack.setSize(wrongValue);
	}

	
	@Test(expected = IllegalArgumentException.class)
	public void testCompareToInvalidFirstParameter() {
		final Attack Attack = new Attack();
		new AttackSizeComparator().compare(null, Attack); // Should fail
	}

	
	@Test(expected = IllegalArgumentException.class)
	public void testCompareToInvalidSecondParameter() {
		final Attack Attack = new Attack();
		new AttackSizeComparator().compare(Attack, null); // Should fail
	}

	
	@Test
	public void testComparisonWithTwoElements() {
		final Attack b1 = new Attack();
		final Attack b2 = new Attack();

		b1.setSize(5.0);
		b2.setSize(10.0);

		final List<Attack> list = Arrays.asList(b1, b2);
		Collections.sort(list, new AttackSizeComparator());

		Assert.assertEquals(b2, list.get(0));
		Assert.assertEquals(b1, list.get(1));
	}

	
	@Test
	public void testComparisonWithThreeElements() {
		final Attack b1 = new Attack();
		final Attack b2 = new Attack();
		final Attack b3 = new Attack();

		b1.setSize(5.0);
		b2.setSize(10.0);
		b3.setSize(2.0);

		final List<Attack> list = Arrays.asList(b1, b2, b3);
		Collections.sort(list, new AttackSizeComparator());

		Assert.assertEquals(b2, list.get(0));
		Assert.assertEquals(b1, list.get(1));
		Assert.assertEquals(b3, list.get(2));
	}

	
	@Test(expected = IllegalArgumentException.class)
	public void testSettingWrongMaxCosnumptionRate() {
		final Attack Attack = new Attack();

		final double wrongValue = RandomHelper.nextDoubleFromTo(
				-Double.MAX_VALUE, -Double.MIN_VALUE);

		Attack.setMaxConsumptionRate(wrongValue); // Should fail
	}

	
	@Test
	public void testSettingMaxCosnumptionRate() {
		final Attack Attack = new Attack();

		final double value = RandomHelper.nextDoubleFromTo(0, Double.MAX_VALUE);
		Attack.setMaxConsumptionRate(value);

		Assert.assertEquals(value, Attack.getMaxConsumptionRate(), Constants.DELTA);
	}

	
	@Test(expected = IllegalArgumentException.class)
	public void testSettingNegativeSurvivalProbability() {
		final Attack Attack = new Attack();

		final double wrongValue = RandomHelper.nextDoubleFromTo(
				-Double.MAX_VALUE, -Double.MIN_VALUE);

		Attack.setSurvivalProbability(wrongValue); // Should fail
	}

	
	@Test(expected = IllegalArgumentException.class)
	public void testSettingAboveOneSurvivalProbability() {
		final Attack Attack = new Attack();

		final double wrongValue = RandomHelper.nextDoubleFromTo(1.0,
				Double.MAX_VALUE);

		Attack.setSurvivalProbability(wrongValue); // Should fail
	}

	
	@Test
	public void testSettingSurvivalProbability() {
		final Attack Attack = new Attack();

		final double value = RandomHelper.nextDoubleFromTo(0, 1.0);
		Attack.setSurvivalProbability(value);

		Assert.assertEquals(value, Attack.getSurvivalProbability(),
				Constants.DELTA);
	}

	
	@Test
	public void testDying() {
		final Attack Attack = new Attack();
		final Context<Object> context = new DefaultContext<Object>();
		RunState.init().setMasterContext(context);
		context.add(Attack);

		Assert.assertTrue(context.contains(Attack));
		Attack.die();
		Assert.assertFalse(context.contains(Attack));
	}

	
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidParameterForSetInitialSize() {
		final Attack Attack = new Attack();
		Attack.setInitialSize(null);
	}

	
	@Test
	public void testGrow() {
		// Given
		final Attack AttackSpy = Mockito.spy(new Attack());

		final double prevSize = RandomHelper.nextDoubleFromTo(0,
				Double.MAX_VALUE);
		AttackSpy.setSize(prevSize);

		final double availableFood = RandomHelper.nextDoubleFromTo(0,
				Double.MAX_VALUE);

		doReturn(availableFood).when(AttackSpy).foodConsumption();

		
		AttackSpy.grow();

	
		Assert.assertEquals(prevSize + availableFood, AttackSpy.getSize(),
				Constants.DELTA);
	}

	
	@Test(expected = IllegalStateException.class)
	public void testGetUnderlyingCellWhenNoCellGiven() {
		final Context<Object> context = TestUtils.initContext();
		final Grid<Object> grid = TestUtils.initEmptyGrid(context);

		final Attack Attack = new Attack();
		context.add(Attack);
		grid.moveTo(Attack, 5, 5);

		Attack.getUnderlyingCell(); // Should fail: No underlying Server
	}

	
	@Test(expected = IllegalStateException.class)
	public void testGetUnderlyingCellWhenMultipleCellGiven() {
		final Context<Object> context = TestUtils.initContext();
		final Grid<Object> grid = TestUtils.initGrid(context);

		final Attack Attack = new Attack();
		context.add(Attack);
		grid.moveTo(Attack, 5, 5);

		final Server secondCell = new Server(new CellData(5, 5, 0.02));
		context.add(secondCell);
		grid.moveTo(secondCell, 5, 5);

		Attack.getUnderlyingCell(); // Should fail: Multiple Servers
	}

	
	@Test
	public void testGetUnderlying() {
	
		final Context<Object> context = TestUtils.initContext();
		final Grid<Object> grid = TestUtils.initGrid(context);

		
		final Server expectedCell = (Server) grid.getObjectAt(5, 5);

		final Attack Attack = new Attack();
		context.add(Attack);
		grid.moveTo(Attack, 5, 5);

		
		final Server actualCell = Attack.getUnderlyingCell();

		
		Assert.assertSame(expectedCell, actualCell);
	}

	
	@Test
	public void testFoodConsumptionWhenCellHasLesserFood() {
		
		final Attack AttackSpy = Mockito.spy(new Attack());

		
		final double food = 0.5;
		final Server underlyingCell = new Server(new CellData(5, 5,
				0.5));
		underlyingCell.setFoodAvailability(food);

		doReturn(underlyingCell).when(AttackSpy).getUnderlyingCell();

		
		final double consumption = AttackSpy.foodConsumption();

		
		Assert.assertEquals(food, consumption, Constants.DELTA);
	}

	
	@Test
	public void testFoodConsumptionWhenCellHasMoreFood() {
	
		final Attack AttackSpy = Mockito.spy(new Attack());

		
		final double food = 1.5;
		final Server underlyingCell = new Server(new CellData(5, 5,
				0.5));
		underlyingCell.setFoodAvailability(food);

		doReturn(underlyingCell).when(AttackSpy).getUnderlyingCell();

		
		final double consumption = AttackSpy.foodConsumption();

	
		Assert.assertEquals(1.0, consumption, Constants.DELTA);
	}

	
	@Test
	public void testMortalityWhenAttackAlwaysSurvives() {
		final Attack AttackSpy = spy(new Attack());
		AttackSpy.setSize(0);
		AttackSpy.setSurvivalProbability(1.0);
		doNothing().when(AttackSpy).die();

		AttackSpy.mortality();

		verify(AttackSpy, never()).die();
	}

	
	@Test
	public void testMortalityWhenAttackDoesNotSurvives() {
		final Attack AttackSpy = spy(new Attack());
		AttackSpy.setSize(0);
		AttackSpy.setSurvivalProbability(0.0);
		doNothing().when(AttackSpy).die();

		AttackSpy.mortality();

		verify(AttackSpy).die();
	}

	
	@Test
	public void testMortalityReproduction() {
		final Attack AttackSpy = spy(new Attack());
		AttackSpy.setSize(10.0);

		doNothing().when(AttackSpy).reproduce();
		doNothing().when(AttackSpy).die();

		AttackSpy.mortality();

		verify(AttackSpy).reproduce();
		verify(AttackSpy).die();
	}

	
	@Test
	public void testReproduceWithEmptyCellsInRange() {
		// Given
		final Context<Object> context = TestUtils.initContext();
		final Grid<Object> grid = TestUtils.initGrid(context);

		final Attack Attack = new Attack();
		Attack.setSize(10);
		context.add(Attack);
		grid.moveTo(Attack, 5, 5);

		// When
		Attack.reproduce();
		Attack.die();

		// Then
		final int Attacks = context.getObjects(Attack.class).size();
		
	}

	
	@Test
	public void testReproduceWithNoEmptyCellsInRange() {
		
		final Context<Object> context = TestUtils.initContext();
		final Grid<Object> grid = TestUtils.initGrid(context);

		final Attack Attack = new Attack();
		Attack.setSize(10);
		context.add(Attack);
		grid.moveTo(Attack, 5, 5);

		final ArrayList<Attack> prevAttacks = new ArrayList<Attack>();

		final int n = Constants.BUG_REPRODUCTION_RANGE;
		for (int i = 5 - n; i <= 5 + n; ++i) {
			for (int j = 5 - n; j <= 5 + n; ++j) {
				if (5 == i && 5 == j) {
					continue;
				}

				final Attack act = new Attack();
				context.add(act);
				grid.moveTo(act, i, j);
				prevAttacks.add(act);
			}
		}

		
		Attack.reproduce();
		Attack.die();

	
		final IndexedIterable<Object> newAttacks = context.getObjects(Attack.class);
		final int Attacks = newAttacks.size();


		Assert.assertEquals(prevAttacks.size(), Attacks);

	
		for (final Object o : newAttacks) {
			Assert.assertTrue(prevAttacks.contains(o));
		}
	}

	
	@Test
	public void testToStringWithoutGrid() {
		final Attack Attack = new Attack();
		Assert.assertNotNull(Attack.toString());
	}

	
	@Test
	public void testToStringWithGrid() {
		final Attack Attack = new Attack();
		final int x = RandomHelper.nextIntFromTo(0, Integer.MAX_VALUE);
		final int y = RandomHelper.nextIntFromTo(0, Integer.MAX_VALUE);

		final Context<Object> context = new DefaultContext<Object>();
		RunState.init().setMasterContext(context);

		@SuppressWarnings("unchecked")
		final Grid<Object> grid = mock(Grid.class);
		when(grid.getName()).thenReturn(Constants.GRID_ID);
		when(grid.getLocation(Attack)).thenReturn(new GridPoint(x, y));

		context.addProjection(grid);

		context.add(Attack);
		Assert.assertNotNull(Attack.toString());
	}

}
