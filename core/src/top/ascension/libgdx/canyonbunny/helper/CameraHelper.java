package top.ascension.libgdx.canyonbunny.helper;

import aurelienribon.tweenengine.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import top.ascension.libgdx.canyonbunny.screen.GameController;

public class CameraHelper {
    private static final String TAG = GameController.class.getSimpleName( );

    public boolean bMoving;

    public CameraHelper( ) {
        Tween.registerAccessor( OrthographicCamera.class, new CameraTweenAccessor( ) );
        reset();
    }

    public void reset( ) {
        bMoving = false;
    }

    public void refocusCamera( float x, float y, OrthographicCamera cmr, TweenManager tweenManager ) {
        bMoving = true;

        tweenManager.killTarget( cmr );
        Tween.to( cmr, CameraTweenAccessor.POS_XY, 0.6f )
                .target( x, y )
                .ease( TweenEquations.easeOutQuad )
                .start( tweenManager )
                .setCallbackTriggers( TweenCallback.COMPLETE )
                .setCallback( this::twCompHandler );
    }

    private void twCompHandler( int type, BaseTween< ? > source ){
        bMoving = false;
    }

}
