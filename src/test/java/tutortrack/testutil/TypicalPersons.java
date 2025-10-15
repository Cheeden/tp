package tutortrack.testutil;

import static tutortrack.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static tutortrack.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static tutortrack.logic.commands.CommandTestUtil.VALID_COST_AMY;
import static tutortrack.logic.commands.CommandTestUtil.VALID_COST_BOB;
import static tutortrack.logic.commands.CommandTestUtil.VALID_DAYTIME_AMY;
import static tutortrack.logic.commands.CommandTestUtil.VALID_DAYTIME_BOB;
import static tutortrack.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static tutortrack.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static tutortrack.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static tutortrack.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static tutortrack.logic.commands.CommandTestUtil.VALID_SUBJECTLEVEL_AMY;
import static tutortrack.logic.commands.CommandTestUtil.VALID_SUBJECTLEVEL_BOB;
import static tutortrack.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static tutortrack.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tutortrack.model.AddressBook;
import tutortrack.model.person.Person;

/**
 * A utility class containing a list of {@code Person} objects to be used in tests.
 */
public class TypicalPersons {

    public static final Person ALICE = new PersonBuilder().withName("Alice Pauline")
            .withAddress("123, Jurong West Ave 6, #08-111")
            .withPhone("94351253").withSubjectLevel("P4-Math").withDayTime("Monday 1200").withCost("$50")
            .withTags("friends").build();
    public static final Person BENSON = new PersonBuilder().withName("Benson Meier")
            .withAddress("311, Clementi Ave 2, #02-25").withPhone("98765432")
            .withSubjectLevel("P5-Math").withDayTime("Monday 1300").withCost("$60")
            .withTags("owesMoney", "friends").build();
    public static final Person CARL = new PersonBuilder().withName("Carl Kurz").withPhone("95352563")
            .withSubjectLevel("P1-Chinese").withDayTime("Tuesday 1200").withCost("$40")
            .withAddress("wall street").build();
    public static final Person DANIEL = new PersonBuilder().withName("Daniel Meier").withPhone("87652533")
            .withSubjectLevel("Sec1-Math").withDayTime("Friday 1400").withCost("$70")
            .withAddress("10th street").withTags("friends").build();
    public static final Person ELLE = new PersonBuilder().withName("Elle Meyer").withPhone("9482224")
            .withSubjectLevel("P6-English").withDayTime("Thursday 1000").withCost("$55")
            .withAddress("michegan ave").build();
    public static final Person FIONA = new PersonBuilder().withName("Fiona Kunz").withPhone("9482427")
            .withSubjectLevel("P3-Science").withDayTime("Sunday 1200").withCost("$60")
            .withAddress("little tokyo").build();
    public static final Person GEORGE = new PersonBuilder().withName("George Best").withPhone("9482442")
            .withSubjectLevel("P1-Math").withDayTime("Monday 1800").withCost("$40")
            .withAddress("4th street").build();

    // Manually added
    public static final Person HOON = new PersonBuilder().withName("Hoon Meier").withPhone("8482424")
            .withSubjectLevel("P2-Math").withDayTime("Monday 1000").withCost("$50")
            .withAddress("little india").build();
    public static final Person IDA = new PersonBuilder().withName("Ida Mueller").withPhone("8482131")
            .withSubjectLevel("P4-English").withDayTime("Saturday 1200").withCost("$50")
            .withAddress("chicago ave").build();

    // Manually added - Person's details found in {@code CommandTestUtil}
    public static final Person AMY = new PersonBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
            .withSubjectLevel(VALID_SUBJECTLEVEL_AMY).withDayTime(VALID_DAYTIME_AMY)
            .withCost(VALID_COST_AMY).withAddress(VALID_ADDRESS_AMY).withTags(VALID_TAG_FRIEND).build();
    public static final Person BOB = new PersonBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
            .withSubjectLevel(VALID_SUBJECTLEVEL_BOB).withDayTime(VALID_DAYTIME_BOB)
            .withCost(VALID_COST_BOB).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND)
            .build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalPersons() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical persons.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Person person : getTypicalPersons()) {
            ab.addPerson(person);
        }
        return ab;
    }

    public static List<Person> getTypicalPersons() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
