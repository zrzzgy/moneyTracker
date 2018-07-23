package runze.moneytracker.models;

import java.util.Date;
import java.util.HashSet;

/**
 * Object that saves info about a spending
 */

public class Expense {
    private HashSet<String> mCategories;
    private Double mAmount;
    private Date mDate;
    private String mDescription;

    public Expense(HashSet<String> category, Double amount, Date date, String description){
        mCategories = category;
        mAmount = amount;
        mDate = date;
        mDescription = description;
    }

    public HashSet<String> getCategory() {
        return mCategories;
    }

    public void setCategory(HashSet<String> newCategory){
        mCategories = newCategory;
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

    public boolean isSameExpense(Expense expense){
        return this.getAmount().equals(expense.getAmount()) &&
                this.getCategory().equals(expense.getCategory()) &&
                this.getDescription().equals(expense.getDescription()) &&
                this.getDate().equals(expense.getDate());
    }

    @Override
    public String toString() {
        return "Amount: " + mAmount.toString() + "\n" +
                "Category: " + mCategories.toString() + "\n" +
                "Date: " + getDate().toString();
    }
}
