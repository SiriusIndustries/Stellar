package javax.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.annotation.meta.When;

import static java.lang.annotation.ElementType.*;

/**
 * This annotation is used to denote a method whose return value should always
 * be checked after invoking the method.
 *
 * @since 1u1
 * @author Brian Goetz
 * @author Mechite
 */
@Documented
@Target({
		METHOD,
		CONSTRUCTOR,
		TYPE,
		PACKAGE
})
public @interface CheckReturnValue {
	When when() default When.ALWAYS;
}