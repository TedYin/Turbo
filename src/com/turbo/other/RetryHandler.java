package com.turbo.other;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashSet;

import javax.net.ssl.SSLException;

import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

import android.os.SystemClock;

/**
 * 出错重试处理
 * @author Ted
 *
 */
public class RetryHandler implements HttpRequestRetryHandler {

	private static HashSet<Class<?>> exceptionWhitelist = new HashSet<Class<?>>();
	private static HashSet<Class<?>> exceptionBlacklist = new HashSet<Class<?>>();
	static {
		// Retry if the server dropped connection on us
		exceptionWhitelist.add(NoHttpResponseException.class);
		// retry-this, since it may happens as part of a Wi-Fi to 3G failover
		exceptionWhitelist.add(UnknownHostException.class);
		// retry-this, since it may happens as part of a Wi-Fi to 3G failover
		exceptionWhitelist.add(SocketException.class);
		
		// never retry timeouts
        exceptionBlacklist.add(InterruptedIOException.class);
		// never retry SSL handshake failures
		exceptionBlacklist.add(SSLException.class);
	}

	private final int maxRetries;
	private final int retrySleepTimeMS;

	public RetryHandler(int maxRetries) {
		this(maxRetries,3000);
	}
	
	public RetryHandler(int maxRetries,int retrySleepTimeMS) {
		if (maxRetries > 3 && maxRetries < 6)
			this.maxRetries = maxRetries;
		else
			this.maxRetries = 4;
		this.retrySleepTimeMS = retrySleepTimeMS;
	}
	

	@Override
	public boolean retryRequest(IOException exception, int executionCount,
			HttpContext context) {
		boolean retry = true;
		Boolean b = (Boolean) context
				.getAttribute(ExecutionContext.HTTP_REQ_SENT);
		boolean sent = (b != null && b);
		if (executionCount > maxRetries) {
			// Do not retry if over max retry count
			retry = false;
		} else if (isInList(exceptionBlacklist, exception)) {
			retry = false;
		} else if (isInList(exceptionWhitelist, exception)) {
			retry = true;
		} else if (!sent) {
			retry = true;
		}

		if (retry) {
			// resend all idempotent requests
			HttpUriRequest currentReq = (HttpUriRequest) context
					.getAttribute(ExecutionContext.HTTP_REQUEST);
			if (currentReq == null) {
				return false;
			}
			String requestType = currentReq.getMethod();
			retry = !requestType.equals("POST");
		}
		if (retry) {
			SystemClock.sleep(retrySleepTimeMS * executionCount);
		} else {
			exception.printStackTrace();
		}
		return retry;
	}

	public static void addClassToBalcklist(Class<?> clazz) {
		exceptionBlacklist.add(clazz);
	}

	public static void addClassToWhitelist(Class<?> clazz) {
		exceptionWhitelist.add(clazz);
	}

	private boolean isInList(HashSet<Class<?>> list, Throwable error) {
		for (Class<?> aList : list) {
			if (aList.isInstance(error)) {
				return true;
			}
		}
		return false;
	}

}
