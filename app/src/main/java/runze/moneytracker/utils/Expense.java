package runze.moneytracker.utils;

import java.util.Date;

/**
 * Object that saves info about a spending
 */

public class Expense {
    private String mCategory;
    private Double mAmount;
    private Date mDate;

    public Expense(String category, Double amount, Date date){
        mCategory = category;
        mAmount = amount;
        mDate = date;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String newCategory){
        mCategory = newCategory;
    }

    public Double getAmount() {
        return mAmount;
    }

    public Date getDate() {
        return mDate;
    }
}
