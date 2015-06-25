package de.uni.freiburg.iig.telematik.sepia.serialize;

import java.io.File;
import java.io.IOException;

import de.invation.code.toval.file.FileUtils;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.Labeling;

public class LabelingSerialization {

	public static String serialize(Labeling labeling) throws SerializationException {
		Validate.notNull(labeling);
		return new LabelingSerializer(labeling).serialize();
	}

	public static void serialize(Labeling labeling, String fileName) throws SerializationException, IOException {
		Validate.noDirectory(fileName);

		File file = new File(fileName);
		serialize(labeling, FileUtils.getPath(file), FileUtils.separateFileNameFromEnding(file));
	}

	public static void serialize(Labeling labeling, String path, String fileName) throws SerializationException, IOException {
		Validate.notNull(labeling);
		new LabelingSerializer(labeling).serialize(path, fileName);
	}

}
