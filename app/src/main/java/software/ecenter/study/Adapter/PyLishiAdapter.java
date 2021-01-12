package software.ecenter.study.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.luck.picture.lib.tools.ToolbarUtil;

import java.util.ArrayList;
import java.util.List;

import software.ecenter.study.Interface.OnClickItemListener;
import software.ecenter.study.R;
import software.ecenter.study.bean.CommType;
import software.ecenter.study.bean.PinLishi;
import software.ecenter.study.utils.TimeUtil;
import software.ecenter.study.utils.ToolUtil;

/**
 * Message 拼音拼读历史
 * Create by Administrator
 * Create by 2019/11/8
 */
public class PyLishiAdapter extends RecyclerView.Adapter<PyLishiAdapter.Holder> {
    private Context mContext;
    private OnClickItemListener listener;
    private List<PinLishi.DataBean.Pindu> mList = new ArrayList<>();

    public PyLishiAdapter(Context mContext, OnClickItemListener listener) {
        this.mContext = mContext;
        this.listener = listener;
    }

    public void setData(List<PinLishi.DataBean.Pindu> list) {
        mList.clear();
        if (ToolUtil.isList(list)) {
            mList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void setMoreData(List<PinLishi.DataBean.Pindu> list) {
        if (ToolUtil.isList(list)) {
            mList.addAll(list);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.itme_lishi, viewGroup, false);
        return new Holder(inflate);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int i) {
        PinLishi.DataBean.Pindu bean = mList.get(i);
        if (bean != null) {
            String date = bean.getExerciseDate().split(" ")[0];
            String[] split = date.split("-");
            holder.tv_name.setText(ToolUtil.getString(bean.getResourceName()));
            holder.tv_year.setText(split[0] + "年");
            holder.tv_month.setText(split[1] + "月" + split[2] + "日");
            if (i == 0) {
                int currentYear = TimeUtil.getCurrentYear();//获取当前年份
                String mon = TimeUtil.getCurrentDate().split("-")[1]; //当前月份
                String day = TimeUtil.getCurrentDate().split("-")[2]; //当前日期
                if (currentYear == ToolUtil.getIntValue(split[0])) {
                    holder.tv_year.setVisibility(View.GONE);
                } else {
                    holder.tv_year.setVisibility(View.VISIBLE);
                }
                if (currentYear == ToolUtil.getIntValue(split[0])) {
                    if (ToolUtil.getIntValue(mon) == ToolUtil.getIntValue(split[1])) {
                        if (ToolUtil.getIntValue(day) == ToolUtil.getIntValue(split[2])) {
                            holder.tv_month.setText("今天");
                        }
                    }
                }
                holder.tv_month.setVisibility(View.VISIBLE);
                holder.iv_dian.setVisibility(View.VISIBLE);
            } else {
                String upDate = mList.get(i - 1).getExerciseDate().split(" ")[0];
                String[] upSplit = upDate.split("-");
                if (split[0].equals(upSplit[0])) {//和上一个年份相同
                    holder.tv_year.setVisibility(View.GONE);
                } else {
                    holder.tv_year.setVisibility(View.VISIBLE);
                }
                if (date.equals(upDate)) {//是否是同一天
                    holder.tv_month.setVisibility(View.GONE);
                    holder.iv_dian.setVisibility(View.INVISIBLE);
                } else {
                    holder.tv_month.setVisibility(View.VISIBLE);
                    holder.iv_dian.setVisibility(View.VISIBLE);
                }
            }
            String text = "音频" + bean.getAudioCount() + "个";
            SpannableStringBuilder builder = new SpannableStringBuilder(text);
            ForegroundColorSpan startColor = new ForegroundColorSpan(mContext.getResources().getColor(R.color.err_ef5350));
            builder.setSpan(startColor, 2, text.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tv_num.setText(builder);
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

    public String getChoseDataId(int poc) {
        if (ToolUtil.isList(mList) && poc < mList.size()) {
            return mList.get(poc).getId();
        }
        return "";
    }

    public String getChoseDataMonth(int poc) {
        if (ToolUtil.isList(mList) && poc < mList.size()) {
            return mList.get(poc).getExerciseDate();
        }
        return "";
    }


    public class Holder extends RecyclerView.ViewHolder {

        private final TextView tv_year;
        private final TextView tv_month;
        private final TextView tv_name;
        private final TextView tv_num;
        private final ImageView iv_dian;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tv_year = itemView.findViewById(R.id.tv_year);
            tv_month = itemView.findViewById(R.id.tv_month);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_num = itemView.findViewById(R.id.tv_num);
            iv_dian = itemView.findViewById(R.id.iv_dian);
        }
    }
}
