package subsystem.vnPay;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import common.exception.PaymentException;
import common.exception.vnPayException.TransactionExceptionHolder;
import entity.order.entities.RefundResponse;
import entity.order.entities.RefundTransaction;

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

public class RefundVNPay {
    private RefundTransaction refundTrans;
    private RefundResponse refundRes;

    public RefundVNPay(RefundTransaction refundTrans){
        this.refundTrans = refundTrans;
    }

    public RefundVNPay(RefundResponse refundRes){
        this.refundRes = refundRes;
    }

    public RefundResponse refund() throws IOException, PaymentException {

        //Command: refund
        String vnp_RequestId = Config.getRandomNumber(8);
        String vnp_Version = "2.1.0";
        String vnp_Command = "refund";
        String vnp_TmnCode = Config.vnp_TmnCode;
        String vnp_TransactionType = refundTrans.getTrantype(); // 02 or 03
        String vnp_TxnRef = refundTrans.getOrder_id();

        long amount = Long.parseLong(refundTrans.getAmount())*100;
        String vnp_Amount = String.valueOf(amount);
        String vnp_OrderInfo = "Hoan tien GD OrderId:" + vnp_TxnRef;

        String vnp_TransactionNo = refundTrans.getTransactionNo();
        String vnp_TransactionDate = refundTrans.getTrans_date();
        String vnp_CreateBy = refundTrans.getUser();

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
}


