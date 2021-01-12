package software.ecenter.study.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import software.ecenter.study.R;
import software.ecenter.study.activity.BigPicActivity;
import software.ecenter.study.bean.MineBean.ImageLookBean;
import software.ecenter.study.tool.ImageCacheManager;

/**
 * Created by Mike on 2018/4/25.
 */

public class ImageLookAdapter extends RecyclerView.Adapter<ImageLookAdapter.ViewHolder> implements View.OnClickListener {
    private List<ImageLookBean> DataList = new ArrayList<>();
    private Context mContext;
    private OnItemClickListener mItemClickListener;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.pic_img);

        }
    }

    /**
     * 用于把要展示的数据源传进来，并赋值给一个全局变量mFruitList，我们后续的操作都将在这个数据源的基础上进行。
     *
     * @param
     */
    public ImageLookAdapter(Context Context) {
        this.mContext = Context;
    }

    public void refreshData(List<ImageLookBean> list) {
        DataList.clear();
        DataList.addAll(list);
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_image_look_item, parent, false);
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
        final ImageLookBean item = DataList.get(position);
        final String ImageUrL = item.getImgeUrl();
        try {
            if (mContext != null) {
                Glide.with(mContext).load(ImageUrL).placeholder(R.drawable.morentu).error(R.drawable.morentu).into(holder.img);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, BigPicActivity.class);
                intent.putExtra("ImageUrl", ImageUrL);
                mContext.startActivity(intent);
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

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

}
