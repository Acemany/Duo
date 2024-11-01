package math;

import arc.math.collision.BoundingBox;
import arc.math.geom.Vector3;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CollisionTest{

    @Test
    public void testBoundingBox(){
        BoundingBox b1 = new BoundingBox(Vector3.Zero, new Vector3(1, 1, 1));
        BoundingBox b2 = new BoundingBox(new Vector3(1, 1, 1), new Vector3(2, 2, 2));
        assertTrue(b1.contains(Vector3.Zero));
        assertTrue(b1.contains(b1));
        assertFalse(b1.contains(b2));
        // Note, in stage the bottom and left sides are inclusive while the right and top sides are exclusive.
    }

}
