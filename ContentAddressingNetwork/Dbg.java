public class Dbg {
	/**
	 * Debug level. Should change to 1 (show stop error only) in the final release.
	 * 3 - Print all the debug information, warnings (exceptions), and stop errors, with error location.
	 * 2 - Print warnings and stop errors, with location.
	 * 1 - Print error messages, no location information. It's for product release use, not for debug.
	 */
	static int DEBUG_LEVEL = 1;


	public static void verbose(String s) {
		if (DEBUG_LEVEL < 3)
			return;
		
		System.out.print("[VERBOSE]");
		System.out.println(s);
	}
	

	public static void warning(String s) {
		if (DEBUG_LEVEL < 2)
			return;
		
		System.out.print("[WARNING]");
		System.out.println(s);
	}
	
	
	public static void stop(String s) {
		System.out.println(s);
		
		throw new Error(s);
	}
	
	
	public static void kickass(String s) {
		System.out.println("[KICK ASS] I suppose this log will never be printed. If you are lucky see this, please " +
				"print this screen and take it to see Kenny and ask him for a free meal. His email:she@scu.edu");
		System.out.println(s);
	}
}
