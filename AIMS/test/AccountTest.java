import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import entity.user.Account;

import static org.junit.jupiter.api.Assertions.*;
public class AccountTest {

private Account account= new Account("Test Name", "testUsername", "TestPassword123!", "12/05/2002", "0123456789");  

    
    // Đặt mật khẩu đúng định dạng
    @Test
    public void test1() {
       
       Account account1 = new Account("Test Name", "testUsername", "TestPassword1!", "03/02/2002", "089124678");
        boolean isValid = account.validateLoginInformation();

        assertTrue(isValid);
    }
   // Đặt mật khẩu không đúng định dạng
    @Test
    public void test2() {
     
        account = new Account("Test Name", "testUsername", "123456789123145678", "03/02/2002", "089124678");
        try {
			 account.validateSignUpInformation();
		    } catch (Exception e) {
		     
		      assertEquals(e.getMessage(), "Sai format mật khẩu");
		    }
     
    }
  // Đặt thông tin user đúng
    @Test
    public void test3() throws SQLException {
        account = new Account("Test Name", "testUsername", "Nam1234!", "12/05/2002", "0123456789");
       
        boolean isValid = account.validateSignUpInformation();

        assertTrue(isValid);
    }
// Kiểm tra username sai
    @Test
    public void test4() {
      String message = "";
        account = new Account("Test Name", "Viet", "Viet0123!", "03/02/2002", "089124678");
        try {
			 account.validateSignUpInformation();
       
		    } catch (Exception e) {
		    message = e.getMessage();
		      
		    }
        assertEquals(message, "Tài khoản phải có độ dài từ 8-20 kí tự");
    }
// Kiểm tra username sai
    @Test
    public void test5() {
      String message = "";
        account = new Account("Test Name", "Viet198234719847138149812049819871124987129481723432948203948", "Nam0123!", "03/02/2002", "089124678");
        try {
			 account.validateSignUpInformation();
       
		    } catch (Exception e) {
		    message = e.getMessage();
		      
		    }
        assertEquals(message, "Tài khoản phải có độ dài từ 8-20 kí tự");
    }
// Kiểm tra tên sai
    @Test
    public void test6() {
    account = new Account("8fasdjkfhasdjkfajskdfjalksdfjalksdjfalksdjfalksdfjalksdfjalksdfjalksdfjalksdjfalksdfjalksdjfaslkdjfalksdjfalksdjfalksdjfalksdf", "testUsername", "123456789", "03/02/2002", "089124678");
        try {
			 account.validateSignUpInformation();
		    } catch (Exception e) {
		     
		      assertEquals(e.getMessage(), "Tên không được vượt quá 30 kí tự");
		    }
       
    }
// Kiểm tra số điện thoại sai
 @Test
    public void test7() {
    account = new Account("Test Name", "testUsername", "123456789", "03/02/2002", "0896");
        try {
			 account.validateSignUpInformation();
		    } catch (Exception e) {
		     
		      assertEquals(e.getMessage(), "Số điện thoại không hợp lệ");
		    }
       
    }
    // Kiểm tra số điện thoại sai
 @Test
    public void test8() {
    account = new Account("Test Name", "testUsername", "123456789", "12/05/2002", "0896124327852782782785785785728278");
        try {
			 account.validateSignUpInformation();
		    } catch (Exception e) {
		     
		      assertEquals(e.getMessage(), "Số điện thoại không hợp lệ");
		    }
       
    }
}