package com.xavelo.filocitas.logging;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TimedOperation {

    /**
     * Optional name for the operation that will be logged. When left blank the fully-qualified
     * method signature will be used.
     */
    String value() default "";
}
