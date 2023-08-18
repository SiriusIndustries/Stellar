import sirius.stellar.logging.dispatch.Dispatcher;

module sirius.stellar.logging.jul {

	requires sirius.stellar.facility;
	requires sirius.stellar.logging;

	requires java.logging;

	provides Dispatcher.Provider with sirius.stellar.logging.dispatch.jul.JulFactory;
}