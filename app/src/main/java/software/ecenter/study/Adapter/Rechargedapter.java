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
import software.ecenter.study.bean.RechargeBean;

/**
 * Created by Mike on 2018/4/25.
 */

public class Rechargedapter extends RecyclerView.Adapter<Rechargedapter.ViewHolder> implements View.OnClickListener{
    private List<RechargeBean> DataList;
    private Context mContext;
    private OnItemClickListener mItemClickListener;


    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView rechargeBi;
        TextView rechargeMoney;
        TextView rechargeJiFen;
        ImageView check;

        public ViewHolder(View itemView) {
            super(itemView);
            rechargeBi = itemView.findViewById(R.id.recharge_bi);
            rechargeMoney = itemView.findViewById(R.id.recharge_money);
            check = itemView.findViewById(R.id.check_img);
            rechargeJiFen = itemView.findViewById(R.id.recharge_jifen);
        }
    }

    /**
     * 用于把要展示的数据源传进来，并赋值给一个全局变量mFruitList，我们后续的操作都将在这个数据源的基础上进行。
     * @param DataList
     */
    public Rechargedapter(Context mContext, List<RechargeBean> DataList) {
        this.DataList = DataList;
        this.mContext = mContext;

    }

    /**
     * 用于创建ViewHolder实例的，并把加载出来的布局传入到构造函数当中，最后将viewholder的实例返回
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_recharge_item,parent,false);
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
        RechargeBean item = DataList.get(position);
        holder.rechargeBi.setText(item.getRechargeBi());
        holder.rechargeMoney.setText(item.getRechargeMoney());
        holder.rechargeJiFen.setText(item.getRechargeJiFen());
        if (item.isCheck()) {
            holder.check.setVisibility(View.VISIBLE);
        } else {
            holder.check.setVisibility(View.INVISIBLE);
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
