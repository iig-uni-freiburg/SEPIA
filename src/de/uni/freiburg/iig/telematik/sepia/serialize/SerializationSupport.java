package de.uni.freiburg.iig.telematik.sepia.serialize;

import java.io.IOException;

public interface SerializationSupport {
	
	public String serialize() throws SerializationException;
	
	public void serialize(String path, String fileName, String extension) throws SerializationException, IOException;
	
}
