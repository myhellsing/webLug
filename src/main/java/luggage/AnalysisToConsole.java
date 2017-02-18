package luggage;

import luggage.data.Category;
import luggage.data.MonthHistory;
import luggage.data.Transaction;

import java.util.*;

/**
 * Created by myhellsing on 13/02/17.
 */
public class AnalysisToConsole {
    AnalysisCalc analysisCalc;

    public static void main(String[] args) {
        new AnalysisToConsole().run();

    }

    /**
     * Выведем в консоль информацию по годам
     */
    public void printInOutComeByYears(){
        System.out.println("Год \t\t Приход \t\t Расход \t\t Разница");
        HashMap<Integer,Double> income = analysisCalc.getSummaryByYearsAndType( Transaction.TransactionType.INCOME);
        HashMap<Integer,Double> outcome = analysisCalc.getSummaryByYearsAndType(Transaction.TransactionType.OUTCOME);

        // TODO refactor this
        ArrayList<Integer> years =new ArrayList<>();
        years.addAll(income.keySet());
        Collections.sort(years);

        double incomeAll=0;
        double outcomeAll=0;
        for (int year:years){
            System.out.printf("%d\t%,15d\t%,15d\t%,15d\n",
                    year,
                    Math.round(income.get(year)),
                    Math.round(outcome.get(year)),
                    Math.round(income.get(year) - outcome.get(year)));
            incomeAll+=income.get(year);
            outcomeAll+=outcome.get(year);
        }
        System.out.printf("Итого\t%,15d\t%,15d\t%,15d\n",
                Math.round(incomeAll),
                Math.round(outcomeAll),
                Math.round(incomeAll-outcomeAll));

    }

    /**
     * Выведем статистика по расходам/доходам по месяцам
     */
    public void printMonthSummary(){
        double prewBalance = 0;
        System.out.println("Дата\t  Баланс на начало\tБаланс на конец\t\tПриход\t\tРасход\t\t\tПотери");
        for (MonthHistory m:analysisCalc.getMonthHistories()){

            System.out.printf("%s \t %,10d \t %,10d \t %,10d \t %,10d \t %,10d \n",
                    Transaction.dateFormat.format(m.date),
                    Math.round(m.balanceAtBegin),
                    Math.round(m.getCurrentBalance()),
                    Math.round(m.getSummaryIncome()),
                    Math.round(m.getSummaryOutcome()),
                    Math.round((Math.abs(prewBalance - m.balanceAtBegin)))
            );
            prewBalance = m.getCurrentBalance();
        }
    }

    /**
     * Выведем траты, которые встречаются в каждом месяце
     */

    public void printToConsoleEveryMonthCategories(){
        HashMap<Category,Double>  everyMonthCategories = analysisCalc.calcEveryMonthCategories();
        System.out.println("Категории, которые встречаются в каждом месяце");
        for (Category c:everyMonthCategories.keySet()){
            System.out.printf("%15s\t %15.0f\n", c.name, everyMonthCategories.get(c));
        }
    }

    /**
     * Доход и расход за все года.
     */
    public void printAllIncomeAndOutcome(){
        double income =0;
        double outcome =0;
        int cntMonths = 0;
        for (MonthHistory m: analysisCalc.getMonthHistories()){
            income+=m.getSummaryIncome();
            outcome+=m.getSummaryOutcome();
        }
        System.out.printf("Приход:\t %15.0f\n",income);
        System.out.printf("Расход:\t %15.0f\n",outcome);
        System.out.printf("Кол-во месяцев(лет):\t %d(%d)\n",analysisCalc.getMonthHistories().size(), (analysisCalc.getMonthHistories().size()/12));
    }

    /**
     * Суммированный список по названия трат( и расходы и доходы)
     */

    public void printSummaryByNameOfTransaction(){
        LinkedHashMap<String,Double> result = analysisCalc.getSummaryByNameOfTransactions();
        System.out.println("Название траты\t\tСумма");
        for (String s:result.keySet()){
            System.out.printf("%20s\t%,15d\n",s,Math.round(result.get(s)));
        }

    }

    /**
     * Суммированный список по категориям ( и расходы и доходы)
     */

    public void printSummaryByCategoryName(){
        LinkedHashMap<Category,Double> result = analysisCalc.getSummaryByCategoryName();
        System.out.println("Название категории\t\tСумма");
        for (Category s:result.keySet()){
            System.out.printf("%20s\t%,15d\n",s.name,Math.round(result.get(s)));
        }
    }


    public void printTransactions(LinkedList<Transaction> transactions){
        System.out.println("Список траты: "+ transactions.size());
        transactions.forEach(t->{
            System.out.println(t);
        });
    }

    public void printGeneratedAliases(){
        ArrayList<Category> categories = analysisCalc.generateCategoryAliases();
        for (Category c:categories) {
            if (c.aliases.size() > 0) {
                System.out.print(c.name + "\t");
                for (String s : c.aliases) {
                    System.out.print(" " + s);
                }
                System.out.println();
            }
        }
    }

    public void run(){
        analysisCalc = new AnalysisCalc();

/*
        printAllIncomeAndOutcome();
        System.out.println("--------------------------------------------------------");

        printInOutComeByYears();
        System.out.println("--------------------------------------------------------");

        printMonthSummary();
        System.out.println("--------------------------------------------------------");

        printToConsoleEveryMonthCategories();
        System.out.println("--------------------------------------------------------");

        printSummaryByNameOfTransaction();
        System.out.println("--------------------------------------------------------");

        printSummaryByCategoryName();
        System.out.println("--------------------------------------------------------");


        printTransactions(analysisCalc.getTransactionsByCategory(analysisCalc.getMonthHistories(),new Category(Category.UNKNOWN)) );
        System.out.println("--------------------------------------------------------");

        */

        printGeneratedAliases();
        System.out.println("--------------------------------------------------------");
    }

}
