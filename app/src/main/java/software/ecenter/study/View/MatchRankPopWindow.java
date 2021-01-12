package software.ecenter.study.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import software.ecenter.study.Adapter.RankManAdapter;
import software.ecenter.study.Adapter.SchoolPopAdapter;
import software.ecenter.study.R;
import software.ecenter.study.bean.NameAndIdArrayBean;
import software.ecenter.study.utils.ToolUtil;

/**
 * 排行榜
 * Created by Mike on 2018/4/25.
 */

public class MatchRankPopWindow extends PopupWindow {

    private Context mContext;
    private ImageView iv_close;
    private RecyclerView rv_list;
    private RankManAdapter adapter;

    public MatchRankPopWindow(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.matchrank_popwindow, null);
        setContentView(view);
        iv_close = view.findViewById(R.id.iv_close);
        rv_list = view.findViewById(R.id.rv_list);
        rv_list.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new RankManAdapter(mContext);
        rv_list.setAdapter(adapter);


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
                dismiss();
            }
        });
    }

    //设置数据
    @SuppressLint("SetTextI18n")
    public void setData(List<RankManBean.DataBean> data) {
        adapter.setData(data);
    }
    public List<RankManBean.DataBean> getData(){
        return adapter.getmList();
    }
}
