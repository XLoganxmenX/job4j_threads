package ru.job4j.cache;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CacheTest {
    @Test
    public void whenAddFind() {
        var base = new Base(1,  "Base", 1);
        var cache = new Cache();
        cache.add(base);
        var find = cache.findById(base.id());
        assertThat(find.get().name())
                .isEqualTo("Base");
    }

    @Test
    public void whenAddUpdateFind() throws OptimisticException {
        var base = new Base(1, "Base", 1);
        var cache = new Cache();
        cache.add(base);
        cache.update(new Base(1, "Base updated", 1));
        var find = cache.findById(base.id());
        assertThat(find.get().name())
                .isEqualTo("Base updated");
    }

    @Test
    public void whenUpdateThenIncrementVersion() throws OptimisticException {
        var base = new Base(1, "Base", 1);
        var cache = new Cache();
        cache.add(base);
        cache.update(new Base(1, "Base updated", 1));
        var find = cache.findById(base.id());
        assertThat(find.get().version())
                .isEqualTo(2);
    }

    @Test
    public void whenUpdateExistThenTrue() throws OptimisticException {
        var base = new Base(1, "Base", 1);
        var cache = new Cache();
        cache.add(base);

        assertThat(cache.update(new Base(1, "Base updated", 1)))
                .isTrue();
    }

    @Test
    public void whenUpdateNotExistThenFalse() throws OptimisticException {
        var cache = new Cache();
        boolean resultUpdate;
        resultUpdate = cache.update(new Base(1, "Base updated", 1));

        assertThat(cache.findById(1)).isEmpty();
        assertThat(resultUpdate).isFalse();
    }

    @Test
    public void whenAddDeleteFind() throws OptimisticException {
        var base = new Base(1,   "Base", 1);
        var cache = new Cache();
        cache.add(base);
        cache.delete(1);
        var find = cache.findById(base.id());
        assertThat(find.isEmpty()).isTrue();
    }

    @Test
    public void whenMultiUpdateThrowException() throws OptimisticException {
        var base = new Base(1,  "Base", 1);
        var cache = new Cache();
        cache.add(base);
        cache.update(base);
        assertThatThrownBy(() -> cache.update(base))
                .isInstanceOf(OptimisticException.class);
    }

    @Test
    public void whenMultiAddUpdateDeleteFind() throws OptimisticException {
        var base = new Base(1,  "Base", 1);
        var base2 = new Base(2,  "Base2", 1);
        var base3 = new Base(3,  "Base3", 1);
        var cache = new Cache();

        cache.add(base);
        cache.add(base2);
        cache.add(base3);
        cache.update(new Base(1, "Base updated", 1));
        cache.update(new Base(2, "Base2 updated", 1));
        cache.update(new Base(3, "Base3 updated", 1));
        cache.delete(3);

        assertThat(cache.findById(base.id()).get().name()).isEqualTo("Base updated");
        assertThat(cache.findById(base2.id()).get().name()).isEqualTo("Base2 updated");
        assertThat(cache.findById(base3.id()).isEmpty()).isTrue();
    }
}