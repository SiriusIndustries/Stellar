package sirius.stellar.facility.functional;

import org.jetbrains.annotations.Contract;

import java.util.function.Predicate;

import static sirius.stellar.facility.Strings.*;

/**
 * Represents a predicate (boolean-valued function) of one argument, that may throw a
 * {@link Throwable} which is automatically caught and rethrown, providing a temporary
 * {@link Thread.UncaughtExceptionHandler} for if the rethrown exception is unhandled,
 * preventing any traceability issues.
 * <p>
 * This is a {@linkplain java.util.function functional interface}
 * whose functional method is {@link #testRethrowing(T)}.
 *
 * @see java.util.function.Predicate
 *
 * @since 1u1
 * @author Mechite
 */
@FunctionalInterface
public interface RethrowingPredicate<T> extends Predicate<T> {

    /**
     * Evaluates this predicate on the provided argument.
	 * Implement this, rather than {@link Predicate#test}.
     */
	boolean testRethrowing(T t) throws Throwable;

	@Override
	default boolean test(T t) {
		Thread thread = Thread.currentThread();
		Thread.UncaughtExceptionHandler handler = thread.getUncaughtExceptionHandler();

		try {
			return this.testRethrowing(t);
		} catch (Throwable throwable) {
			thread.setUncaughtExceptionHandler((exceptionThread, exception) -> {
				String name = exceptionThread.getName();
				System.err.println(format("Unhandled exception thrown from a RethrowingPredicate, executed on thread '{0}': {1}", name, exception));
			});
			throw new RuntimeException(throwable);
		} finally {
			thread.setUncaughtExceptionHandler(handler);
		}
	}

	/**
	 * Provides a {@link Predicate} for the provided {@link RethrowingPredicate}.
	 * <p>
	 * This method performs no operation except allow the use of lambda syntax to
	 * construct a {@link RethrowingPredicate} for any method that normally accepts a
	 * {@link Predicate}.
	 */
	@Contract("_ -> param1")
	static <T> Predicate<T> rethrowing(RethrowingPredicate<T> predicate) {
		return predicate;
	}
}