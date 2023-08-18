package sirius.stellar.facility

import spock.lang.Specification

class StringsSpecification extends Specification {

	//#region format(String, Object...)
	def "format(String, Object,,,) invokes MessageFormat"() {
		given:
			String format = "Lorem {0} dolor sit amet."
			Object[] arguments = ["ipsum"]
		when:
			def result = Strings.format(format, arguments)
		then:
			result == "Lorem ipsum dolor sit amet."
	}

	def "format(String, Object,,,) invokes String#format"() {
		given:
			String format = "Lorem %s dolor sit amet."
			Object[] arguments = ["ipsum"]
		when:
			def result = Strings.format(format, arguments)
		then:
			result == "Lorem ipsum dolor sit amet."
	}

	def "format(String, Object,,,) with null inputs doesn't throw NullPointerException"() {
		when:
			Strings.format(null, null)
		then:
			notThrown(NullPointerException)
	}

	def "format(String, Object,,,) with null formatting arguments doesn't throw NullPointerException"() {
		given:
			String format = "Lorem {1} dolor sit amet."
			String[] arguments = [null, "ipsum"]
		when:
			Strings.format(format, arguments)
		then:
			notThrown(NullPointerException)
	}
	//#endregion

	//#region format(Locale, String, Object...)
	def "format(Locale, String, Object,,,) invokes MessageFormat"() {
		given:
			Locale locale = Locale.ENGLISH;
			String format = "Lorem {0} dolor sit amet."
			Object[] arguments = ["ipsum"]
		when:
			def result = Strings.format(locale, format, arguments)
		then:
			result == "Lorem ipsum dolor sit amet."
	}

	def "format(Locale, String, Object,,,) invokes String#format"() {
		given:
			Locale locale = Locale.ENGLISH;
			String format = "Lorem %s dolor sit amet."
			Object[] arguments = ["ipsum"]
		when:
			def result = Strings.format(locale, format, arguments)
		then:
			result == "Lorem ipsum dolor sit amet."
	}

	def "format(Locale, String, Object,,,) with null inputs doesn't throw NullPointerException"() {
		when:
			Strings.format(null, null, null)
		then:
			notThrown(NullPointerException)
	}

	def "format(Locale, String, Object,,,) with null formatting arguments doesn't throw NullPointerException"() {
		given:
			String format = "Lorem {1} dolor sit amet."
			String[] arguments = [null, "ipsum"]
		when:
			Strings.format(format, arguments)
		then:
			notThrown(NullPointerException)
	}

	def "format(Locale, String, Object,,,) correctly formats with locale"() {
		given:
			Locale locale = Locale.GERMAN;
			String format = "Foobar costs {0}"
			Object[] arguments = [123_456.789]
		when:
			def result = Strings.format(locale, format, arguments)
		then:
			result == "Foobar costs 123.456,789"
	}
	//#endregion
}