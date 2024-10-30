package arc.util.serialization;

import arc.files.FileHandle;

import java.io.InputStream;

public interface BaseJsonReader{
    JsonValue parse(InputStream input);

    JsonValue parse(FileHandle file);
}
