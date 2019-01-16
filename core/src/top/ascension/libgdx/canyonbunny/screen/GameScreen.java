package top.ascension.libgdx.canyonbunny.screen;

import top.ascension.libgdx.canyonbunny.GlobalRef;
import top.ascension.libgdx.canyonbunny.gamecell.FreeXBlocks;
import top.ascension.libgdx.canyonbunny.gamecell.IGameCell;

public class GameScreen extends AbstScreen {

    protected static final String TAG = GameScreen.class.getSimpleName( );

    private GameRenderer gmrdrr;
    private boolean bRunning;

    @Override
    public void show( ) {
        super.show();

        /// current using
        IGameCell gameCell = new FreeXBlocks();

        gmrdrr = new GameRenderer( gameCell );
        GlobalRef.gmctllr = new GameController( gameCell );

        bRunning = true;
    }

    @Override
    protected void readyRender( float delta ) {
        if ( bRunning ) GlobalRef.gmctllr.updateForRender( delta );
        gmrdrr.render( );
    }

    @Override
    public void resize( int width, int height ) {
        super.resize( width, height );
        GlobalRef.gmctllr.resize( width, height );
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
        super.dispose( );
    }
}
