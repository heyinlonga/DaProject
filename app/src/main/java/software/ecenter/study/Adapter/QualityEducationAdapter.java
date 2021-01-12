package software.ecenter.study.Adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import software.ecenter.study.R;
import software.ecenter.study.bean.HomeBean.QualityEducationBean;
import software.ecenter.study.tool.ImageCacheManager;
import software.ecenter.study.utils.ToolUtil;

/**
 * Created by Mike on 2018/4/25.
 */

public class QualityEducationAdapter extends RecyclerView.Adapter<QualityEducationAdapter.ViewHolder> implements View.OnClickListener {
    private List<QualityEducationBean> DataList;
    private ImageCacheManager imageCacheManager;
    private Context mContext;
    private int type;//区分书包还是首页
    private OnItemClickListener mItemClickListener;

    public void setType(int type) {
        this.type = type;
    }


    static class ViewHolder extends RecyclerView.ViewHolder{


        ImageView Image;
        ImageView kecheng_moneyicon;
//        TextView Name;
        TextView Money;

        public ViewHolder(View itemView) {
            super(itemView);
            Image = (ImageView) itemView.findViewById(R.id.kecheng_image);
//            Name = (TextView) itemView.findViewById(R.id.kecheng_name);
            Money = (TextView) itemView.findViewById(R.id.kecheng_money);
            kecheng_moneyicon = (ImageView) itemView.findViewById(R.id.kecheng_moneyicon);

        }
    }

    /**
     * 用于把要展示的数据源传进来，并赋值给一个全局变量mFruitList，我们后续的操作都将在这个数据源的基础上进行。
     * @param DataList
     */
    public QualityEducationAdapter(Context mContext, List<QualityEducationBean> DataList) {
        this.DataList = DataList;
        this.mContext = mContext;
        imageCacheManager = new ImageCacheManager(mContext, R.drawable.morenshu, true);
    }

    public void refreshData( List<QualityEducationBean> DataList) {
        this.DataList = DataList;
        notifyDataSetChanged();
    }
    /**
     * 用于创建ViewHolder实例的，并把加载出来的布局传入到构造函数当中，最后将viewholder的实例返回
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class_jaoyu,parent,false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class_jaoyu_new,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    /**
     * 用于对RecyclerView子项的数据进行赋值的，会在每个子项被滚动到屏幕内的时候执行，这里我们通过
     * position参数得到当前项的Fruit实例，然后再将数据设置到ViewHolder的Imageview和textview当中即可，
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(position);

        QualityEducationBean bean = DataList.get(position);
        if (bean == null) return;
//        holder.Name.setText(bean.getName());
        String price = ToolUtil.getString(bean.getCoinPrice(), "0");
        if (price.equals("0")){
            holder.Money.setText("免费");
            holder.kecheng_moneyicon.setVisibility(View.GONE);
        }else {
            holder.Money.setText(bean.getCoinPrice());
            holder.kecheng_moneyicon.setVisibility(View.VISIBLE);
        }

        String url = bean.getImgUrl();
        //1、判断缓存是否存在
        BitmapDrawable drawable = imageCacheManager.getBitmapFromMemoryCache(url);
        if (drawable != null) {
            holder.Image.setImageDrawable(drawable);
            //取消掉后台的潜在任务 然后进行下载
        } else if (imageCacheManager.cancelPotentialWork(url, holder.Image)) {
            imageCacheManager.setImage(url, holder.Image, 250, 200);
        }

    }



    @Override
    public int getItemCount() {
        return DataList.size();
    }

    @Override
    public void onClick(View v) {
        if (mItemClickListener!=null){
            mItemClickListener.onItemClick((Integer) v.getTag());
        }
    }


    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }
}
