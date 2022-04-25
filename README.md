# CSE-3311-Spring-2022
This is our repository for Team 10 in CSE 3311-002 Spring 2022 for Christoph Csallner

# Project Description - UTA Reviews
Our app (UTAReviews) plans to provide current and future students (future students as in students who attend the university in the future but not currently) of the University of Texas at Arlington (UTA) a tool that allows detailed reviews and discussions of professors at UTA. An issue with other tools such as RateMyProfessor and Reddit is that RateMyProfessor does not allow for detailed reviews and discussions, the issue with Reddit is that it is not centralized. We hope to bring the ability for students to leave extensive reviews and allow them to discuss amongst each other, rate each other's reviews or comments, and review the professors being reviewed, and have it centralized to UTA specifically.

There are currently only plans to make this application through Android development with Android Studio. We are looking at possibly making a website to allow more users the opportunity to use our application but for the time being, we only plan Android development.

# Vision Statement:
To help nourish quality education among current and future students at the University of Texas at Arlington through open student and teacher discussions. We want to be successful in helping students avoid situations that can lead to stress, depression, and failure through poor choices of professors or class combinations while giving professors a medium to communicate and take feedback from reviewers.

# Compile Instructions
1. Download project into Android Studio
2. Make sure the right Android SDKs are selected under File -> Settings -> Appearance & Behavior -> System Settings -> Android SDK
   Check all that are listed below:
   1. Android API 31                                  <-- Under SDK Platforms
   2. Android 11.0 (R)                                <-- Under SDK Platforms
   3. Android SDK Build-Tools 33-rc1                  <-- Under SDK Tools
   4. Android SDK Command-line Tools (latest)         <-- Under SDK Tools
   5. Android SDK Platform-tools                      <-- Under SDK Tools
   6. Intel x86 Emulator Accelerator (HAXM installer) <-- Under SDK Tools
3. Start run in Android Studio (shift + F10)
   1. If you are getting errors, it may be because of Gradle issues, a fix to this could be by going to File -> Invalidate Caches / Restart.
   2. Once there, click the Invalidate and Restart Button, allow Android Studio to restart and then try running the app again.
