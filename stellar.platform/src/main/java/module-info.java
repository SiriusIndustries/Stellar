module sirius.stellar.platform {

	requires static org.jetbrains.annotations;

	requires transitive sirius.stellar.facility;
	requires transitive sirius.stellar.logging;

	requires transitive io.avaje.inject;
	requires transitive io.avaje.jsonb;

	requires java.management;

	//	provides io.avaje.inject.spi.Module with sirius.stellar.platform.PlatformModule;
}