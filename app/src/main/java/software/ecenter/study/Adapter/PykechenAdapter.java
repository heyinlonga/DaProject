package software.ecenter.study.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import software.ecenter.study.Interface.OnClickItemListener;
import software.ecenter.study.R;
import software.ecenter.study.bean.CommType;
import software.ecenter.study.bean.PinLishiBean;
import software.ecenter.study.utils.ToolUtil;

/**
 * Message 拼音拼读提升课
 * Create by Administrator
 * Create by 2019/11/8
 */
public class PykechenAdapter extends RecyclerView.Adapter<PykechenAdapter.Holder> {
    private Context mContext;
    private OnClickItemListener listener;
    private List<PinLishiBean.DataBean.AudioListBean> mList = new ArrayList<>();
    public PykechenAdapter(Context mContext, OnClickItemListener listener) {
        this.mContext = mContext;
        this.listener = listener;
    }
    public String getPath(int poc) {
        if (ToolUtil.isList(mList) && poc < mList.size()) {
            return mList.get(poc).getAudioUrl();
        }
        return "";
    }
    //获取平均分
    public int getScore() {
        if (ToolUtil.isList(mList)) {
            int num = 0;
            for (int i = 0; i < mList.size(); i++) {
                String score = mList.get(i).getScore();
                num += ToolUtil.getIntValue(score);
            }
            return (int)(num*1f / mList.size()+0.5);
        }
        return 0;
    }
    public void setData(List<PinLishiBean.DataBean.AudioListBean> list) {
        mList.clear();
        if (ToolUtil.isList(list)) {
            mList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void setPlay(int poc, boolean isPlay) {
        if (ToolUtil.isList(mList) && poc < mList.size()) {
            for (int i = 0; i < mList.size(); i++) {
                mList.get(i).setSelect(isPlay && (i == poc));
            }
            notifyDataSetChanged();
        }
    }
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.itme_pykechen, viewGroup, false);
        return new Holder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int i) {
        PinLishiBean.DataBean.AudioListBean bean = mList.get(i);
        if (bean!=null){
            holder.iv_play.setSelected(bean.isSelect());
            holder.tv_title.setText(ToolUtil.getString(bean.getTitle()));
            holder.tv_time.setText(bean.getDuration()+"秒");
            holder.tv_score.setText(bean.getScore());
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(0, i);
                }
            }
        });
        holder.iv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(1, i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class Holder extends RecyclerView.ViewHolder{

        private final ImageView iv_play;
        private final TextView tv_title;
        private final TextView tv_time;
        private final TextView tv_score;

        public Holder(@NonNull View itemView) {
            super(itemView);
            iv_play = itemView.findViewById(R.id.iv_play);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_score = itemView.findViewById(R.id.tv_score);
        }
    }
}
