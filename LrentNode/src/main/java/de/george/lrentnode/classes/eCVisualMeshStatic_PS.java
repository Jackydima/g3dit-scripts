package de.george.lrentnode.classes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.george.g3utils.io.G3FileReader;

public class eCVisualMeshStatic_PS extends eCVisualMeshBase_PS {
	private static final Logger logger = LoggerFactory.getLogger(eCVisualMeshStatic_PS.class);

	public eCVisualMeshStatic_PS(String className, G3FileReader reader) {
		super(className, reader);
	}

	@Override
	protected void readPostClassVersion(G3FileReader reader) {
		if (classVersion == 3 || classVersion == 4) {
			reader.raiseError(logger, "Version 3 or 4 are not supported.");
		}
		super.readPostClassVersion(reader);
	}
}
