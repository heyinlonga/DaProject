package software.ecenter.study.Adapter;

import android.content.Context;
import android.graphics.Typeface;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import software.ecenter.study.R;
import software.ecenter.study.bean.MatchRankList;
import software.ecenter.study.utils.TimeUtil;
import software.ecenter.study.utils.ToolUtil;

/**
 * Message 状元榜
 * Create by Administrator
 * Create by 2019/10/30
 */
public class MatchRankAdapter extends RecyclerView.Adapter<MatchRankAdapter.ViewHolder> {
    private Context mContext;
    private List<MatchRankList.DataBean.ContentBean> mList = new ArrayList<>();

    public MatchRankAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<MatchRankList.DataBean.ContentBean> list) {
        mList.clear();
        if (ToolUtil.isList(list)) {
            mList.addAll(list);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_rank, viewGroup, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        MatchRankList.DataBean.ContentBean bean = mList.get(i);
        if (bean != null) {
            holder.iv_bg.setVisibility(View.GONE);
            holder.iv_type.setVisibility(View.INVISIBLE);
            holder.tv_pai.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            holder.tv_name.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            holder.tv_zql.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            holder.tv_time.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            if (i == 0) {
                holder.iv_bg.setVisibility(View.VISIBLE);
                holder.iv_type.setVisibility(View.VISIBLE);
                holder.iv_bg.setImageResource(R.drawable.rankj);
                holder.iv_type.setImageResource(R.drawable.jinpai);
                holder.tv_pai.setText("状元");
            } else if (i == 1) {
                holder.iv_bg.setVisibility(View.VISIBLE);
                holder.iv_type.setVisibility(View.VISIBLE);
                holder.iv_bg.setImageResource(R.drawable.ranky);
                holder.iv_type.setImageResource(R.drawable.yinpai);
                holder.tv_pai.setText("榜眼");
            } else if (i == 2) {
                holder.iv_bg.setVisibility(View.VISIBLE);
                holder.iv_type.setVisibility(View.VISIBLE);
                holder.iv_bg.setImageResource(R.drawable.rankt);
                holder.iv_type.setImageResource(R.drawable.tongpai);
                holder.tv_pai.setText("探花");
            } else {
                holder.tv_pai.setText(String.valueOf(i + 1));
                holder.tv_pai.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                holder.tv_name.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                holder.tv_zql.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                holder.tv_time.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            }
            //是否是自己
            if (bean.isIsSelf()){
                holder.tv_pai.setTextColor(mContext.getResources().getColor(R.color.err_ef5350));
                holder.tv_name.setTextColor(mContext.getResources().getColor(R.color.err_ef5350));
                holder.tv_zql.setTextColor(mContext.getResources().getColor(R.color.err_ef5350));
                holder.tv_time.setTextColor(mContext.getResources().getColor(R.color.err_ef5350));
            }else {
                holder.tv_pai.setTextColor(mContext.getResources().getColor(R.color.color_6a3906));
                holder.tv_name.setTextColor(mContext.getResources().getColor(R.color.color_6a3906));
                holder.tv_zql.setTextColor(mContext.getResources().getColor(R.color.color_6a3906));
                holder.tv_time.setTextColor(mContext.getResources().getColor(R.color.color_6a3906));
            }
            holder.tv_name.setText(ToolUtil.getString(bean.getNickname()));
            holder.tv_zql.setText(ToolUtil.getString(bean.getCorrectPercent()));
            holder.tv_time.setText(TimeUtil.getTimeLimit(ToolUtil.getLongValue(bean.getTimeCost())));
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView iv_bg;
        private final ImageView iv_type;
        private final TextView tv_pai;
        private final TextView tv_name;
        private final TextView tv_zql;
        private final TextView tv_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_bg = itemView.findViewById(R.id.iv_bg);
            iv_type = itemView.findViewById(R.id.iv_type);
            tv_pai = itemView.findViewById(R.id.tv_pai);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_zql = itemView.findViewById(R.id.tv_zql);
            tv_time = itemView.findViewById(R.id.tv_time);
        }
    }
}
