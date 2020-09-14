package io.ceph.rgw.client.model;

/**
 * Created by zhuangshuo on 2020/4/28.
 */
public class Filter {
    private final KeyFilter keyFilter;

    public Filter(KeyFilter keyFilter) {
        this.keyFilter = keyFilter;
    }

    public KeyFilter getKeyFilter() {
        return keyFilter;
    }

    @Override
    public String toString() {
        return "Filter{" +
                "keyFilter=" + keyFilter +
                '}';
    }

    static class NestedBuilder<T extends NestedBuilder<T>> extends NestedGenericBuilder<T, Filter> {
        private KeyFilter keyFilter;

        public KeyFilterBuilder<T> withKeyFilter() {
            return new KeyFilterBuilder<>(this);
        }

        public T withKeyFilter(KeyFilter keyFilter) {
            this.keyFilter = keyFilter;
            return self();
        }

        @Override
        protected Filter build() {
            return new Filter(keyFilter);
        }
    }

    public static class KeyFilterBuilder<T extends NestedBuilder<T>> extends KeyFilter.NestedBuilder<KeyFilterBuilder<T>> {
        final NestedBuilder<T> builder;

        KeyFilterBuilder(NestedBuilder<T> builder) {
            this.builder = builder;
        }

        public T endKeyFilter() {
            return builder.withKeyFilter(build());
        }
    }

    public static class Builder extends NestedBuilder<Builder> {
        @Override
        public Filter build() {
            return super.build();
        }
    }
}
