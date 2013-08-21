package com.undeadscythes.udsyaml.exceptions;

/**
 * @author UndeadScythes
 */
@SuppressWarnings("serial")
public class NoSuchYamlPathException extends Exception {
    public NoSuchYamlPathException(String path) {
        super("No such path " + path + " exists.");
    }
}
