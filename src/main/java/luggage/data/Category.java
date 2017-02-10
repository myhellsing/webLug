package luggage.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by myhellsing on 09/02/16.
 */
public class Category  implements Serializable, Comparable{

    public static String COLLECTION_NAME ="Category";
    public static String UNKNOWN = "unknown";

    public String name;
    public ArrayList<String> aliases;

    public Category(String name, ArrayList<String> aliases) {
        this.name = name;
        this.aliases = aliases;
    }

    public Category(String name) {
        this.name = name;
        this.aliases = new ArrayList<>();
    }


    @Override
    public int compareTo(Object o) {
        Category c =(Category)o;
        return name.compareTo(c.name);
    }
}

