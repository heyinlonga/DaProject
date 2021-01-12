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
import software.ecenter.study.R;

import software.ecenter.study.bean.HomeBean.XiTiNanDuBean;
import software.ecenter.study.utils.ToastUtils;

/**
 * Created by Mike on 2018/4/25.
 */

public class SpinnerPopWindow extends PopupWindow implements PopAdapter.OnItemClickListener, View.OnClickListener {

    private RecyclerView mRecyclerView;
    private RefreshHeaderView mSwipeRefreshHeader;
    private LoadMoreFooterView mLoadMoreFooterView;
    private SwipeToLoadLayout mSwipeToLoadLayout;

    private TextView mPopTile;
    private TextView mTagTitle;
    private ImageView mPopClose;

    private LinearLayout mTagLine;
    private TextView mTagOneText;
    private ImageView mTagOneImage;
    private TextView mTagTwoText;
    private ImageView mTagTwoImage;
    private TextView mTagThreeText;
    private ImageView mTagThreeImage;
    private TextView mTagFourText;
    private ImageView mTagFourImage;

    private RelativeLayout mOneRL;
    private RelativeLayout mTwoRL;
    private RelativeLayout mThreeRL;
    private RelativeLayout mFourRL;


    private PopAdapter mAdapter;
    private List<String> ListData;

    private Context mContext;
    private MItemSelectListener mItemSelectListener;
    private MOnclickListener mOnclickListener;

    private int level = -1;

