package entity.product;

import entity.ProductPublisher;
import entity.db.AIMSDB;
import utils.Utils;
import views.ProductObserver;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

/**
 * The general media class, for another media it can be done by inheriting this class
 *
 * @author nguyenlm
 */
public class Product implements ProductPublisher {
    // For observer pattern
    protected List<ProductObserver> productObservers = new ArrayList<ProductObserver>();
    protected boolean isSupportedPlaceRushOrder = new Random().nextBoolean();
    private static Logger LOGGER = Utils.getLogger(Product.class.getName());
    protected Statement stm;
    protected int id;
    protected String title;
    protected String category;
    protected int value; // the real price of product (eg: 450)
    protected int price; // the price which will be displayed on browser (eg: 500)
    protected int quantity;
    protected String type;
    protected String imageURL;

    public Product() throws SQLException {
        stm = AIMSDB.getConnection().createStatement();
    }

    public Product(int id, String title, String category, int price, int quantity, String type) throws SQLException {
        this.id = id;
        this.title = title;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.type = type;

        //stm = AIMSDB.getConnection().createStatement();
    }

    /**
     * @return boolean
     */
    public boolean getIsSupportedPlaceRushOrder() {
        return this.isSupportedPlaceRushOrder;
    }

    /**
     * @return int
     * @throws SQLException
     */
    public int getQuantity() throws SQLException {
        int updated_quantity = getProductById(id).quantity;
        this.quantity = updated_quantity;
        return updated_quantity;
    }

    /**
     * @param quantity
     * @return Product
     */
    public Product setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    /**
     * @param id
     * @return Product
     * @throws SQLException
     */
    public Product getProductById(int id) throws SQLException {
        String sql = "SELECT * FROM Product ;";
        Statement stm = AIMSDB.getConnection().createStatement();
        ResultSet res = stm.executeQuery(sql);
        if (res.next()) {

            return new Product()
                    .setId(res.getInt("id"))
                    .setTitle(res.getString("title"))
                    .setQuantity(res.getInt("quantity"))
                    .setCategory(res.getString("category"))
                    .setProductURL(res.getString("imageUrl"))
                    .setPrice(res.getInt("price"))
                    .setType(res.getString("type"));
        }
        return null;
    }

    /**
     * @return List
     * @throws SQLException
     */
    public List getAllProduct() throws SQLException {
        Statement stm = AIMSDB.getConnection().createStatement();
        ResultSet res = stm.executeQuery("select * from Product");
        ArrayList products = new ArrayList<>();
        while (res.next()) {
            Product product = new Product()
                    .setId(res.getInt("id"))
                    .setTitle(res.getString("title"))
                    .setQuantity(res.getInt("quantity"))
                    .setCategory(res.getString("category"))
                    .setProductURL(res.getString("imageUrl"))
                    .setPrice(res.getInt("price"))
                    .setType(res.getString("type"));
            products.add(product);
        }
        return products;
    }

    public void removeProduct() throws SQLException {
        Statement stm = AIMSDB.getConnection().createStatement();
        stm.executeUpdate("delete from Product where id=" + this.id + ";");
        notifyObserver();
    }

    /**
     * @param tbname
     * @param id
     * @param field
     * @param value
     * @throws SQLException
     */
    public void updateProductFieldById(String tbname, int id, String field, Object value) throws SQLException {
        Statement stm = AIMSDB.getConnection().createStatement();
        if (value instanceof String) {
            value = "\"" + value + "\"";
        }
        stm.executeUpdate(" update " + tbname + " set" + " "
                + field + "=" + value + " "
                + "where id=" + id + ";");
    }

    public List<Product> searchProduct(String title) throws SQLException{
        String sql = "SELECT * FROM Product where title like '%" + title + "%';";
        Statement stm = AIMSDB.getConnection().createStatement();
        ResultSet res = stm.executeQuery(sql);
        List<Product> list = new ArrayList<Product>();
        while (res.next()) {

            Product m = new Product()
                    .setId(res.getInt("id"))
                    .setTitle(res.getString("title"))
                    .setQuantity(res.getInt("quantity"))
                    .setCategory(res.getString("category"))
                    .setProductURL(res.getString("imageUrl"))
                    .setPrice(res.getInt("price"))
                    .setType(res.getString("type"));
            list.add(m);
        }
        return list;
    }

    /**
     * @return int
     */
    // getter and setter
    public int getId() {
        return this.id;
    }

    /**
     * @param id
     * @return Product
     */
    private Product setId(int id) {
        this.id = id;
        return this;
    }

    /**
     * @return String
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * @param title
     * @return Product
     */
    public Product setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * @return String
     */
    public String getCategory() {
        return this.category;
    }

    /**
     * @param category
     * @return Product
     */
    public Product setCategory(String category) {
        this.category = category;
        return this;
    }

    /**
     * @return int
     */
    public int getPrice() {
        return this.price;
    }

    /**
     * @param price
     * @return Product
     */
    public Product setPrice(int price) {
        this.price = price;
        return this;
    }

    /**
     * @return String
     */
    public String getImageURL() {
        return this.imageURL;
    }

    /**
     * @param url
     * @return Product
     */
    public Product setProductURL(String url) {
        this.imageURL = url;
        return this;
    }

    /**
     * @return String
     */
    public String getType() {
        return this.type;
    }

    /**
     * @param type
     * @return Product
     */
    public Product setType(String type) {
        this.type = type;
        return this;
    }

    /**
     * @return String
     */
    @Override
    public String toString() {
        return "{" +
                " id='" + id + "'" +
                ", title='" + title + "'" +
                ", category='" + category + "'" +
                ", price='" + price + "'" +
                ", quantity='" + quantity + "'" +
                ", type='" + type + "'" +
                ", imageURL='" + imageURL + "'" +
                "}";
    }

    // For observer
    @Override
    public void registerObserver(ProductObserver observer) {
        productObservers.add(observer);
    }

    @Override
    public void unregisterObserver(ProductObserver observer) {
        productObservers.remove(observer);
    }

    @Override
    public void notifyObserver() {
        //test
//        System.out.println("Delete product");
        productObservers.forEach(observer -> observer.productUpdate());
    }
}