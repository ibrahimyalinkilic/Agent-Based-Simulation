
package stupidmodel.common;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import repast.simphony.query.space.grid.GridCell;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.ContextUtils;
import stupidmodel.agents.Attack;


public final strictfp class SMUtils {

	
	public static <T> List<GridCell<T>> getFreeGridCells(
			final List<GridCell<T>> neighborhood) {
		if (null == neighborhood) {
			throw new IllegalArgumentException(
					"Parameter neighborhood cannot be null.");
		}

		final ArrayList<GridCell<T>> ret = new ArrayList<GridCell<T>>();

		for (final GridCell<T> act : neighborhood) {
			if (0 == act.size()) {
				ret.add(act);
			}
		}

		return ret;
	}

	
	public static <T> T randomElementOf(final List<T> list) {
		if (null == list) {
			throw new IllegalArgumentException("Parameter list cannot be null.");
		}

		if (list.isEmpty()) {
			throw new IllegalArgumentException(
					"Cannot return a random element from an empty list.");
		}

		return list.get(RandomHelper.nextIntFromTo(0, list.size() - 1));
	}

	
	public static boolean prob(final double threshold) {
		if (threshold < 0.0 || 1.0 < threshold) {
			throw new IllegalArgumentException(String.format(
					"Parameter threshold=%f should be in interval [0, 1].",
					threshold));
		}

		return (threshold < RandomHelper.nextDouble());
	}

	
	public static List<CellData> readDataFile(final String cellDataFileName) {
		if (null == cellDataFileName) {
			throw new IllegalArgumentException(
					"Parameter cellDataFileName cannot be null.");
		}

		if (cellDataFileName.isEmpty()) {
			throw new IllegalArgumentException(
					"A file name cannot have an empty name.");
		}

		final ArrayList<CellData> ret = new ArrayList<CellData>();

		try {
			final BufferedReader br = new BufferedReader(new FileReader(
					cellDataFileName));

			try {

				// Read lines, parse data and add a new CellData for each one

				String line = null;

				while ((line = br.readLine()) != null) {
					// Split the line around whitespaces
					final String[] data = line.split("\\s+");

					// Check if current line seems all right
					if (data.length != 3) {
						throw new IllegalArgumentException(String.format(
								"File %s contains a malformed input line: %s",
								cellDataFileName, line));
					}

					try {
						int idx = 0;
						final int x = Integer.parseInt(data[idx++]);
						final int y = Integer.parseInt(data[idx++]);
						final double foodProductionRate = Double
								.parseDouble(data[idx++]);

						ret.add(new CellData(x, y, foodProductionRate));
					} catch (final NumberFormatException e) {
						throw new IllegalArgumentException(String.format(
								"File %s contains a malformed input line: %s",
								cellDataFileName, line), e);
					}
				}
			} finally {
				br.close();
			}
		} catch (final FileNotFoundException e) {
			throw new IllegalArgumentException(e);
		} catch (final IOException e) {
			throw new IllegalArgumentException(e);
		}

		return Collections.unmodifiableList(ret);
	}

	
	public static Grid<Object> getGrid(final Object o) {
		@SuppressWarnings("unchecked")
		final Grid<Object> grid = (Grid<Object>) ContextUtils.getContext(o)
				.getProjection(Constants.GRID_ID);

		if (null == grid) {
			throw new IllegalStateException("Cannot locate grid in context.");
		}

		return grid;
	}

	private SMUtils() {
		;
	}

}
