
package stupidmodel.common;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.engine.environment.RunState;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.TestGridCellFactory;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import stupidmodel.agents.Attack;


public class TestSMUtils {

	
	@Test
	public void giveMeCoverageForMyPrivateConstructor() throws Exception {
		final Constructor<?> constructor = SMUtils.class
				.getDeclaredConstructor();
		constructor.setAccessible(true);
		constructor.newInstance();
	}

	
	@Test(expected = IllegalArgumentException.class)
	public void testGetFreeGridCellsForNull() {
		SMUtils.getFreeGridCells(null);
	}

	
	@Test
	public void testGetFreeGridCellsForEmptyList() {
		final List<GridCell<Object>> ret = SMUtils
				.getFreeGridCells(new ArrayList<GridCell<Object>>());
		Assert.assertEquals(ret, Collections.emptyList());
	}

	
	@Test
	public void testGetFreeGridCellsForSimpleEmptyList() {
		final ArrayList<GridCell<Object>> neighborhood = new ArrayList<GridCell<Object>>();

		final GridCell<Object> gridCell = new GridCell<Object>(new GridPoint(1,
				1), Object.class);
		neighborhood.add(gridCell);

		final List<GridCell<Object>> ret = SMUtils
				.getFreeGridCells(neighborhood);
	
		Assert.assertEquals(ret, Collections.singletonList(gridCell));
	}

	
	@SuppressWarnings("unchecked")
	@Test
	public void testGetFreeGridCellsForMultipleEmptyElements() {
		final ArrayList<GridCell<Object>> neighborhood = new ArrayList<GridCell<Object>>();

		final GridCell<Object> gridCell1 = new GridCell<Object>(new GridPoint(
				1, 1), Object.class);
		final GridCell<Object> gridCell2 = new GridCell<Object>(new GridPoint(
				2, 2), Object.class);

		neighborhood.add(gridCell1);
		neighborhood.add(gridCell2);

		final List<GridCell<Object>> ret = SMUtils
				.getFreeGridCells(neighborhood);
		
		Assert.assertEquals(ret, Arrays.asList(gridCell1, gridCell2));
	}

	
	@Test
	public void testGetFreeGridCellsForSimpleList() {
		final ArrayList<GridCell<String>> neighborhood = new ArrayList<GridCell<String>>();

		final GridCell<String> oneObjectCell = TestGridCellFactory
				.createGridCellToTest(new GridPoint(1, 1), String.class, "A");
		final GridCell<String> emptyCell = new GridCell<String>(new GridPoint(
				2, 2), String.class);

		neighborhood.add(oneObjectCell);
		neighborhood.add(emptyCell);

		final List<GridCell<String>> ret = SMUtils
				.getFreeGridCells(neighborhood);

		Assert.assertEquals(ret, Collections.singletonList(emptyCell));
	}

	
	@Test
	public void testGetFreeGridCellsForSimpleOccupiedList() {
		final ArrayList<GridCell<String>> neighborhood = new ArrayList<GridCell<String>>();

		final GridCell<String> cell = TestGridCellFactory.createGridCellToTest(
				new GridPoint(1, 1), String.class, "A");

		final GridCell<String> bell = TestGridCellFactory.createGridCellToTest(
				new GridPoint(2, 2), String.class, "B");

		neighborhood.add(cell);
		neighborhood.add(bell);

		final List<GridCell<String>> ret = SMUtils
				.getFreeGridCells(neighborhood);

		Assert.assertEquals(ret, Collections.emptyList());
	}

	
	@Test(expected = IllegalArgumentException.class)
	public void testRandomElementOfForNull() {
		SMUtils.randomElementOf(null);
	}

	
	@Test(expected = IllegalArgumentException.class)
	public void testRandomElementOfForEmptyList() {
		SMUtils.randomElementOf(Collections.emptyList());
	}

	
	@Test
	public void testRandomElementOfForSingletonList() {
		final String value = "A";
		final String randomElement = SMUtils.randomElementOf(Collections
				.singletonList(value));

		Assert.assertEquals(value, randomElement);
	}

	
	@Test
	public void testRandomElementOfContainsResult() {
		final List<String> list = Arrays.asList("A", "B", "C", "D", "E");
		final String randomElement = SMUtils.randomElementOf(list);

		Assert.assertTrue(list.contains(randomElement));
	}

	
	@Test
	public void testRandomElementOfDoesNotModifiesOriginalList() {
		final List<String> list = Arrays.asList("A", "B", "C", "D", "E");
		final List<String> backupList = new ArrayList<String>(list);

		SMUtils.randomElementOf(list);
		Assert.assertEquals(backupList, list);
	}

	
	@Test(expected = IllegalArgumentException.class)
	public void testProbNegativeValue() {
		final double wrongValue = RandomHelper.nextDoubleFromTo(
				-Double.MAX_VALUE, -Double.MIN_VALUE);

		SMUtils.prob(wrongValue); // Should fail
	}

	
	@Test(expected = IllegalArgumentException.class)
	public void testProbParamAboveOne() {
		final double wrongValue = RandomHelper.nextDoubleFromTo(
				1.0 + Double.MIN_VALUE, Double.MAX_VALUE);

		SMUtils.prob(wrongValue); // Should fail
	}

	
	@Test
	public void testProbAlwaysFalse() {
		Assert.assertFalse(SMUtils.prob(1.0));
	}

	
	@Test
	public void testProbAlwaysTrue() {
		Assert.assertTrue(SMUtils.prob(0.0));
	}

	
	@Test(expected = IllegalStateException.class)
	public void testNoGrid() {
		
		final Context<Object> context = new DefaultContext<Object>();
		RunState.init().setMasterContext(context);

		final Attack Attack= new Attack();
		context.add(Attack);
		SMUtils.getGrid(Attack); // Should fail
	}

	
	@Test
	public void testGridQuery() {
		final Context<Object> context = new DefaultContext<Object>();
		RunState.init().setMasterContext(context);

		@SuppressWarnings("unchecked")
		final Grid<Object> grid = mock(Grid.class);
		when(grid.getName()).thenReturn(Constants.GRID_ID);

		context.addProjection(grid);

		final Attack attack= new Attack();
		context.add(attack);
		Assert.assertSame(grid, SMUtils.getGrid(attack));
	}

	
	@Test(expected = IllegalArgumentException.class)
	public void testReadDataFileWithNullParameter() {
		SMUtils.readDataFile(null); // Should fail
	}

	
	@Test(expected = IllegalArgumentException.class)
	public void testReadDataFileWithEmptyParameter() {
		SMUtils.readDataFile(""); // Should fail
	}

	
	@Test(expected = IllegalArgumentException.class)
	public void testReadDataFileWithNoTarget() {
	
		Assert.assertFalse(new File("/some/unexistent/file/here").exists());

		SMUtils.readDataFile("/some/unexistent/file/here");
	}

	
	@Test(expected = IllegalArgumentException.class)
	public void testReadDataFileWithLockedFile() {
		final String lockedFile = "test/stupidmodel/common/Stupid_Cell_LockedFile.Data";
		try {
			final FileChannel in = new RandomAccessFile(lockedFile, "rw")
					.getChannel();
			final java.nio.channels.FileLock lock = in.lock();
			try {
				SMUtils.readDataFile(lockedFile); // Should fail
			} finally {
				lock.release();
				in.close();
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	
	@Test(expected = IllegalArgumentException.class)
	public void testReadDataFileWithMissingData() {
		SMUtils.readDataFile("test/stupidmodel/common/Stupid_Cell_Missing.Data"); 
	}

	
	@Test(expected = IllegalArgumentException.class)
	public void testReadDataFileWithInvalidData() {
		// Chtulhu makes the test fail :-)
		SMUtils.readDataFile("test/stupidmodel/common/Stupid_Cell_Invalid.Data"); 
	}

	
	@Test
	public void testReadDataFileWithSampleData() {
		final List<CellData> data = SMUtils
				.readDataFile("test/stupidmodel/common/Stupid_Cell_Sample.Data");

		Assert.assertEquals(new CellData(5, 5, 0.1), data.get(0));
		Assert.assertEquals(new CellData(10, 15, 0.2), data.get(1));
		Assert.assertEquals(new CellData(3, 7, 0.3), data.get(2));
	}

}
