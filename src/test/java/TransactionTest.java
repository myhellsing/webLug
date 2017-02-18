import luggage.data.Category;
import luggage.data.Transaction;
import org.junit.Test;

import java.util.Date;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Created by myhellsing on 18/02/17.
 */
public class TransactionTest {

    @Test
    public void testBelongCategory(){
        Transaction transaction = new Transaction("Utkonos",100.0, new Category("Eat"), new Date(), Transaction.TransactionType.OUTCOME);

        Category category1 = new Category("Eat");
        category1.aliases.add("Еда");
        assertTrue(transaction.belongCategory(category1));

        Category category2 =  new Category("Eating");
        category2.aliases.add("Eat");
        assertTrue(transaction.belongCategory(category2));

        Category category3 =  new Category("Home");
        category2.aliases.add("House");
        assertFalse(transaction.belongCategory(category3));
    }
}
