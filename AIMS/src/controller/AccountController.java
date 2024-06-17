package controller;

import java.sql.SQLException;

import common.exception.SignupFailedException;
import entity.user.Account;

public class AccountController extends BaseController {
    private Account acc;
    private static final AccountController ACCOUNT_CONTROLLER = new AccountController();

    public void login(String username, String password) throws SQLException {
        Account loginAccount = new Account(username, password);
        acc = loginAccount.login();
        System.out.println( "Trong ACCCONTROLLER " +acc.getName());
    }

    public Account getLoggedInAccount() {
        return acc;
    }

    public void signup(String name, String username, String password, String confirmPassword, String birthDate, String phoneNumber) throws SQLException {
        if (password.compareTo(confirmPassword) == 0) {
            Account loginAccount = new Account(name, username, password, birthDate, phoneNumber);
            acc = loginAccount.signup();
        } else {System.out.println(password + " " + confirmPassword);
            throw new SignupFailedException("Mật khẩu và xác nhận mật khẩu không trùng nhau");
        }
    }
    
    public static AccountController getAccountController() {
        return ACCOUNT_CONTROLLER;
    }
}
