package tutortrack.testutil;

import java.util.HashSet;
import java.util.Set;

import tutortrack.model.person.Address;
import tutortrack.model.person.Email;
import tutortrack.model.person.Name;
import tutortrack.model.person.Person;
import tutortrack.model.person.Phone;
import tutortrack.model.tag.Tag;
import tutortrack.model.util.SampleDataUtil;

/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_SUBJECTLEVEL = "P4-Math";
    public static final String DEFAULT_DAYTIME = "Monday 1200";
    public static final String DEFAULT_COST = "$50";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";

    private Name name;
    private Phone phone;
    private Email email;
    private String subjectLevel;
    private String dayTime;
    private String cost;
    private Address address;
    private Set<Tag> tags;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        subjectLevel = DEFAULT_SUBJECTLEVEL;
        dayTime = DEFAULT_DAYTIME;
        cost = DEFAULT_COST;
        address = new Address(DEFAULT_ADDRESS);
        tags = new HashSet<>();
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        name = personToCopy.getName();
        phone = personToCopy.getPhone();
        email = personToCopy.getEmail();
        subjectLevel = personToCopy.getSubjectLevel();;
        dayTime = personToCopy.getDayTime();
        cost = personToCopy.getCost();
        address = personToCopy.getAddress();
        tags = new HashSet<>(personToCopy.getTags());
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
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public PersonBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Person} that we are building.
     */
    public PersonBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    /**
     * Sets the {@code SubjectLevel} of the {@code Person} that we are building.
     */
    public PersonBuilder withSubjectLevel(String subjectLevel) {
        this.subjectLevel = subjectLevel;
        return this;
    }

    /**
     * Sets the {@code DayTime} of the {@code Person} that we are building.
     */
    public PersonBuilder withDayTime(String dayTime) {
        this.dayTime = dayTime;
        return this;
    }

    /**
     * Sets the {@code Cost} of the {@code Person} that we are building.
     */
    public PersonBuilder withCost(String cost) {
        this.cost = cost;
        return this;
    }

    public Person build() {
        return new Person(name, phone, email, subjectLevel, dayTime, cost, address, tags);
    }

}
