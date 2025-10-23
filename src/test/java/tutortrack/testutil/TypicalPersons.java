package tutortrack.testutil;

import static tutortrack.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static tutortrack.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static tutortrack.logic.commands.CommandTestUtil.VALID_CONTACT_AMY;
import static tutortrack.logic.commands.CommandTestUtil.VALID_CONTACT_BOB;
import static tutortrack.logic.commands.CommandTestUtil.VALID_COST_AMY;
import static tutortrack.logic.commands.CommandTestUtil.VALID_COST_BOB;
import static tutortrack.logic.commands.CommandTestUtil.VALID_DAYTIME_AMY;
import static tutortrack.logic.commands.CommandTestUtil.VALID_DAYTIME_BOB;
import static tutortrack.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static tutortrack.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static tutortrack.logic.commands.CommandTestUtil.VALID_NOK_CONTACT_AMY;
import static tutortrack.logic.commands.CommandTestUtil.VALID_NOK_CONTACT_BOB;
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
                                               .withSelfContact("94351253").withNokContact("81234567")
                                               .withSubjectLevel("P4-Math").withDayTime("Monday 1200").withCost("$50")
                                               .withTags("friends")
                                               .withLessonPlan("2025-10-16|Cover chapter 1")
                                               .withLessonProgress("2025-10-16|Covered chapter 1").build();

    public static final Person BENSON = new PersonBuilder().withName("Benson Meier")
                                                .withAddress("311, Clementi Ave 2, #02-25")
                                                .withSelfContact("98765432").withNokContact("87654321")
                                                .withSubjectLevel("P5-Math").withDayTime("Monday 1300").withCost("$60")
                                                .withTags("owesMoney", "friends")
                                                .withLessonPlan("2025-10-16|Cover chapter 2")
                                                .withLessonProgress("2025-10-16|Covered chapter 2").build();

    public static final Person CARL = new PersonBuilder().withName("Carl Kurz")
                                              .withSelfContact("95352563").withNokContact("90001234")
                                              .withSubjectLevel("P1-Chinese").withDayTime("Tuesday 1200")
                                              .withCost("$40").withAddress("wall street")
                                              .withLessonPlan("2025-10-16|Cover chapter 3")
                                              .withLessonProgress("2025-10-16|Covered chapter 3").build();

    public static final Person DANIEL = new PersonBuilder().withName("Daniel Meier")
                                                .withSelfContact("87652533").withNokContact("83334444")
                                                .withSubjectLevel("Sec1-Math").withDayTime("Friday 1400")
                                                .withCost("$70").withAddress("10th street").withTags("friends")
                                                .withLessonPlan("2025-10-16|Cover chapter 4")
                                                .withLessonProgress("2025-10-16|Covered chapter 4").build();

    public static final Person ELLE = new PersonBuilder().withName("Elle Meyer")
                                              .withSelfContact("9482224").withNokContact("81112222")
                                              .withSubjectLevel("P6-English").withDayTime("Thursday 1000")
                                              .withCost("$55").withAddress("michegan ave")
                                              .withLessonPlan("2025-10-16|Cover chapter 5")
                                              .withLessonProgress("2025-10-16|Covered chapter 5").build();

    public static final Person FIONA = new PersonBuilder().withName("Fiona Kunz")
                                               .withSelfContact("9482427").withNokContact("82223333")
                                               .withSubjectLevel("P3-Science").withDayTime("Sunday 1200")
                                               .withCost("$60").withAddress("little tokyo")
                                               .withLessonPlan("2025-10-16|Cover chapter 6")
                                               .withLessonProgress("2025-10-16|Covered chapter 6").build();

    public static final Person GEORGE = new PersonBuilder().withName("George Best")
                                                .withSelfContact("9482442").withNokContact("89998888")
                                                .withSubjectLevel("P1-Math").withDayTime("Monday 1800").withCost("$40")
                                                .withAddress("4th street")
                                                .withLessonPlan("2025-10-16|Cover chapter 1")
                                                .withLessonProgress("2025-10-16|Covered chapter 7").build();

    // Manually added
    public static final Person HOON = new PersonBuilder().withName("Hoon Meier")
                                              .withSelfContact("8482424").withNokContact("87776666")
                                              .withSubjectLevel("P2-Math").withDayTime("Monday 1000").withCost("$50")
                                              .withAddress("little india")
                                              .withLessonPlan("2025-10-16|Cover chapter 8")
                                              .withLessonProgress("2025-10-16|Covered chapter 8").build();

    public static final Person IDA = new PersonBuilder().withName("Ida Mueller")
                                             .withSelfContact("8482131").withNokContact("86665555")
                                             .withSubjectLevel("P4-English").withDayTime("Saturday 1200")
                                             .withCost("$50").withAddress("chicago ave")
                                             .withLessonPlan("2025-10-16|Cover chapter 9")
                                             .withLessonProgress("2025-10-16|Covered chapter 9").build();

    // Manually added - Person's details found in {@code CommandTestUtil}
    public static final Person AMY = new PersonBuilder().withName(VALID_NAME_AMY)
                                             .withSelfContact(VALID_CONTACT_AMY).withNokContact(VALID_NOK_CONTACT_AMY)
                                             .withSubjectLevel(VALID_SUBJECTLEVEL_AMY).withDayTime(VALID_DAYTIME_AMY)
                                             .withCost(VALID_COST_AMY).withAddress(VALID_ADDRESS_AMY)
                                             .withTags(VALID_TAG_FRIEND).build();

    public static final Person BOB = new PersonBuilder().withName(VALID_NAME_BOB)
                                             .withSelfContact(VALID_CONTACT_BOB).withNokContact(VALID_NOK_CONTACT_BOB)
                                             .withSubjectLevel(VALID_SUBJECTLEVEL_BOB).withDayTime(VALID_DAYTIME_BOB)
                                             .withCost(VALID_COST_BOB).withAddress(VALID_ADDRESS_BOB)
                                             .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).build();

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
