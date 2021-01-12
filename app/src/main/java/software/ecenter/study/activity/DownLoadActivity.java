package software.ecenter.study.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
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
import software.ecenter.study.Adapter.DownloadAdapter;
import software.ecenter.study.R;
import software.ecenter.study.View.LoadingDialog;
import software.ecenter.study.bean.BookInfoBean;
import software.ecenter.study.bean.HomeBean.ResourceBean;
import software.ecenter.study.bean.HomeBean.ResourceIdBean;
import software.ecenter.study.bean.UpDataBean;
import software.ecenter.study.net.LoadFileModel;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.service.DownLoadIntentService;
import software.ecenter.study.tool.FileManager;
import software.ecenter.study.utils.AccountUtil;
import software.ecenter.study.utils.NetworkUtil;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;

/**
 * dec 我的下载
 */
public class DownLoadActivity extends BaseActivity {

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
    @BindView(R.id.ll_btn_delete)
    LinearLayout liBtnDelete;
    @BindView(R.id.ll_search_all_no_data)
    LinearLayout llSearchAllNoData;

    private List<ResourceBean> ListData = new ArrayList<>();
    private DownloadAdapter mAdapter;


    public LoadingDialog mWatiDialog;

    private MyBroadcastReceiver mBroadcastReceiver;

