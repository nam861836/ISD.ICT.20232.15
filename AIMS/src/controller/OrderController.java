package controller;

import common.exception.PaymentException;
import entity.order.entities.DetailResponse;
import entity.payment.TransactionResult;
import common.exception.vnPayException.ProcessingException;
import entity.order.Order;
import javafx.scene.control.Alert;
import subsystem.VnPayInterface;
import entity.order.entities.RefundTransaction;
import subsystem.vnPay.VnPaySubsystemController;
import utils.Utils;
import utils.enums.OrderStatus;

import java.io.IOException;
import java.util.List;

public class OrderController extends BaseController {

    private Order order;

    private VnPayInterface vnPayService;
    public OrderController(){
        order = new Order();
        vnPayService = new VnPaySubsystemController();
    }





    /**
     * this method gets all Order in DB and return back to display
     *
     *
     *
     */
   public List<Order> getOrders() {
       return  order.getListOrders();
   }

   private boolean validateOrderCancel(Order order) {
       if(order.getStatus().equals(OrderStatus.Rejected)){
           return false;
       }
       return true;
   }

   public TransactionResult cancelOrder(Order orderCancel) {
       var result = new TransactionResult();
       if(!validateOrderCancel(orderCancel)){
           result.setResult("FAILED");
           result.setMessage("Có lỗi xảy ra, vui lòng liên hệ AIMS TEAM để được hỗ trợ!");
           return  result;
       }
       var trans = orderCancel.getPaymentTransaction();
        var requestParams = new RefundTransaction("02", String.valueOf(trans.getAmount()), trans.getTxnRef(), trans.getTransactionNo(), Utils.formatDateTime(trans.getCreatedAt(), "yyyyMMddHHmmss"), orderCancel.getName());


       try {

           var refund =  vnPayService.refund(requestParams);
           result.setResult("SUCCESS");
           result.setMessage("REFUND SUCCESS, PLEASE CHECK YOUR BANK");

           orderCancel.setStatus(OrderStatus.Rejected);
           orderCancel.updateStatus(OrderStatus.Rejected, orderCancel.getId());
           return result;


       }
       catch (ProcessingException e){
           result.setResult("REFUND PROCESSING");
           result.setMessage("REFUND PROCESSING, BY VNPAYY, PLEASE CHECK YOUR BANK AFTER 3 DAYS");


           orderCancel.setStatus(OrderStatus.Rejected);
           orderCancel.updateStatus(OrderStatus.Rejected, orderCancel.getId());

       }
       catch (PaymentException e){
           result.setResult("REFUND FAILED");
           result.setMessage(e.getMessage());
       }
       catch (IOException e) {

           throw new RuntimeException(e);
       }

       return result;

   }

   public DetailResponse getDetailOrder(Order order) throws IOException {
        var response = vnPayService.getDetailTransaction(order);
        return response;
   }

}