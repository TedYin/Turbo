package com.turbo;

/**
 * 异常类
 * @author Ted
 */
public class TurboException extends Exception{

	private static final long serialVersionUID = -5399414927092845718L;
	
	private String errorMsg;
	private Object errorObj;
	
	
	public TurboException() {
		// TODO Auto-generated constructor stub
	}
	
	public TurboException(String msg){
		this.errorMsg = msg;
	}
	
	public TurboException(String msg,Object errorObj){
		this.errorMsg = msg;
		this.errorObj = errorObj;
	}
	
	/**
	 * 默认的错误信息
	 * @author Ted
	 */
	public interface DefaultErrorType{
		public static TurboException CONNECTION_TIME_OUT_EXCEPTION = new TurboException("CONNECTION_TIME_OUT,连接超时！");
		public static TurboException NO_RESPONSE_EXCEPTION = new TurboException("NO_RESPONSE_EXCEPTION,请求无响应！");
		public static TurboException NET_ERROR_EXCEPTION = new TurboException("NET RESPONS CODE IS NOT 200,状态码非200！");
		public static TurboException NET_IO_EXCEPTION = new TurboException("NET_IO_EXCEPTION,网络IO错误！");
	}

	
	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Object getErrorObj() {
		return errorObj;
	}

	public void setErrorObj(Object errorObj) {
		this.errorObj = errorObj;
	}
	
	

}
