import sirius.stellar.logging.dispatch.log4j2.Log4j2ContextFactory;

module sirius.stellar.logging.slf4j {

	requires sirius.stellar.facility;
	requires sirius.stellar.logging;

	requires org.apache.logging.log4j;

	provides org.apache.logging.log4j.spi.LoggerContextFactory with Log4j2ContextFactory;
}