package subsystem.vnPay;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import entity.order.Order;
import entity.order.entities.DetailResponse;
import entity.payment.PaymentTransaction;
import javafx.scene.control.Alert;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

public class PayResponseVNPay {
    private Order order;
    public PayResponseVNPay(Order order){
        this.order = order;
    }

    public DetailResponse getDetailTransaction() throws IOException {
        if (order == null) {
            return null;
        }

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
