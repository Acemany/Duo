package arc.util;

import arc.Core;
import arc.files.FileHandle;
import arc.util.reflect.ClassReflection;
import arc.util.reflect.Method;

public class ScreenRecorder{
    private static Runnable record;

    static{
        try{
            Class<?> recorderClass = ClassReflection.forName("arc.recorder.GifRecorder");
            Object recorder = ClassReflection.getConstructor(recorderClass).newInstance();
            Method method = ClassReflection.getMethod(recorderClass, "setExportDirectory", FileHandle.class);
            method.invoke(recorder, Core.files.local("../../desktop/gifexport"));
            Method r = ClassReflection.getMethod(recorderClass, "update");
            Object[] args = {};
            record = () -> {
                try{
                    r.invoke(recorder, args);
                }catch(Exception ignored){
                }
            };
        }catch(Throwable ignored){}
    }

    public static void record(){
        if(record == null) return;
        record.run();
    }
}
