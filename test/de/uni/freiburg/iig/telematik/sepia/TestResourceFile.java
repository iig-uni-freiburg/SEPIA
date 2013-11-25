package de.uni.freiburg.iig.telematik.sepia;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;

import org.junit.rules.ExternalResource;

/**
 * <p>
 * This class extends the {@link ExternalResource} class from JUnit and adds the functionality to read resources as {@link InputStream}, as {@link File}, or as
 * {@link String}.
 * </p>
 * <p>
 * As it can be seen in the javadocs of JUnit, this class should be used with the <code>@Rule</code> annotation:
 * </p>
 * 
 * <pre>
 * import static org.junit.Assert.*;
 * 
 * import org.junit.Rule;
 * import org.junit.Test;
 * 
 * public class SomeRandomTest {
 * 	&#064;Rule
 * 	public TestResourceFile resA = new TestResourceFile(&quot;someXMLFile.xml&quot;);
 * 
 * 	&#064;Test
 * 	public void test() throws Exception {
 * 		assertTrue(resA.getFile().exists());
 * 	}
 * }
 * </pre>
 * 
 * <p>
 * Due to its methods <code>before()</code> and <code>after()</code> the {@link InputStream} and {@link File} objects gets initialized and closed or cleaned up
 * automatically.
 * </p>
 * 
 * @author Adrian Lange
 */
public class TestResourceFile extends ExternalResource {

	/** Path to the resource to load. Given by the constructor. */
	private String resourcePath;
	/** {@link File} object representing the resource. */
	private File file = null;
	/** {@link InputStream} to read the resource. Gets initialized by the method <code>before()</code> and closed by the method <code>after()</code>. */
	private InputStream stream = null;

	/**
	 * Creates a new TestResourceFile from the given path.
	 */
	public TestResourceFile(String resourcePath) {
		if (resourcePath == null)
			throw new NullPointerException("Resource path mustn't be null!");
		this.resourcePath = resourcePath;
	}

	/**
	 * Returns the resource as {@link File} object.
	 */
	public File getFile() {
		if (file == null) {
			createFile();
		}
		return file;
	}

	/**
	 * <p>
	 * Returns the {@link InputStream} that is used to read the resource.
	 * </p>
	 * <p>
	 * Because it gets initialized in the method <code>before()</code>, there's no need to check if the stream is <code>null</code> or not.
	 * </p>
	 */
	public InputStream getInputStream() {
		return stream;
	}

	/**
	 * Returns the content of the resource using the charset <i>utf-8</i>.
	 */
	public String getContent() throws IOException {
		return getContent("utf-8");
	}

	/**
	 * Returns the content of the resource using the given charset.
	 */
	public String getContent(String charSet) throws IOException {
		InputStreamReader reader = new InputStreamReader(stream, Charset.forName(charSet));
		char[] tmp = new char[4096];
		StringBuilder str = new StringBuilder();
		try {
			while (true) {
				int length = reader.read(tmp);
				if (length < 0) {
					break;
				}
				str.append(tmp, 0, length);
			}
			reader.close();
		} finally {
			reader.close();
		}
		return str.toString();
	}

	/**
	 * Initializes the resource file and initiates {@link InputStream}.
	 */
	@Override
	protected void before() throws Throwable {
		super.before();
		stream = getClass().getClassLoader().getResourceAsStream(resourcePath);
	}

	/**
	 * Teardown of the resource file. Executed after the test. Closes the {@link InputStream} and deletes temporary files.
	 */
	@Override
	protected void after() {
		try {
			stream.close();
		} catch (Exception e) {
		}
		super.after();
	}

	/**
	 * Creates a {@link File} object from the resource.
	 */
	private void createFile() {
		URL url = getClass().getClassLoader().getResource(resourcePath);
		if (url != null) {
			URI uri = null;
			try {
				uri = url.toURI();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			if (uri != null)
				file = new File(uri);
		}
		if (url == null || file == null) {
			// having an initialized File object leads to an assertion error instead of a NullPointerException
			file = new File(resourcePath);
		}
	}
}
