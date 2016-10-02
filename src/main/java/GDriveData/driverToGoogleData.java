package GDriveData;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.*;
import com.google.gdata.util.ServiceException;
import luggage.BalanceByMonth;
import luggage.data.Category;
import luggage.data.Transaction;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Created by myhellsing on 01.12.14.
 */
public class driverToGoogleData {

    // service for connection to Google Drive
    public static SpreadsheetService service = null;
    // url for data
 //   URL SPREADSHEET_FEED_URL = null;

    public ArrayList<BalanceByMonth> balanceByMonths=null;

    public static String[] defaultUrl = {"https://spreadsheets.google.com/feeds/spreadsheets/0Aoa5WkgCFdrudEhzcnE0bU83QksteENZS3puSTZJRUE",
            "https://spreadsheets.google.com/feeds/spreadsheets/1ypwMnuc1NP5cnjtSWEafp-XofXn0tWkc4mkIFNNO39Q"};


    public driverToGoogleData() {
        init();
//        try {
//            SPREADSHEET_FEED_URL = new URL(feed_url);
//        } catch (MalformedURLException e) {
//            System.out.println("SPREADSHEET_FEED_URL may be is not correct "+ e.getMessage() );
//            e.printStackTrace();
//        }
    }



    public void init()  {
        GoogleSpreadAuth gss= new GoogleSpreadAuth();
        if (service == null) try {
            service = gss.getSpreadsheetServiceService();
        } catch (IOException e) {
            e.printStackTrace();
        }
        balanceByMonths=new ArrayList<>();
    }


