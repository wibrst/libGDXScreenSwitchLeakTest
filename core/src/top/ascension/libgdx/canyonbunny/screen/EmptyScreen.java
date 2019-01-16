package top.ascension.libgdx.canyonbunny.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import top.ascension.libgdx.canyonbunny.ScreenSwitchLeakTest;

/// 准白屏幕
public class EmptyScreen extends AbstScreen {

    private ScreenSwitchLeakTest ref_game;

    public EmptyScreen( ScreenSwitchLeakTest game ) {
        this.ref_game = game;
    }


    @Override
    protected void readyRender( float delta ) {
        Gdx.gl.glClearColor( 0.9f, 0.9f, 0.9f, 1f );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );
        if ( Gdx.input.isTouched() ) {
            this.ref_game.leaveScreen( this );
        }
    }

    @Override
    public void dispose( ) {
        this.ref_game = null;
        super.dispose();
    }
}
