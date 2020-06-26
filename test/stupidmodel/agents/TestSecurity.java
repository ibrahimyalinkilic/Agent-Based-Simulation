package stupidmodel.agents;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import junit.framework.Assert;

import org.junit.Test;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.engine.environment.RunState;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import stupidmodel.common.Constants;


public class TestSecurity {

	
	@Test
	public void testToStringWithoutGrid() {
		final Security Security = new Security();
		Assert.assertNotNull(Security.toString());
	}

	
	@Test
	public void testToStringWithGrid() {
		final Security Security = new Security();
		final int x = RandomHelper.nextIntFromTo(0, Integer.MAX_VALUE);
		final int y = RandomHelper.nextIntFromTo(0, Integer.MAX_VALUE);

		final Context<Object> context = new DefaultContext<Object>();
		RunState.init().setMasterContext(context);

		@SuppressWarnings("unchecked")
		final Grid<Object> grid = mock(Grid.class);
		when(grid.getName()).thenReturn(Constants.GRID_ID);
		when(grid.getLocation(Security)).thenReturn(new GridPoint(x, y));

		context.addProjection(grid);

		context.add(Security);
		Assert.assertNotNull(Security.toString());
	}

}
