package sirius.stellar.logging.dispatch.jul;

import sirius.stellar.facility.doctation.Internal;
import sirius.stellar.logging.dispatch.Dispatcher;

/**
 * Implementation of {@link Dispatcher.Provider} used for obtaining instances of {@link JulDispatcher}.
 * This is run with {@link java.util.ServiceLoader}.
 *
 * @since 1u1
 * @author Mechite
 */
@Internal
public final class JulFactory implements Dispatcher.Provider {

	@Override
	public Dispatcher create() {
		return new JulDispatcher(this);
	}
}