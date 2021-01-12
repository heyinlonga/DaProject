package software.ecenter.study.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import software.ecenter.study.Interface.OnClickItemListener;
import software.ecenter.study.Interface.OnItemListener;
import software.ecenter.study.R;
import software.ecenter.study.bean.ReportList;
import software.ecenter.study.utils.ToolUtil;

/**
 * Message  学情报告
 * Create by Administrator
 * Create by 2019/10/31
 */
public class StudyReportAdapter extends RecyclerView.Adapter<StudyReportAdapter.ViewHolder> {
    public Context mContext;
    public List<ReportList.DataBean> mList = new ArrayList<>();
    public OnClickItemListener listener;

    public StudyReportAdapter(Context mContext, OnClickItemListener listener) {
        this.mContext = mContext;
        this.listener = listener;
    }

    public ReportList.DataBean getChose(int poc) {
        if (ToolUtil.isList(mList) && poc < mList.size()) {
            return mList.get(poc);
        }
        return null;
    }

    public void setData(List<ReportList.DataBean> list) {
        mList.clear();
        if (ToolUtil.isList(list)) {
            mList.addAll(list);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_studyreport, viewGroup, false);
        return new ViewHolder(inflate);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {
        ReportList.DataBean bean = mList.get(i);
        if (bean != null) {
            holder.tv_name.setText(ToolUtil.getString(bean.getTitle()));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
        }
    }
}
