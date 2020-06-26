/*
 * Version info:
 *     $HeadURL: https://cscs-repast-demos.googlecode.com/svn/richard/StupidModel/tags/2011_06_18_model_16/test/apitest/TestRepastAPI.java $
 *     $LastChangedDate: 2011-06-18 16:19:31 +0200 (Szo, 18 jún. 2011) $
 *     $LastChangedRevision: 428 $
 *     $LastChangedBy: richard.legendi@gmail.com $
 */
package apitest;

import junit.framework.Assert;

import org.junit.Test;

import repast.simphony.random.RandomHelper;

/**
 * Simple test cases to make assertions how the <i>Repast Simphony Java API</i>
 * works.
 * 
 * @author Richard O. Legendi (richard.legendi)
 * @since 2.0-beta, 2011
 * @version $Id: TestRepastAPI.java 428 2011-06-18 14:19:31Z richard.legendi@gmail.com $
 */
public class TestRepastAPI {

	/**
	 * Number of experiments to perform for each assertion.
	 */
	private static final int EXPERIMENTS = 10 * 1000;

	/**
	 * Assert that by using the default <code>RandomHelper</code> uniform
	 * distribution, the returned values are from the interval
	 * <code>[0,1]</code>.
	 */
	@Test
	public void testDefaultRandomHelperUniformDistribution() {
		for (int i = 0; i < EXPERIMENTS; ++i) {
			final double d = RandomHelper.nextDouble();
			Assert.assertTrue(0.0 <= d);
			Assert.assertTrue(d <= 1.0);
		}
	}

}
