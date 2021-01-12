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
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import software.ecenter.study.Adapter.DownloadDAdapter;
import software.ecenter.study.Adapter.UpDataAdapter;
import software.ecenter.study.R;
import software.ecenter.study.View.LoadingDialog;
import software.ecenter.study.bean.HomeBean.ResourceBean;
import software.ecenter.study.bean.MineBean.UpdataBean;
import software.ecenter.study.bean.MineBean.UpdataDetailBean;
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
 * Message 上传下载
 * Create by Administrator
 * Create by 2019/9/4
 */
public class DownLoadAndUpdataActivity extends BaseActivity implements DownloadDAdapter.OnItemClickListener {
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_all)
    TextView tv_all;
    @BindView(R.id.tv_cancle)
    TextView tv_cancle;
    @BindView(R.id.rv_list)
    RecyclerView rv_list;
    @BindView(R.id.ll_delete)
    LinearLayout ll_delete;
    @BindView(R.id.tv_del)
    TextView tv_del;
    @BindView(R.id.tv_gengx)
    TextView tv_gengx;
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

    private int mType = 1;
    private DownloadDAdapter mAdapter;
    public LoadingDialog mWatiDialog;
    private MyBroadcastReceiver mBroadcastReceiver;
    private boolean ischeck = false;//是否处于选择
    private List<ResourceBean> localData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloadandupdata);
        ButterKnife.bind(this);
        rv_list.setLayoutManager(new LinearLayoutManager(mContext));
        getData();
    }

    //上传
    public void getUserUpload() {
        if (!showNetWaitLoding()) {
            return;
        }
        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getUserUpload(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        UpdataDetailBean mUpdataDetailBean = ParseUtil.parseBean(response, UpdataDetailBean.class);
                        ll_error.setVisibility(View.GONE);
                        if (mUpdataDetailBean == null) {
                            ll_no.setVisibility(View.VISIBLE);
                            rv_list.setVisibility(View.GONE);
                            return;
                        }
                        List<UpdataBean> listData = mUpdataDetailBean.getData().getData();
                        UpDataAdapter mAdapter = new UpDataAdapter(mContext, listData);
                        rv_list.setAdapter(mAdapter);
                        if (listData.size() <= 0) {
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

    //复原控件状态
    private void recoverLay(boolean hasCheck) {
        iv_back.setVisibility(hasCheck ? View.GONE : View.VISIBLE);
        tv_all.setVisibility(hasCheck ? View.VISIBLE : View.GONE);
        tv_cancle.setVisibility(hasCheck ? View.VISIBLE : View.GONE);
        ll_delete.setVisibility(hasCheck ? View.VISIBLE : View.GONE);
    }

    //切换tab
    private void showLay(int type) {
        if (mType == type) {
            return;
        }
        ll_no.setVisibility(View.GONE);
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

    @OnClick({R.id.iv_back, R.id.tv_all, R.id.tv_cancle, R.id.tv_del, R.id.tv_gengx, R.id.ll_left, R.id.ll_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back://退出
                finish();
                break;
            case R.id.tv_all://全选
                checkAllItem(true);
                break;
            case R.id.tv_cancle://取消
                checkAllItem(false);
                break;
            case R.id.tv_del://删除
                toDelete();
                break;
            case R.id.tv_gengx://更新
                toGengxin();
                break;
            case R.id.ll_left://上传
                showLay(1);
                break;
            case R.id.ll_right://下载
                showLay(2);
                break;
            case R.id.ll_error://错误界面
                getData();
                break;
        }
    }

    //获取数据
    private void getData() {
        if (mType == 1) {//上传
            getUserUpload();
        } else {//下载
            rv_list.setVisibility(View.VISIBLE);
            mAdapter = new DownloadDAdapter(this);
            mAdapter.setItemClickListener(this);
            rv_list.setAdapter(mAdapter);
            mWatiDialog = new LoadingDialog(mContext);
            registerMyReceiver();
            PermissionUtils.newIntence().requestPerssion(this, ToolUtil.PERSSION_WRITE, 101, new PermissionUtils.IPermission() {
                @Override
                public void success(int code) {
                    initView();
                }
            });
        }
    }

    //初始化数据
    public void initView() {
        FileManager.getInstance(this).initLocalResourceList();
        localData = new ArrayList<>();
        for (ResourceBean resourceBean : FileManager.LocalResourceList) {
            if (resourceBean.getUserId() == null)
                resourceBean.setUserId(AccountUtil.getAccountMobile(mContext));
//            if (resourceBean.getUserId() != null && resourceBean.getUserId().equals(AccountUtil.getAccountMobile(mContext)))
            localData.add(resourceBean);
        }
        for (ResourceBean resourceBean : DownLoadIntentService.allTask) {
            if (resourceBean.getUserId() == null)
                resourceBean.setUserId(AccountUtil.getAccountMobile(mContext));
//            if (resourceBean.getUserId() != null && resourceBean.getUserId().equals(AccountUtil.getAccountMobile(mContext)))
            localData.add(resourceBean);
        }
        if (NetworkUtil.isConnected(mContext)) {
            //有网络 就获取更新提示
            getDownData();
        } else {
            mAdapter.setData(localData);
            showNoData();
        }
    }

    //无数据界面
    private void showNoData() {
        if (mAdapter != null) {
            List<ResourceBean> dataList = mAdapter.getDataList();
            if (dataList != null && dataList.size() <= 0) {
                ll_no.setVisibility(View.VISIBLE);
            } else {
                ll_no.setVisibility(View.GONE);
            }
        }
    }

    //获取更新
    private void getDownData() {
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
                        showNoData();
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
            if (localData != null && localData.size() > 0) {
                for (int i = 0; i < serviceList.size(); i++) {
                    ResourceBean resourceBean = serviceList.get(i);
                    //有此记录
                    boolean local = getLocal(resourceBean.getResourceId());
                    //是否有资源
                    boolean havePath = getHavePath(resourceBean.getResourceId());
                    resourceBean.setResourceTitle(resourceBean.getResourceName());
                    resourceBean.setAnwer(true);
                    resourceBean.setDownloadOk(true);
                    if (!local || !havePath) {
                        resourceBean.setNeedUpdate(true);
                    }
                    dataList.add(resourceBean);
//                    if (local) {
//                        mAdapter.setUpName(resourceBean.getResourceName(), resourceBean.getResourceId());
//                        if (havePath) {
//                            mAdapter.setUpAnwer(resourceBean.getResourceId(), resourceBean.isNeedUpdate());
//                        } else {
//                            mAdapter.setUpAnwer(resourceBean.getResourceId(), true);
//                        }
//                    } else {//本地没有此资源记录  添加到新的集合
//                        resourceBean.setResourceTitle(resourceBean.getResourceName());
//                        resourceBean.setAnwer(true);
//                        resourceBean.setNeedUpdate(true);
//                        resourceBean.setDownloadOk(true);
//                        dataList.add(resourceBean);
//                    }
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
        for (int i = 0; i < localData.size(); i++) {
            ResourceBean bean = localData.get(i);
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
        for (int i = 0; i < localData.size(); i++) {
            ResourceBean bean = localData.get(i);
            if (bean != null && resid != null && !TextUtils.isEmpty(bean.getResourceId()) && resid.equals(bean.getResourceId())) {
                return true;
            }
        }
        return false;
    }

    //全选 和 取消
    public void checkAllItem(boolean isCheck) {
        mAdapter.setChoseAll(isCheck);
        recoverLay(isCheck);
        if (isCheck) {
            //是否有更新
            tv_gengx.setClickable(mAdapter.getUpDataStatus());
            tv_gengx.setSelected(mAdapter.getUpDataStatus());
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
            ToastUtils.showToastSHORT(DownLoadAndUpdataActivity.this, "准备删除" + deleteData.size() + "个资源");
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
        recoverLay(ischeck);
        if (ischeck) {
            //是否有更新
            tv_gengx.setClickable(mAdapter.getUpDataStatus());
            tv_gengx.setSelected(mAdapter.getUpDataStatus());
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
                    ToastUtils.showToastSHORT(DownLoadAndUpdataActivity.this, "已经删除" + num + "个资源");
                } else {
                    ToastUtils.showToastSHORT(DownLoadAndUpdataActivity.this, "请选择您要删除的资源");
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
                Log.e("DownLoad", String.format("%d%% done\n", progress * 100 / total) + "   resouceId-->" + resouceId);
                if (!ischeck && mType != 1)
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
