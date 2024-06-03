package ru.job4j.cash;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    void whenAddNewAccountThenTrue() {
        var storage = new AccountStorage();
        assertThat(storage.add(new Account(1, 100))).isTrue();
    }

    @Test
    void whenAddExistAccountThenFalse() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        assertThat(storage.add(new Account(1, 200))).isFalse();
        assertThat(storage.getById(1).get().amount()).isEqualTo(100);
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
    void whenUpdateSuccessThenTrue() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        assertThat(storage.update(new Account(1, 200))).isTrue();
    }

    @Test
    void whenUpdateNotExistsAccountThenFalse() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        assertThat(storage.update(new Account(2, 200))).isFalse();
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
    void whenTransferMoreAmountThanExistInAccountThenFalse() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        storage.add(new Account(2, 100));

        assertThat(storage.transfer(1, 2, 1000)).isFalse();
        assertThat(storage.getById(1).get().amount()).isEqualTo(100);
        assertThat(storage.getById(2).get().amount()).isEqualTo(100);
    }

    @Test
    void whenTransferInNotExistAccountThenThrowException() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        storage.add(new Account(2, 100));

        assertThatThrownBy(() -> storage.transfer(3, 2, 100))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Not found account by id = 3");
    }

    @Test
    void whenTransferNegativeAmountThenThrowException() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        storage.add(new Account(2, 100));

        assertThatThrownBy(() -> storage.transfer(1, 2, -100))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Amount is negative");
    }
}