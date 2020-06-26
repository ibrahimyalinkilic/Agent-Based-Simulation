
package stupidmodel.common;

import java.lang.reflect.Constructor;

import org.junit.Test;


public class TestConstants {

	
	@Test
	public void giveMeCoverageForMyPrivateConstructor() throws Exception {
		final Constructor<?> constructor = Constants.class
				.getDeclaredConstructor();
		constructor.setAccessible(true);
		constructor.newInstance();
	}

}
