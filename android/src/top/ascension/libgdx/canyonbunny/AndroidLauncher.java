package top.ascension.libgdx.canyonbunny;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import top.ascension.libgdx.canyonbunny.helper.ILeakCanaryHolder;

public class AndroidLauncher extends AndroidApplication implements ILeakCanaryHolder {

	private RefWatcher refWatcher;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if(LeakCanary.isInAnalyzerProcess( this )){
			return;
		}
		refWatcher = LeakCanary.install( this.getApplication() );
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new ScreenSwitchLeakTest( this ), config);
	}

	@Override
	public void watch( Object object ) {
		refWatcher.watch( object );
	}
}
