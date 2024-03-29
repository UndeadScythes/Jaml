package com.undeadscythes.udsyaml.configuration.file;

import com.undeadscythes.udsyaml.configuration.*;
import com.undeadscythes.udsyaml.exceptions.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import org.yaml.snakeyaml.*;
import org.yaml.snakeyaml.error.*;
import org.yaml.snakeyaml.representer.*;

/**
 * @author Dinnerbone
 * @author grum
 * @author Wolvereness
 * @author feildmaster
 * @author SagaciousZed
 * @author UndeadScythes
 */
public class YamlConfiguration extends FileConfiguration {
    protected static final String COMMENT_PREFIX = "# ";
    protected static final String BLANK_CONFIG = "{}\n";
    private final DumperOptions yamlOptions = new DumperOptions();
    private final Representer yamlRepresenter = new YamlRepresenter();
    private final Yaml yaml = new Yaml(new YamlConstructor(), yamlRepresenter, yamlOptions);

    @Override
    public String saveToString() {
        yamlOptions.setIndent(options().indent());
        yamlOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        yamlRepresenter.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        String header = buildHeader();
        String dump = yaml.dump(getValues(false));
        if (dump.equals(BLANK_CONFIG)) {
            dump = "";
        }
        return header + dump;
    }

    @Override
    public void loadFromString(String contents) throws InvalidConfigurationException {
        if(contents == null) throw new NullPointerException("Contents cannot be null.");
        Map<?, ?> input;
        try {
            input = (Map<?, ?>) yaml.load(contents);
        } catch (YAMLException e) {
            throw new InvalidConfigurationException(e);
        } catch (ClassCastException e) {
            throw new InvalidConfigurationException("Top level is not a Map.");
        }
        String header = parseHeader(contents);
        if (header.length() > 0) {
            options().header(header);
        }
        if (input != null) {
            convertMapsToSections(input, this);
        }
    }

    protected void convertMapsToSections(Map<?, ?> input, ConfigurationSection section) {
        for (Map.Entry<?, ?> entry : input.entrySet()) {
            String key = entry.getKey().toString();
            Object value = entry.getValue();
            if (value instanceof Map) {
                convertMapsToSections((Map<?, ?>) value, section.createSection(key));
            } else {
                section.set(key, value);
            }
        }
    }

    protected String parseHeader(String input) {
        String[] lines = input.split("\r?\n", -1);
        StringBuilder result = new StringBuilder(0);
        boolean readingHeader = true;
        boolean foundHeader = false;
        for (int i = 0; (i < lines.length) && (readingHeader); i++) {
            String line = lines[i];
            if (line.startsWith(COMMENT_PREFIX)) {
                if (i > 0) {
                    result.append("\n");
                }
                if (line.length() > COMMENT_PREFIX.length()) {
                    result.append(line.substring(COMMENT_PREFIX.length()));
                }
                foundHeader = true;
            } else if ((foundHeader) && (line.length() == 0)) {
                result.append("\n");
            } else if (foundHeader) {
                readingHeader = false;
            }
        }
        return result.toString();
    }

    @Override
    protected String buildHeader() {
        String header = options().header();
        if (options().copyHeader()) {
            Configuration def = getDefaults();
            if ((def != null) && (def instanceof FileConfiguration)) {
                FileConfiguration filedefaults = (FileConfiguration) def;
                String defaultsHeader = filedefaults.buildHeader();
                if ((defaultsHeader != null) && (defaultsHeader.length() > 0)) {
                    return defaultsHeader;
                }
            }
        }
        if (header == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder(0);
        String[] lines = header.split("\r?\n", -1);
        boolean startedHeader = false;
        for (int i = lines.length - 1; i >= 0; i--) {
            builder.insert(0, "\n");
            if ((startedHeader) || (lines[i].length() != 0)) {
                builder.insert(0, lines[i]);
                builder.insert(0, COMMENT_PREFIX);
                startedHeader = true;
            }
        }
        return builder.toString();
    }

    @Override
    public YamlConfigurationOptions options() {
        if (options == null) {
            options = new YamlConfigurationOptions(this);
        }
        return (YamlConfigurationOptions) options;
    }

    public static YamlConfiguration loadConfiguration(File file) {
        if(file == null) throw new NullPointerException("File cannot be null.");
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.load(file);
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
            Logger.getLogger(YamlConfiguration.class.getName()).log(Level.SEVERE, "Cannot load " + file, ex);
        } catch (InvalidConfigurationException ex) {
            Logger.getLogger(YamlConfiguration.class.getName()).log(Level.SEVERE, "Cannot load " + file , ex);
        }
        return config;
    }

    public static YamlConfiguration loadConfiguration(InputStream stream) {
        if(stream == null) throw new NullPointerException("Stream cannot be null.");
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.load(stream);
        } catch (IOException ex) {
            Logger.getLogger(YamlConfiguration.class.getName()).log(Level.SEVERE, "Cannot load configuration from stream", ex);
        } catch (InvalidConfigurationException ex) {
            Logger.getLogger(YamlConfiguration.class.getName()).log(Level.SEVERE, "Cannot load configuration from stream", ex);
        }
        return config;
    }
}
