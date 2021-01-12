package software.ecenter.study.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import software.ecenter.study.Interface.OnClickItemListener;
import software.ecenter.study.R;
import software.ecenter.study.bean.CommType;
import software.ecenter.study.utils.ToolUtil;

/**
 * Message 比赛题
 * Create by Administrator
 * Create by 2019/11/1
 */
public class MatchTiAdapter extends RecyclerView.Adapter<MatchTiAdapter.ViewHolder> {
    private Context mContext;
    private List<CommType> mList = new ArrayList<>();
    private OnClickItemListener listener;

    public MatchTiAdapter(Context mContext, OnClickItemListener listener) {
        this.mContext = mContext;
        this.listener = listener;
    }

    public void setData(String myAnswer, List<String> list) {
        mList.clear();
        if (ToolUtil.isList(list)) {
            for (int i = 0; i < list.size(); i++) {
                boolean isHave = false;
                if (!TextUtils.isEmpty(myAnswer) && !TextUtils.isEmpty(list.get(i))) {
                    isHave = myAnswer.contains(list.get(i).substring(0, 1));
                }
                mList.add(new CommType(list.get(i), isHave));
            }
        }
        notifyDataSetChanged();
    }

    //设置选中
    public void setChose(int pos) {
        if (ToolUtil.isList(mList) && pos < mList.size()) {
            mList.get(pos).setSelect(!mList.get(pos).isSelect());
        }
        notifyDataSetChanged();
    }

    //获取选中的数据
    public String getChose() {
        StringBuilder str = new StringBuilder();
        if (ToolUtil.isList(mList)) {
            for (int i = 0; i < mList.size(); i++) {
                CommType commType = mList.get(i);
                if (commType != null) {
                    String name = commType.getName();
                    if (commType.isSelect() && !TextUtils.isEmpty(name)) {
                        String s = name.substring(0, 1);
                        str.append(s);
                        str.append(",");
                    }
                }
            }
        }
        if (str.length() > 0)
            str.deleteCharAt(str.length() - 1);
        return str.toString();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_matchti, viewGroup, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {
        CommType bean = mList.get(i);
        if (bean != null) {
            holder.tv_name.setText(ToolUtil.getString(bean.getName()));
            holder.tv_name.setSelected(!bean.isSelect());
            holder.ll_ti.setSelected(!bean.isSelect());
            holder.ll_ti.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(i);
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_name;
        private final LinearLayout ll_ti;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ll_ti = itemView.findViewById(R.id.ll_ti);
            tv_name = itemView.findViewById(R.id.tv_name);
        }
    }
}
