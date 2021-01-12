package software.ecenter.study.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import software.ecenter.study.Interface.OnClickItemListener;
import software.ecenter.study.R;
import software.ecenter.study.bean.CateBean;
import software.ecenter.study.utils.ToastUtils;
import software.ecenter.study.utils.ToolUtil;

/**
 * Message 整本书阅读  章列表
 * Create by Administrator
 * Create by 2019/8/15
 */
public class WholeBookOneAdapter extends RecyclerView.Adapter<WholeBookOneAdapter.ViewHolder> {
    private Context context;
    private int type;//1：显示到章，2：显示到节
    private List<CateBean> mList = new ArrayList<>();
    private OnClickItemListener listener;

    public WholeBookOneAdapter(Context context, OnClickItemListener listener) {
        this.context = context;
        this.listener = listener;
    }

    //获取数据
    public CateBean getChoseData(int pos) {
        if (ToolUtil.isList(mList) && pos < mList.size()) {
            return mList.get(pos);
        }
        return new CateBean();
    }

    public void setType(int type) {
        this.type = type;
    }

    //更新数据
    public void setData(List<CateBean> list) {
        mList.clear();
        if (list != null && list.size() > 0) {
            mList.addAll(list);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_whole_one, viewGroup, false);
        return new ViewHolder(inflate);
    }

    //隐藏
    public void setHide(int pos) {
        if (mList != null && pos < mList.size()) {
            mList.get(pos).setHide(!mList.get(pos).isHide());
            notifyDataSetChanged();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {
        CateBean cateBean = mList.get(i);
        if (cateBean != null) {
            holder.tv_name.setText(cateBean.getName());
            if (cateBean.isNoFile()) {
                if (type == 1) {
                    holder.ll_data.setBackgroundColor(context.getResources().getColor(R.color.color_afcccf));
                } else {
                    holder.ll_data.setBackgroundColor(context.getResources().getColor(R.color.color_b37070));
                }
            } else {
                holder.ll_data.setBackgroundColor(context.getResources().getColor(R.color.cardviewback));
            }
            if (type == 1) {//章
                holder.tv_name.setTextColor(context.getResources().getColor(R.color.textHoldColor));
                holder.iv_state.setVisibility(View.GONE);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onItemClick(i);
                        }
                    }
                });
            } else {//节
                holder.tv_name.setTextColor(context.getResources().getColor(R.color.color_F77450));
                holder.iv_state.setVisibility(View.VISIBLE);
                holder.adapter.setData(i, cateBean.getChildren());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<CateBean> children = mList.get(i).getChildren();
                        if (children != null && children.size() > 0) {
                            setHide(i);
                        } else {
                            ToastUtils.showToastSHORT(context, "资源即将上传，敬请期待");
                        }
                    }
                });
                if (cateBean.isHide()) {
                    holder.iv_state.setImageResource(R.drawable.shangla);
                    holder.rv_list.setVisibility(View.GONE);
                } else {
                    holder.rv_list.setVisibility(View.VISIBLE);
                    holder.iv_state.setImageResource(R.drawable.xiala);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout ll_data;
        private final CardView cd_view;
        private final TextView tv_name;
        private final ImageView iv_state;
        private final RecyclerView rv_list;
        private final WholeBookChildAdapter adapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ll_data = itemView.findViewById(R.id.ll_data);
            cd_view = itemView.findViewById(R.id.cd_view);
            tv_name = itemView.findViewById(R.id.tv_name);
            iv_state = itemView.findViewById(R.id.iv_state);
            rv_list = itemView.findViewById(R.id.rv_list);
            rv_list.setLayoutManager(new LinearLayoutManager(context) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
            adapter = new WholeBookChildAdapter(context, listener);
            rv_list.setAdapter(adapter);
        }
    }
}
