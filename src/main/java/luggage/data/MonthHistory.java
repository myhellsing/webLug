package luggage.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Отчет за месяц, аналог WorksheetEntry =  один лист в Excel
 * Created by myhellsing on 24/05/16.
 */
public class MonthHistory  implements Serializable, Comparable {

    public Date date;
    public ArrayList<Transaction> transactions; //список трат
    public double balanceAtBegin;  //сумма на начало месяца
  //  public double SummaryIncome;     // итоговая сумма прихода за месяц
  //  public double SummaryOutcome;   // итоговая сумма расхода за месяц
  //  public double BalanceEnd;   // сумма на конец месяца, нужна для неучтенных расходов/приходов
    public HashMap<Category,Double> sumByCategory; // список категорий и общая сумма трат

    public MonthHistory(Date date) {
        this.date = date;
        this.transactions = new ArrayList<>();
        this.balanceAtBegin = 0;
    }

    public MonthHistory(Date date, double balanceBegin, ArrayList<Transaction> transactions, HashMap<Category, Double> sumByCategory) {
        this.date = date;
        this.transactions = transactions;
        this.balanceAtBegin = balanceBegin;
        this.sumByCategory = sumByCategory;
    }

    /**
     * Посчитаем текущий баланс: баланс на начало месяца - сумма расходов + сумма прихода
     * @return
     */
    public double getCurrentBalance(){
        return balanceAtBegin + getSummaryIncome()- getSummaryOutcome();

    }

    /**
     * Общая сумма прихода на текущий момент
     * @return
     */
    public double getSummaryIncome(){
        return  getSummaryByType(Transaction.TransactionType.INCOME);
    }

    /**
     * Общая сумма расхода на текущий момент
     * @return
     */
    public double getSummaryOutcome(){
        return getSummaryByType(Transaction.TransactionType.OUTCOME);
    }

    /**
     * Считаем сумму по типу
     * @param type Тип транзакции  INCOME or OUTCOME
     * @return
     */
    public double getSummaryByType(Transaction.TransactionType type){
        double summary = 0;
        for (Transaction t: transactions){
            if ( t.type == type)
                summary+=t.sum;
        }
        return summary;
    }

    /**
     * Список уникальный категорий и суммы трат по каждой
     * @return
     */
    public HashMap<Category,Double> getSumByCategory(){
        if (sumByCategory == null){
            calcSumByCategory();
        }
        return sumByCategory;
    }

    /**
     * Вычислим уникальный список категорий и общую сумму трат по каждой категории ( без учета алиасов)
     */
    public void calcSumByCategory(){
        sumByCategory = new HashMap<>();
        for ( Transaction t: transactions){
            //если такая запись уже есть в хеше, то достанем ее значение и прибавим в текущему
            double sum = (sumByCategory.containsKey(t.category) ? sumByCategory.get(t.category):0);
            sumByCategory.put(t.category,t.sum+sum); //обновим значение или запишем новое
        }
    }


    @Override
    public int compareTo(Object o) {
        MonthHistory m = (MonthHistory)o;
        return date.compareTo(m.date);
    }
}
