import sirius.stellar.logging.dispatch.log4j2x.Log4j2ContextFactory;

module sirius.stellar.logging.log4j2x {

	requires sirius.stellar.facility;
	requires sirius.stellar.logging;

	requires org.apache.logging.log4j;

	exports sirius.stellar.logging.dispatch.log4j2x;

	provides org.apache.logging.log4j.spi.LoggerContextFactory with Log4j2ContextFactory;
}