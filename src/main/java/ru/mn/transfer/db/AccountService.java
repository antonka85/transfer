package ru.mn.transfer.db;

import io.micronaut.spring.tx.annotation.Transactional;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Singleton
public class AccountService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public Optional<Account> findById(@NotNull Long id) {
        return Optional.ofNullable(entityManager.find(Account.class, id));
    }

    @Transactional
    public Account add(String  number) {
        Account transfer = new Account(number, BigDecimal.ZERO);
        entityManager.persist(transfer);
        return transfer;
    }

    @Transactional
    public int update(Long id, BigDecimal balance) {
        return entityManager.createQuery("UPDATE Account a SET balance = :balance where id = :id")
                .setParameter("balance", balance)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Transactional
    public List<Account> findAll() {
        String query = "SELECT a FROM Account a";
        return entityManager.createQuery(query, Account.class).getResultList();
    }
}
