package software.ecenter.study.Adapter;


import android.content.Context;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import software.ecenter.study.R;
import software.ecenter.study.View.treelist.Node;
import software.ecenter.study.View.treelist.TreeRecyclerAdapter;
import software.ecenter.study.utils.ToastUtils;

/**
 * Created by Mike on 2018/4/28.
 */

public class ChapterListAdapter extends TreeRecyclerAdapter {

    /**
     * 点击的回调接口
     */
    private OnTreeNodeClickListener onTreeNodeClickListener;

    public void setOnTreeNodeClickListener(OnTreeNodeClickListener onTreeNodeClickListener) {
        this.onTreeNodeClickListener = onTreeNodeClickListener;
    }

    /**
     * @param mTree
     * @param context
     * @param datas
     * @param defaultExpandLevel 默认展开几级树
     */
    public ChapterListAdapter(RecyclerView mTree, Context context, List datas, int defaultExpandLevel) throws IllegalArgumentException, IllegalAccessException {
        super(mTree, context, datas, defaultExpandLevel);
    }

    @Override
    public void onBindViewHolder(final Node node, RecyclerView.ViewHolder holder, final int position) {

        final MyHoder viewHolder = (MyHoder) holder;

        if (!node.isLeaf()) {
            viewHolder.item_R1.setVisibility(View.VISIBLE);
            viewHolder.item_R2.setVisibility(View.GONE);
            viewHolder.text_name_1.setText(node.getName());
            viewHolder.iv_suo1.setVisibility(node.isNewBatch()?View.VISIBLE:View.GONE);
            if (node.getIcon() == -1) {
                viewHolder.img1.setVisibility(View.INVISIBLE);
            } else {
                viewHolder.img1.setVisibility(View.VISIBLE);
                viewHolder.img1.setImageResource(node.getIcon());
            }
            if (node.getIsHaveFile() == 1) {
                viewHolder.item_R1.setCardBackgroundColor(mContext.getResources().getColor(R.color.d9d9));
                viewHolder.text_name_1.setTextColor(mContext.getResources().getColor(R.color.white));
            } else {
                viewHolder.item_R1.setCardBackgroundColor(mContext.getResources().getColor(R.color.cardviewback));
                viewHolder.text_name_1.setTextColor(mContext.getResources().getColor(R.color.textHoldColor));
            }

        } else {
            viewHolder.item_R1.setVisibility(View.GONE);
            viewHolder.item_R2.setVisibility(View.VISIBLE);
            viewHolder.text_name_2.setText(node.getName());
            viewHolder.iv_suo2.setVisibility(node.isNewBatch()?View.VISIBLE:View.GONE);
            if (node.getIsHaveFile() == 1) {
                viewHolder.item_R2.setCardBackgroundColor(mContext.getResources().getColor(R.color.d9d9));
                viewHolder.rlTreeItemR2.setBackgroundResource(R.drawable.background_shape_circle_stoke_d9);
                viewHolder.text_name_2.setTextColor(mContext.getResources().getColor(R.color.white));
                viewHolder.tvItemR2Look.setTextColor(mContext.getResources().getColor(R.color.white));
                viewHolder.ivYou.setImageDrawable(mContext.getResources().getDrawable(R.drawable.youbai));
            } else {
                viewHolder.item_R2.setCardBackgroundColor(mContext.getResources().getColor(R.color.white));
                viewHolder.rlTreeItemR2.setBackgroundResource(R.drawable.background_shape_circle_stoke);
                viewHolder.text_name_2.setTextColor(mContext.getResources().getColor(R.color.textHoldColor));
                viewHolder.tvItemR2Look.setTextColor(mContext.getResources().getColor(R.color.textColor));
                viewHolder.ivYou.setImageDrawable(mContext.getResources().getDrawable(R.drawable.gengduo));
            }
        }
        viewHolder.text_name_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onTreeNodeClickListener != null)
                    onTreeNodeClickListener.onClick((Node) mNodes.get(position), position, true);
            }
        });
        viewHolder.text_name_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onTreeNodeClickListener != null)
                    onTreeNodeClickListener.onClick((Node) mNodes.get(position), position, true);
            }
        });
        viewHolder.ll_cha_kan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onTreeNodeClickListener != null)
                    onTreeNodeClickListener.onClick((Node) mNodes.get(position), position, true);
            }
        });
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHoder(View.inflate(mContext, R.layout.list_tree_chapter, null));
    }

    class MyHoder extends RecyclerView.ViewHolder {

        public CardView item_R1;
        public CardView item_R2;
        public TextView text_name_1;
        public TextView text_name_2;
        public ImageView img1;
        public ImageView ivYou;

        private RelativeLayout rlTreeItemR2;
        public LinearLayout ll_cha_kan;
        public TextView tvItemR2Look;
        public ImageView iv_suo1;
        public ImageView iv_suo2;

        public MyHoder(View itemView) {
            super(itemView);
            item_R1 = itemView.findViewById(R.id.tree_item_r1);
            item_R2 = itemView.findViewById(R.id.tree_item_r2);
            rlTreeItemR2 = itemView.findViewById(R.id.rl_tree_item_r2);
            text_name_1 = itemView.findViewById(R.id.tree_item_name1);
            text_name_2 = itemView.findViewById(R.id.tree_item_name2);
            img1 = itemView.findViewById(R.id.tree_item_icon1);
            ivYou = itemView.findViewById(R.id.iv_you);
            tvItemR2Look = itemView.findViewById(R.id.tv_item_r2_look);
            ll_cha_kan = itemView.findViewById(R.id.ll_cha_kan);
            iv_suo1 = itemView.findViewById(R.id.iv_suo1);
            iv_suo2 = itemView.findViewById(R.id.iv_suo2);

        }

    }
}