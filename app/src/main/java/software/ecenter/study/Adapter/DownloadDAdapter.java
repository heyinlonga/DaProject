package software.ecenter.study.Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dinuscxj.progressbar.CircleProgressBar;

import java.util.ArrayList;
import java.util.List;

import software.ecenter.study.R;
import software.ecenter.study.bean.HomeBean.ResourceBean;
import software.ecenter.study.utils.DataUtils;
import software.ecenter.study.utils.DateUtils;


/**
 * 修改  下载列表
 * Created by Mike on 2018/4/25.
 */

public class DownloadDAdapter extends RecyclerView.Adapter<DownloadDAdapter.ViewHolder> implements View.OnClickListener {
    private List<ResourceBean> DataList = new ArrayList<>();
    private Context mContext;
    private OnItemClickListener mItemClickListener;
    private boolean isChose;//是否处于选中状态


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView Name;
        ImageView book_upload;
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
            book_upload = itemView.findViewById(R.id.book_upload);
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
     */
    public DownloadDAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public List<ResourceBean> getDataList() {
        return DataList;
    }

    //设置数据
    public void setData(List<ResourceBean> list) {
        DataList.clear();
        if (list != null && list.size() > 0) {
            DataList.addAll(list);
        }
        notifyDataSetChanged();
    }

    //设置数据
    public void setMoreData(List<ResourceBean> list) {
        DataList.clear();
        if (list != null && list.size() > 0) {
            DataList.addAll(list);
        }
        notifyDataSetChanged();
    }

    //更新单条数据
    public void refreshOneData(String id, boolean isOk) {
        if (DataList != null && DataList.size() > 0) {
            for (int i = 0; i < DataList.size(); i++) {
                ResourceBean item = DataList.get(i);
                if (item.getResourceId().equals(id)) {
                    item.setDownloadOk(true);
                    item.setNeedUpdate(!isOk);
                    DataList.set(i, item);
                }
            }
            notifyDataSetChanged();
        }
    }

    //删除
    public void delOneData(String resid) {
        if (DataList != null && DataList.size() > 0 && resid != null) {
            for (int i = 0; i < DataList.size(); i++) {
                String resourceId = DataList.get(i).getResourceId();
                if (resourceId != null && resourceId.equals(resid)) {
                    DataList.remove(i);
                    return;
                }
            }
        }
    }

    //更新状态
    public void setUpAnwer(String resid, boolean status) {
        if (DataList != null && DataList.size() > 0 && resid != null) {
            for (int i = 0; i < DataList.size(); i++) {
                String resourceId = DataList.get(i).getResourceId();
                if (resourceId != null && resourceId.equals(resid)) {
                    DataList.get(i).setAnwer(true);
                    DataList.get(i).setDownloadOk(true);
                    DataList.get(i).setNeedUpdate(status);
                }
            }
            if (status)
                notifyDataSetChanged();
        }
    }

    //是否有更新
    public boolean getUpDataStatus() {
        if (DataList != null && DataList.size() > 0) {
            for (int i = 0; i < DataList.size(); i++) {
                boolean check = DataList.get(i).isCheck();
                boolean update = DataList.get(i).isNeedUpdate();
                if (check && update) {
                    return true;
                }
            }
        }
        return false;
    }


    //是否全选
    public void setChoseAll(boolean isAll) {
        if (DataList != null && DataList.size() > 0) {
            for (int i = 0; i < DataList.size(); i++) {
                DataList.get(i).setCheck(isAll);
            }
            notifyDataSetChanged();
        }
        isChose = getChose();
    }

    //设置选中
    public void setChoseOne(int position) {
        if (DataList != null && DataList.size() > 0 && position < DataList.size()) {
            DataList.get(position).setCheck(!DataList.get(position).isCheck());
            notifyDataSetChanged();
        }
        isChose = getChose();
    }

    //是否有选择的
    public boolean getChose() {
        if (DataList != null && DataList.size() > 0) {
            for (int i = 0; i < DataList.size(); i++) {
                boolean check = DataList.get(i).isCheck();
                if (check) {
                    return true;
                }
            }
        }
        return false;
    }

