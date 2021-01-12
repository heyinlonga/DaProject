package software.ecenter.study.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import software.ecenter.study.R;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.EditUtils;
import software.ecenter.study.utils.ToastUtils;
import software.ecenter.study.utils.ToolUtil;

/**
 * 修改昵称跟学校
 */
public class UserInfoChangeActivity extends BaseActivity {

    @BindView(R.id.btn_left_title_text)
    TextView btnLeftTitleText;
    @BindView(R.id.title_content)
    TextView titleContent;
    @BindView(R.id.btn_right_title_text)
    TextView btnRightTitleText;
    @BindView(R.id.title_temp)
    RelativeLayout titleTemp;
    @BindView(R.id.userinfo_changge_edit)
    EditText userinfoChanggeEdit;

    private String nickName;
    private String school;
    private int mModifyType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_change);
        ButterKnife.bind(this);
        nickName = getIntent().getStringExtra("nickName");
        school = getIntent().getStringExtra("school");
        mModifyType = getIntent().getIntExtra("mModifyType", 1);
        if (mModifyType == 1) {
            titleContent.setText("修改昵称");
            userinfoChanggeEdit.setHint("输入修改的内容（10字以内）");
            userinfoChanggeEdit.setText(ToolUtil.getString(nickName));
        } else if (mModifyType == 5) {
            titleContent.setText("修改学校");
            userinfoChanggeEdit.setText(ToolUtil.getString(school));
        }
        EditUtils.showEditNoEmoji(userinfoChanggeEdit);
        userinfoChanggeEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String ss = s.toString();
                if (mModifyType == 1&&ss.length() >10) {//修改昵称
                    titleContent.setText("修改昵称");
                    userinfoChanggeEdit.setText(ss.substring(0,10));
                    userinfoChanggeEdit.setSelection(ss.substring(0,10).length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    //入参类型（ 1、昵称 2、年级 3、地区 4、身份 5、学校 6、头像 ） 注：（后台根据modifyType 取对应的字段，以下是否可空也与此相关）
    public void updateUserInfo(int modifyType, String nickname, String grade, String addressProvince, String addressCity, String role, String school, String headImage) {
        if (!showNetWaitLoding()) {
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            //接口入参，如果只有一个参数用键值对，多个参数用json
            Map<String, String> map = new HashMap<>();
            jsonObject.put("modifyType", "" + modifyType);
            switch (modifyType) {
                    case 1:
                    jsonObject.put("nickname", nickname);
                    break;
                case 2:
                    jsonObject.put("grade", grade);
                    break;
                case 3:
                    jsonObject.put("addressProvince", addressProvince);
                    jsonObject.put("addressCity", addressCity);
                    break;
                case 4:
                    jsonObject.put("role", role);
                    break;
                case 5:
                    jsonObject.put("school", school);
                    break;
                case 6:
                    jsonObject.put("headImageUrl", headImage);
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).updateUserInfo(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, "修改成功");

                        Intent intent = new Intent();
                        intent.putExtra("result", userinfoChanggeEdit.getText().toString());
                        setResult(Activity.RESULT_OK, intent);
                        finish();

                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });


    }


    @OnClick({R.id.btn_left_title_text, R.id.btn_right_title_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left_title_text:
                onBackPressed();
                break;
            case R.id.btn_right_title_text:
                String s = userinfoChanggeEdit.getText().toString();
                if (TextUtils.isEmpty(s)) {
                    ToastUtils.showToastSHORT(mContext, "请输入内容");
                    return;
                }
                if (!ToolUtil.isKongGe(s)) {
                    ToastUtils.showToastSHORT(mContext, mModifyType== 1 ?"请输入昵称":"请输入学校名称");
                    return;
                }
                if (mModifyType == 1) {
                    updateUserInfo(mModifyType, userinfoChanggeEdit.getText().toString(), "", "", "", "", "", "");
                } else if (mModifyType == 5) { //学校
                    updateUserInfo(mModifyType, "", "", "", "", "", userinfoChanggeEdit.getText().toString(), "");
                }

                break;
        }
    }
}
