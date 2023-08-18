package sirius.stellar.logging.dispatch.jul;

import sirius.stellar.logging.Logger;
import sirius.stellar.logging.LoggerLevel;
import sirius.stellar.logging.dispatch.Dispatcher;

import java.io.ObjectStreamException;
import java.io.Serial;
import java.util.Map;
import java.util.ServiceLoader;

import static sirius.stellar.facility.Strings.*;

/**
 * Implementation of {@link java.util.logging.Handler} which dispatches to {@link Logger}.
 *
 * <table>
 *     <caption>Level Mapping</caption>
 *     <tr> <th>FINEST</th><th>STACKTRACE</th> </tr>
 *     <tr> <th>FINER</th><th>DEBUGGING</th> </tr>
 *     <tr> <th>FINE</th><th>DEBUGGING</th> </tr>
 *     <tr> <th>CONFIG</th><th>CONFIGURATION</th> </tr>
 *     <tr> <th>INFO</th><th>INFORMATION</th> </tr>
 *     <tr> <th>WARNING</th><th>WARNING</th> </tr>
 *     <tr> <th>SEVERE</th><th>ERROR</th> </tr>
 * </table>
 *
 * @since 1u1
 * @author Mechite
 */
public final class JulDispatcher extends java.util.logging.Handler implements Dispatcher {

	@Serial
	private static final long serialVersionUID = 2451156701961930648L;

	private static final java.util.logging.LogManager manager = java.util.logging.LogManager.getLogManager();
	private static final Map<java.util.logging.Level, LoggerLevel> conversions = Map.of(
			java.util.logging.Level.FINEST, LoggerLevel.STACKTRACE,
			java.util.logging.Level.FINER, LoggerLevel.DEBUGGING,
			java.util.logging.Level.FINE, LoggerLevel.DEBUGGING,
			java.util.logging.Level.CONFIG, LoggerLevel.CONFIGURATION,
			java.util.logging.Level.INFO, LoggerLevel.INFORMATION,
			java.util.logging.Level.WARNING, LoggerLevel.WARNING,
			java.util.logging.Level.SEVERE, LoggerLevel.ERROR
	);

	private transient final Dispatcher.Provider provider;

	JulDispatcher(Dispatcher.Provider provider) {
		this.provider = provider;
	}

	@Override
	public void wire() {
		manager.getLogger("").addHandler(this);
	}

	@Override
	public void publish(java.util.logging.LogRecord record) {
		java.util.logging.Level original = record.getLevel();
		if (original == null) return;
		LoggerLevel level = conversions.get(original);
		if (level == null) return;
		Logger.dispatch(record.getInstant(), level, Thread.currentThread().getName(), record.getSourceClassName(), format(record.getMessage(), record.getParameters()));
	}

	@Override
	public void flush() {
		assert true;
	}

	@Override
	public void close() {
		assert true;
	}

	@Serial
	private Object readResolve() throws ObjectStreamException {
		return this.provider.create();
	}
}