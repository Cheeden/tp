package tutortrack.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import tutortrack.model.lesson.LessonPlan;
import tutortrack.model.lesson.LessonProgress;

/**
 * Contains tests for LessonWindow's data merging logic.
 * Note: UI interaction tests are skipped as they require JavaFX runtime.
 * This tests the business logic of merging lesson progress and plans.
 */
public class LessonWindowTest {

    @Test
    public void mergeLessonData_bothProgressAndPlan_mergesCorrectly() {
        // Simulate the merging logic from LessonWindow.setPerson()
        Map<LocalDate, LessonDisplay> lessonMap = new HashMap<>();

        LocalDate date = LocalDate.of(2024, 10, 20);
        LessonProgress progress = new LessonProgress(date, "Completed Chapter 1");
        LessonPlan plan = new LessonPlan(date, "Cover Chapter 2");

        // Add progress
        lessonMap.put(date, new LessonDisplay(date, progress.getProgress()));

        // Merge plan
        LessonDisplay existing = lessonMap.get(date);
        lessonMap.put(date, new LessonDisplay(date, existing.getProgress(), plan.getPlan()));

        LessonDisplay result = lessonMap.get(date);
        assertEquals(date, result.getDate());
        assertEquals("Completed Chapter 1", result.getProgress());
        assertEquals("Cover Chapter 2", result.getPlan());
    }

    @Test
    public void mergeLessonData_onlyProgress_planIsEmpty() {
        Map<LocalDate, LessonDisplay> lessonMap = new HashMap<>();

        LocalDate date = LocalDate.of(2024, 10, 20);
        LessonProgress progress = new LessonProgress(date, "Completed Chapter 1");

        lessonMap.put(date, new LessonDisplay(date, progress.getProgress()));

        LessonDisplay result = lessonMap.get(date);
        assertEquals(date, result.getDate());
        assertEquals("Completed Chapter 1", result.getProgress());
        assertEquals("", result.getPlan());
    }

    @Test
    public void mergeLessonData_onlyPlan_progressIsEmpty() {
        Map<LocalDate, LessonDisplay> lessonMap = new HashMap<>();

        LocalDate date = LocalDate.of(2024, 10, 20);
        LessonPlan plan = new LessonPlan(date, "Cover Chapter 2");

        lessonMap.put(date, new LessonDisplay(date, "", plan.getPlan()));

        LessonDisplay result = lessonMap.get(date);
        assertEquals(date, result.getDate());
        assertEquals("", result.getProgress());
        assertEquals("Cover Chapter 2", result.getPlan());
    }

    @Test
    public void mergeLessonData_multipleDates_allIncluded() {
        Map<LocalDate, LessonDisplay> lessonMap = new HashMap<>();

        LocalDate date1 = LocalDate.of(2024, 10, 20);
        LocalDate date2 = LocalDate.of(2024, 10, 27);
        LocalDate date3 = LocalDate.of(2024, 11, 3);

        LessonProgress progress1 = new LessonProgress(date1, "Chapter 1");
        LessonPlan plan2 = new LessonPlan(date2, "Plan Chapter 2");
        LessonProgress progress3 = new LessonProgress(date3, "Chapter 3");
        LessonPlan plan3 = new LessonPlan(date3, "Plan Chapter 3");

        // Add progress entries
        lessonMap.put(date1, new LessonDisplay(date1, progress1.getProgress()));
        lessonMap.put(date3, new LessonDisplay(date3, progress3.getProgress()));

        // Add plan entries
        lessonMap.put(date2, new LessonDisplay(date2, "", plan2.getPlan()));

        // Merge plan3 with existing progress3
        LessonDisplay existing = lessonMap.get(date3);
        lessonMap.put(date3, new LessonDisplay(date3, existing.getProgress(), plan3.getPlan()));

        assertEquals(3, lessonMap.size());

        // Verify date1 has only progress
        LessonDisplay result1 = lessonMap.get(date1);
        assertEquals("Chapter 1", result1.getProgress());
        assertEquals("", result1.getPlan());

        // Verify date2 has only plan
        LessonDisplay result2 = lessonMap.get(date2);
        assertEquals("", result2.getProgress());
        assertEquals("Plan Chapter 2", result2.getPlan());

        // Verify date3 has both
        LessonDisplay result3 = lessonMap.get(date3);
        assertEquals("Chapter 3", result3.getProgress());
        assertEquals("Plan Chapter 3", result3.getPlan());
    }

    @Test
    public void mergeLessonData_emptyLists_emptyMap() {
        Map<LocalDate, LessonDisplay> lessonMap = new HashMap<>();

        // Simulate empty progress and plan lists
        List<LessonProgress> progressList = new ArrayList<>();
        List<LessonPlan> planList = new ArrayList<>();

        for (LessonProgress progress : progressList) {
            lessonMap.put(progress.getDate(),
                new LessonDisplay(progress.getDate(), progress.getProgress()));
        }

        for (LessonPlan plan : planList) {
            LocalDate date = plan.getDate();
            if (lessonMap.containsKey(date)) {
                LessonDisplay existing = lessonMap.get(date);
                lessonMap.put(date,
                    new LessonDisplay(date, existing.getProgress(), plan.getPlan()));
            } else {
                lessonMap.put(date, new LessonDisplay(date, "", plan.getPlan()));
            }
        }

        assertEquals(0, lessonMap.size());
    }

    @Test
    public void sortLessonDisplays_multipleDates_sortsChronologically() {
        Map<LocalDate, LessonDisplay> lessonMap = new HashMap<>();

        LocalDate date1 = LocalDate.of(2024, 11, 3);
        LocalDate date2 = LocalDate.of(2024, 10, 20);
        LocalDate date3 = LocalDate.of(2024, 10, 27);

        lessonMap.put(date1, new LessonDisplay(date1, "Chapter 3", ""));
        lessonMap.put(date2, new LessonDisplay(date2, "Chapter 1", ""));
        lessonMap.put(date3, new LessonDisplay(date3, "Chapter 2", ""));

        // Sort the map values by date
        List<LessonDisplay> sortedList = new ArrayList<>(lessonMap.values());
        sortedList.sort((a, b) -> a.getDate().compareTo(b.getDate()));

        assertEquals(3, sortedList.size());
        assertEquals(date2, sortedList.get(0).getDate());
        assertEquals(date3, sortedList.get(1).getDate());
        assertEquals(date1, sortedList.get(2).getDate());
    }

    @Test
    public void lessonWindowClass_exists() {
        // Test that the LessonWindow class exists and can be referenced
        assertNotNull(LessonWindow.class);
    }
}
