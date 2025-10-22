package tutortrack.model.person;

import static tutortrack.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tutortrack.commons.util.ToStringBuilder;
import tutortrack.model.lesson.LessonPlan;
import tutortrack.model.lesson.LessonProgress;
import tutortrack.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Phone phone;

    // Data fields
    private final SubjectLevel subjectLevel;
    private final DayTime dayTime;
    private final Cost cost;
    private final Address address;
    private final Set<Tag> tags = new HashSet<>();
    private final ObservableList<LessonProgress> lessonProgressList = FXCollections.observableArrayList();
    private final ObservableList<LessonPlan> lessonPlanList = FXCollections.observableArrayList();

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, SubjectLevel subjectLevel,
                  DayTime dayTime, Cost cost, Address address, Set<Tag> tags) {
        requireAllNonNull(name, phone, subjectLevel, dayTime, cost, address, tags);
        this.name = name;
        this.phone = phone;
        this.subjectLevel = subjectLevel;
        this.dayTime = dayTime;
        this.cost = cost;
        this.address = address;
        this.tags.addAll(tags);
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public SubjectLevel getSubjectLevel() {
        return subjectLevel;
    }

    public DayTime getDayTime() {
        return dayTime;
    }

    public Cost getCost() {
        return cost;
    }

    public Address getAddress() {
        return address;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    public void addLessonProgress(LessonProgress lp) {
        lessonProgressList.add(lp);
    }

    public ObservableList<LessonProgress> getLessonProgressList() {
        return lessonProgressList;
    }

    public ObservableList<LessonPlan> getLessonPlanList() {
        return lessonPlanList;
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName());
    }

    /**
     * Returns true if both persons have the same identity and data fields except cost.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && subjectLevel.equals(otherPerson.subjectLevel)
                && dayTime.equals(otherPerson.dayTime)
                && address.equals(otherPerson.address)
                && tags.equals(otherPerson.tags);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, subjectLevel, dayTime, cost, address, tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("subjectLevel", subjectLevel)
                .add("dayTime", dayTime)
                .add("cost", cost)
                .add("address", address)
                .add("tags", tags)
                .add("lesson plan", lessonPlanList)
                .add("lesson progress", lessonProgressList)
                .toString();
    }
}
