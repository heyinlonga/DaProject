package software.ecenter.study.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import software.ecenter.study.R;
import software.ecenter.study.bean.MineBean.ImageLookBean;
import software.ecenter.study.bean.MineBean.QusetionLookBean;


/**
 * Created by Mike on 2018/4/25.
 */
@SuppressLint("SetTextI18n")
public class QuestionDetailAdapter extends RecyclerView.Adapter<QuestionDetailAdapter.ViewHolder> implements View.OnClickListener {
    private List<QusetionLookBean> DataList;
    private Context mContext;
    private OnItemClickListener mItemClickListener;
    private OnItemBtnAddClickListener mItemBtnAddClickListener;


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView questionDetailTitle;
        RelativeLayout questionDetailAudio;
        TextView quetionRole;
        RecyclerView recyclerView;
        TextView play;
        private final ImageLookAdapter imageLookAdapter;


        public ViewHolder(View itemView) {
            super(itemView);
            questionDetailTitle = itemView.findViewById(R.id.question_detail_miaoshu);
            questionDetailAudio = itemView.findViewById(R.id.btn_question_detail_miaoshu);
            quetionRole = itemView.findViewById(R.id.question_detail_type);
            recyclerView = itemView.findViewById(R.id.recyclerView_image);
            play = itemView.findViewById(R.id.lv_text);
            imageLookAdapter = new ImageLookAdapter(mContext);
            recyclerView.setLayoutManager(new GridLayoutManager(itemView.getContext(), 4) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
            recyclerView.setAdapter(imageLookAdapter);
        }
    }

    /**
     * 用于把要展示的数据源传进来，并赋值给一个全局变量mFruitList，我们后续的操作都将在这个数据源的基础上进行。
     *
     * @param DataList
     */
    public QuestionDetailAdapter(Context mContext, List<QusetionLookBean> DataList) {
        this.DataList = DataList;
        this.mContext = mContext;

    }

    public void refreshData(List<QusetionLookBean> DataList) {
        this.DataList = DataList;
        notifyDataSetChanged();

    }

    /**
     * 用于创建ViewHolder实例的，并把加载出来的布局传入到构造函数当中，最后将viewholder的实例返回
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_question_detail_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    /**
     * 用于对RecyclerView子项的数据进行赋值的，会在每个子项被滚动到屏幕内的时候执行，这里我们通过
     * position参数得到当前项的Fruit实例，然后再将数据设置到ViewHolder的Imageview和textview当中即可，
     *
     * @param holder
     * @param position
     */

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.itemView.setTag(position);
        final QusetionLookBean item = DataList.get(position);
        if (item != null) {
            holder.questionDetailTitle.setText(item.getQuestionText());
            List<ImageLookBean> questionImageList = item.getQuestionImageList();
            holder.imageLookAdapter.refreshData(questionImageList);
            if (questionImageList != null && questionImageList.size() > 0) {
                holder.recyclerView.setVisibility(View.VISIBLE);
            } else {
                holder.recyclerView.setVisibility(View.GONE);
            }
            if (TextUtils.isEmpty(item.getQuestionAudio())) {
                holder.questionDetailAudio.setVisibility(View.GONE);
            } else {
                holder.questionDetailAudio.setVisibility(View.VISIBLE);
            }
            switch (item.getQuestionRole()) {
                case 1:
                    holder.quetionRole.setText("问题描述");
                    holder.quetionRole.setBackgroundResource(R.drawable.biaoqian1);
                    break;
                case 2:
                    holder.quetionRole.setText("我的追问");
                    holder.quetionRole.setBackgroundResource(R.drawable.biaoqian1);

                    break;
                case 3:
                    holder.quetionRole.setText("教师回答");
                    holder.quetionRole.setBackgroundResource(R.drawable.biaoqian2);
                    break;
            }
            holder.questionDetailAudio.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (mItemBtnAddClickListener != null) {
                        mItemBtnAddClickListener.onItemBtnAddClick(v, position, item.getQuestionAudio());
                    }
                }
            });
            if (item.isPlaying()) {
                holder.play.setText("正在播放(" + item.getCurDuration() + "s)");
            } else {
                holder.play.setText("点击播放(" + item.getAudioDuration() + "s)");
            }
        }
    }


    @Override
    public int getItemCount() {
        return DataList.size();
    }

    @Override
    public void onClick(View v) {
        if (mItemClickListener != null) {
            mItemClickListener.onItemClick((Integer) v.getTag());
        }
    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnItemBtnAddClickListener {
        void onItemBtnAddClick(View v, int position, String url);
    }

    public void setmItemBtnAddClickListener(OnItemBtnAddClickListener mItemBtnAddClickListener) {
        this.mItemBtnAddClickListener = mItemBtnAddClickListener;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

}
