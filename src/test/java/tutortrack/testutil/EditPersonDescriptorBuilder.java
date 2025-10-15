package tutortrack.testutil;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import tutortrack.logic.commands.EditCommand.EditPersonDescriptor;
import tutortrack.model.lesson.LessonProgress;
import tutortrack.model.person.Address;
import tutortrack.model.person.Name;
import tutortrack.model.person.Person;
import tutortrack.model.person.Phone;
import tutortrack.model.tag.Tag;

/**
 * A utility class to help with building EditPersonDescriptor objects.
 */
public class EditPersonDescriptorBuilder {

    private EditPersonDescriptor descriptor;

    public EditPersonDescriptorBuilder() {
        descriptor = new EditPersonDescriptor();
    }

    public EditPersonDescriptorBuilder(EditPersonDescriptor descriptor) {
        this.descriptor = new EditPersonDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditPersonDescriptor} with fields containing {@code person}'s details
     */
    public EditPersonDescriptorBuilder(Person person) {
        descriptor = new EditPersonDescriptor();
        descriptor.setName(person.getName());
        descriptor.setPhone(person.getPhone());
        descriptor.setSubjectLevel(person.getSubjectLevel());
        descriptor.setDayTime(person.getDayTime());
        descriptor.setCost(person.getCost());
        descriptor.setAddress(person.getAddress());
        descriptor.setTags(person.getTags());
        descriptor.setLessonProgressList(person.getLessonProgressList());
    }

    /**
     * Sets the {@code Name} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withName(String name) {
        descriptor.setName(new Name(name));
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withPhone(String phone) {
        descriptor.setPhone(new Phone(phone));
        return this;
    }


    /**
     * Sets the {@code SubjectLevel} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withSubjectLevel(String subjectLevel) {
        descriptor.setSubjectLevel(subjectLevel);
        return this;
    }

    /**
     * Sets the {@code dayTime} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withDayTime(String dayTime) {
        descriptor.setDayTime(dayTime);
        return this;
    }

    /**
     * Sets the {@code Cost} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withCost(String cost) {
        descriptor.setCost(cost);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withAddress(String address) {
        descriptor.setAddress(new Address(address));
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code EditPersonDescriptor}
     * that we are building.
     */
    public EditPersonDescriptorBuilder withTags(String... tags) {
        Set<Tag> tagSet = Stream.of(tags).map(Tag::new).collect(Collectors.toSet());
        descriptor.setTags(tagSet);
        return this;
    }

    public EditPersonDescriptorBuilder withLessonProgresses(LessonProgress... lessonProgresses) {
        descriptor.setLessonProgressList(Arrays.asList(lessonProgresses));
        return this;
    }

    public EditPersonDescriptor build() {
        return descriptor;
    }
}
