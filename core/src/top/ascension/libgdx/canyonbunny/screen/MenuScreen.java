package top.ascension.libgdx.canyonbunny.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import top.ascension.libgdx.canyonbunny.ScreenSwitchLeakTest;

public class MenuScreen extends AbstScreen {

    protected static final String TAG = MenuScreen.class.getSimpleName();
    private ScreenSwitchLeakTest ref_game;

    /// 绿色屏幕
    public MenuScreen( ScreenSwitchLeakTest game ) {
        this.ref_game = game;
        Gdx.input.setCatchBackKey( true );
    }

    @Override
    public void show( ) {
        super.show();

    }

    protected void readyRender( final float delta ) {
        Gdx.gl.glClearColor( 0xAA / 255f, 0x66 / 255f, 0x33 / 255f, 1f );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );
        if ( Gdx.input.isTouched() ) {
            /// 当前只有两个屏幕，当屏幕增多时，需要加入参数，因为可能希望跳转至不同的目标屏幕
            /// 当前菜单屏幕没有需要dispose的对象，因此直接跳转
            this.ref_game.leaveScreen( this );
        }
    }

    @Override
    public void resize( int width, int height ) {
        super.resize( width, height );
    }

    @Override
    public void hide( ) {
        Gdx.input.setCatchBackKey( false );
        super.hide();
    }

    @Override
    public void pause( ) {
        super.pause();
    }

    @Override
    public void resume( ) {
        super.resume();
    }

    @Override
    public void dispose( ) {
        super.dispose();
        this.ref_game = null;
    }
}
