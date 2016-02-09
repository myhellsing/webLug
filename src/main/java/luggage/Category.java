package luggage;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.List;

/**
 * Created by myhellsing on 09/02/16.
 */
public class Category implements Serializable {

    public ObjectId objectId;
    public String name;
    public List<String> aliases;

    public Category(String name) {
        this.name = name;
    }

    public Document createBSON(){
         return new Document().append("name",name).append("aliases",aliases);
    }
}
