package software.ecenter.study.activity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import software.ecenter.study.Adapter.ExerciseExerciseAdapter;
import software.ecenter.study.R;
import software.ecenter.study.bean.HomeBean.ExerciseBean;
import software.ecenter.study.bean.HomeBean.ExerciseDetailBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.ToastUtils;

/***
 * dsc 练习解答
 *
 * */
public class ExerciseExerciseActivity extends BaseActivity {


    @BindView(R.id.btn_left_title)
    ImageView btnLeftTitle;
    @BindView(R.id.btn_right_title)
    Button btnRightTitle;
    @BindView(R.id.list_exercise_exercise)
    RecyclerView listExerciseExercise;
    @BindView(R.id.resoure_name)
    TextView resoureName;

    private ExerciseExerciseAdapter exerciseExerciseAdapter;
    private List<ExerciseBean> listData = new ArrayList<>();

    private ExerciseDetailBean mExerciseDetailBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_exercise);
        ButterKnife.bind(this);
        mExerciseDetailBean = (ExerciseDetailBean) getIntent().getSerializableExtra("ExerciseDetailBean");
        if (mExerciseDetailBean != null) {
            initView();
        }
    }

    public void initView() {

        resoureName.setText(mExerciseDetailBean.getData().getResourceName());
        listData = mExerciseDetailBean.getData().getExerciseList();
        exerciseExerciseAdapter = new ExerciseExerciseAdapter(mContext, listData, true);
        exerciseExerciseAdapter.setmItemBtnAddClickListener(new ExerciseExerciseAdapter.OnItemBtnAddClickListener() {
            @Override
            public void onItemBtnAddClick(int position) {

                submitExercise(listData.get(position).getExerciseId(), listData.get(position).getYourAnswer(), listData.get(position).getType());
            }
        });
        listExerciseExercise.setLayoutManager(new LinearLayoutManager(mContext));
        listExerciseExercise.setAdapter(exerciseExerciseAdapter);
    }

    //加入题集
    public void submitExercise(String exerciseId, String yourAnswer, int type) {

        if (!showNetWaitLoding()) {
            return;
        }

        //接口入参，如果只有一个参数用键值对，多个参数用json
        JSONObject json = new JSONObject();
        try {
            json.put("exerciseId", exerciseId);
            json.put("yourAnswer", yourAnswer);
            json.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).submitExercise(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();

                        ToastUtils.showToastSHORT(mContext, "加入成功");
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();

                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });

    }

    @OnClick({R.id.btn_left_title, R.id.btn_right_title})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left_title:
                finish();
                break;
            case R.id.btn_right_title:
                startActivity(SetExerciseActivity.class);
                break;
        }
    }


}
