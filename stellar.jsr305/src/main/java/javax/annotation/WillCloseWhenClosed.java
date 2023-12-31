package javax.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Used to annotate a constructor/factory parameter to indicate that returned
 * object (X) will close the resource when X is closed.
 *
 * @see WillClose
 * @see WillNotClose
 *
 * @since 1u1
 * @author Brian Goetz
 * @author Mechite
 */
@Documented
public @interface WillCloseWhenClosed {}