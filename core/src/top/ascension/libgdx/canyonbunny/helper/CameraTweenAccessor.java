package top.ascension.libgdx.canyonbunny.helper;

import aurelienribon.tweenengine.TweenAccessor;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class CameraTweenAccessor implements TweenAccessor< OrthographicCamera > {

    public static final int POS_XY = 0;

    @Override
    public int getValues( OrthographicCamera target, int tweenType, float[] returnValues ) {
        switch ( tweenType ){
            case POS_XY:
                returnValues[0] = target.position.x;
                returnValues[1] = target.position.y;
                return 2;
        }
        return 0;
    }

    @Override
    public void setValues( OrthographicCamera target, int tweenType, float[] newValues ) {
        switch ( tweenType ){
            case POS_XY:
                target.position.x = newValues[0];
                target.position.y = newValues[1];
        }
    }
}
