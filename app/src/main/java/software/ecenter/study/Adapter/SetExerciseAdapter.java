package software.ecenter.study.Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import software.ecenter.study.R;
import software.ecenter.study.bean.HomeBean.ExerciseBean;


/**
 * Created by Mike on 2018/4/25.
 */

public class SetExerciseAdapter extends RecyclerView.Adapter<SetExerciseAdapter.ViewHolder> implements View.OnClickListener, View.OnLongClickListener {
    private List<ExerciseBean> DataList;
    private Context mContext;
    private OnItemClickListener mItemClickListener;
    private OnLongClickListener mOnLongClickListener;


    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView Name;
        ImageView check;
        RelativeLayout rlitemCard;

        public ViewHolder(View itemView) {
            super(itemView);
            Name =  itemView.findViewById(R.id.book_name);
            check = (ImageView) itemView.findViewById(R.id.check_img);
            rlitemCard = itemView.findViewById(R.id.rl_card_item);


        }
    }

    /**
     * 用于把要展示的数据源传进来，并赋值给一个全局变量mFruitList，我们后续的操作都将在这个数据源的基础上进行。
     *
     * @param DataList
     */
    public SetExerciseAdapter(Context mContext, List<ExerciseBean> DataList) {
        this.DataList = DataList;
        this.mContext = mContext;

    }

    public void refreshData(List<ExerciseBean> DataList) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_set_exercise_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
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

        ExerciseBean bean = DataList.get(position);
        holder.Name.setText(bean.getExerciseTitle());
        if (bean.isCheckMode()) {
            if (bean.isCheck()) {
                holder.check.setImageResource(R.drawable.xuan_r2);
            } else {
                holder.check.setImageResource(R.drawable.xuan3);
            }

        } else {
            holder.check.setImageResource(R.drawable.xuan3);
        }
        holder.check.setOnClickListener(this);
        holder.check.setTag(position);
        holder.rlitemCard.setOnClickListener(this);
        holder.rlitemCard.setTag(position);

    }


    @Override
    public int getItemCount() {
        return DataList.size();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.check_img:
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick((Integer) v.getTag(), false);
                }
                break;
            case R.id.rl_card_item:
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick((Integer) v.getTag(), true);
                }
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mOnLongClickListener != null) {
            mOnLongClickListener.onLongClick((Integer) v.getTag());
        }
        return true;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, boolean isJump);
    }

    public interface OnLongClickListener {
        void onLongClick(int position);
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public void setOnLongClickListener(OnLongClickListener listener) {
        mOnLongClickListener = listener;
    }
}
