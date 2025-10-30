package tutortrack.logic.parser;

import static tutortrack.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tutortrack.logic.parser.CliSyntax.PREFIX_DAYTIME;
import static tutortrack.logic.parser.CliSyntax.PREFIX_SUBJECTLEVEL;
import static tutortrack.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

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
        assert args != null : "Arguments cannot be null";

        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            logger.warning("Find command received empty arguments");
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        // Tokenize arguments including tag, day/time and subject-level prefixes
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args,
                PREFIX_TAG, PREFIX_DAYTIME, PREFIX_SUBJECTLEVEL);

        // Check if any prefix is present
        boolean hasPrefixes = argMultimap.getValue(PREFIX_TAG).isPresent()
                || argMultimap.getValue(PREFIX_DAYTIME).isPresent()
                || argMultimap.getValue(PREFIX_SUBJECTLEVEL).isPresent();

        // If a prefix is present, preamble should be empty. Provide more specific
        // feedback depending on which prefix was used to help the user correct common mistakes
        if (hasPrefixes && !argMultimap.getPreamble().isEmpty()) {
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

        // If day/time prefix is present, search by day and sort by time
        if (argMultimap.getValue(PREFIX_DAYTIME).isPresent()) {
            String dayKeyword = extractNonEmptyValue(argMultimap, PREFIX_DAYTIME);
            String validatedDay = ParserUtil.parseDay(dayKeyword);
            LessonDayPredicate predicate = new LessonDayPredicate(validatedDay);
            return new FindCommand(predicate, predicate.getComparator());
        }

        // If subject-level prefix is present, search by subject
        if (argMultimap.getValue(PREFIX_SUBJECTLEVEL).isPresent()) {
            String subject = extractNonEmptyValue(argMultimap, PREFIX_SUBJECTLEVEL);
            // validate subject-level format and give user a clear error if invalid
            SubjectLevel parsedSubject = ParserUtil.parseSubjectLevel(subject);
            SubjectLevelMatchesPredicate predicate = new SubjectLevelMatchesPredicate(parsedSubject.toString());
            return new FindCommand(predicate);
        }

        // If tag prefix is present and has values, search by tags
        if (argMultimap.getValue(PREFIX_TAG).isPresent()) {
            String tagKeywordsString = extractNonEmptyValue(argMultimap, PREFIX_TAG);
            List<String> tagKeywords = Arrays.asList(tagKeywordsString.split("\\s+"));
            return new FindCommand(new TagContainsKeywordsPredicate(tagKeywords));
        }

        // Otherwise, search by name with ranking (default behavior)
        String[] nameKeywords = trimmedArgs.split("\\s+");
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(Arrays.asList(nameKeywords));

        // Use the predicate's comparator for ranked results
        return new FindCommand(predicate, predicate.getComparator());
    }

}
