package runze.moneytracker.utils;

import java.util.Date;

/**
 * Object that saves info about a spending
 */

public class Expense {
    private String mCategory;
    private Double mAmount;
    private Date mDate;
    private String mDescription;

    public Expense(String category, Double amount, Date date, String description){
        mCategory = category;
        mAmount = amount;
        mDate = date;
        mDescription = description;
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

    public void setAmount(Double amount) {
        mAmount = amount;
    }

    public Date getDate() {
        return mDate;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }
}
