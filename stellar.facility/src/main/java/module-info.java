module sirius.stellar.facility {

	requires static org.jetbrains.annotations;
	requires static sirius.stellar.jsr305x;

	exports sirius.stellar.facility.concurrent;
	exports sirius.stellar.facility.doctation;
	exports sirius.stellar.facility.exception;
	exports sirius.stellar.facility.executor;
	exports sirius.stellar.facility.terminal;
	exports sirius.stellar.facility.tuple;
	exports sirius.stellar.facility;
}