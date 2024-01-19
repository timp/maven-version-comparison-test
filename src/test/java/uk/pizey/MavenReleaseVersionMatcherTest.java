package uk.pizey;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MavenReleaseVersionMatcherTest {
    private MavenReleaseVersionMatcher it;


    @Test
    public void testDigits() {
        it = new MavenReleaseVersionMatcher("1");
        assertTrue(it.digits.size() == 1);
        it = new MavenReleaseVersionMatcher("1.1");
        assertTrue(it.digits.size() == 2);
        it = new MavenReleaseVersionMatcher("1.1");
        assertTrue(it.digits.size() == 2);
        it = new MavenReleaseVersionMatcher("1.1-SNAPSHOT");
        assertTrue(it.digits.size() == 2);
    }

    /*

| 2023-VTW360-1.0.3-SNAPSHOT | 2023-VTW361-1.0.4-SNAPSHOT | Eh? |
   */
    @Test
    public void testversionPatternBreakdown() {
        it = new MavenReleaseVersionMatcher("2023-VTW360-1.0.3-SNAPSHOT");
        assertTrue(it.digits.size() == 1);
    }
/*
| 2023&#x7c;VTW-360&#x7c;1.0.14 | Illegal characters in jar name | F |
| 2023#VTW-360#1.0.14-SNAPSHOT | 2024#VTW-360#1.0.14-SNAPSHOT | Eh? |
| 2023.VTW-360.1.0.14-SNAPSHOT | 2024.VTW-360.1.0.14-SNAPSHOT | Eh? |
| 360.2023-1.0.15-SNAPSHOT | 360.2023-2.0.15-SNAPSHOT | Eh? |
| 2023.360.1.0.14-SNAPSHOT | 2023.360.1.0.15-SNAPSHOT | Meh |
| 1.0.19-VTW360-2023.SNAPSHOT | hangs | Eh? |
| 1.0.19-VTW360-2023-SNAPSHOT | 1.0.19-VTW361-2023-SNAPSHOT | Eh? |
| 1.0.19-2023-VTW360-SNAPSHOT | 1.0.19-2024-VTW360-SNAPSHOT | Eh? |
| 1.0.19-YY24-VTW360-SNAPSHOT | 1.0.19-YY25-VTW360-SNAPSHOT | F |
| 1-0-19-2025-VTW360-SNAPSHOT | 1-1-19-2025-VTW360-SNAPSHOT | Eh? |
| 360.2023.1.0.16-SNAPSHOT | 360.2023.1.0.17-SNAPSHOT | MKay |

     */

    @Test
    public void parseFailure() {
        fails("VTW360-2023-1.0.3-SNAPSHOT");
        fails("VTW360.2023.1.0.4-SNAPSHOT");
        fails("VTW360-2023.1.0.4-SNAPSHOT");
        fails("VTW-360-2023.1.0.14-SNAPSHOT");
        fails("VTW-360.2023.1.0.16-SNAPSHOT");
    }

    private void fails(String v) {
        try {
            it = new MavenReleaseVersionMatcher(v);
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().startsWith("Unable to parse the version string"));
        }
    }
}