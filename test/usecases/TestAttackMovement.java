
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
import stupidmodel.common.Constants;
import stupidmodel.common.SMUtils;


public class TestAttackMovement {

	
	private void testMovementWhenNothingNearbyExperiment(
			final GridPointTranslator borders) {
		final int experimentCount = Integer.parseInt(Constants.ATTACK_STRENGTH)
				*Integer.parseInt(Constants.ATTACK_STRENGTH);

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
		// Given
		final Context<Object> context = initContext();
		final Grid<Object> grid = initGrid(borders, context);

		final int bugX = RandomHelper.nextIntFromTo(0, TEST_GRID_SIZE - 1);
		final int bugY = RandomHelper.nextIntFromTo(0, TEST_GRID_SIZE - 1);

		final Attack bug = new Attack();
		context.add(bug);
		grid.moveTo(bug, bugX, bugY);

		// When
		bug.step();

		// Then
		final GridPoint location = grid.getLocation(bug);

		final ArrayList<GridPoint> expectedLocations = new ArrayList<GridPoint>();
		for (int i = bugX - Integer.parseInt(Constants.ATTACK_STRENGTH); i <= bugX
				+ Integer.parseInt(Constants.ATTACK_STRENGTH); ++i) {
			for (int j = bugY -Integer.parseInt(Constants.ATTACK_STRENGTH); j <= bugY
					+ Integer.parseInt(Constants.ATTACK_STRENGTH); ++j) {
				// Skip original location (since there are plenty of locations
				// to move at)
				if (bugX == i && bugY == j) {
					continue;
				}

				final GridPoint act = new GridPoint(tr(i), tr(j));
				expectedLocations.add(act);
			}
		}

		Assert.assertTrue(location + " not in " + expectedLocations,
				expectedLocations.contains(location));
	}

	
	private void testMovementWithNoFreeCellsWithinRangeExperiment(
			final GridPointTranslator borders) {
		final int experimentCount =Integer.parseInt(Constants.ATTACK_STRENGTH)
				* Integer.parseInt(Constants.ATTACK_STRENGTH);

		for (int i = 0; i < experimentCount; ++i) {
			testMovementWithNoFreeCellsWithinRange(borders);
		}
	}

	
	@Test
	public void testMovementWithNoFreeCellsWithinRangeExperimentWithStrictBorders() {
		testMovementWithNoFreeCellsWithinRangeExperiment(new StrictBorders());
	}

	@Test
	public void testMovementWithNoFreeCellsWithinRangeExperimentWithWrapAroundBorders() {
		testMovementWithNoFreeCellsWithinRangeExperiment(new WrapAroundBorders());
	}

	
	private void testMovementWithNoFreeCellsWithinRange(
			final GridPointTranslator borders) {
		// Given
		final Context<Object> context = initContext();
		final Grid<Object> grid = initGrid(borders, context);

		final ArrayList<Attack> bugs = new ArrayList<Attack>();

		for (int i = 0; i < TEST_GRID_SIZE; ++i) {
			for (int j = 0; j < TEST_GRID_SIZE; ++j) {
				final Attack act = new Attack();

				context.add(act);
				grid.moveTo(act, i, j);
				bugs.add(act);
			}
		}

		final Attack bug = SMUtils.randomElementOf(bugs); // Bug to test
		final GridPoint prevLocation = grid.getLocation(bug);

		
		bug.step();

		
		final GridPoint newLocation = grid.getLocation(bug);

		
		Assert.assertEquals(prevLocation, newLocation);
	}

}
