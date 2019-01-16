package top.ascension.libgdx.canyonbunny;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import top.ascension.libgdx.canyonbunny.helper.ILeakCanaryHolder;
import top.ascension.libgdx.canyonbunny.screen.EmptyScreen;
import top.ascension.libgdx.canyonbunny.screen.MenuScreen;
import top.ascension.libgdx.canyonbunny.status.LeaveStatus;
import top.ascension.libgdx.canyonbunny.status.ScreenList;

public class ScreenSwitchLeakTest extends Game {

    private static final String TAG = ScreenSwitchLeakTest.class.getSimpleName();

    private int _iCurrScreen;

    private boolean _bEmptyTest;

    public ScreenSwitchLeakTest( ILeakCanaryHolder leakCanaryHolder ) {
        GlobalRef.lch = leakCanaryHolder;

        _bEmptyTest = true;
        _iCurrScreen = ScreenList.VERY_BEGIN;
    }

    @Override
    public void create( ) {

        Gdx.app.setLogLevel( Application.LOG_INFO );
        // Gdx.app.d

        Layout.SCREEN_H = Gdx.graphics.getHeight();
        Layout.SCREEN_W = Gdx.graphics.getWidth();
        GlobalRef.gameHost = this;

        // SamplingProfilerFacade.init( 10, 1000, Thread.currentThread());
        // SamplingProfilerFacade.startSampling();

        this.leaveScreen( null );
    }

    public void leaveScreen( Screen screen ) {
        if ( screen != null )
            screen.dispose();
        /// GameScreen switch flow: VERY_BEGIN -> MENU_SCREEN -> EMPTY_SCREEN
        /// then roll back: MENU_SCREEN <--> EMPTY_SCREEN
        /// switch code is same at two place : Gdx.input.isTouched(),
        /// respectively in class MenuScreen and EmptyScreen.
        switch ( _iCurrScreen ) {
            case ScreenList.VERY_BEGIN:
            case ScreenList.EMPTY_SCREEN:    /// 空测试
                _iCurrScreen = ScreenList.MENU_SCREEN;
                setScreen( new MenuScreen() );
                break;

            case ScreenList.MENU_SCREEN:
                _iCurrScreen = ScreenList.EMPTY_SCREEN;
                setScreen( new EmptyScreen() );
                break;
        }
    }
}
