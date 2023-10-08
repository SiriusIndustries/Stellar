package sirius.stellar.facility.doctation;

import org.jetbrains.annotations.Contract;

import javax.annotation.Nonnull;
import javax.annotation.meta.TypeQualifierDefault;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

/**
 * Marks the provided elements as conforming to the semantics as outlined below
 * (should be annotated on a package or class declaration, applies to all elements
 * inside the marked package, or all elements inside the marked class declaration):
 * <ol>
 *     <li>
 *         All methods can only consume and return non-null values unless directly
 *         annotated with {@link Nullable}. However, the outcome of what happens
 *         when a method not annotated consumes a null value could vary. This is
 *         why rule #3 should also be conformed to.
 *
 *         <pre>{@code
 *             void example(String string) {}
 *
 *             @Contract("_ -> new")
 *             String exampleTwo(String string) {
 *                 return String.valueOf(string);
 *             }
 *         }</pre>
 *
 *         In {@code example(String)}, the provided string should never be null,
 *         and while the behaviour depends on what effect the method has, it will
 *         never be desirable behaviour. However, in {@code exampleTwo(String)},
 *         as the method returns an {@code String} as well, it has been documented
 *         with {@link Contract} to show that no matter what value is passed in to
 *         this method, even with {@code null}, it will always return a new object.
 *     </li>
 *     <li>
 *         All fields are non-null unless directly annotated with {@link Nullable}.
 *         However, all instance fields must be private, and any public, accessible
 *         static fields must be adequately documented and eventually either deleted,
 *         made private and have static accessors created for it, or made constant
 *         and marked {@code final}.
 *     </li>
 *     <li>
 *         The behaviour of all "function" methods (method accepting a value and
 *         returning a value) should always be documented with {@link Contract},
 *         unless it is an {@link Override} method.
 *     </li>
 * </ol>
 *
 * @since 1u1
 * @author Mechite
 */
@Documented @Inherited
@Target({
		TYPE,
		PACKAGE
})
@Nonnull
@TypeQualifierDefault({
		METHOD,
		PARAMETER
})
public @interface Conforms {}