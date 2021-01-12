package software.ecenter.study.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import software.ecenter.study.R;
import software.ecenter.study.bean.ImageBean;

/**
 * Created by Mike on 2018/4/25.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> implements View.OnClickListener {
    private List<ImageBean> DataList;
    private Context mContext;
    private OnItemClickListener mItemClickListener;
    private OnItemBtnAddClickListener mItemBtnAddClickListener;
    private boolean cannotDelete;


    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView shanchu;
        ImageView img;


        public ViewHolder(View itemView) {
            super(itemView);
            shanchu = itemView.findViewById(R.id.pic_delete);
            img = itemView.findViewById(R.id.pic_img);

        }
    }

    public void setCannotDelete(boolean cannotDelete) {
        this.cannotDelete = cannotDelete;
    }

    /**
     * 用于把要展示的数据源传进来，并赋值给一个全局变量mFruitList，我们后续的操作都将在这个数据源的基础上进行。
     *
     * @param DataList
     */
    public ImageAdapter(Context mContext, List<ImageBean> DataList) {
        this.DataList = DataList;
        this.mContext = mContext;

    }

    public void refreshData(List<ImageBean> DataList) {
        this.DataList = DataList;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_imag_item, parent, false);
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
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.itemView.setTag(position);
        ImageBean item = DataList.get(position);

        if (item.isAddImage()) {
            holder.img.setImageResource(R.drawable.tianjiatu);
            holder.shanchu.setVisibility(View.GONE);
        } else {
            if (item.getThumBitmap() != null)
                holder.img.setImageBitmap(item.getThumBitmap());
            else
                Glide.with(mContext).load(item.getTargetPicPath()).into(holder.img);

            if (!cannotDelete) {
                holder.shanchu.setVisibility(View.VISIBLE);
            } else {
                holder.shanchu.setVisibility(View.GONE);
            }
        }
        holder.shanchu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mItemBtnAddClickListener != null) {
                    mItemBtnAddClickListener.onItemBtnAddClick(position);
                }
            }
        });

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

    public interface OnItemBtnAddClickListener {
        void onItemBtnAddClick(int position);
    }

    public void setmItemBtnAddClickListener(OnItemBtnAddClickListener mItemBtnAddClickListener) {
        this.mItemBtnAddClickListener = mItemBtnAddClickListener;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

}
