package controller;

import common.exception.*;
import entity.cart.Cart;
import entity.order.Order;
import entity.payment.PaymentTransaction;
import subsystem.VnPayInterface;
import subsystem.vnPay.VnPaySubsystemController;
import common.exception.vnPayException.TransactionExceptionHolder;
import utils.enums.OrderStatus;

import java.io.IOException;
import java.text.ParseException;
import java.util.Hashtable;
import java.util.Map;

/**
 * This {@code PaymentController} class control the flow of the payment process
 * in our AIMS Software.
 *
 * @author hieud
 */
public class PaymentController extends BaseController {


    /**
     * Represent the Interbank subsystem
     */
    private VnPayInterface vnPayService;


    public PaymentController(){
        this.vnPayService = new VnPaySubsystemController();
    }

    //Control Coupling
    public Map<String, String> makePayment(Map<String, String> res, int orderId) {
        Map<String, String> result = new Hashtable<String, String>();
        PaymentTransaction trans = null;
        try {

            trans = this.vnPayService.makePaymentTransaction(res);
            if(trans != null) trans.save(orderId);
            var order = new Order();
            if(trans.getErrorCode().equals("00")){
                result.put("RESULT", "PAYMENT SUCCESSFUL!");
                result.put("MESSAGE", "You have succesffully paid the order!");
                order.updateStatus(OrderStatus.Paid, orderId);
            } else{
                var ex = TransactionExceptionHolder.getInstance().getException(trans.getErrorCode());
                if(ex != null){
                    result.put("MESSAGE", ex.getMessage());
                    result.put("RESULT", "PAYMENT FAILED!");
                    order.updateStatus(OrderStatus.Rejected, orderId);
                }else{
                    result.put("MESSAGE", "Unknown error, contact to AIMS Team to get helping please.");
                    result.put("RESULT", "PAYMENT FAILED!");
                    order.updateStatus(OrderStatus.Rejected, orderId);
                }
            }


        } catch ( UnrecognizedException ex) {
            result.put("MESSAGE", "Fail occur, contact to AIMS Team to get helping please.");
            result.put("RESULT", "PAYMENT FAILED!");

        }
        catch (ParseException e) {
            throw new RuntimeException(e);
        }


        return result;
    }

    /**
     * Gen url thanh toán vnPay
     * @param amount
     * @param content
     * @return
     */

    //Functional Cohesion
    //Data Coupling
    public String getUrlPay(int amount, String content){

        String url = null;


        try {
            url = this.vnPayService.generatePayUrl(amount, content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return url;
    }

    //Functional Cohesion
    //Control Coupling
    public void emptyCart() {
        Cart.getCart().emptyCart();
    }
}