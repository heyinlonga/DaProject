package software.ecenter.study.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import software.ecenter.study.R;
import software.ecenter.study.utils.ToolUtil;

/**
 * Message 答案
 * Create by Administrator
 * Create by 2019/10/31
 */
public class AnwerAdapter extends RecyclerView.Adapter<AnwerAdapter.ViewHolder> {
    public Context mContext;
    public int type;
    public boolean isCorrect;
    public String myAnswer;
    public List<String> mList = new ArrayList<>();

    public AnwerAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public AnwerAdapter(Context mContext, int type) {
        this.mContext = mContext;
        this.type = type;
    }

    public void setData(List<String> list, String myAnswer, boolean isCorrect) {
        this.myAnswer = myAnswer;
        this.isCorrect = isCorrect;
        mList.clear();
        if (ToolUtil.isList(list)) {
            mList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void setData(List<String> list) {
        mList.clear();
        if (ToolUtil.isList(list)) {
            mList.addAll(list);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_anwer, viewGroup, false);
        return new ViewHolder(inflate);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        String str = ToolUtil.getString(mList.get(i));
        if (type == 1) {
            //是否是我的答案
            if (str.startsWith(ToolUtil.getString(myAnswer))) {
                SpannableStringBuilder builder = new SpannableStringBuilder(str);
                if (isCorrect) {
                    ForegroundColorSpan startColor = new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_48B63B));
                    builder.setSpan(startColor, 2, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    ForegroundColorSpan startColor = new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_FE0000));
                    builder.setSpan(startColor, 2, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                holder.tv_name.setText(builder);
            } else {
                holder.tv_name.setTextColor(mContext.getResources().getColor(R.color.color_C9AB79));
                holder.tv_name.setText(str);
            }
        } else {
            holder.tv_name.setText(str);
        }
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
