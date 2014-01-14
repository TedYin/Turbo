package com.turbo.app;

import android.app.Activity;
import android.os.Bundle;

public class TurboBaseActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TurboAppManager.newInstance().add(this);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		TurboAppManager.newInstance().finish(this);
	}
}
