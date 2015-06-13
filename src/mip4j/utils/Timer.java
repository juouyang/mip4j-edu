package mip4j.utils;

import java.util.Hashtable;

public class Timer {
	private static Hashtable<Integer, Long> startTimeTable = new Hashtable<>();

	public Timer() {
		startTimeTable.put(this.hashCode(), System.nanoTime());
	}

	public double getElapsedTime() {
		return (System.nanoTime() - startTimeTable.get(this.hashCode())) / 1000000.0;
	}

	public void showElapsedTime(String title) {
		System.out.println(title + "\t" + toString(getElapsedTime()));
	}

	public void showElapsedTime() {
		System.out.println(toString(getElapsedTime()));
	}

	public static String toString(double ms) {
		String ret = new String();

		if (ms < 1000)
			ret = String.format("%d ms ", (int) ms);
		else if (ms < 60000)
			ret = String.format("%d s ", (int) (ms / 1000.0))
					+ toString(ms % 1000.0);
		else if (ms < 3600000)
			ret = String.format("%d min ", (int) (ms / 60000.0))
					+ toString(ms % 60000.0);
		else
			ret = String.format("%d hr ", (int) (ms / 3600000.0))
					+ toString(ms % 3600000.0);

		return ret;
	}

	public static void main(String[] args) throws InterruptedException {
		Timer t = new Timer();
		Thread.sleep(62543);
		t.showElapsedTime();
	}
}
