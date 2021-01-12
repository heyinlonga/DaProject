package software.ecenter.study.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import software.ecenter.study.R;
import software.ecenter.study.bean.HomeBean.ResourceBean;
import software.ecenter.study.utils.DataUtils;
import software.ecenter.study.utils.DateUtils;


/**
 * Created by Mike on 2018/4/25.
 */

public class ZiyuanAdapter extends RecyclerView.Adapter<ZiyuanAdapter.ViewHolder> implements View.OnClickListener {
    private List<ResourceBean> DataList;
    private Context mContext;
    private OnItemClickListener mItemClickListener;

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView Name;
        LinearLayout ShijianLine;
        TextView Shijian;
        LinearLayout DaxiaoLine;
        TextView Daxiao;
        CardView cardView;
        ImageView iv_suo;
        ImageView iv_xin;

        public ViewHolder(View itemView) {
            super(itemView);
            Name = (TextView) itemView.findViewById(R.id.book_name);
            ShijianLine = (LinearLayout) itemView.findViewById(R.id.book_shijian_line);
            Shijian = (TextView) itemView.findViewById(R.id.book_shijian);
            DaxiaoLine = (LinearLayout) itemView.findViewById(R.id.book_daxiao_line);
            Daxiao = (TextView) itemView.findViewById(R.id.book_daxiao);
            cardView = itemView.findViewById(R.id.cv_ziyuan_detail);
            iv_suo = itemView.findViewById(R.id.iv_suo);
            iv_xin = itemView.findViewById(R.id.iv_xin);
        }
    }

    /**
     * 用于把要展示的数据源传进来，并赋值给一个全局变量mFruitList，我们后续的操作都将在这个数据源的基础上进行。
     *
     * @param DataList
     */
    public ZiyuanAdapter(Context mContext, List<ResourceBean> DataList) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_ziyuan_item, parent, false);
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
        holder.Name.setText(bean.getResourceTitle());
        if (TextUtils.isEmpty(bean.getResourceTime())) {
            holder.ShijianLine.setVisibility(View.GONE);
        } else {
            holder.ShijianLine.setVisibility(View.VISIBLE);
            DataUtils getData = new DataUtils();
            holder.Shijian.setText(getData.toTimeStr(Integer.parseInt(bean.getResourceTime())));
        }

        if (TextUtils.isEmpty(bean.getResourceSize())) {
            holder.DaxiaoLine.setVisibility(View.GONE);
        } else {

            holder.DaxiaoLine.setVisibility(View.VISIBLE);
            DateUtils dateUtils = new DateUtils();
            holder.Daxiao.setText(dateUtils.formetFileSize(Double.parseDouble(bean.getResourceSize())));

        }
        if (!bean.isHaveResourceFile()) {
            holder.cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.d9d9));
            holder.Name.setTextColor(mContext.getResources().getColor(R.color.white));
            holder.DaxiaoLine.setVisibility(View.GONE);
            holder.ShijianLine.setVisibility(View.GONE);
        }
        //第2批次为购买
        holder.iv_suo.setVisibility(bean.isNewBatch() ? View.VISIBLE : View.GONE);
        holder.iv_xin.setVisibility(bean.isNewBatch() ? View.VISIBLE : View.GONE);
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
