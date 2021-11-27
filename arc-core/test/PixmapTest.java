import arc.graphics.*;
import arc.util.*;
import arc.util.noise.*;
import org.junit.*;

import static arc.math.Mathf.*;
import static org.junit.Assert.*;

public class PixmapTest{

    @Test
    public void pixmapCreate(){

        //test with no natives
        Pixmap pix = new Pixmap(100, 100);
        pix.fillCircle(50, 50, 30, Color.red.rgba());

        assertEquals(Color.red.rgba(), pix.get(50, 50));
        assertEquals(Color.red.rgba(), pix.get(54, 54));
        assertEquals(0, pix.get(0, 0));

        ArcNativesLoader.load();

        pix = new Pixmap(100, 100);
        pix.fillCircle(50, 50, 30, Color.red.rgba());

        assertEquals(Color.red.rgba(), pix.get(50, 50));
        assertEquals(Color.red.rgba(), pix.get(54, 54));
        assertEquals(0, pix.get(0, 0));
    }

    /*
    @Test
    public void normals(){
        Vec3 light = new Vec3(1, 1, 1).nor();
        Pixmap normals = new Pixmap(new Fi("/home/anuke/Projects/Mindustry/core/assets/sprites/cloud_normal.png"));
        Pixmap clouds = new Pixmap(new Fi("/home/anuke/Projects/Mindustry/core/assets/sprites/clouds_basic.png"));
        normals.each((x, y) -> {
            Tmp.c1.set(normals.get(x, y));
            Tmp.v31.set(Tmp.c1.r * 2f - 1f, Tmp.c1.g * 2f - 1f, (Tmp.c1.b - 0.5f) * 2f - 1f).nor();
            float dot = Tmp.v31.dot(light);
            float alpha = Mathf.clamp(dot + 1);
            clouds.set(x, y, Tmp.c1.set(clouds.get(x, y)).mul(Mathf.lerp(alpha, 1f, 0.85f)));
            //float l = Mathf.lerp(alpha, 1f, 0.5f);
            //normals.set(x, y, Color.rgba8888(l, l, l, Mathf.lerp(alpha, 1f, 0f)));
        });
        new Fi("/home/anuke/out.png").writePng(normals);
        new Fi("/home/anuke/clouds.png").writePng(clouds);
    }*/

    void bench(Runnable a, Runnable b, int amount){
        for(int i = 0; i < amount/3; i++){
            a.run();
            b.run();
        }

        Time.mark();

        for(int i = 0; i < amount; i++){
            a.run();
        }

        Log.info("Time for A: " + Time.elapsed());

        Time.mark();

        for(int i = 0; i < amount; i++){
            b.run();
        }

        Log.info("Time for B: " + Time.elapsed());
    }

}