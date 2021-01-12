package software.ecenter.study.View.CapterView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import androidx.recyclerview.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import java.util.List;

import software.ecenter.study.R;

/**
 * Created by Mike on 2018/4/25.
 */

public class CaptureeAdapter extends RecyclerView.Adapter<CaptureeAdapter.ViewHolder> implements View.OnClickListener{
    private List<calElement> DataList;
    private Context mContext;
    private OnItemClickListener mItemClickListener;

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView sday;

        public ViewHolder(View itemView) {
            super(itemView);
            sday =  itemView.findViewById(R.id.day);

        }
    }

    /**
     * 用于把要展示的数据源传进来，并赋值给一个全局变量mFruitList，我们后续的操作都将在这个数据源的基础上进行。
     * @param DataList
     */
    public CaptureeAdapter(Context mContext, List<calElement> DataList) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_captureday_item,parent,false);
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

        calElement day = DataList.get(position);
        if(day.isEmpty()){
            holder.sday.setText("");
        }else {
            SpannableString sp;
            sp = new SpannableString(day.getsDay() + ""); // 只有阳历
            sp.setSpan(new StyleSpan(Typeface.NORMAL), 0, String.valueOf(day.getsDay()).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            sp.setSpan(new RelativeSizeSpan(1.2f), 0, String.valueOf(day.getsDay()).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.sday.setText(sp);
        }
        if(day.isSign()){
            holder.sday.setBackgroundResource(R.drawable.background_point_20);
            holder.sday.setTextColor(mContext.getResources().getColor(R.color.white));
        }else{
            holder.sday.setBackgroundColor(Color.TRANSPARENT);
            holder.sday.setTextColor(mContext.getResources().getColor(R.color.textHoldColor));
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
