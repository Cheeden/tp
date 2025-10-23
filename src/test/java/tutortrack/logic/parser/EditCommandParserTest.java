package tutortrack.logic.parser;

import static tutortrack.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tutortrack.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static tutortrack.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static tutortrack.logic.commands.CommandTestUtil.COST_DESC_AMY;
import static tutortrack.logic.commands.CommandTestUtil.DAYTIME_DESC_AMY;
import static tutortrack.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static tutortrack.logic.commands.CommandTestUtil.INVALID_COST_DESC;
import static tutortrack.logic.commands.CommandTestUtil.INVALID_DAYTIME_DESC;
import static tutortrack.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static tutortrack.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static tutortrack.logic.commands.CommandTestUtil.INVALID_SUBJECTLEVEL_DESC;
import static tutortrack.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static tutortrack.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static tutortrack.logic.commands.CommandTestUtil.CONTACT_DESC_AMY;
import static tutortrack.logic.commands.CommandTestUtil.CONTACT_DESC_BOB;
import static tutortrack.logic.commands.CommandTestUtil.NOK_CONTACT_DESC_AMY;
import static tutortrack.logic.commands.CommandTestUtil.NOK_CONTACT_DESC_BOB;
import static tutortrack.logic.commands.CommandTestUtil.SUBJECTLEVEL_DESC_AMY;
import static tutortrack.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static tutortrack.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static tutortrack.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static tutortrack.logic.commands.CommandTestUtil.VALID_COST_AMY;
import static tutortrack.logic.commands.CommandTestUtil.VALID_DAYTIME_AMY;
import static tutortrack.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static tutortrack.logic.commands.CommandTestUtil.VALID_CONTACT_AMY;
import static tutortrack.logic.commands.CommandTestUtil.VALID_CONTACT_BOB;
import static tutortrack.logic.commands.CommandTestUtil.VALID_NOK_CONTACT_AMY;
import static tutortrack.logic.commands.CommandTestUtil.VALID_NOK_CONTACT_BOB;
import static tutortrack.logic.commands.CommandTestUtil.VALID_SUBJECTLEVEL_AMY;
import static tutortrack.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static tutortrack.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static tutortrack.logic.parser.CliSyntax.*;
import static tutortrack.logic.parser.CommandParserTestUtil.assertParseFailure;
import static tutortrack.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static tutortrack.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static tutortrack.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static tutortrack.testutil.TypicalIndexes.INDEX_THIRD_PERSON;

import org.junit.jupiter.api.Test;

import tutortrack.commons.core.index.Index;
import tutortrack.logic.Messages;
import tutortrack.logic.commands.EditCommand;
import tutortrack.logic.commands.EditCommand.EditPersonDescriptor;
import tutortrack.model.person.Address;
import tutortrack.model.person.Cost;
import tutortrack.model.person.DayTime;
import tutortrack.model.person.Name;
import tutortrack.model.person.Phone;
import tutortrack.model.person.SubjectLevel;
import tutortrack.model.tag.Tag;
import tutortrack.testutil.EditPersonDescriptorBuilder;

public class EditCommandParserTest {

    private static final String TAG_EMPTY = " " + PREFIX_TAG;

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE);

    private EditCommandParser parser = new EditCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_NAME_AMY, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", EditCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_NAME_DESC, Name.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + INVALID_PHONE_DESC, Phone.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + INVALID_ADDRESS_DESC, Address.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + INVALID_TAG_DESC, Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + INVALID_SUBJECTLEVEL_DESC, SubjectLevel.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + INVALID_DAYTIME_DESC, DayTime.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + INVALID_COST_DESC, Cost.MESSAGE_CONSTRAINTS);

        // multiple invalid values, only first one captured
        assertParseFailure(parser, "1" + INVALID_NAME_DESC + VALID_ADDRESS_AMY + VALID_CONTACT_AMY,
                Name.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_PERSON;
        String userInput = targetIndex.getOneBased()
                                   + CONTACT_DESC_BOB + NOK_CONTACT_DESC_BOB
                                   + TAG_DESC_HUSBAND + ADDRESS_DESC_AMY + NAME_DESC_AMY
                                   + SUBJECTLEVEL_DESC_AMY + DAYTIME_DESC_AMY + COST_DESC_AMY
                                   + TAG_DESC_FRIEND;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                                                  .withName(VALID_NAME_AMY)
                                                  .withSelfContact(VALID_CONTACT_BOB)
                                                  .withNokContact(VALID_NOK_CONTACT_BOB)
                                                  .withAddress(VALID_ADDRESS_AMY)
                                                  .withSubjectLevel(VALID_SUBJECTLEVEL_AMY)
                                                  .withDayTime(VALID_DAYTIME_AMY)
                                                  .withCost(VALID_COST_AMY)
                                                  .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND)
                                                  .build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + CONTACT_DESC_BOB + NOK_CONTACT_DESC_BOB;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                                                  .withSelfContact(VALID_CONTACT_BOB)
                                                  .withNokContact(VALID_NOK_CONTACT_BOB)
                                                  .build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        Index targetIndex = INDEX_THIRD_PERSON;

        // name
        String userInput = targetIndex.getOneBased() + NAME_DESC_AMY;
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // contact
        userInput = targetIndex.getOneBased() + CONTACT_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withSelfContact(VALID_CONTACT_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // nok contact
        userInput = targetIndex.getOneBased() + NOK_CONTACT_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withNokContact(VALID_NOK_CONTACT_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // subject level
        userInput = targetIndex.getOneBased() + SUBJECTLEVEL_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withSubjectLevel(VALID_SUBJECTLEVEL_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // day time
        userInput = targetIndex.getOneBased() + DAYTIME_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withDayTime(VALID_DAYTIME_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // cost
        userInput = targetIndex.getOneBased() + COST_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withCost(VALID_COST_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // address
        userInput = targetIndex.getOneBased() + ADDRESS_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withAddress(VALID_ADDRESS_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // tags
        userInput = targetIndex.getOneBased() + TAG_DESC_FRIEND;
        descriptor = new EditPersonDescriptorBuilder().withTags(VALID_TAG_FRIEND).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_failure() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased()
                                   + CONTACT_DESC_AMY + NOK_CONTACT_DESC_AMY + ADDRESS_DESC_AMY
                                   + CONTACT_DESC_AMY + NOK_CONTACT_DESC_AMY + ADDRESS_DESC_AMY
                                   + CONTACT_DESC_BOB + NOK_CONTACT_DESC_BOB + ADDRESS_DESC_BOB;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_SELF_CONTACT, PREFIX_NOK_CONTACT, PREFIX_ADDRESS));
    }

    @Test
    public void parse_resetTags_success() {
        Index targetIndex = INDEX_THIRD_PERSON;
        String userInput = targetIndex.getOneBased() + TAG_EMPTY;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withTags().build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
