package sirius.stellar.logging.dispatch.slf4j;

import org.slf4j.ILoggerFactory;
import org.slf4j.IMarkerFactory;
import org.slf4j.helpers.BasicMDCAdapter;
import org.slf4j.helpers.BasicMarkerFactory;
import org.slf4j.spi.MDCAdapter;

/**
 * Implementation of {@link org.slf4j.spi.SLF4JServiceProvider} used for obtaining instances of {@link Slf4jFactory}.
 *
 * @version SLF4J 2.0.7 (or any other binary compatible variant).
 * @since 1u1
 * @author Mechite
 */
public final class Slf4jServiceProvider implements org.slf4j.spi.SLF4JServiceProvider {

	private ILoggerFactory loggerFactory;
	private IMarkerFactory markerFactory;
	private MDCAdapter mdcAdapter;

	@Override
	public ILoggerFactory getLoggerFactory() {
		return this.loggerFactory;
	}

	@Override
	public IMarkerFactory getMarkerFactory() {
		return this.markerFactory;
	}

	@Override
	public MDCAdapter getMDCAdapter() {
		return this.mdcAdapter;
	}

	@Override
	public String getRequestedApiVersion() {
		return "2.0.7";
	}

	@Override
	public void initialize() {
		this.loggerFactory = new Slf4jFactory();
		this.markerFactory = new BasicMarkerFactory();
		this.mdcAdapter = new BasicMDCAdapter();
	}
}
