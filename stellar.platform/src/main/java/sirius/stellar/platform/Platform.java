package sirius.stellar.platform;

import io.avaje.inject.BeanScope;
import io.avaje.inject.BeanScopeBuilder;
import io.avaje.jsonb.Json;
import org.jetbrains.annotations.Contract;
import sirius.stellar.facility.Strings;
import sirius.stellar.facility.concurrent.Latched;
import sirius.stellar.logging.Logger;
import sirius.stellar.logging.collect.Collector;

import java.io.Serial;
import java.io.Serializable;
import java.lang.management.*;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.Temporal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.random.RandomGenerator;

import static java.lang.management.ManagementFactory.*;

/**
 * This class represents the central component of the framework facilitating
 * inversion-of-control and the management of dependencies within applications.
 * <p>
 * It provides an entrypoint that can be used by the application to achieve a
 * very opinionated startup process where the only classes in any given source
 * tree will be data models, singleton beans, controllers, views, etc.
 * <p>
 * Injecting this class into any given bean provides a lot of global metadata
 * about the current process. The static methods provided by this class are
 * only to be used to retro-fit applications written contrary to the paradigm
 * promoted by the use of this class, and their use should be avoided.
 * <p>
 * Here is a full list of provided beans in the inversion-of-control container:
 * <ul>
 *     <li>
 *         Useful JMX beans, including:
 *         <ul>
 *     	       <li>{@link ClassLoadingMXBean}</li>
 *             <li>{@link CompilationMXBean}</li>
 *             <li>
 *                 {@link GarbageCollectorMXBean}s
 *                 (inject using a {@link List} or {@link Set})
 *             </li>
 *             <li>{@link MemoryMXBean}</li>
 *             <li>
 *                 {@link MemoryManagerMXBean}s
 *                 (inject using a {@link List} or {@link Set})
 *             </li>
 *             <li>
 *                 {@link MemoryPoolMXBean}s
 *                 (inject using a {@link List} or {@link Set})
 *             </li>
 *             <li>{@link OperatingSystemMXBean}</li>
 *             <li>{@link RuntimeMXBean}</li>
 *             <li>{@link ThreadMXBean}</li>
 *         </ul>
 *     </li>
 *     <li>
 *         {@link ExecutorService} - {@link Executors#newVirtualThreadPerTaskExecutor()}
 *         is used to create the provided instance (it executes with virtual threads).
 *     </li>
 *     <li>
 *         {@link RandomGenerator} - {@link SecureRandom#SecureRandom()} is used to create
 *         the provided instance (prioritizing randomness over performance, as generation
 *         is an I/O operation) - if this is undesirable, a different instance could be
 *         provided in your own factory, then that specific instance retrieved directly
 *         instead of the abstract interface in order to use it.
 *     </li>
 *     <li>
 *         {@link Platform} - An instance of this class which can be used to retrieve any
 *         global metadata about the current process, e.g., {@link Platform#startup()}.
 *     </li>
 *     <li>{@link Runtime} - Retrieved with {@link Runtime#getRuntime()}.</li>
 * </ul>
 *
 * @since 1u1
 * @author Mechite
 */
@Json
public final class Platform implements AutoCloseable, Serializable {

	private static final Latched<BeanScope> scope = new Latched<>();

	@Serial
	private static final long serialVersionUID = 561030072926744922L;

	@Json.Ignore
	private transient final String[] arguments;

	@Json.Ignore
	private transient final List<Consumer<BeanScope>> hooks;

	@Json.Property("uid")
	private final String identifier;

	@Json.Property("stp")
	private final Instant startup;

	@Json.Property("cfg")
	private final Properties configuration;

	@Json.Property("ins")
	private final Set<Platform> instances;

	/**
	 * Constructor used to instantiate the bean to insert it into the scope.
	 * This should never be called in a written application source directly.
	 * <p>
	 * This constructor will also populate the configuration object by loading potential
	 * file candidates or default resource versions, as well as environment variables and
	 * application properties, i.e. (in order - any source that loads files with wildcard
	 * as demonstrated in the name shown first selects the files in lexicographical order,
	 * and from the current working directory):
	 * <ol>
	 *     <li>*.properties</li>
	 *     <li>*.env - These are parsed as properties files so may be incompatible with
	 *     the full dotenv syntax.</li>
	 *     <li>*.json - These are parsed with Avaje Jsonb into a {@code Map<String, String>}
	 *     as other types are not supported here.</li>
	 *     <li>*.yaml &amp; *.yml - These are parsed with SnakeYAML if present (an optional
	 *     dependency) into a {@code Map<String, String>}. If SnakeYAML is not present, they
	 *     will simply not be loaded.</li>
	 *     <li>{@link System#getProperties() System Properties}</li>
	 *     <li>{@link System#getenv() Environment Variables}</li>
	 * </ol>
	 * Everything listed here can have a default version defined by appending {@code .defc}
	 * to the end of the extension, e.g., {@code .properties.defc} and putting it anywhere
	 * in any JAR on the module path, or anywhere on the classpath. It will be copied into
	 * the current working directory with the extension stripped unless a file already
	 * exists there (done before anything is loaded).
	 * <p>
	 * Default configurations are useful in order to add comments to document the options
	 * inside them as well as in order to simply provide a file, with default options set
	 * so that the application will function without any configuration.
	 * <p>
	 * However, it should be noted that environment variables should always be preferred
	 * over any other option, and will override any other option as they are loaded last.
	 */
	public Platform(String[] arguments, Instant startup) {
		this.arguments = arguments;
		this.hooks = new ArrayList<>();

		this.identifier = Instant.now().toEpochMilli() + "-" + UUID.randomUUID();
		this.startup = startup;
		this.configuration = new Properties();
		this.instances = new HashSet<>();
	}

