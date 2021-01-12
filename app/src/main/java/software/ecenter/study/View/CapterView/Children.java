package software.ecenter.study.View.CapterView;


import java.util.ArrayList;

public class Children
{

	public Children(){
		dayList = new ArrayList<calElement>();
	}

	private ArrayList<calElement> dayList;
	private int length;       //公历当月天数
	private int firstWeek;    //公历当月1日星期几
	private int year;
	private int month;

	public ArrayList<calElement> getDayList() {
		return dayList;
	}

	public void setDayList(ArrayList<calElement> dayList) {
		this.dayList = dayList;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getFirstWeek() {
		return firstWeek;
	}

	public void setFirstWeek(int firstWeek) {
		this.firstWeek = firstWeek;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

}
