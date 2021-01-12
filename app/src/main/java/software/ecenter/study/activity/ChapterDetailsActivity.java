package software.ecenter.study.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;

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
import software.ecenter.study.Adapter.ZiyuanDownLoadAdapter;
import software.ecenter.study.Interface.OnItemListener;
import software.ecenter.study.R;
import software.ecenter.study.View.AutoSplitTextView;
import software.ecenter.study.View.MyDialog;
import software.ecenter.study.bean.BindBookBean;
import software.ecenter.study.bean.HomeBean.ChapterDetailBean;
import software.ecenter.study.bean.HomeBean.ResourceBean;
import software.ecenter.study.bean.HomeBean.SecurityCodeBean;
import software.ecenter.study.net.LoadFileModel;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.service.DownLoadIntentService;
import software.ecenter.study.tool.FileManager;
import software.ecenter.study.utils.AccountUtil;
import software.ecenter.study.utils.NetworkUtil;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.PermissionUtils;
import software.ecenter.study.utils.ToastUtils;
import software.ecenter.study.utils.ToolUtil;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

/***
 * dsc 资源详情
 *
 * */
public class ChapterDetailsActivity extends BaseActivity {

    @BindView(R.id.btn_left_title)
    ImageView btnLeftTitle;
    @BindView(R.id.title_content)
    TextView titleContent;
    @BindView(R.id.title_temp)
    RelativeLayout titleTemp;
    @BindView(R.id.list_kecheng)
    RecyclerView listKecheng;
    @BindView(R.id.btn_down)
    Button btnDown;
    @BindView(R.id.btn_buy)
    Button btnBuy;
    @BindView(R.id.btn_left_title_text)
    TextView btnLeftTitleText;
    @BindView(R.id.btn_right_title_text)
    TextView btnRightTitleText;
    @BindView(R.id.btn_refresh_net)
    LinearLayout btnRefreshNet;
    @BindView(R.id.ll_search_all_no_data)
    LinearLayout llSearchAllNoData;
    @BindView(R.id.tv_tip)
    AutoSplitTextView tvTip;

    private List<ResourceBean> ResourceData = new ArrayList<>();
    private ZiyuanDownLoadAdapter ziyuanAdapter;

    private String chapterId;
    private String chapterName;
    private int newBatch;
    private int type;//1精品课程0图书
    private ChapterDetailsActivity.MyBroadcastReceiver mBroadcastReceiver;


