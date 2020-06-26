
package stupidmodel.common;

import java.io.Serializable;
import java.util.Comparator;

import stupidmodel.agents.Server;


public class CellData {

	
	private final int x, y;

	
	private final double foodProductionRate;

	
	public static final CellDataXComparator CELL_DATA_X_COMPARATOR = new CellDataXComparator();

	
	public static final CellDataYComparator CELL_DATA_Y_COMPARATOR = new CellDataYComparator();

	
	private static final class CellDataXComparator implements
			Comparator<CellData>, Serializable {
		
		private static final long serialVersionUID = 4149351630089726905L;

	
		@Override
		public int compare(final CellData o1, final CellData o2) {
			return o1.getX() - o2.getX();
		}
	}

	
	private static final class CellDataYComparator implements
			Comparator<CellData>, Serializable {
		
		private static final long serialVersionUID = -5955739679291874417L;

		
		@Override
		public int compare(final CellData o1, final CellData o2) {
			return o1.getY() - o2.getY();
		}
	}

	
	public CellData(final int x, final int y, final double foodProductionRate) {
		super();

		if (x < 0) {
			throw new IllegalArgumentException(String.format(
					"Parameter x == %d < 0", x));
		}

		if (y < 0) {
			throw new IllegalArgumentException(String.format(
					"Parameter y == %d < 0", y));
		}

		if (foodProductionRate < 0) {
			throw new IllegalArgumentException(String.format(
					"Parameter serverProductionRate == %f < 0",
					foodProductionRate));
		}

		this.x = x;
		this.y = y;
		this.foodProductionRate = foodProductionRate;
	}

	
	public int getX() {
		return x;
	}

	
	public int getY() {
		return y;
	}

	
	public double getFoodProductionRate() {
		return foodProductionRate;
	}

	
	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof CellData) {
			final CellData other = (CellData) obj;
			return (x == other.x && y == other.y && Double
					.doubleToLongBits(foodProductionRate) == Double
					.doubleToLongBits(other.foodProductionRate));
		}

		return false;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		final long temp = Double.doubleToLongBits(foodProductionRate);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	
	@Override
	public String toString() {
		
		return String.format("CellData [x=%d, y=%d, serverProductionRate=%f", x,
				y, foodProductionRate);
	}

}
