package software.ecenter.study.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import software.ecenter.study.Adapter.ResourceByCategoryAdapter;
import software.ecenter.study.R;
import software.ecenter.study.bean.HomeBean.TeacherResChapterBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;

/***
 * dsc 资源详情
 *
 * */
public class TeacherResChapterDetailsActivity extends BaseActivity {

    @BindView(R.id.btn_left_title)
    ImageView btnLeftTitle;
    @BindView(R.id.title_content)
    TextView titleContent;
    @BindView(R.id.title_temp)
    RelativeLayout titleTemp;
    @BindView(R.id.list_kecheng)
    RecyclerView listKecheng;
    @BindView(R.id.btn_refresh_net)
    LinearLayout btnRefreshNet;
    @BindView(R.id.ll_search_all_no_data)
    LinearLayout llSearchAllNoData;

    private List<TeacherResChapterBean.DataBean> ResourceData = new ArrayList<>();
    private ResourceByCategoryAdapter resourceByCategoryAdapter;

    private String chapterId;
    private String chapterName;
    private TeacherResChapterBean mChapterDetailBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_res_chapter_details);
        ButterKnife.bind(this);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        listKecheng.setLayoutManager(manager);
        chapterId = getIntent().getStringExtra("chapterId");
        chapterName = getIntent().getStringExtra("chapterName");
        titleContent.setText(chapterName);
        getData();
    }

    private void getData() {
        if (!showNetWaitLoding()) {
            return;
        }
        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        map.put("id", chapterId);
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getResourceByCategory(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        btnRefreshNet.setVisibility(View.GONE);
                        final TeacherResChapterBean bean = ParseUtil.parseBean(response, TeacherResChapterBean.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setResponseView(bean);
                            }
                        });
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        btnRefreshNet.setVisibility(View.VISIBLE);
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }

    String types = "mp3,mp4,pdf,img";

    private void setResponseView(TeacherResChapterBean bean) {
        mChapterDetailBean = bean;
        ResourceData = bean.getData();
        resourceByCategoryAdapter = new ResourceByCategoryAdapter(mContext, ResourceData);
        resourceByCategoryAdapter.setItemClickListener(new ResourceByCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, boolean isJump) {

                String type = ResourceData.get(position).getType().toLowerCase();
                Intent intent = new Intent(mContext, SeeTeacherResourcesVideoActivity.class);
                intent.putExtra("resourceId", ResourceData.get(position).getId());
                intent.putExtra("title", ResourceData.get(position).getName());
                startActivity(intent);
//                if (types.contains(type.toLowerCase())) {
//                    Intent intent = new Intent(mContext, SeeTeacherResourcesVideoActivity.class);
//                    intent.putExtra("resourceId", ResourceData.get(position).getId());
//                    intent.putExtra("title", ResourceData.get(position).getName());
//                    startActivity(intent);
//                } else {
//                    ToastUtils.showToastSHORT(mContext, "此资源不支持在线观看，请前往PC端查看或下载");
//                }

            }

        });
        listKecheng.setAdapter(resourceByCategoryAdapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @OnClick({R.id.btn_left_title, R.id.btn_refresh_net})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left_title:
                onBackPressed();
                break;
            case R.id.btn_refresh_net:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
