package subsystem.vnPay;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import common.exception.*;
import entity.order.Order;
import entity.order.entities.DetailResponse;
import entity.order.entities.RefundTransaction;
import entity.payment.PaymentTransaction;
import javafx.scene.control.Alert;
import subsystem.VnPayInterface;
import com.google.gson.JsonObject;
import entity.order.entities.RefundResponse;
import common.exception.vnPayException.TransactionExceptionHolder;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 */
public class VnPaySubsystemController implements VnPayInterface {

    private final String PAY_COMMAND = "pay";
    private final String VERSION = "2.1.0";


    /**
     * Data coupling
     * Functional cohesion
     * @param money
     * @param contents
     */
    @Override
    public String generatePayUrl(int money, String contents) throws IOException {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        long amount = money * 100L * 1000;


        String vnp_TxnRef = Config.getRandomNumber(8);
        String vnp_IpAddr = Config.getIpAddress();

        String vnp_TmnCode = Config.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");

        vnp_Params.put("vnp_BankCode", "");

        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", contents);
        vnp_Params.put("vnp_OrderType", orderType);


        vnp_Params.put("vnp_Locale", "vn");

        vnp_Params.put("vnp_ReturnUrl", Config.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        return Config.vnp_PayUrl + "?" + queryUrl;
    }

    /**
     * Data coupling
     * Functional Cohesion
     * @param response
     * @return PaymentTransaction
     */
    public PaymentTransaction makePaymentTransaction(Map<String, String> response) throws ParseException {
        if (response == null) {
            return null;
        }

        // Create Payment transaction
        String errorCode = response.get("vnp_TransactionStatus");
        String cardType = response.get("vnp_CardType");

        String txnRef = response.get("vnp_TxnRef");
        String transactionId = response.get("vnp_TransactionNo");
        String transactionContent = response.get("vnp_OrderInfo");
        int amount = Integer.parseInt((String) response.get("vnp_Amount")) / 100;
        String createdAt = response.get("vnp_PayDate");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = dateFormat.parse(createdAt);
        PaymentTransaction trans = new
                PaymentTransaction(errorCode, transactionId, transactionContent, amount, date, cardType, txnRef);
        return trans;
    }

    public RefundResponse refund(RefundTransaction refundTransaction) throws  IOException, PaymentException {

        //Command: refund
        String vnp_RequestId = Config.getRandomNumber(8);
        String vnp_Version = "2.1.0";
        String vnp_Command = "refund";
        String vnp_TmnCode = Config.vnp_TmnCode;
        String vnp_TransactionType = refundTransaction.getTrantype(); // 02 or 03
        String vnp_TxnRef = refundTransaction.getOrder_id();

        long amount = Long.parseLong(refundTransaction.getAmount())*100;
        String vnp_Amount = String.valueOf(amount);
        String vnp_OrderInfo = "Hoan tien GD OrderId:" + vnp_TxnRef;

        String vnp_TransactionNo = refundTransaction.getTransactionNo();
        String vnp_TransactionDate = refundTransaction.getTrans_date();
        String vnp_CreateBy = refundTransaction.getUser();

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());

        String vnp_IpAddr = Config.getIpAddress();

        JsonObject vnp_Params =new JsonObject ();

        vnp_Params.addProperty("vnp_RequestId", vnp_RequestId);
        vnp_Params.addProperty("vnp_Version", vnp_Version);
        vnp_Params.addProperty("vnp_Command", vnp_Command);
        vnp_Params.addProperty("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.addProperty("vnp_TransactionType", vnp_TransactionType);
        vnp_Params.addProperty("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.addProperty("vnp_Amount", vnp_Amount);
        vnp_Params.addProperty("vnp_OrderInfo", vnp_OrderInfo);

        if(vnp_TransactionNo != null && !vnp_TransactionNo.isEmpty())
        {
            vnp_Params.addProperty("vnp_TransactionNo", vnp_TransactionNo);
        }

        vnp_Params.addProperty("vnp_TransactionDate", vnp_TransactionDate);
        vnp_Params.addProperty("vnp_CreateBy", vnp_CreateBy);
        vnp_Params.addProperty("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.addProperty("vnp_IpAddr", vnp_IpAddr);

        String hash_Data= String.join("|", vnp_RequestId, vnp_Version, vnp_Command, vnp_TmnCode,
                vnp_TransactionType, vnp_TxnRef, vnp_Amount, vnp_TransactionNo, vnp_TransactionDate,
                vnp_CreateBy, vnp_CreateDate, vnp_IpAddr, vnp_OrderInfo);

        String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hash_Data.toString());

        vnp_Params.addProperty("vnp_SecureHash", vnp_SecureHash);

        var response = refundRequest(vnp_Params);
        return handleRefundResponse(response);

    }
    private String refundRequest(JsonObject vnp_Params) throws IOException {
        URL url = new URL (Config.vnp_ApiUrl);
        var con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");

        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(vnp_Params.toString());
        wr.flush();
        wr.close();
        int responseCode = con.getResponseCode();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String output;
        StringBuffer response = new StringBuffer();
        while ((output = in.readLine()) != null) {
            response.append(output);
        }
        in.close();
        return response.toString();
    }
    private RefundResponse handleRefundResponse(String response) throws PaymentException {
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> resultMap = new Gson().fromJson(response, type);

        var trans = new RefundResponse();
        trans.setErrorCode(resultMap.get("vnp_ResponseCode "));
        trans.setAmount((int) (Long.parseLong(resultMap.get("vnp_Amount")) / 100));
        trans.setTransactionContent(resultMap.get("vnp_OrderInfo"));
        String createdAt = resultMap.get("vnp_PayDate");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

        Date date = null;
        try {
            if(createdAt != null)
            date = dateFormat.parse(createdAt);
        } catch (ParseException e) {
            date = null;
        }
        trans.setCreatedAt(date);
        trans.setTransactionContent(resultMap.get("vnp_TransactionNo"));
        trans.setTxnRef(resultMap.get("vnp_TxnRef"));
        var ex = TransactionExceptionHolder.getInstance().getException(trans.getErrorCode());
        if(ex != null){
            throw ex;
        }
        return trans;
    }

    public DetailResponse getDetailTransaction(Order order) throws IOException {
        PaymentTransaction transaction = order.getPaymentTransaction();
        JsonObject params =new JsonObject ();

        String vnp_RequestId = Config.getRandomNumber(8);
        params.addProperty("vnp_RequestId", vnp_RequestId);
        String vnp_Version = "2.1.0";
        params.addProperty("vnp_Version", vnp_Version);
        String vnp_Command = "querydr";
        params.addProperty("vnp_Command", vnp_Command);

        String vnp_TmnCode = Config.vnp_TmnCode;
        params.addProperty("vnp_TmnCode", vnp_TmnCode);
        String vnp_TxnRef = transaction.getTxnRef();
        params.addProperty("vnp_TxnRef", vnp_TxnRef);

        String vnp_OrderInfo = transaction.getTransactionContent();
        params.addProperty("vnp_OrderInfo", vnp_OrderInfo);
        String vnp_TransactionNo = transaction.getTransactionNo();
        params.addProperty("vnp_TransactionNo", vnp_TransactionNo);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_TransactionDate = formatter.format(transaction.getCreatedAt());
        params.addProperty("vnp_TransactionDate", vnp_TransactionDate);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        String vnp_CreateDate = formatter.format(cld.getTime());
        params.addProperty("vnp_CreateDate", vnp_CreateDate);

        String vnp_IpAddr = Config.getIpAddress();
        params.addProperty("vnp_IpAddr", vnp_IpAddr);

        String hash_Data= String.join("|", vnp_RequestId, vnp_Version, vnp_Command, vnp_TmnCode, vnp_TxnRef, vnp_TransactionDate, vnp_CreateDate, vnp_IpAddr, vnp_OrderInfo);

        String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hash_Data.toString());

        params.addProperty("vnp_SecureHash", vnp_SecureHash);

        System.out.println(params.toString());

        //return Config.vnp_ApiUrl + params.toString();

        URL url = new URL (Config.vnp_ApiUrl);
        var con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");

        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(params.toString());
        wr.flush();
        wr.close();
        int responseCode = con.getResponseCode();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String output;
        StringBuffer response = new StringBuffer();
        while ((output = in.readLine()) != null) {
            response.append(output);
        }
        in.close();

        System.out.println(responseCode);

        return handleDetailResponse(response.toString());
        /*return handleRefundResponse(response.toString());*/

    }

    public DetailResponse handleDetailResponse(String res) {
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> resultMap = new Gson().fromJson(res, type);

        var trans = new DetailResponse();

        var errorCode = resultMap.get("vnp_ResponseCode");
        if (!errorCode.equals("00")) {
            String message = null;
            switch (errorCode) {
                case "02":
                    message = "Mã định danh kết nối không hợp lệ (kiểm tra lại TmnCode)";
                    break;
                case "03":
                    message = "Dữ liệu gửi sang không đúng định dạng";
                    break;
                case "91":
                    message = "Không tìm thấy giao dịch yêu cầu";
                    break;
                case "94":
                    message = "Yêu cầu trùng lặp, duplicate request trong thời gian giới hạn của API";
                    break;
                case "97":
                    message = "Checksum không hợp lệ";
                    break;
                case "99":
                    message = "Lỗi không xác định";
                    break;
                default:
                    message = "Lỗi không xác định";
                    break;
            }
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
            throw new RuntimeException(message);
        }
        trans.setAmount(resultMap.get("vnp_Amount"));
        trans.setOrderInfo(resultMap.get("vnp_OrderInfo"));
        String payDate = resultMap.get("vnp_PayDate");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            Date day = dateFormat.parse(payDate);
            trans.setPayDate(day.toString());
        }
        catch (ParseException e){
            System.out.println(e);
        }

        trans.setBankCode(resultMap.get("vnp_BankCode"));
        trans.setTransactionId(resultMap.get("vnp_TransactionNo"));

        String transType = resultMap.get("vnp_TransactionType");
        switch (transType) {
            case "01":
                transType = "Giao dich thanh toan";
                break;
            case "02":
                transType = "Giao dich hoan tra mot phan";
                break;
            case "03":
                transType = "Giao dich hoan tra toan bo";
                break;
            default:
                break;
        }
        trans.setTransactionType(transType);
        String statusText ="";
        var status = resultMap.get("vnp_TransactionStatus");
        switch (status) {
            case "00":
                statusText = "Giao dịch thành công";
                break;
            case "01":
                statusText = "Giao dịch chưa hoàn tất";
                break;
            case "02":
                statusText = "Giao dịch bị lỗi";
                break;
            case "04":
                statusText = "Giao dịch đảo";
                break;
            case "05":
                statusText = "VNPAY đang xử lý giao dịch này";
                break;
            case "06":
                statusText = "VNPAY đã gửi yêu cầu hoàn tiền sang Ngân hàng";
                break;
            case "07":
                statusText = "Giao dịch bị nghi ngờ gian lận";
                break;
            case "09":
                statusText = "GD Hoàn trả bị từ chối";
                break;
            default:
                break;
        }
        trans.setTransactionStatus(statusText);
        trans.setPromotionCode(resultMap.get("vnp_PromotionCode"));
        trans.setPromotionAmount(resultMap.get("vnp_PromotionAmount"));

        return trans;
    }
}