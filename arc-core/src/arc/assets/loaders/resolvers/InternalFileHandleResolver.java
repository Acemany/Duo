package arc.assets.loaders.resolvers;

import arc.Core;
import arc.assets.loaders.FileHandleResolver;
import arc.files.FileHandle;

public class InternalFileHandleResolver implements FileHandleResolver{
    @Override
    public FileHandle resolve(String fileName){
        return Core.files.internal(fileName);
    }
}
