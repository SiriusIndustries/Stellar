package sirius.stellar.facility.executor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Executes all submitted tasks directly in the same thread as the caller.
 *
 * @since 1u1
 * @author Mechite
 */
public final class SynchronousExecutorService extends AbstractExecutorService implements Serializable {

	@Serial
	private static final long serialVersionUID = 4766450303790084245L;

	private volatile boolean terminated;

    @Override
    public void shutdown() {
        terminated = true;
    }

    @Override
    public boolean isShutdown() {
        return terminated;
    }

    @Override
    public boolean isTerminated() {
        return terminated;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) {
        this.shutdown();
        return terminated;
    }

    @Override
    public List<Runnable> shutdownNow() {
        return Collections.emptyList();
    }

    @Override
    public void execute(Runnable runnable) {
        runnable.run();
    }
}