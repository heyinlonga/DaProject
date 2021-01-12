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

import software.ecenter.study.R;
import software.ecenter.study.bean.TypeBean;

/**
 * Message  备选评论
 * Create by Administrator
 * Create by 2019/6/22
 */
public class TypeCommentAdapter extends RecyclerView.Adapter<TypeCommentAdapter.ViewHolder> {
    private Context context;
    private OnClick onClick;
    private List<TypeBean> mList = new ArrayList<>();

    public TypeCommentAdapter(Context context,OnClick onClick) {
        this.context = context;
        this.onClick = onClick;
    }
    public TypeBean getChoseData(int pos){
        if (mList!=null && mList.size()>0 && pos < mList.size()){
            return mList.get(pos);
        }
        return null;
    }

    public void setNoChose() {
        if (mList!=null && mList.size()>0 ){
            for (int i = 0; i < mList.size(); i++) {
                mList.get(i).setSelect(false);
            }
            notifyDataSetChanged();
        }
    }

    public interface OnClick{
        void OnClick(int poc);
    }
    public void setData(List<TypeBean> list) {
        mList.clear();
        if (list != null && list.size() > 0) {
            mList.addAll(list);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_tyepcomment, viewGroup, false);
        return new ViewHolder(inflate);
    }
    //是否有选中
    public boolean isChose() {
        if (mList != null && mList.size() > 0) {
            for (int i = 0; i <mList.size(); i++) {
                boolean select = mList.get(i).isSelect();
                if (select) return true;
            }
        }
        return false;
    }

    public void setChose(int poc) {
        if (mList != null && mList.size() > 0 && poc < mList.size()) {
            for (int i = 0; i < mList.size(); i++) {
                mList.get(i).setSelect(i == poc);
            }
            notifyDataSetChanged();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        TypeBean bean = mList.get(i);
        if (bean != null) {
            String s = bean.getName();
            if (!TextUtils.isEmpty(s)) {
                viewHolder.tv_name.setText(s);
            } else {
                viewHolder.tv_name.setText("");
            }
            if (bean.isSelect()) {
                viewHolder.ll_bg.setBackground(context.getResources().getDrawable(R.drawable.shape_select_click));
                viewHolder.tv_name.setTextColor(context.getResources().getColor(R.color.white));
            } else {
                viewHolder.ll_bg.setBackground(context.getResources().getDrawable(R.drawable.shape_select_nor));
                viewHolder.tv_name.setTextColor(context.getResources().getColor(R.color.textHoldColor));
            }
        }
        viewHolder.ll_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setChose(i);
                if (onClick!=null){
                    onClick.OnClick(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_name;
        private final LinearLayout ll_bg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            ll_bg = itemView.findViewById(R.id.ll_bg);
        }
    }
}
