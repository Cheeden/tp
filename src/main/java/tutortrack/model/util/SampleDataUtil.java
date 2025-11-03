package tutortrack.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import tutortrack.model.AddressBook;
import tutortrack.model.ReadOnlyAddressBook;
import tutortrack.model.person.Address;
import tutortrack.model.person.Cost;
import tutortrack.model.person.DayTime;
import tutortrack.model.person.Name;
import tutortrack.model.person.Person;
import tutortrack.model.person.Phone;
import tutortrack.model.person.SubjectLevel;
import tutortrack.model.tag.Tag;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            // with both contacts
            new Person(new Name("Alex Yeoh"), new Phone("87438807"), new Phone("91234567"),
                    new SubjectLevel("P2-Math"), new DayTime("Monday 1200"), new Cost("$50"),
                    new Address("Blk 30 Geylang Street 29, #06-40"),
                    getTagSet("friends")),

            // with both contacts
            new Person(new Name("Bernice Yu"), new Phone("99272758"), new Phone("88112233"),
                    new SubjectLevel("P5-Chinese"), new DayTime("Tuesday 1600"), new Cost("$60"),
                    new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"),
                    getTagSet("colleagues", "friends")),

            // with both contacts
            new Person(new Name("Charlotte Oliveiro"), new Phone("93210765"), new Phone("98765432"),
                    new SubjectLevel("P6-Science"), new DayTime("Wednesday 1400"), new Cost("$50"),
                    new Address("Blk 11 Ang Mo Kio Street 74, #11-04"),
                    getTagSet("neighbours")),

            // with both contacts
            new Person(new Name("David Li"), new Phone("91031282"), new Phone("92345678"),
                    new SubjectLevel("P1-English"), new DayTime("Sunday 1100"), new Cost("$40"),
                    new Address("Blk 436 Serangoon Gardens Street 26, #16-43"),
                    getTagSet("family")),

            // with both contacts
            new Person(new Name("Irfan Ibrahim"), new Phone("92492021"), new Phone("94351253"),
                    new SubjectLevel("Sec1-AMath"), new DayTime("Friday 1800"), new Cost("$70"),
                    new Address("Blk 47 Tampines Street 20, #17-35"),
                    getTagSet("classmates")),

            // with both contacts
            new Person(new Name("Roy Balakrishnan"), new Phone("96665544"), new Phone("91122334"),
                    new SubjectLevel("P4-Math"), new DayTime("Saturday 1300"), new Cost("$55"),
                    new Address("Blk 45 Aljunied Street 85, #11-31"),
                    getTagSet("colleagues"))
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

}
