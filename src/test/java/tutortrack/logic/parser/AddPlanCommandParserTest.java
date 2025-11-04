package tutortrack.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import tutortrack.commons.core.index.Index;
import tutortrack.logic.commands.AddPlanCommand;
import tutortrack.logic.parser.exceptions.ParseException;
import tutortrack.model.lesson.LessonPlan;

public class AddPlanCommandParserTest {

    private final AddPlanCommandParser parser = new AddPlanCommandParser();

    @Test
    public void parse_allFieldsPresent_success() throws Exception {
        String userInput = "1 pl/2025-11-10|Topic 1: Linear equations";

        LessonPlan lessonPlan = new LessonPlan(
                LocalDate.of(2025, 11, 10),
                "Topic 1: Linear equations"
        );

        AddPlanCommand expectedCommand = new AddPlanCommand(Index.fromOneBased(1), lessonPlan);

        AddPlanCommand result = parser.parse(userInput);

        assertEquals(expectedCommand, result);
    }

    @Test
    public void parse_missingLessonPlanPrefix_failure() {
        // Missing "pl/" prefix
        String userInput = "1 2025-11-10|Topic 1: Linear equations";

        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_missingIndex_failure() {
        // Missing preamble index
        String userInput = "pl/2025-11-10|Topic 1: Linear equations";

        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_invalidIndex_failure() {
        // Non-numeric index
        String userInput = "a pl/2025-11-10|Topic 1: Linear equations";

        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_emptyLessonPlan_failure() {
        // Prefix present but no content
        String userInput = "1 pl/";

        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }
}
