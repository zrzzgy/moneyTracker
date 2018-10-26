package runze.moneytracker.models;

import junit.framework.TestCase;

import java.util.Date;

public class DailyExpenseTotalTest extends TestCase {

    /**
     * This runs before each test is run
     * @throws Exception e
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * This runs after each test is run
     *  e.g.
     * Must be run if shared preferences is changed
     * @throws Exception e
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     *  Unit test
     */
    public void testGetTotalAmount(){
        DailyExpenseTotal dailyExpenseTotal = new DailyExpenseTotal((double) 1,new Date(0));
        assertEquals((double) 1, dailyExpenseTotal.getTotalAmount());

        dailyExpenseTotal.setTotalAmount(1.1);
        assertEquals(1.1, dailyExpenseTotal.getTotalAmount());

    }
}
