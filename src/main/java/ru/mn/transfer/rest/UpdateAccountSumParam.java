package ru.mn.transfer.rest;

import java.math.BigDecimal;

public class UpdateAccountSumParam {

    private BigDecimal balance;

    private UpdateAccountSumParam() {
    }

    public UpdateAccountSumParam(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
