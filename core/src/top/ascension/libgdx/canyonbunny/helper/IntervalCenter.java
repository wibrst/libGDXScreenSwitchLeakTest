package top.ascension.libgdx.canyonbunny.helper;

import com.badlogic.gdx.Gdx;
import top.ascension.libgdx.canyonbunny.debug.DbgMark;

import java.util.WeakHashMap;

public class IntervalCenter {
    protected static final String TAG = IntervalCenter.class.getSimpleName( );

    private WeakHashMap< String, IntervalChecker > map;

    public static IntervalCenter i( ) {
        if ( _i == null )
            _i = new IntervalCenter( );
        return _i;
    }

    private static IntervalCenter _i;

    private IntervalCenter( ) {
        map = new WeakHashMap<>( );
    }

    public void register( long interval, String id ) {
        map.put( id, new IntervalChecker( interval ) );
        Gdx.app.log( TAG + DbgMark.LOGIC, "register of " + id );
    }

    public void unregister( String id ) {
        if ( map.get( id ) == null ) {
            Gdx.app.error( TAG + DbgMark.ERROR, "not registered of " + id );
        } else {
            map.remove( id );
            Gdx.app.log( TAG + DbgMark.LOGIC, "unregistered of " + id );
        }
    }

    public boolean check( String id ) {
        if ( map.get( id ) == null ) {
            try {
                throw new Exception( "register first!" );
            } catch ( Exception e ) {
                e.printStackTrace( );
            }
        }
        return map.get( id ).checkAndRecord( );
    }

    public void dispose( ) {
        map.clear( );
        _i = null;
        Gdx.app.log( TAG + DbgMark.FLOW, "dispose() ---  _i =" + _i );
    }

    /// for leak debug
    static public boolean checkSingletonNull(){
        return _i == null;
    }


    private class IntervalChecker {
        private long timestampPrev;
        private long interval;

        IntervalChecker( long interval ) {
            this.timestampPrev = -100000;
            this.interval = interval;
        }

        public boolean checkAndRecord( ) {
            long tmCurr = System.currentTimeMillis( );
            boolean result = tmCurr - this.timestampPrev < interval;
            this.timestampPrev = tmCurr;
            return result;
        }
    }
}
