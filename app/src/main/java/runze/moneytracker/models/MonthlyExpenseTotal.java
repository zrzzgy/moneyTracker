package runze.moneytracker.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MonthlyExpenseTotal extends BaseExpenseTotal {

    private String month;
    public MonthlyExpenseTotal(Double amount, Date date){
        super(amount, date);
        SimpleDateFormat df = new SimpleDateFormat("MM-yyyy", Locale.getDefault());
        month = df.format(mDate);

    }

    public String getMonth() {
        return month;
    }

}
