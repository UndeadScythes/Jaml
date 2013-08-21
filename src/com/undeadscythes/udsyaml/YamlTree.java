package com.undeadscythes.udsyaml;

import com.undeadscythes.udsyaml.configuration.file.*;
import com.undeadscythes.udsyaml.exceptions.*;
import java.io.*;
import java.util.*;

/**
 * @author UndeadScythes
 */
public class YamlTree {
    private YamlConfiguration config = null;
    private File file;
    private boolean loaded = false;

    public YamlTree(String path) {
        file = new File(path);
    }

    public void load() throws IOException {
        if(!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        config = YamlConfiguration.loadConfiguration(file);
        loaded = true;
    }

    public void save() throws IOException {
        if(!loaded) throw new YamlConfigNotLoadedException();
        config.save(file);
    }

    public Map<String, Object> getValues(String path) throws NoSuchYamlPathException {
        if(!loaded) throw new YamlConfigNotLoadedException();
        if(!config.isConfigurationSection(path)) throw new NoSuchYamlPathException(path);
        return config.getConfigurationSection(path).getValues(false);
    }

    public void remove(String path) {
        config.set(path, null);
    }

    public void reset() throws IOException {
        if(file.exists()) {
            file.delete();
        }
        load();
    }

    public void set(String path, Object obj) {
        config.set(path, obj);
    }

    public List<?> getList(String path) throws NoSuchYamlPathException {
        if(!loaded) throw new YamlConfigNotLoadedException();
        if(config.get(path) == null) throw new NoSuchYamlPathException(path);
        return config.getList(path);
    }
}
