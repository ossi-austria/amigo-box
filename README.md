# Setup

* Install Android Studio 4.1.2
* For Windows: install Putty + Putty Keygen
* create a new key (with passphrase) and add to gitlab.com/-/profile/keys

## Setup local env

Add the API_ENDPOINT pointing to your local development server:

~/.gradle/gradle.properties


# Tutorials

## Domain Data & Persistence

To understand the persistence, a certain amount of lightSQL, domain-driven-design principles and object-orientated
architecture will be needed.

We use three major technologies:
- apis/fetchers := retrofit, for fetching data from http webservices
- daqs/data access objects := room db, for storing data on device
- repositories := store4, to conclude fetchers and daos together

We use mostly Room to store our model objects into the local Sqlite database

Read the following for catching up:

- https://developer.android.com/training/data-storage/room/accessing-data
- https://developer.android.com/training/data-storage/room/relationships?authuser=1
- https://square.github.io/retrofit/

## Navigation and Fragments

We use a single-activity-architecture

ONE MainActivity connects all Fragments and viewModels

- https://oozou.com/blog/reasons-to-use-android-single-activity-architecture-with-navigation-component-36
- https://developer.android.com/guide/fragments/communicate#kotlin

## Architecture

Some things to remember:

### Understanding principles of clean architecture

* test driven development
* split UI and business logic (Separation of concerns)
* implement testable classes (do not use android components (context, activity) in business logic classes)
* write concise unit test for those components
* put business logic into viewModels and Services
* think of "Clean Architecture" and split classes into their responsibilites and layers: https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html
* understand repositories, dao and apis for a good architecture: https://developer.android.com/jetpack/guide

### ViewModels and LiveData
* https://developer.android.com/topic/libraries/architecture/viewmodel
* https://developer.android.com/topic/libraries/architecture/viewmodel#coroutines
* https://medium.com/androiddevelopers/viewmodels-and-livedata-patterns-antipatterns-21efaef74a54
* https://medium.com/androiddevelopers/lifecycle-aware-data-loading-with-android-architecture-components-f95484159de4

### Coroutines

Coroutines can be difficult in beginning, but you can think of them as lightweight Threads.

* watch out for "suspend" functions and Coroutine scopes
* https://kotlinlang.org/docs/coroutines-overview.html
* https://developer.android.com/kotlin/coroutines
* Testing and couroutines: https://proandroiddev.com/how-to-unit-test-code-with-coroutines-50c1640f6bef

## Testing

* https://developer.android.com/training/testing/fundamentals
* https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-test/
* use Mockk to create mocks (fake classes) to simulate behaviour: https://mockk.io/
* use Mockk with coroutines: https://mockk.io/#coroutines