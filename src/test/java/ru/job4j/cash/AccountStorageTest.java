package ru.job4j.cash;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class AccountStorageTest {
    @Test
    void whenAdd() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        var firstAccount = storage.getById(1)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
        assertThat(firstAccount.amount()).isEqualTo(100);
    }

    @Test
    void whenUpdate() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        storage.update(new Account(1, 200));
        var firstAccount = storage.getById(1)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
        assertThat(firstAccount.amount()).isEqualTo(200);
    }

    @Test
    void whenDelete() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        storage.delete(1);
        assertThat(storage.getById(1)).isEmpty();
    }

    @Test
    void whenTransfer() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        storage.add(new Account(2, 100));
        storage.transfer(1, 2, 100);
        var firstAccount = storage.getById(1)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
        var secondAccount = storage.getById(2)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
        assertThat(firstAccount.amount()).isEqualTo(0);
        assertThat(secondAccount.amount()).isEqualTo(200);
    }

    @Test
    void whenTransferAndFromAccountDoesNotExist() {
        var storage = new AccountStorage();
        storage.add(new Account(2, 100));
        var result = storage.transfer(1, 2, 50);
        assertThat(result).isFalse();
        assertThat(storage.getById(2).orElseThrow().amount()).isEqualTo(100);
    }

    @Test
    void whenTransferAndToAccountDoesNotExist() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        var result = storage.transfer(1, 2, 50);
        assertThat(result).isFalse();
        assertThat(storage.getById(1).orElseThrow().amount()).isEqualTo(100);
    }

    @Test
    void whenTransferAndNotEnoughMoney() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 30));
        storage.add(new Account(2, 70));
        var result = storage.transfer(1, 2, 50);
        assertThat(result).isFalse();
        assertThat(storage.getById(1).orElseThrow().amount()).isEqualTo(30);
        assertThat(storage.getById(2).orElseThrow().amount()).isEqualTo(70);
    }
}