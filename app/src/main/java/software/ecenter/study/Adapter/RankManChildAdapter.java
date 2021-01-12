package software.ecenter.study.Adapter;

import android.content.Context;

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
import software.ecenter.study.View.RankManBean;
import software.ecenter.study.utils.ToolUtil;

/**
 * Message 获奖名单  人
 * Create by Administrator
 * Create by 2019/10/31
 */
public class RankManChildAdapter extends RecyclerView.Adapter<RankManChildAdapter.ViewHolder> {
    private Context mContext;
    private List<RankManBean.DataBean.UsersBean> mList = new ArrayList<>();

    public RankManChildAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<RankManBean.DataBean.UsersBean> list) {
        mList.clear();
        if (ToolUtil.isList(list)) {
            mList.addAll(list);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_rankman_child, viewGroup, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        RankManBean.DataBean.UsersBean bean = mList.get(i);
        if (bean != null) {
            holder.tv_name.setText(ToolUtil.getString(bean.getName()));
            if (bean.isIsSelf()){
                holder.tv_name.setTextColor(mContext.getResources().getColor(R.color.err_ef5350));
            }else {
                holder.tv_name.setTextColor(mContext.getResources().getColor(R.color.color_6a3906));
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);

        }
    }
}
