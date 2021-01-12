package software.ecenter.study.View;

import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import moe.codeest.enviews.ENDownloadView;
import software.ecenter.study.R;
import software.ecenter.study.utils.AccountUtil;

/**
 * Created by shuyu on 2016/12/23.
 * CustomGSYVideoPlayer是试验中，建议使用的时候使用StandardGSYVideoPlayer
 */
public class LandLayoutVideo extends StandardGSYVideoPlayer {

    public View mButonStartTwo;
    public TextView tvSpeed;

    /**
     * 1.5.0开始加入，如果需要不同布局区分功能，需要重载
     */
    public LandLayoutVideo(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public LandLayoutVideo(Context context) {
        super(context);
    }

    public LandLayoutVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void init(Context context) {
        super.init(context);
        mButonStartTwo = findViewById(R.id.startTwo);
        tvSpeed = findViewById(R.id.tv_speed);
        if (mButonStartTwo != null) {
            mButonStartTwo.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startPlay();
                }
            });
        }
        if (tvSpeed != null) {
            setSpeedView();
            tvSpeed.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (tvSpeed.getText().toString().equals("1X")) {
                        if (isInPlayingState())
                            setSpeedPlaying(1.5f, false);
                        else
                            setSpeed(1.5f, false);
                        tvSpeed.setText("1.5X");
                    } else if (tvSpeed.getText().toString().equals("1.5X")) {
                        if (isInPlayingState())
                            setSpeedPlaying(2f, false);
                        else
                            setSpeed(2f, false);
                        tvSpeed.setText("2X");
                        return;
                    } else if (tvSpeed.getText().toString().equals("2X")) {
                        if (isInPlayingState())
                            setSpeedPlaying(1f, false);
                        else
                            setSpeed(1f, false);
                        tvSpeed.setText("1X");
                        return;
                    }
                }
            });
        }
    }
    public void revice(){
        setSpeed(1f, false);
        tvSpeed.setText("1X");
    }

    private void setSpeedView() {
        float speed = AccountUtil.getSpeednum(getActivityContext());
        if (speed == 2) {
            tvSpeed.setText("2X");
        } else if (speed == 1.5) {
            tvSpeed.setText("1.5X");
        } else {
            tvSpeed.setText("1X");
        }
    }

    public void startPlay() {
        clickStartIcon();
        if (mThumbImageView != null)
            setViewShowState(mThumbImageViewLayout, VISIBLE);
    }

    @Override
    protected void showWifiDialog() {
        if (AccountUtil.getWifiTipSet(mContext)) {
            MyDialog.Builder builder = new MyDialog.Builder(mContext);
            builder.setTitle("提示");
            builder.setMessage("您当前正在使用移动网络，继续播放将消耗流量？");

            builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });

            builder.setNegativeButton("确定",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //设置你的操作事项
                            startButtonLogic();
                            dialog.dismiss();
                        }
                    });

            builder.create().show();

        } else {
            startButtonLogic();
        }

    }

    //这个必须配置最上面的构造才能生效
    @Override
    public int getLayoutId() {
        if (mIfCurrentIsFullscreen) {
            return R.layout.sample_video_land;
        }
        return R.layout.sample_video_normal;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == com.shuyu.gsyvideoplayer.R.id.thumb) {
            if (mIfCurrentIsFullscreen && mLockCurScreen && mNeedLockFull) {
                setViewShowState(mLockScreen, VISIBLE);
                return;
            }
            if (mCurrentState == CURRENT_STATE_PREPAREING) {
                if (mBottomContainer != null) {
                    if (mBottomContainer.getVisibility() == View.VISIBLE) {
                        changeUiToPrepareingClearD();
                    } else {
                        changeUiToPreparingShowD();
                    }
                }
            } else if (mCurrentState == CURRENT_STATE_PLAYING) {
                if (mBottomContainer != null) {
                    if (mBottomContainer.getVisibility() == View.VISIBLE) {
                        changeUiToPlayingClearD();
                    } else {
                        changeUiToPlayingShowD();
                    }
                }
            } else if (mCurrentState == CURRENT_STATE_PAUSE) {
                if (mBottomContainer != null) {
                    if (mBottomContainer.getVisibility() == View.VISIBLE) {
                        changeUiToPauseClearD();
                    } else {
                        changeUiToPauseShowD();
                    }
                }
            } else if (mCurrentState == CURRENT_STATE_AUTO_COMPLETE) {
                if (mBottomContainer != null) {
                    if (mBottomContainer.getVisibility() == View.VISIBLE) {
                        changeUiToCompleteClearD();
                    } else {
                        changeUiToCompleteShowD();
                    }
                }
            } else if (mCurrentState == CURRENT_STATE_PLAYING_BUFFERING_START) {
                if (mBottomContainer != null) {
                    if (mBottomContainer.getVisibility() == View.VISIBLE) {
                        changeUiToPlayingBufferingClearD();
                    } else {
                        changeUiToPlayingBufferingShowD();
                    }
                }
            }
        }
    }

    private void changeUiToPlayingBufferingShowD() {
        setViewShowState(mTopContainer, VISIBLE);
        setViewShowState(mBottomContainer, VISIBLE);
        setViewShowState(mStartButton, INVISIBLE);
        setViewShowState(mLoadingProgressBar, VISIBLE);
//        setViewShowState(mThumbImageViewLayout, INVISIBLE);
        setViewShowState(mBottomProgressBar, INVISIBLE);
        setViewShowState(mLockScreen, GONE);

        if (mLoadingProgressBar instanceof ENDownloadView) {
            ENDownloadView enDownloadView = (ENDownloadView) mLoadingProgressBar;
            if (enDownloadView.getCurrentState() == ENDownloadView.STATE_PRE) {
                ((ENDownloadView) mLoadingProgressBar).start();
            }
        }
    }

    private void changeUiToPlayingBufferingClearD() {
        setViewShowState(mTopContainer, INVISIBLE);
        setViewShowState(mBottomContainer, INVISIBLE);
        setViewShowState(mStartButton, INVISIBLE);
        setViewShowState(mLoadingProgressBar, VISIBLE);
//        setViewShowState(mThumbImageViewLayout, INVISIBLE);
        setViewShowState(mBottomProgressBar, VISIBLE);
        setViewShowState(mLockScreen, GONE);

        if (mLoadingProgressBar instanceof ENDownloadView) {
            ENDownloadView enDownloadView = (ENDownloadView) mLoadingProgressBar;
            if (enDownloadView.getCurrentState() == ENDownloadView.STATE_PRE) {
                ((ENDownloadView) mLoadingProgressBar).start();
            }
        }
        updateStartImage();
    }

    private void changeUiToCompleteShowD() {
        setViewShowState(mTopContainer, VISIBLE);
        setViewShowState(mBottomContainer, VISIBLE);
        setViewShowState(mStartButton, VISIBLE);
        setViewShowState(mLoadingProgressBar, INVISIBLE);
//        setViewShowState(mThumbImageViewLayout, VISIBLE);
        setViewShowState(mBottomProgressBar, INVISIBLE);
        setViewShowState(mLockScreen, (mIfCurrentIsFullscreen && mNeedLockFull) ? VISIBLE : GONE);

        if (mLoadingProgressBar instanceof ENDownloadView) {
            ((ENDownloadView) mLoadingProgressBar).reset();
        }
        updateStartImage();
    }

    private void changeUiToCompleteClearD() {
        setViewShowState(mTopContainer, INVISIBLE);
        setViewShowState(mBottomContainer, INVISIBLE);
        setViewShowState(mStartButton, VISIBLE);
        setViewShowState(mLoadingProgressBar, INVISIBLE);
//        setViewShowState(mThumbImageViewLayout, VISIBLE);
        setViewShowState(mBottomProgressBar, VISIBLE);
        setViewShowState(mLockScreen, (mIfCurrentIsFullscreen && mNeedLockFull) ? VISIBLE : GONE);

        if (mLoadingProgressBar instanceof ENDownloadView) {
            ((ENDownloadView) mLoadingProgressBar).reset();
        }
        updateStartImage();
    }

    private void changeUiToPauseShowD() {
        setViewShowState(mTopContainer, VISIBLE);
        setViewShowState(mBottomContainer, VISIBLE);
        setViewShowState(mStartButton, VISIBLE);
        setViewShowState(mLoadingProgressBar, INVISIBLE);
//        setViewShowState(mThumbImageViewLayout, INVISIBLE);
        setViewShowState(mBottomProgressBar, INVISIBLE);
        setViewShowState(mLockScreen, (mIfCurrentIsFullscreen && mNeedLockFull) ? VISIBLE : GONE);

        if (mLoadingProgressBar instanceof ENDownloadView) {
            ((ENDownloadView) mLoadingProgressBar).reset();
        }
        updateStartImage();
        updatePauseCover();
    }

    private void changeUiToPauseClearD() {
        changeUiToClearD();
        setViewShowState(mBottomProgressBar, VISIBLE);
        updatePauseCover();
    }

    private void changeUiToPlayingShowD() {
        setViewShowState(mTopContainer, VISIBLE);
        setViewShowState(mBottomContainer, VISIBLE);
        setViewShowState(mStartButton, VISIBLE);
        setViewShowState(mLoadingProgressBar, INVISIBLE);
//        setViewShowState(mThumbImageViewLayout, INVISIBLE);
        setViewShowState(mBottomProgressBar, INVISIBLE);
        setViewShowState(mLockScreen, (mIfCurrentIsFullscreen && mNeedLockFull) ? VISIBLE : GONE);

        if (mLoadingProgressBar instanceof ENDownloadView) {
            ((ENDownloadView) mLoadingProgressBar).reset();
        }
        updateStartImage();
    }

    private void changeUiToPlayingClearD() {
        changeUiToClearD();
        setViewShowState(mBottomProgressBar, VISIBLE);
    }

    protected void changeUiToClearD() {

        setViewShowState(mTopContainer, INVISIBLE);
        setViewShowState(mBottomContainer, INVISIBLE);
        setViewShowState(mStartButton, INVISIBLE);
        setViewShowState(mLoadingProgressBar, INVISIBLE);
//        setViewShowState(mThumbImageViewLayout, INVISIBLE);
        setViewShowState(mBottomProgressBar, INVISIBLE);
        setViewShowState(mLockScreen, GONE);

        if (mLoadingProgressBar instanceof ENDownloadView) {
            ((ENDownloadView) mLoadingProgressBar).reset();
        }
    }

    private void changeUiToPreparingShowD() {
        setViewShowState(mTopContainer, VISIBLE);
        setViewShowState(mBottomContainer, VISIBLE);
        setViewShowState(mStartButton, INVISIBLE);
        setViewShowState(mLoadingProgressBar, VISIBLE);
//        setViewShowState(mThumbImageViewLayout, INVISIBLE);
        setViewShowState(mBottomProgressBar, INVISIBLE);
        setViewShowState(mLockScreen, GONE);

        if (mLoadingProgressBar instanceof ENDownloadView) {
            ENDownloadView enDownloadView = (ENDownloadView) mLoadingProgressBar;
            if (enDownloadView.getCurrentState() == ENDownloadView.STATE_PRE) {
                ((ENDownloadView) mLoadingProgressBar).start();
            }
        }
    }

    private void changeUiToPrepareingClearD() {
        setViewShowState(mTopContainer, INVISIBLE);
        setViewShowState(mBottomContainer, INVISIBLE);
        setViewShowState(mStartButton, INVISIBLE);
        setViewShowState(mLoadingProgressBar, INVISIBLE);
//        setViewShowState(mThumbImageViewLayout, INVISIBLE);
        setViewShowState(mBottomProgressBar, INVISIBLE);
        setViewShowState(mLockScreen, GONE);

        if (mLoadingProgressBar instanceof ENDownloadView) {
            ((ENDownloadView) mLoadingProgressBar).reset();
        }
    }

    @Override
    protected void updateStartImage() {
        if (mIfCurrentIsFullscreen) {
            if (mStartButton instanceof ImageView) {
                ImageView imageView = (ImageView) mStartButton;
                if (mCurrentState == CURRENT_STATE_PLAYING) {
                    imageView.setImageResource(R.drawable.video_click_pause_selector);
                } else if (mCurrentState == CURRENT_STATE_ERROR) {
                    imageView.setImageResource(R.drawable.video_click_play_selector);
                } else {
                    imageView.setImageResource(R.drawable.video_click_play_selector);
                }
            }
        } else {
            super.updateStartImage();
            if (mButonStartTwo != null) {
                if (mButonStartTwo instanceof ImageView) {
                    ImageView imageView = (ImageView) mButonStartTwo;
                    if (mCurrentState == CURRENT_STATE_PLAYING) {
                        imageView.setImageResource(R.drawable.video_click_pause_selector);
                    } else if (mCurrentState == CURRENT_STATE_ERROR) {
                        imageView.setImageResource(R.drawable.video_click_play_selector);
                    } else {
                        imageView.setImageResource(R.drawable.video_click_play_selector);
                    }
                }
            }
        }
    }

    @Override
    public int getEnlargeImageRes() {
        return R.drawable.szhankai;
    }

    @Override
    public int getShrinkImageRes() {
        return R.drawable.sshousuo;
    }


}
