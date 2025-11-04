package tutortrack.logic.parser;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import tutortrack.logic.parser.exceptions.ParseException;

public class EditProgressCommandParserTest {

    private final EditProgressCommandParser parser = new EditProgressCommandParser();

    @Test
    public void parse_missingLessonPlanPrefix_failure() {
        // Missing "pr/" prefix
        String userInput = "1 2025-11-10|Topic 1: Linear equations";

        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_missingIndex_failure() {
        // Missing preamble index
        String userInput = "pr/2025-11-10|Topic 1: Linear equations";

        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_invalidIndex_failure() {
        // Non-numeric index
        String userInput = "a pr/2025-11-10|Topic 1: Linear equations";

        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_emptyLessonPlan_failure() {
        // Prefix present but no content
        String userInput = "1 pr/";

        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }
}
