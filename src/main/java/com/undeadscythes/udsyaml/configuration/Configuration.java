package com.undeadscythes.udsyaml.configuration;

import java.util.*;

/**
 * @author Dinnerbone
 * @author grum
 * @author feildmaster
 * @author SagaciousZed
 * @author UndeadScythes
 */
public interface Configuration extends ConfigurationSection {
    @Override
    public void addDefault(String path, Object value);
    public void addDefaults(Map<String, Object> defaults);
    public void addDefaults(Configuration defaults);
    public void setDefaults(Configuration defaults);
    public Configuration getDefaults();
    public ConfigurationOptions options();
}
