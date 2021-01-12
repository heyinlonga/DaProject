package software.ecenter.study.Interface;

/**
 * Message
 * Create by Administrator
 * Create by 2019/8/15
 */
public interface OnClickItemListener<T> {
    void onCancle();
    void onConfig();
    void onConfig(T t);
    void onPay();
    void onItemClick(int type ,int poc);
    void onItemClick(int poc);
    void onIndexClick(int index ,int poc);
    void onChildItemClick(int parentPoc,int poc);
}
