package tutortrack.testutil;

import static tutortrack.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static tutortrack.logic.parser.CliSyntax.PREFIX_COST;
import static tutortrack.logic.parser.CliSyntax.PREFIX_DAYTIME;
import static tutortrack.logic.parser.CliSyntax.PREFIX_LESSON_PROGRESS;
import static tutortrack.logic.parser.CliSyntax.PREFIX_NAME;
import static tutortrack.logic.parser.CliSyntax.PREFIX_PHONE;
import static tutortrack.logic.parser.CliSyntax.PREFIX_SUBJECTLEVEL;
import static tutortrack.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.List;
import java.util.Set;

import tutortrack.logic.commands.AddCommand;
import tutortrack.logic.commands.EditCommand.EditPersonDescriptor;
import tutortrack.model.lesson.LessonProgress;
import tutortrack.model.person.Person;
import tutortrack.model.tag.Tag;

/**
 * A utility class for Person.
 */
public class PersonUtil {

    /**
     * Returns an add command string for adding the {@code person}.
     */
    public static String getAddCommand(Person person) {
        return AddCommand.COMMAND_WORD + " " + getPersonDetails(person);
    }

    /**
     * Returns the part of command string for the given {@code person}'s details.
     */
    public static String getPersonDetails(Person person) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + person.getName().fullName + " ");
        sb.append(PREFIX_PHONE + person.getPhone().value + " ");
        sb.append(PREFIX_SUBJECTLEVEL + person.getSubjectLevel() + " ");
        sb.append(PREFIX_DAYTIME + person.getDayTime() + " ");
        sb.append(PREFIX_COST + person.getCost() + " ");
        sb.append(PREFIX_ADDRESS + person.getAddress().value + " ");
        person.getTags().stream().forEach(
            s -> sb.append(PREFIX_TAG + s.tagName + " ")
        );
        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code EditPersonDescriptor}'s details.
     */
    public static String getEditPersonDescriptorDetails(EditPersonDescriptor descriptor) {
        StringBuilder sb = new StringBuilder();
        descriptor.getName().ifPresent(name -> sb.append(PREFIX_NAME).append(name.fullName).append(" "));
        descriptor.getPhone().ifPresent(phone -> sb.append(PREFIX_PHONE).append(phone.value).append(" "));
        descriptor.getSubjectLevel().ifPresent(subjectLevel ->
                                                       sb.append(PREFIX_SUBJECTLEVEL).append(subjectLevel).append(" "));
        descriptor.getDayTime().ifPresent(dayTime -> sb.append(PREFIX_DAYTIME).append(dayTime).append(" "));
        descriptor.getCost().ifPresent(cost -> sb.append(PREFIX_COST).append(cost).append(" "));
        descriptor.getAddress().ifPresent(address -> sb.append(PREFIX_ADDRESS).append(address.value).append(" "));
        if (descriptor.getTags().isPresent()) {
            Set<Tag> tags = descriptor.getTags().get();
            if (!tags.isEmpty()) {
                tags.forEach(s -> sb.append(PREFIX_TAG).append(s.tagName).append(" "));
            }
        }

        if (descriptor.getLessonProgressList().isPresent()) {
            List<LessonProgress> progresses = descriptor.getLessonProgressList().get();
            if (progresses.isEmpty()) {
                sb.append(PREFIX_LESSON_PROGRESS);
            } else {
                progresses.forEach(progress ->
                        sb.append(PREFIX_LESSON_PROGRESS)
                                .append(progress.getDate()).append("|")
                                .append(progress.getProgress()).append(" "));
            }
        }

        return sb.toString();
    }
}
