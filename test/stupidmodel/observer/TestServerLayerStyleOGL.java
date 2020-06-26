
package stupidmodel.observer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Color;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import repast.simphony.random.RandomHelper;
import repast.simphony.valueLayer.ValueLayer;
import stupidmodel.common.Constants;


public class TestServerLayerStyleOGL {

	
	private ServerLayerStyleOGL style;

	
	@Before
	public void initTest() {
		style = new ServerLayerStyleOGL();
	}

	

	
	@Test(expected = IllegalArgumentException.class)
	public void testInitParameterValidation() {
		style.init(null);
	}

	
	@Test(expected = IllegalStateException.class)
	public void testMultipleInit() {
		final ValueLayer layer = mock(ValueLayer.class);
		style.init(layer);
		style.init(layer); 
	}

	
	@Test
	public void testInitWorksProperly() {
		final ValueLayer layer = mock(ValueLayer.class);
		style.init(layer);
		Assert.assertEquals(layer, style.layer); // Testing representation
	}

	
	@Test
	public void testCellSize() {
		Assert.assertEquals(Constants.GUI_CELL_SIZE, style.getCellSize(),
				Constants.DELTA);
	}

	

	
	@Test(expected = IllegalStateException.class)
	public void testInvalidFoodValue() {
		final int x = RandomHelper.nextIntFromTo(0, Integer.MAX_VALUE);
		final int y = RandomHelper.nextIntFromTo(0, Integer.MAX_VALUE);

		final ValueLayer layer = mock(ValueLayer.class);

		final double wrongValue = RandomHelper.nextDoubleFromTo(
				-Double.MAX_VALUE, -Double.MIN_VALUE);

		when(layer.get(x, y)).thenReturn(wrongValue);
		style.init(layer);

		style.getColor(x, y); 
	}

	
	@Test
	public void testBlackColor() {
		final int x = RandomHelper.nextIntFromTo(0, Integer.MAX_VALUE);
		final int y = RandomHelper.nextIntFromTo(0, Integer.MAX_VALUE);

		final ValueLayer layer = mock(ValueLayer.class);
		when(layer.get(x, y)).thenReturn(0.0);
		style.init(layer);

		Assert.assertEquals(Color.BLACK, style.getColor(x, y));
	}

	
	@Test
	public void testGreenColor() {
		final int x = RandomHelper.nextIntFromTo(0, Integer.MAX_VALUE);
		final int y = RandomHelper.nextIntFromTo(0, Integer.MAX_VALUE);

		final ValueLayer layer = mock(ValueLayer.class);
		when(layer.get(x, y)).thenReturn(255.0);
		style.init(layer);

		Assert.assertEquals(Color.GREEN, style.getColor(x, y));
	}

	

}