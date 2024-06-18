package entity.shipping;

import entity.db.AIMSDB;
import jdk.jshell.spi.ExecutionControl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Shipment {

    private int shipType;

    private String deliveryInstruction;
    private int orderId;

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Shipment(){}
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

    private String shipmentDetail;

    private String deliveryTime;

    public Shipment(int shipType, String deliveryInstruction, String shipmentDetail, String deliveryTime, int orderId) {
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
        this.shipType =  shipType;
    }

    public void save(){
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
    //getter setter method
    public String getDeliveryInstruction() {
        return this.deliveryInstruction;
    }

}
