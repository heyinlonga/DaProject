package software.ecenter.study.utils;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import software.ecenter.study.R;

/**
 * 保证整个应用都只有一个吐司
 */
public class ToastUtils {

    private static Toast toast;

    public static void showToastSHORT(Context context, String msg) {
        TextView mTextView = null;
        if (ToolUtil.isActivityActive((Activity) context) && !TextUtils.isEmpty(msg)) {
            if (toast == null) {
                toast = Toast.makeText(context, "", Toast.LENGTH_LONG);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                //自定义布局
                View view = inflater.inflate(R.layout.toast_view, null);
                //自定义toast文本
                mTextView = (TextView) view.findViewById(R.id.toast_text);
                mTextView.setText(msg);
                toast.setView(view);
                toast.setGravity(Gravity.CENTER, 0, 0);
            } else {
                mTextView = (TextView) toast.getView().findViewById(R.id.toast_text);
                mTextView.setText(msg);
            }
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    public static void showToastLONG(Context context, String msg) {
        TextView mTextView = null;
        if (ToolUtil.isActivityActive((Activity) context) && !TextUtils.isEmpty(msg)) {
            if (toast == null) {
                toast = Toast.makeText(context, "", Toast.LENGTH_LONG);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                //自定义布局
                View view = inflater.inflate(R.layout.toast_view, null);
                //自定义toast文本
                mTextView = (TextView) view.findViewById(R.id.toast_text);
                mTextView.setText(msg);
                toast.setView(view);
                toast.setGravity(Gravity.CENTER, 0, 0);
            } else {
                mTextView = (TextView) toast.getView().findViewById(R.id.toast_text);
                mTextView.setText(msg);
            }
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        }

    }

}