    public SpinnerPopWindow(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.spinner_popwindow, null);
        setContentView(view);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.swipe_target);
        mTagTitle = view.findViewById(R.id.tag_title);
        mSwipeRefreshHeader = view.findViewById(R.id.swipe_refresh_header);
        mLoadMoreFooterView = view.findViewById(R.id.swipe_load_more_footer);
        mSwipeToLoadLayout = view.findViewById(R.id.swipeToLoadLayout);

        mPopTile = view.findViewById(R.id.pop_title);
        mPopClose = view.findViewById(R.id.btn_pop_close);

        mTagLine = view.findViewById(R.id.tag_line);
        mTagOneText = view.findViewById(R.id.tag_one_text);
        mTagOneImage = view.findViewById(R.id.tag_one_img);
        mTagTwoText = view.findViewById(R.id.tag_two_text);
        mTagTwoImage = view.findViewById(R.id.tag_two_img);
        mTagThreeText = view.findViewById(R.id.tag_three_text);
        mTagThreeImage = view.findViewById(R.id.tag_three_img);
        mTagFourText = view.findViewById(R.id.tag_four_text);
        mTagFourImage = view.findViewById(R.id.tag_four_img);

        mOneRL = view.findViewById(R.id.tag_one_rl);
        mTwoRL = view.findViewById(R.id.tag_two_rl);
        mThreeRL = view.findViewById(R.id.tag_three_rl);
        mFourRL = view.findViewById(R.id.tag_four_rl);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);


        //为swipeToLoadLayout设置下拉刷新监听者
        mSwipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                ToastUtils.showToastSHORT(mContext, "下拉刷新");
                //设置下拉刷新结束
                mSwipeToLoadLayout.setRefreshing(false);
            }
        });
        //为swipeToLoadLayout设置上拉加载更多监听者
        mSwipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                ToastUtils.showToastSHORT(mContext, "上拉更多");
                //设置上拉加载更多结束
                mSwipeToLoadLayout.setLoadingMore(false);
            }
        });


        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setFocusable(true);//设置为true的时候才能点击列表中的item
        // ColorDrawable cd = new ColorDrawable(Color.TRANSPARENT);
        ColorDrawable cd = new ColorDrawable(0x30000000);// 半透明
        setBackgroundDrawable(cd);// 设置背景图片，不能在布局中设置，要通过代码来设置
        setOutsideTouchable(true);// 触摸popupwindow外部，popupwindow消失。这个要求你的popupwindow要有背景图片才可以成功，如上

        ListData = new ArrayList<>();
        mAdapter = new PopAdapter(mContext, ListData);

        mAdapter.setItemClickListener(this);
        mTagOneText.setOnClickListener(this);
        mTagTwoText.setOnClickListener(this);
        mTagThreeText.setOnClickListener(this);
        mTagFourText.setOnClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

        mPopClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void clearData() {
        level = -1;
        mTagOneText.setTextColor(mContext.getResources().getColor(R.color.textColor));
        mTagOneImage.setVisibility(View.INVISIBLE);
        mTagTwoText.setTextColor(mContext.getResources().getColor(R.color.textColor));
        mTagTwoImage.setVisibility(View.INVISIBLE);
        mTagThreeText.setTextColor(mContext.getResources().getColor(R.color.textColor));
        mTagThreeImage.setVisibility(View.INVISIBLE);
        mTagFourText.setTextColor(mContext.getResources().getColor(R.color.textColor));
        mTagFourImage.setVisibility(View.INVISIBLE);
        mTwoRL.setVisibility(View.INVISIBLE);
        mThreeRL.setVisibility(View.INVISIBLE);
        mFourRL.setVisibility(View.INVISIBLE);

    }


    public void refreshData(List<String> DataList) {
        mAdapter.refreshData(DataList);
    }

    public void refreshData(List<XiTiNanDuBean> DataList, boolean hasXiTI) {
        mAdapter.refreshData(DataList, hasXiTI);
    }


    public void setPopTitle(String text) {
        mPopTile.setText(text);
    }

    public TextView getmPopTile() {
        return mPopTile;
    }

    public RecyclerView getmRecyclerView() {
        return mRecyclerView;
    }
    public void setmTagTitle(String text){
        mTagTitle.setText(text);
    }
    public TextView getmTagTitle(){
        return mTagTitle;
    }

    //进入标签模式
    public void toTagMode(String hint) {
        level = 0;
        mTagLine.setVisibility(View.VISIBLE);
        mTagOneText.setText(hint);
        mTagOneText.setTextColor(mContext.getResources().getColor(R.color.textColor));

    }

    public void TagOneOk(String TextOne, String hintTwo) {
        level = 1;
        mTagOneText.setText(TextOne);
        mTagOneText.setTextColor(mContext.getResources().getColor(R.color.textHoldColor));
        mTagOneImage.setVisibility(View.VISIBLE);
        mTagTwoImage.setVisibility(View.GONE);
        mTagThreeImage.setVisibility(View.GONE);
        mTagFourImage.setVisibility(View.GONE);

        mTwoRL.setVisibility(View.VISIBLE);
        mThreeRL.setVisibility(View.GONE);
        mFourRL.setVisibility(View.GONE);
        mTagTwoText.setText(hintTwo);
        mTagTwoText.setTextColor(mContext.getResources().getColor(R.color.textColor));

    }


    public void TagTwoOk(String TwoOne, String hintThree) {
        level = 2;
        mTagTwoText.setText(TwoOne);
        mTagTwoText.setTextColor(mContext.getResources().getColor(R.color.textHoldColor));
        mTagTwoImage.setVisibility(View.VISIBLE);
        mTagThreeImage.setVisibility(View.GONE);
        mTagFourImage.setVisibility(View.GONE);

        mThreeRL.setVisibility(View.VISIBLE);
        mFourRL.setVisibility(View.GONE);
        mTagThreeText.setText(hintThree);
        mTagThreeText.setTextColor(mContext.getResources().getColor(R.color.textColor));
    }

    public void TagThreeOk(String ThreeOne, String hintFour) {
        level = 3;
        mTagThreeText.setText(ThreeOne);
        mTagThreeText.setTextColor(mContext.getResources().getColor(R.color.textHoldColor));
        mTagThreeImage.setVisibility(View.VISIBLE);
        mTagFourImage.setVisibility(View.GONE);

        mFourRL.setVisibility(View.VISIBLE);
        mTagFourText.setText(hintFour);
        mTagFourText.setTextColor(mContext.getResources().getColor(R.color.textColor));
    }


    public int getLevel() {
        return level;
    }

    @Override
    public void onItemClick(int position) {
        if (mItemSelectListener != null) {
            mItemSelectListener.onItemClick(position);
        }
        if (level == -1) {
            dismiss();
        }

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.tag_one_text:
                if (mOnclickListener != null) {
                    mOnclickListener.onClickListener(view, "");
                }
                break;
            case R.id.tag_two_text:
                if (mOnclickListener != null) {
                    mOnclickListener.onClickListener(view, mTagOneText.getText().toString());
                }
                mTagThreeText.setText("请选择节");
                break;
            case R.id.tag_three_text:
                if (mOnclickListener != null) {
                    mOnclickListener.onClickListener(view, mTagTwoText.getText().toString());
                }
                break;
            case R.id.tag_four_text:
                if (mOnclickListener != null) {
                    mOnclickListener.onClickListener(view, mTagThreeText.getText().toString());
                }
                break;
        }

    }

    //设置监听
    public void setItemSelectListener(MItemSelectListener mItemSelectListener) {
        this.mItemSelectListener = mItemSelectListener;
    }

    //设置点击事件监听
    public void setOnclickListener(MOnclickListener mOnclickListener) {
        this.mOnclickListener = mOnclickListener;
    }

    //自定义list中item的点击监听
    public interface MItemSelectListener {
        void onItemClick(int position);
    }

    public interface MOnclickListener {
        void onClickListener(View view, String upName);
    }
}
