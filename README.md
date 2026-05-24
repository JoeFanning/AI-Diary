# 📔 AI-Diary | University Final Year Project (2014)

[![Platform](https://shields.io)](https://android.com)
[![Language](https://shields.io)](https://java.com)
[![AI Engine](https://shields.io)](https://ibm.com)

An Android-based digital diary application developed in **2014** as a University Final Year Project. It implements cloud-based cognitive computing concepts to psychologically analyze user journal entries and track long-term mental well-being.

---

## 📌 Project Overview
AI-Diary goes beyond a traditional daily journal. By evaluating the text written by the user, the application connects to **IBM Watson's Cloud Cognitive Services** to decode underlying emotional states, sentiment, and psychological tones. This allows users to track their mental well-being and emotional trends over time using Natural Language Processing (NLP).

> 📄 **Technical Report Available:** For a comprehensive breakdown of the application architecture, IBM Watson integration details, and algorithmic logic, please refer to the included file: `AI Diary Technical Report.docx`

---

## 🛠 Features
* 📝 **Digital Journaling:** A secure, local mobile interface to write, edit, and save daily diary entries.
* 🧠 **IBM Watson AI Analysis:** Real-time sentiment and psychological analysis leveraging Watson's Developer Cloud APIs.
* 📈 **Historical Tracking:** Retain and view past entries to visualize changes in writing patterns and mood trends over time.

---

## 💻 Tech Stack
* **Language:** 100% Java
* **Platform:** Android SDK
* **AI/NLP Engine:** IBM Watson Developer Cloud SDK *(Legacy Personality Insights / Natural Language Understanding APIs)*
* **Build System:** Gradle *(utilizing Gradle Wrapper)*

---

## 🚀 Getting Started (Legacy Setup)

> ⚠️ **Note on Legacy Archives:** This is a vintage project from 2014. Building the project today requires strict compatibility with older Android SDK variants, legacy IBM Watson SDK distributions, and older Gradle environments.

### 📋 Prerequisites
* **Android Studio:** Koala or newer *(Note: Older legacy versions will match the historical Gradle wrapper configuration cleaner).*
* **Java Development Kit (JDK):** Version 7 or 8 *(Required to match 2014-era Android development constraints).*
* **IBM Cloud Account:** Required to obtain connection credentials for active/legacy Watson text-analysis services.

### 🔧 Installation & Configuration
1. **Clone the Repository:**
   ```bash
   git clone https://github.com
   ```
2. **Open in IDE:** Open Android Studio, select `File > Open...`, and choose the cloned project folder.
3. **Sync Project:** Trust the project and allow Android Studio to sync layout dependencies via the local Gradle files.
4. **Configure API Credentials:** Locate the Watson instantiation block within the Java source code (`app/src/main/...`) and insert your unique IBM Watson service credentials.

---

## 📂 Repository Structure
```text
├── app/                            # Main Android application module (Source & Layout resources)
├── gradle/wrapper/                 # Gradle wrapper system files for environment consistency
├── .gitignore                      # Git tracking file exclusion configurations
├── AI Diary Technical Report.docx    # Full university documentation and Watson architecture breakdown
├── build.gradle                    # Top-level application build configuration
├── gradlew                         # Gradle wrapper executable script for Unix/Linux systems
├── gradlew.bat                     # Gradle wrapper execution script for Windows environments
└── settings.gradle                 # Gradle cross-module project settings
```

---

## 📄 License and Context
This repository is maintained purely as a historical archive of an academic university project completed in 2014. All code, documentation, and logic elements belong to the original author. 

⚠️ **Academic Integrity Notice:** Please check and review your specific university guidelines regarding plagiarism before using or referencing any portion of this source code for active academic assignments.

