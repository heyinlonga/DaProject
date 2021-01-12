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

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import software.ecenter.study.Adapter.ChapterListAdapter;
import software.ecenter.study.R;
import software.ecenter.study.View.ExpandableTextView;
import software.ecenter.study.View.treelist.Node;
import software.ecenter.study.bean.HomeBean.ChapterItemBean;
import software.ecenter.study.bean.TeacherResDetailBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;

public class TeacherResDetailActivity extends BaseActivity {


    List<ChapterItemBean> ChapterItemBeanList;
    ChapterListAdapter mChapterListAdapter;

    @BindView(R.id.btn_left_title)
    ImageView btnLeftTitle;
    @BindView(R.id.title_content)
    TextView titleContent;
    @BindView(R.id.btn_refresh_net)
    LinearLayout btnRefreshNet;
    @BindView(R.id.img_book)
    ImageView imgBook;
    @BindView(R.id.text_book_name)
    TextView textBookName;
    @BindView(R.id.tip_temp)
    RelativeLayout tipTemp;
    @BindView(R.id.id_source_textview)
    TextView idSourceTextview;
    @BindView(R.id.id_expand_textview)
    TextView idExpandTextview;
    @BindView(R.id.expandable_text)
    ExpandableTextView expandableText;
    @BindView(R.id.list_chapter)
    RecyclerView listChapter;

    private boolean isShowCategory = true;
    private String youhuijia;

    private String id;

    private TeacherResDetailBean teacherResDetailBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_res_detail);
        ButterKnife.bind(this);


        listChapter.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });


        id = getIntent().getStringExtra("id");
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Util.isOnMainThread()) {
            Glide.get(this).clearMemory();
        }
    }

    /**
     * 书的详情
     */
    public void getData(boolean isShow) {
        if (isShow) {
            if (!showNetWaitLoding()) {
                return;
            }
        }

        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getPackageDetail(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        btnRefreshNet.setVisibility(View.GONE);
                        final TeacherResDetailBean bean = ParseUtil.parseBean(response, TeacherResDetailBean.class);
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


    public void setResponseView(TeacherResDetailBean bean) {
        teacherResDetailBean = bean;
        if (Util.isOnMainThread()) {
            if (!this.isFinishing())
                Glide.with(mContext).load(teacherResDetailBean.getData().getImgUrl()).into(imgBook);
        }

        textBookName.setText(bean.getData().getName());
        titleContent.setText(bean.getData().getName());
        expandableText = (ExpandableTextView) findViewById(R.id.expandable_text);
        expandableText.setText(bean.getData().getDescription());

        setChapterList(bean.getData().getCategoryList());


    }


    /**
     * 设置目录
     *
     * @param bean 图书实体类
     */
    private void setChapterList(List<ChapterItemBean> bean) {
        ChapterItemBeanList = bean;
        //第一个参数  RecyclerView
        //第二个参数  上下文
        //第三个参数  数据集
        //第四个参数  默认展开层级数 0为不展开
        //第五个参数  展开的图标
        //第六个参数  闭合的图标
        if (ChapterItemBeanList != null) {
            try {
                mChapterListAdapter = new ChapterListAdapter(listChapter, this,
                        ChapterItemBeanList, 0);

                mChapterListAdapter.setOnTreeNodeClickListener(new ChapterListAdapter.OnTreeNodeClickListener() {
                    @Override
                    public void onClick(Node node, int position, boolean isJump) {
                        ChapterItemBean chapterItemBean = null;
                        for (ChapterItemBean chapterItemBean1 : ChapterItemBeanList) {
                            if (chapterItemBean1.getChapterId().equals(node.getId() + "")) {
                                chapterItemBean = chapterItemBean1;
                            }
                        }
                        if (chapterItemBean == null) {
                            ToastUtils.showToastSHORT(mContext, "该目录下暂无资源！");
                            return;
                        }
                        if (chapterItemBean.getIsExpand() == 1) {
                            mChapterListAdapter.expandOrCollapse(position);
                            return;
                        }

                        if (chapterItemBean.getIsExpand() == 0) {
                            if (chapterItemBean.getResourceCount() > 0) {
                                Intent intent = new Intent(mContext, TeacherResChapterDetailsActivity.class);
                                intent.putExtra("chapterId", "" + node.getId());
                                intent.putExtra("chapterName", "" + node.getName());
                                startActivity(intent);
                                return;
                            } else {
                                ToastUtils.showToastSHORT(mContext, "该目录下暂无资源！");
                                return;
                            }

                        }
                        if (node.isLeaf() || isJump) {
                     /*   Toast.makeText(getApplicationContext(), node.getName(),
                                Toast.LENGTH_SHORT).show();*/

                            if (chapterItemBean.getResourceCount() > 0) {
                                Intent intent = new Intent(mContext, TeacherResChapterDetailsActivity.class);
                                intent.putExtra("chapterId", "" + node.getId());
                                intent.putExtra("chapterName", "" + node.getName());
                                startActivity(intent);
                            } else if (chapterItemBean.getIsExpand() == 0) {
                                ToastUtils.showToastSHORT(mContext, "该目录下暂无资源！");
                            }
                        }

                    }

                });

                listChapter.setAdapter(mChapterListAdapter);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    @OnClick({R.id.btn_left_title, R.id.btn_refresh_net})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left_title:
                onBackPressed();
                break;
            case R.id.btn_refresh_net:
                getData(true);
                break;

        }
    }

}
