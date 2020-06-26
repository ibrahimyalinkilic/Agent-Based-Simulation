
package stupidmodel.observer;

import static org.mockito.Mockito.*;

import java.awt.Color;

import org.junit.Assert;
import org.junit.Test;

import repast.simphony.random.RandomHelper;
import stupidmodel.agents.Attack;


public class TestAttackOGL2D {


	private final AttackStyleOGL2D style = new AttackStyleOGL2D();

	
	@Test
	public void testWhiteColor() {
		final Attack Attack = new Attack();
		Attack.setSize(0);

		final Color color = style.getColor(Attack);

		Assert.assertEquals(new Color(255, 200, 200), color);
	}

	
	@Test(expected = IllegalStateException.class)
	public void testInvalid() {
		final Attack Attack = mock(Attack.class);
		final double wrongValue = RandomHelper.nextDoubleFromTo(
				-Double.MAX_VALUE, -Double.MIN_VALUE);
		when(Attack.getSize()).thenReturn(wrongValue);

		style.getColor(Attack); 
	}

	
	@Test
	public void testNonAttackColor() {
		final Color color = style.getColor(null); 

		
		Assert.assertEquals(Color.BLUE, color);
	}

	
	@Test
	public void testRedColor() {
		final Attack Attack = new Attack();
		Attack.setSize(255);

		final Color color = style.getColor(Attack);
		Assert.assertEquals(Color.RED, color);
	}

	
	@Test
	public void testPinkColor() {
		final Attack Attack = new Attack();
		
		Attack.setSize(1.25);

		final Color color = style.getColor(Attack);
		Assert.assertEquals(Color.PINK, color);
	}

}
