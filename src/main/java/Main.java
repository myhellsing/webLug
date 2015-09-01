
/**
 * Created by myhellsing on 11/05/15.
 */

import com.google.gdata.util.ServiceException;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static spark.Spark.get;
import static spark.SparkBase.staticFileLocation;

public class Main {
    static Gson gson = new Gson();
    public static ArrayList<Transaction> transactions = null;

    public static void loadTransactions(){
        driverToGoogleData rd = new driverToGoogleData("https://spreadsheets.google.com/feeds/spreadsheets/0Aoa5WkgCFdrudEhzcnE0bU83QksteENZS3puSTZJRUE");
        try {
            transactions = rd.getTransactions();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Transaction> getTransactions(){
        if (transactions == null){
            loadTransactions();
        }
        return transactions;
    }

    public static void main(String[] args) {
        gson = new Gson();
        staticFileLocation("/public"); // Static files
        get("/hello", (req, res) -> "Hello World");

        get("/load", (req,res) -> {
            loadTransactions();
            System.out.println("Load done. Load "+ transactions.size()+" elements");
            return transactions.size();
        });

        get("/transactions", (req,res)->{
            return transactions;
        });


        get("/income", (req,res) -> {
            if (transactions == null) loadTransactions();
            GraphData graphData = new GraphData(transactions);
            return graphData.inCome();

        }, gson::toJson);
        get("/outcome", (req,res) -> {
            if (transactions == null) loadTransactions();
            GraphData graphData = new GraphData(transactions);
            return graphData.outCome();

        }, gson::toJson);


        get("/tryHtml", (req,res) ->{
            return new String(Files.readAllBytes(Paths.get("views/test.html")));

        });
    }
}
