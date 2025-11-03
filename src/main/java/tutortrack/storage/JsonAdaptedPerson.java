package tutortrack.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import tutortrack.commons.exceptions.IllegalValueException;
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

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final String name;
    private final String selfContact;
    private final String nokContact;
    private final String subjectLevel;
    private final String dayTime;
    private final String hourlyRate;
    private final String address;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();
    private final List<JsonAdaptedLessonPlan> lessonPlanList = new ArrayList<>();
    private final List<JsonAdaptedLessonProgress> lessonProgressList = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name, @JsonProperty("selfContact") String selfContact,
            @JsonProperty("nokContact") String nokContact,
            @JsonProperty("subjectLevel") String subjectLevel,
            @JsonProperty("dayTime") String dayTime, @JsonProperty("hourlyRate") String hourlyRate,
            @JsonProperty("address") String address, @JsonProperty("tags") List<JsonAdaptedTag> tags,
                             @JsonProperty("lessonPlanList") List<JsonAdaptedLessonPlan> lessonPlanList,
                             @JsonProperty("lessonProgressList") List<JsonAdaptedLessonProgress> lessonProgressList) {
        this.name = name;
        this.selfContact = selfContact;
        this.nokContact = nokContact;
        this.subjectLevel = subjectLevel;
        this.dayTime = dayTime;
        this.hourlyRate = hourlyRate;
        this.address = address;
        if (tags != null) {
            this.tags.addAll(tags);
        }
        if (lessonPlanList != null) {
            this.lessonPlanList.addAll(lessonPlanList);
        }
        if (lessonProgressList != null) {
            this.lessonProgressList.addAll(lessonProgressList);
        }
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().fullName;
        selfContact = (source.getSelfContact() != null) ? source.getSelfContact().value : null;
        nokContact = (source.getNokContact() != null) ? source.getNokContact().value : null;
        subjectLevel = source.getSubjectLevel().value;
        dayTime = source.getDayTime().value;
        hourlyRate = source.getHourlyRate().value;
        address = source.getAddress().value;
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
        lessonPlanList.addAll(source.getLessonPlanList().stream()
                .map(JsonAdaptedLessonPlan::new)
                .collect(Collectors.toList()));
        lessonProgressList.addAll(source.getLessonProgressList().stream()
                .map(JsonAdaptedLessonProgress::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            personTags.add(tag.toModelType());
        }

        final List<LessonPlan> modelLessonPlan = new ArrayList<>();
        for (JsonAdaptedLessonPlan pl : lessonPlanList) {
            modelLessonPlan.add(pl.toModelType());
        }

        final List<LessonProgress> modelLessonProgress = new ArrayList<>();
        for (JsonAdaptedLessonProgress pr : lessonProgressList) {
            modelLessonProgress.add(pr.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if ((selfContact == null || selfContact.isBlank()) && (nokContact == null || nokContact.isBlank())) {
            throw new IllegalValueException("At least one contact (selfContact or nokContact) must be provided!");
        }

        final Phone modelSelfContact;
        final Phone modelNokContact;

        if (selfContact != null && !selfContact.isBlank()) {
            if (!Phone.isValidPhone(selfContact)) {
                throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
            }
            modelSelfContact = new Phone(selfContact);
        } else {
            modelSelfContact = null;
        }

        if (nokContact != null && !nokContact.isBlank()) {
            if (!Phone.isValidPhone(nokContact)) {
                throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
            }
            modelNokContact = new Phone(nokContact);
        } else {
            modelNokContact = null;
        }

        if (subjectLevel == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    SubjectLevel.class.getSimpleName()));
        }
        if (!SubjectLevel.isValidSubjectLevel(subjectLevel)) {
            throw new IllegalValueException(SubjectLevel.MESSAGE_CONSTRAINTS);
        }
        final SubjectLevel modelSubjectLevel = new SubjectLevel(subjectLevel);

        if (dayTime == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    DayTime.class.getSimpleName()));
        }
        if (!DayTime.isValidDayTime(dayTime)) {
            throw new IllegalValueException(DayTime.MESSAGE_CONSTRAINTS);
        }
        final DayTime modelDayTime = new DayTime(dayTime);

        if (hourlyRate == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    HourlyRate.class.getSimpleName()));
        }
        if (!HourlyRate.isValidHourlyRate(hourlyRate)) {
            throw new IllegalValueException(HourlyRate.MESSAGE_CONSTRAINTS);
        }
        final HourlyRate modelHourlyRate = new HourlyRate(hourlyRate);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        final Set<Tag> modelTags = new HashSet<>(personTags);

        Person person = new Person(modelName, modelSelfContact, modelNokContact,
                modelSubjectLevel, modelDayTime, modelHourlyRate, modelAddress, modelTags);

        person.getLessonPlanList().addAll(modelLessonPlan);
        person.getLessonProgressList().addAll(modelLessonProgress);

        return person;
    }

}
