package top.ascension.libgdx.canyonbunny.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Disposable;
import top.ascension.libgdx.canyonbunny.ScreenSwitchLeakTest;
import top.ascension.libgdx.canyonbunny.debug.DbgMark;

import java.lang.ref.WeakReference;

public class AssetsHolder implements Disposable, AssetErrorListener {

    static AssetsHolder _i;

    public static AssetsHolder i( ) {
        if ( _i == null )
            _i = new AssetsHolder( );
        return _i;
    }

    private static final String TAG = AssetsHolder.class.getSimpleName( );
    public FontsManager fontsManager;
    private AssetManager assetManager;

    public void init( AssetManager assetManager ) {
        this.assetManager = assetManager;
        assetManager.setErrorListener( this );

        fontsManager = new FontsManager( );
        fontsManager.loadFonts( assetManager );
        Gdx.app.log( TAG + DbgMark.ASSETS, "init() ---  assetManager.getDiagnostics:" +
                "\n\t" + this.assetManager.getDiagnostics( ) );
    }

    public void extractAssetsWhileLoaded( ) {
        Gdx.app.log( TAG + DbgMark.ASSETS, "extractAssetsWhileLoaded() ---  assetManager.getDiagnostics:" +
                "\n\t" + assetManager.getDiagnostics( ) );
        fontsManager.extractFontsWhileLoaded( assetManager );
    }

    /// service
    public float getProgress( ) {
        return assetManager.getProgress( );
    }

    public boolean checkLoadReady( ) {
        return assetManager.update( );
    }

    @Override
    public void error( AssetDescriptor asset, Throwable throwable ) {
        Gdx.app.error( TAG + DbgMark.ERROR, asset.fileName + " load err: " + throwable.toString( ) );
    }

    @Override
    public void dispose( ) {
        assetManager.clear();
        assetManager.dispose( );
        // fontsManager.dispose( ); // Pixmap already disposed!
        _i = null;
        Gdx.app.log( TAG + DbgMark.FLOW, "dispose() ---  _i =" + _i );
    }
}