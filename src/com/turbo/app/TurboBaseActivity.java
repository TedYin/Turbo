package com.turbo.app;

import android.app.Activity;
import android.os.Bundle;

/**
 * 基础Activity类
 * @author Ted
 * @mail water-cs@qq.com
 * @version 1.0.0
 */
/*该类增加了Activity的管理入口*/
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
