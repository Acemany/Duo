package arc.assets.loaders.resolvers;

import arc.Core;
import arc.assets.loaders.FileHandleResolver;
import arc.files.FileHandle;

public class AbsoluteFileHandleResolver implements FileHandleResolver{
    @Override
    public FileHandle resolve(String fileName){
        return Core.files.absolute(fileName);
    }
}
