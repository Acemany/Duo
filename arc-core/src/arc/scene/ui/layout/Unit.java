package arc.scene.ui.layout;

import arc.Application.ApplicationType;
import arc.Core;

public enum Unit{
    px{
        @Override
        public float scl(float amount){
            return amount;
        }
    },
    dp{
        float scl = -1;

        @Override
        public float scl(float amount){
            if(scl < 0f){
                //calculate scaling value if it hasn't been set yet
                if(Core.app.getType() == ApplicationType.Desktop){
                    scl = 1f * product;
                }else if(Core.app.getType() == ApplicationType.WebGL){
                    scl = 1f;
                }else{
                    //mobile scaling
                    scl = Math.max(Math.round((Core.graphics.getDensity() / 1.5f + addition) / 0.5) * 0.5f, 1f);
                }
            }
            return amount * scl;
        }
    };
    public float addition = 0f;
    public float product = 1f;

    public abstract float scl(float amount);
}