    private boolean ischeck = false;
    private List<ResourceBean> serviceList;//服务器记录

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_load);
        ButterKnife.bind(this);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        listKecheng.setLayoutManager(manager);
        mWatiDialog = new LoadingDialog(mContext);
        registerMyReceiver();
        initView();

    }

    //获取更新
    private void getData() {
        JSONObject json = new JSONObject();
        try {
            json.put("curpage", 0);
            json.put("pageNum", 10000);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).myDownloads(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        UpDataBean bean = ParseUtil.parseBean(response, UpDataBean.class);
                        if (bean!=null){
                            UpDataBean.DataBean data = bean.getData();
                            if (data!=null){
                                serviceList = data.getResourceList();
                                //服务器数据记录
                                if (serviceList!=null &&serviceList.size()>0){
                                    if (ListData!=null &&ListData.size()>0){

                                    }else {
                                        //本地没有数据
                                        for (int i = 0; i < serviceList.size(); i++) {
                                            ResourceBean resourceBean = serviceList.get(i);
                                            resourceBean.setAnwer(true);
                                            resourceBean.setNeedUpdate(true);
                                            resourceBean.setDownloadOk(true);
                                            resourceBean.setCheckMode(true);
                                            ListData.add(resourceBean);
                                        }
                                        mAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        ToastUtils.showToastLONG(mContext, msg);
                    }
                });
    }


    @Override
    protected void onResume() {
        super.onResume();
        ischeck = false;
        LoadFileModel.ProgressHelper.setProgressHandler(new LoadFileModel.ProgressBean.ProgressHandler() {
            @Override
            protected void onProgress(long progress, long total, long speed, boolean done, String resouceId) {
                Log.e("onProgress", String.format("%d%% done\n", progress * 100 / total) + "   resouceId-->" + resouceId);
//                ToastUtils.showToastSHORT(mContext, "已下载" + progress * 100 / total + "%");
                for (ResourceBean resourceBean : ListData) {
                    if (resourceBean.getResourceId().equals(resouceId)) {
                        resourceBean.setResourceDownloadSize(progress * 100 / total + "");
                        if (!ischeck)
                            mAdapter.refreshData(ListData);
                    }
                }
            }
        });
    }

    //初始化数据
    public void initView() {
        FileManager.getInstance(this).initLocalResourceList();
        ListData.clear();
        for (ResourceBean resourceBean : FileManager.LocalResourceList) {
            if (resourceBean.getUserId() == null)
                resourceBean.setUserId(AccountUtil.getAccountMobile(mContext));
            if (resourceBean.getUserId() != null && resourceBean.getUserId().equals(AccountUtil.getAccountMobile(mContext)))
                ListData.add(resourceBean);
        }
        for (ResourceBean resourceBean : DownLoadIntentService.allTask) {
            if (resourceBean.getUserId() == null)
                resourceBean.setUserId(AccountUtil.getAccountMobile(mContext));
            if (resourceBean.getUserId() != null && resourceBean.getUserId().equals(AccountUtil.getAccountMobile(mContext)))
                ListData.add(resourceBean);
        }
        mAdapter = new DownloadAdapter(mContext, ListData);
        mAdapter.setItemClickListener(new DownloadAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, boolean isJump) {
                if (isJump) {//详情
                    boolean hasCheck = false;
                    for (ResourceBean item : ListData) {
                        if (item.isCheck()) {
                            hasCheck = true;
                            break;
                        }
                    }
                    if (hasCheck) {
                        return;
                    }
                    //进入资源
                    Intent intent = new Intent(mContext, SeeResourcesVideoActivity.class);
                    intent.putExtra("resourceId", ListData.get(position).getResourceId());
                    startActivity(intent);
                } else if (ListData.get(position).isCheckMode()) {//选中
                    CheckItem(position);
                } else {

                }
            }


        });
        mAdapter.setOnLongClickListener(new DownloadAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(int position) {
                if (!ListData.get(position).isCheckMode()) {
                    toCheckMode();
                }
            }
        });
        listKecheng.setAdapter(mAdapter);
        if (ListData.size() <= 0) {
            llSearchAllNoData.setVisibility(View.VISIBLE);
        } else {
            llSearchAllNoData.setVisibility(View.GONE);
        }
        mAdapter.setonTouchDown(new DownloadAdapter.OnTouchDown() {
            @Override
            public void onTouchDown(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ischeck = true;
                        break;

                    case MotionEvent.ACTION_MOVE:

                        break;

                    case MotionEvent.ACTION_UP:
                        ischeck = false;
                        break;

                    case MotionEvent.ACTION_CANCEL:
                        ischeck = false;
                        break;

                }
            }
        });
        toCheckMode();
        if (NetworkUtil.isConnected(mContext)) {
            //有网络 就获取更新提示
            getData();
        }

    }

    public void toCheckMode() {
        //全部设置可选模式
        for (ResourceBean item : ListData) {
            item.setCheck(false);//全部置为未选中
            item.setCheckMode(true);
        }
       /* btnLeftTitle.setVisibility(View.INVISIBLE);
        btnLeftTitleText.setVisibility(View.VISIBLE);
        btnRightTitleText.setVisibility(View.VISIBLE);
*/
        mAdapter.notifyDataSetChanged();
    }

    public void OutCheckMode() {
        //全部设置可选模式
        /*for (ResourceBean item : ListData) {
            item.setCheckMode(false);
        }*/
        for (ResourceBean item : ListData) {
            item.setCheck(false);
        }
        liBtnDelete.setVisibility(View.GONE);
        btnLeftTitle.setVisibility(View.VISIBLE);
        btnLeftTitleText.setVisibility(View.INVISIBLE);
        btnRightTitleText.setVisibility(View.INVISIBLE);
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
            liBtnDelete.setVisibility(View.VISIBLE);
            btnLeftTitle.setVisibility(View.INVISIBLE);
            btnLeftTitleText.setVisibility(View.VISIBLE);
            btnRightTitleText.setVisibility(View.VISIBLE);
        } else {
            liBtnDelete.setVisibility(View.GONE);
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

    @OnClick({R.id.btn_left_title, R.id.btn_left_title_text, R.id.btn_right_title_text, R.id.btn_down})
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
            case R.id.btn_down:
                //OutCheckMode();
                toDelete();
                break;
        }
    }
    //记录删除
    private void toDelete() {
        mWatiDialog.show();
        final List<ResourceBean> deleteData = new ArrayList<>();
        for (ResourceBean item : ListData) {
            if (item.isCheck()) {
                //TODO 服务器是否有记录
                deleteData.add(item);
            }
        }
        if (deleteData.size() != 0) {
            ToastUtils.showToastSHORT(DownLoadActivity.this, "准备删除" + deleteData.size() + "个资源");
        }
        DownLoadIntentService.startActionDelete(mContext, deleteData);
    }

    private void deleteOk() {
        if (!this.isFinishing())
            mWatiDialog.dismiss();
        FileManager.getInstance(this).initLocalResourceList();
        ListData = FileManager.LocalResourceList;
        toCheckMode();
        mAdapter.refreshData(ListData);
    }

    private void registerMyReceiver() {
        if (mBroadcastReceiver == null) {
            //注册广播
            mBroadcastReceiver = new MyBroadcastReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(DownLoadIntentService.RESULT_DELETE);
            intentFilter.addAction(DownLoadIntentService.RESULT_DOWN_ONE);
            registerReceiver(mBroadcastReceiver, intentFilter);
        }
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DownLoadIntentService.RESULT_DELETE)) {
                String num = intent.getStringExtra("delete_num");
                if (!num.equals("0")) {
                    ToastUtils.showToastSHORT(DownLoadActivity.this, "已经删除" + num + "个资源");
                } else {
                    ToastUtils.showToastSHORT(DownLoadActivity.this, "请选择您要删除的资源");
                }
                deleteOk();
            }
            if (intent.getAction().equals(DownLoadIntentService.RESULT_DOWN_ONE)) {
                String id = intent.getStringExtra("resourcesId");
                if (ListData != null) {
                    for (ResourceBean item : ListData) {
                        if (item.getResourceId().equals(id)) {
                            item.setDownloadOk(true);
                            if (mAdapter != null) {
                                mAdapter.refreshData(ListData);
                            }
                        }
                    }
                }

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
        }
        mBroadcastReceiver = null;
    }


}
