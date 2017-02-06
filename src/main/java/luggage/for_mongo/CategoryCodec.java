package luggage.for_mongo;

import luggage.data.Category;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.util.ArrayList;

/**
 * Created by myhellsing on 14/02/16.
 */
public class CategoryCodec implements Codec<Category> {

    public CategoryCodec() {
    }


    @Override
    public Category decode(BsonReader reader, DecoderContext decoderContext)
    {
        reader.readStartDocument();
        String  name = reader.readString("name");
        ArrayList<String> aliases = new ArrayList<>();
        reader.readName("aliases");
        reader.readStartArray();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT ){
            aliases.add(reader.readString());
        }
        reader.readEndArray();
        reader.readEndDocument();
        return new Category(name,aliases);
    }

    @Override
    public void encode(BsonWriter writer, Category category, EncoderContext encoderContext)
    {

        writer.writeStartDocument();
        writer.writeString("name", category.name);
        writer.writeName("aliases");
        writer.writeStartArray();
        for (String s:category.aliases){
           writer.writeString(s);
        }
        writer.writeEndArray();
        writer.writeEndDocument();
    }

    @Override
    public Class<Category> getEncoderClass()
    {
        return Category.class;
    }

}