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
import software.ecenter.study.bean.NameAndIdArrayBean;
import software.ecenter.study.utils.ToolUtil;

/**
 * Created by Mike on 2018/4/25.
 */

public class SchoolPopAdapter extends RecyclerView.Adapter<SchoolPopAdapter.ViewHolder> implements View.OnClickListener {
    private List<NameAndIdArrayBean.DataBean> DataList;
    private Context mContext;
    private OnItemClickListener mItemClickListener;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView Name;
        TextView item_type;
        public ViewHolder(View itemView) {
            super(itemView);
            Name = (TextView) itemView.findViewById(R.id.item_text);
            item_type = (TextView) itemView.findViewById(R.id.item_type);
        }
    }

    /**
     * 用于把要展示的数据源传进来，并赋值给一个全局变量mFruitList，我们后续的操作都将在这个数据源的基础上进行。
     *
     * @param DataList
     */
    public SchoolPopAdapter(Context mContext, List<NameAndIdArrayBean.DataBean> DataList) {
        this.DataList = DataList;
        this.mContext = mContext;

    }

    public void refreshData(List<NameAndIdArrayBean.DataBean> list) {
        DataList.clear();
        if (list!=null && list.size()>0){
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.school_item, parent, false);
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
        NameAndIdArrayBean.DataBean dataBean = DataList.get(position);
        holder.Name.setText(dataBean.getName());
        String pinyin = ToolUtil.getString(dataBean.getPinyin());
        String zimu = pinyin.substring(0, 1);
        holder.item_type.setText(zimu.toUpperCase());
        if (position == 0){
            holder.item_type.setVisibility(View.VISIBLE);
        }else {
            NameAndIdArrayBean.DataBean upBean = DataList.get(position-1);
            String upPinyin = upBean.getPinyin();
            String upZimu = upPinyin.substring(0, 1);
            if (zimu.equals(upZimu)){
                holder.item_type.setVisibility(View.INVISIBLE);
            }else {
                holder.item_type.setVisibility(View.VISIBLE);
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