    //扫一扫
    private int REQUEST_CODE_SCAN = 114;
    private ChapterDetailBean mChapterDetailBean;
    private boolean ischeck = false;
    private String buyType = "4";
    private boolean isInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_details);
        ButterKnife.bind(this);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        listKecheng.setLayoutManager(manager);
        registerMyReceiver();

        chapterId = getIntent().getStringExtra("chapterId");
        chapterName = getIntent().getStringExtra("chapterName");
        type = getIntent().getIntExtra("type",0);
        newBatch = getIntent().getIntExtra("newBatch",0);
        if (type == 1){//精品课程章节列表
            buyType = "7";
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData(false);
        hasDownload();
        ischeck = false;
        LoadFileModel.ProgressHelper.setProgressHandler(new LoadFileModel.ProgressBean.ProgressHandler() {
            @Override
            protected void onProgress(final long progress, final long total, long speed, boolean done, String resouceId) {
                //Log.e("onProgress", String.format("%d%% done\n", progress * 100 / total));
//                ToastUtils.showToastSHORT(mContext, "已下载" + progress * 100 / total + "%");
                for (ResourceBean resourceBean : ResourceData) {
                    if (resourceBean.getResourceId().equals(resouceId)) {
                        resourceBean.setResourceDownloadSize(progress * 100 / total + "");
                        if (!ischeck)
                            ziyuanAdapter.notifyDataSetChanged();

                    }
                }
            }
        });
    }

    public void hasDownload() {
        if (ResourceData != null) {
            FileManager.getInstance(this).initLocalResourceList();
            for (ResourceBean item : ResourceData) {
                //判断哪些在下载中
                for (ResourceBean downItem : DownLoadIntentService.allTask) {
                    if (item.getResourceId().equals(downItem.getResourceId())) {
                        item.setDownload(true);
                    }
                }
                //判断哪些在下载过
                for (ResourceBean downItem : FileManager.LocalResourceList) {
                    if (item.getResourceId().equals(downItem.getResourceId())) {
                        item.setDownloadOk(true);
                    }
                }

            }
        }
    }
    //获取数据
    public void getData(boolean isShow) {
        if (isShow) {
            if (!showNetWaitLoding()) {
                return;
            }
        }
        if (type == 1){
            getKeChengChapter();
        }else {
            getBookChapter();
        }
    }
    //课程
    private void getKeChengChapter(){

        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        map.put("id", chapterId);
        map.put("newBatch", newBatch+"");
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).homeCurriculumChapterDetail(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        btnRefreshNet.setVisibility(View.GONE);
                        ChapterDetailBean bean = ParseUtil.parseBean(response, ChapterDetailBean.class);
                        setResponseView(bean);
                        hasDownload();
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        btnRefreshNet.setVisibility(View.VISIBLE);
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }
    //图书
    private void getBookChapter(){

        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        map.put("id", chapterId);

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).homeChapterDetail(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        btnRefreshNet.setVisibility(View.GONE);
                        ChapterDetailBean bean = ParseUtil.parseBean(response, ChapterDetailBean.class);
                        setResponseView(bean);
                        hasDownload();

                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        btnRefreshNet.setVisibility(View.VISIBLE);
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }


    public void setResponseView(ChapterDetailBean bean) {
        mChapterDetailBean = bean;
        titleContent.setText(chapterName);
        if(chapterId.equals("592750")){
            titleContent.setText("二维码修正");
        }
        if (bean.getData().isBuy() || !bean.getData().isSingleBuy()) {
            btnBuy.setVisibility(View.GONE);
        }

        if (bean.getData().isHaveSpecial()) {
            tvTip.setVisibility(View.VISIBLE);
            tvTip.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    tvTip.setText(getResources().getText(R.string.chapter_tip));
                    tvTip.setText(autoSplitText(tvTip, "●"));
                    tvTip.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });

        } else {
            tvTip.setVisibility(View.GONE);
        }

        ResourceData = bean.getData().getResourceList();
        if (isDownBtnGray()) {
            btnDown.setBackgroundResource(R.drawable.background_gray_shape_circle);
            btnDown.setEnabled(false);
        }else {
            btnDown.setBackgroundResource(R.drawable.btn_all_shape_circle_blue_click);
            btnDown.setEnabled(true);
        }
        ziyuanAdapter = new ZiyuanDownLoadAdapter(mContext, ResourceData);
        ziyuanAdapter.setItemClickListener(new ZiyuanDownLoadAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, boolean isJump) {
                if (isJump) {
                    if (!ResourceData.get(position).isHaveResourceFile()) {
                        ToastUtils.showToastLONG(mContext, "资源即将上传");
                        return;
                    }
                    boolean hasCheck = false;
                    for (ResourceBean item : ResourceData) {
                        if (item.isCheck()) {
                            hasCheck = true;
                            break;
                        }
                    }
                    if (hasCheck) return;
                    Intent intent = new Intent(mContext, SeeResourcesVideoActivity.class);
                    intent.putExtra("resourceId", ResourceData.get(position).getResourceId());
                    startActivity(intent);
                } else if (ResourceData.get(position).isCheckMode()) {
                    if (!ResourceData.get(position).isDownload() || mChapterDetailBean.getData().isBuy()) {//当前已经下载中了，忽略选择事件
                        if (ResourceData.get(position).isHaveResourceFile())
                            CheckItem(position);
                    }
                }
            }

        });
        ziyuanAdapter.setonTouchDown(new ZiyuanDownLoadAdapter.OnTouchDown() {
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
       /* ziyuanAdapter.setOnLongClickListener(new ZiyuanDownLoadAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(int position) {
                if (!ResourceData.get(position).isCheckMode()) {
                    toCheckMode();
                }
            }
        });*/
        listKecheng.setAdapter(ziyuanAdapter);
        if (ResourceData.size() <= 0) llSearchAllNoData.setVisibility(View.VISIBLE);
        else llSearchAllNoData.setVisibility(View.GONE);

    }

    /**
     * 离线下载按钮是否置灰
     * @return true-灰色disable  false-蓝色able
     */
    private boolean isDownBtnGray(){

        if(!mChapterDetailBean.getData().isBuy()) return true;

        if (ResourceData == null) return false;

        boolean isGray = true;

        //判断哪些在下载
        for (ResourceBean item : ResourceData) {
            if (item.getType().equals("richtext") || !mChapterDetailBean.getData().isBuy()) {
                item.setCanDownload(false);
            } else
                item.setCanDownload(true);
            for (ResourceBean downItem : DownLoadIntentService.allTask) {
                if (item.getResourceId().equals(downItem.getResourceId())) {
                    item.setDownload(true);
                }
            }
            //判断哪些在下载过
            for (ResourceBean downItem : FileManager.LocalResourceList) {
                if (item.getResourceId().equals(downItem.getResourceId())) {
                    item.setDownloadOk(true);
                }
            }

            if (item.isCheck() && isGray){
                isGray = false;
            }
        }

        List<ResourceBean> resourceBeanList = new ArrayList<>();
        List<ResourceBean> noRichTextBean = new ArrayList<>();

        for (ResourceBean resourceBean : ResourceData) {
            if (!resourceBean.getType().equals("richtext")) {
                noRichTextBean.add(resourceBean);
            }
            if (resourceBean.isDownloadOk()) {
                resourceBeanList.add(resourceBean);
            }
        }
        if (resourceBeanList.size() == noRichTextBean.size()) {
            isGray = true;
        }

        return isGray;
    }
    public void toCheckMode() {
        //全部设置可选模式
        for (ResourceBean item : ResourceData) {
            item.setCheck(false);//全部置为未选中
            if (item.isHaveResourceFile())
                item.setCheckMode(true);
        }
       /* btnLeftTitle.setVisibility(View.INVISIBLE);
        btnLeftTitleText.setVisibility(View.VISIBLE);
        btnRightTitleText.setVisibility(View.VISIBLE);*/


        ziyuanAdapter.notifyDataSetChanged();
    }

    public void OutCheckMode() {
        //全部设置可选模式
//        for (ResourceBean item : ResourceData) {
//            item.setCheckMode(false);
//        }
        for (ResourceBean item : ResourceData) {
            item.setCheck(false);
        }

        btnLeftTitle.setVisibility(View.VISIBLE);
        btnLeftTitleText.setVisibility(View.INVISIBLE);
        btnRightTitleText.setVisibility(View.INVISIBLE);
        ziyuanAdapter.notifyDataSetChanged();
    }


    public void CheckItem(int position) {
        ResourceData.get(position).setCheck(ResourceData.get(position).isCheck() ? false : true);
        //遍历是否有被选中的
        boolean hasCheck = false;
        for (ResourceBean item : ResourceData) {
            if (item.isCheck()) {
                hasCheck = true;
                break;
            }
        }
        if (hasCheck) {
            btnLeftTitle.setVisibility(View.INVISIBLE);
            btnLeftTitleText.setVisibility(View.VISIBLE);
            btnRightTitleText.setVisibility(View.VISIBLE);


            if (isDownBtnGray()) {
                btnDown.setBackgroundResource(R.drawable.background_gray_shape_circle);
                btnDown.setEnabled(false);
            }else {
                btnDown.setBackgroundResource(R.drawable.btn_all_shape_circle_blue_click);
                btnDown.setEnabled(true);
            }

        } else {
            btnLeftTitle.setVisibility(View.VISIBLE);
            btnLeftTitleText.setVisibility(View.INVISIBLE);
            btnRightTitleText.setVisibility(View.INVISIBLE);
            btnDown.setEnabled(false);
            btnDown.setBackgroundResource(R.drawable.background_gray_shape_circle);
        }
        ziyuanAdapter.notifyDataSetChanged();
    }

    public void checkAllItem(boolean isCheck) {
        for (ResourceBean item : ResourceData) {
            if (!item.getType().equals("richtext")) {
                if (item.isHaveResourceFile()) item.setCheck(isCheck);
            }
        }
        //全部没选中时
        if (!isCheck) {
            btnLeftTitle.setVisibility(View.VISIBLE);
            btnLeftTitleText.setVisibility(View.INVISIBLE);
            btnRightTitleText.setVisibility(View.INVISIBLE);
        }
        ziyuanAdapter.notifyDataSetChanged();
    }


    public void toBind(String code, final String bookId) {
        if (!showNetWaitLoding()) {
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("bookId", bookId);
            json.put("code", code);
            json.put("isInput", isInput);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).bindSecurityCode(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastSHORT(mContext, "绑定成功");
                        BindBookBean bindBookBean = ParseUtil.parseBean(response, BindBookBean.class);
                        if (bindBookBean != null) {
                            if (bindBookBean.getData().isBuy()) {
                                getData(true);
                                return;
                            }
                        }
                        Intent intent = new Intent(mContext, ResourceBuyActivity.class);
                        intent.putExtra("Id", chapterId);
                        intent.putExtra("buyType", buyType); //1、图书 2、课程 3、套系 4、章节 5、资源
                        startActivity(intent);
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(ScanActivity.CODED_CONTENT);
                if (content.contains("securityQrcode")) { //防伪码绑定
                    String code = content.substring(content.lastIndexOf("code=") + 5);
                    isInput = false;
                    checkSecurityCode(code);
                    //toBind(code, mChapterDetailBean.getData().getBookId());
                } else if (content.contains("fw=")) { //新防伪码绑定
                    String code = content.substring(content.lastIndexOf("fw=") + 3);
                    isInput = false;
                    checkNewSecurityCode(code);
                    //toBind(code, mChapterDetailBean.getData().getBookId());
                } else if (content.contains("bookQrcode") ||
                        content.contains("resourceQrcode") ||
                        content.contains("contentQrcode") ||
                        content.contains("curriculumQrcode")) { //图书跳转
                    ToastUtils.showToastLONG(mContext, "非防伪码，请在我的课堂进行扫一扫");
                } else {   //二维码错误
                    ToastUtils.showToastLONG(mContext, "防伪码验证未通过！\n" +
                            "请重新扫描正确的防伪码");
                }

            }
        }


    }

    /**
     * 验证防伪码
     *
     * @param code 防伪码
     */
    private void checkSecurityCode(final String code) {

        if (!showNetWaitLoding()) {
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        JSONObject json = new JSONObject();
        try {
            json.put("code", code);
            json.put("type", 2);
            json.put("isInput", isInput);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).checkSecurityCode(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        SecurityCodeBean securityCodeBean = ParseUtil.parseBean(response, SecurityCodeBean.class);
                        if (securityCodeBean.getStatus() == 1) {
                            ToastUtils.showToastLONG(mContext, securityCodeBean.getData().getMsg());
                            if (securityCodeBean.getData().isIsCorrect() && securityCodeBean.getData().isIsCanBind()) {
                                //验证防伪码成功，显示是否绑定
                                showtoBindDialog(code, mChapterDetailBean.getData().getBookId());
                            }
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        ToastUtils.showToastLONG(mContext, msg);
                    }
                });
    }
    /**
     * 新验证防伪码
     *
     * @param code 防伪码
     */
    private void checkNewSecurityCode(final String code) {

        if (!showNetWaitLoding()) {
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        JSONObject json = new JSONObject();
        try {
            json.put("code", code);
            json.put("type", 2);
            json.put("isInput", isInput);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).checkNewSecurityCode(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        SecurityCodeBean securityCodeBean = ParseUtil.parseBean(response, SecurityCodeBean.class);
                        if (securityCodeBean.getStatus() == 1) {
                            ToastUtils.showToastLONG(mContext, securityCodeBean.getData().getMsg());
                            if (securityCodeBean.getData().isIsCorrect() && securityCodeBean.getData().isIsCanBind()) {
                                //验证防伪码成功，显示是否绑定
                                showtoBindDialog(code, mChapterDetailBean.getData().getBookId());
                            }
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        ToastUtils.showToastLONG(mContext, msg);
                    }
                });
    }

    //扫一扫
    public void StartZxing() {
        /*ZxingConfig是配置类  可以设置是否显示底部布局，闪光灯，相册，是否播放提示音  震动等动能
         * 也可以不传这个参数
         * 不传的话  默认都为默认不震动  其他都为true
//         * */
//        ZxingConfig config = new ZxingConfig();
//        config.setShowbottomLayout(false);//底部布局（包括闪光灯和相册）
//        config.setPlayBeep(true);//是否播放提示音
//        config.setShake(false);//是否震动
//        config.setShowAlbum(false);//是否显示相册
//        config.setShowFlashLight(false);//是否显示闪光灯
//
//
//        //如果不传 ZxingConfig的话，两行代码就能搞定了
//        Intent intent = new Intent(mContext, CaptureActivity.class);
//        intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
//        startActivityForResult(intent, REQUEST_CODE_SCAN);
        //这里的上下文是activity  所以回调的应该在 fragment所在的activity的onActivityResult
        IntentIntegrator integrator = new IntentIntegrator(this);
        // 设置要扫描的条码类型，ONE_D_CODE_TYPES：一维码，QR_CODE_TYPES-二维码
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setCaptureActivity(ScanActivity.class); //设置打开摄像头的Activity
        integrator.setPrompt("将二维码放入框内将自动扫描"); //底部的提示文字，设为""可以置空
        integrator.setCameraId(0); //前置或者后置摄像头
        integrator.setBeepEnabled(true); //扫描成功的「哔哔」声，默认开启
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }
    /**
     * 验证防伪码dialog
     */
    public void showBindDialog() {
        if (mChapterDetailBean.getData().isHasSecurityCode() && mChapterDetailBean.getData().isBind()) {
            Intent intent = new Intent(mContext, ResourceBuyActivity.class);
            intent.putExtra("Id", chapterId);
            intent.putExtra("buyType", buyType); //1、图书 2、课程 3、套系 4、章节 5、资源
            startActivity(intent);
            return;
        }
        ToolUtil.showYanCodeDialog(this, chapterName, new OnItemListener() {
            @Override
            public void onPay() {//直接购买
                //设置你的操作事项
                Intent intent = new Intent(mContext, ResourceBuyActivity.class);
                intent.putExtra("Id", chapterId);
                intent.putExtra("buyType", buyType); //1、图书 2、课程 3、套系 4、章节 5、资源
                startActivity(intent);
            }

            @Override
            public void onCancle() {//扫码
                PermissionUtils.newIntence().requestPerssion(ChapterDetailsActivity.this, ToolUtil.PERSSION_PHOTO, 101, new PermissionUtils.IPermission() {
                    @Override
                    public void success(int code) {
                        StartZxing(); //扫码
                    }
                });
            }

            @Override
            public void onConfig() {//手动绑码
                ToolUtil.showBindCodeDialog(ChapterDetailsActivity.this, new OnItemListener<String>(){
                    @Override
                    public void onConfig(String s) {
                        isInput = true;
                        if (s.length() == 18) {//16是新码 18是旧码
                            checkSecurityCode(s);
                        } else {
                            checkNewSecurityCode(s);
                        }
                    }
                });
            }
        });
//        MyDialog.Builder builder = new MyDialog.Builder(mContext);
//        builder.setTitle("验证防伪码");
//        builder.setMessage("验证并绑定本书防伪码后，获取整书资源可享受正版优惠价格、赠送答疑券等多项福利，是否验证？");
//
//        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                //设置你的操作事项
//                Intent intent = new Intent(mContext, ResourceBuyActivity.class);
//                intent.putExtra("Id", chapterId);
//                intent.putExtra("buyType", buyType); //1、图书 2、课程 3、套系 4、章节 5、资源
//                startActivity(intent);
//            }
//        });
//
//        builder.setNegativeButton("确定",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        //进行扫一扫
//                        StartZxing();
//                        dialog.dismiss();
//                    }
//                });
//
//        builder.create().show();

    }


    @OnClick({R.id.btn_left_title, R.id.btn_down, R.id.btn_buy, R.id.btn_left_title_text, R.id.btn_right_title_text, R.id.btn_refresh_net})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left_title:
                onBackPressed();
                break;
            case R.id.btn_down:
                if (!NetworkUtil.isConnected(mContext)) {
                    ToastUtils.showToastLONG(mContext, "网络未连接");
                    return;
                }
                final List<ResourceBean> willDownload = new ArrayList<>();
                boolean isCheck = false;
                for (ResourceBean item : ResourceData) {
                    if (item.isCheck() && !item.isDownload()) {//选中，且未下载的
                        item.setDownload(true);
                        item.setCheck(false);
                        item.setUserId(AccountUtil.getAccountMobile(mContext));
                        //item.setCheckMode(false);
                        willDownload.add(item);
                        isCheck = true;
                    }
                }
                if (!isCheck) {
                    ToastUtils.showToastSHORT(mContext, "请选择下载的资源");
                    return;
                }

                PermissionUtils.newIntence().requestPerssion(this, ToolUtil.PERSSION_WRITE, 101, new PermissionUtils.IPermission() {
                    @Override
                    public void success(int code) {
                        ToastUtils.showToastSHORT(mContext, "后台添加下载" + willDownload.size() + "个资源");
                        DownLoadIntentService.startActionDown(mContext, willDownload);
                        OutCheckMode();
                    }
                });

                break;
            case R.id.btn_buy:
                if (mChapterDetailBean != null) {
                    if (!mChapterDetailBean.getData().isHasSecurityCode()) {
                        if (mChapterDetailBean.getData().isHaveFile()) {
                            Intent intent = new Intent(mContext, ResourceBuyActivity.class);
                            intent.putExtra("Id", chapterId);
                            intent.putExtra("buyType", buyType); //1、图书 2、课程 3、套系 4、章节 5、资源
                            startActivity(intent);
                        } else {
                            showIsHaveFileBook();
                        }

                    } else {
                        if (mChapterDetailBean.getData().isHaveFile())
                            showBindDialog();
                        else
                            showIsHaveFileBook();
                    }
                }

                break;
            case R.id.btn_left_title_text:
                checkAllItem(true);
                break;
            case R.id.btn_right_title_text:
                OutCheckMode();
                break;
            case R.id.btn_refresh_net:
                getData(true);
                break;
        }
    }

    private void registerMyReceiver() {
        if (mBroadcastReceiver == null) {
            //注册广播
            mBroadcastReceiver = new ChapterDetailsActivity.MyBroadcastReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(DownLoadIntentService.RESULT_DELETE);
            intentFilter.addAction(DownLoadIntentService.RESULT_DOWN_ONE);
            intentFilter.addAction(DownLoadIntentService.RESULT_DOWN);
            registerReceiver(mBroadcastReceiver, intentFilter);
        }
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(DownLoadIntentService.RESULT_DOWN_ONE)) {
                String id = intent.getStringExtra("resourcesId");
                if (ResourceData != null) {
                    for (ResourceBean item : ResourceData) {
                        if (item.getResourceId().equals(id)) {
                            item.setDownloadOk(true);
                            item.setDownload(false);
                            if (ziyuanAdapter != null) {
                                ziyuanAdapter.refreshData(ResourceData);
                            }
                        }
                    }
                }

            }
            if (intent.getAction().equals(DownLoadIntentService.RESULT_DOWN)) {
                String num = intent.getStringExtra("down_num");
                String errorNum = intent.getStringExtra("down_error_num");
                ArrayList<String> errIds = intent.getStringArrayListExtra("error_id");
                ToastUtils.showToastLONG(context, "下载完成，共" + num + "个资源" + ",失败" + errorNum + "资源");
                for (ResourceBean item : ResourceData) {
                    if (errIds != null)
                        for (String id : errIds) {
                            if (item.isDownload() && id.equals(item.getResourceId())) {
                                //  item.setDownloadOk(false);
                                item.setDownload(false);
                            }


                        }
                }
                if (ziyuanAdapter != null) {
                    ziyuanAdapter.refreshData(ResourceData);
                    if (isDownBtnGray()) {
                        btnDown.setBackgroundResource(R.drawable.background_gray_shape_circle);
                        btnDown.setEnabled(false);
                    }else {
                        btnDown.setBackgroundResource(R.drawable.btn_all_shape_circle_blue_click);
                        btnDown.setEnabled(true);
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

    /**
     * 显示
     */
    private void showIsHaveFileBook() {
        MyDialog.Builder builder = new MyDialog.Builder(mContext);
        builder.setTitle("提示");
        builder.setMessage("部分资源尚未上传，上传后会自动为您开通使用权限，是否打包购买？");

        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //设置你的操作事项
            }
        });

        builder.setNegativeButton("确认",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        checkHasSecurityCode();
                        dialog.dismiss();
                    }
                });

        builder.create().show();
    }

    public String autoSplitText(final TextView tv, final String indent) {
        final String rawText = tv.getText().toString(); //原始文本
        final Paint tvPaint = tv.getPaint(); //paint，包含字体等信息
        final float tvWidth = tv.getWidth() - tv.getPaddingLeft() - tv.getPaddingRight(); //控件可用宽度

        //将缩进处理成空格
        String indentSpace = "";
        float indentWidth = 0;
        if (!TextUtils.isEmpty(indent)) {
            float rawIndentWidth = tvPaint.measureText(indent);
            if (rawIndentWidth < tvWidth) {
                while ((indentWidth = tvPaint.measureText(indentSpace)) < rawIndentWidth) {
                    indentSpace += " ";
                }
            }
        }

        //将原始文本按行拆分
        String[] rawTextLines = rawText.replaceAll("\r", "").split("\n");
        StringBuilder sbNewText = new StringBuilder();
        for (String rawTextLine : rawTextLines) {
            if (tvPaint.measureText(rawTextLine) <= tvWidth) {
                //如果整行宽度在控件可用宽度之内，就不处理了
                sbNewText.append(rawTextLine);
            } else {
                //如果整行宽度超过控件可用宽度，则按字符测量，在超过可用宽度的前一个字符处手动换行
                float lineWidth = 0;
                for (int cnt = 0; cnt != rawTextLine.length(); ++cnt) {
                    char ch = rawTextLine.charAt(cnt);
                    //从手动换行的第二行开始，加上悬挂缩进
                    if (lineWidth < 0.1f && cnt != 0) {
                        sbNewText.append(indentSpace);
                        lineWidth += indentWidth;
                    }
                    lineWidth += tvPaint.measureText(String.valueOf(ch));
                    if (lineWidth <= tvWidth) {
                        sbNewText.append(ch);
                    } else {
                        sbNewText.append("\n");
                        lineWidth = 0;
                        --cnt;
                        if (cnt == -1) {
                            break;
                        }

                    }
                }
            }
            sbNewText.append("\n");
        }

        //把结尾多余的\n去掉
        if (!rawText.endsWith("\n")) {
            sbNewText.deleteCharAt(sbNewText.length() - 1);
        }

        return sbNewText.toString();

    }

    /**
     * 验证防伪码成功后，显示，是否绑定dialog
     *
     * @param code
     * @param bookId
     */
    public void showtoBindDialog(final String code, final String bookId) {

        MyDialog.Builder builder = new MyDialog.Builder(mContext);
        builder.setTitle("防伪码验证");
        builder.setMessage("现在绑定，购买整书享优惠。");

        builder.setPositiveButton("不绑定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //设置你的操作事项
                Intent intent = new Intent(mContext, ResourceBuyActivity.class);
                intent.putExtra("Id", bookId);
                intent.putExtra("buyType", buyType); //1、图书 2、课程 3、套系 4、章节 5、资源
                startActivity(intent);
            }
        });

        builder.setNegativeButton("绑定",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        toBind(code, bookId);
                        dialog.dismiss();
                    }
                });

        builder.create().show();

    }

    /**
     * 根据是否有安全码判断是否显示扫码绑定
     */
    private void checkHasSecurityCode() {
        if (!mChapterDetailBean.getData().isHasSecurityCode()) {
            Intent intent = new Intent(mContext, ResourceBuyActivity.class);
            intent.putExtra("Id", mChapterDetailBean.getData().getBookId());
            intent.putExtra("buyType", buyType); //1、图书 2、课程 3、套系 4、章节 5、资源
            startActivity(intent);
        } else {
            showBindDialog();
        }
    }
}
