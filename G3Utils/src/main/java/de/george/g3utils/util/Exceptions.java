package de.george.g3utils.util;

import java.io.IOException;
import java.util.concurrent.Callable;

public interface Exceptions {
	public static <T> T wrapAsIoException(Callable<T> func) throws IOException {
		try {
			return func.call();
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new IOException(e);
		}
	}
}
