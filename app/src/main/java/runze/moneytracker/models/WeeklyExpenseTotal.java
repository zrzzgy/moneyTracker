package runze.moneytracker.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class WeeklyExpenseTotal extends BaseExpenseTotal{
    private ArrayList<Integer> mWeek = new ArrayList<>();
    public WeeklyExpenseTotal(Double amount, Date date){
        super(amount, date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(mDate);
        int weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
        int year = cal.get(Calendar.YEAR);
        ArrayList<Integer> weekAndYear = new ArrayList<>();
        weekAndYear.add(0, weekOfYear);
        weekAndYear.add(1,year);
        mWeek = weekAndYear;
    }


    public ArrayList<Integer> getWeek() {
        return mWeek;
    }

}
