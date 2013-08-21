package com.undeadscythes.udsyaml.configuration.serialization;

import java.lang.annotation.*;

/**
 * @author Dinnerbone
 * @author grum
 * @author Celtic Minstrel
 * @author UndeadScythes
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DelegateDeserialization {
    public Class<? extends ConfigurationSerializable> value();
}
