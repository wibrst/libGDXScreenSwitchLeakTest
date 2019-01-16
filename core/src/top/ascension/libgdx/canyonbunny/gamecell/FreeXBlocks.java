package top.ascension.libgdx.canyonbunny.gamecell;

import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import top.ascension.libgdx.canyonbunny.Layout;
import top.ascension.libgdx.canyonbunny.debug.DbgMark;
import top.ascension.libgdx.canyonbunny.helper.CameraHelper;

public class FreeXBlocks implements IGameCell {
    private static final String TAG = FreeXBlocks.class.getSimpleName( );

    private Sprite[] vcSprite;
    private int idxSpriteFocus;

    private CameraHelper r_cmrHpr;
    private OrthographicCamera r_cmrGame;
    private TweenManager r_twnmgr;

    @Override
    public void init( CameraHelper cmrHpr, OrthographicCamera cmrGame, TweenManager twnmgr ) {
        this.r_cmrHpr = cmrHpr;
        r_cmrGame = cmrGame;
        r_twnmgr = twnmgr;

        initSprites( );
    }

    @Override
    public void uninit( ) {
        this.r_cmrHpr = null;
        r_cmrGame = null;
        r_twnmgr = null;

        for ( Sprite sprite : vcSprite ) {
            sprite.getTexture().dispose();
        }
        vcSprite = null;
        Gdx.app.log( TAG + DbgMark.FLOW, "uninit()" );
    }

    @Override
    public void updateForRender( float tmDelta ) {
        moveBlocks( tmDelta );
    }

    @Override
    public void render( SpriteBatch batch ) {
        for ( Sprite sprite : vcSprite ) {
            sprite.draw( batch );
        }
    }

    @Override
    public void backBehavior( ) {
        setSpriteFocus( ++idxSpriteFocus % Layout.NUM_BLOCKS );
    }

    private void moveBlocks( final float tmDelta ) {
        float rot = vcSprite[idxSpriteFocus].getRotation( );
        rot += 360 * tmDelta; /// 每秒90度的速度旋转
        rot %= 360;
        vcSprite[idxSpriteFocus].setRotation( rot );
    }

    private void setSpriteFocus( int i ) {
        idxSpriteFocus = i;
        r_cmrHpr.refocusCamera(
                vcSprite[idxSpriteFocus].getX( ),
                vcSprite[idxSpriteFocus].getY( ),
                r_cmrGame,
                r_twnmgr );
        Gdx.app.log( TAG + DbgMark.INTERACT, "setSpriteFocus() --- Sprite #" + idxSpriteFocus + " Selected!" );
    }

    private void initSprites( ) {
        vcSprite = new Sprite[Layout.NUM_BLOCKS];

        for ( int i = 0; i < Layout.NUM_BLOCKS; i++ ) {
            Pixmap pxm = createBlockPxm( ( i + 1 ) * 0.2f, Layout.W_BLOCK, Layout.H_BLOCK, Layout.FMT_BLOCK );
            Sprite sprite = new Sprite( new Texture( pxm ) );
            sprite.setSize( 1, 1 );
            // sprite.setOrigin( sprite.getWidth()/2,sprite.getHeight()/2 );
            sprite.setOriginCenter( );

            float xRdm = MathUtils.random( -Layout.VP_W * 0.5f, Layout.VP_W * 0.5f );
            float yRdm = MathUtils.random( -Layout.VP_H * 0.5f, Layout.VP_H * 0.5f );
            Gdx.app.log( TAG + DbgMark.LOGIC, "initSprites() --- block born at:" + xRdm + "," + yRdm );
            sprite.setPosition( xRdm, yRdm );

            vcSprite[i] = sprite;
        }

        setSpriteFocus( 0 );
    }

    private Pixmap createBlockPxm( float fFactorMark, int wBlock, int hBlock, Pixmap.Format fmt ) {
        Gdx.app.log( TAG + DbgMark.LOGIC, "createBlockPxm() --- fFactorMark:" + fFactorMark );
        Pixmap pxm = new Pixmap( wBlock, hBlock, fmt );
        pxm.setColor( fFactorMark, fFactorMark * .5f, fFactorMark * .5f, 1f );
        pxm.fill( );

        pxm.setColor( 1, 1, 0, 1 );
        pxm.drawLine( 0, 0, wBlock, hBlock );
        pxm.drawLine( wBlock, 0, 0, hBlock );

        pxm.setColor( 0, 1, 1, 1 );
        pxm.drawRectangle( 0, 0, wBlock, hBlock );

        return pxm;
    }
}
