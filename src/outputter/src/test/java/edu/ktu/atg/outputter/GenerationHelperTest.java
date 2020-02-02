package edu.ktu.atg.outputter;

import org.junit.Assert;
import org.junit.Test;

import edu.ktu.atg.common.executables.ExecutableValue;
import edu.ktu.atg.common.executables.ValueType;

public class GenerationHelperTest {

	@Test
	public void testGenerateNameIfArray() {
		ExecutableValue val = new ExecutableValue(Object[].class, ValueType.COMPLEX);
		String actual = GenerationHelper.generateNameIfArray(val);
		Assert.assertEquals("java.lang.Object[]", actual);
	}

}
