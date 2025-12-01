package dal;

import java.util.ArrayList;
import model.dto.*;

public class FakeDALManager implements IDALManager {
    public boolean verifyUserCalled = false;
    public UserDTO lastUserVerified = null;
    public int updateProductCalls = 0;

    private final ArrayList<UserDTO> users = new ArrayList<>();

    public FakeDALManager() {
        // add a fake user {username: user1, password: pass1}
        UserDTO u = new UserDTO();
        u.setUsername("testuser");
        u.setPassword("password123");
        users.add(u);
    }

    @Override
    public void addUser(UserDTO userObj, Response responseObj) {
        users.add(userObj);
    }

    @Override
    public void verifyUser(UserDTO user, Response responseObj) {
        verifyUserCalled = true;
        lastUserVerified = user;
        boolean found = false;
        for (UserDTO u : users) {
            if (u.getUsername().equals(user.getUsername())) {
                found = true;
                if (u.getPassword().equals(user.getPassword())) {
                    // success
                } else {
                    responseObj.messagesList.add(new Message("Credenciais inválidas", MessageType.Error));
                }
                break;
            }
        }
        if (!found) {
            responseObj.messagesList.add(new Message("Usuário não encontrado", MessageType.Error));
        }
    }

    // Other methods are no-op or simple stubs since tests exercise verifyUser only
    @Override
    public void updatePassword(UserDTO userObj, Response responseObj) {}

    @Override
    public void deleteUser(UserDTO userObj, Response responseObj) {}

    @Override
    public void deleteUsers(ArrayList<UserDTO> users, Response responseObj) {}

    @Override
    public ArrayList<UserDTO> getUsers(Response response) { return new ArrayList<>(); }

    @Override
    public void addUsers(ArrayList<UserDTO> users, Response responseObj) {}

    @Override
    public void saveCustomer(CustomerDTO customer, Response response) {}

    @Override
    public void updateCustomer(CustomerDTO customer, Response response) {}

    @Override
    public void deleteCustomer(CustomerDTO customer, Response response) {}

    @Override
    public ArrayList<CustomerDTO> getCustomers(Response response) { return new ArrayList<>(); }

    @Override
    public void saveSupplier(SupplierDTO supplier, Response response) {}

    @Override
    public void updateSupplier(SupplierDTO suppier, Response response) {}

    @Override
    public void deleteSupplier(SupplierDTO supplier, Response response) {}

    @Override
    public ArrayList<SupplierDTO> getSuppliers(Response res) { return new ArrayList<>(); }

    // product related stubs
    @Override
    public void addProduct(ProductDTO product, Response response) {}

    @Override
    public void deleteProduct(ProductDTO product, Response response) {}

    @Override
    public void updateProduct(ProductDTO product, Response response) { updateProductCalls++; }

    @Override
    public ArrayList<ProductDTO> getProducts(Response response) { return new ArrayList<>(); }

    @Override
    public ArrayList<ProductDTO> searchProductsByName(String searchName, Response res) { return new ArrayList<>(); }

    @Override
    public ProductDTO getProductById(int id, Response response) { return null; }

    @Override
    public model.dto.CategoryDTO getCategoryByName(String name) { return null; }

    @Override
    public model.dto.SupplierDTO getSupplierByName(String name) { return null; }

    @Override
    public ArrayList<model.dto.CategoryDTO> searchCategoryByName(String searchName, Response response) { return new ArrayList<>(); }

}
