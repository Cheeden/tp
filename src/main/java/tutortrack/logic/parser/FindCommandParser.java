package tutortrack.logic.parser;

import static tutortrack.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tutortrack.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Arrays;
import java.util.List;

import tutortrack.logic.commands.FindCommand;
import tutortrack.logic.parser.exceptions.ParseException;
import tutortrack.model.person.NameContainsKeywordsPredicate;
import tutortrack.model.person.TagContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        // Check if the input contains the tag prefix
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_TAG);

        // If tag prefix is present and has values, search by tags
        if (argMultimap.getValue(PREFIX_TAG).isPresent()) {
            String tagKeywordsString = argMultimap.getValue(PREFIX_TAG).get();
            if (tagKeywordsString.isEmpty()) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
            }
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
