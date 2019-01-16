package top.ascension.libgdx.canyonbunny.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import top.ascension.libgdx.canyonbunny.debug.DbgMark;

public class FontsManager {
    private static final String TAG = FontsManager.class.getSimpleName( );
    // public static String ID_FONT_STENTIGA = "fonts/stentiga.ttf";
    public static String ID_FONT_FNT_ARIAL = "fonts/arial-15.fnt";
    public static String ID_FONT_TTF_PROTOTYPE = "fonts/Prototype.ttf";

    public static int SIZE_FONT_FPS = 120;

    // for DEBUG
    public boolean bUseFontTTF;
    public BitmapFont bmfUINorm;
    public FontGeomHolder fontGeomHolder;

    public FontsManager( ) {
        bUseFontTTF = true;    /// DEBUG !!!

        fontGeomHolder = new FontGeomHolder( );
    }

    /// after fonts loaded, then do this:
    public void extractFontsWhileLoaded( AssetManager assetManager ) {
        String idFont;
        idFont = bUseFontTTF ? ID_FONT_TTF_PROTOTYPE : ID_FONT_FNT_ARIAL;

        Gdx.app.log( TAG + DbgMark.ASSETS, "extractFontsWhileLoaded():"
                + "\t\t id:" + idFont
                + "\t\t type:" + assetManager.getAssetType( idFont )
                + "\t\t isLoaded:" + assetManager.isLoaded( idFont )
        );

        if ( assetManager.isLoaded( idFont )
                && assetManager.getAssetType( idFont ) == BitmapFont.class ) {
            bmfUINorm = assetManager.get( idFont, BitmapFont.class );

            if ( !bUseFontTTF ) {          /// fnt want scale something
                bmfUINorm.getData( ).setScale( 6f );
                // bmfUINorm.getRegion().getTexture().setFilter( Texture.TextureFilter.Linear, Texture.TextureFilter.Linear );
            }

            fontGeomHolder.recordGeom( bmfUINorm, idFont );
            return;
        }

        Gdx.app.log( TAG + DbgMark.ASSETS, "extractFontsWhileLoaded() --- font extract failed！" );
    }

    public void dispose( ) {
        fontGeomHolder.dispose( );
        fontGeomHolder = null;

        if ( bmfUINorm != null ) {
            bmfUINorm.dispose( );
            bmfUINorm = null;
        }
        Gdx.app.log( TAG + DbgMark.FLOW, "dispose() " );

    }

    public BitmapFont fetchFont( String fontId ) {
        // if( fontId.equals( ID_FONT_TTF_PROTOTYPE ) ){
        fontGeomHolder.adjustGeom( bmfUINorm, fontId );
        return bmfUINorm;
        // }
        // return null;
    }

    void loadFonts( AssetManager assetManager ) {

        if ( bUseFontTTF ) {
            /// async load fonts from ttf
            FileHandleResolver resolver = new InternalFileHandleResolver( );
            FreeTypeFontGenerator.setMaxTextureSize( FreeTypeFontGenerator.NO_MAXIMUM );
            assetManager.setLoader(
                    FreeTypeFontGenerator.class,
                    new FreeTypeFontGeneratorLoader( resolver )
            );
            assetManager.setLoader( BitmapFont.class, ".ttf", new FreetypeFontLoader( resolver ) );

            FreeTypeFontLoaderParameter parameter = new FreeTypeFontLoaderParameter( );
            parameter.fontFileName = ID_FONT_TTF_PROTOTYPE;
            parameter.fontParameters.flip = true;
            parameter.fontParameters.size = SIZE_FONT_FPS;
            // parameter.fontParameters.characters =
            //         "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.!'()>?: ";
            assetManager.load( ID_FONT_TTF_PROTOTYPE, BitmapFont.class, parameter );
        } else {
            BitmapFontLoader.BitmapFontParameter parameter = new BitmapFontLoader.BitmapFontParameter( );
            parameter.flip = true;
            assetManager.load( ID_FONT_FNT_ARIAL, BitmapFont.class, parameter );
        }
        Gdx.app.log( TAG + DbgMark.ASSETS, "loadFonts  assetManager.load started!  .  .  . " );
    }

}
