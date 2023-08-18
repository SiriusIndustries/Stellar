module sirius.stellar.logging.jsr379 {

	requires sirius.stellar.facility;
	requires sirius.stellar.logging;

	provides System.LoggerFinder with sirius.stellar.logging.dispatch.jsr379.Jsr379Finder;
}