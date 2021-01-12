package software.ecenter.study.Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import software.ecenter.study.R;
import software.ecenter.study.bean.MineBean.UpdataBean;

/**
 * Created by Mike on 2018/4/25.
 */

public class UpDataAdapter extends RecyclerView.Adapter<UpDataAdapter.ViewHolder> implements View.OnClickListener {
    private List<UpdataBean> DataList;
    private Context mContext;
    private OnItemClickListener mItemClickListener;

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView updata_des;
        TextView status;
        TextView jiangli;
        TextView time;

        public ViewHolder(View itemView) {
            super(itemView);
            updata_des = itemView.findViewById(R.id.updata_des);
            title = itemView.findViewById(R.id.updata_title);
            status = itemView.findViewById(R.id.updata_status);
            jiangli = itemView.findViewById(R.id.updata_jiangli);
            time = itemView.findViewById(R.id.updata_time);
        }
    }

    /**
     * 用于把要展示的数据源传进来，并赋值给一个全局变量mFruitList，我们后续的操作都将在这个数据源的基础上进行。
     *
     * @param DataList
     */
    public UpDataAdapter(Context mContext, List<UpdataBean> DataList) {
        this.DataList = DataList;
        this.mContext = mContext;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_updata_item, parent, false);
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(position);

        UpdataBean bean = DataList.get(position);
        holder.title.setText(bean.getMainTitle());
        holder.updata_des.setText(bean.getSubTitle());
        holder.status.setText(bean.getUploadStatus());
        String t = bean.getUploadTime();
        String timedata = t.substring(0,10);
        holder.time.setText(timedata);
        if (bean.getUploadStatus().equals("审核通过")){
            holder.status.setTextColor(mContext.getResources().getColor(R.color.one_b934));
        }else if (bean.getUploadStatus().equals("审核中")){
            holder.status.setTextColor(mContext.getResources().getColor(R.color.textHoldColor));
        }else {
            holder.status.setTextColor(mContext.getResources().getColor(R.color.red_f00));
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

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }
}
