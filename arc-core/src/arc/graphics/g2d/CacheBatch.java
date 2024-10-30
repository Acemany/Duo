package arc.graphics.g2d;

import arc.graphics.Color;
import arc.graphics.Texture;
import arc.graphics.glutils.Shader;
import arc.math.Matrix3;

public class CacheBatch extends SpriteBatch{
    SpriteCache cache;

    public CacheBatch(int size){
        this(new SpriteCache(size, false));
    }

    public CacheBatch(SpriteCache cache){
        super(0);
        this.cache = cache;
    }

    @Override
    public void flush(){
        //does nothing, since flushing like this isn't needed
    }

    @Override
    public void setColor(Color tint){
        cache.setColor(tint);
    }

    @Override
    void setColor(float r, float g, float b, float a){
        cache.setColor(r, g, b, a);
    }

    @Override
    public void setPackedColor(float color){
        cache.setPackedColor(color);
    }

    @Override
    public Color getColor(){
        return cache.getColor();
    }

    @Override
    public float getPackedColor(){
        return cache.getColor().toFloatBits();
    }

    @Override
    public void setProjection(Matrix3 projection){
        cache.setProjectionMatrix(projection);
    }

    public void beginCache(){
        cache.beginCache();
    }

    public int endCache(){
        return cache.endCache();
    }

    @Override
    protected void draw(Texture texture, float[] spriteVertices, int offset, int count){
        cache.add(texture, spriteVertices, offset, count);
    }

    @Override
    protected void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height, float rotation){
        cache.add(region, x, y, originX, originY, width, height, 1f, 1f, rotation);
    }

    @Override
    void setShader(Shader shader){
        setShader(shader, true);
    }

    @Override
    void setShader(Shader shader, boolean apply){
        boolean drawing = cache.isDrawing();

        if(drawing) cache.end();
        cache.setShader(shader);
        if(drawing) cache.begin();
        if(apply && shader != null) shader.apply();
    }

    public void beginDraw(){
        cache.begin();
    }

    public void endDraw(){
        cache.end();
    }

    public void drawCache(int id){
        cache.draw(id);
    }
}
