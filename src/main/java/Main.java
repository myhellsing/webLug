
/**
 * Created by myhellsing on 11/05/15.
 */

import com.google.gson.Gson;
import com.mitchellbosecke.pebble.loader.FileLoader;
import com.mitchellbosecke.pebble.loader.Loader;
import luggage.AnalysisCalc;
import luggage.BalanceByMonth;
import luggage.data.MonthHistory;
import spark.ModelAndView;
import spark.template.pebble.PebbleTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.get;
import static spark.SparkBase.staticFileLocation;

public class Main {
    static Gson gson = new Gson();
    public static ArrayList<MonthHistory> monthHistories = null;
    public static ArrayList<BalanceByMonth> balanceByMonths=null;

    private static void loadData() {
        AnalysisCalc analysisCalc = new AnalysisCalc();
        monthHistories = analysisCalc.getMonthHistories();
    }

    public static void main(String[] args) {
        gson = new Gson();

        staticFileLocation("/public"); // Static files

        Loader loader = new FileLoader();
        loader.setPrefix("views/");

        get("/hello", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("message", "Hello World!");

            return new ModelAndView(attributes, "hello.pebble");
        }, new PebbleTemplateEngine(loader));

        get("/load", (req, res) -> {
            loadData();
            System.out.println("Load done. Load " + monthHistories.size() + " elements");
            return monthHistories.size();
        });

        get("/transactions", (req,res)->{
            return monthHistories;
        });


     /*   get("/income", (req,res) -> {
            if (monthHistories == null) loadData();
            GraphData graphData = new GraphData(monthHistories);
            return graphData.inCome();

        }, gson::toJson);
        get("/outcome", (req,res) -> {
            if (monthHistories == null) loadData();
            GraphData graphData = new GraphData(monthHistories);
            return graphData.outCome();

        }, gson::toJson);

        get("/balance",(req,res) -> {
            if (monthHistories == null) loadData();
            return monthHistories;

        }, gson::toJson);

        get("/tryHtml", (req,res) ->{
            return new String(Files.readAllBytes(Paths.get("views/test.html")));

        });
        get("/balanceHtml", (req,res) ->{
            return new String(Files.readAllBytes(Paths.get("views/balance.html")));

        });*/
    }


}
