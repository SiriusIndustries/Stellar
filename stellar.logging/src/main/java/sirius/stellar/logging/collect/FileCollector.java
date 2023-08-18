package sirius.stellar.logging.collect;

import sirius.stellar.facility.doctation.Internal;
import sirius.stellar.logging.LoggerMessage;

import java.io.Serial;
import java.nio.file.Path;

/**
 * Implementation of {@link Collector} that prints to log files.
 */
@Internal
final class FileCollector implements Collector {

	@Serial
	private static final long serialVersionUID = 4479392734705305030L;

	FileCollector(Path path) {

	}

	@Override
	public void collect(LoggerMessage message) {

	}
}