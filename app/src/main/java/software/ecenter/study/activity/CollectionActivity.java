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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import software.ecenter.study.Adapter.CoolectionAdapter;
import software.ecenter.study.R;
import software.ecenter.study.bean.HomeBean.ResourceBean;
import software.ecenter.study.bean.MineBean.MyCollectionDetailBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;

/**
 * dec 我的收藏
 */
public class CollectionActivity extends BaseActivity {

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
    @BindView(R.id.list_kecheng)
    RecyclerView listKecheng;
    @BindView(R.id.btn_down)
    Button btnDown;
    @BindView(R.id.btn_refresh_net)
    LinearLayout btnRefreshNet;
    @BindView(R.id.bottom_line)
    LinearLayout bottomLine;
    @BindView(R.id.ll_search_all_no_data)
    LinearLayout llSearchAllNoData;

    private List<ResourceBean> ListData = new ArrayList<>();
    private CoolectionAdapter mAdapter;

    private MyCollectionDetailBean mMyCollectionDetailBean;

    private List<ResourceBean> mListCheckData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        ButterKnife.bind(this);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        listKecheng.setLayoutManager(manager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserCollection();
    }

    public void getUserCollection() {
        if (!showNetWaitLoding()) {
            return;
        }

        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getUserCollectionNew(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        btnRefreshNet.setVisibility(View.GONE);
                        mMyCollectionDetailBean = ParseUtil.parseBean(response, MyCollectionDetailBean.class);
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

        if (mMyCollectionDetailBean == null) {
            return;
        }

        ListData = mMyCollectionDetailBean.getData().getData();
        mAdapter = new CoolectionAdapter(mContext, ListData);
        mAdapter.setItemClickListener(new CoolectionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, boolean isJump) {
                if (isJump) {
                    boolean hasCheck = false;
                    for (ResourceBean item : ListData) {
                        if (item.isCheck()) {
                            hasCheck = true;
                            break;
                        }
                    }
                    if (hasCheck) return;
                    if (!ListData.get(position).isTeacherResource()) {
                        Intent intent = new Intent(mContext, SeeResourcesVideoActivity.class);
                        intent.putExtra("resourceId", ListData.get(position).getResourceId());
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(mContext, SeeTeacherResourcesVideoActivity.class);
                        intent.putExtra("resourceId", ListData.get(position).getResourceId());
                        startActivity(intent);
                    }

                } else if (ListData.get(position).isCheckMode()) {
                    CheckItem(position);
                }
            }


        });
        mAdapter.setOnLongClickListener(new CoolectionAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(int position) {
                if (!ListData.get(position).isCheckMode()) {
                    toCheckMode();
                }
            }
        });
        listKecheng.setAdapter(mAdapter);
        toCheckMode();

        if (ListData.size() <= 0)
            llSearchAllNoData.setVisibility(View.VISIBLE);
        else
            llSearchAllNoData.setVisibility(View.GONE);

    }

    public void toCheckMode() {
        //全部设置可选模式
        for (ResourceBean item : ListData) {
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
        for (ResourceBean item : ListData) {
            item.setCheck(false);
        }
        btnLeftTitle.setVisibility(View.VISIBLE);
        btnLeftTitleText.setVisibility(View.INVISIBLE);
        btnRightTitleText.setVisibility(View.INVISIBLE);
        bottomLine.setVisibility(View.GONE);
        mAdapter.notifyDataSetChanged();
    }


    public void CheckItem(int position) {
        ListData.get(position).setCheck(ListData.get(position).isCheck() ? false : true);
        //遍历是否有被选中的
        boolean hasCheck = false;
        for (ResourceBean item : ListData) {
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
            bottomLine.setVisibility(View.GONE);
            btnLeftTitle.setVisibility(View.VISIBLE);
            btnLeftTitleText.setVisibility(View.INVISIBLE);
            btnRightTitleText.setVisibility(View.INVISIBLE);
        }
        mAdapter.notifyDataSetChanged();
    }

    public void checkAllItem(boolean isCheck) {
        for (ResourceBean item : ListData) {
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


    @OnClick({R.id.btn_left_title, R.id.btn_left_title_text, R.id.btn_refresh_net, R.id.btn_right_title_text, R.id.btn_down})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left_title:
                onBackPressed();
                break;
            case R.id.btn_left_title_text:
                checkAllItem(true);
                break;
            case R.id.btn_right_title_text:
                OutCheckMode();
                break;
            case R.id.btn_down:
                removeCollection();
                break;
            case R.id.btn_refresh_net:
                getUserCollection();
                break;
        }
    }

    public void removeCollection() {
        if (!showNetWaitLoding()) {
            return;
        }

        mListCheckData = new ArrayList<>();
        String Ids = "";
        for (ResourceBean item : ListData) {
            if (item.isCheck()) {
                Ids += item.getId() + ",";
                mListCheckData.add(item);
            }
        }

        if (mListCheckData.size() == 0) {
            ToastUtils.showToastSHORT(mContext, "请选择");
        }


        if (Ids.endsWith(",")) {
            Ids = Ids.substring(0, Ids.length() - 1);
        }

        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        map.put("ids", Ids);


        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).removeCollectionNew(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        for (ResourceBean item : mListCheckData) {
                            ListData.remove(item);
                        }
                        if (ListData.size() <= 0) {
                            llSearchAllNoData.setVisibility(View.VISIBLE);
                        }
                        OutCheckMode();
                        ToastUtils.showToastSHORT(mContext, "删除成功");

                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);

                    }

                });

    }


}