    //TODO refactor it
    private Date getDate(String dateText) {
        StringTokenizer st = new StringTokenizer(dateText);
        int month = getMonth(st.nextToken());
        int year=Integer.parseInt(st.nextToken());

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.DATE,2);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        return calendar.getTime();

    }

    private int getMonth(String s) {
        if (s.toLowerCase().contains("январь") ) return 0;
        if (s.toLowerCase().contains("февраль") ) return 1;
        if (s.toLowerCase().contains("март") ) return 2;
        if (s.toLowerCase().contains("апрель") ) return 3;
        if (s.toLowerCase().contains("май") ) return 4;
        if (s.toLowerCase().contains("июнь") ) return 5;
        if (s.toLowerCase().contains("июль") ) return 6;
        if (s.toLowerCase().contains("август") ) return 7;
        if (s.toLowerCase().contains("сентябрь") ) return 8;
        if (s.toLowerCase().contains("октябрь") ) return 9;
        if (s.toLowerCase().contains("ноябрь") ) return 10;
        if (s.toLowerCase().contains("декабрь") ) return 11;

        return 0;
    }

    public URL getSPREADSHEET_FEED_URL(String feed_url){
        try {
            return new URL(feed_url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<WorksheetEntry> getAllWorksheets() {
        //объединяем записи со всех документов
        List<WorksheetEntry> result = new LinkedList<>();
        //   Получили объект файла Трат
        WorksheetFeed worksheetFeed = null;
        for (String url:defaultUrl) {
            try {
                SpreadsheetEntry spreadsheet = service.getEntry(getSPREADSHEET_FEED_URL(url), SpreadsheetEntry.class);
                // Get the first worksheet of the first spreadsheet.  Получили все листы в файле Трат
                worksheetFeed = service.getFeed(
                        spreadsheet.getWorksheetFeedUrl(), WorksheetFeed.class);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ServiceException e) {
                e.printStackTrace();
            }
            result.addAll(worksheetFeed.getEntries());
        }
        return result;
    }


    private boolean needSkip(String name) {
        if (name == null)  return false;
        if (name.trim().equals("наличные")) return true;
        if (name.trim().equals("карта")) return true;
        if (name.trim().equals("на карте")) return true;
        if (name.trim().equals("на спец. счете")) return true;
        if (name.trim().equals("баланс на карте")) return true;
        if (name.trim().equals("наличка")) return true;
        if (name.trim().equals("остаток на карте")) return true;
        return false;
    }


    public ArrayList<Transaction> getTransactionsFromWorksheet(WorksheetEntry worksheet, double lastBalance) throws IOException, ServiceException {
        ArrayList<Transaction> transactions = new ArrayList<Transaction>();
        // Получили список строк
        ListFeed listFeed = service.getFeed(worksheet.getListFeedUrl(), ListFeed.class);
        Date date = getDate(worksheet.getTitle().getPlainText());
        System.out.println(date);
        // Iterate through each row, printing its cell values.
        double balanceAtBeginningMonth =0;
        System.out.println("Количество Строк в Листе "+worksheet.getTitle().getPlainText()+": "+listFeed.getEntries().size());
        double balance =0;
        for (ListEntry row : listFeed.getEntries()) {
            // если это строки с фиксацией текущего состояния на начало месяца - то пропускаем их
            if (needSkip(row.getCustomElements().getValue("название"))) {
                balanceAtBeginningMonth+=getBalance(row); // считаем баланс на начало месяца
                continue;
            }

            // парсим данные - определяем, что относится к доходу, а что к тратам
            String name = "";
            double sum = 0;
            String type="";
            Category category = null;
            for (String tag : row.getCustomElements().getTags()) {
                // System.out.print(row.getCustomElements().getValue(tag) + "\t" +"tags:"+tag+"\t");
                switch (tag) {
                    case "название":
                        name = row.getCustomElements().getValue(tag);
                        break;
                    case "приход":
                        if (row.getCustomElements().getValue(tag) != null){
                            sum = Double.parseDouble(row.getCustomElements().getValue(tag));
                            category= new Category("приход");
                            type=Transaction.INCOME;
                        }
                        break;
                    case "расход":
                        if (sum == 0 && row.getCustomElements().getValue(tag) != null) {
                            sum = Double.parseDouble(row.getCustomElements().getValue(tag));
                            type = Transaction.OUTCOME;
                        }
                        break;
                    case "категория":
                        category = new Category(row.getCustomElements().getValue(tag));
                        break;
                    default:
                        break;
                }
            }
            if (category == null) category = new Category(Category.UNKNOWN);
            // если это не пустая строка в данных
            if (name != null && !name.equals("")) transactions.add(new Transaction(name, sum, category, date, type));
            balance+=sum;
        }
        if (Math.abs(balance+balanceAtBeginningMonth-lastBalance)>0) { // если баланс предыдущего месяца меньше, чем у нас денег на счету
            //значит мы забыли что-то вписать в траты или доходы
            Transaction t =new Transaction("не учтено",(balance+balanceAtBeginningMonth)-lastBalance,new Category("не фиксировано"),date,Transaction.OUTCOME);
            System.out.println(t.getMonth()+"."+t.getYear()+": "+t.sum+" "+lastBalance+" "+(balance+balanceAtBeginningMonth));
            // если это не текущий месяц,то добавим корректировку
            if ((t.getYear() !=Calendar.getInstance().get(Calendar.YEAR) && t.getMonth()!=Calendar.getInstance().get(Calendar.MONTH))  ) {
                transactions.add(t);
            }
        }
        lastBalance=balanceAtBeginningMonth;
        balanceByMonths.add(new BalanceByMonth(date,balanceAtBeginningMonth));

        return transactions;
    };

    public ArrayList<Transaction> getTransactions() throws IOException, ServiceException {

        ArrayList<Transaction> transactions = new ArrayList<Transaction>();
        // список таблиц по месяцам
        List<WorksheetEntry> worksheets = getAllWorksheets();
        System.out.println("Количество Листов: " + worksheets.size());
        // пройдемся по всем месяцам
        double lastBalance = 0;// баланс по результатам предыдущего месяца
        int i =0;
        for (WorksheetEntry worksheet:worksheets) {
            transactions.addAll(getTransactionsFromWorksheet(worksheet,lastBalance));
            lastBalance=balanceByMonths.get(i).balance;
            i++;
        }

        return transactions;
    }

    private double getBalance(ListEntry row) {
        double balance=0;
        for (String tag : row.getCustomElements().getTags()) {
            // System.out.print(row.getCustomElements().getValue(tag) + "\t" +"tags:"+tag+"\t");
            switch (tag) {
                case "приход":
                    if (row.getCustomElements().getValue(tag) != null)
                        balance = Double.parseDouble(row.getCustomElements().getValue(tag));
                    break;
                default:
                    break;
            }
        }
        return balance;
    }

    /**
     * Отдает урл самого свежего листа
     * @return
     * @throws IOException
     * @throws ServiceException
     */

    public URL getLastListFeedUrl() throws IOException, ServiceException {
        return getAllWorksheets().get(0).getListFeedUrl();
    }


    /**
     *  Добавляет строчку в самый свежий лист
     * @param name
     * @param amount
     * @param category
     * @throws IOException
     * @throws ServiceException
     */
    public void writeTransaction(String name, double amount, String category)throws IOException, ServiceException {

        // Create a local representation of the new row.
        ListEntry row = new ListEntry();
        row.getCustomElements().setValueLocal("название", name);
        String  debit = (amount >= 0 ? Double.toString(amount):"");
        String credit = (amount < 0 ? Double.toString(-1*amount):"");
        row.getCustomElements().setValueLocal("приход", debit);
        row.getCustomElements().setValueLocal("расход", credit);
        row.getCustomElements().setValueLocal("категория", category);

        // Send the new row to the API for insertion.
        row = service.insert(getLastListFeedUrl(), row);
    }
}