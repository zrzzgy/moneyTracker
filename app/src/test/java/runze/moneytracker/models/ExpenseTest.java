package runze.moneytracker.models;

import junit.framework.TestCase;

import java.util.Date;
import java.util.HashSet;

public class ExpenseTest extends TestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetCategory() {
        HashSet<String> category = new HashSet<>();
        Expense expense = new Expense(category,null,null,null);

        assertEquals(category, expense.getCategory());
    }

    public void testGetAmount() {
        Double amount = new Double(2.3);
        Expense expense = new Expense(null,amount,null,null);

        assertEquals(amount, expense.getAmount());
    }

    public void testGetDate() {
        Date date= new Date(0);
        Expense expense = new Expense(null,null,date,null);

        assertEquals(date, expense.getDate());
    }

    public void testGetDescription() {
        String description = new String("");
        Expense expense = new Expense(null,null,null,description);

        assertEquals(description, expense.getDescription());
    }

    public void testSetCategory() {
        Expense expense = new Expense(null,null,null,null);
        HashSet<String> category = new HashSet<>();
        expense.setCategory(category);

        assertEquals(category, expense.getCategory());
    }

    public void testSetAmount() {
        Expense expense = new Expense(null,null,null,null);
        Double amount = new Double(2.2);
        expense.setAmount(amount);

        assertEquals(amount, expense.getAmount());
    }

    public void testSetDescription() {
        Expense expense = new Expense(null,null,null,null);
        String description = new String("");
        expense.setDescription(description);

        assertEquals(description, expense.getDescription());
    }

    public void testIsSameExpense() {
        HashSet<String> category = new HashSet<>();
        Double amount = 1.1;
        Date date = new Date(0);
        Expense expense1 = new Expense(category,amount,date,null);
        Expense expense2 = new Expense(category,amount,date,null);

        assertTrue(expense1.isSameExpense(expense2));
    }

    public void testToString() {
        Double amount = new Double(6.6);
        HashSet<String> category = new HashSet<>();
        category.add("Food");
        Date date = new Date(0);
        Expense expense = new Expense(category,amount,date,null);

        assertEquals("Amount: " + amount.toString() + "\n" +
                "Category: " + category.toString() + "\n" +
                "Date: " + date.toString(), expense.toString());
    }
}
