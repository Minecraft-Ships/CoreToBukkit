package org.core.implementation.bukkit.configuration;

import org.array.utils.ArrayUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.core.config.ConfigurationFormat;
import org.core.config.ConfigurationNode;
import org.core.config.ConfigurationStream;
import org.core.config.parser.Parser;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class YAMLConfigurationFile implements ConfigurationStream.ConfigurationFile {

    protected File file;
    protected YamlConfiguration yaml;

    public YAMLConfigurationFile(File file){
        this(file, YamlConfiguration.loadConfiguration(file));
    }

    public YAMLConfigurationFile(File file, YamlConfiguration yaml){
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
        if(!this.yaml.contains(ArrayUtils.toString(".", t -> t, node.getPath()))){
            return Optional.empty();
        }
        if(!(this.yaml.isDouble(ArrayUtils.toString(".", t -> t, node.getPath())) || this.yaml.isInt(ArrayUtils.toString(".", t->t, node.getPath())))){
            return Optional.empty();
        }
        return Optional.of(this.yaml.getDouble(ArrayUtils.toString(".", t -> t, node.getPath())));
    }

    @Override
    public Optional<Integer> getInteger(ConfigurationNode node) {
        if(!this.yaml.contains(ArrayUtils.toString(".", t -> t, node.getPath()))){
            return Optional.empty();
        }
        if(!this.yaml.isInt(ArrayUtils.toString(".", t -> t, node.getPath()))){
            return Optional.empty();
        }
        int value = this.yaml.getInt(ArrayUtils.toString(".", t -> t, node.getPath()));
        return Optional.of(value);
    }

    @Override
    public Optional<Boolean> getBoolean(ConfigurationNode node) {
        if(!this.yaml.contains(ArrayUtils.toString(".", t -> t, node.getPath()))){
            return Optional.empty();
        }
        if(!this.yaml.isBoolean(ArrayUtils.toString(".", t -> t, node.getPath()))){
            return Optional.empty();
        }
        return Optional.of(this.yaml.getBoolean(ArrayUtils.toString(".", t -> t, node.getPath())));
    }

    @Override
    public Optional<String> getString(ConfigurationNode node) {
        if(!this.yaml.contains(ArrayUtils.toString(".", t -> t, node.getPath()))){
            return Optional.empty();
        }
        if(!this.yaml.isString(ArrayUtils.toString(".", t -> t, node.getPath()))){
            return Optional.empty();
        }
        return Optional.ofNullable(this.yaml.getString(ArrayUtils.toString(".", t -> t, node.getPath())));
    }

    @Override
    public <T, C extends Collection<T>> C parseCollection(ConfigurationNode node, Parser<String, T> parser, C collection) {
        List<String> list = this.yaml.getStringList(ArrayUtils.toString(".", t -> t, node.getPath()));
        for(String value : list){
            parser.parse(value).ifPresent(collection::add);
        }
        return collection;
    }

    public void setObject(ConfigurationNode node, Object value){
        if(node.getPath().length == 0){
            throw new IllegalArgumentException("Node must have a path specified");
        }
        this.yaml.set(ArrayUtils.toString(".", t -> t, node.getPath()), value);
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
    public <T> void set(ConfigurationNode node, Parser<String, T> parser, Collection<T> collection) {
        List<String> list = new ArrayList<>(collection.size());
        for(T value : collection){
            try {
                list.add(parser.unparse(value));
            }catch (ClassCastException e){
                System.err.println("Path: " + ArrayUtils.toString(".", t -> t, node.getPath()));
                System.err.println("Value: (" + value.getClass().getName() + ") '" + value.toString() + "'");
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
