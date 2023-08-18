package sirius.stellar.logging.dispatch.log4j2;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link org.apache.logging.log4j.spi.LoggerContext} used for obtaining instances of {@link Log4j2Dispatcher}.
 *
 * @since 1u1
 * @author Mechite
 */
public final class Log4j2Context implements org.apache.logging.log4j.spi.LoggerContext {

	private final List<org.apache.logging.log4j.spi.ExtendedLogger> loggers;
	private final Object externalContext;

	Log4j2Context(Object externalContext) {
		this.loggers = new ArrayList<>();
		this.externalContext = externalContext;
	}

	@Override
	public Object getExternalContext() {
		return this.externalContext;
	}

	@Override
	public org.apache.logging.log4j.spi.ExtendedLogger getLogger(String name) {
		return new Log4j2Dispatcher(name);
	}

	@Override
	public org.apache.logging.log4j.spi.ExtendedLogger getLogger(String name, org.apache.logging.log4j.message.MessageFactory factory) {
		return new Log4j2Dispatcher(name, factory);
	}

	@Override
	public boolean hasLogger(String name) {
		return false;
	}

	@Override
	public boolean hasLogger(String name, Class<? extends org.apache.logging.log4j.message.MessageFactory> messageFactoryClass) {
		return false;
	}

	@Override
	public boolean hasLogger(String name, org.apache.logging.log4j.message.MessageFactory messageFactory) {
		return false;
	}
}