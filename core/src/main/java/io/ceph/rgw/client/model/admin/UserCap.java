package io.ceph.rgw.client.model.admin;

import org.apache.commons.lang3.StringUtils;
import software.amazon.awssdk.core.SdkField;
import software.amazon.awssdk.core.SdkPojo;
import software.amazon.awssdk.core.protocol.MarshallLocation;
import software.amazon.awssdk.core.protocol.MarshallingType;
import software.amazon.awssdk.core.traits.LocationTrait;
import software.amazon.awssdk.utils.builder.Buildable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by zhuangshuo on 2020/8/3.
 */
public class UserCap implements SdkPojo, Buildable {
    private static final SdkField<String> TYPE_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(UserCap::getTypeAsString))
            .setter(setter(UserCap::setType))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("type")
                    .unmarshallLocationName("type").build()).build();
    private static final SdkField<String> PERM_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(UserCap::getPermAsString))
            .setter(setter(UserCap::setPerm))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("perm")
                    .unmarshallLocationName("perm").build()).build();
    private static final List<SdkField<?>> SDK_FIELDS = Collections.unmodifiableList(Arrays.asList(TYPE_FIELD, PERM_FIELD));

    private Type type;
    private Perm perm;

    UserCap() {
    }

    UserCap(Type type, Perm perm) {
        this.type = type;
        this.perm = perm;
    }

    public Type getType() {
        return type;
    }

    private String getTypeAsString() {
        return String.valueOf(type);
    }

    private void setType(String type) {
        this.type = Type.fromString(type);
    }

    public Perm getPerm() {
        return perm;
    }

    private String getPermAsString() {
        return String.valueOf(perm);
    }

    private void setPerm(String perm) {
        this.perm = Perm.fromString(perm);
    }

    @Override
    public String toString() {
        return "UserCap{" +
                "type=" + type +
                ", perm=" + perm +
                '}';
    }

    public static List<UserCap> parse(String userCaps) {
        if (StringUtils.isBlank(userCaps)) {
            return Collections.emptyList();
        }
        String[] items = userCaps.split("\\s*;\\s*");
        List<UserCap> result = new ArrayList<>(items.length);
        String[] pair;
        Type type;
        Perm perm;
        for (String item : items) {
            pair = item.split("\\s*=\\s*", 2);
            type = Type.fromString(pair[0]);
            perm = Perm.fromString(pair[1]);
            if (type != null && perm != null) {
                result.add(new UserCap(type, perm));
            }
        }
        return result.size() > 0 ? result : Collections.emptyList();
    }

    public static String format(List<UserCap> userCaps) {
        if (userCaps == null || userCaps.size() == 0) {
            return "";
        }
        Map<Type, Perm> map = new HashMap<>(3);
        for (UserCap cap : userCaps) {
            map.compute(cap.getType(), (k, v) -> cap.getPerm().and(v));
        }
        return map.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining("; "));
    }

    private static <T> Function<Object, T> getter(Function<UserCap, T> g) {
        return obj -> g.apply((UserCap) obj);
    }

    private static <T> BiConsumer<Object, T> setter(BiConsumer<UserCap, T> s) {
        return (obj, val) -> s.accept((UserCap) obj, val);
    }

    @Override
    public List<SdkField<?>> sdkFields() {
        return SDK_FIELDS;
    }

    @Override
    public Object build() {
        return this;
    }

    public enum Type {
        USERS,
        BUCKETS,
        METADATA,
        USAGE,
        ZONE;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }

        public static Type fromString(String type) {
            if (StringUtils.isBlank(type)) {
                return null;
            }
            for (Type value : values()) {
                if (value.toString().equals(type.trim().toLowerCase())) {
                    return value;
                }
            }
            return null;
        }
    }

    public enum Perm {
        READ("read"),
        WRITE("write"),
        READ_WRITE("*");
        private final String perm;

        Perm(String perm) {
            this.perm = perm;
        }

        public Perm and(Perm other) {
            if (other == null) {
                return this;
            }
            return values()[((this.ordinal() + 1) | (other.ordinal() + 1)) - 1];
        }

        @Override
        public String toString() {
            return perm;
        }

        public static Perm fromString(String perm) {
            if (StringUtils.isBlank(perm)) {
                return null;
            }
            for (Perm value : values()) {
                if (value.perm.equals(perm.trim().toLowerCase())) {
                    return value;
                }
            }
            String[] items = perm.split("\\s*,\\s*");
            if (items.length > 1) {
                return Stream.of(items).map(Perm::fromString).filter(Objects::nonNull).reduce(Perm::and).orElse(null);
            }
            return null;
        }

    }
}
