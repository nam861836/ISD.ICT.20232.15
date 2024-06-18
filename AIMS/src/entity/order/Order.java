package entity.order;

import controller.AccountController;
import entity.db.AIMSDB;
import entity.payment.PaymentTransaction;
import entity.shipping.Shipment;
import entity.user.Account;
import utils.Configs;
import utils.enums.OrderStatus;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Order {


    private String phone;
    private int id;
    private int shippingFees;
    private List<OrderItem> listOrderItem;
    private Shipment shipment;
    private AccountController loggedAcc;
    //private Account acc;
    private String name;
    private String province;
    private String instruction;
    private OrderStatus status;
    private PaymentTransaction paymentTransaction;

    private int userID;

    public PaymentTransaction getPaymentTransaction() {
        return paymentTransaction;
    }

    public OrderStatus getStatus(){
        return status;
    }

    public void setStatus(OrderStatus stt){
        status = stt;
    }
    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    private String address;

    public List<OrderItem> getListOrderItem() {
        return listOrderItem;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Order() {
        this.listOrderItem = new ArrayList<OrderItem>();
    }

    public Order(List listOrderProduct) {
        this.listOrderItem = listOrderProduct;
    }

    public List<Order> getListOrders(){
        List<Order> orders = new ArrayList<>();
        loggedAcc = AccountController.getAccountController();
        userID = loggedAcc.getLoggedInAccount().getId();

        try {
            var query = "SELECT o.id, o.name, o.province, o.address, o.phone, o.shippingFees, o.status, " +
                    "s.shipType, s.deliveryInstruction, s.deliveryTime, s.shipmentDetail, " +
                    "p.transactionNo, p.txnRef, p.cardType, p.amount, p.createdAt, p.content " +
                    "FROM `Order` o " +
                    "LEFT JOIN Shipment s ON s.orderId = o.id " +
                    "LEFT JOIN PaymentTransaction p ON o.id = p.orderID " +
                    "WHERE o.userID = ? " +  // Add the condition here
                    "ORDER BY p.createdAt DESC";

            PreparedStatement preparedStatement = AIMSDB.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, userID);  // Set the userID parameter
            ResultSet resultSet = preparedStatement.executeQuery();

            HashMap<Integer, Order> orderMap = new HashMap<>();

            while (resultSet.next()) {
                int orderId = resultSet.getInt("id");
                Order order = orderMap.get(orderId);

                if (order == null) {
                    order = new Order();
                    order.setId(orderId);
                    order.setName(resultSet.getString("name"));
                    order.setProvince(resultSet.getString("province"));
                    order.setAddress(resultSet.getString("address"));
                    order.setPhone(resultSet.getString("phone"));
                    order.setShippingFees(resultSet.getInt("shippingFees"));
                    order.setStatus(OrderStatus.values()[resultSet.getInt("status")]);


                    var shipment = new Shipment();
                    shipment.setShipType(resultSet.getInt("shipType"));
                    shipment.setDeliveryInstruction(resultSet.getString("deliveryInstruction"));
                    shipment.setDeliveryTime(resultSet.getString("deliveryTime"));
                    shipment.setShipmentDetail(resultSet.getString("shipmentDetail"));

                    order.setShipment(shipment);

                    var paymentTransaction = new PaymentTransaction();
                    paymentTransaction.setTransactionNo(resultSet.getString(("transactionNo")));
                    paymentTransaction.setTxnRef(resultSet.getString(("txnRef")));
                    paymentTransaction.setCardType(resultSet.getString(("cardType")));
                    paymentTransaction.setAmount(resultSet.getInt(("amount")));
                    paymentTransaction.setCreatedAt(resultSet.getDate(("createdAt")));
                    paymentTransaction.setTransactionContent(resultSet.getString(("content")));



                    order.paymentTransaction = paymentTransaction;

                    orderMap.put(orderId, order);
                    orders.add(order);
                }


                OrderItem orderItem = new OrderItem();  // Replace with actual logic
                order.addOrderItem(orderItem);
            }

            resultSet.close();
            preparedStatement.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return orders;
    }

    public void createOrderEntity(){
        try {
            Statement stm = AIMSDB.getConnection().createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        loggedAcc = AccountController.getAccountController();
        userID = loggedAcc.getLoggedInAccount().getId();

        String query = "INSERT INTO 'Order' (name, province, address, phone, shippingFees, userID) " +
                "VALUES ( ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = AIMSDB.getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, province);
            preparedStatement.setString(3, address);
            preparedStatement.setString(4, phone);
            preparedStatement.setInt(5, shippingFees);
            preparedStatement.setInt(6, userID);



            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating order failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    id = generatedKeys.getInt(1);

                } else {
                    throw new SQLException("Creating order failed, no ID obtained.");
                }
            }
            shipment.setOrderId(id);
            shipment.save();
            listOrderItem.forEach(medio -> {
                medio.save(id);
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateStatus(OrderStatus status, int orderId) {
        try {
            String query = "UPDATE `Order` SET status = ? WHERE id = ?";
            try (PreparedStatement preparedStatement = AIMSDB.getConnection().prepareStatement(query)) {
                if (status != null) {
                    preparedStatement.setInt(1, status.ordinal());
                } else {
                    throw new IllegalArgumentException("Status cannot be null");
                }
                preparedStatement.setInt(2, orderId);

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Update order failed, no rows affected.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @param om
     */
    public void addOrderItem(OrderItem om) {
        this.listOrderItem.add(om);
    }


    /**
     * @param orderItem
     */
    public void removeOrderItem(OrderItem orderItem) {
        this.listOrderItem.remove(orderItem);
    }


    /**
     * @return int
     */
    public int getShippingFees() {
        return shippingFees;
    }

    /**
     * @param shippingFees
     */
    public void setShippingFees(int shippingFees) {
        this.shippingFees = shippingFees;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    /**
     * @return int
     */
    public int getAmount() {
        double amount = 0;
        for (Object object : listOrderItem) {
            OrderItem om = (OrderItem) object;
            amount += om.getPrice();
        }
        return (int) (amount + (Configs.PERCENT_VAT / 100) * amount);
    }

    public void setPaymentTransaction(PaymentTransaction paymentTransaction) {
    }
}
