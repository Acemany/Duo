package arc.maps.tiled.objects;

import arc.graphics.g2d.TextureRegion;
import arc.maps.MapObject;
import arc.maps.objects.TextureMapObject;
import arc.maps.tiled.TiledMapTile;
import arc.maps.tiled.tiles.AnimatedTiledMapTile;
import arc.maps.tiled.tiles.StaticTiledMapTile;

/**
 * A {@link MapObject} with a {@link TiledMapTile}. Can be both {@link StaticTiledMapTile} or {@link AnimatedTiledMapTile}. For
 * compatibility reasons, this extends {@link TextureMapObject}. Use {@link TiledMapTile#getTextureRegion()} instead of
 * {@link #getTextureRegion()}.
 * @author Daniel Holderbaum
 */
public class TiledMapTileMapObject extends TextureMapObject{

    private boolean flipHorizontally;
    private boolean flipVertically;

    private TiledMapTile tile;

    public TiledMapTileMapObject(TiledMapTile tile, boolean flipHorizontally, boolean flipVertically){
        this.flipHorizontally = flipHorizontally;
        this.flipVertically = flipVertically;
        this.tile = tile;

        TextureRegion textureRegion = new TextureRegion(tile.getTextureRegion());
        textureRegion.flip(flipHorizontally, flipVertically);
        setTextureRegion(textureRegion);
    }

    public boolean isFlipHorizontally(){
        return flipHorizontally;
    }

    public void setFlipHorizontally(boolean flipHorizontally){
        this.flipHorizontally = flipHorizontally;
    }

    public boolean isFlipVertically(){
        return flipVertically;
    }

    public void setFlipVertically(boolean flipVertically){
        this.flipVertically = flipVertically;
    }

    public TiledMapTile getTile(){
        return tile;
    }

    public void setTile(TiledMapTile tile){
        this.tile = tile;
    }

}
