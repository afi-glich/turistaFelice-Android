# TuristaFelice — Android Travel Planner

TuristaFelice is an Android application written in Java for discovering nearby points of interest and organizing trips.

The project combines location-aware features, external REST APIs, local user preferences, and a multi-screen Android UI. I keep it in my portfolio as an example of a complete Java application outside the backend domain.

## Main features

- discover nearby points of interest using the device location;
- display places on Google Maps;
- filter places by category such as museums, parks, restaurants and transport;
- retrieve additional place information from Wikipedia;
- create and organize trips with destination, dates and points of interest;
- maintain favourites and a trip timeline;
- manage basic profile information locally.

## Tech stack

- Java 8
- Android SDK / AndroidX
- Google Maps SDK
- Google Places API
- Retrofit 2 + Gson
- Android Architecture Components
  - ViewModel
  - LiveData
  - Navigation Component
- SharedPreferences
- Picasso
- Gradle

## Project structure

The Android project is located in `TuristaFeliceTest/`.

```text
app/src/main/java/com/marco/turistafelicetest/
├── models/          Domain and API response models
├── repositories/    Data access and API orchestration
├── Wikipedia/       Wikipedia API integration
├── planning/        Trip creation and planning flow
├── ui/
│   ├── around/      Nearby places and map
│   ├── profile/     Profile and favourites
│   └── timeline/    Saved trips and trip details
└── utils/           Constants and API interfaces
```

The application uses a repository layer for external data access and ViewModels/LiveData to connect data with the UI.

## External services

The project integrates with:

- Google Maps;
- Google Places;
- Wikipedia APIs.

API keys are intentionally not stored in the repository. To run the application, provide your own Google Maps/Places credentials in the appropriate configuration placeholders.

## Local data

User preferences, favourites and planning data are stored locally with Android `SharedPreferences`.

## Build notes

This is a historical Android project targeting **Android SDK 29** and using dependency versions from the period in which it was developed. Modern Android Studio versions may require Gradle or dependency updates before the project can be built.

The Gradle project can be found under:

```text
TuristaFeliceTest/
```

