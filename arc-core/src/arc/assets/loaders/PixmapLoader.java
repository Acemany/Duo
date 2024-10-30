package arc.assets.loaders;

import arc.assets.AssetDescriptor;
import arc.assets.AssetLoaderParameters;
import arc.assets.AssetManager;
import arc.collection.Array;
import arc.files.FileHandle;
import arc.graphics.Pixmap;

/**
 * {@link AssetLoader} for {@link Pixmap} instances. The Pixmap is loaded asynchronously.
 * @author mzechner
 */
public class PixmapLoader extends AsynchronousAssetLoader<Pixmap, PixmapLoader.PixmapParameter>{
    Pixmap pixmap;

    public PixmapLoader(FileHandleResolver resolver){
        super(resolver);
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, PixmapParameter parameter){
        pixmap = null;
        pixmap = new Pixmap(file);
    }

    @Override
    public Pixmap loadSync(AssetManager manager, String fileName, FileHandle file, PixmapParameter parameter){
        Pixmap pixmap = this.pixmap;
        this.pixmap = null;
        return pixmap;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, PixmapParameter parameter){
        return null;
    }

    public static class PixmapParameter extends AssetLoaderParameters<Pixmap>{
    }
}
