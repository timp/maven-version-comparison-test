package uk.pizey;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ComparableVersionTest  {

    @Test
    public void existingTest() {
        ComparableVersion it = new ComparableVersion("17.0.10");
        ComparableVersion previous = new ComparableVersion("17.0.9");
        ComparableVersion previousSnapshot = new ComparableVersion("17.0.9-snapshot");

        assertTrue(it.compareTo(previous) > 0);
        assertTrue(previous.compareTo(previous) == 0);
        assertTrue(previous.compareTo(it) < 0);

        assertTrue(previous.compareTo(previousSnapshot) > 0);
    }
    @Test
    public void initialProposalTest() {
        ComparableVersion it = new ComparableVersion("SMD130-2.0.6");
        ComparableVersion previous = new ComparableVersion("SMD130-2.0.4");
        ComparableVersion previousSnapshot = new ComparableVersion("SMD130-2.0.4-snapshot");

        assertTrue(it.compareTo(previous) > 0);
        assertTrue(previous.compareTo(previous) == 0);
        assertTrue(previous.compareTo(it) < 0);
        assertTrue(previous.compareTo(previousSnapshot) > 0);

        assertTrue(previous.compareTo(previousSnapshot) > 0);
    }

    /* Initial proposal does not work*/
    @Test
    public void initialNewOldComparisonFailsTest() {
        ComparableVersion it = new ComparableVersion("SMD130-2.0.6");
        ComparableVersion previous = new ComparableVersion("2.0.6");
        assertFalse(it.compareTo(previous) > 0);
        assertTrue(previous.compareTo(previous) == 0);
        assertFalse(previous.compareTo(it) < 0);
    }

    @Test
    public void workableProposalTest() {
        ComparableVersion it = new ComparableVersion("2024-SMD130-2.0.6");
        ComparableVersion previous = new ComparableVersion("2024-SMD130-2.0.4");
        ComparableVersion previousSnapshot = new ComparableVersion("2024-SMD130-2.0.4-snapshot");

        assertTrue(it.compareTo(previous) > 0);
        assertTrue(previous.compareTo(previous) == 0);
        assertTrue(previous.compareTo(it) < 0);
        assertTrue(previous.compareTo(previousSnapshot) > 0);

        assertTrue(previous.compareTo(previousSnapshot) > 0);
    }
    @Test
    public void workableNewOldComparisonTest() {
        ComparableVersion it = new ComparableVersion("2024-SMD130-2.0.6");
        ComparableVersion previous = new ComparableVersion("2.0.6");
        assertTrue(it.compareTo(previous) > 0);
        assertTrue(previous.compareTo(previous) == 0);
        assertTrue(previous.compareTo(it) < 0);
    }

}
