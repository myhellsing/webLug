package luggage.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by myhellsing on 09/02/16.
 */
public class Category  implements Serializable, Comparable {

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
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if ( name == null && ((Category)obj).name ==null) return true; //по идее такой ситуации не дожно быть никогда. Падать ?
        return name.equals(((Category)obj).name);

    }

    @Override
    public int hashCode() {
        return  (name ==null) ? 0: name.hashCode();
    }

    @Override
    public int compareTo(Object o) {
        if ( name == null || ((Category)o).name == null) return 1; //по идее такой ситуации не дожно быть никогда. Падать ?
      //  if ( name == null && ((Category)o).name ==null) return 0;
        return name.compareTo(((Category) o).name);
    }
}

