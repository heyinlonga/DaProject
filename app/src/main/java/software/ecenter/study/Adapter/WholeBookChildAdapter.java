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

import software.ecenter.study.Interface.OnClickItemListener;
import software.ecenter.study.R;
import software.ecenter.study.bean.CateBean;

/**
 * Message 整本书阅读  节列表
 * Create by Administrator
 * Create by 2019/8/15
 */
public class WholeBookChildAdapter extends RecyclerView.Adapter<WholeBookChildAdapter.ViewHolder> {
    private Context context;
    private int parentPos;
    private List<CateBean>  mList = new ArrayList<>();
    private OnClickItemListener listener;
    public WholeBookChildAdapter(Context context,OnClickItemListener listener) {
        this.context = context;
        this.listener = listener;
    }
    //更新数据
    public void setData(int parentPos,List<CateBean> list){
        this.parentPos = parentPos;
        mList.clear();
        if (list!=null && list.size()>0){
            mList.addAll(list);
        }
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_wholebookchild, viewGroup, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {
        CateBean cateBean = mList.get(i);
        if (cateBean!=null){
            holder.tv_name.setText(cateBean.getName());
            if (cateBean.isNoFile()) {
                holder.ll_top.setBackgroundColor(0);
//                holder.ll_top.setBackgroundColor(context.getResources().getColor(R.color.color_b37070));
            } else {
                holder.ll_top.setBackgroundColor(context.getResources().getColor(R.color.cardviewback));
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.onChildItemClick(parentPos,i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder{

        private final LinearLayout ll_top;
        private final TextView tv_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ll_top = itemView.findViewById(R.id.ll_top);
            tv_name = itemView.findViewById(R.id.tv_name);
        }
    }
}
