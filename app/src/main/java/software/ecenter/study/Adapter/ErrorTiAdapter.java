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
import android.webkit.WebView;
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
 * Message 错题
 * Create by Administrator
 * Create by 2019/10/31
 */
public class ErrorTiAdapter extends RecyclerView.Adapter<ErrorTiAdapter.ViewHolder> {
    public Context mContext;
    public List<TiBean> mList = new ArrayList<>();

    public ErrorTiAdapter(Context mContext) {
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
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_errorti, viewGroup, false);
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
            holder.adapter.setData(bean.getOptions());
            holder.tv_right.setText(ToolUtil.getString(bean.getAnswer()));
            holder.tv_meAnwer.setText(ToolUtil.getString(bean.getMyAnswer()));
            holder.tv_des.setText(ToolUtil.getString(bean.getAnswerExplanation(), "暂无"));
            String html = "<html><header>" + ToolUtil.css + "</header>" + ToolUtil.getString(bean.getAnswerExplanation(), "暂无") + "</html>";
            holder.web_des.loadDataWithBaseURL("", html, "text/html", "utf-8", null);
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

        private final WebView web_des;
        private final TextView tv_name;
        private final TextView tv_right;
        private final TextView tv_meAnwer;
        private final TextView tv_des;
        private final ImageView iv_icon;
        private final RecyclerView rv_anwer;
        private final AnwerAdapter adapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            web_des = itemView.findViewById(R.id.web_des);
            web_des.setBackgroundColor(0);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_right = itemView.findViewById(R.id.tv_right);
            tv_meAnwer = itemView.findViewById(R.id.tv_meAnwer);
            tv_des = itemView.findViewById(R.id.tv_des);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            rv_anwer = itemView.findViewById(R.id.rv_anwer);
            rv_anwer.setLayoutManager(new LinearLayoutManager(mContext));
            adapter = new AnwerAdapter(mContext);
            rv_anwer.setAdapter(adapter);
        }
    }
}
