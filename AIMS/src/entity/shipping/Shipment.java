package entity.shipping;

import entity.db.AIMSDB;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

public class Shipment {
    private String phone;
    private String name;
    private String province;
    private String address;

    private int shipType;
    private String deliveryInstruction;
    private int orderId;

    private String shipmentDetail;
    private String deliveryTime;
    private AdditionalInfo addInfo;

    public void enterAdditonal(AdditionalInfo addInfo){
       this.addInfo = addInfo;
       this.deliveryInstruction = addInfo.getDeliveryInstruction();
       this.deliveryTime = addInfo.getDeliveryTime();
    }

    public AdditionalInfo getAddtionalInfo(){
        return this.addInfo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Shipment() {
    }

    public int getShipType() {
        return shipType;
    }

    public void setShipType(int shipType) {
        this.shipType = shipType;
    }

    public void setDeliveryInstruction(String deliveryInstruction) {
        this.deliveryInstruction = deliveryInstruction;
    }

    public String getShipmentDetail() {
        return shipmentDetail;
    }

    public void setShipmentDetail(String shipmentDetail) {
        this.shipmentDetail = shipmentDetail;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Shipment(int shipType, String deliveryInstruction, String shipmentDetail, String deliveryTime,
            int orderId) {
        super();
        this.shipType = shipType;
        this.orderId = orderId;
        if (shipType == utils.Configs.PLACE_RUSH_ORDER) {
            this.deliveryInstruction = deliveryInstruction;
            this.shipmentDetail = shipmentDetail;
            this.deliveryTime = deliveryTime;
        }
    }

    public Shipment(int shipType) {
        super();
        this.shipType = shipType;
    }

    public void save() {
        try {
            Statement stm = AIMSDB.getConnection().createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        var query = "INSERT INTO Shipment ( shipType, deliveryInstruction, deliveryTime, shipmentDetail, orderId) " +
                "VALUES ( ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement2 = AIMSDB.getConnection().prepareStatement(query)) {

            preparedStatement2.setInt(1, this.shipType);
            preparedStatement2.setString(2, this.deliveryInstruction);
            preparedStatement2.setString(3, this.deliveryTime);
            preparedStatement2.setString(4, this.shipmentDetail);
            preparedStatement2.setInt(5, this.orderId);
            preparedStatement2.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return String
     */
    // getter setter method
    public String getDeliveryInstruction() {
        return this.deliveryInstruction;
    }

    public boolean checkDeliveryInfo() {
        String province = new String(this.getProvince());
        List<String> supportedProvinces = Arrays.asList(utils.Configs.PROVINCES_SUPPORT);
        return supportedProvinces.contains(province);
    }
}
