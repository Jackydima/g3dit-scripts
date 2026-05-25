package de.george.g3utils.io;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.george.g3utils.structure.Stringtable;
import de.george.g3utils.util.Exceptions;
import de.george.g3utils.util.Misc;

public abstract class GenomeFile implements Saveable {
	private static final Logger logger = LoggerFactory.getLogger(GenomeFile.class);

	protected static final byte[] GENOME_MAGIC = Misc.asByte("47454E4F4D464C45");

	protected Stringtable stringtable;

	public Stringtable getStringtable() {
		return stringtable;
	}

	protected final void read(G3FileReaderEx reader) throws IOException {
		raiseNotGenomeFile(reader);

		reader.skip(2); // Skip Version

		int deadbeefOffset = reader.readInt();

		reader.readStringtable(deadbeefOffset + 4);
		stringtable = reader.getStringtable();

		readInternal(reader);
	}

	protected final void write(G3FileWriterEx writer) throws IOException {
		writer.write(GENOME_MAGIC).writeUnsignedShort(1);

		// Size placeholder
		writer.writeInt(0xFFFFFFFF);

		writeInternal(writer);

		writer.write("EFBEADDE");
		int deadbeef = writer.getSize() - 4;
		writer.replaceInt(deadbeef, 10); // DEADBEEF Platzhalter mit dem richtigen Wert ersetzen

		writeInternalAfterDeadbeef(writer, deadbeef);

		// Stringtable schreiben
		writer.writeStringtable();
	}

	protected abstract void readInternal(G3FileReaderEx reader) throws IOException;

	protected abstract void writeInternal(G3FileWriterEx writer) throws IOException;

	protected void writeInternalAfterDeadbeef(G3FileWriterEx writer, int deadbeef) {

	}

	private G3FileWriterEx prepareSave() throws IOException {
		G3FileWriterEx writer = new G3FileWriterEx("");
		if (stringtable == null) {
			stringtable = new Stringtable();
		}
		writer.setStringtable(stringtable);
		write(writer);
		return writer;
	}

	@Override
	public void save(Path file) throws IOException {
		Exceptions.wrapAsIoException(() -> {
			prepareSave().save(file);
			return null;
		});
	}

	@Override
	public void save(OutputStream out) throws IOException {
		Exceptions.wrapAsIoException(() -> {
			prepareSave().save(out);
			return null;
		});
	}

	protected static void raiseNotGenomeFile(G3FileReaderEx reader) throws IOException {
		if (!isGenomeFile(reader)) {
			reader.raiseError(logger, "Not a valid Genome file.");
		}
		reader.skip(8);
	}

	public static boolean isGenomeFile(G3FileReader reader) {
		return reader.getSize() >= 8 && Arrays.equals(reader.readSilentByteArray(8), GENOME_MAGIC);
	}
}
