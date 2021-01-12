package software.ecenter.study.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.LeadingMarginSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

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
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import software.ecenter.study.Adapter.ImageAdapter;
import software.ecenter.study.Adapter.TipQuestResourceAdapter;
import software.ecenter.study.OSSSImple.OSSConfig;
import software.ecenter.study.OSSSImple.PutObjectSamples;
import software.ecenter.study.R;
import software.ecenter.study.View.AutoSplitTextView;
import software.ecenter.study.View.MyDialog;
import software.ecenter.study.View.SpinnerPopWindow;
import software.ecenter.study.activity.AnswerBuyActivity;
import software.ecenter.study.activity.ChapterDetailsActivity;
import software.ecenter.study.activity.QuestionActivity;
import software.ecenter.study.activity.QuestionDetailActivity;
import software.ecenter.study.activity.SeeResourcesVideoActivity;
import software.ecenter.study.bean.HomeBean.BookBean;
import software.ecenter.study.bean.HomeBean.RecommendInfoBean;
import software.ecenter.study.bean.HomeBean.ResourceBean;
import software.ecenter.study.bean.ImageBean;
import software.ecenter.study.bean.OssTokenBean;
import software.ecenter.study.bean.QuestionBean.ChapterDetailBean;
import software.ecenter.study.bean.QuestionBean.CurriculumBean;
import software.ecenter.study.bean.QuestionBean.DayiDetailBean;
import software.ecenter.study.bean.QuestionBean.ListCurriculumBean;
import software.ecenter.study.bean.QuestionBean.QuestionPayDetailBean;
import software.ecenter.study.bean.QuestionBean.TopicDetailBean;
import software.ecenter.study.bean.QuestionBean.chapterBean;
import software.ecenter.study.bean.QuestionBean.sectionBean;
import software.ecenter.study.bean.QuestionBean.sectionDetailBean;
import software.ecenter.study.bean.QuestionBean.topicBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.net.StudyAPI;
import software.ecenter.study.service.DownLoadIntentService;
import software.ecenter.study.utils.AccountUtil;
import software.ecenter.study.utils.DataUtils;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.PermissionUtils;
import software.ecenter.study.utils.ToastUtils;
import software.ecenter.study.utils.ToolUtil;

/**
 * dec 答疑
 * Created by Mike on 2018/4/25.
 */

public class TabTwoFragment extends BasePicFragment implements View.OnTouchListener {


    Unbinder unbinder;
    @BindView(R.id.btn_right_title)
    Button btnRightTitle;
    @BindView(R.id.radio_one)
    RadioButton radioOne;
    @BindView(R.id.radio_two)
    RadioButton radioTwo;
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;
    @BindView(R.id.btn_nianji_chose)
    RelativeLayout btnNianjiChose;
    @BindView(R.id.btn_xueke_chose)
    RelativeLayout btnXuekeChose;
    @BindView(R.id.btn_shujie_chose)
    RelativeLayout btnShujieChose;
    @BindView(R.id.edit_miaoshu)
    EditText editMiaoshu;
    @BindView(R.id.btn_tijiao_tiwen)
    Button btnTijiaoTiwen;
    @BindView(R.id.list_tip)
    RecyclerView listTip;
    @BindView(R.id.nianji_tip)
    TextView nianjiTip;
    @BindView(R.id.nianji_text)
    TextView nianjiText;
    @BindView(R.id.nianji_img)
    ImageView nianjiImg;
    @BindView(R.id.xueke_tip)
    TextView xuekeTip;
    @BindView(R.id.xueke_text)
    TextView xuekeText;
    @BindView(R.id.xueke_img)
    ImageView xuekeImg;
    @BindView(R.id.shujie_chose_tip)
    TextView shujieChoseTip;
    @BindView(R.id.shujie_chose_text)
    TextView shujieChoseText;
    @BindView(R.id.shujie_chose_img)
    ImageView shujieChoseImg;
    @BindView(R.id.list_image)
    RecyclerView listImage;

    @BindView(R.id.btn_refresh_net)
    LinearLayout btnRefreshNet;
    @BindView(R.id.iv_auditions)
    ImageView ivAuditions;
//    @BindView(R.id.tv_tip)

    AutoSplitTextView tvTip;
    @BindView(R.id.tv_one_key_clear)
    TextView tvOneKeyClear;
    @BindView(R.id.lv_text)
    TextView lvText;    //录制或播放时间
    @BindView(R.id.tip_ly)
    ImageButton tipLy;  //有录音文件的按钮，用于控制播放和停止播放
    @BindView(R.id.rl_lv_yin)
    RelativeLayout rlLuYin;  //录音播放暂停、显示时间、删除按钮对应布局
    @BindView(R.id.iv_play_or_pause)
    ImageView ivPlayOrPause;//录制和暂停录制按钮
    @BindView(R.id.iv_delete_lu_yin)
    ImageView ivDeleteLuYin; //删除录音按钮
    @BindView(R.id.dayi_tv_ly_submit)
    TextView tvDaYiLuYinSubmit; // 完成录音按钮


    private SpinnerPopWindow spinnerPopWindowNJ;
    private SpinnerPopWindow spinnerPopWindowXK;
    private SpinnerPopWindow spinnerPopWindowZJ;

    private List<String> spinnerList;
    private List<String> spinnerListtwo;
    private List<String> spinnerListThree;

    private DayiDetailBean mDayiDetailBean;
    private ChapterDetailBean mChapterDetailBean;
    private sectionDetailBean mSectionDetailBean;
    private TopicDetailBean mTopicDetailBean;
    private CurriculumBean curriculumBean;
    private ListCurriculumBean listCurriculumBean;
    private BookBean bookBean;
    private chapterBean chapterBean;
    private sectionBean sectionBean;
    private topicBean topicBean;

    private String grade = "";
    private String userGrade= "";
    private String subject= "";
    private int curriculumId = 0;//精品课程Id

    private QuestionPayDetailBean mQuestionPayDetailBean;
    private List<ResourceBean> ResourceListData;

    private String mShujieText; //书章节选择完后显示的文字
    private TipQuestResourceAdapter mTipResourceAdapter;

