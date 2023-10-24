@InjectModule(provides = {
		ClassLoadingMXBean.class,
		CompilationMXBean.class,
		GarbageCollectorMXBean.class,
		MemoryMXBean.class,
		MemoryManagerMXBean.class,
		MemoryPoolMXBean.class,

		OperatingSystemMXBean.class,
		RuntimeMXBean.class,
		ThreadMXBean.class,

		ExecutorService.class,
		RandomGenerator.class,

		Platform.class,
		Runtime.class
})
package sirius.stellar.platform;

import io.avaje.inject.InjectModule;

import java.lang.management.*;
import java.util.concurrent.ExecutorService;
import java.util.random.RandomGenerator;