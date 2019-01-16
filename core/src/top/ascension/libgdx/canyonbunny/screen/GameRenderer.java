package top.ascension.libgdx.canyonbunny.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import top.ascension.libgdx.canyonbunny.Layout;
import top.ascension.libgdx.canyonbunny.gamecell.IGameCell;
import top.ascension.libgdx.canyonbunny.ui.AssetsHolder;
import top.ascension.libgdx.canyonbunny.ui.FontsManager;

public class GameRenderer implements Disposable {
    private static final String TAG = GameRenderer.class.getSimpleName( );

    private IGameCell ref_gameCell;
    private GameController ref_gameController;

    private SpriteBatch batch;
    private Texture img;

    public GameRenderer( IGameCell gameCell, GameController gameController ) {
        init( gameCell, gameController );
    }

    private void init( IGameCell gameCell, GameController gameController ) {
        this.ref_gameCell = gameCell;
        this.ref_gameController = gameController;

        batch = new SpriteBatch( );
        img = new Texture( "libgdx.png" );
    }

    public void render( ) {

        Gdx.gl.glClearColor( 0x33 / 255f, 0x88 / 255f, 0x2a / 255f, 0x99 / 255f );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );

        /// render gameHost cell
        batch.setProjectionMatrix( this.ref_gameController.cmrGame.combined );
        batch.begin( );
        ref_gameCell.render( batch );
        batch.end( );

        /// render GUI
        batch.setProjectionMatrix( this.ref_gameController.cmrGUI.combined );
        batch.begin( );
        renderFPS( batch );
        batch.end( );
    }

    private void renderFPS( SpriteBatch batch ) {
        float x = Layout.SCREEN_W - FontsManager.SIZE_FONT_FPS * 3.6f - 20;
        float y = Layout.SCREEN_H - FontsManager.SIZE_FONT_FPS - 20;
        int iFPS = Gdx.graphics.getFramesPerSecond( );

        String fontId = AssetsHolder.i().fontsManager.bUseFontTTF ?
                FontsManager.ID_FONT_TTF_PROTOTYPE : FontsManager.ID_FONT_FNT_ARIAL;
        BitmapFont fontFPS = AssetsHolder.i().fontsManager.fetchFont( fontId );

        if ( iFPS >= 45 ) fontFPS.setColor( 0, 1, 0, 1 );
        else if ( iFPS >= 30 ) fontFPS.setColor( .6f, .6f, .3f, 1 );
        else fontFPS.setColor( 1, 0, 0, 1 );
        fontFPS.draw( batch, "FPS: " + iFPS, x, y );
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose( ) {
        this.ref_gameCell = null;
        this.ref_gameController = null;
        batch.dispose( );
        img.dispose( );
    }
}
