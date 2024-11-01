package math;

import arc.math.geom.Rectangle;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RectangleTest{
    @Test
    public void testToString(){
        assertEquals("[5.0,-4.1,0.03,-0.02]", new Rectangle(5f, -4.1f, 0.03f, -0.02f).toString());
    }

    @Test
    public void testFromString(){
        assertEquals(new Rectangle(5f, -4.1f, 0.03f, -0.02f), new Rectangle().fromString("[5.0,-4.1,0.03,-0.02]"));
    }
}
