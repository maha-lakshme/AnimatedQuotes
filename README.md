# Animated Quotes App

Animated Quotes App is an innovative Android application that fetches and displays inspiring quotes with smooth animations. Built with Kotlin in Android Studio and following the MVVM architectural pattern, the app leverages modern Jetpack components such as **LiveData** (for reactive data updates), **Data Binding** (for efficient UI-to-data connections), and **Retrofit** (for robust network communication). Extensive test suites—including unit tests, integration tests, and Espresso UI tests—ensure a reliable, high-quality user experience.

## Overview

Animated Quotes App delivers motivating and inspiring quotes with eye-catching animations. The app follows the Model-View-ViewModel (MVVM) pattern to clearly separate the UI, business logic, and data handling layers. Modern Android development practices—such as LiveData for UI reactivity, Data Binding to reduce boilerplate code, and Retrofit for asynchronous API calls—ensure that your experience is both smooth and engaging.

## Features

- **Dynamic Animated Quotes:**  
  Visually appealing animations display a series of inspiring quotes to uplift users.

- **Reactive UI Updates:**  
  LiveData automatically refreshes the UI upon data changes, ensuring that the display is always current.

- **Robust Networking:**  
  Retrofit handles all API calls for fetching quotes, providing efficient and error-handled network communication.

- **Efficient Data Binding:**  
  Data Binding creates a direct connection between UI components and data sources, reducing the need for manual updates.

- **Comprehensive Test Coverage:**  
  The project includes unit tests, integration tests, and Espresso UI tests to maintain a high standard of quality and reliability.

## Tech Stack

- **Language:** Kotlin  
- **Development Environment:** Android Studio  
- **Architecture:** MVVM  
- **UI Components:** Data Binding, LiveData, ViewModel, Material Design  
- **Networking:** Retrofit  
- **Testing Frameworks:**
  - **Unit Testing:** JUnit, Mockito/MockK  
  - **Integration Testing:** AndroidX Test framework  
  - **UI Testing:** Espresso  
- **Build Tool:** Gradle

## Architecture

The application is organized using the Model-View-ViewModel (MVVM) pattern:

- **Model:**  
  Contains data models (e.g., a `Quote` data class) and API response objects.

- **View:**  
  Comprises Activities and Fragments that render the UI using Data Binding, allowing seamless connections between the UI components and observable data.

- **ViewModel:**  
  Serves as an intermediary that exposes LiveData to the UI while handling business logic independently from UI components.

- **Repository:**  
  Manages data operations, such as fetching quotes from remote APIs using Retrofit (and caching if needed).

- **Testing:**  
  - **Unit Tests:** Validate ViewModel logic and Repository methods in isolation.  
  - **Integration Tests:** Verify that different components interact correctly together.  
  - **Espresso (UI) Tests:** Automate and confirm that user interactions produce the expected results.

## Screenshots

Below are some screenshots showcasing the Animated Quotes App:

<div align="center">
  <img src="screenshots/screen1.png" alt="Home Screen - Animated Quotes" width="200px" />
  <img src="screenshots/screen2.png" alt="Quote Detail Screen" width="200px" />
  <img src="screenshots/screen3.png" alt="Animation in Action" width="200px" />
</div>

> **Note:**  
> To ensure screenshots display correctly on GitHub:  
> 1. Create a folder named `screenshots` in your repository.  
> 2. Add your screenshot image files (e.g., `screen1.png`, `screen2.png`, etc.) into that folder.  
> 3. Verify that the `<img>` tag paths (e.g., `screenshots/screen1.png`) correctly match the file names in your repository.

## Testing

The app is fully covered by a comprehensive suite of tests:

- **Unit Testing:**  
  Validate core logic within the ViewModels and Repository:
  ```bash
  ./gradlew testDebugUnitTest
  
## Integration Testing: 
Test the interactions between your ViewModel and Repository:

bash
./gradlew connectedDebugAndroidTest
## Espresso UI Testing: Run automated UI tests on an emulator or device:

bash
./gradlew connectedAndroidTest
(Ensure an Android device or emulator is connected before running this command.)

## Continuous Integration
A Continuous Integration (CI) workflow is included via GitHub Actions. The workflow file, located at .github/workflows/ci.yml, automatically runs all tests (unit, integration, and Espresso UI) on every commit or pull request. This ensures every change meets quality and functionality standards.

## License
This project is licensed under the MIT License. See the LICENSE file for further details.

## Contact
Developer: Your Name Email: maha21.kanagaraj@gmail.com GitHub: github.com/maha-lakshme
