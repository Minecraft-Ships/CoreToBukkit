package org.core.implementation.bukkit.configuration;

import org.bukkit.configuration.file.YamlConfiguration;
import org.core.config.ConfigurationFormat;
import org.core.config.ConfigurationNode;
import org.core.config.ConfigurationStream;
import org.core.config.parser.Parser;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class YAMLConfigurationFile implements ConfigurationStream.ConfigurationFile {

    protected final File file;
    protected YamlConfiguration yaml;

    public YAMLConfigurationFile(File file) {
        this(file, YamlConfiguration.loadConfiguration(file));
    }

    public YAMLConfigurationFile(File file, YamlConfiguration yaml) {
        this.file = file;
        this.yaml = yaml;
    }

    public YamlConfiguration getYaml() {
        return this.yaml;
    }

    @Override
    public File getFile() {
        return this.file;
    }

    @Override
    public ConfigurationFormat getFormat() {
        return ConfigurationFormat.FORMAT_YAML;
    }

    @Override
    public Optional<Double> getDouble(ConfigurationNode node) {
        if (!this.yaml.contains(String.join(".", node.getPath()))) {
            return Optional.empty();
        }
        if (!(this.yaml.isDouble(String.join(".", node.getPath())) || this.yaml.isInt(
                String.join(".", node.getPath())))) {
            return Optional.empty();
        }
        return Optional.of(this.yaml.getDouble(String.join(".", node.getPath())));
    }

    @Override
    public Optional<Integer> getInteger(ConfigurationNode node) {
        if (!this.yaml.contains(String.join(".", node.getPath()))) {
            return Optional.empty();
        }
        if (!this.yaml.isInt(String.join(".", node.getPath()))) {
            return Optional.empty();
        }
        int value = this.yaml.getInt(String.join(".", node.getPath()));
        return Optional.of(value);
    }

    @Override
    public Optional<Boolean> getBoolean(ConfigurationNode node) {
        if (!this.yaml.contains(String.join(".", node.getPath()))) {
            return Optional.empty();
        }
        if (!this.yaml.isBoolean(String.join(".", node.getPath()))) {
            return Optional.empty();
        }
        return Optional.of(this.yaml.getBoolean(String.join(".", node.getPath())));
    }

    @Override
    public Optional<String> getString(ConfigurationNode node) {
        if (!this.yaml.contains(String.join(".", node.getPath()))) {
            return Optional.empty();
        }
        if (!this.yaml.isString(String.join(".", node.getPath()))) {
            return Optional.empty();
        }
        return Optional.ofNullable(this.yaml.getString(String.join(".", node.getPath())));
    }

    @Override
    public <T, C extends Collection<T>> C parseCollection(ConfigurationNode node,
                                                          Parser<? super String, T> parser,
                                                          C collection,
                                                          C defaultValue) {
        String path = String.join(".", node.getPath());
        if (!this.yaml.isList(path)) {
            return defaultValue;
        }

        List<String> list = this.yaml.getStringList(path);
        for (String value : list) {
            parser.parse(value).ifPresent(collection::add);
        }
        return collection;
    }

    public void setObject(ConfigurationNode node, Object value) {
        if (node.getPath().length == 0) {
            throw new IllegalArgumentException("Node must have a path specified");
        }
        this.yaml.set(String.join(".", node.getPath()), value);
    }

    @Override
    public void set(ConfigurationNode node, int value) {
        this.setObject(node, value);
    }

    @Override
    public void set(ConfigurationNode node, double value) {
        this.setObject(node, value);

    }

    @Override
    public void set(ConfigurationNode node, boolean value) {
        this.setObject(node, value);
    }

    @Override
    public void set(ConfigurationNode node, String value) {
        this.setObject(node, value);
    }

    @Override
    public <T> void set(ConfigurationNode node, Parser<String, ? super T> parser, Collection<T> collection) {
        Collection<String> list = new ArrayList<>(collection.size());
        for (T value : collection) {
            try {
                list.add(parser.unparse(value));
            } catch (ClassCastException e) {
                System.err.println("Path: " + String.join(".", node.getPath()));
                System.err.println("Value: (" + value.getClass().getName() + ") '" + value + "'");
                e.printStackTrace();
            }
        }
        this.setObject(node, list);
    }

    @Override
    public Set<ConfigurationNode> getChildren(ConfigurationNode node) {
        Set<ConfigurationNode> value = new HashSet<>();
        this.yaml.getKeys(true).forEach(p -> value.add(new ConfigurationNode(p.split("\\."))));
        return value;
    }

    @Override
    public void reload() {
        this.yaml = YamlConfiguration.loadConfiguration(this.file);
    }

    @Override
    public void save() {
        try {
            this.yaml.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
