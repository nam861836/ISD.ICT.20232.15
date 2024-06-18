package entity.product;

import entity.db.AIMSDB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

public class Book extends Product {

    String author;
    String coverType;
    String publisher;
    Date publishDate;
    int numOfPages;
    String language;
    String bookCategory;

    public Book() throws SQLException {

    }

    public Book(int id, String title, String category, int price, int quantity, String type, String author,
                String coverType, String publisher, Date publishDate, int numOfPages, String language,
                String bookCategory) throws SQLException {
        super(id, title, category, price, quantity, type);
        this.author = author;
        this.coverType = coverType;
        this.publisher = publisher;
        this.publishDate = publishDate;
        this.numOfPages = numOfPages;
        this.language = language;
        this.bookCategory = bookCategory;
    }


    /**
     * @return int
     */
    // getter and setter
    public int getId() {
        return this.id;
    }


    /**
     * @return String
     */
    public String getAuthor() {
        return this.author;
    }


    /**
     * @param author
     * @return Book
     */
    public Book setAuthor(String author) {
        this.author = author;
        return this;
    }


    /**
     * @return String
     */
    public String getCoverType() {
        return this.coverType;
    }


    /**
     * @param coverType
     * @return Book
     */
    public Book setCoverType(String coverType) {
        this.coverType = coverType;
        return this;
    }


    /**
     * @return String
     */
    public String getPublisher() {
        return this.publisher;
    }


    /**
     * @param publisher
     * @return Book
     */
    public Book setPublisher(String publisher) {
        this.publisher = publisher;
        return this;
    }


    /**
     * @return Date
     */
    public Date getPublishDate() {
        return this.publishDate;
    }


    /**
     * @param publishDate
     * @return Book
     */
    public Book setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
        return this;
    }


    /**
     * @return int
     */
    public int getNumOfPages() {
        return this.numOfPages;
    }


    /**
     * @param numOfPages
     * @return Book
     */
    public Book setNumOfPages(int numOfPages) {
        this.numOfPages = numOfPages;
        return this;
    }


    /**
     * @return String
     */
    public String getLanguage() {
        return this.language;
    }


    /**
     * @param language
     * @return Book
     */
    public Book setLanguage(String language) {
        this.language = language;
        return this;
    }


    /**
     * @return String
     */
    public String getBookCategory() {
        return this.bookCategory;
    }


    /**
     * @param bookCategory
     * @return Book
     */
    public Book setBookCategory(String bookCategory) {
        this.bookCategory = bookCategory;
        return this;
    }


    /**
     * @param id
     * @return Product
     * @throws SQLException
     */
    @Override
    public Product getProductById(int id) throws SQLException {
        String sql = "SELECT * FROM " +
                "aims.Book " +
                "INNER JOIN aims.Product " +
                "ON Product.id = Book.id " +
                "where Product.id = " + id + ";";
        Statement stm = AIMSDB.getConnection().createStatement();
        ResultSet res = stm.executeQuery(sql);
        if (res.next()) {

            // from Product table
            String title = "";
            String type = res.getString("type");
            int price = res.getInt("price");
            String category = res.getString("category");
            int quantity = res.getInt("quantity");

            // from Book table
            String author = res.getString("author");
            String coverType = res.getString("coverType");
            String publisher = res.getString("publisher");
            Date publishDate = res.getDate("publishDate");
            int numOfPages = res.getInt("numOfPages");
            String language = res.getString("language");
            String bookCategory = res.getString("bookCategory");

            return new Book(id, title, category, price, quantity, type,
                    author, coverType, publisher, publishDate, numOfPages, language, bookCategory);

        } else {
            throw new SQLException();
        }
    }


    /**
     * @return List
     */
    @Override
    public List getAllProduct() {
        return null;
    }


    /**
     * @return String
     */
    @Override
    public String toString() {
        return "{" +
                super.toString() +
                " author='" + author + "'" +
                ", coverType='" + coverType + "'" +
                ", publisher='" + publisher + "'" +
                ", publishDate='" + publishDate + "'" +
                ", numOfPages='" + numOfPages + "'" +
                ", language='" + language + "'" +
                ", bookCategory='" + bookCategory + "'" +
                "}";
    }
}
