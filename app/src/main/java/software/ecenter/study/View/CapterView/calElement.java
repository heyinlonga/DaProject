package software.ecenter.study.View.CapterView;

public class calElement
{



	/**
	 *  阳历
	 * **/
	public calElement(int sYear,int sMonth,int sDay) {
		this.isSign = false;
		//瓣句
		this.sYear = sYear;    //公元年4位数字
		this.sMonth = sMonth;  //公元月数字
		this.sDay = sDay;      //公元日数字

	}
	public calElement() {
		this.isSign = false;
		this.isEmpty = true;
	}

	private boolean isSign;
	private boolean isEmpty;//空天数

	private int sYear ;    //公元年4位数字
	private int sMonth;  //公元月数字
	private int sDay;      //公元日数字

	public boolean isSign() {
		return isSign;
	}

	public void setSign(boolean sign) {
		isSign = sign;
	}

	public int getsYear() {
		return sYear;
	}

	public void setsYear(int sYear) {
		this.sYear = sYear;
	}

	public int getsMonth() {
		return sMonth;
	}

	public void setsMonth(int sMonth) {
		this.sMonth = sMonth;
	}

	public int getsDay() {
		return sDay;
	}

	public void setsDay(int sDay) {
		this.sDay = sDay;
	}

	public boolean isEmpty() {
		return isEmpty;
	}

	public void setEmpty(boolean empty) {
		isEmpty = empty;
	}
}
