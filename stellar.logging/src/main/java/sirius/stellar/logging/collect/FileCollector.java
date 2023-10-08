package sirius.stellar.logging.collect;

import sirius.stellar.facility.doctation.Internal;
import sirius.stellar.logging.LoggerMessage;

import java.io.IOException;
import java.io.Serial;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * Implementation of {@link Collector} that prints to log files.
 */
@Internal
final class FileCollector implements Collector {

	@Serial
	private static final long serialVersionUID = 4479392734705305030L;

	private final Path path;
	private final Duration duration;

	private final AtomicBoolean closing;
	private final AtomicBoolean writing;

	private FileChannel channel;
	private Instant rolled;

	FileCollector(Path path, Duration duration) {
		this.path = path;
		this.duration = duration;

		this.closing = new AtomicBoolean();
		this.writing = new AtomicBoolean();

		this.roll();
	}

	@Override
	public void collect(LoggerMessage message) {
		try {
			if (this.closing.get()) return;
			this.writing.set(true);

			if (this.rolled.plus(this.duration).isBefore(Instant.now())) this.roll();

			byte[] text = ("\"" +
					message.time() + "\",\"" +
					message.level() + "\",\"" +
					message.thread() + "\",\"" +
					message.name() + "\",\"" +
					Arrays.stream(message.text()
							.replaceAll("\"", "`")
							.replaceAll("'", "`")
							.split("\n"))
							.map(string -> "'" + string + "'")
							.collect(Collectors.joining()) +
			"\"\n").getBytes(StandardCharsets.UTF_8);

			int written = this.channel.write(ByteBuffer.wrap(text));
			if (written != text.length) throw new IllegalStateException("Failed to append to file (written length does not match expected length)");

			this.writing.set(false);
		} catch (IOException exception) {
			throw new IllegalStateException("Failed to append to file", exception);
		}
	}

	@Override
	public void close() throws IOException {
		this.closing.set(true);
		while (this.writing.get()) Thread.onSpinWait();
		this.channel.close();
	}

	/**
	 * Rolls the internal file channel for this collector to allow for a new
	 * file to be used and the old one to serve as an archive for the previous
	 * duration of logging.
	 */
	private void roll() {
		try {
			if (this.channel != null && this.channel.isOpen()) this.channel.close();
			this.rolled = Instant.now();

			Path file = this.path.resolve(this.rolled.toEpochMilli() + "-" + UUID.randomUUID() + ".csv");
			Files.createDirectories(this.path);
			Files.createFile(file);
			this.channel = FileChannel.open(file, StandardOpenOption.APPEND);

			byte[] header = "\"time\",\"level\",\"thread\",\"name\",\"text\"\n".getBytes(StandardCharsets.UTF_8);
			int written = this.channel.write(ByteBuffer.wrap(header));
			if (written != header.length) throw new IllegalStateException("Failed to write header to file while rolling collector (written length does not match expected length)");
		} catch (IOException exception) {
			throw new IllegalStateException("Failed to roll collector", exception);
		}
	}
}