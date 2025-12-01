package test.integration;

import dal.FakeDALManager;
import model.POSController;
import model.dto.Response;
import model.dto.UserDTO;
import org.junit.Test;
import static org.junit.Assert.*;

public class AuthenticationTest {

    @Test
    public void testUserCorrectButPasswordWrong() {
        FakeDALManager fake = new FakeDALManager();
        POSController controller = new POSController(fake);
        UserDTO user = new UserDTO();
        user.setUsername("testuser");
        user.setPassword("wrongpass");
        Response res = new Response();
        controller.verifyUser(user, res);
        assertFalse("Response should indicate failure", res.isSuccessfull());
    }

    @Test
    public void testUserNotFound() {
        FakeDALManager fake = new FakeDALManager();
        POSController controller = new POSController(fake);
        UserDTO user = new UserDTO();
        user.setUsername("doesnotexist");
        user.setPassword("whatever");
        Response res = new Response();
        controller.verifyUser(user, res);
        assertFalse("Response should indicate failure for nonexistent user", res.isSuccessfull());
    }

    @Test
    public void testEmptyFields() {
        FakeDALManager fake = new FakeDALManager();
        POSController controller = new POSController(fake);
        UserDTO user = new UserDTO();
        user.setUsername("");
        user.setPassword("");
        Response res = new Response();
        controller.verifyUser(user, res);
        // CommonValidator should fail validation before calling DAL
        assertFalse("Empty fields should cause validation failure", res.isSuccessfull());
    }

}
