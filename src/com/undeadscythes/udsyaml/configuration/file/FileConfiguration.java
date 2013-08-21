package com.undeadscythes.udsyaml.configuration.file;

import com.undeadscythes.udsyaml.configuration.*;
import com.undeadscythes.udsyaml.exceptions.*;
import java.io.*;

/**
 * @author Dinnerbone
 * @author grum
 * @author Wolvereness
 * @author feildmaster
 * @author SagaciousZed
 * @author UndeadScythes
 */
public abstract class FileConfiguration extends MemoryConfiguration {
    public FileConfiguration() {
        super();
    }

    public FileConfiguration(Configuration defaults) {
        super(defaults);
    }

    public void save(File file) throws IOException {
        if(file == null) throw new NullPointerException("File cannot be null.");
        file.getParentFile().mkdirs();
        String data = saveToString();
        FileWriter writer = new FileWriter(file);
        try {
            writer.write(data);
        } finally {
            writer.close();
        }
    }

    public void save(String file) throws IOException {
        if(file == null) throw new NullPointerException("File cannot be null.");
        save(new File(file));
    }

    public abstract String saveToString();

    public void load(File file) throws FileNotFoundException, IOException, InvalidConfigurationException {
        if(file == null) throw new NullPointerException("File cannot be null.");
        load(new FileInputStream(file));
    }

    public void load(InputStream stream) throws IOException, InvalidConfigurationException {
        if(stream == null) throw new NullPointerException("Stream cannot be null.");
        InputStreamReader reader = new InputStreamReader(stream);
        StringBuilder builder = new StringBuilder(0);
        BufferedReader input = new BufferedReader(reader);
        try {
            for(String line = input.readLine(); line != null; line = input.readLine()) {
                builder.append(line);
                builder.append('\n');
            }
        } finally {
            input.close();
        }
        loadFromString(builder.toString());
    }

    public void load(String file) throws FileNotFoundException, IOException, InvalidConfigurationException {
        if(file == null) throw new NullPointerException("File cannot be null.");
        load(new File(file));
    }

    public abstract void loadFromString(String contents) throws InvalidConfigurationException;

    protected abstract String buildHeader();

    @Override
    public FileConfigurationOptions options() {
        if (options == null) {
            options = new FileConfigurationOptions(this);
        }
        return (FileConfigurationOptions) options;
    }
}