    //更新标题
    public void setUpName(String resourceName, String id) {
        if (DataList != null && DataList.size() > 0) {
            for (int i = 0; i < DataList.size(); i++) {
                ResourceBean item = DataList.get(i);
                if (item.getResourceId().equals(id)) {
                    if (!TextUtils.isEmpty(resourceName))
                        item.setResourceTitle(resourceName);
                }
            }
        }
    }

    //更新进度
    public void refreshDownLoadSize(String id, String s) {
        if (DataList != null && DataList.size() > 0) {
            for (int i = 0; i < DataList.size(); i++) {
                ResourceBean item = DataList.get(i);
                if (item.getResourceId().equals(id)) {
                    item.setDownloadOk(id.equals("100"));
                    item.setNeedUpdate(!id.equals("100"));
                    item.setResourceDownloadSize(s);
                    DataList.set(i, item);
                    Log.e("DownLoad", s + " 更新进度  resouceId-->" + id);
                }
            }
            notifyDataSetChanged();
        }
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
//        holder.book_upload.setTag(position);

        ResourceBean bean = DataList.get(position);
        holder.Name.setText(bean.getResourceTitle());
        //时间
        if (TextUtils.isEmpty(bean.getResourceTime())) {
            holder.ShijianLine.setVisibility(View.GONE);
        } else {
            holder.ShijianLine.setVisibility(View.VISIBLE);
            DataUtils getData = new DataUtils();
            holder.Shijian.setText(getData.toTimeStr(Integer.parseInt(bean.getResourceTime())));
        }
        //大小
        if (TextUtils.isEmpty(bean.getResourceSize())) {
            holder.DaxiaoLine.setVisibility(View.GONE);
        } else {
            holder.DaxiaoLine.setVisibility(View.VISIBLE);
            DateUtils dateUtils = new DateUtils();
            holder.Daxiao.setText(dateUtils.formetFileSize(Double.parseDouble(bean.getResourceSize())));
        }
        //不是选择状态
        if (!isChose) {
            if (bean.isDownloadOk()) {
                holder.check.setVisibility(View.VISIBLE);
                holder.downloading.setVisibility(View.GONE);
                holder.check.setImageResource(R.drawable.xuan3);
                holder.circleProgressBar.setVisibility(View.GONE);
//                if (bean.isNeedUpdate()) {
//                    holder.book_upload.setVisibility(View.VISIBLE);
//                } else {
//                    holder.book_upload.setVisibility(View.GONE);
//                }
            } else {
                holder.check.setVisibility(View.GONE);
//                holder.book_upload.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(bean.getResourceDownloadSize())) {
                    holder.downloading.setVisibility(View.GONE);
                    holder.circleProgressBar.setVisibility(View.VISIBLE);
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
        } else {//选择状态
            holder.check.setVisibility(View.VISIBLE);
            holder.downloading.setVisibility(View.GONE);
//            holder.book_upload.setVisibility(View.GONE);
            holder.circleProgressBar.setVisibility(View.GONE);
        }

        if (bean.isNeedUpdate()) {
            holder.book_upload.setVisibility(View.VISIBLE);
        } else {
            holder.book_upload.setVisibility(View.GONE);
        }

        if (bean.isCheck()) {
            holder.check.setImageResource(R.drawable.xuan_r2);
        } else {
            holder.check.setImageResource(R.drawable.xuan3);
        }
        holder.check.setOnClickListener(this);
        holder.rlCardItem.setOnClickListener(this);
//        holder.book_upload.setOnClickListener(this);
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
                    mItemClickListener.onItemClick(v, (Integer) v.getTag());
                }
                break;
            case R.id.rl_card_item:
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(v, (Integer) v.getTag());
                }
                break;
//            case R.id.book_upload:
//                if (mItemClickListener != null) {
//                    mItemClickListener.onItemClick(v, (Integer) v.getTag());
//                }
//                break;
        }

    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

}
