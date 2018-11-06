package ru.mn.transfer.rest;

public class AddAccountParam {

    private String number;

    private AddAccountParam() {

    }

    public AddAccountParam(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
