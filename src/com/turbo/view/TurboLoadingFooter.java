
package com.turbo.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tedyin.turbo.R;

/**
 * ListView等的加载底部框
 * @author Ted
 * @mail water-cs@qq.com
 * @version 1.0.0
 */
public class TurboLoadingFooter {
    protected View mLoadingFooter;

    protected TextView mLoadingText;

    protected State mState = State.Idle;

    private ProgressBar mProgress;

    public static enum State {
        Idle, TheEnd, Loading
    }
    
    public TurboLoadingFooter(Context context) {
        mLoadingFooter = LayoutInflater.from(context).inflate(R.layout.turbo_view_loading_footer, null);
        mLoadingFooter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 屏蔽点击
            }
        });
        mProgress = (ProgressBar) mLoadingFooter.findViewById(R.id.progressBar);
        mLoadingText = (TextView) mLoadingFooter.findViewById(R.id.textView);
        setState(State.Idle);
    }

    public View getView() {
        return mLoadingFooter;
    }

    public State getState() {
        return mState;
    }

    public void setState(final State state, long delay) {
        mLoadingFooter.postDelayed(new Runnable() {

            @Override
            public void run() {
                setState(state);
            }
        }, delay);
    }

    public void setState(State status) {
        if (mState == status) {
            return;
        }
        mState = status;
        mLoadingFooter.setVisibility(View.VISIBLE);
        switch (status) {
            case Loading:
                mLoadingText.setVisibility(View.GONE);
                mProgress.setVisibility(View.VISIBLE);
                break;
            case TheEnd:
            	mLoadingText.setText(R.string.loading_footer_load_complete);
                mLoadingText.setVisibility(View.VISIBLE);
                mProgress.setVisibility(View.GONE);
                break;
            default:
                mLoadingFooter.setVisibility(View.GONE);
                break;
        }
    }
}
