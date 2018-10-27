package runze.moneytracker.models;

import junit.framework.TestCase;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class DataModelTest extends TestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetDailyTotals() {
        List<DailyExpenseTotal> dailyTotalList = new ArrayList<>();
        DataModel dataModel = new DataModel(null, dailyTotalList, null, null);

        assertEquals(dailyTotalList, dataModel.getDailyTotals());
    }

    public void testGetExpense() {
        List<Expense> expenseList = new ArrayList<>();
        DataModel dataModel = new DataModel(expenseList, null, null, null);

        assertEquals(expenseList, dataModel.getExpenses());
    }

    public void testGetColorList() {
        List<Integer> colorList = new ArrayList<>();
        DataModel dataModel = new DataModel(null, null, null, colorList);

        assertEquals(colorList, dataModel.getColorList());
    }

    public void testSetDailyTotalList() {
        List<DailyExpenseTotal> dailyExpenseTotalList = new ArrayList<>();
        DataModel dataModel = new DataModel(null,null,null,null);
        dataModel.setDailyTotalList(dailyExpenseTotalList);

        assertEquals(dailyExpenseTotalList, dataModel.getDailyTotals());
    }

    public void testSetExpenseList() {
        List<Expense> expenseList = new ArrayList<>();
        DataModel dataModel = new DataModel(null,null,null,null);
        dataModel.setExpenseList(expenseList);

        assertEquals(expenseList, dataModel.getExpenses());
    }

    public void testSetCategoryList() {
        HashSet<String> categoryList = new HashSet<>();
        DataModel dataModel = new DataModel(null,null,null,null);
        dataModel.setCategoryList(categoryList);

        assertEquals(categoryList, dataModel.getCategories());
    }

    public void testSetColorList() {
        List<Integer> colorList = new ArrayList<>();
        DataModel dataModel = new DataModel(null,null,null,null);
        dataModel.setColorList(colorList);

        assertEquals(colorList, dataModel.getColorList());
    }

}
