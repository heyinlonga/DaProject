package software.ecenter.study.Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dinuscxj.progressbar.CircleProgressBar;

import java.util.List;

import software.ecenter.study.R;
import software.ecenter.study.bean.HomeBean.ResourceBean;
import software.ecenter.study.utils.DataUtils;
import software.ecenter.study.utils.DateUtils;


/**
 * Created by Mike on 2018/4/25.
 */

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.ViewHolder> implements View.OnClickListener, View.OnLongClickListener {
    private List<ResourceBean> DataList;
    private Context mContext;
    private OnItemClickListener mItemClickListener;
    private OnLongClickListener mOnLongClickListener;
    private OnTouchDown mOnTouchDown;

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView Name;
        LinearLayout ShijianLine;
        TextView Shijian;
        LinearLayout DaxiaoLine;
        TextView Daxiao;
        ImageView check;
        TextView downloading;
        LinearLayout llBookInfo;
        CircleProgressBar circleProgressBar;
        RelativeLayout rlCardItem;

        public ViewHolder(View itemView) {
            super(itemView);
            Name = (TextView) itemView.findViewById(R.id.book_name);
            ShijianLine = (LinearLayout) itemView.findViewById(R.id.book_shijian_line);
            Shijian = (TextView) itemView.findViewById(R.id.book_shijian);
            DaxiaoLine = (LinearLayout) itemView.findViewById(R.id.book_daxiao_line);
            Daxiao = (TextView) itemView.findViewById(R.id.book_daxiao);
            check = (ImageView) itemView.findViewById(R.id.check_img);
            downloading = (TextView) itemView.findViewById(R.id.check_text);
            llBookInfo = itemView.findViewById(R.id.ll_book_info);
            circleProgressBar = itemView.findViewById(R.id.cpb_progress);
            rlCardItem = itemView.findViewById(R.id.rl_card_item);

        }
    }

    /**
     * 用于把要展示的数据源传进来，并赋值给一个全局变量mFruitList，我们后续的操作都将在这个数据源的基础上进行。
     *
     * @param DataList
     */
    public DownloadAdapter(Context mContext, List<ResourceBean> DataList) {
        this.DataList = DataList;
        this.mContext = mContext;
    }

    public void refreshData(List<ResourceBean> DataList) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_download_item, parent, false);
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
        holder.rlCardItem.setTag(position);
        holder.check.setTag(position);

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
        if (bean.isCheckMode()) {
//            holder.check.setVisibility(View.VISIBLE);
//            holder.downloading.setVisibility(View.GONE);
            if (bean.isDownloadOk()) {
                holder.check.setVisibility(View.VISIBLE);
                holder.downloading.setVisibility(View.GONE);
                holder.check.setImageResource(R.drawable.xuan3);
                holder.circleProgressBar.setVisibility(View.GONE);
            } else {
                holder.check.setVisibility(View.GONE);
//                holder.downloading.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(bean.getResourceDownloadSize())) {
                    holder.downloading.setVisibility(View.GONE);
                    holder.circleProgressBar.setVisibility(View.VISIBLE);
//                    holder.downloading.setText("已下载" + bean.getResourceDownloadSize());
                    holder.circleProgressBar.setProgressFormatter(new CircleProgressBar.ProgressFormatter() {
                        @Override
                        public CharSequence format(int progress, int max) {
                            return progress + "%";
                        }
                    });
                    holder.circleProgressBar.setProgress(Integer.parseInt(bean.getResourceDownloadSize()));
                } else {
                    holder.downloading.setVisibility(View.VISIBLE);
                    holder.downloading.setText("待下载");
                }
            }
            if (bean.isCheck()) {
                holder.check.setImageResource(R.drawable.xuan_r2);
            } else {
                holder.check.setImageResource(R.drawable.xuan3);
            }

        } else {
            if (bean.isDownloadOk()) {
                holder.check.setVisibility(View.VISIBLE);
                holder.downloading.setVisibility(View.GONE);
                holder.check.setImageResource(R.drawable.xuan3);
                holder.circleProgressBar.setVisibility(View.GONE);
            } else {
                holder.check.setVisibility(View.GONE);
                holder.downloading.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(bean.getResourceDownloadSize())) {
//                    holder.downloading.setText("已下载" + bean.getResourceDownloadSize());
                    holder.circleProgressBar.setProgressFormatter(new CircleProgressBar.ProgressFormatter() {
                        @Override
                        public CharSequence format(int progress, int max) {
                            return progress + "%";
                        }
                    });
                    holder.circleProgressBar.setProgress(Integer.parseInt(bean.getResourceDownloadSize()));

                }
            }
        }
        holder.check.setOnClickListener(this);
        holder.rlCardItem.setOnClickListener(this);
        holder.rlCardItem.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (mOnTouchDown != null)
                            mOnTouchDown.onTouchDown(view, motionEvent);
                        break;

                    case MotionEvent.ACTION_MOVE:

                        break;

                    case MotionEvent.ACTION_UP:

                        break;

                    case MotionEvent.ACTION_CANCEL:

                        break;

                }
                return false;

            }
        });
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

    public interface OnTouchDown {
        void onTouchDown(View view, MotionEvent event);
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public void setOnLongClickListener(OnLongClickListener listener) {
        mOnLongClickListener = listener;
    }

    public void setonTouchDown(DownloadAdapter.OnTouchDown onTouchDown) {
        mOnTouchDown = onTouchDown;
    }

}
