package ru.job4j.cash;

import java.util.HashMap;
import java.util.Optional;

public class AccountStorage {
    private final HashMap<Integer, Account> accounts = new HashMap<>();

    public synchronized boolean add(Account account) {
        boolean result = false;
        if (accounts.get(account.id()) == null) {
            accounts.put(account.id(), account);
            result = true;
        }
        return result;
    }

    public synchronized boolean update(Account account) {
        boolean result = false;
        if (accounts.get(account.id()) != null) {
            accounts.put(account.id(), account);
            result = true;
        }
        return result;
    }

    public synchronized void delete(int id) {
        accounts.remove(id);
    }

    public synchronized Optional<Account> getById(int id) {
        return Optional.ofNullable(accounts.get(id));
    }

    public synchronized boolean transfer(int fromId, int toId, int amount) {
        boolean result = false;
        Account fromAccount = accounts.get(fromId);
        Account toAccount = accounts.get(toId);
        if (fromAccount != null && toAccount != null) {
            int fromAmount = fromAccount.amount();
            int toAmount = toAccount.amount();
            if (fromAmount >= amount) {
                update(new Account(fromId, fromAmount - amount));
                update(new Account(toId, toAmount + amount));
                result = true;
            }
        }
        return result;
    }
}
