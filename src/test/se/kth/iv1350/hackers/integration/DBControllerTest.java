package se.kth.iv1350.hackers.integration;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import se.kth.iv1350.hackers.DTO.DiscountDTO;
import se.kth.iv1350.hackers.util.Amount;

public class DBControllerTest {
    private DBController dbController = new DBController();

    @Test
    public void testDiscountRequest() {
        Amount amount = new Amount(0.9);
        String typeOfDiscount = "PlaceholderTypeOfDiscount";

        DiscountDTO discountDTO = new DiscountDTO(amount, typeOfDiscount);
        DiscountDTO otherDiscountDTO = dbController.discountRequest(100);

        boolean expResult = true;
        boolean result = discountDTO.equals(otherDiscountDTO);
        
        assertEquals(expResult, result, "DiscountDTO with the same states are not equal.");
        
    }
}
