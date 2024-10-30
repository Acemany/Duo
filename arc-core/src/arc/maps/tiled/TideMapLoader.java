package arc.maps.tiled;

import arc.assets.AssetDescriptor;
import arc.assets.AssetLoaderParameters;
import arc.assets.AssetManager;
import arc.assets.loaders.FileHandleResolver;
import arc.assets.loaders.SynchronousAssetLoader;
import arc.assets.loaders.resolvers.InternalFileHandleResolver;
import arc.collection.Array;
import arc.collection.ObjectMap;
import arc.files.FileHandle;
import arc.graphics.Texture;
import arc.graphics.g2d.TextureRegion;
import arc.maps.ImageResolver;
import arc.maps.ImageResolver.AssetManagerImageResolver;
import arc.maps.ImageResolver.DirectImageResolver;
import arc.maps.MapProperties;
import arc.maps.tiled.TiledMapTileLayer.Cell;
import arc.maps.tiled.tiles.AnimatedTiledMapTile;
import arc.maps.tiled.tiles.StaticTiledMapTile;
import arc.util.ArcRuntimeException;
import arc.util.serialization.XmlReader;
import arc.util.serialization.XmlReader.Element;

import java.io.IOException;
import java.util.StringTokenizer;

public class TideMapLoader extends SynchronousAssetLoader<TiledMap, TideMapLoader.Parameters>{

    private XmlReader xml = new XmlReader();
    private Element root;
    public TideMapLoader(){
        super(new InternalFileHandleResolver());
    }

    public TideMapLoader(FileHandleResolver resolver){
        super(resolver);
    }

    private static FileHandle getRelativeFileHandle(FileHandle file, String path){
        StringTokenizer tokenizer = new StringTokenizer(path, "\\/");
        FileHandle result = file.parent();
        while(tokenizer.hasMoreElements()){
            String token = tokenizer.nextToken();
            if(token.equals(".."))
                result = result.parent();
            else{
                result = result.child(token);
            }
        }
        return result;
    }

    public TiledMap load(String fileName){
        try{
            FileHandle tideFile = resolve(fileName);
            root = xml.parse(tideFile);
            ObjectMap<String, Texture> textures = new ObjectMap<>();
            for(FileHandle textureFile : loadTileSheets(root, tideFile)){
                textures.put(textureFile.path(), new Texture(textureFile));
            }
            DirectImageResolver imageResolver = new DirectImageResolver(textures);
            TiledMap map = loadMap(root, tideFile, imageResolver);
            map.setOwnedResources(textures.values().toArray());
            return map;
        }catch(IOException e){
            throw new ArcRuntimeException("Couldn't load tilemap '" + fileName + "'", e);
        }

    }

    @Override
    public TiledMap load(AssetManager assetManager, String fileName, FileHandle tideFile, Parameters parameter){
        try{
            return loadMap(root, tideFile, new AssetManagerImageResolver(assetManager));
        }catch(Exception e){
            throw new ArcRuntimeException("Couldn't load tilemap '" + fileName + "'", e);
        }
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle tmxFile, Parameters parameter){
        Array<AssetDescriptor> dependencies = new Array<>();
        try{
            root = xml.parse(tmxFile);
            for(FileHandle image : loadTileSheets(root, tmxFile)){
                dependencies.add(new AssetDescriptor(image.path(), Texture.class));
            }
            return dependencies;
        }catch(IOException e){
            throw new ArcRuntimeException("Couldn't load tilemap '" + fileName + "'", e);
        }
    }

