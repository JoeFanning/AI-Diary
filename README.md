# AI-Diary

[![Platform](https://shields.io)](https://android.com)
[![Language](https://shields.io)](https://java.com)
[![Year](https://shields.io)]()

An Android-based digital diary application that implements artificial intelligence concepts to psychologically analyze user diary entries. 

This project was originally developed in **2014** as a **University Project** to explore the intersection of mobile software development, natural language processing, and psychological sentiment tracking.

---

## 📌 Project Overview

AI-Diary goes beyond a traditional daily journal. By evaluating the text written by the user, the built-in AI components attempt to decode the underlying emotional states and psychological tone of the entries. This allows users to track their mental well-being and emotional trends over time.

For a comprehensive breakdown of the application architecture, design choices, and algorithmic logic, please refer to the included documentation file:
📄 **`AI Diary Technical Report.docx`**

---

## 🛠 Features

* **Digital Journaling:** A secure, local interface to write, edit, and save daily diary entries.
* **Psychological Analysis:** AI-driven text analysis to extract emotional indicators and psychological states from entry text.
* **Historical Tracking:** Retain and view past entries to see changes in writing patterns and mood over time.

---

## 💻 Tech Stack

* **Language:** 100% Java
* **Platform:** Android SDK
* **Build System:** Gradle (utilizing Gradle Wrapper)

---

## 🚀 Getting Started (Legacy Setup)

> [!NOTE]  
> This is a legacy archive project from 2014. Building the project today requires compatibility with older Android SDKs and Gradle versions.

### Prerequisites
* **Android Studio** (Koala or newer, though an older version may match the legacy Gradle wrapper better)
* **Java Development Kit (JDK)** (Version matching 2014-era Android development, typically JDK 7 or 8)

### Installation
1. Clone the repository to your local machine:
   ```bash
   git clone https://github.com
   ```
2. Open **Android Studio**.
3. Select **File > Open...** and navigate to the cloned project folder.
4. Trust the project and allow Android Studio to sync the project with its Gradle files.
5. (Optional) Update the Gradle wrapper and Android Gradle Plugin (AGP) if prompted, or use a legacy virtual device to run the app in its native environment.

---

## 📂 Repository Structure

```text
├── app/                  # Main Android application module (Source code & resources)
├── gradle/wrapper/       # Gradle wrapper files for build environment consistency
├── .gitignore            # Git ignore configurations
├── AI Diary Technical Report.docx  # Full university documentation and architecture breakdown
├── build.gradle          # Top-level build configuration
├── gradlew               # Gradle wrapper script for Unix
├── gradlew.bat           # Gradle wrapper script for Windows
└── settings.gradle       # Gradle project settings
```

---

## 📄 License and Context

This repository is maintained as a historical archive of a university project completed in 2014. All code, documentation, and logic belong to the original author. Please review university guidelines regarding plagiarism before using any portion of this code for academic assignments.
