package entity.invoice;

import entity.order.Order;
import entity.shipping.Shipment;

public class Invoice {

    private Order order;
    private int amount;
    public Invoice() {

    }

    public Invoice(Order order) {
        this.order = order;
    }


    /**
     * @return Order
     */
    public Order getOrder() {
        return order;
    }

    /**
     * @return int
     */
    public int getAmount() {
        return amount;
    }

    /**
     * @param amount
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void saveInvoice() {

    }

    public int getShipType(){
        Shipment shipment = this.order.getShipment();
        int shipType = shipment.getShipType();
        return shipType;
    }
}
