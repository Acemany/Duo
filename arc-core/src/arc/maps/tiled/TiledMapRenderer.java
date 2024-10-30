package arc.maps.tiled;

import arc.maps.MapLayer;
import arc.maps.MapObject;
import arc.maps.MapRenderer;

public interface TiledMapRenderer extends MapRenderer{
    void renderObjects(MapLayer layer);

    void renderObject(MapObject object);

    void renderTileLayer(TiledMapTileLayer layer);

    void renderImageLayer(TiledMapImageLayer layer);
}
