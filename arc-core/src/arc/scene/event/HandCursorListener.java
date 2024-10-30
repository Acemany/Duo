package arc.scene.event;

import arc.Core;
import arc.Graphics.Cursor.SystemCursor;
import arc.function.BooleanProvider;
import arc.scene.Element;
import arc.scene.utils.UIUtils;

public class HandCursorListener extends ClickListener{
    private BooleanProvider enabled = () -> true;
    private boolean set;

    public void setEnabled(BooleanProvider vis){
        this.enabled = vis;
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Element fromActor){
        super.enter(event, x, y, pointer, fromActor);

        if(pointer != -1 || !enabled.get() || UIUtils.isDisabled(event.targetActor) || UIUtils.isDisabled(fromActor)){
            return;
        }

        Core.graphics.cursor(SystemCursor.hand);
        set = true;
    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Element toActor){
        super.exit(event, x, y, pointer, toActor);

        if(!enabled.get() || !set) return;

        if(pointer == -1){
            Core.graphics.restoreCursor();
        }
        set = false;
    }
}
