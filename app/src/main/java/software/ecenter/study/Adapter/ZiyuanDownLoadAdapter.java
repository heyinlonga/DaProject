package software.ecenter.study.Adapter;

import android.content.Context;

import androidx.cardview.widget.CardView;
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

import com.alibaba.sdk.android.oss.common.utils.DateUtil;
import com.dinuscxj.progressbar.CircleProgressBar;

import java.util.List;

import software.ecenter.study.R;
import software.ecenter.study.bean.HomeBean.ResourceBean;
import software.ecenter.study.utils.DataUtils;
import software.ecenter.study.utils.DateUtils;


/**
 * Created by Mike on 2018/4/25.
 */

public class ZiyuanDownLoadAdapter extends RecyclerView.Adapter<ZiyuanDownLoadAdapter.ViewHolder> implements View.OnClickListener, View.OnLongClickListener {
    private List<ResourceBean> DataList;
    private Context mContext;
    private OnItemClickListener mItemClickListener;
    private OnLongClickListener mOnLongClickListener;
    private OnTouchDown mOnTouchDown;
    private boolean isclick = false;


    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView Name;
        LinearLayout ShijianLine;
        TextView Shijian;
        LinearLayout DaxiaoLine;
        TextView Daxiao;
        ImageView check;
        TextView tvIsDownLoad;
        TextView tvIsCanDownLoad;
        RelativeLayout rlItem;
        CircleProgressBar circleProgressBar;
        CardView cvCardView;
        ImageView iv_xin;

        public ViewHolder(View itemView) {
            super(itemView);
            Name = (TextView) itemView.findViewById(R.id.book_name);
            ShijianLine = (LinearLayout) itemView.findViewById(R.id.book_shijian_line);
            Shijian = (TextView) itemView.findViewById(R.id.book_shijian);
            DaxiaoLine = (LinearLayout) itemView.findViewById(R.id.book_daxiao_line);
            Daxiao = (TextView) itemView.findViewById(R.id.book_daxiao);
            check = (ImageView) itemView.findViewById(R.id.check_img);
            tvIsDownLoad = (TextView) itemView.findViewById(R.id.check_is_download);
            tvIsCanDownLoad = itemView.findViewById(R.id.check_is_can_download);
            rlItem = itemView.findViewById(R.id.rl_card_item);
            circleProgressBar = itemView.findViewById(R.id.cpb_progress);
            cvCardView = itemView.findViewById(R.id.cv_ziyuan_detail);
            iv_xin = itemView.findViewById(R.id.iv_xin);
        }
    }

    /**
     * 用于把要展示的数据源传进来，并赋值给一个全局变量mFruitList，我们后续的操作都将在这个数据源的基础上进行。
     *
     * @param DataList
     */
    public ZiyuanDownLoadAdapter(Context mContext, List<ResourceBean> DataList) {
        this.DataList = DataList;
        this.mContext = mContext;

    }

    public void refreshData(List<ResourceBean> DataList) {
        this.DataList = DataList;
        if (!isclick)
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_ziyuan_download_item, parent, false);
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
           holder.tvIsCanDownLoad.setVisibility(View.GONE);
        ResourceBean bean = DataList.get(position);
        holder.iv_xin.setVisibility(bean.isNewBatch()?View.VISIBLE:View.GONE);
        if (!bean.isHaveResourceFile()) {
            holder.cvCardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.d9d9));
            holder.Name.setTextColor(mContext.getResources().getColor(R.color.white));
        } else {
            holder.cvCardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.cardviewback));
            holder.Name.setTextColor(mContext.getResources().getColor(R.color.textHoldColor));
        }
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
            holder.check.setVisibility(View.VISIBLE);
            if (bean.isDownloadOk()) {
                holder.check.setVisibility(View.VISIBLE);
                holder.tvIsDownLoad.setVisibility(View.VISIBLE);
                holder.tvIsDownLoad.setText("已下载");
                holder.check.setImageResource(R.drawable.xuan3);
                holder.check.setVisibility(View.GONE);
                holder.circleProgressBar.setVisibility(View.GONE);
            }
            if (bean.isDownload()) {
                holder.check.setVisibility(View.GONE);
                holder.tvIsDownLoad.setVisibility(View.GONE);
                holder.tvIsCanDownLoad.setVisibility(View.GONE);
                holder.check.setVisibility(View.GONE);
//                holder.downloading.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(bean.getResourceDownloadSize())) {
                    holder.tvIsDownLoad.setVisibility(View.GONE);
                    holder.circleProgressBar.setVisibility(View.VISIBLE);
//                    holder.downloading.setText("已下载" + bean.getResourceDownloadSize());
                    holder.circleProgressBar.setProgressFormatter(new CircleProgressBar.ProgressFormatter() {
                        @Override
                        public CharSequence format(int progress, int max) {
                            return progress + "%";
                        }
                    });
                    if (Integer.parseInt(bean.getResourceDownloadSize()) > holder.circleProgressBar.getProgress())
                        holder.circleProgressBar.setProgress(Integer.parseInt(bean.getResourceDownloadSize()));
                } else {
                    holder.tvIsDownLoad.setVisibility(View.VISIBLE);
                    holder.tvIsDownLoad.setText("待下载");
                }

            } else if (!bean.isCanDownload() || !bean.isHaveResourceFile()) {
                holder.check.setVisibility(View.GONE);
                holder.tvIsDownLoad.setVisibility(View.GONE);
                if (bean.getType().contains("richtext"))
                    holder.tvIsCanDownLoad.setVisibility(View.VISIBLE);
                else
                    holder.tvIsCanDownLoad.setVisibility(View.GONE);
            } else if (bean.isDownloadOk()) {
                holder.tvIsCanDownLoad.setVisibility(View.GONE);
                holder.check.setVisibility(View.GONE);
                holder.tvIsDownLoad.setVisibility(View.VISIBLE);
                holder.tvIsDownLoad.setText("已下载");
                holder.check.setImageResource(R.drawable.xuan3);
                holder.circleProgressBar.setVisibility(View.GONE);
            } else {
                holder.check.setVisibility(View.VISIBLE);
                holder.tvIsCanDownLoad.setVisibility(View.GONE);
                holder.tvIsDownLoad.setVisibility(View.GONE);

                if (bean.isCheck()) {
                    holder.check.setImageResource(R.drawable.xuan_r2);
                } else {
                    holder.check.setImageResource(R.drawable.xuan3);
                }
            }
        } else {
            //holder.check.setVisibility(View.INVISIBLE);
        }
        if (bean.isSpecial()) {
            if (!bean.isBuy())
                holder.check.setVisibility(View.GONE);
            holder.cvCardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.te_shu_cardviewback));
        }
        holder.check.setOnClickListener(this);
        holder.check.setTag(position);
        holder.rlItem.setOnClickListener(this);
        holder.rlItem.setTag(position);
        holder.rlItem.setOnTouchListener(new View.OnTouchListener() {
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

    public void setonTouchDown(OnTouchDown onTouchDown) {
        mOnTouchDown = onTouchDown;
    }

}
