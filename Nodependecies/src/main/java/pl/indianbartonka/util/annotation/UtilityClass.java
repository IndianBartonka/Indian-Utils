package pl.indianbartonka.util.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@code @UtilityClass} marks a class as a utility class, which is a class that contains only static methods
 * or constants and is not meant to be instantiated.
 *
 * <p>This annotation is primarily used to improve code readability and to indicate the design intent
 * behind the class. By applying this annotation, developers signal that the class should be treated
 * as a stateless utility class.</p>
 *
 *
 * <p><b>Note:</b> This annotation does not enforce any compile-time or runtime checks, as it is
 * retained at the {@code CLASS} level. Its primary purpose is for documentation and organizational
 * clarity in the codebase.</p>
 *
 * <p>To follow the convention of utility classes, it is recommended to:
 * <ul>
 *   <li>Declare the class as {@code final} to prevent inheritance.</li>
 *   <li>Provide a private constructor to prevent instantiation.</li>
 *   <li>Ensure all methods are static.</li>
 * </ul>
 * </p>
 * <p>
 * Documents written by ChatGPT
 * </p>
 *
 * @see RetentionPolicy#CLASS
 * @see ElementType#TYPE
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface UtilityClass {
    /**
     * An optional description or identifier for the utility class.
     * This can be used to provide additional context about the purpose
     * of the class if needed.
     *
     * @return a custom description for the utility class, default is an empty string
     */
    String value() default "";
}
