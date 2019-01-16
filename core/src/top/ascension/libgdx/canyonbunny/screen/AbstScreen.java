package top.ascension.libgdx.canyonbunny.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import top.ascension.libgdx.canyonbunny.debug.DbgMark;
import top.ascension.libgdx.canyonbunny.ui.AssetsHolder;

public abstract class AbstScreen implements Screen {

    private static final String TAG = AbstScreen.class.getSimpleName( );
    protected int iScreenPresentPhase;

    public AbstScreen( ) {
    }

    public void show( ) {

        Gdx.app.log( TAG + DbgMark.FLOW, "show()! "
                + this.getClass( ).getSimpleName( )
                + "================================================"
        );
        initFromScratch();
    }

    private void initFromScratch( ) {
        iScreenPresentPhase = ScreenPresentPhase.ASSETS_WANTED;
        AssetsHolder.i().init( new AssetManager( ) );
    }

    public void render( float delta ) {

        switch ( iScreenPresentPhase ) {
            case ScreenPresentPhase.ASSETS_WANTED:
                if ( AssetsHolder.i().checkLoadReady( ) ) {  /// 需要轮询中，命中：切换状态

                    /// 将所需资源引用保存
                    Gdx.app.log( TAG + DbgMark.FLOW, "render()! "
                            + "--- " + this.getClass( ).getSimpleName( )
                            + " 轮询，mgr.update命中：切换状态! " );

                    AssetsHolder.i().extractAssetsWhileLoaded( );

                    iScreenPresentPhase = ScreenPresentPhase.ASSETS_READY;
                } else {    /// 仍在需要中
                    Gdx.gl.glClearColor( 0f, 0f, 0f, 1f );
                    Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );
                    float progress = AssetsHolder.i().getProgress( );
                    Gdx.app.log( TAG + DbgMark.ASSETS, "render()!loading:" + progress
                            + " --- " + this.getClass( ).getSimpleName( ) );
                    break;
                }
            case ScreenPresentPhase.ASSETS_READY:
                readyRender( delta );
                break;
        }
    }

    protected abstract void readyRender( float delta );

    public void resize( int width, int height ) {
        Gdx.app.log( TAG + DbgMark.FLOW, "resize()! -->" + this.getClass( ).getSimpleName( ) );
    }

    public void pause( ) {
        Gdx.app.log( TAG + DbgMark.FLOW, "pause()! -->" + this.getClass( ).getSimpleName( ) );
    }

    public void resume( ) {
        Gdx.app.log( TAG + DbgMark.FLOW, "resume()! -->" + this.getClass( ).getSimpleName( ) );
        // initFromScratch();   //// 解决纹理丢失的所在，但是加了代码，注释又正常了，待再发现！
    }

    public void hide( ) {
        Gdx.app.log( TAG + DbgMark.FLOW, "hide()! <->" + this.getClass( ).getSimpleName( ) );

    }

    public void dispose( ) {
        AssetsHolder.i().dispose( );
        Gdx.app.log( TAG + DbgMark.FLOW, "dispose()! "
                + this.getClass( ).getSimpleName( )
                + "---------------------------------------------------------"
        );
    }
}
