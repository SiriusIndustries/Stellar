module sirius.stellar.logging.tinylog {

	requires sirius.stellar.facility;
	requires sirius.stellar.logging;

	requires org.tinylog.api;

	exports sirius.stellar.logging.dispatch.tinylog;

	provides org.tinylog.provider.LoggingProvider with sirius.stellar.logging.dispatch.tinylog.TinylogDispatcher;
}