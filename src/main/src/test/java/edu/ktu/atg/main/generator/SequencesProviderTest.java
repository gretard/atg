package edu.ktu.atg.main.generator;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import edu.ktu.atg.common.executables.ExecutableSequence;
import edu.ktu.atg.generator.SequencesProvider;
import edu.ktu.atg.generator.SequencesProvider.ClassContext;

public class SequencesProviderTest {

    @Test
    public void testGetSequences() {
        SequencesProvider sut = new SequencesProvider();
        ClassContext ctx = sut.getSequences(TestClasses.Test1.class);
        System.out.println(ctx.getChromosomes().size());
    }

}
