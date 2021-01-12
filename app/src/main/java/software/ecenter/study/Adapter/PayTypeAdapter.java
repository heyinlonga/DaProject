package software.ecenter.study.Adapter;

import android.content.Context;
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
import software.ecenter.study.bean.HomeBean.payTypeBean;
import software.ecenter.study.utils.ToolUtil;


/**
 * Created by Mike on 2018/4/25.
 */

public class PayTypeAdapter extends RecyclerView.Adapter<PayTypeAdapter.ViewHolder> implements View.OnClickListener {
    private List<payTypeBean> DataList;
    private Context mContext;
    private boolean isDiscount;
    private OnItemClickListener mItemClickListener;


    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView payImg;
        TextView payName;
        TextView payPrice;
        ImageView check;
        CardView cd_view;

        public ViewHolder(View itemView) {
            super(itemView);
            payImg = itemView.findViewById(R.id.pay_img);
            payName = itemView.findViewById(R.id.pay_name);
            payPrice = itemView.findViewById(R.id.pay_price);
            check = itemView.findViewById(R.id.check_img);
            cd_view = itemView.findViewById(R.id.cd_view);
        }
    }

    /**
     * 用于把要展示的数据源传进来，并赋值给一个全局变量mFruitList，我们后续的操作都将在这个数据源的基础上进行。
     *
     * @param DataList
     */
    public PayTypeAdapter(Context mContext, List<payTypeBean> DataList, boolean isDiscount) {
        this.DataList = DataList;
        this.mContext = mContext;
        this.isDiscount = isDiscount;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_pay_type_item, parent, false);
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
        payTypeBean item = DataList.get(position);
        String price;
        if (isDiscount) {//有优惠
            if (!ToolUtil.getString(item.getDiscountNum()).equals("")) {
                price = item.getDiscountNum();
            } else {
                price = item.getPayNum();
            }
        } else {
            price = item.getPayNum();
        }
        if ("元".equals(item.getPayUnit())) {
            holder.payPrice.setText("￥" + price + item.getPayUnit());
        } else {
            holder.payPrice.setText(price + item.getPayUnit());
        }
        //1、微信 2、支付宝 3、学习币 4、积分5、答疑券
        if ("1".equals(item.getPayType())) {
            holder.payImg.setImageResource(R.drawable.weixin);
            holder.payName.setText("微信");
            holder.cd_view.setVisibility(ToolUtil.getDoubleValue(price)>0?View.VISIBLE:View.GONE);
        } else if ("2".equals(item.getPayType())) {
            holder.payImg.setImageResource(R.drawable.zhifubao);
            holder.payName.setText("支付宝");
            holder.cd_view.setVisibility(ToolUtil.getDoubleValue(price)>0?View.VISIBLE:View.GONE);
        } else if ("3".equals(item.getPayType())) {
            holder.payImg.setImageResource(R.drawable.xuexibi);
            holder.payName.setText("元宝");
        } else if ("4".equals(item.getPayType())) {
            holder.payImg.setImageResource(R.drawable.jifenbi);
            holder.payName.setText("积分");
        } else if ("5".equals(item.getPayType())) {
            holder.payImg.setImageResource(R.drawable.dayiquan2);
            holder.payName.setText("答疑券");
        }


        if (item.isCheck()) {
            holder.check.setImageResource(R.drawable.xuan_r2);
        } else {
            holder.check.setImageResource(R.drawable.xuan_r1);
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
