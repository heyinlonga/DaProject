package software.ecenter.study.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import software.ecenter.study.R;
import software.ecenter.study.bean.HomeBean.CommentBean;
import software.ecenter.study.tool.ImageCacheManager;
import software.ecenter.study.utils.ToolUtil;

/**
 * Created by Mike on 2018/4/25.
 */

public class PingLunAdapter extends RecyclerView.Adapter<PingLunAdapter.ViewHolder> implements View.OnClickListener {
    private List<CommentBean> DataList;
    private ImageCacheManager imageCacheManager;
    private Context mContext;
    private OnItemClickListener mItemClickListener;

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView Image;
        TextView Name;
        TextView Time;
        TextView Pinglun;

        public ViewHolder(View itemView) {
            super(itemView);
            Image = (ImageView) itemView.findViewById(R.id.pinglun_item_img_head);
            Name = (TextView) itemView.findViewById(R.id.pinglun_item_name);
            Time = (TextView) itemView.findViewById(R.id.pinglun_item_time);
            Pinglun = (TextView) itemView.findViewById(R.id.pinglun_item_pinglun);

        }
    }

    /**
     * 用于把要展示的数据源传进来，并赋值给一个全局变量mFruitList，我们后续的操作都将在这个数据源的基础上进行。
     *
     * @param DataList
     */
    public PingLunAdapter(Context mContext, List<CommentBean> DataList) {
        this.DataList = DataList;
        this.mContext = mContext;
        imageCacheManager = new ImageCacheManager(mContext, R.drawable.morentx, true);
    }

    public void refreshData(List<CommentBean> DataList) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_pinglun_item, parent, false);
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

        CommentBean bean = DataList.get(position);
        holder.Name.setText(bean.getCommentName());
        holder.Time.setText(bean.getCommentDate());
        holder.Pinglun.setText(bean.getCommentContent());

        String url = bean.getCommentImage();
        if (!TextUtils.isEmpty(url)) {
            Glide.with(mContext).load(ToolUtil.getString(url)).into(holder.Image);
        } else {
            Glide.with(mContext).load(R.drawable.morentx).into(holder.Image);
        }
//        //1、判断缓存是否存在
//        BitmapDrawable drawable = imageCacheManager.getBitmapFromMemoryCache(url);
//        if (drawable != null) {
//            holder.Image.setImageDrawable(drawable);
//            //取消掉后台的潜在任务 然后进行下载
//        } else if (imageCacheManager.cancelPotentialWork(url, holder.Image)) {
//            imageCacheManager.setImage(url, holder.Image, 250, 200);
//        }

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
