package runze.moneytracker.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Class that records date and the total amount of money spent on that date
 */
public class DailyExpenseTotal extends BaseExpenseTotal{

    public DailyExpenseTotal(Double amount, Date date){
        super(amount, date);
    }

    public String getDay() {
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
        return df.format(mDate);
    }

}
