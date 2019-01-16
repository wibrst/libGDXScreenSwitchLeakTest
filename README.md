# libGDXScreenSwitchLeakTest
libGDX Learning project. It just for test memory leak while switch two empty screen

## Test platform: Android

## Two main class:

### public class ScreenSwitchLeakTest extends Game 
Screen switch flow: VERY_BEGIN -> MENU_SCREEN -> EMPTY_SCREEN   
then roll back: MENU_SCREEN <--> EMPTY_SCREEN                   
switch code is same at two place : Gdx.input.isTouched(),       
respectively in class MenuScreen and EmptyScreen.
Both screen is empty,but only a clear code with diffrent color.

### public abstract class AbstScreen implements Screen 
Main resources process: AssetsHolder.i().init( new AssetManager( ) );
It will load a ttf font,and display nothing.
And dispose() of AssetsHolder.i() will be call while the screen dispose() executing.

## Main trouble:  
Use Android Profiler, Memory view, amount of memory will increase outstanding while you touch screen( switch screen code executing ).
