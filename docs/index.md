---
layout: page
title: TutorTrack
---

[![CI Status](https://github.com/AY2526S1-CS2103T-W11-4/tp/workflows/Java%20CI/badge.svg)](https://github.com/AY2526S1-CS2103T-W11-4/tp/actions)
[![codecov](https://codecov.io/gh/AY2526S1-CS2103T-W11-4/tp/branch/master/graph/badge.svg)](https://codecov.io/gh/AY2526S1-CS2103T-W11-4/tp)

![Ui](images/Ui.png)

**TutorTrack is a desktop application designed for private tutors to efficiently manage their students' lesson plans, progress tracking, and contact information.** Built specifically for tutors handling many students, TutorTrack streamlines administrative tasks through a powerful Command Line Interface (CLI) while maintaining a user-friendly Graphical User Interface (GUI) for visual feedback.

## Why TutorTrack?

Private tutors often struggle with tracking multiple students' progress, managing lesson plans, and preparing updates for parents. TutorTrack solves these problems by providing:

* **Centralised Student Management** - Store student contacts, subject levels, lesson times, and costs in one place
* **Lesson Planning** - Create and manage lesson plans for each student with multi-line formatting support
* **Progress Tracking** - Record what was covered in each lesson with detailed notes
* **Unified Lesson View** - See all lesson plans and progress for a student side-by-side in a dedicated window
* **Smart Search** - Find students by name, subject, tags, or lesson day
* **Fast CLI Operations** - Execute commands quickly with keyboard shortcuts for power users

## Getting Started

* **New Users**: Head over to the [_Quick Start_ section of the **User Guide**](UserGuide.html#quick-start) to get TutorTrack up and running in minutes.
* **Developers**: The [**Developer Guide**](DeveloperGuide.html) provides comprehensive documentation on TutorTrack's architecture and implementation.

## Key Features

* Add and manage student profiles with contact details, subject levels, and lesson schedules
* Create lesson plans with multi-line formatting using special characters (`\n`, `\t`, `\\`)
* Track lesson progress with detailed notes on what was covered
* View all lessons for a student in a dedicated window with date-sorted entries
* Search and filter students by various criteria (name, subject, tags, lesson day)
* Automatic data saving - your changes are preserved instantly

## Acknowledgements

* This project is based on the AddressBook-Level3 project created by the [SE-EDU initiative](https://se-education.org).
* Libraries used: [JavaFX](https://openjfx.io/), [Jackson](https://github.com/FasterXML/jackson), [JUnit5](https://github.com/junit-team/junit5)
