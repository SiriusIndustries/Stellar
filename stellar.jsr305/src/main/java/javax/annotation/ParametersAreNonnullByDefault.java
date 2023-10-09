package javax.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.annotation.meta.TypeQualifierDefault;

/**
 * This annotation can be applied to a package, class or method to indicate that the method
 * parameters in that element are nonnull by default unless there is:
 * <ul>
 *     <li>An explicit nullness annotation</li>
 *     <li>The method overrides a method in a superclass (in which case the annotation of the
 *     corresponding parameter in the superclass applies)</li>
 *     <li>There is a default parameter annotation (like {@link ParametersAreNullableByDefault})
 *     applied to a more tightly nested element.</li>
 * </ul>
 *
 * @see Nonnull
 * @since 1u1
 * @author Brian Goetz
 * @author Mechite
 */
@Documented
@Nonnull
@TypeQualifierDefault(ElementType.PARAMETER)
public @interface ParametersAreNonnullByDefault {}