package uk.pizey;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Optional.empty;

public class MavenReleaseVersionMatcher {
    private static final int DIGITS_INDEX = 1;

    private static final int ANNOTATION_SEPARATOR_INDEX = 2;

    private static final int ANNOTATION_INDEX = 3;

    private static final int ANNOTATION_REV_SEPARATOR_INDEX = 4;

    private static final int ANNOTATION_REVISION_INDEX = 5;

    private static final int BUILD_SEPARATOR_INDEX = 6;

    private static final int BUILD_SPECIFIER_INDEX = 7;

    private static final String SNAPSHOT_IDENTIFIER = "SNAPSHOT";

    private static final String DIGIT_SEPARATOR_STRING = ".";

    private static final String DEFAULT_ANNOTATION_REV_SEPARATOR = "-";

    private static final String DEFAULT_BUILD_SEPARATOR = "-";

    public static final Pattern STANDARD_PATTERN = Pattern.compile(
            "^((?:\\d+\\.)*\\d+)" // digit(s) and '.' repeated -
                    // followed by digit (version
                    // digits 1.22.0, etc)
                    + "([-_])?" // optional - or _ (annotation separator)
                    + "([a-zA-Z]*)" // alpha characters (looking for annotation - alpha, beta, RC, etc.)
                    + "([-_])?" // optional - or _ (annotation revision separator)
                    + "(\\d*)" // digits (any digits after rc or beta is an annotation revision)
                    + "(?:([-_])?(.*?))?$"); // - or _ followed everything else (build specifier)
    String annotationRevision;
    String annotationRevSeparator;
    String buildSeparator;
    String buildSpecifier;
    String annotationRevSeparato;
    String annotationSeparator = null;
    String annotation = null;

    List<String> digits;
    public MavenReleaseVersionMatcher(String version) {
        Matcher m = STANDARD_PATTERN.matcher(version);
        if (m.matches()) {
            digits = Arrays.asList(m.group(1).split("\\."));
            if (!SNAPSHOT_IDENTIFIER.equals(m.group(ANNOTATION_INDEX))) {
                annotationSeparator = m.group(ANNOTATION_SEPARATOR_INDEX);
                annotation = nullIfEmpty(m.group(ANNOTATION_INDEX));

                if (!empty(m.group(ANNOTATION_REV_SEPARATOR_INDEX))
                        && empty(m.group(ANNOTATION_REVISION_INDEX))) {
                    // The build separator was picked up as the annotation revision separator
                    buildSeparator = m.group(ANNOTATION_REV_SEPARATOR_INDEX);
                    buildSpecifier = nullIfEmpty(m.group(BUILD_SPECIFIER_INDEX));
                } else {
                    annotationRevSeparator = m.group(ANNOTATION_REV_SEPARATOR_INDEX);
                    annotationRevision = nullIfEmpty(m.group(ANNOTATION_REVISION_INDEX));

                    buildSeparator = m.group(BUILD_SEPARATOR_INDEX);
                    buildSpecifier = nullIfEmpty(m.group(BUILD_SPECIFIER_INDEX));
                }
            } else {
                // Annotation was "SNAPSHOT" so populate the build specifier with that data
                buildSeparator = m.group(ANNOTATION_SEPARATOR_INDEX);
                buildSpecifier = nullIfEmpty(m.group(ANNOTATION_INDEX));
            }
        } else {
            throw new RuntimeException("Unable to parse the version string: \"" + version + "\"");
        }
    }

    private boolean empty(String s) {
        return s != null && s.isEmpty();
    }

    private static String nullIfEmpty(String s) {
        return (s == null || s.isEmpty()) ? null : s;
    }

}
