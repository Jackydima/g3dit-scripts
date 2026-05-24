package de.george.g3utils.io;

import org.slf4j.helpers.MessageFormatter;

import de.george.g3utils.util.Misc;

public class G3ReadException extends RuntimeException {
	private final int filePosition;
	private final String fileName;

	public G3ReadException(String message, int filePosition, String fileName) {
		super(formatMessage(message, filePosition, fileName));
		this.filePosition = filePosition;
		this.fileName = fileName;
	}

	public G3ReadException(String message, Throwable cause, int filePosition, String fileName) {
		super(formatMessage(message, filePosition, fileName), cause);
		this.filePosition = filePosition;
		this.fileName = fileName;
	}

	public G3ReadException(String message, int filePosition, String fileName, Object... params) {
		super(formatMessage(message, filePosition, fileName));
		this.filePosition = filePosition;
		this.fileName = fileName;
	}

	public G3ReadException(String message, Throwable cause, int filePosition, String fileName, Object... params) {
		super(formatMessage(message, filePosition, fileName), cause);
		this.filePosition = filePosition;
		this.fileName = fileName;
	}

	private static String formatMessage(String message, int filePosition, String fileName, Object... params) {
		return MessageFormatter.format(message.concat(" ({}, {})"), Misc.concat(params, filePosition, fileName)).getMessage();
	}

	public String getFileName() {
		return fileName;
	}

	public int getFilePosition() {
		return filePosition;
	}
}
