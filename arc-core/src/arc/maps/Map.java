package arc.maps;

import arc.graphics.Texture;
import arc.maps.objects.CircleMapObject;
import arc.maps.objects.TextureMapObject;
import arc.maps.tiled.TiledMap;
import arc.util.Disposable;

/**
 * A generic level map implementation.</p>
 * <p>
 * A map has {@link MapProperties} which describe general attributes. Availability of properties depends on the type of map, e.g.
 * what format is was loaded from etc.</p>
 * <p>
 * A map has {@link MapLayers}. Map layers are ordered and indexed. A {@link MapLayer} contains {@link MapObjects} which represent
 * things within the layer. Different types of {@link MapObject} are available, e.g. {@link CircleMapObject},
 * {@link TextureMapObject}, and so on.</p>
 * <p>
 * A map can be rendered by a {@link MapRenderer}. A MapRenderer implementation may chose to only render specific MapObject or
 * MapLayer types.</p>
 * <p>
 * There are more specialized implementations of Map for specific use cases. e.g. the {@link TiledMap} class and its associated
 * classes add functionality specifically for tile maps on top of the basic map functionality.</p>
 * <p>
 * Maps must be disposed through a call to {@link #dispose()} when no longer used.
 */
public class Map implements Disposable{
    private MapLayers layers = new MapLayers();
    private MapProperties properties = new MapProperties();

    /** Creates empty map */
    public Map(){

    }

    /** @return the map's layers */
    public MapLayers getLayers(){
        return layers;
    }

    /** @return the map's properties */
    public MapProperties getProperties(){
        return properties;
    }

    /** Disposes all resources like {@link Texture} instances that the map may own. */
    @Override
    public void dispose(){
    }
}
