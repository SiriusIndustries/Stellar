package sirius.stellar.facility.doctation;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

/**
 * Marks the provided element as having a low maturity and may change without any
 * notice, or possibly produce unwanted/unexpected behavior.
 * <p>
 * This is similar to {@link Internal} but anything annotated with it is intended
 * to be part of a public API. However, this can be combined with {@code @Internal}
 * (but always ordering {@code @Internal} first to ensure that it is clear) to
 * mark an implementation as immature. This simply serves as a note for future
 * development to take place on that particular element - perhaps it could be a
 * performance-related enhancement that could be done, etc.
 *
 * @since 1u1
 * @author Mechite
 */
@Documented @Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({
		TYPE,
		FIELD,
		METHOD,
		CONSTRUCTOR,
		ANNOTATION_TYPE,
		PACKAGE,
		TYPE_USE,
		MODULE,
		RECORD_COMPONENT
})
public @interface Experimental {}