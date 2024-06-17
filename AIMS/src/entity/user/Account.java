package entity.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.exception.LoginFailedException;
import common.exception.SignupFailedException;
import entity.db.AIMSDB;

public class Account {
    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    private int id;
    private String name;
    private String username;
    private String password;
    private String birthDate;
    private String phone;
    private int role;

    public Account(String name, String username, String password,String birthDate, String phone) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.birthDate = birthDate;
        this.phone = phone;
        this.role = 1;
    }

    public Account(int id, String name, String username, String password,String birthDate, String phone, int role) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.birthDate = birthDate;
        this.phone = phone;
        this.role = role;
    }

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * @return String
     */
    // getter and setter
    public String getName() {
        return this.name;
    }


    /**
     * @param name
     */
    public void setusername(String name) {
        this.name = name;
    }

    /**
     * @return String
     */
    public String getBirthdate() {
        return this.birthDate;
    }


    /**
     * @param address
     */
    public void setBirthdate(String birthDate) {
        this.birthDate = birthDate;
    }


    /**
     * @return String
     */
    public String getPhone() {
        return this.phone;
    }


    /**
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Account login() throws SQLException {
        if (validateLoginInformation()) {
            Account tmp = getAccountByUsername(username);
            if (tmp.password.compareTo(this.password) == 0) {
                System.out.println(tmp.getName());
                return tmp;
            }
            else {
                throw new LoginFailedException("Sai mật khẩu");
            }
        } else {
            throw new LoginFailedException("Sai format tài khoản/mật khẩu");
        }
    }

    public boolean validateLoginInformation() {
        Matcher matcher = pattern.matcher(password);
        if (matcher.matches()) {
            return true;
        } else {
            throw new LoginFailedException("Sai format mật khẩu");
        }
    }

    public boolean validateSignUpInformation() throws SQLException {
        Matcher matcher = pattern.matcher(password);
        String message = null;
        try {
            Account acc = getAccountByUsername(username);
            message = "Tài khoản đã tồn tại";
        } catch (LoginFailedException e) {
            message = null;
        }
        if (!matcher.matches()) 
            message = "Sai format mật khẩu";
        if (username.length() < 8 || username.length() > 20) 
            message = "Tài khoản phải có độ dài từ 8-20 kí tự";
        if (name.length() > 30) 
            message = "Tên không được vượt quá 30 kí tự";
        if (phone.length() < 10 || phone.length() > 13)
            message = "Số điện thoại không hợp lệ";

        if (message == null) {
            return true;
        } else {
            throw new SignupFailedException(message);
        }

    }

    public Account getAccountByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM " +
                "User " +
                "where username = '" + username + "';";
        Statement stm = AIMSDB.getConnection().createStatement();
        ResultSet res = stm.executeQuery(sql);
        if (res.next()) {
            return new Account(res.getInt("id"), res.getString("name"), res.getString("username"), 
                        res.getString("password"), res.getString("birthdate"), res.getString("phoneNumber"), res.getInt("role"));
        } else 
        {
            throw new LoginFailedException("Không tồn tại tài khoản");
        }
    }

    public Account signup() throws SQLException {
        if (validateSignUpInformation()) {
            String sql = "INSERT INTO User (username,name, password,birthDate,phoneNumber,role) VALUES ('"
                        + username + "','"
                        + name + "','"
                        + password + "','"
                        + birthDate + "','"
                        + phone + "','"
                        + "1'"
                    +");";
            Statement stm = AIMSDB.getConnection().createStatement();
            stm.executeUpdate(sql);

            return getAccountByUsername(username);
        } else {
            throw new SignupFailedException("Sai thông tin");
        }
    }
}
