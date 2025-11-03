package tutortrack.logic.parser;

import static tutortrack.logic.Messages.MESSAGE_FIND_DUPLICATE_DAY;
import static tutortrack.logic.Messages.MESSAGE_FIND_DUPLICATE_SUBJECT;
import static tutortrack.logic.Messages.MESSAGE_FIND_DUPLICATE_TAG;
import static tutortrack.logic.Messages.MESSAGE_FIND_MULTIPLE_PREFIXES;
import static tutortrack.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tutortrack.logic.parser.CliSyntax.PREFIX_DAYTIME;
import static tutortrack.logic.parser.CliSyntax.PREFIX_SUBJECTLEVEL;
import static tutortrack.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Stream;

import tutortrack.commons.core.LogsCenter;
import tutortrack.logic.commands.FindCommand;
import tutortrack.logic.parser.exceptions.ParseException;
import tutortrack.model.person.LessonDayPredicate;
import tutortrack.model.person.NameContainsKeywordsPredicate;
import tutortrack.model.person.SubjectLevel;
import tutortrack.model.person.SubjectLevelMatchesPredicate;
import tutortrack.model.person.TagContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object.
 */
public class FindCommandParser implements Parser<FindCommand> {

    private static final Logger logger = LogsCenter.getLogger(FindCommandParser.class);

    /** Map of prefixes to their duplicate error messages for find command validation */
    private static final Map<Prefix, String> DUPLICATE_PREFIX_ERRORS = Map.of(
        PREFIX_TAG, MESSAGE_FIND_DUPLICATE_TAG,
        PREFIX_DAYTIME, MESSAGE_FIND_DUPLICATE_DAY,
        PREFIX_SUBJECTLEVEL, MESSAGE_FIND_DUPLICATE_SUBJECT
    );

    /**
     * Extracts the value associated with a prefix from the argument multimap.
     * @throws ParseException if the value is empty or contains only whitespace
     */
    private String extractNonEmptyValue(ArgumentMultimap argMultimap, Prefix prefix) throws ParseException {
        String value = argMultimap.getValue(prefix).get().trim();
        if (value.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }
        return value;
    }

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        String trimmedArgs = validateAndTrimArgs(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args,
                PREFIX_TAG, PREFIX_DAYTIME, PREFIX_SUBJECTLEVEL);
        validatePrefixUsage(argMultimap);
        return createFindCommand(argMultimap, trimmedArgs);
    }

    /**
     * Validates and trims the input arguments.
     * @throws ParseException if arguments are null or empty
     */
    private String validateAndTrimArgs(String args) throws ParseException {
        assert args != null : "Arguments cannot be null";
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            logger.warning("Find command received empty arguments");
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }
        return trimmedArgs;
    }

    /**
     * Validates prefix usage to ensure no multiple or duplicate prefixes, and no invalid preamble.
     * @throws ParseException if validation fails
     */
    private void validatePrefixUsage(ArgumentMultimap argMultimap) throws ParseException {
        // Check for multiple different prefixes
        long prefixCount = Stream.of(PREFIX_TAG, PREFIX_DAYTIME, PREFIX_SUBJECTLEVEL)
                .filter(prefix -> argMultimap.getValue(prefix).isPresent())
                .count();

        if (prefixCount > 1) {
            throw new ParseException(MESSAGE_FIND_MULTIPLE_PREFIXES);
        }

        // Check for duplicate usage of the same prefix
        for (Map.Entry<Prefix, String> entry : DUPLICATE_PREFIX_ERRORS.entrySet()) {
            if (argMultimap.getAllValues(entry.getKey()).size() > 1) {
                throw new ParseException(entry.getValue());
            }
        }

        // Validate preamble when prefix is present
        if (prefixCount > 0 && !argMultimap.getPreamble().isEmpty()) {
            if (argMultimap.getValue(PREFIX_SUBJECTLEVEL).isPresent()) {
                throw new ParseException("Invalid subject search format. The subject-level must be a single token "
                    + "in the form Level-Subject (no spaces). Example: find s/P4-Math");
            }
            if (argMultimap.getValue(PREFIX_DAYTIME).isPresent()) {
                throw new ParseException("Invalid day search format. Use 'find d/Monday' "
                    + "(use full weekday names and no abbreviations).");
            }
            throw new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }
    }

    /**
     * Creates the appropriate FindCommand based on the prefixes and arguments provided.
     */
    private FindCommand createFindCommand(ArgumentMultimap argMultimap, String trimmedArgs)
            throws ParseException {
        // Day/time search
        if (argMultimap.getValue(PREFIX_DAYTIME).isPresent()) {
            String dayKeyword = extractNonEmptyValue(argMultimap, PREFIX_DAYTIME);
            String validatedDay = ParserUtil.parseDay(dayKeyword);
            LessonDayPredicate predicate = new LessonDayPredicate(validatedDay);
            return new FindCommand(predicate, predicate.getComparator());
        }

        // Subject-level search
        if (argMultimap.getValue(PREFIX_SUBJECTLEVEL).isPresent()) {
            String subject = extractNonEmptyValue(argMultimap, PREFIX_SUBJECTLEVEL);
            SubjectLevel parsedSubject = ParserUtil.parseSubjectLevel(subject);
            SubjectLevelMatchesPredicate predicate = new SubjectLevelMatchesPredicate(parsedSubject.toString());
            return new FindCommand(predicate);
        }

        // Tag search
        if (argMultimap.getValue(PREFIX_TAG).isPresent()) {
            String tagKeywordsString = extractNonEmptyValue(argMultimap, PREFIX_TAG);
            List<String> tagKeywords = Arrays.asList(tagKeywordsString.split("\\s+"));
            return new FindCommand(new TagContainsKeywordsPredicate(tagKeywords));
        }

        // Name search (default)
        String[] nameKeywords = trimmedArgs.split("\\s+");
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(Arrays.asList(nameKeywords));
        return new FindCommand(predicate, predicate.getComparator());
    }

}
