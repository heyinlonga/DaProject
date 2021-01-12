package software.ecenter.study.activity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import software.ecenter.study.Adapter.ExerciseExerciseAdapter;
import software.ecenter.study.R;
import software.ecenter.study.bean.HomeBean.ExerciseBean;
import software.ecenter.study.bean.HomeBean.ExerciseDetailBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;

/***
 * dec 习题查看
 * */
public class ExerciseLookActivity extends BaseActivity {

    @BindView(R.id.btn_left_title)
    ImageView btnLeftTitle;
    @BindView(R.id.resoure_name)
    TextView resoureName;
    @BindView(R.id.list_exercise_exercise)
    RecyclerView listExerciseExercise;

    private ExerciseExerciseAdapter exerciseExerciseAdapter;
    private List<ExerciseBean> listData = new ArrayList<>();

    private ExerciseDetailBean mExerciseDetailBean;
    private String  exerciseId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_look);
        ButterKnife.bind(this);

        LinearLayoutManager linearLayoutManagerOne = new LinearLayoutManager(mContext);
        linearLayoutManagerOne.setOrientation(LinearLayoutManager.VERTICAL);
        listExerciseExercise.setLayoutManager(linearLayoutManagerOne);

        //解决ScrollView中嵌入一个或多个RecyclerView页面滑动卡顿
        listExerciseExercise.setHasFixedSize(true);
        listExerciseExercise.setNestedScrollingEnabled(false);

        exerciseId = getIntent().getStringExtra("exerciseId");

        showExercise();
    }


    public void showExercise(){

        if (!showNetWaitLoding()) {
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("exerciseId", exerciseId);
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).showExercise(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();

                        ExerciseDetailBean bean = ParseUtil.parseBean(response, ExerciseDetailBean.class);
                        setResponse(bean);
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();

                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });

    }

    public void setResponse(ExerciseDetailBean bean) {
        mExerciseDetailBean = bean;

        listData = mExerciseDetailBean.getData().getExerciseList();
        exerciseExerciseAdapter = new ExerciseExerciseAdapter(mContext, listData,false);
        listExerciseExercise.setAdapter(exerciseExerciseAdapter);

    }

    @OnClick(R.id.btn_left_title)
    public void onViewClicked() {
        onBackPressed();
    }
}
