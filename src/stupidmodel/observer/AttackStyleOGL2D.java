
package stupidmodel.observer;

import java.awt.Color;

import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;
import stupidmodel.agents.Attack;


public class AttackStyleOGL2D extends DefaultStyleOGL2D {

	
	@Override
	public Color getColor(final Object agent) {
		if (agent instanceof Attack) {
			final Attack Attack = (Attack) agent;

			if (Attack.getSize() < 0) {
				throw new IllegalStateException(
						String.format(
								"An agent's size property should be non-negative, but its current value is %f.",
								Attack.getSize()));
			}
			
			final int strength = (int) Math.max(200 - 20 * Attack.getSize(), 0);
			return new Color(0xFF, strength, strength); // 0xFFFFFF - white,
														// 0xFF0000 - red
		}

		return super.getColor(agent);
	}

}
