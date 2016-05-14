package at.ac.univie.frog;

import org.junit.Test;

import at.ac.univie.SplitDAO.Friend;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
        Friend max =  new Friend(1,"Weinbahn", "Andy", "ich@du.com");
        max.toString();
    }
}