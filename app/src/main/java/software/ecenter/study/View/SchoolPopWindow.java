package software.ecenter.study.View;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;

import java.util.ArrayList;
import java.util.List;

import software.ecenter.study.Adapter.PopAdapter;
import software.ecenter.study.Adapter.SchoolPopAdapter;
import software.ecenter.study.R;
import software.ecenter.study.bean.HomeBean.XiTiNanDuBean;
import software.ecenter.study.bean.NameAndIdArrayBean;
import software.ecenter.study.utils.ToastUtils;

/**
 * 学校选择
 * Created by Mike on 2018/4/25.
 */

public class SchoolPopWindow extends PopupWindow implements SchoolPopAdapter.OnItemClickListener {

    private RecyclerView mRecyclerView;
    private TextView mPopTile;
    private ImageView mPopClose;

    private SchoolPopAdapter mAdapter;
    private List<NameAndIdArrayBean.DataBean> ListData;

    private Context mContext;
    private MItemSelectListener mItemSelectListener;

    public SchoolPopWindow(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.school_popwindow, null);
        setContentView(view);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.swipe_target);
        mPopTile = view.findViewById(R.id.pop_title);
        mPopClose = view.findViewById(R.id.btn_pop_close);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);


        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setFocusable(true);//设置为true的时候才能点击列表中的item
        // ColorDrawable cd = new ColorDrawable(Color.TRANSPARENT);
        ColorDrawable cd = new ColorDrawable(0x30000000);// 半透明
        setBackgroundDrawable(cd);// 设置背景图片，不能在布局中设置，要通过代码来设置
        setOutsideTouchable(true);// 触摸popupwindow外部，popupwindow消失。这个要求你的popupwindow要有背景图片才可以成功，如上

        ListData = new ArrayList<>();
        mAdapter = new SchoolPopAdapter(mContext, ListData);

        mAdapter.setItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mPopClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setPopTitle(String text) {
        if (mPopTile != null)
            mPopTile.setText(text);
    }

    public void refreshData(List<NameAndIdArrayBean.DataBean> DataList) {
        mAdapter.refreshData(DataList);
        if (DataList != null && DataList.size() > 0)
            mRecyclerView.scrollToPosition(0);
    }

    @Override
    public void onItemClick(int position) {
        if (mItemSelectListener != null) {
            mItemSelectListener.onItemClick(position);
        }
        dismiss();
    }

    //设置监听
    public void setItemSelectListener(MItemSelectListener mItemSelectListener) {
        this.mItemSelectListener = mItemSelectListener;
    }

    //自定义list中item的点击监听
    public interface MItemSelectListener {
        void onItemClick(int position);
    }

}
