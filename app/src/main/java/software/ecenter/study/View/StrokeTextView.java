package software.ecenter.study.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

import software.ecenter.study.R;

/**
 * Created by Mike on 2018/5/4.
 * 实现方法是两个TextView叠加,只有描边的TextView为底,实体TextView叠加在上面
 */

public class StrokeTextView extends AppCompatTextView {

    private TextView borderText = null;///用于描边的TextView
    private int mOuterColor;
    private float strokeWidth;

    public StrokeTextView(Context context) {
        super(context);
        borderText = new TextView(context);
        init();
    }

    public StrokeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        borderText = new TextView(context,attrs);

        //获取对应的属性值  //获取自定义的XML属性名称
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.StrokeTextView);
        this.mOuterColor = a.getColor(R.styleable.StrokeTextView_outerColor,0xffffff);
        this.strokeWidth = a.getDimension(R.styleable.StrokeTextView_strokeWidth,2);
        init();
    }

    public StrokeTextView(Context context, AttributeSet attrs,
                          int defStyle) {
        super(context, attrs, defStyle);
        borderText = new TextView(context,attrs,defStyle);
        //获取对应的属性值  //获取自定义的XML属性名称
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.StrokeTextView);
        this.mOuterColor = a.getColor(R.styleable.StrokeTextView_outerColor,0xffffff);
        this.strokeWidth = a.getDimension(R.styleable.StrokeTextView_strokeWidth,2);
        init();
    }

    public void init(){
        TextPaint tp1 = borderText.getPaint();
        tp1.setStrokeWidth(strokeWidth);                                  //设置描边宽度
        tp1.setStyle(Paint.Style.STROKE);                             //对文字只描边
        borderText.setTextColor(mOuterColor);  //设置描边颜色
        borderText.setGravity(getGravity());
    }

    @Override
    public void setLayoutParams (ViewGroup.LayoutParams params){
        super.setLayoutParams(params);
        borderText.setLayoutParams(params);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        CharSequence tt = borderText.getText();

        //两个TextView上的文字必须一致
        if(tt== null || !tt.equals(this.getText())){
            borderText.setText(getText());
            this.postInvalidate();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        borderText.measure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onLayout (boolean changed, int left, int top, int right, int bottom){
        super.onLayout(changed, left, top, right, bottom);
        borderText.layout(left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        borderText.draw(canvas);
        super.onDraw(canvas);
    }

}