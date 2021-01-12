package software.ecenter.study.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.PictureSelector;

import java.util.ArrayList;
import java.util.List;

import software.ecenter.study.R;
import software.ecenter.study.bean.TiBean;
import software.ecenter.study.utils.ToolUtil;

/**
 * Message 我的题目
 * Create by Administrator
 * Create by 2019/10/31
 */
public class MyTiAdapter extends RecyclerView.Adapter<MyTiAdapter.ViewHolder> {
    public Context mContext;
    public List<TiBean> mList = new ArrayList<>();

    public MyTiAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<TiBean> list) {
        mList.clear();
        if (ToolUtil.isList(list)) {
            mList.addAll(list);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_myti, viewGroup, false);
        return new ViewHolder(inflate);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        final TiBean bean = mList.get(i);
        if (bean != null) {
            holder.tv_name.setText((i + 1) + "." + ToolUtil.getString(bean.getQuestion()));
            if (!TextUtils.isEmpty(bean.getOptionImgUrl())) {
                holder.iv_icon.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(bean.getOptionImgUrl()).into(holder.iv_icon);
            } else {
                holder.iv_icon.setVisibility(View.GONE);
            }
            holder.adapter.setData(bean.getOptions(), bean.getMyAnswer(), bean.isCorrect());
        }
        holder.iv_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean != null) {
                    PictureSelector.create((Activity) mContext)
                            .externalPicturePreview(0, "study", ToolUtil.getPicData(ToolUtil.getString(bean.getOptionImgUrl())));
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
        private final ImageView iv_icon;
        private final RecyclerView rv_anwer;
        private final AnwerAdapter adapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            rv_anwer = itemView.findViewById(R.id.rv_anwer);
            rv_anwer.setLayoutManager(new LinearLayoutManager(mContext));
            adapter = new AnwerAdapter(mContext, 1);
            rv_anwer.setAdapter(adapter);
        }
    }
}
