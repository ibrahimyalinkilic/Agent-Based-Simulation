
package usecases;

import static stupidmodel.common.TestUtils.TEST_GRID_SIZE;
import static stupidmodel.common.TestUtils.initContext;
import static stupidmodel.common.TestUtils.initGrid;
import static stupidmodel.common.TestUtils.tr;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import repast.simphony.context.Context;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.space.grid.GridPointTranslator;
import repast.simphony.space.grid.StrictBorders;
import repast.simphony.space.grid.WrapAroundBorders;
import stupidmodel.agents.Attack;
import stupidmodel.agents.Security;
import stupidmodel.common.Constants;


public class TestSecurityMovement {

	
	private void testMovementWhenNothingNearbyExperiment(
			final GridPointTranslator borders) {
		final int experimentCount = 9 * Integer.parseInt(Constants.DEFENDER_POWER);

		for (int i = 0; i < experimentCount; ++i) {
			testMovementWhenNothingNearby(borders);
		}
	}

	
	@Test
	public void testMovementWhenNothingNearbyExperimentWithStrictBorders() {
		testMovementWhenNothingNearbyExperiment(new StrictBorders());
	}

	
	@Test
	public void testMovementWhenNothingNearbyExperimentWithWrapAroundBorders() {
		testMovementWhenNothingNearbyExperiment(new WrapAroundBorders());
	}

	
	private void testMovementWhenNothingNearby(final GridPointTranslator borders) {
		
		final Context<Object> context = initContext();
		final Grid<Object> grid = initGrid(borders, context);

		final int bugX = RandomHelper.nextIntFromTo(0, TEST_GRID_SIZE - 1);
		final int bugY = RandomHelper.nextIntFromTo(0, TEST_GRID_SIZE - 1);

		final Security predator = new Security();
		context.add(predator);
		grid.moveTo(predator, bugX, bugY);

		
		predator.hunt();

		
		final GridPoint location = grid.getLocation(predator);

		final ArrayList<GridPoint> expectedLocations = new ArrayList<GridPoint>();
		for (int i = bugX - Integer.parseInt(Constants.DEFENDER_POWER); i <= bugX
				+ Integer.parseInt(Constants.DEFENDER_POWER); ++i) {
			for (int j = bugY - Integer.parseInt(Constants.DEFENDER_POWER); j <= bugY
					+ Integer.parseInt(Constants.DEFENDER_POWER); ++j) {
				final GridPoint act = new GridPoint(tr(i), tr(j));
				expectedLocations.add(act);
			}
		}

		Assert.assertTrue(location + " not in " + expectedLocations,
				expectedLocations.contains(location));
	}

	
	@Test
	public void testMovementWhenBugIsNereby() {
		
		final Context<Object> context = initContext();
		final Grid<Object> grid = initGrid(new StrictBorders(), context);

		final Security predator = new Security();
		context.add(predator);
		grid.moveTo(predator, 5, 5);

		final Attack bug = new Attack();
		context.add(bug);
		grid.moveTo(bug, 4, 4);

		final GridPoint bugLocation = grid.getLocation(bug);

		
		predator.hunt();

		
		final GridPoint newLocation = grid.getLocation(predator);

		
		Assert.assertEquals(bugLocation, newLocation);
		Assert.assertFalse(context.contains(bug));
	}

	
	@Test
	public void testMovementWhenNoEmptyPlaceNearby() {
		
		final Context<Object> context = initContext();
		final Grid<Object> grid = initGrid(new StrictBorders(), context);

		final Security predator = new Security();
		context.add(predator);
		grid.moveTo(predator, 5, 5);

		for (int i = 4; i <= 6; ++i) {
			for (int j = 4; j <= 6; ++j) {
				if (5 == i && 5 == j) {
					continue;
				}

				final Security act = new Security();
				context.add(act);
				grid.moveTo(act, i, j);
			}
		}

		final GridPoint prevLocation = grid.getLocation(predator);

		
		predator.hunt();

		
		final GridPoint newLocation = grid.getLocation(predator);

		
		Assert.assertEquals(prevLocation, newLocation);
	}

	
	@Test
	public void testMovementWhenNothingNereby() {
		
		final Context<Object> context = initContext();
		final Grid<Object> grid = initGrid(new StrictBorders(), context);

		final Security predator = new Security();
		context.add(predator);
		grid.moveTo(predator, 5, 5);

		
		predator.hunt();

		
		final GridPoint newLocation = grid.getLocation(predator);
		final int x = newLocation.getX();
		final int y = newLocation.getY();

		
		Assert.assertTrue(4 <= x && x <= 6 && 4 <= y && y <= 6);
	}

}
