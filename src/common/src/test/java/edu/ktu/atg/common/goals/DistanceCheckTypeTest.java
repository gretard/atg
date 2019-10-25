package edu.ktu.atg.common.goals;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

public class DistanceCheckTypeTest {

    @Test
    public void testMatches() {
        DistanceCheckType type = DistanceCheckType.HITMAX;
        Assert.assertTrue(type.matches(type.initialValue, 10));
        Assert.assertTrue(type.matches(10, 11));
        Assert.assertFalse(type.matches(10, 9));

    }
    
    @Test
    public void testMatches2() {
        DistanceCheckType type = DistanceCheckType.UNHITMIN;
        Assert.assertTrue(type.matches(type.initialValue, 10));
        Assert.assertTrue(type.matches(10, 6));
        Assert.assertFalse(type.matches(10, 19));

    }
    


}
