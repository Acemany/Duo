package arc.backends.lwjgl3;

import arc.Core;
import arc.graphics.Pixmap;
import arc.util.Clipboard;
import org.lwjgl.glfw.GLFW;

/**
 * Clipboard implementation for desktop that uses the system clipboard via GLFW.
 * @author mzechner
 */
public class Lwjgl3Clipboard implements Clipboard{
    @Override
    public String getContents(){
        return GLFW.glfwGetClipboardString(((Lwjgl3Graphics)Core.graphics).getWindow().getWindowHandle());
    }

    @Override
    public void setContents(String content){
        GLFW.glfwSetClipboardString(((Lwjgl3Graphics)Core.graphics).getWindow().getWindowHandle(), content);
    }

    @Override
    public void setContents(Pixmap pixmap){

    }
}