    /**
     * Loads the map data, given the XML root element and an {@link ImageResolver} used to return the tileset Textures
     * @param root the XML root element
     * @param tmxFile the Filehandle of the tmx file
     * @param imageResolver the {@link ImageResolver}
     * @return the {@link TiledMap}
     */
    private TiledMap loadMap(Element root, FileHandle tmxFile, ImageResolver imageResolver){
        TiledMap map = new TiledMap();
        Element properties = root.getChildByName("Properties");
        if(properties != null){
            loadProperties(map.getProperties(), properties);
        }
        Element tilesheets = root.getChildByName("TileSheets");
        for(Element tilesheet : tilesheets.getChildrenByName("TileSheet")){
            loadTileSheet(map, tilesheet, tmxFile, imageResolver);
        }
        Element layers = root.getChildByName("Layers");
        for(Element layer : layers.getChildrenByName("Layer")){
            loadLayer(map, layer);
        }
        return map;
    }

    /**
     * Loads the tilesets
     * @param root the root XML element
     * @return a list of filenames for images containing tiles
     */
    private Array<FileHandle> loadTileSheets(Element root, FileHandle tideFile) throws IOException{
        Array<FileHandle> images = new Array<>();
        Element tilesheets = root.getChildByName("TileSheets");
        for(Element tileset : tilesheets.getChildrenByName("TileSheet")){
            Element imageSource = tileset.getChildByName("ImageSource");
            FileHandle image = getRelativeFileHandle(tideFile, imageSource.getText());
            images.add(image);
        }
        return images;
    }

    private void loadTileSheet(TiledMap map, Element element, FileHandle tideFile, ImageResolver imageResolver){
        if(element.getName().equals("TileSheet")){
            String id = element.getAttribute("Id");
            String description = element.getChildByName("Description").getText();
            String imageSource = element.getChildByName("ImageSource").getText();

            Element alignment = element.getChildByName("Alignment");
            String sheetSize = alignment.getAttribute("SheetSize");
            String tileSize = alignment.getAttribute("TileSize");
            String margin = alignment.getAttribute("Margin");
            String spacing = alignment.getAttribute("Spacing");

            String[] sheetSizeParts = sheetSize.split(" x ");
            int sheetSizeX = Integer.parseInt(sheetSizeParts[0]);
            int sheetSizeY = Integer.parseInt(sheetSizeParts[1]);

            String[] tileSizeParts = tileSize.split(" x ");
            int tileSizeX = Integer.parseInt(tileSizeParts[0]);
            int tileSizeY = Integer.parseInt(tileSizeParts[1]);

            String[] marginParts = margin.split(" x ");
            int marginX = Integer.parseInt(marginParts[0]);
            int marginY = Integer.parseInt(marginParts[1]);

            String[] spacingParts = margin.split(" x ");
            int spacingX = Integer.parseInt(spacingParts[0]);
            int spacingY = Integer.parseInt(spacingParts[1]);

            FileHandle image = getRelativeFileHandle(tideFile, imageSource);
            TextureRegion texture = imageResolver.getImage(image.path());

            TiledMapTileSets tilesets = map.getTileSets();
            int firstgid = 1;
            for(TiledMapTileSet tileset : tilesets){
                firstgid += tileset.size();
            }

            TiledMapTileSet tileset = new TiledMapTileSet();
            tileset.setName(id);
            tileset.getProperties().put("firstgid", firstgid);
            int gid = firstgid;

            int stopWidth = texture.getWidth() - tileSizeX;
            int stopHeight = texture.getHeight() - tileSizeY;

            for(int y = marginY; y <= stopHeight; y += tileSizeY + spacingY){
                for(int x = marginX; x <= stopWidth; x += tileSizeX + spacingX){
                    TiledMapTile tile = new StaticTiledMapTile(new TextureRegion(texture, x, y, tileSizeX, tileSizeY));
                    tile.setId(gid);
                    tileset.putTile(gid++, tile);
                }
            }

            Element properties = element.getChildByName("Properties");
            if(properties != null){
                loadProperties(tileset.getProperties(), properties);
            }

            tilesets.addTileSet(tileset);
        }
    }

