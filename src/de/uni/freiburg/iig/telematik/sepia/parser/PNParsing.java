package de.uni.freiburg.iig.telematik.sepia.parser;

import java.io.File;
import java.io.IOException;

import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.parser.ParserException.ErrorCode;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.parser.petrify.PetrifyParser;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParser;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;

/**
 * <p>
 * Parser class which determines the file type and calls the specific parser
 * implementing the {@link PNParserInterface}.
 * </p>
 * <ul>
 * <li>determine file type</li>
 * <li>call respective parser</li>
 * </ul>
 *
 * @author Adrian Lange
 *
 */
public class PNParsing {

    /**
     * Replacement string for invalid character blocks while sanitizing element
     * names.
     */
    public final static String SANITIZE_INVALID_CHARACTER_REPLACEMENT = "_";

    /**
     * Parses the given file with the parser respective to the file extension.
     *
     * @param <P> Place type
     * @param <T> Transition type
     * @param <F> Flow relation type
     * @param <M> Marking type
     * @param <S> Node value type
     * @param <N> Petri net type
     * @param <G> Petri net graphics type
     * @param file File to parse
     * @return A {@link AbstractGraphicalPN}
     * @throws IOException If the file can't be found or read
     * @throws ParserException For exceptions caused by the parsing
     */
    public static synchronized <P extends AbstractPlace<F, S>,
                                T extends AbstractTransition<F, S>,
                                F extends AbstractFlowRelation<P, T, S>,
                                M extends AbstractMarking<S>,
                                S extends Object,
                                N extends AbstractPetriNet<P, T, F, M, S>,
                                G extends AbstractPNGraphics<P, T, F, M, S>>
            AbstractGraphicalPN<P, T, F, M, S, N, G>
            parse(File file) throws IOException, ParserException {
        validateFile(file);
        PNParsingFormat format = guessFormat(file);
        if (format == null) {
            throw new ParserException(ErrorCode.UNKNOWN_FILE_EXTENSION);
        }
        PNParserInterface parser = getParser(file, format);
        return parser.<P, T, F, M, S, N, G>parse(file);
    }

    /**
     * Parses the given file with the parser respective to the file extension.
     *
     * @param <P> Place type
     * @param <T> Transition type
     * @param <F> Flow relation type
     * @param <M> Marking type
     * @param <S> Node value type
     * @param <N> Petri net type
     * @param <G> Petri net graphics type
     * @param fileName File to parse
     * @return A {@link AbstractGraphicalPN}
     * @throws IOException If the file can't be found or read
     * @throws ParserException For exceptions caused by the parsing
     */
    public static synchronized <P extends AbstractPlace<F, S>,
                                T extends AbstractTransition<F, S>,
                                F extends AbstractFlowRelation<P, T, S>,
                                M extends AbstractMarking<S>,
                                S extends Object,
                                N extends AbstractPetriNet<P, T, F, M, S>,
                                G extends AbstractPNGraphics<P, T, F, M, S>>
            AbstractGraphicalPN<P, T, F, M, S, N, G>
            parse(String fileName) throws IOException, ParserException {
        Validate.notNull(fileName);
        return PNParsing.<P, T, F, M, S, N, G>parse(prepareFile(fileName));
    }

    /**
     * Parses the given file with the parser respective to the file extension.
     *
     * @param <P> Place type
     * @param <T> Transition type
     * @param <F> Flow relation type
     * @param <M> Marking type
     * @param <S> Node value type
     * @param <N> Petri net type
     * @param <G> Petri net graphics type
     * @param file File to parse
     * @param format Format to parse from
     * @return A {@link AbstractGraphicalPN}
     * @throws IOException If the file can't be found or read
     * @throws ParserException For exceptions caused by the parsing
     */
    public static synchronized <P extends AbstractPlace<F, S>,
                                T extends AbstractTransition<F, S>,
                                F extends AbstractFlowRelation<P, T, S>,
                                M extends AbstractMarking<S>,
                                S extends Object,
                                N extends AbstractPetriNet<P, T, F, M, S>,
                                G extends AbstractPNGraphics<P, T, F, M, S>>
            AbstractGraphicalPN<P, T, F, M, S, N, G>
            parse(File file, PNParsingFormat format) throws IOException, ParserException {
        validateFile(file);
        Validate.notNull(format);
        PNParserInterface parser = getParser(file, format);
        return parser.<P, T, F, M, S, N, G>parse(file);
    }

