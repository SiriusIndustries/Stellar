package sirius.stellar.facility.terminal;

/**
 * An enumerator for ANSI escape codes that specify foreground (text) and background colors.
 * <p>
 * These codes are used to control the color of output to terminal or console environments
 * that support ANSI escape sequences.
 * <p>
 * This enumerator offers a "fluent" syntax for obtaining a {@link TerminalConstant} for any
 * given value in order to allow a large amount of customization to the output colors.
 * <p>
 * {@link TerminalColor#DEFAULT} is a special case in this enumerator as it does not differ
 * whether it is dark or bright, the output code is the same (the default color of the current
 * environment). It does differ between whenever it is a foreground or a background color as
 * that defines where it is applied. This is why it is semantically quite different to using
 * {@link TerminalStyle#RESET} instead.
 * <p>
 * A usage exemplar is as follows (where {@code ...TerminalColor.*} is statically imported):
 *
 * <pre>{@code
 *     Strings.format(
 *         "{2}Here are some colors, {0}red {2}and {1}blue{2}.",
 *         RED.foreground().bright(),
 *         BLUE.foreground().bright(),
 *         DEFAULT.foreground().bright()
 *     );
 * }</pre>
 * <p>
 * The documentation for each constant inside of this file provides a trivial overview of what
 * each state of a given color is expected to look like, but it should be understood that the
 * actual display color will differ depending on the terminal that is being output to.
 *
 * @see TerminalColorScoped
 *
 * @since 1u1
 * @author Mechite
 */
public enum TerminalColor {

	/**
	 * <p style="color: #000000">Dark</p>
	 * <p style="color: #565758">Bright</p>
	 */
	BLACK(new TerminalColorScoped(30, 90), new TerminalColorScoped(40, 100)),

	/**
	 * <p style="color: #E5504F">Dark</p>
	 * <p style="color: #F54152">Bright</p>
	 */
	RED(new TerminalColorScoped(31, 91), new TerminalColorScoped(41, 101)),

	/**
	 * <p style="color: #58912F">Dark</p>
	 * <p style="color: #4DBD19">Bright</p>
	 */
	GREEN(new TerminalColorScoped(32, 92), new TerminalColorScoped(42, 102)),

	/**
	 * <p style="color: #A08712">Dark</p>
	 * <p style="color: #DDBC0A">Bright</p>
	 */
	YELLOW(new TerminalColorScoped(33, 93), new TerminalColorScoped(43, 103)),

	/**
	 * <p style="color: #3890D0">Dark</p>
	 * <p style="color: #24B2FD">Bright</p>
	 */
	BLUE(new TerminalColorScoped(34, 94), new TerminalColorScoped(44, 104)),

	/**
	 * <p style="color: #A374C0">Dark</p>
	 * <p style="color: #E57FEB">Bright</p>
	 */
	MAGENTA(new TerminalColorScoped(35, 95), new TerminalColorScoped(45, 105)),

	/**
	 * <p style="color: #08A6A7">Dark</p>
	 * <p style="color: #02DCDE">Bright</p>
	 */
	CYAN(new TerminalColorScoped(36, 96), new TerminalColorScoped(46, 106)),

	/**
	 * <p style="color: #7B7C7E">Dark</p>
	 * <p style="color: #F5F6F7">Bright</p>
	 */
	WHITE(new TerminalColorScoped(37, 97), new TerminalColorScoped(47, 107)),

	/**
	 * Previews for this constant are not available as it represents the default
	 * colors for any given terminal environment.
	 * <p>
	 * This includes the default background colors too, so if you are looking to
	 * e.g., flip the colors for a selection on otherwise default colored text,
	 * look into {@link TerminalStyle}.
	 */
	DEFAULT(new TerminalColorScoped(39, 39), new TerminalColorScoped(49, 49));

	private final TerminalColorScoped foreground;
	private final TerminalColorScoped background;

	TerminalColor(TerminalColorScoped foreground, TerminalColorScoped background) {
		this.foreground = foreground;
		this.background = background;
	}

	/**
	 * Returns this color as a foreground (text) color.
	 * @since 1u1
	 */
	public TerminalColorScoped foreground() {
		return this.foreground;
	}

	/**
	 * Returns this color as a background color.
	 * @since 1u1
	 */
	public TerminalColorScoped background() {
		return this.background;
	}

	/**
	 * Represents either a foreground (text) or background color (not abstract).
	 *
	 * @see TerminalColor
	 * @since 1u1
	 */
	public static class TerminalColorScoped {

		private final int dark;
		private final int bright;

		private TerminalColorScoped(int dark, int bright) {
			this.dark = dark;
			this.bright = bright;
		}

		/**
		 * Gets the constant, dark escape code value of this terminal color.
		 * This can then be output to any supported environment using {@link TerminalConstant#toString()}.
		 *
		 * @see TerminalConstant
		 * @since 1u1
		 */
		public TerminalConstant dark() {
			return new TerminalConstant(this.dark);
		}

		/**
		 * Gets the constant, bright escape code value of this terminal color.
		 * This can then be output to any supported environment using {@link TerminalConstant#toString()}.
		 *
		 * @see TerminalConstant
		 * @since 1u1
		 */
		public TerminalConstant bright() {
			return new TerminalConstant(this.bright);
		}
	}
}