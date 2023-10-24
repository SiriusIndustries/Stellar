package sirius.stellar.logging.collect.slf4j;

import sirius.stellar.logging.Logger;
import sirius.stellar.logging.LoggerMessage;
import sirius.stellar.logging.collect.Collector;

import java.io.Serial;
import java.util.HashMap;

/**
 * Implementation of {@link Collector} that delegates to SLF4J.
 * <p>
 * This is not used by default and is available for users to make use of
 * {@code stellar.logging.*} as an API, passing logging messages to their
 * desired underlying implementation in order to retrofit applications
 * making use of more complicated logging setups.
 * <p>
 * Notable factors when using this collector include the fact that the
 * level configured for {@link Logger} is still significant, the fact that
 * all messages will be reported as arriving from the thread that the collector
 * is invoked from (which is managed by the executor in {@link Logger}), the
 * slight delay that may be experienced in timestamps as the underlying SLF4J
 * implementation will compute the timestamp, and the level mapping.
 * <p>
 * Do not use this implementation if you do not have a different implementation
 * for SLF4J available on the classpath or module path, as it will lead to an
 * endless loop of logging being dispatched to this collector, from this
 * collector to the SLF4J dispatcher, and then back to this collector.
 *
 * <table>
 *     <caption>Level Mapping</caption>
 *     <tr> <th>ALL</th><th>INFO</th> </tr>
 *     <tr> <th>INFORMATION</th><th>INFO</th> </tr>
 *     <tr> <th>WARNING</th><th>WARN</th> </tr>
 *     <tr> <th>ERROR</th><th>ERROR</th> </tr>
 *     <tr> <th>STACKTRACE</th><th>TRACE</th> </tr>
 *     <tr> <th>DEBUGGING</th><th>DEBUG</th> </tr>
 *     <tr> <th>CONFIGURATION</th><th>DEBUG</th> </tr>
 *     <tr> <th>OFF</th><th>DEBUG</th> </tr>
 * </table>
 */
public record Slf4jCollector(HashMap<String, org.slf4j.Logger> loggers) implements Collector {

	@Serial
	private static final long serialVersionUID = 4175820898924806606L;

	public Slf4jCollector() {
		this(new HashMap<>());
	}

	@Override
	public void collect(LoggerMessage message) {
		this.loggers.computeIfAbsent(message.name(), org.slf4j.LoggerFactory::getLogger)
				.atLevel(switch (message.level()) {
					case ALL, INFORMATION -> org.slf4j.event.Level.INFO;
					case WARNING -> org.slf4j.event.Level.WARN;
					case ERROR -> org.slf4j.event.Level.ERROR;
					case STACKTRACE -> org.slf4j.event.Level.TRACE;
					case DEBUGGING, CONFIGURATION, OFF -> org.slf4j.event.Level.DEBUG;
				})
				.setMessage(message.text())
				.log();
	}
}