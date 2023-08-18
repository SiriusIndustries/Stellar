module sirius.stellar.logging.minlog {

	requires sirius.stellar.facility;
	requires sirius.stellar.logging;

	requires org.jboss.logging;

	provides org.jboss.logging.LoggerProvider with sirius.stellar.logging.dispatch.jboss.JbossProvider;
}