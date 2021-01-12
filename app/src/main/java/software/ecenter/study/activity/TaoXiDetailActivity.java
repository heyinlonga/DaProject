package software.ecenter.study.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
import software.ecenter.study.Adapter.ZuheBookAdapter;
import software.ecenter.study.Interface.OnItemListener;
import software.ecenter.study.R;
import software.ecenter.study.View.ExpandableTextView;
import software.ecenter.study.View.MyDialog;
import software.ecenter.study.bean.BindBookBean;
import software.ecenter.study.bean.HomeBean.BookBean;
import software.ecenter.study.bean.HomeBean.CurriculumBean;
import software.ecenter.study.bean.HomeBean.PackageDetailBean;
import software.ecenter.study.bean.HomeBean.SecurityCodeBean;
import software.ecenter.study.bean.ZuheBookBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.NetworkUtil;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.PermissionUtils;
import software.ecenter.study.utils.ToastUtils;
import software.ecenter.study.utils.ToolUtil;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

/***
 * dsc 套系组合
 *
 * */
public class TaoXiDetailActivity extends BaseActivity {

    @BindView(R.id.btn_left_title)
    ImageView btnLeftTitle;
    @BindView(R.id.title_content)
    TextView titleContent;
    @BindView(R.id.img_book)
    ImageView imgBook;
    @BindView(R.id.text_book_name)
    TextView textBookName;
    @BindView(R.id.text_book_jiage)
    TextView textBookJiage;
    @BindView(R.id.text_book_youhuijia)
    TextView textBookYouHuiJia;
    @BindView(R.id.btn_buy)
    Button btnBuy;
    @BindView(R.id.tip_temp)
    RelativeLayout tipTemp;
    @BindView(R.id.id_source_textview)
    TextView idSourceTextview;
    @BindView(R.id.id_expand_textview)
    TextView idExpandTextview;
    @BindView(R.id.expandable_text)
    ExpandableTextView expandableText;
    @BindView(R.id.list_zuhe)
    RecyclerView listZuhe;
    @BindView(R.id.btn_refresh_net)
    LinearLayout btnRefreshNet;


    private String packageId = "";
    private List<ZuheBookBean> ZuheList;

