package edu.ktu.atg.generator;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.ktu.atg.generator.SequencesProvider.ClassContext;

public class SequencesProviderTest {

    @Test
    public void testGetSequences() {
      SequencesProvider sut = new SequencesProvider();
      ClassContext context = sut.getSequences(TestEnum.class);
      System.out.println(context.getChromosomes().size());
    }

    public static enum TestEnum {
        ONE;
    }

}
