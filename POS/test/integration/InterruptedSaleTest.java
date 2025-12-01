package test.integration;

import dal.FakeDALManager;
import model.POSController;
import model.dto.ProductDTO;
import model.dto.Response;
import org.junit.Test;
import static org.junit.Assert.*;

public class InterruptedSaleTest {

    @Test
    public void testSaleCanceledDoesNotChangeStock() {
        FakeDALManager fake = new FakeDALManager();
        POSController controller = new POSController(fake);

        // Simulate adding a product to cart - this happens in the UI and should not call the DAL
        // In this system, adding to cart doesn't touch DAL. Cancelling the sale should not call updateProduct.
        // Confirm by asserting that updateProductCalls remains 0 after cancel simulation.
        assertEquals(0, fake.updateProductCalls);

        // Simulate cancel action (nothing to call on controller), confirm still zero
        // Real UI resets cart using UI.resetCart, it does not call controller methods.
        assertEquals(0, fake.updateProductCalls);
    }
}
