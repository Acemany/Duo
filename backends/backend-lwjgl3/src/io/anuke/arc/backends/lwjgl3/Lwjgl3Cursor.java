package arc.backends.lwjgl3;

import arc.collection.Array;
import arc.Graphics.Cursor;
import arc.graphics.Pixmap;
import arc.util.ArcRuntimeException;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;

import java.util.HashMap;
import java.util.Map;

public class Lwjgl3Cursor implements Cursor{
    static final Array<Lwjgl3Cursor> cursors = new Array<>();
    static final Map<SystemCursor, Long> systemCursors = new HashMap<>();

    final Lwjgl3Window window;
    final long glfwCursor;
    Pixmap pixmapCopy;
    GLFWImage glfwImage;

    Lwjgl3Cursor(Lwjgl3Window window, Pixmap pixmap, int xHotspot, int yHotspot){
        this.window = window;
        if(pixmap.getFormat() != Pixmap.Format.RGBA8888){
            throw new ArcRuntimeException("Cursor image pixmap is not in RGBA8888 format.");
        }

        if((pixmap.getWidth() & (pixmap.getWidth() - 1)) != 0){
            throw new ArcRuntimeException(
            "Cursor image pixmap width of " + pixmap.getWidth() + " is not a power-of-two greater than zero.");
        }

        if((pixmap.getHeight() & (pixmap.getHeight() - 1)) != 0){
            throw new ArcRuntimeException("Cursor image pixmap height of " + pixmap.getHeight()
            + " is not a power-of-two greater than zero.");
        }

        if(xHotspot < 0 || xHotspot >= pixmap.getWidth()){
            throw new ArcRuntimeException("xHotspot coordinate of " + xHotspot
            + " is not within image width bounds: [0, " + pixmap.getWidth() + ").");
        }

        if(yHotspot < 0 || yHotspot >= pixmap.getHeight()){
            throw new ArcRuntimeException("yHotspot coordinate of " + yHotspot
            + " is not within image height bounds: [0, " + pixmap.getHeight() + ").");
        }

        this.pixmapCopy = new Pixmap(pixmap.getWidth(), pixmap.getHeight(), Pixmap.Format.RGBA8888);
        this.pixmapCopy.drawPixmap(pixmap, 0, 0);

        glfwImage = GLFWImage.malloc();
        glfwImage.width(pixmapCopy.getWidth());
        glfwImage.height(pixmapCopy.getHeight());
        glfwImage.pixels(pixmapCopy.getPixels());
        glfwCursor = GLFW.glfwCreateCursor(glfwImage, xHotspot, yHotspot);
        cursors.add(this);
    }

    static void dispose(Lwjgl3Window window){
        for(int i = cursors.size - 1; i >= 0; i--){
            Lwjgl3Cursor cursor = cursors.get(i);
            if(cursor.window.equals(window)){
                cursors.remove(i).dispose();
            }
        }
    }

    static void disposeSystemCursors(){
        for(long systemCursor : systemCursors.values()){
            GLFW.glfwDestroyCursor(systemCursor);
        }
        systemCursors.clear();
    }

    static void setSystemCursor(long windowHandle, SystemCursor systemCursor){
        Long glfwCursor = systemCursors.get(systemCursor);
        if(glfwCursor == null){
            long handle = 0;
            if(systemCursor == SystemCursor.arrow){
                handle = GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR);
            }else if(systemCursor == SystemCursor.crosshair){
                handle = GLFW.glfwCreateStandardCursor(GLFW.GLFW_CROSSHAIR_CURSOR);
            }else if(systemCursor == SystemCursor.hand){
                handle = GLFW.glfwCreateStandardCursor(GLFW.GLFW_HAND_CURSOR);
            }else if(systemCursor == SystemCursor.horizontalResize){
                handle = GLFW.glfwCreateStandardCursor(GLFW.GLFW_HRESIZE_CURSOR);
            }else if(systemCursor == SystemCursor.verticalResize){
                handle = GLFW.glfwCreateStandardCursor(GLFW.GLFW_VRESIZE_CURSOR);
            }else if(systemCursor == SystemCursor.ibeam){
                handle = GLFW.glfwCreateStandardCursor(GLFW.GLFW_IBEAM_CURSOR);
            }else{
                throw new ArcRuntimeException("Unknown system cursor " + systemCursor);
            }

            if(handle == 0){
                return;
            }
            glfwCursor = handle;
            systemCursors.put(systemCursor, glfwCursor);
        }
        GLFW.glfwSetCursor(windowHandle, glfwCursor);
    }

    @Override
    public void dispose(){
        if(pixmapCopy == null){
            throw new ArcRuntimeException("Cursor already disposed");
        }
        cursors.removeValue(this, true);
        pixmapCopy.dispose();
        pixmapCopy = null;
        glfwImage.free();
        GLFW.glfwDestroyCursor(glfwCursor);
    }
}