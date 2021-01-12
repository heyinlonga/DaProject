package software.ecenter.study.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import software.ecenter.study.Interface.OnClickItemListener;
import software.ecenter.study.R;
import software.ecenter.study.bean.MatchList;
import software.ecenter.study.utils.TimeUtil;
import software.ecenter.study.utils.ToolUtil;

/**
 * Message  比赛
 * Create by Administrator
 * Create by 2019/10/28
 */
public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.ViewHolder> {
    private List<MatchList> mList = new ArrayList<>();
    private Context mContext;
    private OnClickItemListener listener;
    private int type = 1;

    public MatchAdapter(Context mContext, int type, OnClickItemListener listener) {
        this.mContext = mContext;
        this.type = type;
        this.listener = listener;
    }

    //获取数据
    public MatchList getChoseData(int pos) {
        if (ToolUtil.isList(mList) && pos < mList.size()) {
            return mList.get(pos);
        }
        return new MatchList();
    }

    //设置数据
    public void setData(List<MatchList> list) {
        mList.clear();
        if (ToolUtil.isList(list)) {
            mList.addAll(list);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_macth, viewGroup, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {
        MatchList bean = mList.get(i);
        if (bean != null) {
            holder.tv_name.setText(ToolUtil.getString(bean.getName()));
            if (ToolUtil.getString(bean.getMatchBeginDate()).equals(ToolUtil.getString(bean.getMatchEndDate()))) {
                holder.tv_time.setText(ToolUtil.getString(bean.getMatchBeginDate()));
            } else {
                holder.tv_time.setText(ToolUtil.getString(bean.getMatchBeginDate()) + "至" + ToolUtil.getString(bean.getMatchEndDate()));
            }
            holder.ll_enrollTime.setVisibility(View.GONE);
            holder.tv_bao.setVisibility(View.GONE);
            holder.tv_ckbd.setVisibility(View.GONE);
            holder.tv_wdcj.setVisibility(View.GONE);
            if (type == 1) {//正在进行
                holder.tv_time.setTextColor(mContext.getResources().getColor(R.color.color_C9AB79));
            } else if (type == 2) {//即将开始
                holder.tv_time.setTextColor(mContext.getResources().getColor(R.color.color_6a3906));
                holder.ll_enrollTime.setVisibility(View.VISIBLE);
                holder.tv_enrollTime.setText(ToolUtil.getString(bean.getEnrollEndDate()));
                holder.tv_bao.setText(bean.isEnroll() ? "已报名" : "报名");
                //是否报名截止时间
                long matchCha = TimeUtil.getTimeCha(TimeUtil.getTime(TimeUtil.NORMAL_PATTERN), bean.getEnrollEndDate() + " 00:00:00");
                holder.tv_bao.setVisibility(bean.isEndEnroll() ? View.GONE : View.VISIBLE);
            } else if (type == 3) {//往期比赛
                holder.tv_time.setTextColor(mContext.getResources().getColor(R.color.color_C9AB79));
                holder.tv_wdcj.setVisibility(bean.isEnroll() && bean.isMatch() ? View.VISIBLE : View.GONE);
                holder.tv_ckbd.setVisibility(View.VISIBLE);
            }
            //报名
            holder.tv_bao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onIndexClick(1, i);
                    }
                }
            });
            //我的成绩
            holder.tv_wdcj.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onIndexClick(2, i);
                    }
                }
            });
            //查看榜单
            holder.tv_ckbd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onIndexClick(3, i);
                    }
                }
            });
            //详情
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(type, i);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_name;
        private final LinearLayout ll_enrollTime;
        private final TextView tv_enrollTime;
        private final TextView tv_time;
        private final TextView tv_wdcj;
        private final TextView tv_ckbd;
        private final TextView tv_bao;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            ll_enrollTime = itemView.findViewById(R.id.ll_enrollTime);
            tv_enrollTime = itemView.findViewById(R.id.tv_enrollTime);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_wdcj = itemView.findViewById(R.id.tv_wdcj);
            tv_ckbd = itemView.findViewById(R.id.tv_ckbd);
            tv_bao = itemView.findViewById(R.id.tv_bao);
        }
    }
}
