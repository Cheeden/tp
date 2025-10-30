---
layout: page
title: User Guide
---

TutorTrack is a **desktop app for managing student contacts, optimized for use via a Command Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI). If you can type fast, TutorTrack can help you track lesson plans, assignments, deadlines, and student progress more efficiently faster than traditional GUI apps.

* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `17` or above installed in your Computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1. Download the latest `.jar` file from [here](https://github.com/AY2526S1-CS2103T-W11-4/tp/releases).

1. Copy the file to the folder you want to use as the _home folder_ for your TutorTrack.

1. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar tutortrack.jar` command to run the application.<br>
   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

   * `list` : Lists all contacts.

   * `add n/John Doe p/98765432 s/P4-Math d/Monday 1200 c/$60 a/John street, block 123, #01-01` : Adds a contact named `John Doe` to the Address Book.

   * `delete 3` : Deletes the 3rd contact shown in the current list.

   * `clear` : Deletes all contacts.

   * `exit` : Exits the app.

1. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add n/NAME`, `NAME` is a parameter which can be used as `add n/John Doe`.

* Items in square brackets are optional.<br>
  e.g `n/NAME [t/TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[t/TAG]…​` can be used as ` ` (i.e. 0 times), `t/friend`, `t/friend t/family` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* `INDEX` refers to the index number shown in the displayed person list, which **starts from 1**.<br>
  The index **must be a positive integer** 1, 2, 3, …​<br>
  e.g. the command `edit 2 n/NAME` will edit the name of the 2nd person in the list.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</div>

### Viewing help : `help`

Shows a message explaining how to access the help page.

![help message](images/helpMessage.png)

Format: `help`


### Adding a person: `add`

Adds a person to the address book.

Format: `add n/NAME s/SUBJECTLEVEL d/DAYTIME c/COST a/ADDRESS [sc/SELFCONTACT] [nc/NOKCONTACT] [t/TAG]…​`

* `sc/SELFCONTACT` is the student's own contact number.

* `nc/NOKCONTACT` is the next-of-kin's contact number.

* At least one of `sc/` or `nc/` must be provided and contain a valid phone number.

* It is not allowed for both fields to be missing or for both fields to be empty (e.g., `sc/` `nc/`).

<div markdown="span" class="alert alert-primary">:bulb: **Tip:**
A person can have any number of tags (including 0)
</div>

Examples:
* `add n/John Doe sc/98765432 s/P4-Math d/Monday 1200 c/$60 a/John street, block 123, #01-01`
* `add n/Betsy Crowe t/friend s/P6-Science d/Tuesday 1400 c/$50 a/Newgate Prison sc/1234567 nc/ t/criminal`

More examples showing accepted SubjectLevel formats and common variations:
* `add n/Alice Tan sc/91234567 s/P6-Math d/Wednesday 0900 c/$45 a/Blk 88, Bedok St` — classic short level `P6`.
* `add n/Ben Lim sc/98761234 s/Pri6-Math d/Thursday 1000 c/$40 a/Blk 12, Clementi` — alternative short form `Pri6` is allowed.
* `add n/Chong Wei sc/91230000 s/Primary6-Math d/Friday 1100 c/$50 a/123, Jurong St` — longer level text such as `Primary6` is allowed.
* `add n/Debra Koh sc/87654321 s/Sec1-English d/Monday 1300 c/$55 a/Blk 2, Queen St` — secondary levels like `Sec1` are allowed.

Notes on SubjectLevel validation
* Format: Subject level must be in the form `Level-Subject` where the two parts are separated by a single dash (`-`).
  - The "Level" part may contain letters and digits (alphanumeric) and must not contain spaces (examples: `P4`, `P6`, `Pri6`, `Primary6`, `Sec1`).
  - The "Subject" part must contain only letters (no digits, no spaces, no punctuation) and represents the subject name (examples: `Math`, `English`, `Science`).
* Examples of valid subject-level tokens:
  - `P4-Math`, `P6-Science`, `Pri6-Math`, `Primary6-Mathematics` (note: `Mathematics` must be a single word without spaces)
  - `Sec1-English`, `Sec2-Physics`
* Examples of invalid subject-level tokens and why they are rejected:
  - `P4 Math` (missing dash between level and subject)
  - `P4-M4th` (subject contains digits)
  - `P4-Math-Advanced` (extra dash; only a single dash separator is allowed)
  - `Primary 6-Math` (spaces in the level part are not allowed)

If you enter an invalid subject-level, the parser will show an error message explaining the required format so you can correct it.

### Listing all persons : `list`

Shows a list of all persons in the address book.

Format: `list`

### Editing a person : `edit`

Edits an existing person in the address book.

Format: `edit INDEX [n/NAME] [sc/SELFCONTACT] [nc/NOKCONTACT] [s/SUBJECTLEVEL] [d/DAYTIME] [c/COST] [a/ADDRESS] [t/TAG]…​`

* Edits the person at the specified `INDEX`.
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* When editing tags, the existing tags of the person will be removed i.e adding of tags is not cumulative.
* You can remove all the person’s tags by typing `t/` without
    specifying any tags after it.

Examples:
*  `edit 1 s/P4-Math ` Edits the subject level of the 1st person to be `P4-Math`.
*  `edit 2 n/Betsy Crower t/` Edits the name of the 2nd person to be `Betsy Crower` and clears all existing tags.

### Locating persons: `find`

Finds persons whose names, tags, or lesson days match any of the given keywords.

Format: `find KEYWORD [MORE_KEYWORDS]` OR `find s/SUBJECT_LEVEL` OR `find t/TAG_KEYWORD [MORE_TAG_KEYWORDS]` OR `find d/DAY`

**Search by Name:**
* The search is case-insensitive. e.g `hans` will match `Hans`
* The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
* By default (without prefix eg t/), the name of the student is searched using prefix matching.
* Any name token starting with the keyword will be matched e.g. `Han` will match `Hans` and `Hannah`
* **Results are ranked by relevance**: First name matches appear first, followed by last name matches, then alphabetically.
* Persons matching at least one keyword will be returned (i.e. `OR` search).
  e.g. `Hans Bo` will return `Hans Gruber`, `Bo Yang`

**Search by Tag (with `t/` prefix):**
* Only tags are searched.
* Only full words will be matched.
* The search is case-insensitive.
* Persons matching at least one tag keyword will be returned.

**Search by Lesson Day (with `d/` prefix):**
* Only searches by the lesson day (Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday).
* The search is case-insensitive. e.g. `monday`, `MONDAY`, and `Monday` all work.
* **Results are automatically sorted by lesson time** (earliest to latest).
* If multiple lessons have the same time, they are sorted alphabetically by name.
* Only one day can be searched at a time.

Examples:
* `find John` returns `john` and `John Doe` (first names starting with "John" ranked higher)
* `find Jo` returns `John Doe` and `Joseph Tan` (both match first name, alphabetically sorted)
* `find alex david` returns `Alex Yeoh`, `David Li`<br>
  ![result for 'find alex david'](images/findAlexDavidResult.png)
* `find t/friends` returns all persons tagged with `friends`
* `find t/friends colleagues` returns all persons tagged with either `friends` or `colleagues`
* `find d/Monday` returns all persons with Monday lessons, sorted by lesson time (e.g., 0900 before 1400)
* `find d/tuesday` returns all persons with Tuesday lessons (case-insensitive)

**Search by Subject (with `s/` prefix):**
* Only searches by the person's subject level. The subject level uses the format `Level-Subject` (e.g., `P4-Math`, `Sec1-English`).
* The search is case-insensitive and matches full subject-level tokens (so `s/P4-Math` matches `P4-Math` regardless of case).
* Results are not specially ranked for subject matches (they are returned in the current list order or as provided by the command context).

Examples:
* `find s/P4-Math` returns all persons whose subject level is `P4-Math` (case-insensitive)

<div markdown="span" class="alert alert-warning">:exclamation: **Note:**
If no persons match your search criteria, an error message will be displayed and your current list will remain unchanged. The search command will stay in the command box (with a red border) so you can easily edit and retry your search.
</div>

### Deleting a person : `delete`

Deletes the specified person from the address book.

Format: `delete INDEX`

* Deletes the person at the specified `INDEX`.

Examples:
* `list` followed by `delete 2` deletes the 2nd person in the address book.
* `find Betsy` followed by `delete 1` deletes the 1st person in the results of the `find` command.


## Lesson Management Commands

TutorTrack allows tutors to add, edit, and delete lesson plans and lesson progress for each student.

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the Lesson Management command format:**<br>

* `DATE` represents the day of a lesson. Each student can have at most one `PLAN` and one `PROGRESS` per lesson day.
* `DATE` must be in the format YYYY-MM-DD.<br>
  e.g. `2025-10-30` is the only valid input format, while `Oct 30, 2025` and `2025/10/30` are invalid formats (There could be other invalid formats).
* `DATE` MM must be between 1 and 12 inclusive and DD must be between 1 and 31 inclusive.<br>
  Otherwise, there will be an error message.
* `PLAN` is a short description of the topics or activities planned for that lesson.<br>
  `PROGRESS` is a short description of what was covered or achieved in that lesson.
* There must be a '|' to segment `DATE` and `DESCRIPTION`.<br>
  e.g. `addplan INDEX pl/DATE|PLAN` is the only valid input format, while `addplan INDEX pl/DATE, DESCRIPTION` and `addplan INDEX pl/DATE DESCRIPTION` are invalid formats (There could be other invalid formats).
* The commands are case-sensitive, therefore must use lowercase letters.<br>
  e.g. `addplan`, `editplan`, `deleteprogress`, `viewlessons`

### Adding lesson plan : `addplan`

Adds a new lesson plan entry for a specific student.

Format:
`addplan INDEX pl/DATE|PLAN`

* Adds a lesson plan to the student at the specified `INDEX`.
* Each new entry will be added to the student’s lesson plan list.
* Entries can later be viewed with the `viewlessons` command.

> **Tip:**
> Use the `addplan` command to schedule and keep track of upcoming lessons for each student.

Examples:

* `addplan 1 pl/2025-10-15|Cover Chapter 5`
Adds a lesson plan on 15 Oct 2025 for the 1st student.

* `addplan 2 pl/2025-10-22|Cover Chapter 6 and Review Assignment 2`
Adds a lesson plan on 22 Oct 2025 for the 2nd student.

* `addplan 3 pl/2025-10-29|Review past chapters and then complete a practice paper`
Adds a lesson plan on 29 Oct 2025 for the 2nd student.

Expected outcome:<br>

* A success message will be displayed in the result box confirming that the lesson plan has been added.
* The new entry will appear in the student’s lesson plan list, viewable using `viewlessons`.
* ![Add Plan Success](images/addPlanSuccess.png)

### Deleting lesson plan : `deleteplan`

Deletes a lesson plan entry for a specific student on a given date.

Format: `deleteplan INDEX DATE`

* Deletes the lesson plan for the student at the specified `INDEX` on the specified `DATE`.
* The lesson plan on that date must exist. If no lesson plan exists on that date, an error message will be shown.

Examples:

* `deleteplan 1 2025-10-25` deletes the lesson plan for the 1st student on 25 Oct 2025.
* `deleteplan 2 2025-10-23` deletes the lesson plan for the 2nd student on 23 Oct 2025.

### Add Lesson Progress : `addprogress`

Adds a lesson progress to a student.

Format: `addprogress INDEX Date|Description`

* Adds a lesson progress entry to the student at the specified INDEX.
* Each new entry will be added to the student’s lesson progress history.
* Entries can later be viewed with the `viewlessons` command.

> **Tip:**
>Use the `addprogress` command regularly to keep an updated record of each student’s learning progress.

Examples:

* `addprogress 1 pr/2025-10-15|Reviewed Chapter 5`
Adds a progress entry on 15 Oct 2025 for the 1st student.

* `addprogress 2 pr/2025-10-22|Covered Chapter 6, Assignment 2 not completed`
Adds a progress entry on 22 Oct 2025 for the 2nd student.

* `addprogress 3 pr/2025-10-29|Revision on Chapter 4, completed Dunman Secondary School 2023 Practice Paper`
Adds a progress entry on 29 Oct 2025 for the 3rd student.

Expected outcome:<br>

* A success message will be displayed in the result box confirming that the lesson progress has been added.
* The new entry will appear in the student's lesson progress list, viewable using `viewlessons`.
* ![Add Progress Success](images/addProgressSuccess.png)

### Edit Lesson Plan : `editplan`

Updates an existing lesson plan entry for a student.

Format: `editplan INDEX pl/Date|Description`

* Updates an existing lesson plan entry for the student at the specified INDEX.
* **The input `DATE` must match an existing lesson plan entry for the student.**<br>
  If no lesson plan or more than one lesson plan entry is found for the specified date, an error message will be shown.
* `DESCRIPTION` is the new description for the lesson plan, which will overwrite the old entry for that date.

> **Tip:**
> Use `editplan` to adjust future lesson plans as a student's needs change. Use `viewplans` first to see which dates have entries you can edit.

Examples:

* `editplan 1 pl/2025-10-28|Focus on polynomial division instead`
  Updates the lesson plan for 28 Oct 2025 for the 1st student with the new description.

* `editplan 2 pl/2025-10-22|Spend more time on thesis statements`
  Updates the lesson plan for 22 Oct 2025 for the 2nd student with the new description.

Expected outcome:

* A success message will be displayed in the result box confirming that the lesson plan has been updated.
* The updated entry will appear in the student's lesson plan list, viewable using `viewlessons`.
* ![Edit Plan Success](images/editPlanSuccess.png)

### Edit Lesson Progress : `editprogress`

Updates an existing lesson progress entry for a student.

Format: `editprogress INDEX pr/Date|Description`

* Updates an existing lesson progress entry for the student at the specified INDEX.
* **The input `DATE` must match an existing progress entry for the student.**<br>
  If no progress entry or more than one progress entry is found for the specified date, an error message will be shown.
* `DESCRIPTION` is the new description for the lesson progress, which will overwrite the old entry for that date.

> **Tip:**
> Use `editprogress` to correct mistakes or add details to a past lesson's entry. Use `viewlessons` first to see which dates have entries you can edit.

Examples:

* `editprogress 1 pr/2025-10-21|Also covered simultaneous equations`
  Updates the progress entry from 21 Oct 2025 for the 1st student with the new description.

* `editprogress 2 pr/2025-10-15|Final review of essay complete`
  Updates the progress entry from 15 Oct 2025 for the 2nd student with the new description.

Expected outcome:

* A success message will be displayed in the result box confirming that the lesson progress has been updated.
* The updated entry will appear in the student's lesson progress list, viewable using `viewlessons`.
* ![Edit Progress Success](images/editProgressSuccess.png)

### Deleting lesson progress : `deleteprogress`

Deletes a lesson progress entry for a specific student on a given date.

Format: `deleteprogress INDEX DATE`

* Deletes the lesson progress for the student at the specified `INDEX` on the specified `DATE`.
* **The lesson progress on that date must exist.** If no lesson progress exists on that date, an error message will be shown.

Examples:

* `deleteprogress 1 2025-10-21` deletes the lesson progress for the 1st student on 21 Oct 2025.
* `deleteprogress 2 2025-10-15` deletes the lesson progress for the 2nd student on 15 Oct 2025.

### Viewing Lesson Window : `viewlessons`

Shows the lesson progress history for a specific student in a separate window.

Format: `viewlessons INDEX`

* Views the lesson plan and progress of the student at the specified `INDEX`.
* Opens a new window displaying all recorded lesson progress entries.
* Each entry shows the date and progress description.
* Entries can be sort by Date

> **Tip:**
> 1. Add lesson plan using the `addplan` command before viewing<br>
> 2. Add lesson progress using the `addprogress` command before viewing.

Examples:
* `list` followed by `viewlessons 1` opens a window showing the lesson window for the 1st student.
* `find John` followed by `viewlessons 1` shows the lesson window for the 1st person in the filtered results.

Expected output:<br>
![View Lessons Window](images/viewlessonsWindow.png)
* A new window titled "Lessons" will appear.
* The window contains a table with three columns: **Date**, **Lesson Plan** and **Lesson Progress**.
* If the student has no lesson plan or lesson progress recorded, an empty table is shown.

### Clearing all entries : `clear`

Clears all entries from the address book.

Format: `clear`

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Saving the data

TutorTrack data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

TutorTrack data are saved automatically as a JSON file `[JAR file location]/data/tutortrack.json`. Advanced users are welcome to update data directly by editing that data file.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
If your changes to the data file makes its format invalid, TutorTrack will discard all data and start with an empty data file at the next run. Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause the TutorTrack to behave in unexpected ways (e.g., if a value entered is outside of the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</div>

### Archiving data files `[coming in v2.0]`

_Details coming soon ..._

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous TutorTrack home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action | Format, Examples
--------|------------------
**Add** | `add n/NAME p/PHONE_NUMBER s/SUBJECTLEVEL d/DAYTIME c/COST a/ADDRESS [t/TAG]…​` <br> e.g., `add n/James Ho p/22224444 a/123, Clementi Rd, 1234665 t/friend t/colleague`
**Clear** | `clear`
**Delete** | `delete INDEX`<br> e.g., `delete 3`
**Edit** | `edit INDEX [n/NAME] [p/PHONE_NUMBER] [s/SUBJECTLEVEL] [d/DAYTIME] [c/COST] [a/ADDRESS] [t/TAG]…​`<br> e.g.,`edit 2 n/James Lee`
**Find** | `find KEYWORD [MORE_KEYWORDS]` (by name prefix) <br> `find s/SUBJECT_LEVEL` (by subject level) <br> `find t/TAG_KEYWORD [MORE_TAG_KEYWORDS]` (by tag) <br> `find d/DAY` (by lesson day, sorted by time) <br> e.g., `find Jo` (matches John, Joseph), `find s/P4-Math`, `find t/friends`, `find d/Monday`
**List** | `list`
**Help** | `help`
**Add plan** | `addplan INDEX Date Description`<br> e.g., `addplan 1 ll/2025-10-21\|Introduce essay writing skills`
**Edit plan** | `editplan INDEX lpl/DATE\|NEW_PLAN`<br> e.g., `editplan 1 lpl/2025-10-21\|Review essay writing and grammar`
**Delete plan** | `deleteplan INDEX DATE`<br> e.g., `deleteplan 1 2025-10-21`
**Add progress** | `addprogress INDEX Date Description`<br> e.g., `addprogress 1 lp/2025-10-21\|Introduced new algebra concepts`
**Edit progress** | `editprogress INDEX lp/DATE\|NEW_PROGRESS`<br> e.g., `editprogress 1 lp/2025-10-21\|Completed algebra concepts`
**Delete progress** | `deleteprogress INDEX DATE`<br> e.g., `deleteprogress 1 2025-10-21`
**View Lessons** | `viewlessons INDEX`<br> e.g., `viewlessons 1`
