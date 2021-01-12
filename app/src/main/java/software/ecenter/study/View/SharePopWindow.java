package software.ecenter.study.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.util.List;

import software.ecenter.study.Adapter.RankManAdapter;
import software.ecenter.study.R;

/**
 * 分享
 * Created by Mike on 2018/4/25.
 */

public class SharePopWindow extends PopupWindow {

    private Context mContext;
    private ImageView iv_close;
    private LinearLayout ll_pyq;
    private LinearLayout ll_wx;
    private LinearLayout ll_qq;
    private LinearLayout ll_kj;
    private SchoolPopWindow.MItemSelectListener listener;
    public SharePopWindow(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.share_popwindow, null);
        setContentView(view);
        iv_close = view.findViewById(R.id.iv_close);
        ll_pyq = view.findViewById(R.id.ll_pyq);
        ll_wx = view.findViewById(R.id.ll_wx);
        ll_qq = view.findViewById(R.id.ll_qq);
        ll_kj = view.findViewById(R.id.ll_kj);


        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        setFocusable(true);//设置为true的时候才能点击列表中的item
        // ColorDrawable cd = new ColorDrawable(Color.TRANSPARENT);
        ColorDrawable cd = new ColorDrawable(0x30000000);// 半透明
        setBackgroundDrawable(cd);// 设置背景图片，不能在布局中设置，要通过代码来设置
        setOutsideTouchable(true);// 触摸popupwindow外部，popupwindow消失。这个要求你的popupwindow要有背景图片才可以成功，如上
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.onItemClick(0);
                }
                dismiss();
            }
        });
        ll_pyq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.onItemClick(1);
                }
            }
        });ll_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.onItemClick(2);
                }
            }
        });ll_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.onItemClick(3);
                }
            }
        });ll_kj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.onItemClick(4);
                }
            }
        });
    }
    //设置监听
    public void setItemSelectListener(SchoolPopWindow.MItemSelectListener mItemSelectListener) {
        this.listener = mItemSelectListener;
    }

    //自定义list中item的点击监听
    public interface MItemSelectListener {
        void onItemClick(int position);
    }
}