	/**
	 * The entrypoint that should be used if the framework is being used in full.
	 * <p>
	 * This will automatically create an inversion-of-control container and wire
	 * all dependencies using the {@link java.util.ServiceLoader}.
	 * <p>
	 * When this entrypoint is being used, the {@link Platform} class is available
	 * inside the container and can be injected into any wired beans. For example:
	 * <pre>{@code
	 *     @Component
	 *     class Example {
	 *
	 *         @Inject
	 *         Example(Platform platform) {
	 *             // ...
	 *         }
	 *     }
	 * }</pre>
	 * However, at the point in time in which the constructor would be invoked, only
	 * the instance methods against this class should be invoked (i.e., against the
	 * instance that is injected). Methods such as {@link Platform#scope()} should
	 * not be called as the scope has not finished being built. Should these methods
	 * be needed, they can be accessed in an instance method inside a given class
	 * annotated with {@link io.avaje.inject.PostConstruct}, as follows:
	 * <pre>{@code
	 *     // (omit class declaration, @Component, etc for brevity) ...
	 *     @PostConstruct
	 *     public void postConstruct(BeanScope scope) {
	 *         // ...
	 *     }
	 * }</pre>
	 * The {@link BeanScope} argument is provided here to demonstrate how the scope
	 * can be injected directly through the method as this is a common use case (though,
	 * an anti-pattern in most cases). However, it can be omitted, and any method
	 * simply annotated with {@link io.avaje.inject.PostConstruct} will be executed.
	 * <p>
	 * This also calls {@code Logger.collector(Collector.console())} to forward logging
	 * to console, as if this entrypoint is being used, that is most likely desirable
	 * behavior. If this is not desirable behavior, this entrypoint should not be used
	 * in favor of a domain-specific fix, eventually migrating to use the framework in
	 * full and allowing the logging system to collect logs to console as well.
	 * <p>
	 * Remaining on the topic of potential side effects, this method allows for a small
	 * amount of flexibility in the contract for the "arguments" parameter; not only is
	 * it using a declaration with varargs, but it also can be provided as null (which
	 * case it will simply be replaced with {@code new String[0]}), or contain null
	 * values inside the array (which will be replaced with {@link Strings#EMPTY}).
	 * <p>
	 * This functionality should not provide any benefit to applications conforming to
	 * well-executed paradigms, but is beneficial to retrofit applications that may have
	 * complex launch sequences that may call this method from a different context to the
	 * common Java Virtual Machine initialization; in any case, the contract here is very
	 * lenient and allows for any of these environments to be retrofitted, stating this as
	 * the application entrypoint.
	 *
	 * @see Platform#Platform(String[], Instant)
	 * @since 1u1
	 */
	public static void main(String... arguments) {
		Instant startup = Instant.ofEpochMilli(getRuntimeMXBean().getStartTime());
		Logger.collector(Collector.console());

		Platform platform = new Platform(arguments == null ? new String[0] : Arrays.stream(arguments)
				.map(argument -> argument == null ? Strings.EMPTY : argument)
				.toArray(String[]::new), startup);
		Runtime runtime = Runtime.getRuntime();

		BeanScopeBuilder builder = BeanScope.builder();
		builder.shutdownHook(true);

		builder.bean(ClassLoadingMXBean.class, getClassLoadingMXBean());
		builder.bean(CompilationMXBean.class, getCompilationMXBean());

		getGarbageCollectorMXBeans().forEach(bean -> builder.bean(GarbageCollectorMXBean.class, bean));

		builder.bean(MemoryMXBean.class, getMemoryMXBean());
		getMemoryManagerMXBeans().forEach(bean -> builder.bean(MemoryManagerMXBean.class, bean));
		getMemoryPoolMXBeans().forEach(bean -> builder.bean(MemoryPoolMXBean.class, bean));

		builder.bean(OperatingSystemMXBean.class, getOperatingSystemMXBean());
		builder.bean(RuntimeMXBean.class, getRuntimeMXBean());
		builder.bean(ThreadMXBean.class, getThreadMXBean());

		builder.bean(ExecutorService.class, Executors.newVirtualThreadPerTaskExecutor());
		builder.bean(RandomGenerator.class, new SecureRandom());

		builder.bean(Platform.class, platform);
		builder.bean(Runtime.class, runtime);

		scope.set(builder.build());
		scope.release();

		Runtime.getRuntime().addShutdownHook(new Thread(Logger::close));
	}

