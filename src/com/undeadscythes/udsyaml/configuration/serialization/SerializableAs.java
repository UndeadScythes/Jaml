package com.undeadscythes.udsyaml.configuration.serialization;

import java.lang.annotation.*;

/**
 * @author Dinnerbone
 * @author grum
 * @author Celtic Minstrel
 * @author feildmaster
 * @author SagaciousZed
 * @author UndeadScythes
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SerializableAs {
    public String value();
}
