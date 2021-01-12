package software.ecenter.study.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import software.ecenter.study.R;
import software.ecenter.study.bean.HomeBean.XiTiNanDuBean;

/**
 * Created by Mike on 2018/4/25.
 */

public class PopAdapter extends RecyclerView.Adapter<PopAdapter.ViewHolder> implements View.OnClickListener {
    private List<String> DataList;
    private List<XiTiNanDuBean> dataList;
    private Context mContext;
    private OnItemClickListener mItemClickListener;
    private boolean isShowImage;
    private boolean isShowRightText;

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView Name;
        ImageView img;
        TextView tvRightText;

        public ViewHolder(View itemView) {
            super(itemView);
            Name = (TextView) itemView.findViewById(R.id.item_text);
            img = itemView.findViewById(R.id.item_img);
            tvRightText = itemView.findViewById(R.id.tv_right);


        }
    }

    /**
     * 用于把要展示的数据源传进来，并赋值给一个全局变量mFruitList，我们后续的操作都将在这个数据源的基础上进行。
     *
     * @param DataList
     */
    public PopAdapter(Context mContext, List<String> DataList) {
        this.DataList = DataList;
        this.mContext = mContext;

    }


    public void setShowImage(boolean showImage) {
        isShowImage = showImage;
    }

    public void setShowRightText(boolean showRightText) {
        isShowRightText = showRightText;
    }

    public void refreshData(List<String> DataList) {
        this.DataList = DataList;
        notifyDataSetChanged();
    }

    public void refreshData(List<XiTiNanDuBean> DataList, boolean isShowRightText) {
        this.isShowRightText = isShowRightText;
        this.dataList = DataList;
        for (XiTiNanDuBean xiTiNanDuBean : dataList) {
            this.DataList.add(xiTiNanDuBean.getName());
        }
        notifyDataSetChanged();
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_item, parent, false);
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
        holder.Name.setText(DataList.get(position));
        if (isShowImage) {
            holder.img.setVisibility(View.VISIBLE);
        } else {
            holder.img.setVisibility(View.GONE);
        }
        if (dataList != null) {
            if (dataList.get(position).isHasXiTi()) {
                holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.cardviewback));
                holder.tvRightText.setVisibility(View.GONE);
                holder.Name.setTextColor(mContext.getResources().getColor(R.color.textHoldColor));
            } else {
                holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.ccc));
                holder.Name.setTextColor(mContext.getResources().getColor(R.color.white));
                holder.tvRightText.setVisibility(View.VISIBLE);
                holder.itemView.setEnabled(false);
            }
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