    /**
     * Parses the given file with the parser respective to the file extension.
     *
     * @param <P> Place type
     * @param <T> Transition type
     * @param <F> Flow relation type
     * @param <M> Marking type
     * @param <S> Node value type
     * @param <N> Petri net type
     * @param <G> Petri net graphics type
     * @param fileName File to parse
     * @param format Format to parse from
     * @return A {@link AbstractGraphicalPN}
     * @throws IOException If the file can't be found or read
     * @throws ParserException For exceptions caused by the parsing
     */
    public static synchronized <P extends AbstractPlace<F, S>,
                                T extends AbstractTransition<F, S>,
                                F extends AbstractFlowRelation<P, T, S>,
                                M extends AbstractMarking<S>,
                                S extends Object,
                                N extends AbstractPetriNet<P, T, F, M, S>,
                                G extends AbstractPNGraphics<P, T, F, M, S>>
            AbstractGraphicalPN<P, T, F, M, S, N, G>
            parse(String fileName, PNParsingFormat format) throws IOException, ParserException {
        Validate.notNull(fileName);
        return PNParsing.<P, T, F, M, S, N, G>parse(prepareFile(fileName), format);
    }

    private static File prepareFile(String fileName) throws IOException {
        File file = new File(fileName);
        validateFile(file);
        return file;
    }

    private static void validateFile(File file) throws IOException {
        if (!file.exists()) {
            throw new IOException("I/O Error on opening file: File does not exist!");
        }
        if (file.isDirectory()) {
            throw new IOException("I/O Error on opening file: File is a directory!");
        }
        if (!file.canRead()) {
            throw new IOException("I/O Error on opening file: Unable to read file!");
        }
    }

    /**
     * @param file File to parse
     * @param format Format to parse from
     * @return Returns the for the file's extension suitable parser or
     * <code>null</code> if no suitable parser could be found
     * @throws ParserException If the file can't be found
     */
    @SuppressWarnings("rawtypes")
    public static synchronized PNParserInterface getParser(File file, PNParsingFormat format) throws ParserException {
        switch (format) {
            case PNML:
                return new PNMLParser();
            case PETRIFY:
                return new PetrifyParser();
        }
        throw new ParserException(ErrorCode.UNSUPPORTED_FORMAT);
    }

    public static PNParsingFormat guessFormat(File file) {
        return guessFormat(file.getName());
    }

    public static PNParsingFormat guessFormat(String file) {
        for (PNParsingFormat format : PNParsingFormat.values()) {
            if (file.endsWith(format.getFileFormat().getFileExtension().toUpperCase())) {
                return format;
            } else if (file.endsWith(format.getFileFormat().getFileExtension().toLowerCase())) {
                return format;
            }
        }
        return null;
    }

    /**
     * Sanitizes element names by the XML ID datatype standard. Names must start
     * with a character of the range [a-zA-Z] and must only contain alphanumeric
     * characters and the symbols <code>-_.:</code>.
     *
     * @param name Name to sanitize.
     * @param leadingCharacters String to prepend to the name if it has not a
     * valid beginning.
     * @return Sanitized element name.
     */
    public static String sanitizeElementName(String name, String leadingCharacters) {
        Validate.notEmpty(leadingCharacters);

        // replace forbidden characters by "_"
        name = name.replaceAll(AbstractPetriNet.XML_ID_FORBIDDEN_CHARACTERS.pattern(), SANITIZE_INVALID_CHARACTER_REPLACEMENT);
        // check if first element is in range [a-zA-Z]
        if (name.length() == 0 || !name.substring(0, 1).matches("^[a-zA-Z]$")) {
            name = leadingCharacters + name;
        }
        return name;
    }
}
