package tutortrack.model.person;

import java.util.Comparator;
import java.util.function.Predicate;

import tutortrack.commons.util.ToStringBuilder;

public class LessonDayPredicate implements Predicate<Person> {
    private final String dayKeyword;

    public LessonDayPredicate(String dayKeyword) {
        assert dayKeyword != null : "Day keyword cannot be null";
        assert !dayKeyword.trim().isEmpty() : "Day keyword cannot be empty";
        this.dayKeyword = dayKeyword;
    }

    @Override
    public boolean test(Person person) {
        assert person != null : "Person cannot be null";
        assert person.getDayTime() != null : "Person's dayTime cannot be null";
        
        String dayTime = person.getDayTime().value;
        // Extract day from "Monday 1200" format
        String day = dayTime.split("\\s+")[0];
        
        return day.equalsIgnoreCase(dayKeyword);
    }

    /**
     * Returns a comparator that sorts persons by time (earliest first).
     * If times are equal, sorts alphabetically by name.
     */
    public Comparator<Person> getComparator() {
        return (person1, person2) -> {
            String firstDayTime = person1.getDayTime().value;
            String secondDayTime = person2.getDayTime().value;

            // extract time part of the dayTime string
            String time1 = firstDayTime.split("\\s+")[1];
            String time2 = secondDayTime.split("\\s+")[1];

            // compare times numerically
            int timeComparison = Integer.compare(Integer.parseInt(time1), Integer.parseInt(time2));

            if (timeComparison != 0) {
                return timeComparison;
            }

            // times are equal, sort alphabetically by name
            return person1.getName().fullName.compareToIgnoreCase(person2.getName().fullName);
        };
    }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            if (!(other instanceof LessonDayPredicate)) {
                return false;
            }

            LessonDayPredicate otherLessonDayPredicate = (LessonDayPredicate) other;
            return dayKeyword.equals(otherLessonDayPredicate.dayKeyword);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this).add("dayKeyword", dayKeyword).toString();
        }
}