package com.undeadscythes.udsyaml.exceptions;

/**
 * @author UndeadScythes
 */
@SuppressWarnings("serial")
public class YamlConfigNotLoadedException extends RuntimeException {
    public YamlConfigNotLoadedException() {
        super("The YAML configuration file has not yet been loaded.");
    }
}
