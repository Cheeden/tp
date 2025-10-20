package tutortrack.logic.parser;

import static tutortrack.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tutortrack.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static tutortrack.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static tutortrack.logic.commands.CommandTestUtil.COST_DESC_AMY;
import static tutortrack.logic.commands.CommandTestUtil.COST_DESC_BOB;
import static tutortrack.logic.commands.CommandTestUtil.DAYTIME_DESC_AMY;
import static tutortrack.logic.commands.CommandTestUtil.DAYTIME_DESC_BOB;
import static tutortrack.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static tutortrack.logic.commands.CommandTestUtil.INVALID_COST_DESC;
import static tutortrack.logic.commands.CommandTestUtil.INVALID_DAYTIME_DESC;
import static tutortrack.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static tutortrack.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static tutortrack.logic.commands.CommandTestUtil.INVALID_SUBJECTLEVEL_DESC;
import static tutortrack.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static tutortrack.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static tutortrack.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static tutortrack.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static tutortrack.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static tutortrack.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static tutortrack.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static tutortrack.logic.commands.CommandTestUtil.SUBJECTLEVEL_DESC_AMY;
import static tutortrack.logic.commands.CommandTestUtil.SUBJECTLEVEL_DESC_BOB;
import static tutortrack.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static tutortrack.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static tutortrack.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static tutortrack.logic.commands.CommandTestUtil.VALID_COST_BOB;
import static tutortrack.logic.commands.CommandTestUtil.VALID_DAYTIME_BOB;
import static tutortrack.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static tutortrack.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static tutortrack.logic.commands.CommandTestUtil.VALID_SUBJECTLEVEL_BOB;
import static tutortrack.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static tutortrack.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static tutortrack.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static tutortrack.logic.parser.CliSyntax.PREFIX_NAME;
import static tutortrack.logic.parser.CliSyntax.PREFIX_PHONE;
import static tutortrack.logic.parser.CommandParserTestUtil.assertParseFailure;
import static tutortrack.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static tutortrack.testutil.TypicalPersons.AMY;
import static tutortrack.testutil.TypicalPersons.BOB;

import org.junit.jupiter.api.Test;

