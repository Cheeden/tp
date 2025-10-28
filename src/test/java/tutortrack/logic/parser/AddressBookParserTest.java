package tutortrack.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tutortrack.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tutortrack.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static tutortrack.testutil.Assert.assertThrows;
import static tutortrack.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import tutortrack.logic.commands.AddCommand;
import tutortrack.logic.commands.ClearCommand;
import tutortrack.logic.commands.DeleteCommand;
import tutortrack.logic.commands.DeletePlanCommand;
import tutortrack.logic.commands.EditCommand;
import tutortrack.logic.commands.ExitCommand;
import tutortrack.logic.commands.FindCommand;
import tutortrack.logic.commands.HelpCommand;
import tutortrack.logic.commands.ListCommand;
import tutortrack.logic.commands.ViewLessonProgressCommand;
import tutortrack.logic.parser.exceptions.ParseException;
import tutortrack.model.person.NameContainsKeywordsPredicate;
import tutortrack.model.person.Person;
import tutortrack.testutil.EditPersonDescriptorBuilder;
import tutortrack.testutil.PersonBuilder;
import tutortrack.testutil.PersonUtil;

public class AddressBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_add() throws Exception {
        Person person = new PersonBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(PersonUtil.getAddCommand(person));
        assertEquals(new AddCommand(person), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Person person = new PersonBuilder().build();
        EditCommand.EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
        EditCommand command = (EditCommand)
            parser.parseCommand(EditCommand.COMMAND_WORD + " "
            + INDEX_FIRST_PERSON.getOneBased() + " "
            + PersonUtil.getEditPersonDescriptorDetails(descriptor));
        // assertEquals(new EditCommand(INDEX_FIRST_PERSON, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(keywords);
        assertEquals(new FindCommand(predicate, predicate.getComparator()), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_viewLessonProgress() throws Exception {
        ViewLessonProgressCommand command = (ViewLessonProgressCommand) parser.parseCommand(
                ViewLessonProgressCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new ViewLessonProgressCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_deletePlan() throws Exception {
        DeletePlanCommand command = (DeletePlanCommand) parser.parseCommand(
                DeletePlanCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased() + " 2025-10-15");
        assertEquals(new DeletePlanCommand(INDEX_FIRST_PERSON, LocalDate.of(2025, 10, 15)), command);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}
