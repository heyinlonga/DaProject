package software.ecenter.study.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.google.zxing.integration.android.IntentIntegrator;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.listener.GSYVideoProgressListener;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import software.ecenter.study.Adapter.PingLunAdapter;
import software.ecenter.study.Adapter.TipResourceAdapter;
import software.ecenter.study.Adapter.TypeCommentAdapter;
import software.ecenter.study.Interface.ConstantData;
import software.ecenter.study.Interface.OnItemListener;
import software.ecenter.study.R;
import software.ecenter.study.View.CircleImageView;
import software.ecenter.study.View.LandLayoutVideo;
import software.ecenter.study.View.MyDialog;
import software.ecenter.study.View.SpinnerPopWindow;
import software.ecenter.study.bean.AfterBean;
import software.ecenter.study.bean.AuthBean;
import software.ecenter.study.bean.BaseBean;
import software.ecenter.study.bean.Bean;
import software.ecenter.study.bean.BindBookBean;
import software.ecenter.study.bean.BookInfoBean;
import software.ecenter.study.bean.HomeBean.BookDetailBean;
import software.ecenter.study.bean.HomeBean.CommentBean;
import software.ecenter.study.bean.HomeBean.CommentDetailBean;
import software.ecenter.study.bean.HomeBean.ResourceBean;
import software.ecenter.study.bean.HomeBean.ResourceDetailBean;
import software.ecenter.study.bean.HomeBean.SecurityCodeBean;
import software.ecenter.study.bean.HomeBean.XiTiNanDuBean;
import software.ecenter.study.bean.TypeBean;
import software.ecenter.study.bean.TypeCommentBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.service.DownLoadIntentService;
import software.ecenter.study.tool.FileAccessor;
import software.ecenter.study.tool.FileManager;
import software.ecenter.study.tool.SuffixNameTool;
import software.ecenter.study.utils.AccountUtil;
import software.ecenter.study.utils.EditUtils;
import software.ecenter.study.utils.GlideCircleTransform;
import software.ecenter.study.utils.NetworkUtil;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.PermissionUtils;
import software.ecenter.study.utils.SysUtil;
import software.ecenter.study.utils.ToastUtils;
import software.ecenter.study.utils.ToolUtil;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;


/**
 * dec 资源查看
 * 单独的视频播放页面
 */
