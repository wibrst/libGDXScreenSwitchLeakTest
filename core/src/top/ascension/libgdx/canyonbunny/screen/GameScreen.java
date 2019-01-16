package top.ascension.libgdx.canyonbunny.screen;

import top.ascension.libgdx.canyonbunny.ScreenSwitchLeakTest;
import top.ascension.libgdx.canyonbunny.gamecell.FreeXBlocks;
import top.ascension.libgdx.canyonbunny.gamecell.IGameCell;

public class GameScreen extends AbstScreen {

    protected static final String TAG = GameScreen.class.getSimpleName( );
    private GameController gmctllr;

    private ScreenSwitchLeakTest ref_game;
    private GameRenderer gmrdrr;
    private boolean bRunning;

    GameScreen( ScreenSwitchLeakTest game ){
        this.ref_game = game;
    }

    @Override
    public void show( ) {
        super.show();

        /// current using
        IGameCell gameCell = new FreeXBlocks();

        gmctllr = new GameController( gameCell, this );
        gmrdrr = new GameRenderer( gameCell, gmctllr );

        bRunning = true;
    }

    public void leaveScreen( ) {
        this.ref_game.leaveScreen( this );
        this.ref_game = null;
    }

    @Override
    protected void readyRender( float delta ) {
        if ( bRunning ) gmctllr.updateForRender( delta );
        gmrdrr.render( );
    }

    @Override
    public void resize( int width, int height ) {
        super.resize( width, height );
        gmctllr.resize( width, height );
    }

    @Override
    public void pause( ) {
        super.pause();
        bRunning = false;
    }

    @Override
    public void resume( ) {
        super.resume();
        bRunning = true;
    }

    @Override
    public void hide( ) {
        super.hide();
        gmrdrr.dispose( );
    }

    @Override
    public void dispose( ) {
        this.ref_game = null;
        super.dispose( );
    }
}
