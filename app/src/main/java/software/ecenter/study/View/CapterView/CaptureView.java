package software.ecenter.study.View.CapterView;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import software.ecenter.study.R;
import software.ecenter.study.View.StrokeTextView;

/**@author yanglei*/
public class CaptureView extends LinearLayout
{

	LayoutInflater inflater = null;
	private Context content;
	private LinearLayout patent = null;

	private LunarManager lunarManager;
	private Children children;
	private CaptureeAdapter adapter;
	private int select_day; //选择日期的天
	private int select_year;
	private int select_month;

	private RecyclerView mRecyclerView;

	private StrokeTextView timeTitle;


	public CaptureView(Context context)
	{
		super(context);

		init(context);
	}

	public CaptureView(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		init(context);

	}

	public void init(Context context)
	{
		this.content = context;
		this.inflater = LayoutInflater.from(content);
		patent = (LinearLayout) inflater.inflate(R.layout.captureview, this,
				true);
		timeTitle = (StrokeTextView) patent.findViewById(R.id.time_title);
	}

	public void setTitle(String text){
		timeTitle.setText(text);
	}

	public void install(){
		mRecyclerView= findViewById(R.id.rili_recyclerview);
		resume();
		lunarManager = new LunarManager();
		children = lunarManager.calendar(select_year,
				select_month,select_day);

		GridLayoutManager manager = new GridLayoutManager(content,7);
		mRecyclerView.setLayoutManager(manager);
		adapter = new CaptureeAdapter(content,children.getDayList());
		mRecyclerView.setAdapter(adapter);
	}
	public void resume(){
		Calendar time = Calendar.getInstance();
		select_year = time.get(Calendar.YEAR);
		select_month = time.get(Calendar.MONTH);
		select_day =  time.get(Calendar.DAY_OF_MONTH);

	}
	public void refresh(List<String> signList) throws ParseException {
		for(calElement item:children.getDayList()){
			for(String day:signList){
				Calendar cal = Calendar.getInstance();
				DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
				Date myDate1 = dateFormat1.parse(day);
				cal.setTime(myDate1);
				if(cal.get(Calendar.DATE)==item.getsDay())
				{
					item.setSign(true);
				}
			}
		}
		adapter.notifyDataSetChanged();
	}


}
