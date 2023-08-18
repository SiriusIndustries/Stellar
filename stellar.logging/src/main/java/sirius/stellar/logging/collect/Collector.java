package sirius.stellar.logging.collect;

import org.jetbrains.annotations.Contract;
import sirius.stellar.logging.LoggerMessage;

import java.io.PrintStream;
import java.io.Serializable;
import java.nio.file.Path;

/**
 * Represents an operation that takes place when a logger message is emitted.
 * <p>
 * Static methods are also available under this interface for obtaining default
 * implementations of the interface, such as for console logging.
 * <p>
 * When implementing this interface, the {@link LoggerMessage} object emitted when
 * collecting a message can have heavy assertions made against it and any heavy
 * operations needed to, e.g., publish messages through a distributed log, can be
 * done safely, as it is expected that collectors are always invoked asynchronously.
 * <p>
 * This means that performance is predictable, log ordering is never affected, and
 * the impact logging has on your application is negligible. There is a possibility
 * of
 *
 * @since 1u1
 * @author Mechite
 */
@FunctionalInterface
public interface Collector extends AutoCloseable, Serializable {

	/**
	 * Runs when a logger message is emitted.
	 */
	void collect(LoggerMessage message);

	/**
	 * Runs when this collector is closed.
	 * <p>
	 * This method is implemented by default to allow for simple collectors to
	 * be defined by implementing only one abstract method, i.e., with a lambda.
	 */
	@Override
	default void close() {
		assert true;
	}

	/**
	 * Returns an instance that prints to console.
	 * <p>
	 * This method can only be called once across the application lifecycle as it
	 * runs {@link System#setOut(PrintStream)} and {@link System#setErr(PrintStream)}.
	 * <p>
	 * This essentially means that this not only provides a collector that collects
	 * logs to {@link System#out}, but also it sets a dispatcher so that any later
	 * calls to try and output to {@code stdout} will be redirected to logging.
	 *
	 * @since 1u1
	 */
	static Collector console() {
		return ConsoleCollector.get();
	}

	/**
	 * Returns an instance that prints to log files.
	 * This method does not accept a path argument and defaults to {@code logging/}.
	 *
	 * @see Collector#file(Path)
	 * @since 1u1
	 */
	static Collector file() {
		return file(Path.of("logging"));
	}

	/**
	 * Returns an instance that prints to log files.
	 * The provided path will be the root of where the files are output.
	 *
	 * @see Collector#file()
	 * @since 1u1
	 */
	@Contract("null -> fail; !null -> new")
	static Collector file(Path path) {
		return new FileCollector(path);
	}
}