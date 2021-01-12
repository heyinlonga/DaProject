package software.ecenter.study.View.CapterView;
import java.util.Calendar;
public class LunarManager
{


	/** 阳历每月的天数 ***/
	int[] solarMonth = new int[]
			{ 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

	// ==============================返回公历 y年某m+1月的天数
	public int solarDays(int y, int m)
	{
		if (m == 1)
			return (((y % 4 == 0) && (y % 100 != 0) || (y % 400 == 0)) ? 29
					: 28);
		else
			return (solarMonth[m]);
	}


	public Children calendar(int y, int m, int Today)
	{
		Calendar sDObj = Calendar.getInstance();
		Children child = new Children();
		child.setLength(solarDays(y, m)); // 公历当月天数
		sDObj.set(Calendar.DAY_OF_MONTH, 1);
		child.setFirstWeek(sDObj.get(Calendar.DAY_OF_WEEK)); // 公历当月1日星期几 星期日是第一天
		child.setYear(y);
		child.setMonth(m + 1);


		//把前面的空缺补上
		for (int i = 0; i < child.getFirstWeek() - 1; i++){
			calElement day = new calElement();
			child.getDayList().add(day);
		}

		// 本月
		for (int i = 0; i < child.getLength(); i++)
		{

			// sYear,sMonth,sDay,week,
			// lYear,lMonth,lDay,isLeap,
			// cYear,cMonth,cDay
			calElement day = new calElement(y, m + 1, i + 1);
			child.getDayList().add(day);
		}
/*
		if (Today != -1)
		{
			child.getDayList().get(Today - 1).setToday(true);
		}*/
		return child;
	}
}
