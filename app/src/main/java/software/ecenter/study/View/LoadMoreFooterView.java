package software.ecenter.study.View;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.aspsine.swipetoloadlayout.SwipeLoadMoreTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;

/**
 * Created by Mike on 2018/4/27.
 */

public class LoadMoreFooterView extends AppCompatTextView implements SwipeTrigger, SwipeLoadMoreTrigger {
    public LoadMoreFooterView(Context context) {
        super(context);
    }

    public LoadMoreFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onLoadMore() {
       // setText("加载更多");
    }

    @Override
    public void onPrepare() {
      //  setText("");
    }

    @Override
    public void onMove(int yScrolled, boolean isComplete, boolean automatic) {
   /*     if (!isComplete) {
            if (yScrolled <= -getHeight()) {
                setText("释放加载");
            } else {
                setText("释放加载");
            }
        } else {
            setText("LOAD MORE RETURNING");
        }*/
    }

    @Override
    public void onRelease() {
       // setText("LOADING MORE");
    }

    @Override
    public void onComplete() {
        //setText("加载完成");
    }

    @Override
    public void onReset() {
        //setText("");
    }
}
