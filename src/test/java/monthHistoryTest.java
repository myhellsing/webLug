import luggage.AnalysisCalc;
import luggage.data.MonthHistory;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.util.*;

import static org.junit.Assert.assertEquals;


/**
 * Created by myhellsing on 06/02/17.
 */
public class monthHistoryTest {

    MonthHistory monthHistory;
    ArrayList<MonthHistory> monthHistories;
    Date dateFrom;
    Date dateTo;
    @Before
    public void loadMontHistorySeptember2016(){
        AnalysisCalc analysisCalc = new AnalysisCalc();
        monthHistories = analysisCalc.getMonthHistories();
        Collections.sort(monthHistories);
        for (MonthHistory m:monthHistories){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(m.date);
            if (calendar.get(Calendar.MONTH) ==8 && calendar.get(Calendar.YEAR)==2016 ){
                monthHistory=m;
            }
        }
        try {
            dateTo= analysisCalc.dateFormat.parse("01.2017");
            dateFrom= analysisCalc.dateFormat.parse("01.2016");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testHelloWorld(){
        String hello="HelloWorld";
        assertEquals("HelloWorld",hello);
    }

    @Test
    public void testBalanceAtBegin(){
        assertEquals(569483,monthHistory.balanceAtBegin,0.01);
    }
    @Test
    public void testGetCurrentBalance(){
        assertEquals(586216,monthHistory.getCurrentBalance(),0.01);
    }

    @Test
    public void testGetSummaryIncome(){
        assertEquals(380704,monthHistory.getSummaryIncome(),0.01);
    }
    @Test
    public void testGetSummaryOutcome(){
        assertEquals(363971,monthHistory.getSummaryOutcome(),0.01);
    }
    @Test
    public void testTransactionSize(){
        assertEquals(60,monthHistory.transactions.size());
    }

    @Test
    public void testAllIncomeByMonth(){
        ArrayList<Double> incomeExpected = new ArrayList<Double>(Arrays.asList
                (564521.0, 767124.0, 966720.0,380704.0,149604.0,358263.0,329427.0,629770.0,231543.0,276582.0,329106.0,137700.0));
        ArrayList<Double> incomeActual = new ArrayList<>();
        monthHistories.forEach(( m ) -> { if (m.date.after(dateFrom) && m.date.before(dateTo)) incomeActual.add(m.getSummaryIncome()); } );
        Collections.reverse(incomeActual);
        assertEquals(incomeExpected, incomeActual);

    }
    @Test
    public void testAllOutComeByMonth(){
        ArrayList<Double> outcomeExpected = new ArrayList<Double>(Arrays.asList
                (285724.0, 438368.0, 1176954.0,363971.0,1150741.0,170924.0,176384.0,300503.0,142771.0,516748.0,318299.0,430436.0));
        ArrayList<Double> outcomeActual = new ArrayList<>();
        monthHistories.forEach(( m ) -> { if (m.date.after(dateFrom) && m.date.before(dateTo)) outcomeActual.add(m.getSummaryOutcome()); } );
        Collections.reverse(outcomeActual);
        assertEquals(outcomeExpected,outcomeActual);

    }

}
