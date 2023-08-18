module sirius.stellar.logging {

	requires static org.jetbrains.annotations;

	requires sirius.stellar.facility;

	exports sirius.stellar.logging.collect;
	exports sirius.stellar.logging.dispatch;
	exports sirius.stellar.logging.supplier;
	exports sirius.stellar.logging;

	uses sirius.stellar.logging.dispatch.Dispatcher.Provider;
}