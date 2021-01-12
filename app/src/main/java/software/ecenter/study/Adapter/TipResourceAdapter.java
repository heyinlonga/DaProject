package software.ecenter.study.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import software.ecenter.study.R;
import software.ecenter.study.bean.HomeBean.ResourceBean;


/**
 * Created by Mike on 2018/4/25.
 */

public class TipResourceAdapter extends RecyclerView.Adapter<TipResourceAdapter.ViewHolder> implements View.OnClickListener {
    private List<ResourceBean> DataList = new ArrayList<>();
    private Context mContext;
    private OnItemClickListener mItemClickListener;

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView Name;

        public ViewHolder(View itemView) {
            super(itemView);
            Name = (TextView) itemView.findViewById(R.id.book_name);

        }
    }

    /**
     * 用于把要展示的数据源传进来，并赋值给一个全局变量mFruitList，我们后续的操作都将在这个数据源的基础上进行。
     *
     * @param
     */
    public TipResourceAdapter(Context mContext, List<ResourceBean> list) {
        DataList.clear();
        if (list != null && list.size() > 0) {
            DataList.addAll(list);
        }
        this.mContext = mContext;

    }

    public void refreshData(List<ResourceBean> list) {
        DataList.clear();
        if (list != null && list.size() > 0) {
            DataList.addAll(list);
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_tip_item, parent, false);
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

        ResourceBean bean = DataList.get(position);
        if (TextUtils.isEmpty(bean.getResourceTitle())) {
            holder.Name.setText(bean.getResourceName());
        } else {
            holder.Name.setText(bean.getResourceTitle());
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
