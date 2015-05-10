/**
 * Created by myhellsing on 11/05/15.
 */
import static spark.Spark.*;

public class Helloworld {
    public static void main(String[] args) {
        get("/hello", (req, res) -> "Hello World");
    }
}