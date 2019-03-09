package com.manager.portfolio.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.Document;

import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {

    @JsonProperty(value = "first_name")
    private String fname;

    @JsonProperty(value = "last_name")
    private String lname;

    @JsonProperty(value = "dob")
    private String dob;

    @JsonProperty(value = "age")
    private int age;

    @JsonProperty(value = "company_name")
    private String companyName;

    @NotNull
    @JsonProperty(value = "contact")
    private long contact;

    @NotNull
    @JsonProperty(value = "email_id")
    private String emailId;

    @NotNull
    @JsonProperty(value = "password")
    private String password;

    @NotNull
    @JsonProperty(value = "confirm_password")
    private String confirmPwd;

    @NotNull
    @JsonProperty(value = "pan_card")
    private String panNo;

    @NotNull
    @JsonProperty(value = "acc_no")
    private long accountNumber;

    @NotNull
    @JsonProperty(value = "bank_name")
    private String bankName;

    @JsonProperty(value = "type")
    private String type;

    @NotNull
    @JsonProperty(value = "risk_factor")
    private int riskFactor;

    @NotNull
    @JsonProperty(value = "target_return")
    private int returnFactor;

    @NotNull
    @JsonProperty(value = "budget")
    private long budget;

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public long getContact() {
        return contact;
    }

    public void setContact(long contact) {
        this.contact = contact;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPwd() {
        return confirmPwd;
    }

    public void setConfirmPwd(String confirmPwd) {
        this.confirmPwd = confirmPwd;
    }

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRiskFactor() {
        return riskFactor;
    }

    public void setRiskFactor(int riskFactor) {
        this.riskFactor = riskFactor;
    }

    public int getReturnFactor() {
        return returnFactor;
    }

    public void setReturnFactor(int returnFactor) {
        this.returnFactor = returnFactor;
    }

    public long getBudget() {
        return budget;
    }

    public void setBudget(long budget) {
        this.budget = budget;
    }

    /**
     * Converts the dto to mongo document type.
     *
     * @return
     */
    public Document toDocument() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(this);
            return Document.parse(json);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
