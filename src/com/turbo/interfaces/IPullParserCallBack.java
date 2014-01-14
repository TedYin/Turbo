package com.turbo.interfaces;

/**
 * Pull解析XML回调接口
 * @author Ted
 */
public interface IPullParserCallBack {
	public void startDocCallBack();
	public void startTagCallBack();
	public void endTagCallBack();
	public void endDocCallBack();
	public boolean isCanInterrupt();
}
