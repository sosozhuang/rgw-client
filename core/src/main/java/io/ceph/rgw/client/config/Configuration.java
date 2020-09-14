package io.ceph.rgw.client.config;

import io.ceph.rgw.client.util.ResourceLoader;
import org.apache.commons.lang3.Validate;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * An immutable configuration implementation.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/3/16.
 */
public final class Configuration {
    private final Map<String, String> configMap;

    public Configuration(String resource) throws IOException {
        Validate.notBlank(resource, "resource cannot be empty string");
        URL url;
        try {
            url = new URL(resource);
        } catch (MalformedURLException e) {
            url = ResourceLoader.getResource(resource);
        }
        if (url == null) {
            throw new FileNotFoundException("Can not find resource: [" + resource + "]");
        }
        Properties properties = getPropertiesFromURL(url);
        this.configMap = new HashMap<>(properties.size());
        properties.stringPropertyNames().forEach(k -> configMap.put(k, properties.getProperty(k)));
    }

    private static Properties getPropertiesFromURL(URL url) {
        Properties properties = new Properties();
        InputStream is = null;
        URLConnection conn;
        try {
            conn = url.openConnection();
            conn.setUseCaches(false);
            is = conn.getInputStream();
            properties.load(is);
        } catch (Exception e) {
            if (e instanceof InterruptedIOException) {
                Thread.currentThread().interrupt();
            }
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (InterruptedIOException ignored) {
                    Thread.currentThread().interrupt();
                } catch (IOException | RuntimeException ignored) {
                }
            }
        }
        return properties;
    }

    public Configuration(File file) throws IOException {
        Validate.notNull(file, "file cannot be null");
        Properties properties = new Properties();
        properties.load(new BufferedInputStream(new FileInputStream(file)));
        this.configMap = new HashMap<>(properties.size());
        properties.stringPropertyNames().forEach(k -> configMap.put(k, properties.getProperty(k)));
    }

    public Configuration(Map<String, String> configMap) {
        if (configMap == null || configMap.size() == 0) {
            this.configMap = Collections.emptyMap();
        } else {
            this.configMap = new HashMap<>(configMap.size());
            this.configMap.putAll(configMap);
        }
    }

    public String getString(String key) {
        return getString(key, null);
    }

    public String getString(String key, String defaultValue) {
        String value = configMap.get(key);
        if (value != null) {
            return value;
        }
        return defaultValue;
    }

    public List<String> getStrings(String key) {
        return getStrings(key, null);
    }

    public List<String> getStrings(String key, List<String> defaultValue) {
        String value = configMap.get(key);
        if (value != null) {
            return Arrays.stream(value.split(","))
                    .map(String::trim).collect(Collectors.toList());
        }
        return defaultValue;
    }

    public Short getShort(String key) {
        return getShort(key, null);
    }

    public Short getShort(String key, Short defaultValue) {
        String value = configMap.get(key);
        if (value != null) {
            return Short.parseShort(value);
        }
        return defaultValue;
    }

    public Integer getInteger(String key) {
        return getInteger(key, null);
    }

    public Integer getInteger(String key, Integer defaultValue) {
        String value = configMap.get(key);
        if (value != null) {
            return Integer.parseInt(value);
        }
        return defaultValue;
    }

    public Long getLong(String key) {
        return getLong(key, null);
    }

    public Long getLong(String key, Long defaultValue) {
        String value = configMap.get(key);
        if (value != null) {
            return Long.parseLong(value);
        }
        return defaultValue;
    }

    public Boolean getBoolean(String key) {
        return getBoolean(key, null);
    }

    public Boolean getBoolean(String key, Boolean defaultValue) {
        String value = configMap.get(key);
        if (value != null) {
            return Boolean.parseBoolean(value);
        }
        return defaultValue;
    }

    public Map<String, String> asMap() {
        return Collections.unmodifiableMap(configMap);
    }

    public Configuration getSubConfig(String prefix) {
        Validate.notBlank(prefix, "prefix cannot be empty string");
        String p = prefix + ".";
        int i = p.length();
        Map<String, String> map = configMap.entrySet().stream()
                .filter(e -> e.getKey().startsWith(p) && e.getKey().length() > i)
                .collect(Collectors.toMap(e -> e.getKey().substring(i), Map.Entry::getValue));
        return new Configuration(map);
    }

    private static final Pattern PATTERN = Pattern.compile("\\[(\\w+)\\]((\\.([a-zA-Z]\\w*(\\[\\w+\\])?))+)");

    public Map<String, Configuration> getSubConfigMap(String prefix) {
        Validate.notBlank(prefix, "prefix cannot be empty string");
        int i = prefix.length();
        Map<String, HashMap<String, String>> map = new HashMap<>();
        Map<String, String> item;
        String index;
        String key;
        Matcher matcher;
        for (Map.Entry<String, String> entry : configMap.entrySet()) {
            if (!entry.getKey().startsWith(prefix)) {
                continue;
            }
            matcher = PATTERN.matcher(entry.getKey().substring(i));
            if (matcher.matches()) {
                index = matcher.group(1);
                key = matcher.group(2).substring(1);
                item = map.computeIfAbsent(index, k -> new HashMap<>());
                item.put(key, entry.getValue());
            }
        }
        return map.size() == 0 ? Collections.emptyMap()
                : map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> new Configuration(e.getValue())));
    }

    public List<Configuration> getSubConfigList(String prefix) {
        return getSubConfigMap(prefix).entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(Integer.parseInt(e1.getKey()), Integer.parseInt(e2.getKey())))
                .map(Map.Entry::getValue).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "configMap=" + configMap +
                '}';
    }
}
