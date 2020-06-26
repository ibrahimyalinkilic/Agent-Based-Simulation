
package stupidmodel.observer;

import java.awt.Color;

import repast.simphony.valueLayer.ValueLayer;
import repast.simphony.visualizationOGL2D.ValueLayerStyleOGL;
import stupidmodel.agents.Attack;
import stupidmodel.common.Constants;


public class ServerLayerStyleOGL implements ValueLayerStyleOGL {

	
	protected ValueLayer layer = null; 								

	
	@Override
	public void init(final ValueLayer layer) {
		if (null == layer) {
			throw new IllegalArgumentException(
					"Parameter layer cannot be null.");
		}

		if (this.layer != null) {
			throw new IllegalStateException(
					String.format("Server value layer should not be reinitialized with a new ValueLayer instance."));
		}

		this.layer = layer;
	}

	
	@Override
	public float getCellSize() {
		return Constants.GUI_CELL_SIZE;
	}

	
	@Override
	public Color getColor(final double... coordinates) {
		final double food = layer.get(coordinates);

		if (food < 0) {
			throw new IllegalStateException(
					String.format(
							"A cell's server availability property should be non-negative, but its current value is %f.",
							food));
		}

		final int strength = (int) Math.min(200 * food, 255);
		return new Color(0, strength, 0); // 0x000000 - black,
											// 0x00FF00 - green
	}
}
