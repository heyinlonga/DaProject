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
import software.ecenter.study.Adapter.SetExerciseAdapter;
import software.ecenter.study.R;
import software.ecenter.study.bean.HomeBean.ExerciseBean;
import software.ecenter.study.bean.HomeBean.ResourceBean;
import software.ecenter.study.bean.MineBean.MyExerciseDetailBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;

/**
 * 我的题集
 */
public class SetExerciseActivity extends BaseActivity {

    @BindView(R.id.btn_left_title)
    ImageView btnLeftTitle;
    @BindView(R.id.btn_left_title_text)
    TextView btnLeftTitleText;
    @BindView(R.id.title_content)
    TextView titleContent;
    @BindView(R.id.tip_text_one)
    TextView tipTextone;
    @BindView(R.id.tip_text_two)
    TextView tipTextTwo;
    @BindView(R.id.btn_right_title_text)
    TextView btnRightTitleText;
    @BindView(R.id.title_temp)
    RelativeLayout titleTemp;
    @BindView(R.id.tip_one)
    View tipOne;
    @BindView(R.id.btn_one)
    RelativeLayout btnOne;
    @BindView(R.id.tip_two)
    View tipTwo;
    @BindView(R.id.btn_two)
    RelativeLayout btnTwo;
    @BindView(R.id.tab_line)
    LinearLayout tabLine;
    @BindView(R.id.list_kecheng)
    RecyclerView listKecheng;
    @BindView(R.id.btn_down)
    Button btnDown;
    @BindView(R.id.bottom_line)
    LinearLayout bottomLine;
    @BindView(R.id.ll_search_all_no_data)
    LinearLayout llSearchAllNoData;
    private List<ExerciseBean> ListData = new ArrayList<>();
    private SetExerciseAdapter mAdapter;

    private MyExerciseDetailBean mMyExerciseDetailBean;
    private int exerciseType = 1;

