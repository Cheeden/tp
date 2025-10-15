package tutortrack.logic.parser;

import static tutortrack.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tutortrack.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static tutortrack.logic.parser.CliSyntax.PREFIX_COST;
import static tutortrack.logic.parser.CliSyntax.PREFIX_DAYTIME;
import static tutortrack.logic.parser.CliSyntax.PREFIX_NAME;
import static tutortrack.logic.parser.CliSyntax.PREFIX_PHONE;
import static tutortrack.logic.parser.CliSyntax.PREFIX_SUBJECTLEVEL;
import static tutortrack.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;
import java.util.stream.Stream;

import tutortrack.logic.commands.AddCommand;
import tutortrack.logic.parser.exceptions.ParseException;
import tutortrack.model.person.Address;
import tutortrack.model.person.Name;
import tutortrack.model.person.Person;
import tutortrack.model.person.Phone;
import tutortrack.model.tag.Tag;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser implements Parser<AddCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_SUBJECTLEVEL,
                        PREFIX_DAYTIME, PREFIX_COST, PREFIX_ADDRESS, PREFIX_TAG);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_ADDRESS, PREFIX_PHONE,
                PREFIX_SUBJECTLEVEL, PREFIX_DAYTIME, PREFIX_COST)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_SUBJECTLEVEL,
                PREFIX_DAYTIME, PREFIX_COST, PREFIX_ADDRESS);
        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
        String subjectLevel = argMultimap.getValue(PREFIX_SUBJECTLEVEL).get();
        String dayTime = argMultimap.getValue(PREFIX_DAYTIME).get();
        String cost = argMultimap.getValue(PREFIX_COST).get();
        Address address = ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get());
        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        Person person = new Person(name, phone,
                subjectLevel, dayTime, cost, address, tagList);

        return new AddCommand(person);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