    //扫一扫
    private int REQUEST_CODE_SCAN = 113;
    private PackageDetailBean mPackageDetailBean;
    private String youhuijia;
    private String bookname;
    private boolean isInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tao_xi_detail);
        ButterKnife.bind(this);


        listZuhe.setLayoutManager(new LinearLayoutManager(mContext){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        packageId = getIntent().getStringExtra("packageId");
        if (TextUtils.isEmpty(packageId)) {
            Bundle bundle = getIntent().getBundleExtra("package");
            if (bundle != null)
                packageId = (String) bundle.get("packageId");
        }

        getData(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData(false);
    }

    public void getData(boolean isShow) {
        if (isShow) {
            if (!showNetWaitLoding()) {
                return;
            }
        }

        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        map.put("packageId", packageId);

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).homePackageDetail(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        btnRefreshNet.setVisibility(View.GONE);
                        PackageDetailBean bean = ParseUtil.parseBean(response, PackageDetailBean.class);
                        setResponseView(bean);
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        btnRefreshNet.setVisibility(View.VISIBLE);
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }

    public void setResponseView(PackageDetailBean bean) {
        mPackageDetailBean = bean;

        Glide.with(mContext).load(bean.getData().getPackageImage()).into(imgBook);
        bookname = bean.getData().getPackageName();
        textBookName.setText(bean.getData().getPackageName());

        if (bean.getData().isBuy()) {
            btnBuy.setText("已获取");
            btnBuy.setEnabled(false);
            btnBuy.setBackground(getResources().getDrawable(R.drawable.btn_sign_shape_circle_unclick));
        }

        String yuanjia = "";
        if (bean.getData().isHasSecurityCode())
            yuanjia = "原价：" + bean.getData().getPackagePrice() + " " + "元宝";
        else
            yuanjia = "价格：" + bean.getData().getPackagePrice() + " " + "元宝";

        if (bean.getData().getPackageSalePrice().equals("0")) {
            youhuijia = "正版：" + "免费";
            btnBuy.setVisibility(View.GONE);
        } else {
            youhuijia = "正版：" + bean.getData().getPackageSalePrice() + "元宝";
        }
        SpannableStringBuilder builder = new SpannableStringBuilder(yuanjia);
        ForegroundColorSpan blueColor = new ForegroundColorSpan(getResources().getColor(R.color.price_color));
        builder.setSpan(blueColor, yuanjia.indexOf("：") + 1,
                yuanjia.lastIndexOf("元") == -1 ?builder.length():
                        yuanjia.lastIndexOf("元"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textBookJiage.setText(yuanjia);

        SpannableStringBuilder builder1 = new SpannableStringBuilder(youhuijia);
        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(65);
        if (bean.getData().getPackageSalePrice().equals("0")) {
            builder1.setSpan(blueColor, youhuijia.indexOf("：") + 1, youhuijia.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder1.setSpan(absoluteSizeSpan, youhuijia.indexOf("：") + 1, youhuijia.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            builder1.setSpan(blueColor, youhuijia.indexOf("：") + 1, youhuijia.indexOf("元") == -1 ? youhuijia.length() : youhuijia.indexOf("元"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            //builder1.setSpan(absoluteSizeSpan, youhuijia.indexOf("：") + 1, youhuijia.indexOf("元"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textBookYouHuiJia.setText(builder1);
        if (!bean.getData().isHasSecurityCode()) {
            textBookYouHuiJia.setVisibility(View.GONE);
        }
        titleContent.setText(bean.getData().getPackageName());

        expandableText = (ExpandableTextView) findViewById(R.id.expandable_text);
        expandableText.setText(bean.getData().getPackageIntroduction());


        ZuheList = new ArrayList<>();

        ZuheBookBean item = new ZuheBookBean();
        if (!TextUtils.isEmpty(bean.getData().getBookId())) {
            item.setId(bean.getData().getBookId());
            item.setImage(bean.getData().getBookImage());
            item.setName(bean.getData().getBookName());
            item.setType(1);//图书
            item.setHaveFile(bean.getData().isHaveFile());
            ZuheList.add(item);
        }

        for (BookBean bookBean : bean.getData().getBookBeanList()) {
            ZuheBookBean itemOne = new ZuheBookBean();
            itemOne.setId(bookBean.getBookId());
            itemOne.setImage(bookBean.getBookImage());
            itemOne.setName(bookBean.getBookName());
            itemOne.setType(1);//图书
            itemOne.setHaveFile(bookBean.isHaveFile());
            ZuheList.add(itemOne);
        }
        for (int i = 0; i < bean.getData().getCurriculumList().size(); i++) {
            CurriculumBean cur = bean.getData().getCurriculumList().get(i);
            ZuheBookBean itemTwo = new ZuheBookBean();
            itemTwo.setId(cur.getCurriculumId());
            itemTwo.setImage(cur.getCurriculumImage());
            itemTwo.setName(cur.getCurriculumName());
            itemTwo.setType(2);//课程
            itemTwo.setHaveFile(cur.isHaveFile());
            ZuheList.add(itemTwo);
        }


        ZuheBookAdapter zuheBookAdapter = new ZuheBookAdapter(mContext, ZuheList);
        zuheBookAdapter.setItemClickListener(new ZuheBookAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (ZuheList.get(position).getType() == 1) { //图书
                    Intent intent = new Intent(mContext, BookDetailActivity.class);
                    intent.putExtra("bookId", ZuheList.get(position).getId());
                    startActivity(intent);
                } else if (ZuheList.get(position).getType() == 2) { //课程
                    Intent intent = new Intent(mContext, KengChengDetailActivity.class);
                    intent.putExtra("curriculumId", ZuheList.get(position).getId());
                    startActivity(intent);
                }
            }
        });
        listZuhe.setAdapter(zuheBookAdapter);


    }


    public void toBind(String code, final String packageId) {
        if (!showNetWaitLoding()) {
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("bookId", packageId);
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
                        BindBookBean bookInfoBean = ParseUtil.parseBean(response, BindBookBean.class);
                        if (bookInfoBean != null && bookInfoBean.getData() != null) {
                            if (bookInfoBean.getData().isBuy()) {
                                getData(true);
                                return;
                            }
                        }
                        Intent intent = new Intent(mContext, ResourceBuyActivity.class);
                        intent.putExtra("Id",  mPackageDetailBean.getData().getPackageId());
                        intent.putExtra("buyType", "3"); //1、图书 2、课程 3、套系 4、章节 5、资源
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
//                    toBind(code, mPackageDetailBean.getData().getBookId());
                    isInput = false;
                    checkSecurityCode(code);
                } else   if (content.contains("fw=")) { //新防伪码绑定
                    String code = content.substring(content.lastIndexOf("fw=") + 3);
                    isInput = false;
                    checkNewSecurityCode(code);
                } else if (content.contains("bookQrcode") || content.contains("resourceQrcode") || content.contains("contentQrcode") || content.contains("curriculumQrcode")) { //图书跳转
                    ToastUtils.showToastLONG(mContext, "非防伪码，请在我的课堂进行扫一扫");
                } else {   //二维码错误
                    ToastUtils.showToastLONG(mContext, "无效的二维码");
                }

            }
        }


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
     * 验证防伪码dialog
     */
    public void showBindDialog() {

        if (mPackageDetailBean.getData().isHasSecurityCode() && mPackageDetailBean.getData().isBind()) {
            Intent intent = new Intent(mContext, ResourceBuyActivity.class);
            intent.putExtra("Id", mPackageDetailBean.getData().getPackageId());
            intent.putExtra("buyType", "3"); //1、图书 2、课程 3、套系 4、章节 5、资源
            startActivity(intent);
            return;
        }
        ToolUtil.showYanCodeDialog(this, bookname, new OnItemListener() {
            @Override
            public void onPay() {//直接购买
                //设置你的操作事项
                Intent intent = new Intent(mContext, ResourceBuyActivity.class);
                intent.putExtra("Id", mPackageDetailBean.getData().getPackageId());
                intent.putExtra("buyType", "3"); //1、图书 2、课程 3、套系 4、章节 5、资源
                startActivity(intent);
            }

            @Override
            public void onCancle() {//扫码
                PermissionUtils.newIntence().requestPerssion(TaoXiDetailActivity.this, ToolUtil.PERSSION_PHOTO, 101, new PermissionUtils.IPermission() {
                    @Override
                    public void success(int code) {
                        StartZxing(); //扫码
                    }
                });
            }

            @Override
            public void onConfig() {//手动绑码
                ToolUtil.showBindCodeDialog(TaoXiDetailActivity.this, new OnItemListener<String>(){
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
//                intent.putExtra("Id", mPackageDetailBean.getData().getPackageId());
//                intent.putExtra("buyType", "3"); //1、图书 2、课程 3、套系 4、章节 5、资源
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


    @OnClick({R.id.btn_left_title, R.id.btn_buy, R.id.btn_refresh_net})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left_title:
                onBackPressed();
                break;
            case R.id.btn_buy://购买
                if (!NetworkUtil.isConnected(mContext)) {
                    ToastUtils.showToastLONG(mContext, "网络未连接");
                    return;
                }

                if (mPackageDetailBean != null) {
                    if (mPackageDetailBean.getData().isBuy()) {
                        ToastUtils.showToastLONG(mContext, getResources().getString(R.string.resources_have_been_purchased));
                        return;
                    }
                    if (!mPackageDetailBean.getData().isHaveFile()) {
                        showIsHaveFileBook();
                        return;
                    }
//                    btnBuy.setEnabled(false);
                    if (!mPackageDetailBean.getData().isHasSecurityCode()) {
                        Intent intent = new Intent(mContext, ResourceBuyActivity.class);
                        intent.putExtra("Id", mPackageDetailBean.getData().getPackageId());
                        intent.putExtra("buyType", "3"); //1、图书 2、课程 3、套系 4、章节 5、资源
                        startActivity(intent);
                    } else {
                        showBindDialog();
                    }
                }

                break;
            case R.id.btn_refresh_net:
                getData(true);
                break;
        }
    }

    /**
     * 验证防伪码
     *
     * @param code 防伪码
     */
    private void checkSecurityCode(final String code) {

        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        JSONObject json = new JSONObject();
        try {
            json.put("code", code);
            json.put("type", 4);
            json.put("isInput", isInput);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());


        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).checkSecurityCode(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        SecurityCodeBean securityCodeBean = ParseUtil.parseBean(response, SecurityCodeBean.class);
                        if (securityCodeBean.getStatus() == 1) {
                            ToastUtils.showToastLONG(mContext, securityCodeBean.getData().getMsg());
                            if (securityCodeBean.getData().isIsCorrect() && securityCodeBean.getData().isIsCanBind()) {
                                showtoBindDialog(code, mPackageDetailBean.getData().getBookBeanList().get(0).getBookId());
                            } else {
                                Intent intent = new Intent(mContext, ResourceBuyActivity.class);
                                intent.putExtra("Id", mPackageDetailBean.getData().getPackageId());
                                intent.putExtra("buyType", "3"); //1、图书 2、课程 3、套系 4、章节 5、资源
                                startActivity(intent);
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

        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        JSONObject json = new JSONObject();
        try {
            json.put("code", code);
            json.put("type", 4);
            json.put("isInput", isInput);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());


        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).checkNewSecurityCode(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        SecurityCodeBean securityCodeBean = ParseUtil.parseBean(response, SecurityCodeBean.class);
                        if (securityCodeBean.getStatus() == 1) {
                            ToastUtils.showToastLONG(mContext, securityCodeBean.getData().getMsg());
                            if (securityCodeBean.getData().isIsCorrect() && securityCodeBean.getData().isIsCanBind()) {
                                showtoBindDialog(code, mPackageDetailBean.getData().getBookBeanList().get(0).getBookId());
                            } else {
                                Intent intent = new Intent(mContext, ResourceBuyActivity.class);
                                intent.putExtra("Id", mPackageDetailBean.getData().getPackageId());
                                intent.putExtra("buyType", "3"); //1、图书 2、课程 3、套系 4、章节 5、资源
                                startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        ToastUtils.showToastLONG(mContext, msg);
                    }
                });
    }

    public void showtoBindDialog(final String code, final String packageId) {

        MyDialog.Builder builder = new MyDialog.Builder(mContext);
        builder.setTitle("防伪码验证");
        builder.setMessage("防伪码验证成功，是否进行绑定");

        builder.setPositiveButton("不绑定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //设置你的操作事项
                Intent intent = new Intent(mContext, ResourceBuyActivity.class);
                intent.putExtra("Id", mPackageDetailBean.getData().getPackageId());
                intent.putExtra("buyType", "3"); //1、图书 2、课程 3、套系 4、章节 5、资源
                startActivity(intent);
            }
        });

        builder.setNegativeButton("绑定",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        toBind(code, packageId);
                        dialog.dismiss();
                    }
                });

        builder.create().show();

    }

    /**
     * 显示
     */
    private void showIsHaveFileBook() {
        MyDialog.Builder builder = new MyDialog.Builder(mContext);
        builder.setTitle("提示");
        builder.setMessage("部分资源尚未上传，上传后会自动为您开通使用权限，是否获取套系？");

        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //设置你的操作事项
            }
        });

        builder.setNegativeButton("确认",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (mPackageDetailBean.getData().isHasSecurityCode())
                        showBindDialog();
                        else{
                            Intent intent = new Intent(mContext, ResourceBuyActivity.class);
                            intent.putExtra("Id", mPackageDetailBean.getData().getPackageId());
                            intent.putExtra("buyType", "3"); //1、图书 2、课程 3、套系 4、章节 5、资源
                            startActivity(intent);
                        }
                        dialog.dismiss();
                    }
                });

        builder.create().show();
    }
}