import tutortrack.logic.Messages;
import tutortrack.logic.commands.AddCommand;
import tutortrack.model.person.Address;
import tutortrack.model.person.Cost;
import tutortrack.model.person.DayTime;
import tutortrack.model.person.Name;
import tutortrack.model.person.Person;
import tutortrack.model.person.Phone;
import tutortrack.model.person.SubjectLevel;
import tutortrack.model.tag.Tag;
import tutortrack.testutil.PersonBuilder;

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Person expectedPerson = new PersonBuilder(BOB).withTags(VALID_TAG_FRIEND).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_BOB + PHONE_DESC_BOB
                + SUBJECTLEVEL_DESC_BOB + DAYTIME_DESC_BOB + COST_DESC_BOB + ADDRESS_DESC_BOB + TAG_DESC_FRIEND,
                new AddCommand(expectedPerson));


        // multiple tags - all accepted
        Person expectedPersonMultipleTags = new PersonBuilder(BOB).withTags(VALID_TAG_FRIEND, VALID_TAG_HUSBAND)
                .build();
        assertParseSuccess(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB
                        + SUBJECTLEVEL_DESC_BOB + DAYTIME_DESC_BOB + COST_DESC_BOB
                        + ADDRESS_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                new AddCommand(expectedPersonMultipleTags));
    }

    @Test
    public void parse_repeatedNonTagValue_failure() {
        String validExpectedPersonString = NAME_DESC_BOB + PHONE_DESC_BOB
                + SUBJECTLEVEL_DESC_BOB + DAYTIME_DESC_BOB + COST_DESC_BOB
                + ADDRESS_DESC_BOB + TAG_DESC_FRIEND;

        // multiple names
        assertParseFailure(parser, NAME_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // multiple phones
        assertParseFailure(parser, PHONE_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // multiple addresses
        assertParseFailure(parser, ADDRESS_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));

        // multiple fields repeated
        assertParseFailure(parser,
                PHONE_DESC_AMY + NAME_DESC_AMY + ADDRESS_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME, PREFIX_ADDRESS, PREFIX_PHONE));

        // invalid value followed by valid value

        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid phone
        assertParseFailure(parser, INVALID_PHONE_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid address
        assertParseFailure(parser, INVALID_ADDRESS_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));

        // valid value followed by invalid value

        // invalid name
        assertParseFailure(parser, validExpectedPersonString + INVALID_NAME_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid phone
        assertParseFailure(parser, validExpectedPersonString + INVALID_PHONE_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid address
        assertParseFailure(parser, validExpectedPersonString + INVALID_ADDRESS_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Person expectedPerson = new PersonBuilder(AMY).withTags().build();
        assertParseSuccess(parser, NAME_DESC_AMY + PHONE_DESC_AMY
                + SUBJECTLEVEL_DESC_AMY + DAYTIME_DESC_AMY + COST_DESC_AMY + ADDRESS_DESC_AMY,
                new AddCommand(expectedPerson));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_BOB + PHONE_DESC_BOB
                        + SUBJECTLEVEL_DESC_BOB + DAYTIME_DESC_BOB + COST_DESC_BOB + ADDRESS_DESC_BOB,
                expectedMessage);

        // missing phone prefix
        assertParseFailure(parser, NAME_DESC_BOB + VALID_PHONE_BOB
                        + SUBJECTLEVEL_DESC_BOB + DAYTIME_DESC_BOB + COST_DESC_BOB + ADDRESS_DESC_BOB,
                expectedMessage);

        // missing subject level prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB
                        + VALID_SUBJECTLEVEL_BOB + DAYTIME_DESC_BOB + COST_DESC_BOB + ADDRESS_DESC_BOB,
                expectedMessage);

        // missing daytime prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB
                        + SUBJECTLEVEL_DESC_BOB + VALID_DAYTIME_BOB + COST_DESC_BOB + ADDRESS_DESC_BOB,
                expectedMessage);

        // missing cost prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB
                        + SUBJECTLEVEL_DESC_BOB + DAYTIME_DESC_BOB + VALID_COST_BOB + ADDRESS_DESC_BOB,
                expectedMessage);

        // missing address prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB
                        + SUBJECTLEVEL_DESC_BOB + DAYTIME_DESC_BOB + COST_DESC_BOB + VALID_ADDRESS_BOB,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_BOB + VALID_PHONE_BOB
                        + VALID_SUBJECTLEVEL_BOB + VALID_DAYTIME_BOB + VALID_COST_BOB + VALID_ADDRESS_BOB,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB
                + SUBJECTLEVEL_DESC_BOB + DAYTIME_DESC_BOB + COST_DESC_BOB + ADDRESS_DESC_BOB
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Name.MESSAGE_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser, NAME_DESC_BOB + INVALID_PHONE_DESC
                + SUBJECTLEVEL_DESC_BOB + DAYTIME_DESC_BOB + COST_DESC_BOB + ADDRESS_DESC_BOB
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Phone.MESSAGE_CONSTRAINTS);

        // invalid address
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB
                + SUBJECTLEVEL_DESC_BOB + DAYTIME_DESC_BOB + COST_DESC_BOB + INVALID_ADDRESS_DESC
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Address.MESSAGE_CONSTRAINTS);

        // invalid subject level
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB
                + INVALID_SUBJECTLEVEL_DESC + DAYTIME_DESC_BOB + COST_DESC_BOB + ADDRESS_DESC_BOB
                + TAG_DESC_HUSBAND, SubjectLevel.MESSAGE_CONSTRAINTS);

        // invalid day/time (e.g. invalid format or invalid hour)
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB
                + SUBJECTLEVEL_DESC_BOB + INVALID_DAYTIME_DESC + COST_DESC_BOB + ADDRESS_DESC_BOB,
                DayTime.MESSAGE_CONSTRAINTS);

        // invalid cost (missing $ sign)
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB
                + SUBJECTLEVEL_DESC_BOB + DAYTIME_DESC_BOB + INVALID_COST_DESC + ADDRESS_DESC_BOB,
                Cost.MESSAGE_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB
                + SUBJECTLEVEL_DESC_BOB + DAYTIME_DESC_BOB + COST_DESC_BOB + ADDRESS_DESC_BOB
                + INVALID_TAG_DESC + VALID_TAG_FRIEND, Tag.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB
                        + SUBJECTLEVEL_DESC_BOB + DAYTIME_DESC_BOB + COST_DESC_BOB + INVALID_ADDRESS_DESC,
                Name.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_BOB + PHONE_DESC_BOB
                        + SUBJECTLEVEL_DESC_BOB + DAYTIME_DESC_BOB + COST_DESC_BOB
                        + ADDRESS_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
    }
}
