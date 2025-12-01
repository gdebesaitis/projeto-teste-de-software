package test.model;

import java.util.ArrayList;
import java.util.List;
import model.SaleCalculator;
import model.dto.SaleItemDTO;
import org.junit.Test;
import static org.junit.Assert.*;

public class SaleCalculatorTest {

    @Test
    public void testCalculateSubtotalWithIntegers() {
        List<SaleItemDTO> items = new ArrayList<>();
        SaleItemDTO a = new SaleItemDTO();
        a.setPrice(10.0);
        a.setQuantity(2);
        items.add(a);
        SaleItemDTO b = new SaleItemDTO();
        b.setPrice(5.0);
        b.setQuantity(3);
        items.add(b);

        double subtotal = SaleCalculator.calculateSubtotal(items);
        assertEquals(35.0, subtotal, 0.0001);
    }

    @Test
    public void testCalculateSubtotalWithDecimals() {
        List<SaleItemDTO> items = new ArrayList<>();
        SaleItemDTO a = new SaleItemDTO();
        a.setPrice(10.55);
        a.setQuantity(2);
        items.add(a);
        SaleItemDTO b = new SaleItemDTO();
        b.setPrice(5.25);
        b.setQuantity(1);
        items.add(b);

        double subtotal = SaleCalculator.calculateSubtotal(items);
        assertEquals(26.35, subtotal, 0.0001);
    }

    @Test
    public void testCalculateTotalWithDiscountZeroAndPositive() {
        List<SaleItemDTO> items = new ArrayList<>();
        SaleItemDTO a = new SaleItemDTO();
        a.setPrice(10.0);
        a.setQuantity(2);
        items.add(a);

        double subtotal = SaleCalculator.calculateSubtotal(items);
        assertEquals(20.0, subtotal, 0.0001);

        double totalNoDiscount = SaleCalculator.calculateTotal(items, 0.0);
        assertEquals(20.0, totalNoDiscount, 0.0001);

        double totalWithDiscount = SaleCalculator.calculateTotal(items, 5.0);
        assertEquals(15.0, totalWithDiscount, 0.0001);
    }

}
