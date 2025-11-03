package tutortrack.testutil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import tutortrack.logic.parser.ParserUtil;
import tutortrack.model.lesson.LessonPlan;
import tutortrack.model.lesson.LessonProgress;
import tutortrack.model.person.Address;
import tutortrack.model.person.DayTime;
import tutortrack.model.person.HourlyRate;
import tutortrack.model.person.Name;
import tutortrack.model.person.Person;
import tutortrack.model.person.Phone;
import tutortrack.model.person.SubjectLevel;
import tutortrack.model.tag.Tag;
import tutortrack.model.util.SampleDataUtil;

/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_SELF_CONTACT = "98765432";
    public static final String DEFAULT_NOK_CONTACT = "87438807";
    public static final String DEFAULT_SUBJECTLEVEL = "P4-Math";
    public static final String DEFAULT_DAYTIME = "Monday 1200";
    public static final String DEFAULT_HOURLYRATE = "$50";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";
    public static final String DEFAULT_LESSON_PLAN = "2025-10-15|Cover Chapter 1";
    public static final String DEFAULT_LESSON_PROGRESS = "2025-10-15|Covered Chapter 1";

    private Name name;
    private Phone selfContact;
    private Phone nokContact;
    private SubjectLevel subjectLevel;
    private DayTime dayTime;
    private HourlyRate hourlyRate;
    private Address address;
    private Set<Tag> tags;
    private List<LessonPlan> lessonPlanList = new ArrayList<>();
    private List<LessonProgress> lessonProgressList = new ArrayList<>();

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        name = new Name(DEFAULT_NAME);
        selfContact = new Phone(DEFAULT_SELF_CONTACT);
        nokContact = new Phone(DEFAULT_NOK_CONTACT);
        subjectLevel = new SubjectLevel(DEFAULT_SUBJECTLEVEL);
        dayTime = new DayTime(DEFAULT_DAYTIME);
        hourlyRate = new HourlyRate(DEFAULT_HOURLYRATE);
        address = new Address(DEFAULT_ADDRESS);
        tags = new HashSet<>();
        try {
            String[] parts = DEFAULT_LESSON_PLAN.split("\\|", 2);
            LocalDate date = LocalDate.parse(parts[0].trim());
            String desc = parts[1].trim();
            lessonPlanList.add(ParserUtil.parseLessonPlan(DEFAULT_LESSON_PLAN));
        } catch (Exception e) {
            System.err.println("Warning: failed to parse default lesson plan. Using empty list.");
        }
        try {
            String[] parts = DEFAULT_LESSON_PROGRESS.split("\\|", 2);
            LocalDate date = LocalDate.parse(parts[0].trim());
            String desc = parts[1].trim();
            lessonProgressList.add(ParserUtil.parseLessonProgress(DEFAULT_LESSON_PROGRESS));
        } catch (Exception e) {
            System.err.println("Warning: failed to parse default lesson progress. Using empty list.");
        }
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        name = personToCopy.getName();
        selfContact = personToCopy.getSelfContact();
        nokContact = personToCopy.getNokContact();
        subjectLevel = personToCopy.getSubjectLevel();;
        dayTime = personToCopy.getDayTime();
        hourlyRate = personToCopy.getHourlyRate();
        address = personToCopy.getAddress();
        tags = new HashSet<>(personToCopy.getTags());
        lessonPlanList = new ArrayList<>();
        if (personToCopy.getLessonPlanList() != null) {
            lessonPlanList.addAll(personToCopy.getLessonPlanList());
        }
        lessonProgressList = new ArrayList<>();
        if (personToCopy.getLessonProgressList() != null) {
            lessonProgressList.addAll(personToCopy.getLessonProgressList());
        }
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Person} that we are building.
     */
    public PersonBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Person} that we are building.
     */
    public PersonBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code selfContact} of the {@code Person} that we are building.
     */
    public PersonBuilder withSelfContact(String phone) {
        this.selfContact = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code nokContact} of the {@code Person} that we are building.
     */
    public PersonBuilder withNokContact(String phone) {
        this.nokContact = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code SubjectLevel} of the {@code Person} that we are building.
     */
    public PersonBuilder withSubjectLevel(String subjectLevel) {
        this.subjectLevel = new SubjectLevel(subjectLevel);
        return this;
    }

    /**
     * Sets the {@code DayTime} of the {@code Person} that we are building.
     */
    public PersonBuilder withDayTime(String dayTime) {
        this.dayTime = new DayTime(dayTime);
        return this;
    }

    /**
     * Sets the {@code hourlyRate} of the {@code Person} that we are building.
     */
    public PersonBuilder withHourlyRate(String hourlyRate) {
        this.hourlyRate = new HourlyRate(hourlyRate);
        return this;
    }

    /**
     * Sets the {@code Lesson Progress} of the {@code Person} that we are building.
     */
    public PersonBuilder withLessonPlan(String... plans) {
        this.lessonPlanList = Arrays.stream(plans)
                .map(s -> {
                    String[] parts = s.split("\\|", 2);
                    LocalDate date = LocalDate.parse(parts[0].trim());
                    String desc = parts[1].trim();
                    return new LessonPlan(date, desc);
                })
                .collect(Collectors.toList());
        return this;
    }

    /**
     * Sets the {@code Lesson Progress} of the {@code Person} that we are building.
     */
    public PersonBuilder withLessonProgress(String... progresses) {
        this.lessonProgressList = Arrays.stream(progresses)
                .map(s -> {
                    String[] parts = s.split("\\|", 2);
                    LocalDate date = LocalDate.parse(parts[0].trim());
                    String desc = parts[1].trim();
                    return new LessonProgress(date, desc);
                })
                .collect(Collectors.toList());
        return this;
    }

    /**
     * Builds a person.
     */
    public Person build() {
        Person person = new Person(name, selfContact, nokContact, subjectLevel, dayTime, hourlyRate, address, tags);
        person.getLessonProgressList().addAll(lessonProgressList);
        return person;
    }
}
