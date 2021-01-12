package software.ecenter.study.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
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
import software.ecenter.study.View.RankManBean;
import software.ecenter.study.utils.ToolUtil;

/**
 * Message 获奖名单
 * Create by Administrator
 * Create by 2019/10/31
 */
public class RankManAdapter extends RecyclerView.Adapter<RankManAdapter.ViewHolder> {
    private Context mContext;
    private List<RankManBean.DataBean> mList = new ArrayList<>();

    public RankManAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<RankManBean.DataBean> list) {
        mList.clear();
        if (ToolUtil.isList(list)) {
            mList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public List<RankManBean.DataBean> getmList() {
        return mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_rankman, viewGroup, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        RankManBean.DataBean bean = mList.get(i);
        if (bean != null) {
            holder.iv_type.setVisibility(View.INVISIBLE);
            holder.ll_bg.setBackground(null);
            if (i == 0) {
                holder.ll_bg.setBackground(mContext.getResources().getDrawable(R.drawable.rankj));
                holder.iv_type.setImageResource(R.drawable.jinpai);
                holder.iv_type.setVisibility(View.VISIBLE);
            } else if (i == 1) {
                holder.ll_bg.setBackground(mContext.getResources().getDrawable(R.drawable.ranky));
                holder.iv_type.setImageResource(R.drawable.yinpai);
                holder.iv_type.setVisibility(View.VISIBLE);
            } else if (i == 2) {
                holder.ll_bg.setBackground(mContext.getResources().getDrawable(R.drawable.rankt));
                holder.iv_type.setImageResource(R.drawable.tongpai);
                holder.iv_type.setVisibility(View.VISIBLE);
            }
            holder.tv_name.setText(ToolUtil.getString(bean.getPrizeName()));
            holder.tv_bonus.setText(String.valueOf(bean.getBonusPoints()));
            holder.adapter.setData(bean.getUsers());
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout ll_bg;
        private final ImageView iv_type;
        private final TextView tv_name;
        private final TextView tv_bonus;
        private final RecyclerView rv_man;
        private final RankManChildAdapter adapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ll_bg = itemView.findViewById(R.id.ll_bg);
            iv_type = itemView.findViewById(R.id.iv_type);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_bonus = itemView.findViewById(R.id.tv_bonus);
            rv_man = itemView.findViewById(R.id.rv_man);
            rv_man.setLayoutManager(new GridLayoutManager(mContext,3));
            adapter = new RankManChildAdapter(mContext);
            rv_man.setAdapter(adapter);
        }
    }
}
