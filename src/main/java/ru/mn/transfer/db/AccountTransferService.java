package ru.mn.transfer.db;

import io.micronaut.spring.tx.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Singleton
public class AccountTransferService {

    @Inject
    private AccountService accountService;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public Optional<AccountTransfer> findById(@NotNull Long accountId, @NotNull Long id) {
        String qlString = String.format("SELECT at FROM AccountTransfer at where id =:id and src_id =:src_id");
        return entityManager.createQuery(qlString, AccountTransfer.class)
                .setParameter("id", id)
                .setParameter("src_id", accountId)
                .getResultList().stream().findFirst();
    }

    @Transactional
    public AccountTransfer add(Account src, Account dest, TransferKind transferKind, BigDecimal sum) {
        AccountTransfer transfer = new AccountTransfer(src, dest, transferKind, sum);
        entityManager.persist(transfer);
        return transfer;
    }

    @Transactional
    public List<AccountTransfer> findAllTransfer(@NotNull Long accountId) {
        String qlString = String.format("SELECT at FROM AccountTransfer at where src_id =:src_id");
        return entityManager.createQuery(qlString, AccountTransfer.class)
                .setParameter("src_id", accountId)
                .getResultList();
    }

    @Transactional
    public AccountTransfer execute(Long srcAccountId, Long destAccountId, BigDecimal value) {
        Account src = accountService.findById(srcAccountId)
                .orElseThrow(() -> new IllegalArgumentException("не могу получить счет по id" + srcAccountId));
        Account dest = accountService.findById(destAccountId)
                .orElseThrow(() -> new IllegalArgumentException("не могу получить счет по id" + destAccountId));
        BigDecimal newBalance = src.getBalance().subtract(value);
        if (newBalance.compareTo(BigDecimal.ZERO) >= 0) {
            this.add(dest, src, TransferKind.IN, value);
            accountService.update(src.getId(), newBalance);
            return this.add(src, dest, TransferKind.OUT, value.negate());
        }  else {
            throw new IllegalArgumentException("превышен лимит по счету");
        }
    }

}
