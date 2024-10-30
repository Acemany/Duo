package arc.assets.loaders;

import arc.assets.AssetLoaderParameters;
import arc.assets.AssetManager;
import arc.files.FileHandle;

public abstract class SynchronousAssetLoader<T, P extends AssetLoaderParameters<T>> extends AssetLoader<T, P>{
    public SynchronousAssetLoader(FileHandleResolver resolver){
        super(resolver);
    }

    public abstract T load(AssetManager assetManager, String fileName, FileHandle file, P parameter);
}
