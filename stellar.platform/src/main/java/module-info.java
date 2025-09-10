module sirius.stellar.platform {

	requires static org.jetbrains.annotations;

	requires transitive sirius.stellar.facility;
	requires transitive sirius.stellar.logging;

	requires transitive io.avaje.inject;
	requires transitive io.avaje.jsonb;
	requires transitive io.avaje.jsonb.plugin;

	requires java.management;

	exports sirius.stellar.platform;
	exports sirius.stellar.platform.jsonb;

	provides io.avaje.inject.spi.InjectExtension with sirius.stellar.platform.PlatformModule;
	provides io.avaje.jsonb.spi.JsonbExtension with sirius.stellar.platform.jsonb.GeneratedJsonComponent;
}