package top.ascension.libgdx.canyonbunny.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

import java.util.WeakHashMap;

/// hack the display region dirty bug,but not effect for TTF!
public class FontGeomHolder {

    WeakHashMap fontGeomMap;

    public FontGeomHolder( ) {
        fontGeomMap = new WeakHashMap<>( );
    }

    public void dispose( ) {
        fontGeomMap.clear( );
    }

    public void recordGeom( BitmapFont bitmapFont, String bitmapFontId ) {
        BitmapFont.BitmapFontData data = bitmapFont.getData( );

        fontGeomMap.put( bitmapFontId, new float[]{
                data.lineHeight,
                data.spaceXadvance,
                data.xHeight,
                data.capHeight,
                data.ascent,
                data.descent,
                data.down,
                data.scaleX,
                data.scaleY
        } );
    }

    // Call this at start of each frame.
    public void adjustGeom( BitmapFont bitmapFont, String bitmapFontId ) {

        float[] props = ( float[] ) fontGeomMap.get( bitmapFontId );

        if ( props != null && props.length >= 9 ) {
            BitmapFont.BitmapFontData data = bitmapFont.getData( );
            data.lineHeight = props[0];
            data.spaceXadvance = props[1];
            data.xHeight = props[2];
            data.capHeight = props[3];
            data.ascent = props[4];
            data.descent = props[5];
            data.down = props[6];
            data.scaleX = props[7];
            data.scaleY = props[8];
        }
    }

    // private float lineHeight;
    // private float spaceXadvance;
    // private float xHeight;
    // private float capHeight;
    // private float ascent;
    // private float descent;
    // private float down;
    // private float scaleX;
    // private float scaleY;
}

