package sirius.stellar.logging.dispatch.jsr379;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of {@link System.LoggerFinder} used for obtaining instances of {@link Jsr379Dispatcher}.
 *
 * @since 1u1
 * @author Mechite
 */
public final class Jsr379Finder extends System.LoggerFinder {
	
	private static final Map<String, Jsr379Dispatcher> loggers = new HashMap<>();
	
	@Override
	public System.Logger getLogger(String name, Module module) {
		return loggers.computeIfAbsent(name, Jsr379Dispatcher::new);
	}
}