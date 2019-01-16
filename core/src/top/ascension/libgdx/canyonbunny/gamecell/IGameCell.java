package top.ascension.libgdx.canyonbunny.gamecell;

import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import top.ascension.libgdx.canyonbunny.helper.CameraHelper;

public interface IGameCell {

    void init( CameraHelper cmrHpr, OrthographicCamera cmrGame,
                       TweenManager twnmgr );

    void uninit( );

    void updateForRender( float tmDelta );

    void render( SpriteBatch batch );

    void backBehavior( );

}
