package sirius.stellar.facility.doctation;

import javax.annotation.Nonnull;
import javax.annotation.meta.TypeQualifierNickname;
import javax.annotation.meta.When;
import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * Marks the provided element as potentially being / supporting {@code null}.
 * <p>
 * This should be used in association with {@link Conforms} to override the
 * default nullability semantics - elements are non-null unless annotated with
 * this method. All {@link Override} methods should repeat this annotation,
 * unless they behave differently.
 * <p>
 * JSR-305 meta-annotations are implemented to indicate nullability to common
 * tools that support these annotations, but the way in which support is
 * implemented could change in the future.
 *
 * @since 1u1
 * @author Mechite
 */
@Documented @Inherited
@Target({
		METHOD,
		PARAMETER,
		FIELD,
		RECORD_COMPONENT
})
@Nonnull(when = When.MAYBE)
@TypeQualifierNickname
public @interface Nullable {}