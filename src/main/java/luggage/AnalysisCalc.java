package luggage;

import GDriveData.driverToGoogleData;
import com.google.gdata.util.ServiceException;
import luggage.data.Category;
import luggage.data.MonthHistory;
import luggage.data.Transaction;

import java.io.*;
import java.util.*;

/**
 * Created by myhellsing on 03/11/15.
 */
public class AnalysisCalc {

    protected ArrayList<MonthHistory> monthHistories;
    public  String localCacheTransactions="data/transactions.txt";
    public Boolean quietMode =true;
    public ArrayList<Category> categories;

    /**
     * Конструктор для поведения по умолчанию
     */
    public AnalysisCalc() {
        loadMonthHistories();
        loadCategories();
    }

    public void loadCategories() {
        categories=this.generateCategoryAliases();
    }

    /**
     * Конструктор для тестов :)
     * @param monthHistories
     */
    public AnalysisCalc(ArrayList<MonthHistory> monthHistories) {
        this.monthHistories = monthHistories;
    }

    public ArrayList<MonthHistory> getMonthHistories() {
        if (monthHistories == null) loadMonthHistories();
        return monthHistories;
    }

    public void loadMonthHistories(){
        monthHistories = (ArrayList<MonthHistory>)getFromLocalCache(localCacheTransactions);
        if (monthHistories != null) return;
        driverToGoogleData rd = new driverToGoogleData();
        try {
            monthHistories = rd.getMonthHistories();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        saveLocalCache(localCacheTransactions,monthHistories);
    }

    /**
     * Достаем данные из локального файла, путь к которому указан в localCache
     * @return
     */
    public  Object getFromLocalCache(String fileName){
        Object result= null;
        try {
            File f = new File(fileName);
            if (!f.exists()) return null;
            FileInputStream fileIn = new FileInputStream(f);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            result = in.readObject();
            in.close();
            fileIn.close();

            return result;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Сохраняем данные в локальный файл по пути, указанному в localCache
     */
    public  void saveLocalCache (String fileName, Object object){
        try {
            //кешируем траты
            FileOutputStream fileOut = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(object);
            out.close();
            fileOut.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Название Траты - список категорий, который для нее указывался.
     * @return
     */

    public HashMap<String,LinkedList<Category>> getCategoriesBySameTransactionName() {
        HashMap<String, LinkedList<Category>> sameCategoryByTransaction = new HashMap<>();
        for (MonthHistory m : monthHistories) {  //  пройдемся по месяцам
            for (Transaction t : m.transactions) { // список трат в месяце
                if (isUnknownCategoryOrIncome(t)) continue;
                LinkedList<Category> categoryList = new LinkedList<>(); // список похожих категорий
                if (sameCategoryByTransaction.containsKey(t.name)) {  // если трата с таким именем уже встречалась - добавим категорию
                    categoryList = sameCategoryByTransaction.get(t.name);
                }
                if (!categoryList.contains(t.category))
                    categoryList.add(t.category);// добавим категорию, если еще не было такой
                sameCategoryByTransaction.put(t.name, categoryList);
            }
        }
        return sameCategoryByTransaction;
    }


    public boolean isUnknownCategoryOrIncome(Transaction t){
        // неизвестные категории или Приход будем игнорировать.
        return (t.category.name.compareTo(Category.UNKNOWN) ==0 || t.type == Transaction.TransactionType.INCOME);
    }


    /**
     * Генерируем список алиасов для категоний на основании трат
     * @return список категорий с заполненными алиасами
     */

    public ArrayList<Category> generateCategoryAliases(){
        HashMap<String,Category> categoriesWithAliases = new HashMap<>();
        for (MonthHistory m:monthHistories){
            for (Transaction t:m.transactions){
                Category category = t.category;
                // неизвестные категории или Приход будем игнорировать.
                if (isUnknownCategoryOrIncome(t)) continue;
                if (categoriesWithAliases.containsKey(t.name)){
                    category = categoriesWithAliases.get(t.name);
                    //если названия категории еще нет среди алиасов и название категории траты не совпадает с уже запомненным названием категории
                    if (!category.aliases.contains(t.category.name) && category.name.compareTo(t.category.name)!=0)
                        category.aliases.add(t.category.name);
                }
                categoriesWithAliases.put(t.name,category);
            }
        }

        //Теперь нужно убрать дублирующиеся  категории с разными тратами
        ArrayList<Category> result = new ArrayList<Category>();
        for (Category category:categoriesWithAliases.values() ){
            if (result.contains(category)){
                result.get(result.indexOf(category)).aliases.addAll(category.aliases);
            } else
            result.add(category);
        }
        return result;
 /**/
    }



    /**
     *  Список ежемесячных трат
     */
    public HashMap<Category,Double> calcEveryMonthCategories(){
        TreeSet<Category> categories = new TreeSet<>();
        HashMap<Category,Double> everyMonthCategories= new HashMap<>();
        for (MonthHistory m : monthHistories){
            if (m.getSumByCategory()!= null){
                categories.addAll(m.getSumByCategory().keySet());
            }
        }
        for (Category c:categories){
            boolean wasIt = true;
            double sum = 0;
            for (MonthHistory m :monthHistories){
                //Так как не всегда мы записывали категории, добавим костыль, что проверять будем на наличие, только
                //если категорий больше 3.
                if (m.getSumByCategory().size() >3 && !m.getSumByCategory().containsKey(c)) {
                    wasIt = false;
                    break;
                }
                sum+=(m.getSumByCategory().containsKey(c)) ? m.getSumByCategory().get(c) : 0;
            }

            if (wasIt){
                everyMonthCategories.put(c, sum);
            }
        }
        return everyMonthCategories;
    }



  // сумма по названиям трат

    /**
     *  Сумма по названиям трат ( не по категориям!)
     * @return
     */
    public LinkedHashMap<String,Double> getSummaryByNameOfTransactions(){
        HashMap<String,Double> unsortedResult= new HashMap<>();
        for (MonthHistory m:monthHistories) {
            for (Transaction t : m.transactions) {
                double sum = (unsortedResult.containsKey(t.name) ? unsortedResult.get(t.name) : 0);
                unsortedResult.put(t.name, sum + t.sum);
            }
        }
        LinkedHashMap<String,Double> result = new LinkedHashMap<>();

        unsortedResult.entrySet().stream().sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .forEachOrdered(x -> result.put(x.getKey(), x.getValue()));

        return result;
    }

    /**
     * Сумма по категориям за все время ( без учета алиасов категорий)
     * @return
     */

    public LinkedHashMap<Category,Double> getSummaryByCategoryName(){
        HashMap<Category,Double> unsortedResult =  new HashMap<>();
        for ( MonthHistory m:monthHistories){
            for (Category c:m.getSumByCategory().keySet()){
                double sum =  (unsortedResult.containsKey(c) ? unsortedResult.get(c) : 0);
                unsortedResult.put(c,sum+m.getSumByCategory().get(c));
            }
        }

        LinkedHashMap<Category,Double> result = new LinkedHashMap<>();
        unsortedResult.entrySet().stream().sorted(Map.Entry.<Category,Double>comparingByValue().reversed())
                .forEachOrdered(x -> result.put(x.getKey(),x.getValue()));

        return result;
    }

    /**
     *  Список трат по одной категории ( без учета алиасов)
     * @param category
     * @return
     */
    public LinkedList<Transaction> getTransactionsByCategory(Category category){
       LinkedList<Transaction> transactions = new LinkedList<>();
        for (MonthHistory m:monthHistories){
            transactions.addAll(m.getTransactionsByCategory(category));
        }
        return transactions;
    }

    /**
     * Список транзакций, которые относятся к категории
     * @param category Категории
     * @return
     */

    public LinkedList<Transaction> getTransactionsByCategoryAndAliases(Category category){
        LinkedList<Transaction> sortedTransactions =new LinkedList<>();
        for (MonthHistory m:monthHistories) {
            for (Transaction transaction : m.transactions) {
                if (transaction.belongCategory(category)) {
                    sortedTransactions.add(transaction);
                }
            }
        }
        return sortedTransactions;
    }

/*




    public void calcAuto(){
        List<String> aliases = new LinkedList<>();
        aliases.add("машина");
        aliases.add("авто");

        long allmoney =0;
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (Transaction t:getTransactionsByCategoryAliases(aliases)){
            if (currentYear!=t.getYear()){
                printMoney(currentYear, allmoney);
                currentYear=t.getYear();
                allmoney=0;

            }
            else{
                if (!t.name.contains("каско"))
                    allmoney+=t.sum;

            }

        }
        printMoney(currentYear,(allmoney));

        //2015 по статьям
        System.out.println("2015");
        sumByNameOfTransactions(getTransactionsByYear(2015, getTransactionsByCategoryAliases(aliases)));
        System.out.println("2016");
        sumByNameOfTransactions(getTransactionsByYear(2016, getTransactionsByCategoryAliases(aliases)));
    }
*/

    /**
     *  Считаем сумму по годам в зависимости от типа - Расход или Доход
     * @param type тип -  INCOME или OUTCOME
     * @return
     */

    public HashMap<Integer,Double> getSummaryByYearsAndType(Transaction.TransactionType type){
        HashMap<Integer,Double> summary = new HashMap<>();
        double sum =0;
        for (MonthHistory m:monthHistories){
            Calendar cal = Calendar.getInstance();
            cal.setTime(m.date);
            int year = cal.get(Calendar.YEAR);
            sum= (summary.containsKey(year) ? summary.get(year):0);
            summary.put(year, m.getSummaryByType(type) + sum);
        }
        return summary;
    }


}