	/**
	 * Returns the command line arguments associated with the current process, if
	 * any, or simply returns an empty array.
	 *
	 * @since 1u1
	 */
	public String[] arguments() {
		return arguments;
	}

	/**
	 * Returns when this platform bean was instantiated.
	 * <p>
	 * Essentially, this method allows you to return the time in which the process
	 * was started, so that {@link RuntimeMXBean#getStartTime()} does not have to
	 * be used, relieving the direct dependency on {@code java.lang.management}.
	 *
	 * @since 1u1
	 */
	public Instant startup() {
		return this.startup;
	}

	/**
	 * Returns the duration in which this platform bean has been available.
	 * <p>
	 * Essentially, this method allows you to return the total time in which the process
	 * has been alive for so that {@link RuntimeMXBean#getUptime()} does not have to be
	 * used, relieving the direct dependency on {@code java.lang.management}.
	 *
	 * @since 1u1
	 */
	// TODO - @Json.Property("upt")
	public Duration uptime() {
		return Duration.ofMillis(scope().get(RuntimeMXBean.class).getUptime());
	}

	/**
	 * Returns the duration between when this platform bean has been available and the
	 * provided {@link Temporal} (whose precision influences the output precision).
	 * <p>
	 * Essentially, this method allows you to return the total time in which the process
	 * has been alive for so that {@code RuntimeMXBean.getUptime()} does not have to be
	 * used, relieving the direct dependency on {@code java.lang.management}.
	 * <p>
	 * This method is different to {@link Platform#uptime()} as you could provide the
	 * return value of {@link Platform#startup()} to get an uptime value that does not
	 * use {@link RuntimeMXBean} at the invocation level for calculation, while that
	 * method retrieves the {@link RuntimeMXBean} and invokes {@code getUptime()}.
	 *
	 * @since 1u1
	 */
	public Duration uptime(Temporal temporal) {
		return Duration.between(temporal, this.startup);
	}

	/**
	 * Registers a shutdown hook which executes at shutdown and consumes {@link BeanScope}
	 * to allow for dependencies to be accessed inside the scope of the hook.
	 * <p>
	 * Essentially, this method allows you to safely add shutdown hooks that will not cause
	 * logging to be negatively affected by a hook registered late, as it will run alongside
	 * the {@link AutoCloseable#close()} method for any auto-closeable beans that are registered
	 * inside the scope. A usage exemplar is as follows:
	 * <pre>{@code
	 *     platform.shutdownHook(scope -> {
	 *         scope.get(Runtime.class).exec("echo Hello, world!");
	 *     });
	 * }</pre>
	 *
	 * @since 1u1
	 */
	@Contract(value = "null -> fail; !null -> this", pure = true)
	public Platform shutdownHook(Consumer<BeanScope> hook) {
		if (hook == null) throw new NullPointerException("Shutdown hook cannot be null");
		this.hooks.add(hook);
		return this;
	}

	@Override
	public void close() {
		for (Consumer<BeanScope> hook : this.hooks) hook.accept(Platform.scope());
	}

	/**
	 * Returns the {@link BeanScope} associated with the current process.
	 * <p>
	 * If all beans have not yet been wired, this method will wait until the scope
	 * has finished being created (and all beans being wired).
	 * <p>
	 * Therefore, this method is entirely thread-safe, though it should not be called
	 * during the creation/instantiation of a bean as this would cause a deadlock and
	 * prevent the scope from ever finishing being created.
	 * <p>
	 * It is also quite common that many code analysis tools will report the use of
	 * an {@link AutoCloseable} without ever calling the {@link AutoCloseable#close()}
	 * method or using a "try-with-resources" statement - the scope should never be
	 * closed through this method unless required, as a shutdown hook is always already
	 * registered for it.
	 *
	 * @since 1u1
	 */
	public static BeanScope scope() {
		return scope.get();
	}

	/**
	 * Returns the instance of {@link Platform} associated with the current process.
	 * <p>
	 * This is shorthand to directly invoking {@code Platform.scope().get(Platform.class)}
	 * and is therefore thread-safe and will block the current thread until the scope is
	 * available (more information available in the {@link Platform#scope()} method).
	 *
	 * @see Platform#scope()
	 * @since 1u1
	 */
	public static Platform platform() {
		return scope().get(Platform.class);
	}
}