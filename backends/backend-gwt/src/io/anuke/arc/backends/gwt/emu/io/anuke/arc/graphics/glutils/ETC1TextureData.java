package arc.graphics.glutils;

import arc.files.FileHandle;
import arc.graphics.Pixmap;
import arc.graphics.Pixmap.Format;
import arc.graphics.TextureData;
import arc.util.ArcRuntimeException;

public class ETC1TextureData implements TextureData{
    public ETC1TextureData(FileHandle file){
        throw new ArcRuntimeException("ETC1TextureData not supported in GWT backend");
    }

    public ETC1TextureData(FileHandle file, boolean useMipMaps){
        throw new ArcRuntimeException("ETC1TextureData not supported in GWT backend");
    }

    @Override
    public TextureDataType getType(){
        return null;
    }

    @Override
    public boolean isPrepared(){
        return false;
    }

    @Override
    public void prepare(){
    }

    @Override
    public Pixmap consumePixmap(){
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean disposePixmap(){
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void consumeCustomData(int target){
        // TODO Auto-generated method stub

    }

    @Override
    public int getWidth(){
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getHeight(){
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Format getFormat(){
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean useMipMaps(){
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isManaged(){
        // TODO Auto-generated method stub
        return false;
    }

}