    private void loadLayer(TiledMap map, Element element){
        if(element.getName().equals("Layer")){
            String id = element.getAttribute("Id");
            String visible = element.getAttribute("Visible");

            Element dimensions = element.getChildByName("Dimensions");
            String layerSize = dimensions.getAttribute("LayerSize");
            String tileSize = dimensions.getAttribute("TileSize");

            String[] layerSizeParts = layerSize.split(" x ");
            int layerSizeX = Integer.parseInt(layerSizeParts[0]);
            int layerSizeY = Integer.parseInt(layerSizeParts[1]);

            String[] tileSizeParts = tileSize.split(" x ");
            int tileSizeX = Integer.parseInt(tileSizeParts[0]);
            int tileSizeY = Integer.parseInt(tileSizeParts[1]);

            TiledMapTileLayer layer = new TiledMapTileLayer(layerSizeX, layerSizeY, tileSizeX, tileSizeY);
            layer.setName(id);
            layer.setVisible(visible.equalsIgnoreCase("True"));
            Element tileArray = element.getChildByName("TileArray");
            Array<Element> rows = tileArray.getChildrenByName("Row");
            TiledMapTileSets tilesets = map.getTileSets();
            TiledMapTileSet currentTileSet = null;
            int firstgid = 0;
            int x, y;
            for(int row = 0, rowCount = rows.size; row < rowCount; row++){
                Element currentRow = rows.get(row);
                y = rowCount - 1 - row;
                x = 0;
                for(int child = 0, childCount = currentRow.getChildCount(); child < childCount; child++){
                    Element currentChild = currentRow.getChild(child);
                    String name = currentChild.getName();
                    if(name.equals("TileSheet")){
                        currentTileSet = tilesets.getTileSet(currentChild.getAttribute("Ref"));
                        firstgid = currentTileSet.getProperties().get("firstgid");
                    }else if(name.equals("Null")){
                        x += currentChild.getIntAttribute("Count");
                    }else if(name.equals("Static")){
                        Cell cell = new Cell();
                        cell.setTile(currentTileSet.getTile(firstgid + currentChild.getIntAttribute("Index")));
                        layer.setCell(x++, y, cell);
                    }else if(name.equals("Animated")){
                        // Create an AnimatedTile
                        int interval = currentChild.getInt("Interval");
                        Element frames = currentChild.getChildByName("Frames");
                        Array<StaticTiledMapTile> frameTiles = new Array<>();
                        for(int frameChild = 0, frameChildCount = frames.getChildCount(); frameChild < frameChildCount; frameChild++){
                            Element frame = frames.getChild(frameChild);
                            String frameName = frame.getName();
                            if(frameName.equals("TileSheet")){
                                currentTileSet = tilesets.getTileSet(frame.getAttribute("Ref"));
                                firstgid = currentTileSet.getProperties().get("firstgid");
                            }else if(frameName.equals("Static")){
                                frameTiles.add((StaticTiledMapTile)currentTileSet.getTile(firstgid + frame.getIntAttribute("Index")));
                            }
                        }
                        Cell cell = new Cell();
                        cell.setTile(new AnimatedTiledMapTile(interval / 1000f, frameTiles));
                        layer.setCell(x++, y, cell); // TODO: Reuse existing animated tiles
                    }
                }
            }

            Element properties = element.getChildByName("Properties");
            if(properties != null){
                loadProperties(layer.getProperties(), properties);
            }

            map.getLayers().add(layer);
        }
    }

    private void loadProperties(MapProperties properties, Element element){
        if(element.getName().equals("Properties")){
            for(Element property : element.getChildrenByName("Property")){
                String key = property.getAttribute("Key", null);
                String type = property.getAttribute("Type", null);
                String value = property.getText();

                if(type.equals("Int32")){
                    properties.put(key, Integer.parseInt(value));
                }else if(type.equals("String")){
                    properties.put(key, value);
                }else if(type.equals("Boolean")){
                    properties.put(key, value.equalsIgnoreCase("true"));
                }else{
                    properties.put(key, value);
                }
            }
        }
    }

    public static class Parameters extends AssetLoaderParameters<TiledMap>{

    }

}