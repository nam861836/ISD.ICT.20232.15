package controller;

import entity.product.Product;

import java.sql.SQLException;
import java.util.List;

/**
 * This class controls the flow of events in homescreen
 *
 *
 */
public class HomeController extends BaseController {

    /**
     * this method gets all Product in DB and return back to home to display
     *
     * @return List[Product]
     * @throws SQLException
     */
    //Coincidental Cohesion
    //Không xác định coupling
    public List getAllProduct() throws SQLException {
        return new Product().getAllProduct();
    }

    public void deleteProduct(Product product) throws SQLException {
        product.removeProduct();
    }

    public List<Product> search(String searchText) throws SQLException{
        return new Product().searchProduct(searchText);
    }
}
