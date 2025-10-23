package tutortrack.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {

    /* Prefix definitions */
    public static final Prefix PREFIX_NAME = new Prefix("n/");
    public static final Prefix PREFIX_SELF_CONTACT = new Prefix("sc/");
    public static final Prefix PREFIX_NOK_CONTACT = new Prefix("nc/");
    public static final Prefix PREFIX_SUBJECTLEVEL = new Prefix("s/");
    public static final Prefix PREFIX_DAYTIME = new Prefix("d/");
    public static final Prefix PREFIX_COST = new Prefix("c/");
    public static final Prefix PREFIX_ADDRESS = new Prefix("a/");
    public static final Prefix PREFIX_TAG = new Prefix("t/");
    public static final Prefix PREFIX_LESSON_PROGRESS = new Prefix("lp/");
    public static final Prefix PREFIX_LESSON_PLAN = new Prefix("ll/");

}