public class SeeResourcesVideoActivity extends BaseActivity implements ConstantData {
    //推荐使用StandardGSYVideoPlayer，功能一致 //CustomGSYVideoPlayer部分功能处于试验阶段
    @BindView(R.id.post_detail_nested_scroll)
    NestedScrollView postDetailNestedScroll;
    @BindView(R.id.activity_detail_player)
    RelativeLayout activityDetailPlayer;
    @BindView(R.id.zan_img)
    ImageView zanImg;
    @BindView(R.id.zan_text)
    TextView zanText;
    @BindView(R.id.btn_zan)
    LinearLayout btnZan;
    @BindView(R.id.pinglun_img)
    ImageView pinglunImg;
    @BindView(R.id.pinglun_text)
    TextView pinglunText;
    @BindView(R.id.btn_pinglun)
    LinearLayout btnPinglun;
    @BindView(R.id.btn_shouchang_img)
    ImageView btnShouchangImg;
    @BindView(R.id.btn_fenxiang_img)
    ImageView btnFenxiangImg;
    @BindView(R.id.btn_xiazei_img)
    ImageView btnXiazeiImg;
    @BindView(R.id.list_kecheng)
    RecyclerView listKecheng;
    @BindView(R.id.list_pinglun)
    RecyclerView listPinglun;
    @BindView(R.id.pinglun_img_head)
    CircleImageView pinglunImgHead;
    @BindView(R.id.edit_pinglun)
    EditText editPinglun;
    @BindView(R.id.detail_player)
    LandLayoutVideo detailPlayer;
    @BindView(R.id.btn_look_ok)
    Button btnLookOk;
    @BindView(R.id.btn_look_no)
    Button btnLookNo;
    @BindView(R.id.btn_updata)
    Button btnUpdata;
    @BindView(R.id.rootView)
    LinearLayout rootView;
    @BindView(R.id.btn_comment)
    Button btnComment;
    @BindView(R.id.detail_img)
    ImageView detailImg;
    @BindView(R.id.detail_text)
    TextView detailText;
    @BindView(R.id.tv_speed)
    TextView tvSpeed;
    @BindView(R.id.list_kecheng_no_resource)
    TextView tvkeChengNoResource;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.btn_left_title)
    ImageView btnLeft;
    @BindView(R.id.see_resource_need_buy)
    RelativeLayout rlSeeResourceNeedBuy;
    @BindView(R.id.tv_buy_resources_tip)
    TextView tvBuyResourcesTip;
    @BindView(R.id.auth_lay)
    LinearLayout auth_lay;
    @BindView(R.id.auth_icon)
    ImageView auth_icon;
    @BindView(R.id.auth_name)
    TextView auth_name;
    @BindView(R.id.btn_buy_resources)
    Button btnBuyResources;
    @BindView(R.id.tv_comment)
    TextView tv_comment;
    @BindView(R.id.rv_type_comment)
    RecyclerView rv_type_comment;
    private TextView tv;
    private LinearLayout buttonbottom;
    private RelativeLayout pinglun;

    private boolean isPlay;
    private boolean isPause;

    private OrientationUtils orientationUtils;
    private SpinnerPopWindow spinnerPopWindow;
    private List<String> spinnerList;

    private String resourceId;
    private ResourceDetailBean mResourceDetailBean;

    private List<ResourceBean> ResourceListData;
    private List<CommentBean> pingLunList = new ArrayList<>();
    private CommentDetailBean mCommentDetailBean;
    private BookInfoBean bookInfoBean;
    private PingLunAdapter adapterPinglun;
    //扫一扫
    private int REQUEST_CODE_SCAN = 115;
    private boolean isCollection;//是否收藏了
    private int downloadStatus; //0 为下载  1 下载中 2 已经下载
    private boolean OffLineMode;//离线模式
    private String localFakePath;//本地假名字的路径
    private String localReallyPath;//本地假名字的路径
    //评论数
    private int num = 0;
    private AuthBean bean;
    private BookDetailBean mBookDetailBean;
    private boolean isVideo;
    private TypeCommentAdapter typeCommentAdapter;
    private boolean isBook;
    private String bookname;
    private boolean isInput;
    private boolean isZang;
    private int type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_resources_video);
        ButterKnife.bind(this);
        resourceId = getIntent().getStringExtra("resourceId");
        type = getIntent().getIntExtra("type", 0);
        mBookDetailBean = (BookDetailBean) getIntent().getSerializableExtra("data");
        initView();
        auth_name.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        EditUtils.showEditNoEmoji(editPinglun, this, "请不要输入表情符号", "评论字数限100字~");
        setCommentScrollView();

        if (mBookDetailBean != null) {
            BookDetailBean.Data data = mBookDetailBean.getData();
            if (data != null) {
                String bookSalePrice = data.getBookSalePrice();
                if (ToolUtil.getString(bookSalePrice).equals("0")) {
                    btnBuyResources.setText("获取");
                } else {
                    btnBuyResources.setText("购买");
                }
            }
        }
        if (type == 1) {//讲座
            btnLookNo.setVisibility(View.GONE);
        }
    }

    //讲座备选
    private void getJianzuoComment() {
        if (!showNetWaitLoding()) {
            return;
        }

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getDefaultLectureComments())
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        TypeCommentBean bean = ParseUtil.parseBean(response, TypeCommentBean.class);
                        if (bean != null)
                            typeCommentAdapter.setData(getTypeList(bean));
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }

    //素质教育备选
    private void getShuziComment() {
        if (!showNetWaitLoding()) {
            return;
        }

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getDefaultQualityComments())
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        TypeCommentBean bean = ParseUtil.parseBean(response, TypeCommentBean.class);
                        if (bean != null)
                            typeCommentAdapter.setData(getTypeList(bean));
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }

    //获取备选评论
    private void getBeiComment() {
        if (!showNetWaitLoding()) {
            return;
        }

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getDefaultComments())
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        TypeCommentBean bean = ParseUtil.parseBean(response, TypeCommentBean.class);
                        if (bean != null)
                            typeCommentAdapter.setData(getTypeList(bean));
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }

    private List<TypeBean> getTypeList(TypeCommentBean bean) {
        List<TypeBean> list = new ArrayList<>();
        List<String> data = bean.getData();
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                list.add(new TypeBean(data.get(i), false));
            }
        }
        return list;
    }

    @Override
    protected void onResume() {
        if (isVideo) {
            getCurPlay().onVideoResume(true);
        }
        getDataById();
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (isVideo) {
            getCurPlay().onVideoPause();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AccountUtil.saveSpeednum(mContext, 1);
        if (isPlay) {
            getCurPlay().release();
        }
        //GSYPreViewManager.instance().releaseMediaPlayer();
        if (orientationUtils != null)
            orientationUtils.releaseListener();

        if (!TextUtils.isEmpty(localReallyPath) && !TextUtils.isEmpty(localReallyPath)) {
            try {
                FileAccessor.renameFile(localReallyPath, localFakePath);//退出时改成虚假文件
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {

        if (orientationUtils != null) {
            orientationUtils.backToProtVideo();
        }

        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }

    @OnClick({R.id.btn_left_title, R.id.tv_speed, R.id.auth_lay,
            R.id.btn_zan, R.id.btn_shouchang_img,
            R.id.detail_img, R.id.detail_text, R.id.btn_fenxiang_img,
            R.id.btn_xiazei_img, R.id.btn_look_ok, R.id.btn_look_no,
            R.id.btn_updata, R.id.btn_comment, R.id.tv_comment, R.id.btn_buy_resources})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.auth_lay://作者弹框
                if (bean != null) {
                    ToolUtil.showAuthDialog(SeeResourcesVideoActivity.this, bean);
                } else {
                    getAuth();
                }
                break;
            case R.id.btn_left_title:
                onBackPressed();
                break;
            case R.id.detail_img:
                clickBuydetailImg();
                break;
            case R.id.detail_text:
                clickBuydetailText();
                break;
            case R.id.btn_zan:
                if (!NetworkUtil.isConnected(mContext)) return;
                if (!isZang) {
                    submitResourceThumbUp(1);
                } else {
                    submitResourceThumbUp(0);
                }
                break;
            case R.id.btn_shouchang_img:
                if (!NetworkUtil.isConnected(mContext)) return;
                if (!isCollection) {
                    submitResourceCollection(1);
                } else {
                    submitResourceCollection(0);
                }
                break;
            case R.id.tv_comment://评论
                if (!NetworkUtil.isConnected(mContext)) return;
                if (mResourceDetailBean != null && mResourceDetailBean.getData() != null && mResourceDetailBean.getData().isNeedBuy()) {
                    ToastUtils.showToastSHORT(mContext, "购买资源后才能评论哦~");
                    return;
                }
                if (mResourceDetailBean != null && mResourceDetailBean.getData() != null && !mResourceDetailBean.getData().isCheck() && !TextUtils.isEmpty(mResourceDetailBean.getData().getType()) && mResourceDetailBean.getData().getType().contains("video")) {
                    ToastUtils.showToastLONG(mContext, "完整观看视频后才能评论");
                    return;
                }
                String comment = tv.getText().toString();
                if (TextUtils.isEmpty(comment)) {
                    ToastUtils.showToastSHORT(mContext, "请选择评论语");
                    return;
                }
                submitResourceComment(comment);
                break;
            case R.id.btn_comment:
                if (!NetworkUtil.isConnected(mContext)) return;
                String commentContent = editPinglun.getText().toString();
                if (TextUtils.isEmpty(commentContent)) {
                    ToastUtils.showToastSHORT(mContext, "请输入评论");
                    return;
                }
                submitResourceComment(commentContent);
                break;
            case R.id.btn_fenxiang_img:
                if (!NetworkUtil.isConnected(mContext)) return;
                //TODO 分享不知道分享什么，没有对应资源的H5页面，他们没做
                break;
            case R.id.btn_xiazei_img:
                clickBuyDownLoadImg();
                break;
            case R.id.btn_look_ok:
                if (!NetworkUtil.isConnected(mContext)) return;
                if (mResourceDetailBean == null || mResourceDetailBean.getData() == null) return;
                if (mResourceDetailBean.getData().isHaveExercise()) {//是否有练习题
                    if (!mResourceDetailBean.getData().isCheck() && !TextUtils.isEmpty(mResourceDetailBean.getData().getType()) && mResourceDetailBean.getData().getType().contains("video")) {
                        ToastUtils.showToastLONG(mContext, "完整观看视频后才能做题和提问哦！");
                        return;
                    }
                    spinnerPopWindow.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                } else if (mResourceDetailBean.getData().isHaveAfterClassExercise()) {//是否有课后突破题
                    if (!mResourceDetailBean.getData().isCheck() && !TextUtils.isEmpty(mResourceDetailBean.getData().getType()) && mResourceDetailBean.getData().getType().contains("video")) {
                        ToastUtils.showToastLONG(mContext, "完整观看视频后才能做题和提问哦！");
                        return;
                    }
                    getAfterClassExercise();
                } else if (mResourceDetailBean.getData().isHavePinduExercise()) {//是否有拼音拼读
                    if (!mResourceDetailBean.getData().isCheck() && !TextUtils.isEmpty(mResourceDetailBean.getData().getType()) && mResourceDetailBean.getData().getType().contains("video")) {
                        ToastUtils.showToastLONG(mContext, "完整观看视频后才能做题和提问哦！");
                        return;
                    }
                    startActivity(new Intent(mContext, PinyinDetailActivity.class).putExtra("id",resourceId));
                } else {
                    ToastUtils.showToastLONG(mContext, "暂无练习题");
                }

                break;
            case R.id.btn_look_no:
                if (!NetworkUtil.isConnected(mContext)) return;
                if (!mResourceDetailBean.getData().isCheck() && !TextUtils.isEmpty(mResourceDetailBean.getData().getType()) && mResourceDetailBean.getData().getType().contains("video")) {
                    ToastUtils.showToastLONG(mContext, "完整观看视频后才能做题和提问哦！");
                    return;
                }
                Intent intent = new Intent(mContext, DaYiActivity.class);
                intent.putExtra("source", 0);
                intent.putExtra("resourceId", mResourceDetailBean.getData().getResourceId());
                startActivity(intent);
                break;
            case R.id.btn_updata://上传资源
//                if (!NetworkUtil.isConnected(mContext)) return;
//                if (AccountUtil.getIsTeacherChecked(mContext) != 1) {
//                    ToastUtils.showToastLONG(mContext, "请先前往“我的”栏目，进行教师资格认证");
//                    return;
//                }
//                Intent intent1 = new Intent(mContext, TeacherUpdataActivity.class);
//                intent1.putExtra("resourceId", resourceId);
//                startActivity(intent1);
                break;
            case R.id.tv_speed:
                clickBuySpeed();
                break;
            case R.id.btn_buy_resources:
                if (!NetworkUtil.isConnected(mContext)) return;
                clickBuy();
                break;
            case R.id.see_resource_need_buy:
                // ToastUtils.showToastLONG(mContext, "点击layout");
                break;
        }
    }

    //是否有课后突破题
    private void getAfterClassExercise() {
        if (!showNetWaitLoding()) {
            return;
        }
        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        map.put("resourceId", resourceId);

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getAfterClassId(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        AfterBean bean = ParseUtil.parseBean(response, AfterBean.class);
                        if (bean != null) {
                            AfterBean.DataBean data = bean.getData();
                            if (data != null) {
                                //todo---------
                                if (!SysUtil.checkAppInstalled(mContext, "com.study.talkcompute")) {
                                    SysUtil.goToMarket(mContext, "http://xzykt.longmenshuju.com/pc-api/qrcode/ksQrcode");
                                    return;
                                }
                                int afterClassId = data.getAfterClassId();
                                Intent intent = new Intent();
                                ComponentName componentName = new ComponentName("com.study.talkcompute", "com.study.talkcompute.ui.SplashActivity");
                                intent.putExtra("khtpID", afterClassId);
                                intent.setComponent(componentName);
                                startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }

    //获取作者详情
    private void getAuth() {
        if (!showNetWaitLoding()) {
            return;
        }
        JSONObject json = new JSONObject();
        try {
            json.put("id", mResourceDetailBean.getData().getAuthorId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getAuthDetail(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        bean = ParseUtil.parseBean(response, AuthBean.class);
                        if (bean != null)
                            ToolUtil.showAuthDialog(SeeResourcesVideoActivity.this, bean);
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });

    }

    //获取购买资源id
    public String getBuyResId(BookInfoBean.DataBean data) {
        if (isBook) {//图书
            return data.getBookId() == null ? data.getChapterId() : data.getBookId();
        } else {//课程
            return data.getCurriculumId() == null ? data.getChapterId() : data.getCurriculumId();
        }
    }

    //获取购买资源type
    public String getBuyResType(BookInfoBean.DataBean data) {
        if (isBook) {//图书
            return data.getBookId() == null ? "4" : "1";
        } else {//课程
            return data.getCurriculumId() == null ? "6" : "2";
        }
    }

    //点击购买
    private void clickBuy() {
        if (bookInfoBean != null) {
            BookInfoBean.DataBean data = bookInfoBean.getData();
            if (data != null) {
                if (data.isHaveFile() && !data.isHasSecurityCode()) {//不需要绑定
                    Intent intentBuyResources = new Intent(mContext, ResourceBuyActivity.class);
                    intentBuyResources.putExtra("Id", getBuyResId(data));
                    intentBuyResources.putExtra("buyType", getBuyResType(data)); //1、图书 2、课程 3、套系 4、章节 5、资源  6、课程章节
                    startActivity(intentBuyResources);
                    return;
                }
                if (data.isHaveFile() && data.isHasSecurityCode()) {
                    showBindDialog(data);
                    return;
                }
                if (!data.isHaveFile()) {
                    showBuyTip(data);
                }

            }
        }
    }

    //根据资源Id 获取图书
    private void getBookInfoBuyResourceId(final String resourceId) {

        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        map.put("resourceId", resourceId);

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getBookBuyResourceId(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        bookInfoBean = ParseUtil.parseBean(response, BookInfoBean.class);
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }

    //根据资源Id 获取课程
    private void getCurriculumByResourceId(final String resourceId) {

        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        map.put("resourceId", resourceId);

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getCurriculumByResourceId(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        bookInfoBean = ParseUtil.parseBean(response, BookInfoBean.class);
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }

    /**
     * 设置视频播放速度
     */
    private void clickBuySpeed() {
        if (tvSpeed.getText().toString().equals("1X")) {
            if (detailPlayer.isInPlayingState())
                detailPlayer.setSpeedPlaying(1.5f, false);
            else
                detailPlayer.setSpeed(1.5f, false);
            tvSpeed.setText("1.5X");
            AccountUtil.saveSpeednum(mContext, 1.5f);
        } else if (tvSpeed.getText().toString().equals("1.5X")) {
            if (detailPlayer.isInPlayingState())
                detailPlayer.setSpeedPlaying(2f, false);
            else
                detailPlayer.setSpeed(2f, false);
            tvSpeed.setText("2X");
            AccountUtil.saveSpeednum(mContext, 2);
            return;
        } else if (tvSpeed.getText().toString().equals("2X")) {
            if (detailPlayer.isInPlayingState())
                detailPlayer.setSpeedPlaying(1f, false);
            else
                detailPlayer.setSpeed(1f, false);
            tvSpeed.setText("1X");
            AccountUtil.saveSpeednum(mContext, 1);
            return;
        }

    }

    private void clickBuyDownLoadImg() {
        if (!NetworkUtil.isConnected(mContext)) {
            ToastUtils.showToastLONG(mContext, "网络未连接");
            return;
        }
        if (!TextUtils.isEmpty(mResourceDetailBean.getData().getType()) && mResourceDetailBean.getData().getType().contains("richtext")) {
            ToastUtils.showToastLONG(mContext, "本资源不可离线下载");
            return;
        }
        if (mResourceDetailBean.getData().isNeedBuy()) {

            showBuyDialog(mResourceDetailBean.getData().getCoinPrice() + "");
            return;
        }
        if (downloadStatus == 0) {
            final List<ResourceBean> willDownload = new ArrayList<>();
            ResourceBean item = new ResourceBean();
            item.setResourceId(mResourceDetailBean.getData().getResourceId());
            item.setResourceTitle(mResourceDetailBean.getData().getResourceName());
            item.setResourceUrl(mResourceDetailBean.getData().getResourceUrl());
            item.setResourceType(mResourceDetailBean.getData().getResourceType());
            item.setResourceSize(mResourceDetailBean.getData().getResourceSize());
            item.setResourceTime(mResourceDetailBean.getData().getResourceTime());
            item.setType(mResourceDetailBean.getData().getType());
            item.setDownload(true);
            item.setUserId(AccountUtil.getAccountMobile(mContext));
            willDownload.add(item);

            PermissionUtils.newIntence().requestPerssion(this, ToolUtil.PERSSION_WRITE, 101, new PermissionUtils.IPermission() {
                @Override
                public void success(int code) {
                    ToastUtils.showToastSHORT(mContext, "后台添加下载" + willDownload.size() + "个资源");
                    DownLoadIntentService.startActionDown(mContext, willDownload);
                    downloadStatus = 1;
                }
            });


        } else if (downloadStatus == 1) {
            ToastUtils.showToastSHORT(mContext, "资源已在下载队列");
        }
        if (downloadStatus == 2) {
            ToastUtils.showToastLONG(mContext, "资源已下载");
        }
    }

    private void clickBuydetailText() {
        if (!TextUtils.isEmpty(mResourceDetailBean.getData().getType()) && mResourceDetailBean.getData().getType().equals("richtext")) {
            if (mResourceDetailBean.getData().isNeedBuy()) {
                showBuyDialog(mResourceDetailBean.getData().getCoinPrice() + "");
                return;
            }
            Intent intent = new Intent(this, RichtextActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("bean", mResourceDetailBean.getData().getContent());
            bundle.putSerializable("title", mResourceDetailBean.getData().getResourceName());
            intent.putExtras(bundle);
            startActivity(intent);
            saveUserCheckResource();
            return;
        }
        if (OffLineMode) {
            if (mResourceDetailBean.getData().isNeedBuy()) {
                showBuyDialog(mResourceDetailBean.getData().getCoinPrice() + "");
                return;
            }
            PermissionUtils.newIntence().requestPerssion(this, ToolUtil.PERSSION_WRITE, 101, new PermissionUtils.IPermission() {
                @Override
                public void success(int code) {
                    Intent intentTwo = new Intent(SeeResourcesVideoActivity.this, FileDisplayActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("path", localReallyPath);
                    intentTwo.putExtras(bundle);
                    startActivity(intentTwo);
                    saveUserCheckResource();
                }
            });

        } else {
            if (mResourceDetailBean.getData().isNeedBuy()) {
                //需要购买
                showBuyDialog(mResourceDetailBean.getData().getCoinPrice() + "");

            } else {
                PermissionUtils.newIntence().requestPerssion(this, ToolUtil.PERSSION_WRITE, 101, new PermissionUtils.IPermission() {
                    @Override
                    public void success(int code) {
                        //不需要购买，直接展示
                        Intent intentTwo = new Intent(SeeResourcesVideoActivity.this, FileDisplayActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("path", mResourceDetailBean.getData().getResourceUrl());
                        intentTwo.putExtras(bundle);
                        startActivity(intentTwo);
                        saveUserCheckResource();
                    }
                });
            }
        }
    }

    private void clickBuydetailImg() {
        if (OffLineMode) {
            Intent intent = new Intent(mContext, BigPicActivity.class);
            intent.putExtra("ImageUrl", localReallyPath); //这里用ImageUrl ，为了加载gif动画  Glide
            startActivity(intent);

        } else {
            if (mResourceDetailBean.getData().isNeedBuy()) {
                //需要购买
                showBuyDialog(mResourceDetailBean.getData().getCoinPrice() + "");
            } else {
                //不需要购买，直接展示
                Intent intent = new Intent(mContext, BigPicActivity.class);
                intent.putExtra("ImageUrl", mResourceDetailBean.getData().getResourceUrl());
                startActivity(intent);
                saveUserCheckResource();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(ScanActivity.CODED_CONTENT);
                if (content.contains("securityQrcode")) { //旧防伪码绑定
                    String code = content.substring(content.lastIndexOf("code=") + 5);
                    isInput = false;
                    checkSecurityCode(code);
                    //toBind(code, mResourceDetailBean.getData().getBookId());
                } else if (content.contains("fw=")) { //新防伪码绑定
                    String code = content.substring(content.lastIndexOf("fw=") + 3);
                    isInput = false;
                    checkNewSecurityCode(code);
                } else if (content.contains("bookQrcode") || content.contains("resourceQrcode") || content.contains("contentQrcode") || content.contains("curriculumQrcode")) { //图书跳转
                    ToastUtils.showToastLONG(mContext, "非防伪码，请在我的课堂进行扫一扫");
                } else {   //二维码错误
                    ToastUtils.showToastLONG(mContext, "防伪码验证未通过！\n" +
                            "请重新扫描正确的防伪码");
                }

            }
        }

    }

    public void getData(boolean isShow) {
        if (isShow) {
            if (!showNetWaitLoding()) {
                return;
            }
        }

        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        map.put("resourceId", resourceId);

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getResourceDetail(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        ResourceDetailBean bean = ParseUtil.parseBean(response, ResourceDetailBean.class);
                        if (bean != null) {
                            setResponseView(bean);
                            ResourceDetailBean.Data data = bean.getData();
                            if (data != null) {
                                int isSingleBuy = data.getIsSingleBuy();
                                if (isSingleBuy != 1) {//不支持单独购买
                                    if (TextUtils.isEmpty(data.getBookId())) {//课程
                                        isBook = false;
                                        getCurriculumByResourceId(resourceId);
                                    } else {//图书
                                        isBook = true;
                                        getBookInfoBuyResourceId(resourceId);
                                    }
                                }
                                int sourceType = data.getSourceType();
                                if (sourceType == 1) {//讲座
                                    getJianzuoComment();
                                } else if (sourceType == 2) {//素质教育
                                    btnLookNo.setEnabled(false);
                                    btnLookNo.setBackgroundResource(R.drawable.background_gray_shape_circle);
                                    getShuziComment();
                                } else {//其他
                                    getBeiComment();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });

    }

    public void hasDownload() {
        if (mResourceDetailBean != null) {
            FileManager.getInstance(this).initLocalResourceList();

            //判断哪些在下载中
            for (ResourceBean downItem : DownLoadIntentService.allTask) {
                if (mResourceDetailBean.getData().getResourceId().equals(downItem.getResourceId())) {
                    downloadStatus = 1; //下载中
                }
            }
            //判断是否下载过
            for (ResourceBean downItem : FileManager.LocalResourceList) {
                if (mResourceDetailBean.getData().getResourceId().equals(downItem.getResourceId())) {
                    downloadStatus = 2; //下载完成
                    mResourceDetailBean.getData().setLocalPathDir(downItem.getLocalPathDir());//设置本地路径
                }
            }

        }
    }

    /**
     * 设置资源详情数据
     *
     * @param bean 资源实体
     */
    public void setResponseView(ResourceDetailBean bean) {
        Log.d("12312dwqd1", "设置资源详情数据");
        mResourceDetailBean = bean;
        if (mResourceDetailBean == null) return;
        setDifficultyLevel();
        zanText.setText("" + mResourceDetailBean.getData().getThumbUpNum());//点赞数
        if (mResourceDetailBean.getData().getThumbUp()) {
            zanImg.setImageResource(R.drawable.zan2);
            isZang = true;
        }
        if (mResourceDetailBean.getData().getCollection()) {
            btnShouchangImg.setImageResource(R.drawable.shoucang2);
            isCollection = true;
        }
        //根据是否播放过来判断是否可以去提问
        if (!TextUtils.isEmpty(mResourceDetailBean.getData().getType()) && mResourceDetailBean.getData().getType().contains("video"))
            playComplete(mResourceDetailBean.getData().isCheck());
        else
            playComplete(true);
        pinglunText.setText("" + mResourceDetailBean.getData().getCommentNum());//评论数
//        tv.setHint("已有" + mResourceDetailBean.getData().getCommentNum() + "条精选评论，快来说说你的想法吧！");
//        editPinglun.setHint("已有" + mResourceDetailBean.getData().getCommentNum() + "条精选评论，快来说说你的想法吧！");
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetworkUtil.isConnected(mContext)) return;
                if (mResourceDetailBean != null && mResourceDetailBean.getData() != null && mResourceDetailBean.getData().isNeedBuy()) {
                    ToastUtils.showToastSHORT(mContext, "购买资源后才能评论哦~");
                    return;
                }
                if (!mResourceDetailBean.getData().isCheck() && !TextUtils.isEmpty(mResourceDetailBean.getData().getType()) && mResourceDetailBean.getData().getType().contains("video")) {
                    ToastUtils.showToastLONG(mContext, "完整观看视频后才能评论");
                    return;
                }
                editPinglun.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(mContext.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
        //获取键盘的隐藏和弹起
        getWindow().getDecorView().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                //获取View可见区域的bottom
                Rect rect = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                if (bottom != 0 && oldBottom != 0 && bottom - rect.bottom <= 300) {
                    //Toast.makeText(mContext, "隐藏", Toast.LENGTH_SHORT).show();
                    buttonbottom.setVisibility(View.VISIBLE);
                    pinglun.setVisibility(View.GONE);
                } else {
                    //Toast.makeText(mContext, "弹出", Toast.LENGTH_SHORT).show();
                    buttonbottom.setVisibility(View.GONE);
                    pinglun.setVisibility(View.VISIBLE);
                }
            }
        });
        try {
            Glide.with(mContext).load(mResourceDetailBean.getData().getHeadImage()).error(R.drawable.morentx).into(pinglunImgHead);//用户头像
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (2 == bean.getData().getRole()) { //身份( 1、学生  2、老师)
            //教师有上传按钮
            btnUpdata.setVisibility(View.VISIBLE);
            btnUpdata.setBackgroundResource(R.drawable.background_gray_shape_circle);
            buttonbottom.setVisibility(View.VISIBLE);
        } else {
            btnUpdata.setVisibility(View.GONE);
            buttonbottom.setVisibility(View.GONE);
        }

        ResourceListData = bean.getData().getResourceList();
        TipResourceAdapter adapterTwo = new TipResourceAdapter(mContext, ResourceListData);
        adapterTwo.setItemClickListener(new TipResourceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(mContext, SeeResourcesVideoActivity.class);
                intent.putExtra("resourceId", ResourceListData.get(position).getResourceId());
                startActivity(intent);

            }
        });
        listKecheng.setAdapter(adapterTwo);
        if (ResourceListData != null && ResourceListData.size() <= 0) {
            tvkeChengNoResource.setVisibility(View.VISIBLE);
        } else {
            tvkeChengNoResource.setVisibility(View.GONE);
        }
        hasDownload();
        if (downloadStatus == 2) {
            localSoure();
        } else {
            netSoure();
        }
        bookname = mResourceDetailBean.getData().getResourceName();
        title.setText(mResourceDetailBean.getData().getResourceName());
        if (!TextUtils.isEmpty(mResourceDetailBean.getData().getType()) && mResourceDetailBean.getData().getType().equals("richtext")
                || mResourceDetailBean.getData().getResourceType().equals("richtext")) {
            detailText.setVisibility(View.VISIBLE);
//            detailText.setText(mResourceDetailBean.getData().getResourceName());
            detailText.setBackground(getResources().getDrawable(R.drawable.doc_bg));
        }

        if (mResourceDetailBean.getData().getIsSingleBuy() != 1 && mResourceDetailBean.getData().isNeedBuy()) {
            rlSeeResourceNeedBuy.setVisibility(View.VISIBLE);
            detailText.setEnabled(false);
            detailImg.setEnabled(false);
            detailPlayer.setEnabled(false);
        } else {
            rlSeeResourceNeedBuy.setVisibility(View.GONE);
            detailPlayer.setEnabled(true);
            detailImg.setEnabled(true);
            detailText.setEnabled(true);
        }

        String authorId = mResourceDetailBean.getData().getAuthorId();
        if (!TextUtils.isEmpty(authorId)) {
            //作者信息
            auth_name.setText(mResourceDetailBean.getData().getAuthorName());
            Glide.with(mContext).load(mResourceDetailBean.getData().getAuthorImg())
                    .transform(new CenterCrop(mContext), new GlideCircleTransform(mContext)).error(R.drawable.morentx).into(auth_icon);//用户头像
            auth_lay.setVisibility(View.VISIBLE);
        } else {
            auth_lay.setVisibility(View.GONE);
        }
    }

    /**
     * 获取评论详情
     *
     * @param curpage
     */
    public void getCommentData(int curpage) {
        JSONObject json = new JSONObject();
        try {
            json.put("resourceId", resourceId);
            json.put("curpage", curpage);
            json.put("pageNum", pageNum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getCommentList(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        CommentDetailBean bean = ParseUtil.parseBean(response, CommentDetailBean.class);
                        setCommentResponseView(bean);

                    }

                    @Override
                    public void onFail(int type, String msg) {
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }


    public void setCommentResponseView(final CommentDetailBean bean) {
        if (bean == null) return;
        mCommentDetailBean = bean;
        int curpage = mCommentDetailBean.getData().getCurpage();
        if (curpage == 0) {
            pingLunList.clear();
        }
        pingLunList.addAll(bean.getData().getCommentsList());
        if (adapterPinglun == null) {
            adapterPinglun = new PingLunAdapter(mContext, pingLunList);
            listPinglun.setAdapter(adapterPinglun);
        } else {
            adapterPinglun.refreshData(pingLunList);
        }

    }

    //视频播放完成,做练习和去提问按钮
    private void playComplete(boolean isPlayed) {
        if (!TextUtils.isEmpty(mResourceDetailBean.getData().getType()) && mResourceDetailBean.getData().getType().contains("video")) {
            playComplete(isPlayed, true);
        } else
            playComplete(isPlayed, false);

    }

    private void playComplete(boolean isPlayed, boolean isVideo) {
        if (mResourceDetailBean.getData().isHaveExercise() || mResourceDetailBean.getData().isHaveAfterClassExercise()
                || mResourceDetailBean.getData().isHaveKSExercises() || mResourceDetailBean.getData().isHavePinduExercise()) {
            if (!isVideo) btnLookOk.setEnabled(isPlayed);
            else btnLookOk.setEnabled(true);
            btnLookOk.setText("看懂了，做练习");
            btnLookOk.setBackgroundResource(isPlayed ? R.drawable.btn_all_shape_circle_blue_click :
                    R.drawable.background_gray_shape_circle);
        } else {
            btnLookOk.setEnabled(false);
            btnLookOk.setText("暂无配套习题");
            btnLookOk.setBackgroundResource(R.drawable.background_gray_shape_circle);
        }
        if (!isVideo) btnLookNo.setEnabled(isPlayed);
        else btnLookNo.setEnabled(true);
        btnLookNo.setBackgroundResource(isPlayed ? R.drawable.btn_all_shape_circle_click :
                R.drawable.background_gray_shape_circle);


    }

    public void initVideo(String url) {
        if (isPlay) {
            return;
        }
        //String url = "http://up.mcyt.net/?down/33662.mp3";
        //String url = "android.resource://" + getPackageName() + "/" + R.raw.test;
        //注意，用ijk模式播放raw视频，这个必须打开
        //GSYVideoManager.instance().enableRawPlay(getApplicationContext());
        //断网自动重新链接，url前接上ijkhttphook:
        //String url = "ijkhttphook:https://res.exexm.com/cw_145225549855002";


        //如果视频帧数太高导致卡画面不同步
        //VideoOptionModel videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 5);
        //如果视频seek之后从头播放
        //VideoOptionModel videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "enable-accurate-seek", 1);
        //List<VideoOptionModel> list = new ArrayList<>();
        //list.add(videoOptionModel);
        //GSYVideoManager.instance().setOptionModelList(list);

        //GSYVideoManager.instance().setTimeOut(4000, true);

        //增加封面
 /*       ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.mipmap.xxx1);*/

//        detailPlayer.setThumbImageView(imageView);

        resolveNormalVideoUI();

        //外部辅助的旋转，帮助全屏
        orientationUtils = new OrientationUtils(this, detailPlayer);
        //初始化不打开外部的旋转
        orientationUtils.setEnable(false);

        GSYVideoOptionBuilder gsyVideoOption = new GSYVideoOptionBuilder();
        gsyVideoOption//.setThumbImageView(imageView)
                .setIsTouchWiget(true)
                .setRotateViewAuto(false)
                .setLockLand(false)
                .setThumbPlay(true)
                .setShowFullAnimation(false)
                .setNeedLockFull(true)
                .setSeekRatio(1)
                .setUrl(url)
                .setCacheWithPlay(false)
                .setVideoTitle(mResourceDetailBean.getData().getResourceName())
                .setVideoAllCallBack(new GSYSampleCallBack() {
                    @Override
                    public void onPrepared(String url, Object... objects) {
                        Debuger.printfError("***** onPrepared **** " + objects[0]);
                        Debuger.printfError("***** onPrepared **** " + objects[1]);
                        super.onPrepared(url, objects);
                        //开始播放了才能旋转和全屏
                        if (!TextUtils.isEmpty(mResourceDetailBean.getData().getType()) && !mResourceDetailBean.getData().getType().equals("audio"))
                            orientationUtils.setEnable(true);

                        isPlay = true;
                    }

                    @Override
                    public void onEnterFullscreen(String url, Object... objects) {
                        super.onEnterFullscreen(url, objects);
                        Debuger.printfError("***** onEnterFullscreen **** " + objects[0]);//title
                        Debuger.printfError("***** onEnterFullscreen **** " + objects[1]);//当前全屏player
                    }

                    @Override
                    public void onAutoComplete(String url, Object... objects) {
                        super.onAutoComplete(url, objects);
                        mResourceDetailBean.getData().setCheck(true);
                        playComplete(true, true);
                        saveUserCheckResource();

                        tvSpeed.setText("2X");
                        clickBuySpeed();
                        Object o = objects[1];
                        if (o instanceof LandLayoutVideo) {
                            ((LandLayoutVideo) o).revice();
                        }
                    }

                    @Override
                    public void onClickStartError(String url, Object... objects) {
                        super.onClickStartError(url, objects);
                    }

                    @Override
                    public void onQuitFullscreen(String url, Object... objects) {
                        super.onQuitFullscreen(url, objects);
                        Debuger.printfError("***** onQuitFullscreen **** " + objects[0]);//title
                        Debuger.printfError("***** onQuitFullscreen **** " + objects[1]);//当前非全屏player
                        float speed = detailPlayer.getSpeed();
                        if (tvSpeed != null) {
                            if (speed == 2) {
                                tvSpeed.setText("2X");
                            } else if (speed == 1.5) {
                                tvSpeed.setText("1.5X");
                            } else {
                                tvSpeed.setText("1X");
                            }
                        }

                        if (orientationUtils != null) {
                            orientationUtils.backToProtVideo();
                        }
                    }
                })
                .setLockClickListener(new LockClickListener() {
                    @Override
                    public void onClick(View view, boolean lock) {
                        if (orientationUtils != null) {
                            //配合下方的onConfigurationChanged
                            if (!TextUtils.isEmpty(mResourceDetailBean.getData().getType()) && !mResourceDetailBean.getData().getType().equals("audio"))
                                orientationUtils.setEnable(!lock);
                        }
                    }
                })
                .setGSYVideoProgressListener(new GSYVideoProgressListener() {
                    @Override
                    public void onProgress(int progress, int secProgress, int currentPosition, int duration) {
                        View thumbImageView = detailPlayer.getThumbImageView();
                        if (thumbImageView != null) {
                            detailPlayer.getThumbImageViewLayout().setVisibility(View.VISIBLE);
                        }
                        Debuger.printfLog(" progress " + progress + " secProgress " + secProgress + " currentPosition " + currentPosition + " duration " + duration);
                    }
                })
                .build(detailPlayer);
        if (!TextUtils.isEmpty(mResourceDetailBean.getData().getType()) && mResourceDetailBean.getData().getType().equals("audio")) {
            detailPlayer.getFullscreenButton().setVisibility(View.GONE);
        }
        detailPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //直接横屏
                orientationUtils.resolveByClick();

                //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                detailPlayer.startWindowFullscreen(SeeResourcesVideoActivity.this, true, true);
            }
        });
        detailPlayer.getStartButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mResourceDetailBean.getData().isNeedBuy()) {
                    //需要购买
                    showBuyDialog(mResourceDetailBean.getData().getCoinPrice() + "");
                } else {
                    //不需要购买，直接展示
                    detailPlayer.startPlay();
                }
            }
        });
        detailPlayer.getThumbImageViewLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mResourceDetailBean.getData().isNeedBuy()) {
                    //需要购买
                    showBuyDialog(mResourceDetailBean.getData().getCoinPrice() + "");
                } else {
                    //不需要购买，直接展示
                    detailPlayer.startPlay();
                }
            }
        });
    }

    /**
     * 保存用户观看完成记录
     */
    private void saveUserCheckResource() {

        if (!showNetWaitLoding()) {
            return;
        }

        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        map.put("resourceId", resourceId);

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).saveUserCheckResource(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {

                    @Override
                    public void onSuccess(String response) {
                        Log.e(TAG, "save user playing is successful");
                        dismissNetWaitLoging();
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        Log.e(TAG, "save user playing is fail");
                        dismissNetWaitLoging();

                    }
                });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (isPlay) {
            detailPlayer.onConfigurationChanged(this, newConfig, orientationUtils, true, true);
        }
    }

    private void resolveNormalVideoUI() {
        //增加title
        detailPlayer.getTitleTextView().setVisibility(View.GONE);
        detailPlayer.getBackButton().setVisibility(View.GONE);
    }

    private GSYVideoPlayer getCurPlay() {
        if (detailPlayer.getFullWindowPlayer() != null) {
            return detailPlayer.getFullWindowPlayer();
        }
        return detailPlayer;
    }


    //点赞
    public void submitResourceThumbUp(final int type) {
        if (!showNetWaitLoding()) {
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("type", type);
            json.put("id", resourceId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).submitResourceThumbUp(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        if (type == 1) {
                            zanImg.setImageResource(R.drawable.zan2);
                            zanText.setText("" + (mResourceDetailBean.getData().getThumbUpNum() + 1));
                            mResourceDetailBean.getData().setThumbUpNum(mResourceDetailBean.getData().getThumbUpNum() + 1);
                            ToastUtils.showToastSHORT(mContext, "点赞成功！");
                            isZang = true;
                        } else {
                            zanImg.setImageResource(R.drawable.zan1);
                            zanText.setText("" + (mResourceDetailBean.getData().getThumbUpNum() - 1));
                            mResourceDetailBean.getData().setThumbUpNum(mResourceDetailBean.getData().getThumbUpNum() - 1);
                            ToastUtils.showToastSHORT(mContext, "取消点赞！");
                            isZang = false;
                        }

                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }

    //收藏
    public void submitResourceCollection(final int type) {
        if (!showNetWaitLoding()) {
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("type", type);
            json.put("id", resourceId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).submitResourceCollectionNew(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        if (type == 1) {
                            //收藏了
                            btnShouchangImg.setImageResource(R.drawable.shoucang2);
                            isCollection = true;
                            ToastUtils.showToastSHORT(mContext, "收藏成功！");
                        } else {
                            //收藏了
                            btnShouchangImg.setImageResource(R.drawable.shoucang1);
                            isCollection = false;
                            ToastUtils.showToastSHORT(mContext, "取消收藏成功！");
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });

    }

//    //取消收藏
//    public void removeCollection() {
//        if (!showNetWaitLoding()) {
//            return;
//        }
//
//        //接口入参，如果只有一个参数用键值对，多个参数用json
//        Map<String, String> map = new HashMap<>();
//        map.put("resourceIds", resourceId);
//
//        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).removeCollection(map))
//                .handleResponse(new RetroFactory.ResponseListener<String>() {
//                    @Override
//                    public void onSuccess(String response) {
//                        dismissNetWaitLoging();
//                        //收藏了
//                        btnShouchangImg.setImageResource(R.drawable.shoucang1);
//                        isCollection = false;
//                        ToastUtils.showToastSHORT(mContext, "取消收藏成功！");
//                    }
//
//                    @Override
//                    public void onFail(int type, String msg) {
//                        dismissNetWaitLoging();
//                        ToastUtils.showToastLONG(mContext, msg);
//                    }
//
//                });
//
//    }

    //去评论
    public void submitResourceComment(final String commentContent) {


        JSONObject json = new JSONObject();
        try {
            json.put("resourceId", resourceId);
            json.put("commentContent", commentContent);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).submitResourceComment(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        Bean data = ParseUtil.parseBean(response, Bean.class);
                        //清除选中
                        tv.setText("");
                        typeCommentAdapter.setNoChose();
                        ToastUtils.showToastSHORT(mContext, data.getMessage());
                        getCommentData(0);
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        ToastUtils.showToastLONG(mContext, msg);
                    }
                });

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
                        BindBookBean bindBookBean = ParseUtil.parseBean(response, BindBookBean.class);
                        if (bindBookBean.getData().isBuy()) {
                            getData(true);
                            return;
                        }
                        ToastUtils.showToastSHORT(mContext, "绑定成功");
                        Intent intent = new Intent(mContext, ResourceBuyActivity.class);
                        String id = "";
                        String buyType = "";
                        if (bookInfoBean != null && bookInfoBean.getData() != null) {
                            id = getBuyResId(bookInfoBean.getData());
                            buyType = getBuyResType(bookInfoBean.getData());

                        } else {
                            id = resourceId;
                            buyType = "5";
                        }
                        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(buyType)) {
                            return;
                        }
                        intent.putExtra("Id", id);
                        intent.putExtra("buyType", buyType); //1、图书 2、课程 3、套系 4、章节 5、资源 6、课程章节
                        startActivity(intent);
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });


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
            json.put("type", 1);
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
                                showtoBindDialog(code, mResourceDetailBean.getData().getBookId());
                            } else {
//                                Intent intent = new Intent(mContext, ResourceBuyActivity.class);
//                                intent.putExtra("Id", bookId);
//                                intent.putExtra("buyType", "1"); //1、图书 2、课程 3、套系 4、章节 5、资源 6、课程章节
//                                startActivity(intent);
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
            json.put("type", 1);
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
                                showtoBindDialog(code, mResourceDetailBean.getData().getBookId());
                            } else {
//                                Intent intent = new Intent(mContext, ResourceBuyActivity.class);
//                                intent.putExtra("Id", bookId);
//                                intent.putExtra("buyType", "1"); //1、图书 2、课程 3、套系 4、章节 5、资源 6、课程章节
//                                startActivity(intent);
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
         * */
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
                if (bookInfoBean != null && bookInfoBean.getData() != null) {
                    intent.putExtra("Id", getBuyResId(bookInfoBean.getData()));
                    intent.putExtra("buyType", getBuyResType(bookInfoBean.getData())); //1、图书 2、课程 3、套系 4、章节 5、资源  6、课程章节
                } else {
                    intent.putExtra("Id", resourceId);
                    intent.putExtra("buyType", "5"); //1、图书 2、课程 3、套系 4、章节 5、资源 6、课程章节
                }

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
     * 资源购买dialog
     */
    public void showBuyDialog(String price) {
        switch (mResourceDetailBean.getData().getIsSingleBuy()) {
            case 0:
                ToastUtils.showToastSHORT(mContext, getResources().getString(R.string.book_toast0));
                return;
            case -1:
                ToastUtils.showToastSHORT(mContext, getResources().getString(R.string.book_toast__1));
                return;
        }

        MyDialog.Builder builder = new MyDialog.Builder(mContext);
        builder.setTitle("购买资源");
        builder.setPrice(price);
        builder.setMessage("该资源为付费资源，购买后才可以进行观看，是否进行资源购买？");

        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        builder.setNegativeButton("确定",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //设置你的操作事项
                        if (!mResourceDetailBean.getData().isHasSecurityCode()) {
                            //绑定过了，直接购买
                            Intent intent = new Intent(mContext, ResourceBuyActivity.class);
                            if (bookInfoBean != null && bookInfoBean.getData() != null) {
                                intent.putExtra("Id", getBuyResId(bookInfoBean.getData()));
                                intent.putExtra("buyType", getBuyResType(bookInfoBean.getData())); //1、图书 2、课程 3、套系 4、章节 5、资源  6、课程章节
                            } else {
                                intent.putExtra("Id", mResourceDetailBean.getData().getResourceId());
                                intent.putExtra("buyType", "5"); //1、图书 2、课程 3、套系 4、章节 5、资源 6、课程章节
                            }
                            startActivity(intent);
                            dialog.dismiss();
                        } else {
                            //没有绑定，先去绑定再购买
                            showBindDialog();
                            dialog.dismiss();
                        }
                    }
                });

        builder.create().show();


    }

    /**
     * 验证防伪码绑定图书
     */
    public void showBindDialog() {
        showBindDialog(null);
    }

    /**
     * 验证防伪码绑定图书
     */
    public void showBindDialog(final BookInfoBean.DataBean dataBean) {
        if (bookInfoBean.getData().isHasSecurityCode() && bookInfoBean.getData().isBind()) {
            Intent intent = new Intent(mContext, ResourceBuyActivity.class);
            if (dataBean != null) {
                intent.putExtra("Id", getBuyResId(dataBean));
                intent.putExtra("buyType", getBuyResType(dataBean)); //1、图书 2、课程 3、套系 4、章节 5、资源  6、课程章节
            } else {
                intent.putExtra("Id", mResourceDetailBean.getData().getResourceId());
                intent.putExtra("buyType", "5"); //1、图书 2、课程 3、套系 4、章节 5、资源 6、课程章节
            }
            startActivity(intent);
            return;
        }
        ToolUtil.showYanCodeDialog(this, mResourceDetailBean.getData().bookName, new OnItemListener() {
            @Override
            public void onPay() {//直接购买
                //设置你的操作事项
                Intent intent = new Intent(mContext, ResourceBuyActivity.class);
                if (dataBean != null) {
                    intent.putExtra("Id", getBuyResId(dataBean));
                    intent.putExtra("buyType", getBuyResType(dataBean)); //1、图书 2、课程 3、套系 4、章节 5、资源  6、课程章节
                } else {
                    intent.putExtra("Id", mResourceDetailBean.getData().getResourceId());
                    intent.putExtra("buyType", "5"); //1、图书 2、课程 3、套系 4、章节 5、资源 6、课程章节
                }
                startActivity(intent);
            }

            @Override
            public void onCancle() {//扫码
                PermissionUtils.newIntence().requestPerssion(SeeResourcesVideoActivity.this, ToolUtil.PERSSION_PHOTO, 101, new PermissionUtils.IPermission() {
                    @Override
                    public void success(int code) {
                        StartZxing(); //扫码
                    }
                });
            }

            @Override
            public void onConfig() {//手动绑码
                ToolUtil.showBindCodeDialog(SeeResourcesVideoActivity.this, new OnItemListener<String>() {
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
//                if (dataBean != null) {
//                    intent.putExtra("Id", getBuyResId(dataBean));
//                    intent.putExtra("buyType", getBuyResType(dataBean)); //1、图书 2、课程 3、套系 4、章节 5、资源  6、课程章节
//                } else {
//                    intent.putExtra("Id", mResourceDetailBean.getData().getResourceId());
//                    intent.putExtra("buyType", "5"); //1、图书 2、课程 3、套系 4、章节 5、资源 6、课程章节
//                }
//                startActivity(intent);
//
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

    private void initView() {
        tv = findViewById(R.id.tv);
        buttonbottom = findViewById(R.id.button_bottom);
        pinglun = findViewById(R.id.pinglun);

        listKecheng.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        listPinglun.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rv_type_comment.setLayoutManager(new GridLayoutManager(mContext, 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        typeCommentAdapter = new TypeCommentAdapter(this, new TypeCommentAdapter.OnClick() {
            @Override
            public void OnClick(int poc) {
                tv.setText(typeCommentAdapter.getChoseData(poc).getName());
            }
        });
        rv_type_comment.setAdapter(typeCommentAdapter);
    }


    /**
     * 设置难度级别
     */
    private void setDifficultyLevel() {
        //级别
        spinnerList = ToolUtil.StringToArray(Level, ",");
        spinnerPopWindow = new SpinnerPopWindow(mContext);
        List<XiTiNanDuBean> datas = new ArrayList<>();
        for (String string : spinnerList) {
            XiTiNanDuBean xiTiNanDuBean = new XiTiNanDuBean();
            xiTiNanDuBean.setName(string);
            switch (string) {
                case "简单":
                    xiTiNanDuBean.setHasXiTi(mResourceDetailBean.getData().isHaveEasy());
                    datas.add(xiTiNanDuBean);
                    break;
                case "中等":
                    xiTiNanDuBean.setHasXiTi(mResourceDetailBean.getData().isHaveNormal());
                    datas.add(xiTiNanDuBean);
                    break;
                case "困难":
                    xiTiNanDuBean.setHasXiTi(mResourceDetailBean.getData().isHaveDifficulty());
                    datas.add(xiTiNanDuBean);
                    break;
            }
        }
        spinnerPopWindow.refreshData(datas, true);
        spinnerPopWindow.setPopTitle("选择题目难度");
        spinnerPopWindow.setItemSelectListener(new SpinnerPopWindow.MItemSelectListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(mContext, ExerciseActivity.class);
                String difficultyLevel = "";
                boolean isHaveQuestion = false;
                switch (spinnerList.get(position)) {
                    case "简单":
                        if (mResourceDetailBean.getData().isHaveEasy())
                            isHaveQuestion = true;

                        difficultyLevel = "EASY";
                        break;
                    case "中等":
                        if (mResourceDetailBean.getData().isHaveNormal())
                            isHaveQuestion = true;
                        difficultyLevel = "NORMAL";
                        break;
                    case "困难":
                        if (mResourceDetailBean.getData().isHaveDifficulty())
                            isHaveQuestion = true;
                        difficultyLevel = "DIFFICULTY";
                        break;
                }
                if (isHaveQuestion) {
                    intent.putExtra("difficultyLevel", difficultyLevel);
                    intent.putExtra("resourceId", resourceId);
                    startActivity(intent);

                } else {
                    // ToastUtils.showToastLONG(mContext, "暂无" + spinnerList.get(position) + "练习题");
                }

            }
        });
    }

    private void setCommentScrollView() {
        postDetailNestedScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    //Log.i("NET", "Scroll DOWN");
                }
                if (scrollY < oldScrollY) {
                    //Log.i("NET", "Scroll UP");
                }

                if (scrollY == 0) {
                    //Log.i("NET", "TOP SCROLL");
                }

                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    //Log.i("NET", "BOTTOM SCROLL");
                    if (mCommentDetailBean != null && mCommentDetailBean.getData().getHasNextpage() == 1) {
                        Log.i("+++++++++++++", "onScrollChange: 滑动进来了2");
                        getCommentData(mCommentDetailBean.getData().getCurpage() + 1);
                    }
                }
            }
        });
    }

    /**
     * 获取资源通过本地或者网络
     */
    private void getDataById() {
        if (!NetworkUtil.isConnected(mContext)) { //进入离线模式
            ToastUtils.showToastLONG(mContext, "网络未连接");
            //查看本地是否有资源
            FileManager.getInstance(this).initLocalResourceList();
            for (ResourceBean loacal : FileManager.LocalResourceList) {
                if (loacal.getResourceId().equals(resourceId)) {
                    OffLineMode = true;
                    ResourceDetailBean bean = new ResourceDetailBean();
                    bean.init();
                    bean.getData().setResourceId(ToolUtil.getString(loacal.getResourceId()));
                    bean.getData().setResourceUrl(ToolUtil.getString(loacal.getResourceUrl()));
                    bean.getData().setResourceType(ToolUtil.getString(loacal.getResourceType()));
                    bean.getData().setResourceName(ToolUtil.getString(loacal.getResourceTitle()));
                    bean.getData().setLocalPathDir(ToolUtil.getString(loacal.getLocalPathDir()));
                    bean.getData().setType(loacal.getType());
                    btnUpdata.setVisibility(View.GONE);
                    btnLookOk.setVisibility(View.GONE);
                    btnLookNo.setVisibility(View.GONE);
                    mResourceDetailBean = bean;
                    localSoure();
                    tvkeChengNoResource.setVisibility(View.VISIBLE);
                    title.setText(ToolUtil.getString(mResourceDetailBean.getData().getResourceName()));
                    break;
                }
            }

        } else {
            getData(false);
            getCommentData(0);
        }
    }

    /**
     * 加载本地资源
     */
    public void localSoure() {
        //mResourceDetailBean.getData().setNeedBuy(false);
        localFakePath = mResourceDetailBean.getData().getLocalPathDir() + File.separator + FileAccessor.getFileName(mResourceDetailBean.getData().getResourceId()); //假名称
        localReallyPath = localFakePath + SuffixNameTool.getSuffixReallyName(mResourceDetailBean.getData().getResourceType()); //算出真实名称
        try {
            FileAccessor.renameFile(localFakePath, localReallyPath);//生成真实文件
        } catch (IOException e) {
            e.printStackTrace();
        }

        //判断资源格式
        if (mResourceDetailBean.getData().getResourceType().contains("MP3")
                || mResourceDetailBean.getData().getResourceType().contains("mp3")
                || mResourceDetailBean.getData().getResourceType().contains("mp4")
                || mResourceDetailBean.getData().getResourceType().contains("MP4")) {
            //如果是音频，需要在视频播放完或手动拖到视频结束时，才可以点击
            if (mResourceDetailBean.getData().getResourceType().equals("mp4")
                    || mResourceDetailBean.getData().getResourceType().contains("MP4")) {
                playComplete(mResourceDetailBean.getData().isCheck(), true);
                isVideo = true;
            } else
                playComplete(true);
            if (mResourceDetailBean.getData().getResourceType().contains("MP3")
                    || mResourceDetailBean.getData().getResourceType().contains("mp3")) {
                //增加封面
                ImageView imageView = new ImageView(this);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setImageResource(R.drawable.audio_bg);
                detailPlayer.setThumbImageView(imageView);
            }
            detailPlayer.setVisibility(View.VISIBLE);
            initVideo("file:///" + localReallyPath);
        } else if (mResourceDetailBean.getData().getResourceType().equals("jpg")
                || mResourceDetailBean.getData().getResourceType().equals("png")
                || mResourceDetailBean.getData().getResourceType().contains("gif")) {
            //文件格式
            playComplete(true);
            detailImg.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(localReallyPath).into(detailImg);
        } else if (mResourceDetailBean.getData().getResourceType().contains("doc")
                || mResourceDetailBean.getData().getResourceType().contains("docx")
                || mResourceDetailBean.getData().getResourceType().contains("pdf")
                || mResourceDetailBean.getData().getResourceType().contains("ppt")
                || mResourceDetailBean.getData().getResourceType().contains("txt")) {
            detailText.setVisibility(View.VISIBLE);

            playComplete(true);
//            detailText.setText(mResourceDetailBean.getData().getResourceName());
            detailText.setBackground(getResources().getDrawable(R.drawable.doc_bg));
        } else if (!TextUtils.isEmpty(mResourceDetailBean.getData().getType()) && mResourceDetailBean.getData().getType().contains("richtext")) {
            detailText.setVisibility(View.VISIBLE);
            playComplete(true);
//            detailText.setText(mResourceDetailBean.getData().getResourceName());
            detailText.setBackground(getResources().getDrawable(R.drawable.doc_bg));
        }

    }

    /**
     * 网络获取资源
     */
    public void netSoure() {
        //mResourceDetailBean.getData().setNeedBuy(false);
        //判断资源格式
        if (mResourceDetailBean.getData().getResourceType().contains("MP3")
                || mResourceDetailBean.getData().getResourceType().contains("mp3")
                || mResourceDetailBean.getData().getResourceType().contains("mp4")
                || mResourceDetailBean.getData().getResourceType().contains("MP4")) {

            //如果是音频，需要在视频播放完或手动拖到视频结束时，才可以点击
            if (mResourceDetailBean.getData().getResourceType().contains("mp4")
                    || mResourceDetailBean.getData().getResourceType().contains("MP4")) {
                playComplete(mResourceDetailBean.getData().isCheck());
                isVideo = true;
            } else playComplete(true);
            detailPlayer.setVisibility(View.VISIBLE);
            if (mResourceDetailBean.getData().getResourceType().contains("MP3")
                    || mResourceDetailBean.getData().getResourceType().contains("mp3")) {
                //增加封面
                ImageView imageView = new ImageView(this);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setImageResource(R.drawable.audio_bg);
                detailPlayer.setThumbImageView(imageView);
            }
            initVideo(mResourceDetailBean.getData().getResourceUrl());
        } else if (mResourceDetailBean.getData().getResourceType().contains("jpg")
                || mResourceDetailBean.getData().getResourceType().contains("png")
                || mResourceDetailBean.getData().getResourceType().contains("gif")) {
            //文件格式
            detailImg.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(mResourceDetailBean.getData().getResourceUrl()).into(detailImg);
        } else if (mResourceDetailBean.getData().getResourceType().contains("doc")
                || mResourceDetailBean.getData().getResourceType().contains("docx")
                || mResourceDetailBean.getData().getResourceType().contains("pdf")
                || mResourceDetailBean.getData().getResourceType().contains("ppt")
                || mResourceDetailBean.getData().getResourceType().contains("txt")) {
            detailText.setVisibility(View.VISIBLE);
            //            detailText.setText(mResourceDetailBean.getData().getResourceName());
            detailText.setBackground(getResources().getDrawable(R.drawable.doc_bg));
        } else if (mResourceDetailBean.getData().getResourceType().contains("richtext")) {
            detailText.setVisibility(View.VISIBLE);
//            detailText.setText(mResourceDetailBean.getData().getResourceName());
            detailText.setBackground(getResources().getDrawable(R.drawable.doc_bg));
        }
    }

    public void showBuyTip(final BookInfoBean.DataBean dataBean) {
        final MyDialog.Builder builder = new MyDialog.Builder(mContext);
        builder.setTitle("提示");
        if (dataBean.getBookId() != null)
            builder.setMessage("部分资源尚未上传，上传后会自动为您开通使用权限，是否整书购买？");
        else
            builder.setMessage("部分资源尚未上传，上传后会自动为您开通使用权限，是否打包购买？");
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //取消
            }
        });

        builder.setNegativeButton("确定",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (dataBean.isHasSecurityCode() && !dataBean.isBind())
                            showBindDialog(bookInfoBean.getData());
                        else if (dataBean.isHasSecurityCode() && dataBean.isBind()) {
                            Intent intentBuyResources = new Intent(mContext, ResourceBuyActivity.class);

                            intentBuyResources.putExtra("Id", getBuyResId(dataBean));
                            intentBuyResources.putExtra("buyType", getBuyResType(dataBean)); //1、图书 2、课程 3、套系 4、章节 5、资源  6、课程章节
                            startActivity(intentBuyResources);
                        } else {
                            Intent intentBuyResources = new Intent(mContext, ResourceBuyActivity.class);

                            intentBuyResources.putExtra("Id", resourceId);
                            intentBuyResources.putExtra("buyType", "5"); //1、图书 2、课程 3、套系 4、章节 5、资源 6、课程章节
                            startActivity(intentBuyResources);
                        }

                        dialog.dismiss();
                    }
                });

        builder.create().show();


    }

    public void neddBuy(View v) {
    }
}

