package arc.graphics.glutils;

import arc.Core;
import arc.files.FileHandle;
import arc.graphics.Pixmap;
import arc.graphics.Pixmap.Format;
import arc.graphics.TextureData;
import arc.math.Mathf;
import arc.util.ArcRuntimeException;

public class FileTextureData implements TextureData{
    public static boolean copyToPOT;

    final FileHandle file;
    int width = 0;
    int height = 0;
    Format format;
    Pixmap pixmap;
    boolean useMipMaps;
    boolean isPrepared = false;

    public FileTextureData(FileHandle file, Pixmap preloadedPixmap, Format format, boolean useMipMaps){
        this.file = file;
        this.pixmap = preloadedPixmap;
        this.format = format;
        this.useMipMaps = useMipMaps;
        if(pixmap != null){
            pixmap = ensurePot(pixmap);
            width = pixmap.getWidth();
            height = pixmap.getHeight();
            if(format == null) this.format = pixmap.getFormat();
        }
    }

    @Override
    public boolean isPrepared(){
        return isPrepared;
    }

    @Override
    public void prepare(){
        if(isPrepared) throw new ArcRuntimeException("Already prepared");
        if(pixmap == null){
            pixmap = ensurePot(new Pixmap(file));
            width = pixmap.getWidth();
            height = pixmap.getHeight();
            if(format == null) format = pixmap.getFormat();
        }
        isPrepared = true;
    }

    private Pixmap ensurePot(Pixmap pixmap){
        if(Core.gl20 == null && copyToPOT){
            int pixmapWidth = pixmap.getWidth();
            int pixmapHeight = pixmap.getHeight();
            int potWidth = Mathf.nextPowerOfTwo(pixmapWidth);
            int potHeight = Mathf.nextPowerOfTwo(pixmapHeight);
            if(pixmapWidth != potWidth || pixmapHeight != potHeight){
                Pixmap tmp = new Pixmap(potWidth, potHeight, pixmap.getFormat());
                tmp.drawPixmap(pixmap, 0, 0, 0, 0, pixmapWidth, pixmapHeight);
                pixmap.dispose();
                return tmp;
            }
        }
        return pixmap;
    }

    @Override
    public Pixmap consumePixmap(){
        if(!isPrepared) throw new ArcRuntimeException("Call prepare() before calling getPixmap()");
        isPrepared = false;
        Pixmap pixmap = this.pixmap;
        this.pixmap = null;
        return pixmap;
    }

    @Override
    public boolean disposePixmap(){
        return true;
    }

    @Override
    public int getWidth(){
        return width;
    }

    @Override
    public int getHeight(){
        return height;
    }

    @Override
    public Format getFormat(){
        return format;
    }

    @Override
    public boolean useMipMaps(){
        return useMipMaps;
    }

    @Override
    public boolean isManaged(){
        return true;
    }

    public FileHandle getFileHandle(){
        return file;
    }

    @Override
    public TextureDataType getType(){
        return TextureDataType.Pixmap;
    }

    @Override
    public void consumeCustomData(int target){
        throw new ArcRuntimeException("This TextureData implementation does not upload data itself");
    }
}
