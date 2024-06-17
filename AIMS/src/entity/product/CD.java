package entity.product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class CD extends Product {

    String artist;
    String recordLabel;
    String musicType;
    Date releasedDate;

    public CD() throws SQLException {

    }

    public CD(int id, String title, String category, int price, int quantity, String type, String artist,
              String recordLabel, String musicType, Date releasedDate) throws SQLException {
        super(id, title, category, price, quantity, type);
        this.artist = artist;
        this.recordLabel = recordLabel;
        this.musicType = musicType;
        this.releasedDate = releasedDate;
    }


    /**
     * @return String
     */
    public String getArtist() {
        return this.artist;
    }


    /**
     * @param artist
     * @return CD
     */
    public CD setArtist(String artist) {
        this.artist = artist;
        return this;
    }


    /**
     * @return String
     */
    public String getRecordLabel() {
        return this.recordLabel;
    }


    /**
     * @param recordLabel
     * @return CD
     */
    public CD setRecordLabel(String recordLabel) {
        this.recordLabel = recordLabel;
        return this;
    }


    /**
     * @return String
     */
    public String getMusicType() {
        return this.musicType;
    }


    /**
     * @param musicType
     * @return CD
     */
    public CD setMusicType(String musicType) {
        this.musicType = musicType;
        return this;
    }


    /**
     * @return Date
     */
    public Date getReleasedDate() {
        return this.releasedDate;
    }


    /**
     * @param releasedDate
     * @return CD
     */
    public CD setReleasedDate(Date releasedDate) {
        this.releasedDate = releasedDate;
        return this;
    }


    /**
     * @return String
     */
    @Override
    public String toString() {
        return "{" + super.toString() + " artist='" + artist + "'" + ", recordLabel='" + recordLabel + "'"
                + "'" + ", musicType='" + musicType + "'" + ", releasedDate='"
                + releasedDate + "'" + "}";
    }


    /**
     * @param id
     * @return Product
     * @throws SQLException
     */
    @Override
    public Product getProductById(int id) throws SQLException {
        String sql = "SELECT * FROM " +
                "aims.CD " +
                "INNER JOIN aims.Product " +
                "ON Product.id = CD.id " +
                "where Product.id = " + id + ";";
        ResultSet res = stm.executeQuery(sql);
        if (res.next()) {

            // from media table
            String title = "";
            String type = res.getString("type");
            int price = res.getInt("price");
            String category = res.getString("category");
            int quantity = res.getInt("quantity");

            // from CD table
            String artist = res.getString("artist");
            String recordLabel = res.getString("recordLabel");
            String musicType = res.getString("musicType");
            Date releasedDate = res.getDate("releasedDate");

            return new CD(id, title, category, price, quantity, type,
                    artist, recordLabel, musicType, releasedDate);

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

}
