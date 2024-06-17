package entity.user;

public class User {

    private int id;
    private String name;
    private String email;
    private String address;
    private String phone;

    public User(int id, String name, String email, String address, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.phone = phone;
    }


    /**
     * @return String
     */
    // override toString method
    @Override
    public String toString() {
        return "{" +
                "  username='" + name + "'" +
                ", email='" + email + "'" +
                ", address='" + address + "'" +
                ", phone='" + phone + "'" +
                "}";
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
    public String getEmail() {
        return this.email;
    }


    /**
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }


    /**
     * @return String
     */
    public String getAddress() {
        return this.address;
    }


    /**
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
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

}
