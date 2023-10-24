import sirius.stellar.logging.dispatch.Dispatcher;

module sirius.stellar.logging.minlog {

	requires sirius.stellar.facility;
	requires sirius.stellar.logging;

	requires com.esotericsoftware.minlog;

	exports sirius.stellar.logging.dispatch.minlog;

	provides Dispatcher.Provider with sirius.stellar.logging.dispatch.minlog.MinlogFactory;
}