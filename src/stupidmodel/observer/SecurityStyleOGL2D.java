package stupidmodel.observer;

import java.awt.Color;
import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;
import stupidmodel.agents.Security;


public class SecurityStyleOGL2D extends DefaultStyleOGL2D{

	@Override
	public Color getColor(final Object agent) {
		if (agent instanceof Security) {
			final Security Security = (Security) agent;
			
			return new Color(0x00, 0x2F, 0xA7); //blue	
		}

		return super.getColor(agent);
	}
}
