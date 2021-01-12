package software.ecenter.study.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import software.ecenter.study.R;
import software.ecenter.study.View.TextHtml.LinkMovementMethodExt;
import software.ecenter.study.View.TextHtml.MImageGetter;
import software.ecenter.study.View.TextHtml.MessageSpan;
import software.ecenter.study.activity.BigPicActivity;
import software.ecenter.study.bean.HomeBean.ExerciseBean;

/**
 * Created by Mike on 2018/4/25.
 */

public class ExerciseExerciseAdapter extends RecyclerView.Adapter<ExerciseExerciseAdapter.ViewHolder> implements View.OnClickListener{
    private List<ExerciseBean> DataList;
    private Context mContext;
    private OnItemClickListener mItemClickListener;
    private OnItemBtnAddClickListener mItemBtnAddClickListener;
    private boolean showAddBtn;//是否显示加入习题按钮

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
                    }else{

                    }
                }
            }
        }
    };


    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView exerciseTip;
        WebView exerciseTitle;
        RecyclerView answerRecyclerView;
        ImageView answerYourImg;
        TextView answerYourText;
        TextView answerRightText;
        WebView  exerciseJiexi;
        Button btnAdd;

        public ViewHolder(View itemView) {
            super(itemView);
             exerciseTip=itemView.findViewById(R.id.exercise_tip);
             exerciseTitle=itemView.findViewById(R.id.exercise_title);
             answerRecyclerView=itemView.findViewById(R.id.list_answer);
             answerYourImg=itemView.findViewById(R.id.answer_your_img);
             answerYourText=itemView.findViewById(R.id.answer_your_text);
             answerRightText=itemView.findViewById(R.id.answer_right_text);
             exerciseJiexi=itemView.findViewById(R.id.exercise_jiexi);
             btnAdd = itemView.findViewById(R.id.btn_exercise_add);

            GridLayoutManager manager = new GridLayoutManager(itemView.getContext(), 1);
            manager.setAutoMeasureEnabled(true);//设置setAutoMeasureEnabled(true)成自适应高度***
            answerRecyclerView.setLayoutManager(manager);

        }
    }


    public void lookPic(String url) {
        Intent intent = new Intent(mContext, BigPicActivity.class);
        intent.putExtra("ImageUrl", url);
        mContext.startActivity(intent);

    }
    /**
     * 用于把要展示的数据源传进来，并赋值给一个全局变量mFruitList，我们后续的操作都将在这个数据源的基础上进行。
     * @param DataList
     */
    public ExerciseExerciseAdapter(Context mContext, List<ExerciseBean> DataList,boolean showAddBtn) {
        this.DataList = DataList;
        this.mContext = mContext;
        this.showAddBtn =showAddBtn;

    }

    /**
     * 用于创建ViewHolder实例的，并把加载出来的布局传入到构造函数当中，最后将viewholder的实例返回
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_exercise_exercise_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
//        viewHolder.exerciseTitle.setMovementMethod(LinkMovementMethodExt.getInstance(handler, ImageSpan.class));
//        viewHolder.exerciseJiexi.setMovementMethod(LinkMovementMethodExt.getInstance(handler, ImageSpan.class));


        return viewHolder;
    }

    /**
     * 用于对RecyclerView子项的数据进行赋值的，会在每个子项被滚动到屏幕内的时候执行，这里我们通过
     * position参数得到当前项的Fruit实例，然后再将数据设置到ViewHolder的Imageview和textview当中即可，
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.itemView.setTag(position);
        ExerciseBean item = DataList.get(position);
        holder.exerciseTip.setText("问题"+(position+1));

        holder.answerYourText.setText("您的回答："+item.getYourAnswerShow());
        holder.answerRightText.setText("正确答案：" + item.getRightAnswerShow());
        if(item.getRightAnswer().equals(item.getYourAnswer())){
            holder.answerYourImg.setImageResource(R.drawable.zhengque);
            holder.answerYourText.setTextColor(mContext.getResources().getColor(R.color.texterrorColor));

        }else{
            holder.answerYourImg.setImageResource(R.drawable.cuowu);
            holder.answerYourText.setTextColor(mContext.getResources().getColor(R.color.textrightColor));


        }
//        holder.exerciseTitle.setText( Html.fromHtml(item.getExerciseTitle(), new MImageGetter(holder.exerciseTitle, mContext), null));
        showWebViewByContent(holder.exerciseTitle,item.getExerciseTitle());
        showWebViewByContent(holder.exerciseJiexi,item.getExerciseAnalysis());
//        holder.exerciseJiexi.setText( Html.fromHtml(item.getExerciseAnalysis(), new MImageGetter(holder.exerciseJiexi, mContext), null));

        if(holder.answerRecyclerView.getAdapter()==null) {
            holder.answerRecyclerView.setAdapter(new ExerciseAnswerAdapter(mContext,item.getExerciseAnswer(),1));
        }/*else {
            holder.mRecyclerView.getAdapter().notifyDataSetChanged();
        }*/
        holder.btnAdd.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(mItemBtnAddClickListener!=null){
                    mItemBtnAddClickListener.onItemBtnAddClick(position);
                }
            }
        });

        if(showAddBtn){
            holder.btnAdd.setVisibility(View.VISIBLE);
        }else {
            holder.btnAdd.setVisibility(View.GONE);
        }



    }



    @Override
    public int getItemCount() {
        return DataList.size();
    }

    @Override
    public void onClick(View v) {
        if (mItemClickListener!=null){
            mItemClickListener.onItemClick((Integer) v.getTag());
        }
    }



    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public interface OnItemBtnAddClickListener{
        void onItemBtnAddClick(int position);
    }

    public void setmItemBtnAddClickListener(OnItemBtnAddClickListener mItemBtnAddClickListener) {
        this.mItemBtnAddClickListener = mItemBtnAddClickListener;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }


    private void showWebViewByContent(WebView webView, String content) {
        webView.loadDataWithBaseURL("", content, "text/html", "utf-8", null);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JsCallJavaObj() {
            @JavascriptInterface
            @Override
            public void showBigImg(String url) {
                lookPic(url);
            }
        }, "jsCallJavaObj");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                setWebImageClick(view);
            }
        });
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
