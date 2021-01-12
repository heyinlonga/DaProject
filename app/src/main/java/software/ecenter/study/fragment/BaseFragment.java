package software.ecenter.study.fragment;
import android.content.Context;
import android.view.View;

import androidx.fragment.app.Fragment;

import software.ecenter.study.View.LoadingDialog;
import software.ecenter.study.utils.NetworkUtil;
import software.ecenter.study.utils.ToastUtils;

/**
 * @desc:
 * @auth: 哎哎
 * @time: 2016/8/29
 * @fixer:
 * @fixtime:
 * @fixdesc:
 * @remarks:
 */
public abstract class BaseFragment extends Fragment {
    public Context mContext;
    public  String TAG;
    public View mRootView;
    public LoadingDialog mNetWatiDialog;
    public final int pageNum = 10;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        TAG = this.getClass().getSimpleName();
        mNetWatiDialog = new LoadingDialog(mContext);
    }

    public boolean showNetWaitLoding() {
        if(NetworkUtil.isConnected(mContext)){
            try {
                if (!mNetWatiDialog.isShowing()&&!getActivity().isFinishing()) {
                    mNetWatiDialog.show();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            return true;
        }else{
            ToastUtils.showToastLONG(mContext,"网络未连接");
            return false;
        }

    }

    public void dismissNetWaitLoging() {
        try {
            if (mNetWatiDialog.isShowing()) {
                if (!getActivity().isFinishing())
                    mNetWatiDialog.dismiss();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
