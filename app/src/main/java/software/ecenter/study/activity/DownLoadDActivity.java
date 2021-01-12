package software.ecenter.study.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import software.ecenter.study.Adapter.DownloadDAdapter;
import software.ecenter.study.R;
import software.ecenter.study.View.LoadingDialog;
import software.ecenter.study.bean.HomeBean.ResourceBean;
import software.ecenter.study.bean.UpDataBean;
import software.ecenter.study.net.LoadFileModel;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.service.DownLoadIntentService;
import software.ecenter.study.tool.FileAccessor;
import software.ecenter.study.tool.FileManager;
import software.ecenter.study.utils.AccountUtil;
import software.ecenter.study.utils.NetworkUtil;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.PermissionUtils;
import software.ecenter.study.utils.ToastUtils;
import software.ecenter.study.utils.ToolUtil;

/**
 * dec 我的下载
 */
public class DownLoadDActivity extends BaseActivity implements DownloadDAdapter.OnItemClickListener {

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
    TextView btnDown;
    @BindView(R.id.btn_gengx)
    TextView btnGengx;
    @BindView(R.id.ll_btn_delete)
    LinearLayout liBtnDelete;
    @BindView(R.id.ll_search_all_no_data)
    LinearLayout llSearchAllNoData;

    private DownloadDAdapter mAdapter;
    public LoadingDialog mWatiDialog;
    private MyBroadcastReceiver mBroadcastReceiver;
    private boolean ischeck = false;//是否处于选择

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_load);
        ButterKnife.bind(this);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        listKecheng.setLayoutManager(manager);
        mAdapter = new DownloadDAdapter(this);
        mAdapter.setItemClickListener(this);
        listKecheng.setAdapter(mAdapter);
        mWatiDialog = new LoadingDialog(mContext);
        registerMyReceiver();
        PermissionUtils.newIntence().requestPerssion(this, ToolUtil.PERSSION_WRITE, 101, new PermissionUtils.IPermission() {
            @Override
            public void success(int code) {
                initView();
            }
        });
    }

    //初始化数据
    public void initView() {
        FileManager.getInstance(this).initLocalResourceList();
        List<ResourceBean> listdata = new ArrayList<>();
        for (ResourceBean resourceBean : FileManager.LocalResourceList) {
            if (resourceBean.getUserId() == null)
                resourceBean.setUserId(AccountUtil.getAccountMobile(mContext));
//            if (resourceBean.getUserId() != null && resourceBean.getUserId().equals(AccountUtil.getAccountMobile(mContext)))
            listdata.add(resourceBean);
        }
        for (ResourceBean resourceBean : DownLoadIntentService.allTask) {
            if (resourceBean.getUserId() == null)
                resourceBean.setUserId(AccountUtil.getAccountMobile(mContext));
//            if (resourceBean.getUserId() != null && resourceBean.getUserId().equals(AccountUtil.getAccountMobile(mContext)))
            listdata.add(resourceBean);
        }
        mAdapter.setData(listdata);
        showNoData();
        if (NetworkUtil.isConnected(mContext)) {
            //有网络 就获取更新提示
            getData();
        }
    }

    //无数据界面
    private void showNoData() {
        if (mAdapter != null) {
            List<ResourceBean> dataList = mAdapter.getDataList();
            if (dataList != null && dataList.size() <= 0) {
                llSearchAllNoData.setVisibility(View.VISIBLE);
            } else {
                llSearchAllNoData.setVisibility(View.GONE);
            }
        }
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
                        if (bean != null) {
                            UpDataBean.DataBean data = bean.getData();
                            if (data != null) {
                                List<ResourceBean> serviceList = data.getResourceList();
                                //比较本地与服务器的数据
                                equisData(serviceList);
                            }
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        ToastUtils.showToastLONG(mContext, msg);
                    }
                });
    }

    //比较本地与服务器的数据
    private void equisData(List<ResourceBean> serviceList) {
        if (serviceList != null && serviceList.size() > 0) {
            List<ResourceBean> dataList = new ArrayList<>();
            List<ResourceBean> beanList = mAdapter.getDataList();
            if (beanList != null && beanList.size() > 0) {
                for (int i = 0; i < serviceList.size(); i++) {
                    ResourceBean resourceBean = serviceList.get(i);
                    boolean local = getLocal(resourceBean.getResourceId());
                    if (local) {//有此记录
                        mAdapter.setUpName(resourceBean.getResourceName(),resourceBean.getResourceId());
                        //是否有资源
                        boolean havePath = getHavePath(resourceBean.getResourceId());
                        if (havePath) {
                            mAdapter.setUpAnwer(resourceBean.getResourceId(), resourceBean.isNeedUpdate());
                        } else {
                            mAdapter.setUpAnwer(resourceBean.getResourceId(), true);
                        }
                    } else {//本地没有此资源记录  添加到新的集合
                        resourceBean.setResourceTitle(resourceBean.getResourceName());
                        resourceBean.setAnwer(true);
                        resourceBean.setNeedUpdate(true);
                        resourceBean.setDownloadOk(true);
                        dataList.add(resourceBean);
                    }
                }
            } else {
                //本地没有数据
                for (int i = 0; i < serviceList.size(); i++) {
                    ResourceBean resourceBean = serviceList.get(i);
                    resourceBean.setResourceTitle(resourceBean.getResourceName());
                    resourceBean.setAnwer(true);
                    resourceBean.setNeedUpdate(true);
                    resourceBean.setDownloadOk(true);
                    dataList.add(resourceBean);
                }
            }
            mAdapter.setMoreData(dataList);
            showNoData();
        }
    }

    //是否有资源
    private boolean getHavePath(String resid) {
        List<ResourceBean> dataList = mAdapter.getDataList();
        for (int i = 0; i < dataList.size(); i++) {
            ResourceBean bean = dataList.get(i);
            if (bean != null && resid != null && !TextUtils.isEmpty(bean.getResourceId()) && resid.equals(bean.getResourceId())) {
                String localFakePath = bean.getLocalPathDir() + File.separator + FileAccessor.getFileName(bean.getResourceId()); //假名称
                File file = new File(localFakePath);
                //本地文件不存在
                if (!file.exists()) {
                    return false;
                }
            }
        }
        return true;
    }

    //判断本地是否有此资源有此记录
    private boolean getLocal(String resid) {
        List<ResourceBean> dataList = mAdapter.getDataList();
        for (int i = 0; i < dataList.size(); i++) {
            ResourceBean bean = dataList.get(i);
            if (bean != null && resid != null && !TextUtils.isEmpty(bean.getResourceId()) && resid.equals(bean.getResourceId())) {
                return true;
            }
        }
        return false;
    }

    //全选 和 取消
    public void checkAllItem(boolean isCheck) {
        mAdapter.setChoseAll(isCheck);
        if (isCheck) {
            liBtnDelete.setVisibility(View.VISIBLE);

            btnLeftTitle.setVisibility(View.GONE);
            btnLeftTitleText.setVisibility(View.VISIBLE);
            btnRightTitleText.setVisibility(View.VISIBLE);

            //是否有更新
            btnGengx.setClickable(mAdapter.getUpDataStatus());
            btnGengx.setSelected(mAdapter.getUpDataStatus());
        } else {
            liBtnDelete.setVisibility(View.GONE);

            btnLeftTitle.setVisibility(View.VISIBLE);
            btnLeftTitleText.setVisibility(View.GONE);
            btnRightTitleText.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.btn_left_title, R.id.btn_left_title_text, R.id.btn_right_title_text, R.id.btn_down, R.id.btn_gengx})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left_title://退出
                finish();
                break;
            case R.id.btn_left_title_text://全选
                checkAllItem(true);
                break;
            case R.id.btn_right_title_text://取消
                checkAllItem(false);
                break;
            case R.id.btn_down://删除
                toDelete();
                break;
            case R.id.btn_gengx://更新
                toGengxin();
                break;
        }
    }

    //更新
    private void toGengxin() {
        List<ResourceBean> datalist = mAdapter.getDataList();
        final List<ResourceBean> willDownload = new ArrayList<>();
        if (datalist != null && datalist.size() > 0) {
            for (int i = 0; i < datalist.size(); i++) {
                boolean check = datalist.get(i).isCheck();
                boolean update = datalist.get(i).isNeedUpdate();
                //选中且有更新的才更新
                if (check && update) {
                    ResourceBean item = new ResourceBean();
                    item.setResourceId(datalist.get(i).getResourceId());
                    item.setResourceTitle(datalist.get(i).getResourceName());
                    item.setResourceUrl(datalist.get(i).getResourceUrl());
                    item.setResourceType(datalist.get(i).getResourceType());
                    item.setResourceSize(datalist.get(i).getResourceSize());
                    item.setResourceTime(datalist.get(i).getResourceTime());
                    item.setType(datalist.get(i).getType());
                    item.setDownload(true);
                    item.setUserId(AccountUtil.getAccountMobile(mContext));
                    willDownload.add(item);
                }
            }
        }
        PermissionUtils.newIntence().requestPerssion(this, ToolUtil.PERSSION_WRITE, 101, new PermissionUtils.IPermission() {
            @Override
            public void success(int code) {
                DownLoadIntentService.startActionDown(mContext, willDownload);
                checkAllItem(false);
                ischeck = false;
            }
        });

    }

    //记录删除
    private void toDelete() {
        mWatiDialog.show();
        final List<ResourceBean> deleteData = new ArrayList<>();
        for (ResourceBean item : mAdapter.getDataList()) {
            if (item.isCheck()) {
                deleteData.add(item);
            }
        }
        if (deleteData.size() != 0) {
            ToastUtils.showToastSHORT(DownLoadDActivity.this, "准备删除" + deleteData.size() + "个资源");
        }
        DownLoadIntentService.startActionDelete(mContext, deleteData);
    }


    //列表选择事件
    @Override
    public void onItemClick(View v, int position) {
        switch (v.getId()) {
            case R.id.check_img://选择
                CheckItem(position);
                break;
            case R.id.rl_card_item://详情
                if (!ischeck) {
                    //进入资源
                    Intent intent = new Intent(mContext, SeeResourcesVideoActivity.class);
                    intent.putExtra("resourceId", mAdapter.getDataList().get(position).getResourceId());
                    startActivity(intent);
                }
                break;
        }
    }

    //设置选中
    public void CheckItem(int position) {
        mAdapter.setChoseOne(position);
        ischeck = mAdapter.getChose();
        if (ischeck) {
            liBtnDelete.setVisibility(View.VISIBLE);
            btnLeftTitle.setVisibility(View.GONE);
            btnLeftTitleText.setVisibility(View.VISIBLE);
            btnRightTitleText.setVisibility(View.VISIBLE);

            //是否有更新
            btnGengx.setClickable(mAdapter.getUpDataStatus());
            btnGengx.setSelected(mAdapter.getUpDataStatus());
        } else {
            liBtnDelete.setVisibility(View.GONE);
            btnLeftTitle.setVisibility(View.VISIBLE);
            btnLeftTitleText.setVisibility(View.GONE);
            btnRightTitleText.setVisibility(View.GONE);
        }
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DownLoadIntentService.RESULT_DOWN)) {//下载全部
                ArrayList<String> errId = intent.getStringArrayListExtra("error_id");
                if (errId != null && errId.size() > 0) {
                    for (int i = 0; i < errId.size(); i++) {
                        mAdapter.refreshOneData(errId.get(i), false);
                    }
                }
            }
            if (intent.getAction().equals(DownLoadIntentService.RESULT_DELETE)) {
                String num = intent.getStringExtra("delete_num");
                List<ResourceBean> list = (List<ResourceBean>) intent.getSerializableExtra("data");
                if (!num.equals("0")) {
                    ToastUtils.showToastSHORT(DownLoadDActivity.this, "已经删除" + num + "个资源");
                } else {
                    ToastUtils.showToastSHORT(DownLoadDActivity.this, "请选择您要删除的资源");
                }
                deleteOk(list);
            }
            //更新单条数据
            if (intent.getAction().equals(DownLoadIntentService.RESULT_DOWN_ONE)) {
                String id = intent.getStringExtra("resourcesId");
                mAdapter.refreshOneData(id, true);
            }
        }
    }

    //删除
    private void deleteOk(List<ResourceBean> list) {
        if (!this.isFinishing())
            mWatiDialog.dismiss();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                ResourceBean resourceBean = list.get(i);
                mAdapter.delOneData(resourceBean.getResourceId());
            }
        }
        checkAllItem(false);
        showNoData();
    }

    private void registerMyReceiver() {
        if (mBroadcastReceiver == null) {
            //注册广播
            mBroadcastReceiver = new MyBroadcastReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(DownLoadIntentService.RESULT_DELETE);
            intentFilter.addAction(DownLoadIntentService.RESULT_DOWN_ONE);
            intentFilter.addAction(DownLoadIntentService.RESULT_DOWN);
            registerReceiver(mBroadcastReceiver, intentFilter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadFileModel.ProgressHelper.setProgressHandler(new LoadFileModel.ProgressBean.ProgressHandler() {
            @Override
            protected void onProgress(long progress, long total, long speed, boolean done, String resouceId) {
                Log.e("onProgress", String.format("%d%% done\n", progress * 100 / total) + "   resouceId-->" + resouceId);
                if (!ischeck)
                    mAdapter.refreshDownLoadSize(resouceId, progress * 100 / total + "");
            }
        });
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
