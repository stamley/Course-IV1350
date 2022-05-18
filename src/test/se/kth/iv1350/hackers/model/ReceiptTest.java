package se.kth.iv1350.hackers.model;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import se.kth.iv1350.hackers.DTO.*;
import se.kth.iv1350.hackers.util.*;
import org.junit.Test;


public class ReceiptTest {
    @Test
    public void testReceiptToString() {
        Amount total = new Amount(100);
        Amount totalIncludingVAT = new Amount (120);
        Amount totalDiscountedIncludingVAT = new Amount(90);
        TotalPrice totalPrice = new TotalPrice(total, totalIncludingVAT, totalDiscountedIncludingVAT);
        Amount amountPaid = new Amount(200);
        Amount changeAmount = new Amount(110);
        LocalDateTime localDateTime = LocalDateTime.now();

        Amount itemQuantity = new Amount(5);
        String itemIdentifier = "128886678";
        String itemName = "tomato";
        Amount itemPrice = new Amount(20);
        Amount itemVAT = new Amount(20);

        ItemDTO itemDTO = new ItemDTO(itemName, itemPrice, itemVAT);

        Item tomato = new Item(itemDTO, itemIdentifier, itemQuantity);

        HashMap<String, Item> items = new HashMap<String, Item>(){{
            put("128886678", tomato);
        }};

        SaleDTO saleDTO = new SaleDTO(totalPrice, localDateTime, items, amountPaid, changeAmount);

        Receipt testReceipt = new Receipt(saleDTO);

        String expResult = itemIdentifier + "\n" + itemName + "\n" + Double.toString(itemQuantity.getAmount())
                            + "\n" + "Total price: " + Double.toString(total.getAmount())
                            + "\n" + "Total price incl. VAT: " + Double.toString(totalIncludingVAT.getAmount())
                            + "\n" + "Total price incl. VAT and discount: " + Double.toString(totalDiscountedIncludingVAT.getAmount())
                            + "\n" + "Amount paid: " + Double.toString(amountPaid.getAmount())
                            + "\n" + "Change: " + Double.toString(changeAmount.getAmount());

        String result = testReceipt.receiptToString();
        assertTrue(result.contains(expResult), "Wrong printout!");
        assertTrue(result.contains(Integer.toString(localDateTime.getDayOfMonth())),
                            "Wrong rental day.");
    }
}