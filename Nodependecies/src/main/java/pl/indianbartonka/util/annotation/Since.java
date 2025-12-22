package pl.indianbartonka.util.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates the version in which a class, method, or field was first introduced.
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR})
public @interface Since {

    /**
     * The version in which the annotated element was introduced.
     *
     * @return the version string (e.g., "1.0", "2.3.1").
     */
    String value();
}

