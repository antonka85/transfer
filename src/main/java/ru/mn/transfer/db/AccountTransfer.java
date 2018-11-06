package ru.mn.transfer.db;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "accounttransfer")
public class AccountTransfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "src_id")
    private Account src;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "dest_id")
    private Account dest;

    @NotNull
    @Column(name = "sum", nullable = false)
    private BigDecimal sum;

    @Convert(converter = TransferKindToJPAConverter.class)
    @Column(name = "transferKind", nullable = false)
    private TransferKind transferKind;

    private AccountTransfer() {

    }

    public AccountTransfer(@NotNull Account src, @NotNull Account dest,
                           @NotNull TransferKind transferKind, @NotNull BigDecimal sum) {
        this.src = src;
        this.dest = dest;
        this.transferKind = transferKind;
        this.sum = sum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getSrc() {
        return src;
    }

    public void setSrc(Account src) {
        this.src = src;
    }

    public Account getDest() {
        return dest;
    }

    public void setDest(Account dest) {
        this.dest = dest;
    }

    public TransferKind getTransferKind() {
        return transferKind;
    }

    public void setTransferKind(TransferKind transferKind) {
        this.transferKind = transferKind;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }
}
