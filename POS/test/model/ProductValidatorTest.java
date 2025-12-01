package test.model;

import model.dto.ProductDTO;
import model.dto.Response;
import model.validators.ProductValidator;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProductValidatorTest {

    @Test
    public void testValidProduct() {
        ProductDTO p = new ProductDTO();
        p.setProductName("Test");
        p.setPrice(5.0);
        Response res = new Response();
        ProductValidator.validateProduct(p, res);
        assertTrue("Response should be successfull when valid", res.isSuccessfull());
    }

    @Test
    public void testInvalidProductNegativePrice() {
        ProductDTO p = new ProductDTO();
        p.setProductName("Test");
        p.setPrice(-3.0);
        Response res = new Response();
        ProductValidator.validateProduct(p, res);
        assertFalse("Negative price must be invalid", res.isSuccessfull());
    }

    @Test
    public void testInvalidProductMissingName() {
        ProductDTO p = new ProductDTO();
        p.setPrice(2.0);
        Response res = new Response();
        ProductValidator.validateProduct(p, res);
        assertFalse("Missing name must be invalid", res.isSuccessfull());
    }

}
