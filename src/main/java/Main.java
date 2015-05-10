
/**
 * Created by myhellsing on 11/05/15.
 */

import java.util.ArrayList;

import static spark.Spark.get;
public class Main {
    public static ArrayList<Transaction> transactions = null;

    public static void main(String[] args) {
        get("/hello", (req, res) -> "Hello World");

        get("/load", (req,res) -> {
            driverToGoogleData rd = new driverToGoogleData("https://spreadsheets.google.com/feeds/spreadsheets/0Aoa5WkgCFdrudEhzcnE0bU83QksteENZS3puSTZJRUE");
            transactions = rd.getTransactions();
            System.out.println("Load done. Load "+ transactions.size()+" elements");
            return transactions.size();
        });

        get("/transactions", (req,res)->{
            return transactions;
        });
    }
}
