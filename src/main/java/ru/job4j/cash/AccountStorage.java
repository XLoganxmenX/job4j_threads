package ru.job4j.cash;

import net.jcip.annotations.ThreadSafe;

import java.util.HashMap;
import java.util.Optional;

@ThreadSafe
public class AccountStorage {
    private final HashMap<Integer, Account> accounts = new HashMap<>();

    public boolean add(Account account) {
        synchronized (accounts) {
            return accounts.putIfAbsent(account.id(), account) == null;
        }
    }

    public boolean update(Account account) {
        synchronized (accounts) {
            return accounts.replace(account.id(), accounts.get(account.id()), account);
        }
    }

    public void delete(int id) {
        synchronized (accounts) {
            accounts.remove(id);
        }
    }

    public Optional<Account> getById(int id) {
        synchronized (accounts) {
            return Optional.ofNullable(accounts.get(id));
        }
    }

    public boolean transfer(int fromId, int toId, int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount is negative");
        }

        boolean result = false;
        synchronized (accounts) {
            Account fromAccount = getById(fromId)
                    .orElseThrow(() -> new IllegalStateException("Not found account by id = " + fromId));
            Account toAccount = getById(toId)
                    .orElseThrow(() -> new IllegalStateException("Not found account by id = " + toId));

            if (fromAccount.amount() >= amount) {
                Account newStateFromAccount = new Account(fromId, fromAccount.amount() - amount);
                Account newStateToAccount = new Account(toId, toAccount.amount() + amount);

                update(newStateFromAccount);
                update(newStateToAccount);
                result = true;
            }
        }

        return result;
    }
}