    private List<ExerciseBean> mListCheckData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_exercise);
        ButterKnife.bind(this);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        listKecheng.setLayoutManager(manager);

        getExerciseList();
    }


    public void getExerciseList() {
        if (!showNetWaitLoding()) {
            return;
        }

        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        map.put("exerciseType", "" + exerciseType);
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getExerciseList(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        mMyExerciseDetailBean = ParseUtil.parseBean(response, MyExerciseDetailBean.class);
                        initView();

                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });


    }


    public void initView() {
        if (mMyExerciseDetailBean == null) {
            if (ListData != null) {
                ListData.clear();
                if (mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }
                if (ListData.size() <= 0) {
                    llSearchAllNoData.setVisibility(View.VISIBLE);
                    listKecheng.setVisibility(View.GONE);
                } else {
                    llSearchAllNoData.setVisibility(View.GONE);
                    listKecheng.setVisibility(View.VISIBLE);
                }
            }
            return;
        }
        //mAdapter 为null的话，说明第一次加载，不用操作
        if (mAdapter != null) {
            //切换之前，先退出选择模式
            OutCheckMode();
        }
        ListData = mMyExerciseDetailBean.getData().getData();
        if (exerciseType == 1) {
            tipOne.setBackgroundColor(getResources().getColor(R.color.color_F77450));
            tipTextone.setTextColor(getResources().getColor(R.color.color_F77450));
            tipTwo.setBackgroundColor(getResources().getColor(R.color.white));
            tipTextTwo.setTextColor(getResources().getColor(R.color.textHoldColor));
        } else if (exerciseType == 0) {
            tipOne.setBackgroundColor(getResources().getColor(R.color.white));
            tipTextone.setTextColor(getResources().getColor(R.color.textHoldColor));
            tipTwo.setBackgroundColor(getResources().getColor(R.color.color_F77450));
            tipTextTwo.setTextColor(getResources().getColor(R.color.color_F77450));
        }


        if (mAdapter == null) {
            mAdapter = new SetExerciseAdapter(mContext, ListData);
            mAdapter.setItemClickListener(new SetExerciseAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, boolean isJump) {
//                    if (ListData.get(position).isCheckMode()) {
//                        CheckItem(position);
//                    } else {
//                        Intent intent = new Intent(mContext, ExerciseLookActivity.class);
//                        intent.putExtra("exerciseId", ListData.get(position).getExerciseId());
//                        startActivity(intent);
//                    }
                    if (isJump) {
                        boolean hasCheck = false;
                        for (ExerciseBean item : ListData) {
                            if (item.isCheck()) {
                                hasCheck = true;
                                break;
                            }
                        }
                        if (hasCheck) return;
                        Intent intent = new Intent(mContext, ExerciseLookActivity.class);
                        intent.putExtra("exerciseId", ListData.get(position).getExerciseId());
                        startActivity(intent);
                    } else if (ListData.get(position).isCheckMode()) {
                        CheckItem(position);
                    }
                }


            });
            mAdapter.setOnLongClickListener(new SetExerciseAdapter.OnLongClickListener() {
                @Override
                public void onLongClick(int position) {
                    if (!ListData.get(position).isCheckMode()) {
                        toCheckMode();
                    }
                }
            });

            listKecheng.setAdapter(mAdapter);
        } else {
            mAdapter.refreshData(ListData);
        }
        if (ListData.size() <= 0) {
            llSearchAllNoData.setVisibility(View.VISIBLE);
            listKecheng.setVisibility(View.GONE);
        } else {
            llSearchAllNoData.setVisibility(View.GONE);
            listKecheng.setVisibility(View.VISIBLE);
        }
        toCheckMode();

    }

    public void toCheckMode() {
        //全部设置可选模式
        for (ExerciseBean item : ListData) {
            item.setCheck(false);//全部置为未选中
            item.setCheckMode(true);
        }
//        btnLeftTitle.setVisibility(View.INVISIBLE);
//        btnLeftTitleText.setVisibility(View.VISIBLE);
//        btnRightTitleText.setVisibility(View.VISIBLE);
        mAdapter.refreshData(ListData);
    }

    public void OutCheckMode() {
        //全部设置可选模式
//        for (ExerciseBean item : ListData) {
//            item.setCheckMode(false);
//        }
        for (ExerciseBean item : ListData) {
            item.setCheck(false);
        }
        btnLeftTitle.setVisibility(View.VISIBLE);
        btnLeftTitleText.setVisibility(View.INVISIBLE);
        btnRightTitleText.setVisibility(View.INVISIBLE);
        mAdapter.refreshData(ListData);
        bottomLine.setVisibility(View.GONE);
    }


    public void CheckItem(int position) {
        ListData.get(position).setCheck(ListData.get(position).isCheck() ? false : true);
        //遍历是否有被选中的
        boolean hasCheck = false;
        for (ExerciseBean item : ListData) {
            if (item.isCheck()) {
                hasCheck = true;
                break;
            }
        }
        if (hasCheck) {
            bottomLine.setVisibility(View.VISIBLE);
            btnLeftTitle.setVisibility(View.INVISIBLE);
            btnLeftTitleText.setVisibility(View.VISIBLE);
            btnRightTitleText.setVisibility(View.VISIBLE);
        } else {
            bottomLine.setVisibility(View.GONE);
            btnLeftTitle.setVisibility(View.VISIBLE);
            btnLeftTitleText.setVisibility(View.INVISIBLE);
            btnRightTitleText.setVisibility(View.INVISIBLE);
        }
        mAdapter.refreshData(ListData);
    }

    public void checkAllItem(boolean isCheck) {
        for (ExerciseBean item : ListData) {
            item.setCheck(isCheck);
        }
        //全部没选中时
        if (!isCheck) {
            btnLeftTitle.setVisibility(View.VISIBLE);
            btnLeftTitleText.setVisibility(View.INVISIBLE);
            btnRightTitleText.setVisibility(View.INVISIBLE);
        }
        mAdapter.refreshData(ListData);
    }

    @OnClick({R.id.btn_left_title, R.id.btn_left_title_text, R.id.btn_right_title_text, R.id.btn_one, R.id.btn_two, R.id.btn_down})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left_title:
                finish();
                break;
            case R.id.btn_left_title_text:
                checkAllItem(true);
                break;
            case R.id.btn_right_title_text:
                OutCheckMode();
            case R.id.btn_one:
                exerciseType = 1;
                getExerciseList();
                if (exerciseType == 1) {
                    tipOne.setBackgroundColor(getResources().getColor(R.color.color_F77450));
                    tipTextone.setTextColor(getResources().getColor(R.color.color_F77450));
                    tipTwo.setBackgroundColor(getResources().getColor(R.color.white));
                    tipTextTwo.setTextColor(getResources().getColor(R.color.textHoldColor));
                } else if (exerciseType == 0) {
                    tipOne.setBackgroundColor(getResources().getColor(R.color.white));
                    tipTextone.setTextColor(getResources().getColor(R.color.textHoldColor));
                    tipTwo.setBackgroundColor(getResources().getColor(R.color.color_F77450));
                    tipTextTwo.setTextColor(getResources().getColor(R.color.color_F77450));
                }

                break;
            case R.id.btn_two:
                exerciseType = 0;
                getExerciseList();
                if (exerciseType == 1) {
                    tipOne.setBackgroundColor(getResources().getColor(R.color.color_F77450));
                    tipTextone.setTextColor(getResources().getColor(R.color.color_F77450));
                    tipTwo.setBackgroundColor(getResources().getColor(R.color.white));
                    tipTextTwo.setTextColor(getResources().getColor(R.color.textHoldColor));
                } else if (exerciseType == 0) {
                    tipOne.setBackgroundColor(getResources().getColor(R.color.white));
                    tipTextone.setTextColor(getResources().getColor(R.color.textHoldColor));
                    tipTwo.setBackgroundColor(getResources().getColor(R.color.color_F77450));
                    tipTextTwo.setTextColor(getResources().getColor(R.color.color_F77450));
                }

                break;
            case R.id.btn_down:

                deleteExercise();
                break;
        }
    }

    public void deleteExercise() {
        if (!showNetWaitLoding()) {
            return;
        }

        mListCheckData = new ArrayList<>();
        String Ids = "";
        for (ExerciseBean item : ListData) {
            if (item.isCheck()) {
                Ids += item.getExerciseId() + ",";
                mListCheckData.add(item);
            }
        }

        if (mListCheckData.size() == 0) {
            ToastUtils.showToastSHORT(mContext, "请选择");
        }
        if (Ids.endsWith(",")) {
            Ids = Ids.substring(0, Ids.length() - 1);
        }

        JSONObject json = new JSONObject();
        try {
            json.put("exerciseIds", Ids);
            json.put("exerciseType", "" + exerciseType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).deleteExercise(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        for (ExerciseBean item : mListCheckData) {
                            ListData.remove(item);
                        }
                        OutCheckMode();
                        ToastUtils.showToastLONG(mContext, "删除成功");
                        getExerciseList();
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });


    }

}
