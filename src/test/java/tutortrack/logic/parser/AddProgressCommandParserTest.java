package tutortrack.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tutortrack.testutil.Assert.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import tutortrack.commons.core.index.Index;
import tutortrack.logic.commands.AddProgressCommand;
import tutortrack.logic.parser.exceptions.ParseException;
import tutortrack.model.lesson.LessonProgress;

public class AddProgressCommandParserTest {

    private final AddProgressCommandParser parser = new AddProgressCommandParser();

    @Test
    public void parse_allFieldsPresent_success() throws Exception {
        String userInput = "2 pr/2025-11-12|Completed algebra worksheet";

        LessonProgress progress = new LessonProgress(
                LocalDate.of(2025, 11, 12),
                "Completed algebra worksheet"
        );

        AddProgressCommand expectedCommand = new AddProgressCommand(Index.fromOneBased(2), progress);

        AddProgressCommand result = parser.parse(userInput);

        assertEquals(expectedCommand, result);
    }

    @Test
    public void parse_missingProgressPrefix_failure() {
        String userInput = "1 2025-11-12|Completed algebra worksheet";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_missingIndex_failure() {
        String userInput = "pr/2025-11-12|Completed algebra worksheet";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_invalidIndex_failure() {
        String userInput = "x pr/2025-11-12|Completed algebra worksheet";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_emptyProgress_failure() {
        String userInput = "1 pr/";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_invalidDate_failure() {
        String userInput = "1 pr/invalid-date|Completed algebra worksheet";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }
}
