package ru.mn.transfer.rest;

import java.math.BigDecimal;

public class TransferParam {

    private Long dest;
    private BigDecimal sum;

    private TransferParam() {

    }

    public TransferParam(Long dest, BigDecimal sum) {
        this.dest = dest;
        this.sum = sum;
    }

    public Long getDest() {
        return dest;
    }

    public void setDest(Long dest) {
        this.dest = dest;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }
}
