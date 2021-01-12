package software.ecenter.study.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import software.ecenter.study.R;
import software.ecenter.study.View.TextHtml.LinkMovementMethodExt;
import software.ecenter.study.View.TextHtml.MImageGetter;
import software.ecenter.study.View.TextHtml.MessageSpan;
import software.ecenter.study.activity.BigPicActivity;
import software.ecenter.study.activity.ExerciseActivity;
import software.ecenter.study.bean.HomeBean.ExerciseAnswerBean;
import software.ecenter.study.utils.MyWebViewClient;


/**
 * Created by Mike on 2018/4/25.
 */

public class ExerciseAnswerAdapter extends RecyclerView.Adapter<ExerciseAnswerAdapter.ViewHolder> implements View.OnClickListener, View.OnLongClickListener {
    private List<ExerciseAnswerBean> DataList;
    private Context mContext;
    private OnItemClickListener mItemClickListener;
    private OnLongClickListener mOnLongClickListener;

    private int type; //0是选择答题  1 是展示答案

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            int what = msg.what;
            if (what == 200) {
                MessageSpan ms = (MessageSpan) msg.obj;
                Object[] spans = (Object[]) ms.getObj();
                // final ArrayList<String> list = new ArrayList<>();
                for (Object span : spans) {
                    if (span instanceof ImageSpan) {
                        // Log.i("picUrl==", ((ImageSpan) span).getSource());
                        lookPic(((ImageSpan) span).getSource());
                    } else {

                    }
                }
            }
        }
    };


    static class ViewHolder extends RecyclerView.ViewHolder {

        WebView Name;
        RelativeLayout BackGroudRl;
        ImageView check;

        public ViewHolder(View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.answer_name);
            check = (ImageView) itemView.findViewById(R.id.check_img);
            BackGroudRl = itemView.findViewById(R.id.answer_back_Rl);


        }
    }

    /**
     * 用于把要展示的数据源传进来，并赋值给一个全局变量mFruitList，我们后续的操作都将在这个数据源的基础上进行。
     *
     * @param DataList
     */
    public ExerciseAnswerAdapter(Context mContext, List<ExerciseAnswerBean> DataList, int type) {
        this.DataList = DataList;
        this.mContext = mContext;
        this.type = type;

    }

    public void refreshData(List<ExerciseAnswerBean> DataList, int type) {
        this.DataList = DataList;
        this.type = type;
        notifyDataSetChanged();

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_exercise_answer_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
//        viewHolder.Name.setMovementMethod(LinkMovementMethodExt.getInstance(handler, ImageSpan.class));

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
        ExerciseAnswerBean bean = DataList.get(position);
        showWebViewByContent(holder.Name,bean.getAnswerContent());
//        holder.Name.setText(Html.fromHtml(bean.getAnswerContent(), new MImageGetter(holder.Name, mContext), null));
        //showWebViewByContent(holder.Name, bean.getAnswerContent());
        if (0 == type) {
           holder.itemView.findViewById(R.id.tip_temp).setVisibility(View.VISIBLE);
            if (bean.isAnswerIsRight()) {
                holder.check.setBackgroundResource(R.drawable.xuanzhong_1);
                holder.BackGroudRl.setBackgroundResource(R.drawable.background_shape_circle_stoke_blue);

            } else {
                holder.check.setBackgroundResource(R.drawable.xuan_r1);
                holder.BackGroudRl.setBackgroundResource(R.drawable.background_shape_circle_stoke_red);
            }
        } else {
            holder.itemView.findViewById(R.id.tip_temp).setVisibility(View.GONE);
            if (bean.isAnswerIsRight()) {
                holder.BackGroudRl.setBackgroundResource(R.drawable.background_shape_circle_stoke_green);
                holder.Name.setBackgroundColor(0);
                holder.Name.getBackground().setAlpha(0);
            } else {
                holder.BackGroudRl.setBackgroundResource(R.drawable.background_shape_circle_stoke_red);
                holder.Name.setBackgroundColor(0);
                holder.Name.getBackground().setAlpha(0);
            }
        }
    }


    @Override
    public int getItemCount() {
        return DataList.size();
    }

    @Override
    public void onClick(View v) {
        if (mItemClickListener != null) {
            mItemClickListener.onItemClick((Integer) v.getTag());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mOnLongClickListener != null) {
            mOnLongClickListener.onLongClick((Integer) v.getTag());
        }
        return true;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnLongClickListener {
        void onLongClick(int position);
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public void setOnLongClickListener(OnLongClickListener listener) {
        mOnLongClickListener = listener;
    }

    private void showWebViewByContent(WebView webView, String content) {
        webView.loadDataWithBaseURL("", content, "text/html", "utf-8", null);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());
        webView.addJavascriptInterface(new JsCallJavaObj() {
            @JavascriptInterface
            @Override
            public void showBigImg(String url) {
                lookPic(url);
            }
        }, "jsCallJavaObj");
//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                setWebImageClick(view);
//            }
//        });
    }

    public void lookPic(String url) {

        Intent intent = new Intent(mContext, BigPicActivity.class);
        intent.putExtra("ImageUrl", url);
        mContext.startActivity(intent);

    }

    /**
     * 設置網頁中圖片的點擊事件
     *
     * @param view
     */
    private void setWebImageClick(WebView view) {
        String jsCode = "javascript:(function(){" +
                "var imgs=document.getElementsByTagName(\"img\");" +
                "for(var i=0;i<imgs.length;i++){" +
                "imgs[i].onclick=function(){" +
                "window.jsCallJavaObj.showBigImg(this.src);" +
                "}}})()";
        view.loadUrl(jsCode);
    }

    /**
     * Js調用Java接口
     */
    private interface JsCallJavaObj {
        void showBigImg(String url);
    }
}
