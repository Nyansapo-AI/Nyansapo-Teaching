# Nyansapo Teaching

Nyansapo Teaching is an Android application designed for educational purposes, focusing on literacy assessments. It allows for conducting assessments and tracking student progress.

## Project Architecture

The project follows the principles of Clean Architecture, separating the codebase into three main layers:

-   **Data Layer**: Responsible for providing data to the application, from sources like a remote API (Firebase) or a local database (SQLDelight). It contains repositories that abstract the data sources from the rest of the app.
-   **Domain Layer**: Contains the core business logic of the application. It consists of use cases (interactors) and domain models. This layer is independent of any other layer.
-   **Presentation Layer**: Responsible for the UI and user interaction. It uses the Model-View-ViewModel (MVVM) pattern. It is built with Jetpack Compose.

## Technologies and Libraries

This project utilizes a variety of modern Android development tools and libraries:

-   **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose) for building the user interface declaratively.
-   **Dependency Injection**: [Koin](https://insert-koin.io/) for managing dependencies throughout the app.
-   **Navigation**: [Jetpack Navigation](https://developer.android.com/guide/navigation) for handling navigation between screens.
-   **Asynchronous Programming**: Kotlin Coroutines for managing background threads.
-   **Networking & Remote Data**:
    -   [Firebase](https://firebase.google.com/) for services like Analytics.
-   **Local Storage**: [SQLDelight](https://cash.app/sqldelight/) for offline data storage.
-   **Image Loading**: [Coil 3](https://coil-kt.github.io/coil/) for loading images.
-   **Animations**: [Lottie](https://airbnb.io/lottie/) for displaying rich animations.
-   **Media Playback**: [Media3](https://developer.android.com/jetpack/androidx/releases/media3) for handling audio and video.
-   **Serialization**: [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization) for parsing JSON.

## File Structure

The project's code is organized into packages based on features and layers:

```
com.nyansapoai.teaching
├── data
│   ├── firebase
│   ├── remote
│   │   ├── assessment
│   │   └── media
│   └── ... (other data sources)
├── di
│   └── AppModules.kt  // Koin dependency injection modules
├── domain
│   ├── models
│   │   └── assessments
│   └── ... (use cases)
└── presentation
    ├── assessments
    │   ├── literacy
    │   │   ├── components
    │   │   ├── result
    │   │   └── LiteracyState.kt
    │   └── ... (other assessment types)
    └── ... (other UI features)

```
