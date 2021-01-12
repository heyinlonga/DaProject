package software.ecenter.study.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import software.ecenter.study.R;
import software.ecenter.study.bean.GuiZheBean;
import software.ecenter.study.utils.ToolUtil;

/**
 * Message 规则
 * Create by Administrator
 * Create by 2019/10/28
 */
public class GuiZheAdapter extends RecyclerView.Adapter<GuiZheAdapter.ViewHolder> {
    private List<GuiZheBean.DataBean> mList = new ArrayList<>();
    private Context mContext;

    public GuiZheAdapter(Context mContext) {
        this.mContext = mContext;
    }
    public void setData(List<GuiZheBean.DataBean> list){
        mList.clear();
        if (ToolUtil.isList(list)){
            mList.addAll(list);
        }
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_guizhe, viewGroup, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        GuiZheBean.DataBean bean = mList.get(i);
        if (bean!=null){
            holder.tv_name.setText(ToolUtil.getString(bean.getName()));
            holder.tv_des.setText(ToolUtil.getString(bean.getContent()));
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView tv_name;
        private final TextView tv_des;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_des = itemView.findViewById(R.id.tv_des);
        }
    }
}
