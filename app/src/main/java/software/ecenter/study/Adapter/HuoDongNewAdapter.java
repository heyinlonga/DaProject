package software.ecenter.study.Adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import java.util.List;

import software.ecenter.study.R;
import software.ecenter.study.bean.HuoDongBean.ActiviteNewBean;
import software.ecenter.study.bean.HuoDongBean.ActivityBean;
import software.ecenter.study.tool.ImageCacheManager;
import software.ecenter.study.utils.GlideCircleTransform;
import software.ecenter.study.utils.ToolUtil;


public class HuoDongNewAdapter extends RecyclerView.Adapter<HuoDongNewAdapter.ViewHolder> implements View.OnClickListener {
    private List<ActiviteNewBean.DataBean.ActivityListBean> DataList;
    private ImageCacheManager imageCacheManager;
    private Context mContext;
    private OnItemClickListener mItemClickListener;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView Image;
        TextView Name;

        public ViewHolder(View itemView) {
            super(itemView);
            Image = (ImageView) itemView.findViewById(R.id.iv);
            Name = (TextView) itemView.findViewById(R.id.tv);
        }
    }

    public HuoDongNewAdapter(Context mContext, List<ActiviteNewBean.DataBean.ActivityListBean> DataList) {
        this.DataList = DataList;
        this.mContext = mContext;
        imageCacheManager = new ImageCacheManager(mContext, R.drawable.morentu, true);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activite, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(position);

        ActiviteNewBean.DataBean.ActivityListBean bean = DataList.get(position);
        holder.Name.setText(bean.getActivityName());

        String url = bean.getImgUrl();
        Glide.with(mContext).load(ToolUtil.getString(url))
                .transform(new CenterCrop(mContext), new GlideCircleTransform(mContext, 3))
                .error(R.drawable.morentu)
                .into(holder.Image);
//        //1、判断缓存是否存在
//        BitmapDrawable drawable = imageCacheManager.getBitmapFromMemoryCache(url);
//        if (drawable != null) {
//            holder.Image.setImageDrawable(drawable);
//            //取消掉后台的潜在任务 然后进行下载
//        } else if (imageCacheManager.cancelPotentialWork(url, holder.Image)) {
//            imageCacheManager.setImage(url, holder.Image, 200, 200);
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
