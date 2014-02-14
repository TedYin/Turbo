package com.turbo.interfaces;

/**
 * Pull解析XML回调接口
 * @author Ted
 * @mail water-cs@qq.com
 * @version 1.0.0
 */
public interface IPullParserCallBack {
	public void startDocCallBack();
	public void startTagCallBack();
	public void endTagCallBack();
	public void endDocCallBack();
	public boolean isCanInterrupt();
}
