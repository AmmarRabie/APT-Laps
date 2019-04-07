
public abstract class Util {
	private Util() {}
	
	public static void Log(String message) {
		System.out.println(Thread.currentThread().getName() + ": " + message);
	}
}
