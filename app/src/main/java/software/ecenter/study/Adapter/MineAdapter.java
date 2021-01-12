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

/**
 * Message 我的页面
 * Create by Administrator
 * Create by 2019/9/3
 */
public class MineAdapter extends RecyclerView.Adapter<MineAdapter.ViewHolder> {
    private Context mContext;
    private OnClickItemListener listener;
    private List<CommType> mList = new ArrayList<>();

    public MineAdapter(Context mContext,OnClickItemListener listener) {
        this.mContext = mContext;
        this.listener = listener;
    }
    //设置数据
    public void setData(List<CommType> dataList) {
        mList.clear();
        if (dataList!=null){
            mList.addAll(dataList);
        }
        notifyDataSetChanged();
    }
    //获取选中数据
    public CommType getChoseData(int pos){
        if (mList!=null && pos < mList.size()){
            return mList.get(pos);
        }
        return new CommType();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_mine, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        CommType commType = mList.get(i);
        if (commType!=null){
            viewHolder.tv_name.setText(commType.getName());
            viewHolder.iv_icon.setImageResource(commType.getResId());
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.onItemClick(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private final ImageView iv_icon;
        private final TextView tv_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            tv_name = itemView.findViewById(R.id.tv_name);
        }
    }
}
