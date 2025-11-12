package oop.project.nhom4.model;

public class Customer {
    private String id;
    private String name;
    private String phone;
    private String dateOfBirth;
    private Integer purchaseCount;

    public Customer() {
    }

    public Customer(String id, String name, String phone, String dateOfBirth, Integer purchaseCount) {
        super();
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.purchaseCount = purchaseCount;
    }

    // getters & setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getPurchaseCount() {
        return purchaseCount;
    }
}