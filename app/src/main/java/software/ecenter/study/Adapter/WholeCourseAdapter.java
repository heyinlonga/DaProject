package software.ecenter.study.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.umeng.commonsdk.debug.I;

import java.util.ArrayList;
import java.util.List;

import software.ecenter.study.Interface.OnClickItemListener;
import software.ecenter.study.R;
import software.ecenter.study.bean.CateBean;
import software.ecenter.study.bean.CourseBean;
import software.ecenter.study.utils.ToolUtil;

/**
 * Message
 * Create by Administrator
 * Create by 2019/8/15
 */
public class WholeCourseAdapter extends RecyclerView.Adapter<WholeCourseAdapter.ViewHolder> {
    private Context context;
    private OnClickItemListener listener;
    private List<CourseBean> mList = new ArrayList<>();

    public WholeCourseAdapter(Context context, OnClickItemListener listener) {
        this.context = context;
        this.listener = listener;
    }

    //获取数据
    public CourseBean getChoseData(int pos) {
        if (ToolUtil.isList(mList) && pos < mList.size()) {
            return mList.get(pos);
        }
        return new CourseBean();
    }

    //更新数据
    public void setData(List<CourseBean> list) {
        mList.clear();
        if (list != null && list.size() > 0) {
            mList.addAll(list);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_wholecourse, viewGroup, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {
        CourseBean bean = mList.get(i);
        if (bean != null) {
            holder.tv_name.setText(bean.getName());
            holder.tv_gold.setText(bean.getCoinPrice() + "");
            if (bean.getType() == 0) {
                holder.tv_time.setText("直播日期：" + bean.getLiveDate());
            } else {
                holder.tv_time.setText("");
            }
            try {
                Glide.with(context).load(bean.getImgUrl()).error(R.drawable.morentu).into(holder.iv_icon);
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.tv_zhezhao.setVisibility(bean.isHaveFile()?View.GONE:View.VISIBLE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView iv_icon;
        private final TextView tv_name;
        private final TextView tv_gold;
        private final TextView tv_time;
        private final TextView tv_zhezhao;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_zhezhao = itemView.findViewById(R.id.tv_zhezhao);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_gold = itemView.findViewById(R.id.tv_gold);
            tv_time = itemView.findViewById(R.id.tv_time);
        }
    }
}
