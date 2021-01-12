package software.ecenter.study.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import software.ecenter.study.Adapter.CommentAdapter;
import software.ecenter.study.R;
import software.ecenter.study.bean.HomeBean.ExerciseBean;
import software.ecenter.study.bean.HomeBean.ResourceBean;
import software.ecenter.study.bean.MineBean.MyCommitBean;
import software.ecenter.study.bean.MineBean.MyCommitDetailBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;

/**
 * dec 我的评论
 */
public class CommentActivity extends BaseActivity {

    @BindView(R.id.btn_left_title)
    ImageView btnLeftTitle;
    @BindView(R.id.btn_left_title_text)
    TextView btnLeftTitleText;
    @BindView(R.id.title_content)
    TextView titleContent;
    @BindView(R.id.btn_right_title_text)
    TextView btnRightTitleText;
    @BindView(R.id.title_temp)
    RelativeLayout titleTemp;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.btn_delete)
    Button btnDelete;
    @BindView(R.id.btn_refresh_net)
    LinearLayout btnRefreshNet;
    @BindView(R.id.bottom_line)
    LinearLayout bottomLine;
    @BindView(R.id.ll_search_all_no_data)
    LinearLayout llSearchAllNoData;

    private List<MyCommitBean> ListData = new ArrayList<>();
    private CommentAdapter mAdapter;
    private MyCommitDetailBean mMyCommitDetailBean;
    private List<MyCommitBean> mListCheckData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        getUserComments();
    }

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
                        mMyCommitDetailBean = ParseUtil.parseBean(response, MyCommitDetailBean.class);
                        btnRefreshNet.setVisibility(View.GONE);
                        initView();

                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        btnRefreshNet.setVisibility(View.VISIBLE);
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });

    }

    public void initView() {
        if (mMyCommitDetailBean == null) {
            llSearchAllNoData.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            return;
        }

        ListData = mMyCommitDetailBean.getData().getData();
        mAdapter = new CommentAdapter(mContext, ListData);
        mAdapter.setItemClickListener(new CommentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, boolean isJump) {
                if (isJump) {
                    boolean hasCheck = false;
                    for (MyCommitBean item : ListData) {
                        if (item.isCheck()) {
                            hasCheck = true;
                            break;
                        }
                    }
                    if (hasCheck) return;
                    Intent intent = new Intent(mContext, SeeResourcesVideoActivity.class);
                    intent.putExtra("resourceId", ListData.get(position).getResourceId());
                    startActivity(intent);
                } else if (ListData.get(position).isCheckMode()) {
                    CheckItem(position);
                } else {

                }

            }


        });
        mAdapter.setOnLongClickListener(new CommentAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(int position) {
                if (!ListData.get(position).isCheckMode()) {
                    toCheckMode();
                }
            }
        });
        recyclerView.setAdapter(mAdapter);
        if (ListData.size() <= 0) {
            llSearchAllNoData.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            llSearchAllNoData.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        toCheckMode();

    }

    public void toCheckMode() {
        //全部设置可选模式
        for (MyCommitBean item : ListData) {
            item.setCheck(false);//全部置为未选中
            item.setCheckMode(true);
        }
//        btnLeftTitle.setVisibility(View.INVISIBLE);
//        btnLeftTitleText.setVisibility(View.VISIBLE);
//        btnRightTitleText.setVisibility(View.VISIBLE);
        mAdapter.notifyDataSetChanged();
//        bottomLine.setVisibility(View.VISIBLE);
    }

    public void OutCheckMode() {
        //全部设置可选模式
//        for (MyCommitBean item : ListData) {
//            item.setCheckMode(false);
//        }
        for (MyCommitBean item : ListData) {
            item.setCheck(false);
        }
        btnLeftTitle.setVisibility(View.VISIBLE);
        btnLeftTitleText.setVisibility(View.INVISIBLE);
        btnRightTitleText.setVisibility(View.INVISIBLE);
        mAdapter.notifyDataSetChanged();
        bottomLine.setVisibility(View.GONE);
    }


    public void CheckItem(int position) {
        ListData.get(position).setCheck(ListData.get(position).isCheck() ? false : true);
        //遍历是否有被选中的
        boolean hasCheck = false;
        for (MyCommitBean item : ListData) {
            if (item.isCheck()) {
                hasCheck = true;
                break;
            }
        }
        if (hasCheck) {
            bottomLine.setVisibility(View.VISIBLE);
            btnLeftTitle.setVisibility(View.INVISIBLE);
            btnLeftTitleText.setVisibility(View.VISIBLE);
            btnRightTitleText.setVisibility(View.VISIBLE);
        } else {
            btnLeftTitle.setVisibility(View.VISIBLE);
            bottomLine.setVisibility(View.GONE);
            btnLeftTitleText.setVisibility(View.INVISIBLE);
            btnRightTitleText.setVisibility(View.INVISIBLE);
        }
        mAdapter.notifyDataSetChanged();
    }

    public void checkAllItem(boolean isCheck) {
        for (MyCommitBean item : ListData) {
            item.setCheck(isCheck);
        }
        //全部没选中时
        if (!isCheck) {
            btnLeftTitle.setVisibility(View.VISIBLE);
            btnLeftTitleText.setVisibility(View.INVISIBLE);
            btnRightTitleText.setVisibility(View.INVISIBLE);
        }
        mAdapter.notifyDataSetChanged();
    }


    @OnClick({R.id.btn_left_title, R.id.btn_left_title_text, R.id.btn_right_title_text, R.id.btn_delete, R.id.btn_refresh_net})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left_title:
                finish();
                break;
            case R.id.btn_left_title_text:
                checkAllItem(true);
                break;
            case R.id.btn_right_title_text:
                OutCheckMode();
                break;
            case R.id.btn_delete:
                deleteUserComment();
                break;
            case R.id.btn_refresh_net:
                getUserComments();
                break;
        }
    }

    public void deleteUserComment() {
        if (!showNetWaitLoding()) {
            return;
        }

        mListCheckData = new ArrayList<>();
        String Ids = "";
        for (MyCommitBean item : ListData) {
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
                            ListData.remove(item);
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

}
