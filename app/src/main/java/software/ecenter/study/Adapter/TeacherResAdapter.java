package software.ecenter.study.Adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import software.ecenter.study.R;
import software.ecenter.study.bean.HomeBean.HomeMoreBooksBean;
import software.ecenter.study.bean.HomeBean.TeacherResBean;
import software.ecenter.study.tool.ImageCacheManager;


public class TeacherResAdapter extends RecyclerView.Adapter<TeacherResAdapter.ViewHolder> implements View.OnClickListener {
    private List<TeacherResBean.DataBean.PackagesBean> DataList;
    private OnItemClickListener mItemClickListener;
    private boolean isActive = false;

    static class ViewHolder extends RecyclerView.ViewHolder {


        ImageView Image;
        TextView Name;

        public ViewHolder(View itemView) {
            super(itemView);
            Name = (TextView) itemView.findViewById(R.id.tv);
            Image = (ImageView) itemView.findViewById(R.id.iv_suo);


        }
    }

    /**
     * 用于把要展示的数据源传进来，并赋值给一个全局变量mFruitList，我们后续的操作都将在这个数据源的基础上进行。
     *
     * @param DataList
     */
    public TeacherResAdapter(List<TeacherResBean.DataBean.PackagesBean> DataList) {
        this.DataList = DataList;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_teacher_res, parent, false);
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

        TeacherResBean.DataBean.PackagesBean bean = DataList.get(position);
        holder.Name.setText(bean.getName());
        if (isActive || bean.isFree()) {
            holder.Image.setVisibility(View.GONE);
        } else {
            holder.Image.setVisibility(View.VISIBLE);
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
