module sirius.stellar.logging.jcl {

	requires sirius.stellar.facility;
	requires sirius.stellar.logging;

	requires org.apache.commons.logging;

	provides org.apache.commons.logging.LogFactory with sirius.stellar.logging.dispatch.jcl.JclFactory;
}