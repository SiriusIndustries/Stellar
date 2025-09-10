package sirius.stellar.facility.stream

import spock.lang.Specification

import java.util.stream.Stream

import static sirius.stellar.facility.stream.TerminatingStream.*

class TerminatingStreamSpecification extends Specification {

	def "Stream wrapped with #terminalStream() correctly terminates provided stream on terminal operation"() {
		given:
			def stream = Stream.of("a", "b", "c", "d")
			def runnable = Mock(Runnable)
			stream.onClose(runnable)
		when:
			stream = terminalStream(stream)
			def values = stream.toList()
		then:
			1 * runnable.run()
	}
}