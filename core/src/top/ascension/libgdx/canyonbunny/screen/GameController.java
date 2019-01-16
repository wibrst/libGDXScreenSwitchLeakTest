package top.ascension.libgdx.canyonbunny.screen;

import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import top.ascension.libgdx.canyonbunny.GlobalRef;
import top.ascension.libgdx.canyonbunny.Layout;
import top.ascension.libgdx.canyonbunny.debug.DbgMark;
import top.ascension.libgdx.canyonbunny.gamecell.IGameCell;
import top.ascension.libgdx.canyonbunny.helper.CameraHelper;
import top.ascension.libgdx.canyonbunny.helper.IntervalCenter;

public class GameController extends InputAdapter {
    private static final String TAG = GameController.class.getSimpleName();

    private static final String ID_BACK_INTERVAL = "ID_BACK_INTERVAL";

    private TweenManager twnmgr;
    OrthographicCamera cmrGame;
    OrthographicCamera cmrGUI;
    private CameraHelper cmrHpr;

    private IGameCell gameCell; /// full control, include dispose

    public GameController( IGameCell gameCell ) {
        initAndReset( gameCell );
    }

    private void initAndReset( IGameCell gameCell ) {

        Gdx.input.setCatchBackKey( true );    /// 防止默认的返回行为，即系统焦点返回当前Activity之前的其他活动
        Gdx.input.setInputProcessor( this );

        IntervalCenter.i().register( 200, ID_BACK_INTERVAL );

        if ( twnmgr != null ) {
            twnmgr.killAll();
        }
        twnmgr = new TweenManager();

        if ( cmrGame == null )
            cmrGame = new OrthographicCamera( Layout.VP_W, Layout.VP_H );
        cmrGame.position.set( 0, 0, 0 );
        cmrGame.update();

        if ( cmrGUI == null )
            cmrGUI = new OrthographicCamera( Layout.VP_W, Layout.VP_H );
        cmrGUI.position.set( 0, 0, 0 );
        cmrGUI.setToOrtho( true );

        if ( cmrHpr == null ) {
            cmrHpr = new CameraHelper();
        } else {
            cmrHpr.reset();
        }

        this.gameCell = gameCell;
        this.gameCell.init( cmrHpr, cmrGame, twnmgr );
    }

    private void uninit( ) {
        IntervalCenter.i().dispose();
        Gdx.input.setCatchBackKey( false );
        Gdx.input.setInputProcessor( null );
        twnmgr.pause();
        twnmgr.killAll();
        twnmgr = null;
        cmrGame = null;
        cmrGUI = null;
        gameCell.uninit();
        gameCell = null;

        Gdx.app.log( TAG + DbgMark.FLOW,
                "uninit() IntervalCenter.checkSingletonNull:" + IntervalCenter.checkSingletonNull()
        );
    }

    @Override
    public boolean keyUp( int keycode ) {
        switch ( keycode ) {
            case Keys.MENU:
                Gdx.app.log( TAG + DbgMark.INTERACT, "MENU Key Triggered!" );
                leaveScreen();
                return true;
            case Keys.BACK:
                if ( IntervalCenter.i().check( ID_BACK_INTERVAL ) ) {
                    Gdx.app.log( TAG + DbgMark.INTERACT, "DOUBLE-BACK Key Triggered!" );
                    leaveScreen();
                } else
                    gameCell.backBehavior();
                return true;
        }
        return false;
    }

    private void leaveScreen( ) {
        uninit();

        Gdx.app.log( TAG + DbgMark.FLOW, "leaveScreen() " );
        GlobalRef.gameHost.leaveScreen();
    }


    /// 处理游戏逻辑
    public void updateForRender( float tmDelta ) {
        // handleDebugInput(tmDelta);   /// 书上有，但按键为上下左右，需要PC平台
        if ( cmrHpr.bMoving ) {
            twnmgr.update( tmDelta );
            cmrGame.update();
        }
        gameCell.updateForRender( tmDelta );
    }

    public void resize( int w, int h ) {
        cmrGame.viewportWidth = ( Layout.VP_H / h ) * w;
        Gdx.app.log( TAG + DbgMark.FLOW,
                "resize w,h,VP_H,viewportWidth:" + w + "," + h + "," + Layout.VP_H + "," + cmrGame.viewportWidth );
        cmrGame.update();

        cmrGUI.viewportHeight = Layout.SCREEN_H;
        cmrGUI.viewportWidth = Layout.SCREEN_W;
        cmrGUI.position.set( cmrGUI.viewportWidth / 2, cmrGUI.viewportHeight / 2, 0 );
        cmrGUI.update();
    }

}
