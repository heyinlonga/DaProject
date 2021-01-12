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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import software.ecenter.study.Adapter.MessageAdapter;
import software.ecenter.study.R;
import software.ecenter.study.bean.Bean;
import software.ecenter.study.bean.MineBean.MessageBean;
import software.ecenter.study.bean.MineBean.MessageBetailBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.AccountUtil;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;

/**
 * 我的消息
 */
public class MessageActivity extends BaseActivity {

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
    @BindView(R.id.list_message)
    RecyclerView listMessage;
    @BindView(R.id.btn_hasRead)
    Button btnHasRead;
    @BindView(R.id.bottom_line)
    LinearLayout bottomLine;
    @BindView(R.id.btn_refresh_net)
    LinearLayout btnRefreshNet;
    @BindView(R.id.ll_mess_no_data)
    LinearLayout llMessNoata;

    private List<MessageBean> mListData;
    private List<MessageBean> mListHasReadData;
    private MessageAdapter mMessageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);

        mListData = new ArrayList<>();

        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        listMessage.setLayoutManager(manager);
        getMessageList(false);
    }


    public void initView(MessageBetailBean mMessageBetailBean) {
        if (mMessageBetailBean != null) {
            mListData.clear();
            MessageBetailBean.Data data = mMessageBetailBean.getData();
            if (data != null) {
                List<MessageBean> list = data.getData();
                if (list != null && list.size() > 0) {
                    mListData.addAll(list);
                }
            }
            sortListData(mListData);
            mMessageAdapter = new MessageAdapter(mContext, mListData);
            setAdapter();
            toCheckMode();
            if (mListData != null && mListData.size() > 0) {
                llMessNoata.setVisibility(View.GONE);
            } else {
                llMessNoata.setVisibility(View.VISIBLE);
            }
        }else {
            llMessNoata.setVisibility(View.VISIBLE);
            listMessage.setVisibility(View.GONE);
        }
    }

    private void setAdapter() {
        mMessageAdapter.setItemClickListener(new MessageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, boolean isJump) {
                if (isJump) {
                    boolean hasCheck = false;
                    for (MessageBean item : mListData) {
                        if (item.isCheck()) {
                            hasCheck = true;
                            break;
                        }
                    }
                    if (hasCheck) return;
                    MessageBean bean = mListData.get(position);
                    Intent intent = new Intent();
                    if (bean.getIsRead() == 0) {
                        bean.setCheck(true);
                        userReadMark();
                        bean.setIsRead(1);
                        mMessageAdapter.notifyItemChanged(position);
                    }
                    switch (bean.getType()) {
                        case 0:  //0:评论通过审核
                            intent.setClass(mContext, SeeResourcesVideoActivity.class);
                            intent.putExtra("resourceId", bean.getRelatedId() + "");
                            startActivity(intent);
                            break;
                        case 1:  //1：反馈回复、其他推送
                            intent.setClass(mContext, MessageDetailActivity.class);
                            intent.putExtra("messageId", mListData.get(position).getMessageId());
                            startActivity(intent);
                            break;
                        case 2:  //2：答疑回复
                            intent.setClass(mContext, QuestionDetailActivity.class);
                            intent.putExtra("questionId", bean.getRelatedId() + "");
                            startActivity(intent);
                            break;
                        case 3:  //3: 上传资源过审
                            intent.setClass(mContext, MyUpdataActivity.class);
                            startActivity(intent);
                            break;
                        case 5:
                            intent.setClass(mContext, MatchDetailActivity.class);
                            intent.putExtra("id", bean.getRelatedId() + "");
                            startActivity(intent);
                            break;
                    }
                } else if (mListData.get(position).isCheckMode()) {
                    CheckItem(position);
                }
            }
        });
        mMessageAdapter.setOnLongClickListener(new MessageAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(int position) {
                if (!mListData.get(position).isCheckMode()) {
                    toCheckMode();
                }
            }
        });
        listMessage.setAdapter(mMessageAdapter);
    }

    private void sortListData(List<MessageBean> mListData) {
        Collections.sort(mListData, new Comparator<MessageBean>() {
            @Override
            public int compare(MessageBean messageBean, MessageBean t1) {
                return (t1.getMessageDate().compareTo(messageBean.getMessageDate()));
            }
        });
    }


    public void getMessageList(boolean isShow) {
        if (isShow) {
            if (!showNetWaitLoding()) {
                return;
            }
        }

        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getMessageList(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        btnRefreshNet.setVisibility(View.GONE);
                        MessageBetailBean mMessageBetailBean = ParseUtil.parseBean(response, MessageBetailBean.class);

                        initView(mMessageBetailBean);
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                        btnRefreshNet.setVisibility(View.VISIBLE);
                        llMessNoata.setVisibility(View.GONE);
                    }

                });

    }

    public void userReadMark() {
        if (!showNetWaitLoding()) {
            return;
        }
        mListHasReadData = new ArrayList<>();
        String Ids = "";
        for (MessageBean item : mListData) {
            if (item.isCheck()) {
                Ids += item.getMessageId() + ",";
                mListHasReadData.add(item);
            }
        }

        if (Ids.endsWith(",")) {
            Ids = Ids.substring(0, Ids.length() - 1);
        }

        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        map.put("messageIds", Ids);

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).userReadMark(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        for (MessageBean item : mListHasReadData) {
                            item.setIsRead(1);
                        }
                        OutCheckMode();
                        Bean bean = new Bean();
                        bean = ParseUtil.parseBean(response, Bean.class);
                        String msg = "";
                        if (bean != null) {
                            msg = bean.getMessage();
                        }
                        ToastUtils.showToastSHORT(mContext, msg);

                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);

                    }

                });

    }


    public void toCheckMode() {
        //全部设置可选模式
        for (MessageBean item : mListData) {
            item.setCheck(false);//全部置为未选中
            item.setCheckMode(true);
        }
//        btnLeftTitle.setVisibility(View.INVISIBLE);
//        btnLeftTitleText.setVisibility(View.VISIBLE);
//        btnRightTitleText.setVisibility(View.VISIBLE);
//        bottomLine.setVisibility(View.VISIBLE);
        mMessageAdapter.notifyDataSetChanged();
    }

    public void OutCheckMode() {
        //全部设置可选模式
//        for (MessageBean item : mListData) {
//            item.setCheckMode(false);
//        }
        for (MessageBean item : mListData) {
            item.setCheck(false);
        }
        btnLeftTitle.setVisibility(View.VISIBLE);
        btnLeftTitleText.setVisibility(View.INVISIBLE);
        btnRightTitleText.setVisibility(View.INVISIBLE);
        bottomLine.setVisibility(View.GONE);
        mMessageAdapter.notifyDataSetChanged();
    }


    public void CheckItem(int position) {
        mListData.get(position).setCheck(mListData.get(position).isCheck() ? false : true);
        //遍历是否有被选中的
        boolean hasCheck = false;
        for (MessageBean item : mListData) {
            if (item.isCheck()) {
                hasCheck = true;
                break;
            }
        }
        if (hasCheck) {
            btnLeftTitle.setVisibility(View.INVISIBLE);
            btnLeftTitleText.setVisibility(View.VISIBLE);
            btnRightTitleText.setVisibility(View.VISIBLE);
            bottomLine.setVisibility(View.VISIBLE);
        } else {
            btnLeftTitle.setVisibility(View.VISIBLE);
            btnLeftTitleText.setVisibility(View.INVISIBLE);
            btnRightTitleText.setVisibility(View.INVISIBLE);
            bottomLine.setVisibility(View.GONE);
        }
        mMessageAdapter.notifyDataSetChanged();
    }

    public void checkAllItem(boolean isCheck) {
        for (MessageBean item : mListData) {
            item.setCheck(isCheck);
        }
        //全部没选中时
        if (!isCheck) {
            btnLeftTitle.setVisibility(View.VISIBLE);
            btnLeftTitleText.setVisibility(View.INVISIBLE);
            btnRightTitleText.setVisibility(View.INVISIBLE);
        }
        mMessageAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.btn_left_title, R.id.btn_left_title_text, R.id.btn_right_title_text, R.id.btn_hasRead, R.id.btn_refresh_net})
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
            case R.id.btn_hasRead://批量删除
                messdel();
                break;
            case R.id.btn_refresh_net:
                getMessageList(true);
                break;
        }
    }

    //批量删除
    public void messdel() {
        if (!showNetWaitLoding()) {
            return;
        }
        String Ids = "";
        for (MessageBean item : mListData) {
            if (item.isCheck()) {
                Ids += item.getMessageId() + ",";
            }
        }
        if (Ids.length() == 0) return;
        if (Ids.endsWith(",")) {
            Ids = Ids.substring(0, Ids.length() - 1);
        }

        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        map.put("messageIds", Ids);

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).userReadDel(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        OutCheckMode();
                        getMessageList(true);
                        Bean bean = ParseUtil.parseBean(response, Bean.class);
                        if (bean != null) {
                            ToastUtils.showToastSHORT(mContext, bean.getMessage());
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);

                    }

                });

    }
}
