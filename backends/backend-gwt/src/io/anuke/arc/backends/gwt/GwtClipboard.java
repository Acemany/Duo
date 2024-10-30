package arc.backends.gwt;

import arc.util.Clipboard;

/** Basic implementation of clipboard in GWT. Copy-paste only works inside the libgdx application. */
public class GwtClipboard implements Clipboard{

    private String content = "";

    @Override
    public String getContents(){
        return content;
    }

    @Override
    public void setContents(String content){
        this.content = content;
    }
}
