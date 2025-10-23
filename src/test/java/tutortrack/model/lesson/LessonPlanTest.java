package tutortrack.model.lesson;

import static tutortrack.testutil.Assert.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class LessonPlanTest {
    @Test
    void constructor_nullDate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new LessonPlan(null, "Discuss functions"));
    }

    @Test
    void constructor_nullPlan_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new LessonPlan(LocalDate.now(), null));
    }
}
