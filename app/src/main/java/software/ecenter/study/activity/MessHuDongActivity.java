package software.ecenter.study.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import software.ecenter.study.Adapter.CommentAdapter;
import software.ecenter.study.Adapter.QuestionAdapter;
import software.ecenter.study.R;
import software.ecenter.study.bean.MineBean.MyCommitBean;
import software.ecenter.study.bean.MineBean.MyCommitDetailBean;
import software.ecenter.study.bean.MineBean.MyQusetionDetailBean;
import software.ecenter.study.bean.QuestionBean.QuestionBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;

/**
 * Message  信息互动
 * Create by Administrator
 * Create by 2019/9/3
 */
public class MessHuDongActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_all)
    TextView tv_all;
    @BindView(R.id.tv_cancle)
    TextView tv_cancle;
    @BindView(R.id.rv_list)
    RecyclerView rv_list;
    @BindView(R.id.btn_delete)
    Button btn_delete;
    @BindView(R.id.ll_error)
    LinearLayout ll_error;
    @BindView(R.id.ll_no)
    LinearLayout ll_no;
    @BindView(R.id.ll_left)
    LinearLayout ll_left;
    @BindView(R.id.tv_left)
    TextView tv_left;
    @BindView(R.id.view_left)
    View view_left;
    @BindView(R.id.ll_right)
    LinearLayout ll_right;
    @BindView(R.id.tv_right)
    TextView tv_right;
    @BindView(R.id.view_right)
    View view_right;

    private int mType = 1;//1提问2评论
    private CommentAdapter commentAdapter;//评论
    private QuestionAdapter questionAdapter;//提问
    private List<MyCommitBean> commentList = new ArrayList<>();
    private List<QuestionBean> questionList = new ArrayList<>();
    private List<MyCommitBean> mListCheckData = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messhudong);
        ButterKnife.bind(this);
        rv_list.setLayoutManager(new LinearLayoutManager(mContext));
        getData();
    }
    //获取数据
    private void getData() {
        if (mType == 1){
            getQuestionList();
        }else {
            getUserComments();
        }
    }
    //评论
    public void getUserComments() {
        if (!showNetWaitLoding()) {
            return;
        }

        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getUserComments(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        MyCommitDetailBean mMyCommitDetailBean = ParseUtil.parseBean(response, MyCommitDetailBean.class);
                        ll_error.setVisibility(View.GONE);
                        if (mMyCommitDetailBean == null) {
                            ll_no.setVisibility(View.VISIBLE);
                            rv_list.setVisibility(View.GONE);
                            return;
                        }
                        commentList.clear();
                        commentList = mMyCommitDetailBean.getData().getData();
                        setChangeAdapter();
                        if (commentList.size() <= 0) {
                            ll_no.setVisibility(View.VISIBLE);
                            rv_list.setVisibility(View.GONE);
                        } else {
                            ll_no.setVisibility(View.GONE);
                            rv_list.setVisibility(View.VISIBLE);
                        }
                        toCheckMode();

                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ll_error.setVisibility(View.VISIBLE);
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });

    }
    //提问列表
    public void getQuestionList() {
        if (!showNetWaitLoding()) {
            return;
        }
        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getQuestionList(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        MyQusetionDetailBean mMyQusetionDetailBean = ParseUtil.parseBean(response, MyQusetionDetailBean.class);
                        ll_error.setVisibility(View.GONE);
                        if (mMyQusetionDetailBean == null) {
                            ll_no.setVisibility(View.VISIBLE);
                            rv_list.setVisibility(View.GONE);
                            return;
                        }
                        questionList.clear();
                        questionList = mMyQusetionDetailBean.getData().getData();
                        setChangeAdapter();
                        if (questionList.size() <= 0) {
                            ll_no.setVisibility(View.VISIBLE);
                            rv_list.setVisibility(View.GONE);
                        } else {
                            ll_no.setVisibility(View.GONE);
                            rv_list.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ll_error.setVisibility(View.VISIBLE);
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });

    }
    @OnClick({R.id.iv_back, R.id.tv_all, R.id.tv_cancle, R.id.btn_delete, R.id.ll_left, R.id.ll_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back://退出
                finish();
                break;
            case R.id.tv_all://全选
                checkAllItem(true);
                break;
            case R.id.tv_cancle://取消
                OutCheckMode();
                break;
            case R.id.btn_delete://删除
                deleteUserComment();
                break;
            case R.id.ll_left://提问
                showLay(1);
                break;
            case R.id.ll_right://评论
                showLay(2);
                break;
            case R.id.ll_error://错误界面
                getData();
                break;
        }
    }
    //删除评论
    public void deleteUserComment() {
        if (!showNetWaitLoding()) {
            return;
        }
        mListCheckData.clear();
        String Ids = "";
        for (MyCommitBean item : commentList) {
            if (item.isCheck()) {
                Ids += item.getCommentId() + ",";
                mListCheckData.add(item);
            }
        }

        if (mListCheckData.size() == 0) {
            ToastUtils.showToastSHORT(mContext, "请选择");
        }
        if (Ids.endsWith(",")) {
            Ids = Ids.substring(0, Ids.length() - 1);
        }

        Map<String, String> map = new HashMap<>();
        map.put("commentIds", Ids);

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).deleteUserComment(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        for (MyCommitBean item : mListCheckData) {
                            commentList.remove(item);
                        }
                        OutCheckMode();
                        ToastUtils.showToastLONG(mContext, "删除成功");
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });


    }
    //复原控件状态
    private void recoverLay(boolean hasCheck) {
        iv_back.setVisibility(hasCheck?View.GONE:View.VISIBLE);
        tv_all.setVisibility(hasCheck?View.VISIBLE:View.GONE);
        tv_cancle.setVisibility(hasCheck?View.VISIBLE:View.GONE);
        btn_delete.setVisibility(hasCheck?View.VISIBLE:View.GONE);
    }

    //切换tan
    private void showLay(int type) {
        if (mType == type) {
            return;
        }
        mType = type;
        recoverLay(false);
        if (mType == 1) {
            tv_left.setTextColor(getResources().getColor(R.color.color_F77450));
            view_left.setVisibility(View.VISIBLE);

            tv_right.setTextColor(getResources().getColor(R.color.textHoldColor));
            view_right.setVisibility(View.INVISIBLE);
        } else {
            tv_left.setTextColor(getResources().getColor(R.color.textHoldColor));
            view_left.setVisibility(View.INVISIBLE);

            tv_right.setTextColor(getResources().getColor(R.color.color_F77450));
            view_right.setVisibility(View.VISIBLE);
        }
        rv_list.setVisibility(View.GONE);
        getData();
    }
    //切换adapter
    private void setChangeAdapter() {
        if (mType == 1){
            questionAdapter = new QuestionAdapter(mContext,questionList);
            rv_list.setAdapter(questionAdapter);
            questionAdapter.setItemClickListener(new QuestionAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent intent = new Intent(mContext, QuestionDetailActivity.class);
                    intent.putExtra("questionId", questionList.get(position).getQuestionId());
                    startActivity(intent);

                }
            });
        }else {
            commentAdapter = new CommentAdapter(mContext,commentList);
            rv_list.setAdapter(commentAdapter);
            commentAdapter.setItemClickListener(new CommentAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, boolean isJump) {
                    if (isJump) {
                        boolean hasCheck = false;
                        for (MyCommitBean item : commentList) {
                            if (item.isCheck()) {
                                hasCheck = true;
                                break;
                            }
                        }
                        if (hasCheck) return;
                        Intent intent = new Intent(mContext, SeeResourcesVideoActivity.class);
                        intent.putExtra("resourceId", commentList.get(position).getResourceId());
                        startActivity(intent);
                    } else if (commentList.get(position).isCheckMode()) {
                        CheckItem(position);
                    } else {

                    }

                }


            });
            commentAdapter.setOnLongClickListener(new CommentAdapter.OnLongClickListener() {
                @Override
                public void onLongClick(int position) {
                    if (!commentList.get(position).isCheckMode()) {
                        toCheckMode();
                    }
                }
            });
        }
    }
    public void toCheckMode() {
        //全部设置可选模式
        for (MyCommitBean item : commentList) {
            item.setCheck(false);//全部置为未选中
            item.setCheckMode(true);
        }
//        btnLeftTitle.setVisibility(View.INVISIBLE);
//        btnLeftTitleText.setVisibility(View.VISIBLE);
//        btnRightTitleText.setVisibility(View.VISIBLE);
        commentAdapter.notifyDataSetChanged();
//        bottomLine.setVisibility(View.VISIBLE);
    }
    //取消选中
    public void OutCheckMode() {
        for (MyCommitBean item : commentList) {
            item.setCheck(false);
        }
        recoverLay(false);
        commentAdapter.notifyDataSetChanged();
    }
    //选中
    public void CheckItem(int position) {
        commentList.get(position).setCheck(commentList.get(position).isCheck() ? false : true);
        //遍历是否有被选中的
        boolean hasCheck = false;
        for (MyCommitBean item : commentList) {
            if (item.isCheck()) {
                hasCheck = true;
                break;
            }
        }
        recoverLay(hasCheck);
        commentAdapter.notifyDataSetChanged();
    }
    //全选
    public void checkAllItem(boolean isCheck) {
        for (MyCommitBean item : commentList) {
            item.setCheck(isCheck);
        }
        commentAdapter.notifyDataSetChanged();
    }
}