    private String ChapterDetailId; //章节详情Id
    private String audioPath;  //录音路径
    private List<String> imagelistDatasForOss = new ArrayList<>(); //选取的图片路径
    private List<ImageBean> imageBeanList = new ArrayList<>();;    //选取的图片集合
    private int source = 1;    //对应类型，0：普通课程跳转的为0   1：默认答疑   2：精品课程答疑
    private String resourceId = "";  //从资源界面跳转过来的资源Id
    private boolean isCurriculum = false;  //是否精品课程资源
    private int questionType = 0;  //问题类型，0：龙门图书问题，1：非龙门图书问题，2：精品课程资源问题
    private boolean isreset = false; //设置点击回复初始状态

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message message) {

            //首先判断是否有语音路径
            // 没语音、有图
            if (TextUtils.isEmpty(tempRecorderPath)) {
                if (imagelistDatasForOss != null && imagelistDatasForOss.size() == imageBeanList.size()) {
//                    if (message.arg1 == imageBeanList.size())
                        updateData();
                    return;
                }
                //如果有语音路径并且，有图片
            } else if (imageBeanList != null &&  imageBeanList.size()>0) {
                if (!TextUtils.isEmpty(audioPath)) {
                    if (imagelistDatasForOss != null && imagelistDatasForOss.size() == imageBeanList.size()) {
//                        if (message.arg1 == imageBeanList.size())
                            updateData();
                        return;
                    }
                }
                //有语音没图片
            } else if (imageBeanList == null || imageBeanList.size() <= 0) {
                if (!TextUtils.isEmpty(audioPath)) {
                    if (message.arg1 == imageBeanList.size())
                        updateData();
                    return;
                }
            }
            //没语音有图
            if (TextUtils.isEmpty(audioPath)) {
                if (imagelistDatasForOss != null && imagelistDatasForOss.size() == imageBeanList.size()) {
                    updateData();
                    return;
                }
            }

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_two, null);
        tvTip = mRootView.findViewById(R.id.tv_tip);
        unbinder = ButterKnife.bind(this, mRootView);
        if (imagelistDatasForOss == null)
            imagelistDatasForOss = new ArrayList<>();
        initImage("请上传清晰完整的问题图片");
        getIntentData();
        initView();
        if (!TextUtils.isEmpty(resourceId)) {
            int intTyperesourceId = 0;
            intTyperesourceId = Integer.parseInt(resourceId);
            if (intTyperesourceId != 0)
                getRecommendInfo(intTyperesourceId);

        } else {

        }
        tvTip.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (source != 2) {
                    tvTip.setText(getResources().getString(R.string.quest_tip));
                    tvTip.setText(autoSplitText(tvTip, "●  "));
                } else if (source == 2) {
                    tvTip.setText(getResources().getString(R.string.quest_jing_pin_tip));
                    tvTip.setText(autoSplitText(tvTip, "●  "));
                }
            }
        });

        return mRootView;

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        String curGrade = AccountUtil.getRealGrade(mContext);
        if (grade != null && !TextUtils.isEmpty(userGrade) && !userGrade.contains(curGrade)) {
            setGrade();
        }
    }

    private void initView() {
        spinnerList = new ArrayList<>();
        spinnerListtwo = new ArrayList<>();
        spinnerListThree = new ArrayList<>();
        //解决ScrollView中嵌入一个或多个RecyclerView页面滑动卡顿
        listTip.setHasFixedSize(true);
        listTip.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManagerOne = new LinearLayoutManager(mContext);
        linearLayoutManagerOne.setOrientation(LinearLayoutManager.VERTICAL);
        listTip.setLayoutManager(linearLayoutManagerOne);
        listTip.setVisibility(View.GONE);

        spinnerList = ToolUtil.StringToArray(Grade, ",");
        spinnerPopWindowNJ = new SpinnerPopWindow(mContext);
        spinnerPopWindowNJ.refreshData(spinnerList);
        spinnerPopWindowNJ.setPopTitle("选择年级");
        setGrade();
        //年级
        spinnerPopWindowNJ.setItemSelectListener(new SpinnerPopWindow.MItemSelectListener() {
            @Override
            public void onItemClick(int position) {
                shujieChoseText.setText("请选择");
                shujieChoseText.setHint("请选择");

                xuekeText.setText("请选择");
                xuekeText.setHint("请选择");

                grade = spinnerList.get(position);
                nianjiText.setText(grade);
            }
        });

        spinnerPopWindowNJ.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                nianjiImg.setImageResource(R.drawable.xuanze1);
            }
        });

        //学科
        spinnerListtwo = ToolUtil.StringToArray(XueKe, ",");
        spinnerPopWindowXK = new SpinnerPopWindow(mContext);
        spinnerPopWindowXK.refreshData(spinnerListtwo);
        spinnerPopWindowXK.setPopTitle("选择学科");
        spinnerPopWindowXK.setItemSelectListener(new SpinnerPopWindow.MItemSelectListener() {
            @Override
            public void onItemClick(int position) {
                shujieChoseText.setHint("请选择");
                shujieChoseText.setText("请选择");
                subject = spinnerListtwo.get(position);
                xuekeText.setText(subject);
                getAllData();
            }
        });
        spinnerPopWindowXK.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                xuekeImg.setImageResource(R.drawable.xuanze1);
            }
        });


        //图片
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 4);
        listImage.setLayoutManager(gridLayoutManager);
        imageAdapter = new ImageAdapter(mContext, listImageData);
        imageAdapter.setItemClickListener(new ImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (listImageData.get(position).isAddImage()) {
                    PermissionUtils.newIntence().requestPerssion(getActivity(), ToolUtil.PERSSION_PHOTO, 101, new PermissionUtils.IPermission() {
                        @Override
                        public void success(int code) {
                            spinnerPopWindow.showAtLocation(mRootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                        }
                    });

                }
            }
        });
        imageAdapter.setmItemBtnAddClickListener(new ImageAdapter.OnItemBtnAddClickListener() {
            @Override
            public void onItemBtnAddClick(int position) {
                if (listImageData.size() > 0 && !listImageData.get(listImageData.size() - 1).isAddImage()) {//如果最后一个不是添加图，就加上
                    ImageBean item = new ImageBean();
                    item.setAddImage(true);
                    listImageData.add(item);
                }

                listImageData.remove(position);
                imageAdapter.refreshData(listImageData);

            }
        });
        listImage.setAdapter(imageAdapter);
        //btnLvYin.setOnTouchListener(this);
        if (source == 2) {
            tvTip.setText(getResources().getString(R.string.quest_jing_pin_tip));
            tvTip.setText(autoSplitText(tvTip, "●  "));
        }


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_one:
                        btnShujieChose.setVisibility(View.VISIBLE);
                        questionType = 0;
                        break;
                    case R.id.radio_two:
                        btnShujieChose.setVisibility(View.GONE);
                        questionType = 1;
                        break;
                }
            }
        });
    }

    private void getIntentData() {
        if (getArguments() != null) {
            source = getArguments().getInt("source", 1);
            resourceId = getArguments().getString("resourceId", "");
        }
    }

    private void setGrade() {
        //设置默认年级
        DataUtils getData = new DataUtils().invoke();
        int mMonth = getData.getmMonth();
        int mDay = getData.getmDay();
        String curGrade = AccountUtil.getRealGrade(mContext);
        if (!TextUtils.isEmpty(curGrade) || !userGrade.contains(curGrade)) {
            nianjiText.setText(curGrade);
            grade = curGrade;
            userGrade = curGrade;

        }
        nianjiText.setText(grade);

    }

    private void setGrade(String curGrade) {
        //设置默认年级
        DataUtils getData = new DataUtils().invoke();
        int mMonth = getData.getmMonth();
        int mDay = getData.getmDay();
        if (mMonth >= 2 && ((mMonth < 7) || (mMonth == 7 && mDay <= 1))) {
            nianjiText.setText((curGrade.contains("下") || curGrade.contains("上")) ? curGrade : curGrade + "下");
            grade = nianjiText.getText().toString();
        } else {
            nianjiText.setText((curGrade.contains("上") || curGrade.contains("下")) ? curGrade : curGrade + "上");
            grade = nianjiText.getText().toString();
        }
    }

    public void getAllData() {
        if (!showNetWaitLoding()) {
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("grade", grade);
            json.put("subject", subject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
        StudyAPI studyAPI = RetroFactory.getRetroFactory(mContext);
        new RetroFactory(mContext, !isCurriculum ? studyAPI.getQuestionBookList(body) : studyAPI.getCurriculums(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        // btnRefreshNet.setVisibility(View.GONE);
                        if (isCurriculum) {
                            ListCurriculumBean bean = ParseUtil.parseBean(response, ListCurriculumBean.class);
                            initView(bean);
                            return;
                        }
                        DayiDetailBean bean = ParseUtil.parseBean(response, DayiDetailBean.class);
                        initView(bean);
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        //btnRefreshNet.setVisibility(View.VISIBLE);
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }

    //章获取接口
    public void getQuestionChapterList() {
        if (!showNetWaitLoding()) {
            return;
        }

        Log.i("===============", "getQuestionChapterList: 进来了1");
        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        map.put("bookId", bookBean.getBookId());
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getQuestionChapterList(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        // HomeBean临时变量，只含有book数据
                        mChapterDetailBean = ParseUtil.parseBean(response, ChapterDetailBean.class);
                        spinnerListThree.clear();
                        for (chapterBean item : mChapterDetailBean.getData().getChapterList()) {
                            spinnerListThree.add(item.getChapterName());
                        }
                        spinnerPopWindowZJ.TagOneOk(bookBean.getBookName(), "请选择章");
                        spinnerPopWindowZJ.refreshData(spinnerListThree);
                        //选择章节后给文本显示出来
                        Log.i("===============", "getQuestionChapterList: 进来了2" + bookBean.getBookName());

                        //shujieChoseText.setText(bookBean.getBookName());
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                        Log.i("===============", "getQuestionChapterList: 进来了3");
                    }

                });
    }

    //节获取接口
    public void getQuestionSectionList() {
        if (!showNetWaitLoding()) {
            return;
        }

        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        map.put("chapterId", chapterBean.getChapterId());
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getQuestionSectionList(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        // HomeBean临时变量，只含有book数据
                        mSectionDetailBean = ParseUtil.parseBean(response, sectionDetailBean.class);
                        spinnerListThree.clear();
                        for (sectionBean item : mSectionDetailBean.getData().getSectionList()) {
                            spinnerListThree.add(item.getSectionName());
                        }
                        spinnerPopWindowZJ.TagTwoOk(chapterBean.getChapterName(), "请选择节");
                        spinnerPopWindowZJ.refreshData(spinnerListThree);

                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }
                });
    }

    //题获取接口
    public void getQuestionTopicList() {
        if (!showNetWaitLoding()) {
            return;
        }

        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        map.put("sectionId", sectionBean.getSectionId());
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getQuestionTopicList(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        // HomeBean临时变量，只含有book数据
                        mTopicDetailBean = ParseUtil.parseBean(response, TopicDetailBean.class);
                        spinnerListThree.clear();
                        for (topicBean item : mTopicDetailBean.getData().getTopicList()) {
                            spinnerListThree.add(item.getTopicName());
                        }
                        spinnerPopWindowZJ.TagThreeOk(sectionBean.getSectionName(), "请选择题");
                        spinnerPopWindowZJ.refreshData(spinnerListThree);

                        //推荐是跟题相关联的
                        ResourceListData = mTopicDetailBean.getData().getResourceList();
                        if (ResourceListData == null) {
                            return;
                        }
                        /*if (mTipResourceAdapter == null) {
                            mTipResourceAdapter = new TipResourceAdapter(mContext, ResourceListData);
                            listTip.setAdapter(mTipResourceAdapter);

                            mTipResourceAdapter.setItemClickListener(new TipResourceAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    Intent intent = new Intent(mContext, SeeResourcesVideoActivity.class);
                                    intent.putExtra("resourceId", ResourceListData.get(position).getResourceId());
                                    startActivity(intent);


                                }
                            });
                        } else {
                            mTipResourceAdapter.refreshData(ResourceListData);
                        }*/


                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }

    public void initView(DayiDetailBean bean) {
        if (isCurriculum) {
            return;
        }
        mDayiDetailBean = bean;
        //书章节
        spinnerListThree.clear();
        //final List<String> spinnerListThree = ToolUtil.StringToArray(XueKe,",");
        for (BookBean book : mDayiDetailBean.getData().getBookList()) {
            spinnerListThree.add(book.getBookName());
        }
        spinnerPopWindowZJ = new SpinnerPopWindow(mContext);
        spinnerPopWindowZJ.refreshData(spinnerListThree);
        spinnerPopWindowZJ.setPopTitle("选择书章节");
        spinnerPopWindowZJ.toTagMode("请选择书名");
        spinnerPopWindowZJ.setItemSelectListener(new SpinnerPopWindow.MItemSelectListener() {
            @Override
            public void onItemClick(int position) {
                setSpinnerPopWindowZJ(position);
            }
        });
        spinnerPopWindowZJ.setOnclickListener(new SpinnerPopWindow.MOnclickListener() {
            @Override
            public void onClickListener(View view, String upName) { //upName是上一级的内容汉字
                int viewId = view.getId();
                switch (viewId) {
                    case R.id.tag_one_text:
                        getAllData();
                        break;
                    case R.id.tag_two_text:
                        spinnerPopWindowZJ.TagOneOk(bookBean.getBookName(), "请选择章");
                        getQuestionChapterList();
                        break;
                    case R.id.tag_three_text:

                        spinnerPopWindowZJ.TagTwoOk(chapterBean.getChapterName(), "请选择节");
                        getQuestionSectionList();
                        break;
                    case R.id.tag_four_text:
                        spinnerPopWindowZJ.TagThreeOk(chapterBean.getChapterName(), "请选择题");
                        getQuestionTopicList();
                        break;
                }
            }
        });
        spinnerPopWindowZJ.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                shujieChoseImg.setImageResource(R.drawable.xuanze1);
            }
        });
        if (questionType != 1)
            spinnerPopWindowZJ.showAtLocation(mRootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        shujieChoseImg.setImageResource(R.drawable.xuanze2);


    }

    public void initView(ListCurriculumBean bean) {
        if (!isCurriculum) {
            return;
        }
        listCurriculumBean = bean;
        //书章节
        spinnerListThree.clear();
        //final List<String> spinnerListThree = ToolUtil.StringToArray(XueKe,",");
        for (ListCurriculumBean.DataBean.CurriculumListBean book : listCurriculumBean.getData().getCurriculumList()) {
            spinnerListThree.add(book.getCurriculumName());
        }
        spinnerPopWindowZJ = new SpinnerPopWindow(mContext);
        spinnerPopWindowZJ.refreshData(spinnerListThree);
        spinnerPopWindowZJ.setPopTitle("选择精品课程");
        spinnerPopWindowZJ.toTagMode("请选择精品课程名");
        spinnerPopWindowZJ.setItemSelectListener(new SpinnerPopWindow.MItemSelectListener() {
            @Override
            public void onItemClick(int position) {
                //setSpinnerPopWindowZJ(position);
                shujieChoseText.setText(spinnerListThree.get(position));
                for (ListCurriculumBean.DataBean.CurriculumListBean curriculumListBean : listCurriculumBean.getData().getCurriculumList()) {
                    if (curriculumListBean.getCurriculumName().contains(spinnerListThree.get(position))) {
                        curriculumId = Integer.parseInt(curriculumListBean.getCurriculumId());
                    }
                    spinnerPopWindowZJ.dismiss();


                }
            }
        });
        spinnerPopWindowZJ.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                shujieChoseImg.setImageResource(R.drawable.xuanze1);
            }
        });

        spinnerPopWindowZJ.showAtLocation(mRootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        shujieChoseImg.setImageResource(R.drawable.xuanze2);


    }

    private void setSpinnerPopWindowZJ(int position) {
        switch (spinnerPopWindowZJ.getLevel()) {
            case 0:
                bookBean = mDayiDetailBean.getData().getBookList().get(position);
                //重新选择了书：把章节信息全重置；
                chapterBean = null;
                sectionBean = null;
                topicBean = null;
                //选择书章节后弹出框退出
                getQuestionChapterList();
                //spinnerPopWindowZJ.dismiss();

                break;
            case 1:
                chapterBean = mChapterDetailBean.getData().getChapterList().get(position);
                ChapterDetailId = chapterBean.getChapterId();
                if (1 == chapterBean.getHasSection()) { //是否有节（0、否  1、是）
                    getQuestionSectionList();
                } else {
                    mShujieText = bookBean.getBookName() + "-" + chapterBean.getChapterName();
                    shujieChoseText.setText(mShujieText);
                    spinnerPopWindowZJ.dismiss();
                }
                break;
            case 2:
                sectionBean = mSectionDetailBean.getData().getSectionList().get(position);
                ChapterDetailId = sectionBean.getSectionId();//覆盖上个ID
                if (1 == sectionBean.getHasTopic()) {
                    getQuestionTopicList();
                } else {
                    mShujieText = bookBean.getBookName() + "-" + chapterBean.getChapterName() + "-" + sectionBean.getSectionName();
                    shujieChoseText.setText(mShujieText);
                    spinnerPopWindowZJ.dismiss();
                }
                break;

            case 3:
                topicBean = mTopicDetailBean.getData().getTopicList().get(position);
                ChapterDetailId = topicBean.getTopicId();//覆盖上个ID
                String text = bookBean.getBookName() + "-" + chapterBean.getChapterName() + "-" + sectionBean.getSectionName() + "-" + topicBean.getTopicName();
                shujieChoseText.setText(text);
                spinnerPopWindowZJ.dismiss();
                break;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_nianji_chose, R.id.btn_xueke_chose, R.id.btn_refresh_net,
            R.id.btn_shujie_chose, R.id.btn_tijiao_tiwen,
            R.id.btn_right_title, R.id.iv_auditions, R.id.tv_one_key_clear,
            R.id.iv_play_or_pause, R.id.dayi_tv_ly_submit, R.id.tip_ly,
            R.id.iv_delete_lu_yin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_nianji_chose://选择年级
                spinnerPopWindowNJ.showAtLocation(mRootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                nianjiImg.setImageResource(R.drawable.xuanze2);
                break;
            case R.id.btn_xueke_chose://学科
                spinnerPopWindowXK.showAtLocation(mRootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                xuekeImg.setImageResource(R.drawable.xuanze2);
                break;
            case R.id.btn_refresh_net:
                getAllData();
                break;
            case R.id.btn_shujie_chose://选择章节
                clickShuJieChose();
                break;
            case R.id.btn_tijiao_tiwen://提交问题
                submitTiwen();
                break;
            case R.id.btn_right_title://我的提问
                startActivity(new Intent(mContext, QuestionActivity.class));
                break;
            case R.id.rl_lv_yin:
                break;
            case R.id.iv_auditions:
                if (hasRecording && !isRecording) {
                    if (!isPlaying) {
                        startplayRecording();//播放
                    } else {
                        stopPlayRecording();//停止播放
                    }
                }
                break;
            case R.id.tv_one_key_clear://清空
                resetUI();
                setGrade();
                isreset = false;
                break;
            case R.id.iv_play_or_pause:
                if (isreset) {
                    isreset = false;
                    ivPlayOrPause.setImageDrawable(getResources().getDrawable(R.drawable.ks));
                    return;
                }
                audioPlayOrPause();
                break;
            case R.id.dayi_tv_ly_submit:
                if (isRecording) {
                    if (isPause) { //判断是否暂停录音
                        resumeRecording();
                        ivPlayOrPause.setImageDrawable(getResources().getDrawable(R.drawable.zt));
                    }
                    finishRecord();
                }
                break;
            case R.id.tip_ly:
                if (hasRecording && !isRecording) {
                    if (!isPlaying) {
                        startplayRecording();//播放
                        tipLy.setBackground(getResources().getDrawable(R.drawable.hjk));
                    } else {
                        tipLy.setBackground(getResources().getDrawable(R.drawable.bf2));
                        stopPlayRecording();//停止播放
                    }
                }
                break;
            case R.id.iv_delete_lu_yin:
                ivPlayOrPause.setImageDrawable(getResources().getDrawable(R.drawable.cf1));
                rlLuYin.setBackground(getResources().getDrawable(R.drawable.btn_ly_shape_circle_unclick));
                ivPlayOrPause.setEnabled(true);
                isreset = true;
                ivDeleteLuYin.setVisibility(View.GONE);
                tipLy.setBackground(getResources().getDrawable(R.drawable.bf1));
                lvText.setText("");
                if (isPlaying)
                    stopPlayRecording();
                clearAudio();
                break;
        }
    }

    /**
     * 点击完成录制相应操作
     */
    private void finishRecord() {
        ivPlayOrPause.setImageDrawable(getResources().getDrawable(R.drawable.cf2));
        rlLuYin.setBackground(getResources().getDrawable(R.drawable.btn_ly_shape_circle_click));
        ivPlayOrPause.setEnabled(false);
        tvDaYiLuYinSubmit.setEnabled(false);
        tipLy.setBackground(getResources().getDrawable(R.drawable.bf2));
        tvDaYiLuYinSubmit.setBackground(getResources().getDrawable(R.drawable.btn_ly_shape_circle_unclick));
        ivDeleteLuYin.setVisibility(View.VISIBLE);
        stopActionRecording();
    }

    private void audioPlayOrPause() {
        if (isRecording) {  //判断是否正在录音
            if (isPause) { //判断是否暂停录音
                resumeRecording();
                ivPlayOrPause.setImageDrawable(getResources().getDrawable(R.drawable.zt));
            } else {
                pauseRecording();
                ivPlayOrPause.setImageDrawable(getResources().getDrawable(R.drawable.ks));

            }
        } else {
            startRecording();
//            dayi_tv_ly_submit
            tvDaYiLuYinSubmit.setBackground(getResources().getDrawable(R.drawable.btn_ly_shape_circle_green_click));
            tvDaYiLuYinSubmit.setEnabled(true);
            ivPlayOrPause.setImageDrawable(getResources().getDrawable(R.drawable.zt));
        }
    }

    private void clickShuJieChose() {
        if (TextUtils.isEmpty(nianjiText.getText().toString())||"请选择".equals(nianjiText.getText().toString())) {
            ToastUtils.showToastSHORT(mContext, "请选择年级");
            dismissNetWaitLoging();
            return;
        }
        if (TextUtils.isEmpty(xuekeText.getText().toString())||"请选择".equals(xuekeText.getText().toString())) {
            ToastUtils.showToastSHORT(mContext, "请选择学科");
            dismissNetWaitLoging();
            return;
        }
        if (spinnerPopWindowZJ != null && spinnerPopWindowZJ.getLevel() != -1) {
            spinnerPopWindowZJ.clearData();
            spinnerListThree.clear();
            if (!isCurriculum) {
                for (BookBean book : mDayiDetailBean.getData().getBookList()) {
                    spinnerListThree.add(book.getBookName());
                }
            } else {
                for (ListCurriculumBean.DataBean.CurriculumListBean book : listCurriculumBean.getData().getCurriculumList()) {
                    spinnerListThree.add(book.getCurriculumName());
                }
            }

            spinnerPopWindowZJ.refreshData(spinnerListThree);
            spinnerPopWindowZJ.setPopTitle("选择书章节");
            spinnerPopWindowZJ.toTagMode("请选择书名");
            spinnerPopWindowZJ.showAtLocation(mRootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            shujieChoseImg.setImageResource(R.drawable.xuanze2);
        } else {
            getAllData();
        }
    }

    private void submitTiwen() {
        if (isRecording && !isPause) {
            ToastUtils.showToastLONG(mContext, "请停止录音");
            return;
        }

        if (TextUtils.isEmpty(nianjiText.getText().toString())||"请选择".equals(nianjiText.getText().toString())) {
            ToastUtils.showToastSHORT(mContext, "请选择年级");
            return;
        }
        if (TextUtils.isEmpty(xuekeText.getText().toString())||"请选择".equals(xuekeText.getText().toString())) {
            ToastUtils.showToastSHORT(mContext, "请选择学科");
            return;
        }
        String bookId = "";
        String chapterId = "";
        String sectionId = "";
        String topicId = "";


        if (radioOne.isChecked()) {
            if (TextUtils.isEmpty(shujieChoseText.getText().toString())||"请选择".equals(shujieChoseText.getText().toString())) {
                ToastUtils.showToastSHORT(mContext, "请选择书章节");
                return;
            }

            if (bookBean != null) {
                bookId = bookBean.getBookId();
            }
            if (chapterBean != null) {
                chapterId = chapterBean.getChapterId();
            }

            if (sectionBean != null) {
                sectionId = sectionBean.getSectionId();
            }
            if (topicBean != null) {
                topicId = topicBean.getTopicId();
            }
            if (TextUtils.isEmpty(bookId)) {
                ToastUtils.showToastSHORT(mContext, "请选择书籍");
                return;
            }

        }
        if (isRecording && isPause) {
            ToastUtils.showToastSHORT(mContext, "请上传题目图片，并配以文字或语音说明（可选）");
            return;
        }
        if (imagelistDatasForOss != null)
            imagelistDatasForOss.clear();
        if (imageBeanList == null)
            imageBeanList = new ArrayList<>();

        imageBeanList.clear();
        for (ImageBean imageBean : listImageData) {
            if (!TextUtils.isEmpty(imageBean.getTargetPicPath())) {
                imageBeanList.add(imageBean);
            }
        }
              /*  ;
                startActivity(new Intent(mContext, AnswerBuyActivity.class));*/
        if (TextUtils.isEmpty(tempRecorderPath) && imageBeanList.size() <= 0) {
            if (!TextUtils.isEmpty(editMiaoshu.getText().toString().trim())) {
                updateData();
            } else {
                ToastUtils.showToastSHORT(mContext, "请上传题目图片，并配以文字或语音说明（可选）");
            }
            return;
        }
        getOssToken();
    }

    /**
     * 更换接口，但是这个保留以防有什么问题
     */
    @Deprecated
    public void submitUserQuestion() {

        if (TextUtils.isEmpty(nianjiText.getText().toString())||"请选择".equals(nianjiText.getText().toString())) {
            ToastUtils.showToastSHORT(mContext, "请选择年级");
            return;
        }
        if (TextUtils.isEmpty(xuekeText.getText().toString())||"请选择".equals(xuekeText.getText().toString())) {
            ToastUtils.showToastSHORT(mContext, "请选择学科");
            return;
        }


        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        map.put("grade", nianjiText.getText().toString());
        map.put("subject", xuekeText.getText().toString());
        String bookId = "";
        String chapterId = "";
        String sectionId = "";
        String topicId = "";


        if (radioOne.isChecked()) {
            if (TextUtils.isEmpty(shujieChoseText.getText().toString())||"请选择".equals(shujieChoseText.getText().toString())) {
                ToastUtils.showToastSHORT(mContext, "请选择书章节");
                return;
            }

            if (bookBean != null) {
                bookId = bookBean.getBookId();
            }
            if (chapterBean != null) {
                chapterId = chapterBean.getChapterId();
            }

            if (sectionBean != null) {
                sectionId = sectionBean.getSectionId();
            }
            if (topicBean != null) {
                topicId = topicBean.getTopicId();
            }
            if (TextUtils.isEmpty(bookId)) {
                ToastUtils.showToastSHORT(mContext, "请选择书籍");
                return;
            }
            if (TextUtils.isEmpty(chapterId)) {
                ToastUtils.showToastSHORT(mContext, "请选择节");
                return;
            }
            if (TextUtils.isEmpty(topicId)) {
                ToastUtils.showToastSHORT(mContext, "请选择题");
                return;
            }
        }
        map.put("bookId", bookId);
        map.put("chapterId", chapterId);
        map.put("sectionId", sectionId);
        map.put("topicId", topicId);

        if (TextUtils.isEmpty(editMiaoshu.getText().toString()) && !(listImageData != null && listImageData.size() > 1) && TextUtils.isEmpty(tempRecorderPath)) {
            ToastUtils.showToastSHORT(mContext, "请上传题目图片，并配以文字或语音说明（可选）");
            return;
        }

        if (!showNetWaitLoding()) {
            return;
        }

        map.put("describe", editMiaoshu.getText().toString());
        List<MultipartBody.Part> parts = new ArrayList<>();
        for (ImageBean item : listImageData) {
            if (!item.isAddImage()) {
                parts.add(RetroFactory.prepareFilePart("image", new File(item.getTargetPicPath())));
            }
        }

        MultipartBody.Part part = null;
        if (tempRecorderPath != null) {
            part = RetroFactory.prepareFilePart("audio", new File(tempRecorderPath));
        }
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).submitUserQuestion(map, parts, part))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        mQuestionPayDetailBean = ParseUtil.parseBean(response, QuestionPayDetailBean.class);
                        //ToastUtils.showToastLONG(mContext, "提交成功");
                        if ("1".equals(mQuestionPayDetailBean.getData().getHasBuy())) { //需要获取
                            showDialog();
                        } else {
                            editMiaoshu.setText("");
                            clearImageChose();
                            clearAudio();
                            ToastUtils.showToastSHORT(mContext, "提交成功");
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();

                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });

    }

    public void showDialog() {
        MyDialog.Builder builder = new MyDialog.Builder(mContext);
        builder.setTitle("提示");
        builder.setMessage("人工答疑需要一点解答时间，该问题已有疑难解答视频，现在去看吗？");

        builder.setPositiveButton("继续问老师",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(mContext, AnswerBuyActivity.class);
                        intent.putExtra("describe", editMiaoshu.getText().toString());
                        intent.putExtra("mQuestionPayDetailBean", mQuestionPayDetailBean);
                        intent.putExtra("audioPath", tempRecorderPath);
                        intent.putExtra("recordingSecond", recordingSecond);
                        intent.putExtra("buyId", mQuestionPayDetailBean.getData().getQuestionId());
                        intent.putExtra("type", 3);
                        intent.putExtra("buyType", 6);
                        startActivityForResult(intent, 111);
                        dialog.dismiss();
                    }
                });

        builder.setNegativeButton("立即看视频", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //因为章节题关联的资源是个列表

                if (mQuestionPayDetailBean.getData().getHasBuy().equals("1") && !TextUtils.isEmpty(mQuestionPayDetailBean.getData().getResourceId())) {
                    Intent intent1 = new Intent(mContext, SeeResourcesVideoActivity.class);
                    intent1.putExtra("resourceId", mQuestionPayDetailBean.getData().getResourceId());
                    startActivity(intent1);
                } else {
                    Intent intent = new Intent(mContext, ChapterDetailsActivity.class);
                    intent.putExtra("chapterId", ChapterDetailId);
                    intent.putExtra("chapterName", "");
                    startActivity(intent);
                }
                dialog.dismiss();
                //设置你的操作事项
            }
        });

        builder.create().show();

    }


    @Override
    public void curRecordingSecond(int second) {
//        lvText.setText("录制中(" + second + " s)");
        if (!hasRecording && isRecording) lvText.setText(second + "s");

        String str = "";
        ivAuditions.setVisibility(View.GONE);
    }

    @Override
    public void recordingFinished() {
        if (hasRecording)
            lvText.setText(recordingSecond + "s");
        // ivAuditions.setVisibility(View.VISIBLE);
    }

    @Override
    public void recordingFinishedTooMin() {

    }

    @Override
    public void curPlaySecond(int second) {
        lvText.setText(second + " s");
    }

    @Override
    public void playFinished() {
        lvText.setText(recordingSecond + "s");
        tipLy.setBackground(getResources().getDrawable(R.drawable.bf2));
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP://松开事件发生后执行代码的区域
                if (!hasRecording) {
                    isThouchModee = true;
                    stopActionRecording(); //准备停止录音
                } else {
                    isThouchModee = false;
                }
            case MotionEvent.ACTION_DOWN://按住事件发生后执行代码的区域
                if (!hasRecording) {
                    startRecording();//开始录音
                }
                break;
            default:
                break;
        }
        return false;
    }


    public void getOssToken() {
        if (!showNetWaitLoding()) {
            return;
        }
        Map<String, String> header = new HashMap<>();
        header.put("token", AccountUtil.getToken(mContext) + "");
        //获取osstoken
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getOssTokenByDaYi(header))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        Log.e(TAG, "get oss token is successful");
                        OssTokenBean ossTokenBean1 = new OssTokenBean();
                        ossTokenBean1 = ParseUtil.parseBean(response, OssTokenBean.class);
                        Log.e("111111uploadData", "OSSConfig ---"+ossTokenBean1.toString());
                        OssTokenBean ossTokenBean = OssTokenBean.getInstance();
                        ossTokenBean.getData().setAccessKeyId(ossTokenBean1.getData().getAccessKeyId());
                        ossTokenBean.getData().setAccessKeySecret(ossTokenBean1.getData().getAccessKeySecret());
                        ossTokenBean.getData().setBucket(ossTokenBean1.getData().getBucket());
                        ossTokenBean.getData().setEndPoint(ossTokenBean1.getData().getEndPoint());
                        ossTokenBean.getData().setExpiration(ossTokenBean1.getData().getExpiration());
                        ossTokenBean.getData().setSecurityToken(ossTokenBean1.getData().getSecurityToken());
                        ossTokenBean.getData().setUploadUrl(ossTokenBean1.getData().getUploadUrl());
                        ossTokenBean.getData().setUploadFilePath(ossTokenBean1.getData().getUploadFilePath());
                        if ((imageBeanList != null && imageBeanList.size() > 0)) {
                            for (int i = 0; i < imageBeanList.size(); i++) {
                                String targetPicPath = imageBeanList.get(i).getTargetPicPath();
                                if (!TextUtils.isEmpty(targetPicPath))
                                    putData2OSS(i + 1, imageBeanList.get(i));
                            }
                        }
                        if (!TextUtils.isEmpty(tempRecorderPath)) {
                            putData2OSS(0, null);
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        ToastUtils.showToastLONG(mContext, msg);
                        dismissWaitLoging();
                    }
                });

    }

    public void putData2OSS(final int size, final ImageBean imageBean) {
        String path = OssTokenBean.getInstance().getData().getUploadFilePath();
        String fileName = "";
        int fileIndex = 0;
        String localFileUrl = "";
        //判断是否是图片、否则判断是否是语音、否则return
        if (imageBean != null) {
            fileIndex = imageBean.getTargetPicPath().lastIndexOf("/");
            fileName = size+"_"+imageBean.getTargetPicPath().substring(fileIndex + 1, imageBean.getTargetPicPath().length());
            localFileUrl = imageBean.getTargetPicPath();
        } else if (!TextUtils.isEmpty(tempRecorderPath)) {
            fileIndex = tempRecorderPath.lastIndexOf("/");
            fileName = tempRecorderPath.substring(fileIndex + 1, tempRecorderPath.length());
            localFileUrl = tempRecorderPath;
        } else {
            return;
        }


        new PutObjectSamples(OSSConfig.getInstance(mContext),
                OssTokenBean.getInstance().getData().getBucket(),
                path + fileName, localFileUrl) {

        }.asyncPutObjectFromLocalFile(new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(final PutObjectRequest request, PutObjectResult result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("111111uploadData", "onSuccess：===  " +OSSConfig.OSSPATH+ request.getObjectKey());
                        Message message = new Message();
                        if (imageBean != null) {
                            imagelistDatasForOss.add(request.getObjectKey());
                            message.what = 1;
                            message.arg1 = size;
                        } else if (!TextUtils.isEmpty(tempRecorderPath)) {
                            audioPath = request.getObjectKey();
                            message.what = 2;
                            message.arg1 = size;
                        } else {
                            message.what = -2;
                            message.arg1 = size;
                        }
                        handler.sendMessage(message);
                    }
                });

            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientException, ServiceException serviceException) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (imageBean != null) {
                            ToastUtils.showToastSHORT(mContext,"请上传清晰完整的问题图片");
                        } else if (!TextUtils.isEmpty(tempRecorderPath)) {
                            ToastUtils.showToastSHORT(mContext,"上传语音失败");
                        }
                    }
                });
            }
        });
    }

    public void updateData() {
        if (!showNetWaitLoding())
            return;

        //接口入参，如果只有一个参数用键值对，多个参数用json
        JSONObject json = new JSONObject();

        String bookId = "";
        String chapterId = "";
        String sectionId = "";
        String topicId = "";
        StringBuffer imageUrl = new StringBuffer();
        String audioUrl = "";

        if (bookBean != null) {
            bookId = bookBean.getBookId();
        }
        if (chapterBean != null) {
            chapterId = chapterBean.getChapterId();
        }

        if (sectionBean != null) {
            sectionId = sectionBean.getSectionId();
        }
        if (topicBean != null) {
            topicId = topicBean.getTopicId();
        }

        if (TextUtils.isEmpty(nianjiText.getText().toString())||"请选择".equals(nianjiText.getText().toString()) || nianjiText.getText().toString().trim().equals("")) {
            ToastUtils.showToastSHORT(mContext, "请选择年级");
            dismissNetWaitLoging();
            return;
        }
        if (TextUtils.isEmpty(xuekeText.getText().toString())||"请选择".equals(xuekeText.getText().toString()) || xuekeText.getText().toString().trim().equals("")) {
            ToastUtils.showToastSHORT(mContext, "请选择学科");
            dismissNetWaitLoging();
            return;
        }
        if (radioOne.isChecked()) {
            if (TextUtils.isEmpty(shujieChoseText.getText().toString())||"请选择".equals(shujieChoseText.getText().toString()) || shujieChoseText.getText().toString().trim().equals("")) {
                ToastUtils.showToastSHORT(mContext, "请选择书章节");
                dismissNetWaitLoging();
                return;
            }
            if (TextUtils.isEmpty(editMiaoshu.getText().toString().trim()) && !(listImageData != null && listImageData.size() > 1) && TextUtils.isEmpty(tempRecorderPath)) {
                ToastUtils.showToastSHORT(mContext, "请上传题目图片，并配以文字或语音说明（可选）");
                dismissNetWaitLoging();
                return;
            }


            if (bookBean != null) {
                bookId = bookBean.getBookId();
            }
            if (chapterBean != null) {
                chapterId = chapterBean.getChapterId();
            }

            if (sectionBean != null) {
                sectionId = sectionBean.getSectionId();
            }
            if (topicBean != null) {
                topicId = topicBean.getTopicId();
            }
            if (isCurriculum && curriculumId == 0) {
                ToastUtils.showToastSHORT(mContext, "请选择精品课程名称");
                dismissNetWaitLoging();

                return;
            }
        }
        if (imagelistDatasForOss == null || imagelistDatasForOss.size()<1){
            ToastUtils.showToastSHORT(mContext,"请上传清晰完整的问题图片");
            dismissNetWaitLoging();

            return;
        }
        if (imagelistDatasForOss != null) {
            ToolUtil.reciveList(imagelistDatasForOss);
            for (int i = 0; i < imagelistDatasForOss.size(); i++) {
                if (i == imagelistDatasForOss.size() - 1) {
                    imageUrl.append(imagelistDatasForOss.get(i));
                } else {
                    imageUrl.append(imagelistDatasForOss.get(i) + ",");
                }
            }
        }
        if (!TextUtils.isEmpty(audioPath)) {
            audioUrl = audioPath;
        }
        try {
            json.put("grade", nianjiText.getText().toString());
            json.put("subject", xuekeText.getText().toString());
            json.put("bookId", bookId);
            json.put("chapterId", chapterId + "");
            json.put("sectionId", sectionId + "");
            if (!TextUtils.isEmpty(topicId)){
                if (topicId.equals("0")){
                    topicId = "";
                }
            }
            json.put("topicId", topicId + "");
            json.put("describe", editMiaoshu.getText().toString());
            json.put("imageUrl", imageUrl);
            json.put("audioUrl", audioUrl);
            json.put("source", source);
            json.put("audioDuration", recordingSecond);
            json.put("curriculumId", curriculumId);
            json.put("questionType", questionType);
            Log.d("111222333",json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).submitUserQuestionJson(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        mQuestionPayDetailBean = ParseUtil.parseBean(response, QuestionPayDetailBean.class);
                        //ToastUtils.showToastLONG(mContext, "提交成功");
                        if ("1".equals(mQuestionPayDetailBean.getData().getHasBuy())) { //需要获取
                            showDialog();
                        } else if ("0".equals(mQuestionPayDetailBean.getData().getHasBuy())) {
                            Intent intent = new Intent(mContext, AnswerBuyActivity.class);
                            intent.putExtra("describe", editMiaoshu.getText().toString());
                            intent.putExtra("mQuestionPayDetailBean", mQuestionPayDetailBean);
                            intent.putExtra("audioPath", tempRecorderPath);
                            intent.putExtra("buyId", mQuestionPayDetailBean.getData().getQuestionId());
                            intent.putExtra("type", 3);
                            intent.putExtra("buyType", 6);
                            intent.putExtra("recordingSecond", recordingSecond);

                            startActivityForResult(intent, 111);
                        } else {
                            ToastUtils.showToastSHORT(mContext, "提交成功");
                            clearInfo();

                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }

    private void clearInfo() {
        editMiaoshu.setText("");
        clearImageChose();
        clearAudio();
        if (imagelistDatasForOss != null)
            imagelistDatasForOss.clear();
        audioPath = "";
        if (imageBeanList != null)
            imageBeanList.clear();
        bookBean = null;
        chapterBean = null;
        topicBean = null;
        isreset = true;
        sectionBean = null;
    }

    /**
     * Intent intent = new Intent(mContext, AnswerBuyActivity.class);
     * intent.putExtra("describe", editMiaoshu.getText().toString());
     * intent.putExtra("mQuestionPayDetailBean", mQuestionPayDetailBean);
     * intent.putExtra("audioPath", tempRecorderPath);
     * intent.putExtra("buyId", mQuestionPayDetailBean.getData().getQuestionId());
     * intent.putExtra("type", 3);
     * intent.putExtra("buyType", 6);
     * intent.putExtra("recordingSecond", recordingSecond);
     * startActivityForResult(intent, 111);
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 111) {
            editMiaoshu.setText("");
            nianjiText.setText("");
            mQuestionPayDetailBean = new QuestionPayDetailBean();
            tempRecorderPath = "";
            recordingSecond = 0;
            setGrade();
            shujieChoseTip.setText(getResources().getString(R.string.shu_jie_close_title));
        }
        if (resultCode == 111 && requestCode == 111) {
            resetUI();
        }
    }

    private void resetUI() {
        resetAudio();
        editMiaoshu.setText("");
        setGrade();
        mQuestionPayDetailBean = new QuestionPayDetailBean();
        tempRecorderPath = "";
        recordingSecond = 0;
        curriculumId = 0;
        xuekeText.setText("");
        shujieChoseText.setText("");
        switch (source) {
            case 0:
            case 1:
                tvTip.setText(getResources().getString(R.string.quest_tip));
                tvTip.setText(autoSplitText(tvTip, "●  "));
                break;
            case 2:
                tvTip.setText(getResources().getString(R.string.quest_jing_pin_tip));
                tvTip.setText(autoSplitText(tvTip, "●  "));
                break;
        }

        radioGroup.setVisibility(View.VISIBLE);
        shujieChoseTip.setText(getResources().getString(R.string.shu_jie_close_title));
        isCurriculum = false;
        imagelistDatasForOss.clear();
        if (imageBeanList != null) imageBeanList.clear();
        listImageData.clear();
        ImageBean item = new ImageBean();
        item.setAddImage(true);
        listImageData.add(item);
        if (imageAdapter != null) imageAdapter.notifyDataSetChanged();
    }

    private void resetAudio() {
        if (isRecording) {
            stopRecording();
            clearAudio();
            isRecording = false;
        }
        if (isPlaying) {
            stopPlayRecording();
            clearAudio();
            isPlaying = false;
        }
        isreset = true;
        ivPlayOrPause.setEnabled(true);
        ivDeleteLuYin.setVisibility(View.GONE);
        ivPlayOrPause.setImageDrawable(getResources().getDrawable(R.drawable.ks));
        tipLy.setBackground(getResources().getDrawable(R.drawable.bf1));
        lvText.setText("");
        rlLuYin.setBackground(getResources().getDrawable(R.drawable.btn_ly_shape_circle_unclick));
        tvDaYiLuYinSubmit.setBackground(getResources().getDrawable(R.drawable.btn_ly_shape_circle_unclick));
    }

    /**
     * 根据resourceId 获取对应答疑界面信息
     *
     * @param intTyperesourceId
     */
    public void getRecommendInfo(int intTyperesourceId) {
        if (!showNetWaitLoding()) {
            return;
        }
        Map<String, Integer> map = new HashMap<>();
        map.put("resourceId", intTyperesourceId);

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).fromResource(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        RecommendInfoBean recommendInfoBean = new RecommendInfoBean();
                        recommendInfoBean = ParseUtil.parseBean(response, RecommendInfoBean.class);
                        if (recommendInfoBean == null)
                            return;
                        setBaseInfo(recommendInfoBean);
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                    }
                });
    }

    public void setBaseInfo(RecommendInfoBean baseInfo) {
        RecommendInfoBean.DataBean data = baseInfo.getData();
        isCurriculum = data.isCurriculum();
        if (data.isCurriculum()) {
            questionType = 2;
            radioGroup.setVisibility(View.GONE);
            shujieChoseTip.setText(getResources().getString(R.string.shu_jie_close_jingpin_title));
            tvTip.setText(getResources().getText(R.string.quest_jing_pin_tip));
            tvTip.setText(autoSplitText(tvTip, "● "));
        } else {
            radioGroup.setVisibility(View.VISIBLE);
            shujieChoseTip.setText(getResources().getString(R.string.shu_jie_close_title));
            tvTip.setText(getResources().getText(R.string.quest_tip));
            tvTip.setText(autoSplitText(tvTip, "● "));
        }

        subject = data.getSubject();
        xuekeText.setText(subject);
        grade = data.getGrade();
        setGrade(grade);
        bookBean = new BookBean();
        bookBean.setBookId(data.getBookId() + "");
        bookBean.setBookName(data.getBookName());
        chapterBean = new chapterBean();
        chapterBean.setChapterId(data.getCharterId() + "");
        chapterBean.setChapterName(data.getCharterName());
        sectionBean = new sectionBean();
        sectionBean.setSectionId(data.getSectionId() + "");
        sectionBean.setSectionName(data.getSectionName());
        topicBean = new topicBean();
        topicBean.setTopicId(data.getTopicId() + "");
        topicBean.setTopicName(data.getTopicName());
        curriculumBean = new CurriculumBean();
        curriculumBean.setCurriculumId(data.getCurriculumId() + "");
        curriculumBean.setCurriculumName(data.getCurriculumName());
        String shujieChose = "";
        if (bookBean != null && !TextUtils.isEmpty(bookBean.getBookName())) {
            spinnerListThree.add(bookBean.getBookName());
            shujieChose += bookBean.getBookName();
        }
        if (chapterBean != null && !TextUtils.isEmpty(chapterBean.getChapterName())) {
            spinnerListThree.add(chapterBean.getChapterName());
            shujieChose += chapterBean.getChapterName();
        }
        if (sectionBean != null && !TextUtils.isEmpty(sectionBean.getSectionName())) {
            spinnerListThree.add(sectionBean.getSectionName());
            if (!TextUtils.isEmpty(shujieChose))
                shujieChose += "-" + sectionBean.getSectionName();
            else {
                shujieChose += sectionBean.getSectionName();
            }
        }
        if (curriculumBean != null && !TextUtils.isEmpty(curriculumBean.getCurriculumName())) {
            spinnerListThree.add(curriculumBean.getCurriculumName());
            if (TextUtils.isEmpty(shujieChose))
                shujieChose += curriculumBean.getCurriculumName();
            else
                shujieChose += "-" + curriculumBean.getCurriculumName();
        }
        shujieChoseText.setText(shujieChose);
        curriculumId = data.getCurriculumId();
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
                    indentSpace += "   ";
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
}