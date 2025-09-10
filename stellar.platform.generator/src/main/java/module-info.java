module sirius.stellar.platform.generator {

	requires java.compiler;
	requires sirius.stellar.platform;

	exports sirius.stellar.platform.generator;

	provides javax.annotation.processing.Processor with sirius.stellar.platform.generator.PlatformConfigurationProcessor;
}