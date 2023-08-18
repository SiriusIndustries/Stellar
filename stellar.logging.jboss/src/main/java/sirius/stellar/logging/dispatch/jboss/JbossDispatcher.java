package sirius.stellar.logging.dispatch.jboss;

import sirius.stellar.logging.Logger;
import sirius.stellar.logging.LoggerLevel;

import java.time.Instant;

import static sirius.stellar.facility.Throwables.*;

/**
 * Implementation of {@link org.jboss.logging.Logger} which delegates to {@link Logger}.
 *
 * @since 1u1
 * @author Mechite
 */
public final class JbossDispatcher extends org.jboss.logging.Logger {

	JbossDispatcher(String name) {
		super(name);
	}

	@Override
	protected void doLog(org.jboss.logging.Logger.Level level, String name, Object text, Object[] arguments, Throwable throwable) {
		if (throwable != null) text += "\n" + stacktrace(throwable);
		Logger.dispatch(Instant.now(), convert(level), Thread.currentThread().getName(), this.getName(), String.valueOf(text), arguments);
	}

	@Override
	protected void doLogf(org.jboss.logging.Logger.Level level, String name, String text, Object[] arguments, Throwable throwable) {
		this.doLog(level, name, text, arguments, throwable);
	}

	@Override
	public boolean isEnabled(org.jboss.logging.Logger.Level level) {
		return Logger.enabled(convert(level));
	}

	/**
	 * Converts the provided level to a {@link LoggerLevel}.
	 */
	private LoggerLevel convert(org.jboss.logging.Logger.Level level) {
		return switch (level) {
			case FATAL, ERROR -> LoggerLevel.ERROR;
			case WARN -> LoggerLevel.WARNING;
        	case INFO -> LoggerLevel.INFORMATION;
        	case DEBUG -> LoggerLevel.DEBUGGING;
			case TRACE -> LoggerLevel.STACKTRACE;
		};
	}
}