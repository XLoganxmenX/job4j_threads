package ru.job4j.cash;

import net.jcip.annotations.ThreadSafe;

import java.util.HashMap;
import java.util.Optional;

@ThreadSafe
public class AccountStorage {
    private final HashMap<Integer, Account> accounts = new HashMap<>();

    public boolean add(Account account) {
        boolean result = false;
        synchronized (accounts) {
            if (!accounts.containsKey(account.id())) {
                accounts.put(account.id(), account);
                result = true;
            }
        }
        return result;
    }

    public boolean update(Account account) {
        boolean result = false;
        synchronized (accounts) {
            if (accounts.containsKey(account.id())) {
                accounts.replace(account.id(), account);
                result = true;
            }
        }
        return result;
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
        boolean result = false;

        if (amount > 0) {
            synchronized (accounts) {
                Account fromAccount = validateAccount(fromId);
                if (isNegativeBalance(fromAccount)) {
                    throw new IllegalArgumentException("Operation cannot be performed! "
                            + "Account balance is less than zero");
                }
                Account toAccount = validateAccount(toId);

                int newStateOfAmountFromAccount = fromAccount.amount() - amount;
                Account newStateFromAccount = new Account(fromId, newStateOfAmountFromAccount);

                if (isNegativeBalance(newStateFromAccount)) {
                    throw new IllegalArgumentException("Insufficient funds on balance");
                }

                int newStateOfAmountToAccount = toAccount.amount() + amount;
                Account newStateToAccount = new Account(toId, newStateOfAmountToAccount);

                update(newStateFromAccount);
                update(newStateToAccount);
                result = true;
            }
        }

        return result;
    }

    private Account validateAccount(int accountId) {
        Optional<Account> account = getById(accountId);
        if (account.isEmpty()) {
            throw new IllegalArgumentException(String.format("Account with id - %d not found", accountId));
        }

        return account.get();
    }

    private boolean isNegativeBalance(Account account) {
        return account.amount() < 0;
    }
}