# Digital Sticker Album

An Android application for collecting, viewing, and managing a digital football sticker collection for the 2026 World Cup.

This project was developed as an individual university project for the **Mobile Application Development** course at the **University of Sarajevo – Faculty of Science**.

## Features

- Browse a collection of digital stickers
- View detailed information for individual stickers
- Search, filter, and sort stickers
- Save stickers as favorites
- Open sticker packs and add new stickers to the collection
- Offline access through local data caching
- View collection statistics and progress
- Share sticker information through other Android applications
- Quiz and coin system
- Shake-to-open sticker pack interaction using the device accelerometer

## Technologies

- **Kotlin**
- **Jetpack Compose**
- **MVVM architecture**
- **Retrofit**
- **Room Database**
- **Kotlin Coroutines**
- **Flow**
- **Navigation for Compose**
- **DataStore**
- **Material 3**

## Architecture

The application follows the **MVVM (Model-View-ViewModel)** architecture.

The project separates responsibilities between:

- UI screens built with Jetpack Compose
- ViewModels for UI state and application logic
- Repository layer as the main data source
- Retrofit for communication with the external API
- Room for local persistence and offline access

## API and Offline Support

The application integrates an external REST API provided for the university project.

Data retrieved from the API is stored locally using Room, allowing previously synchronized data to remain available when the device is offline.

> Note: Some online functionality depends on the availability of the external course API.

## Project Structure

```text
app/src/main/java/com/example/stickeralbum/
├── data/
├── model/
├── navigation/
├── network/
└── ui/
