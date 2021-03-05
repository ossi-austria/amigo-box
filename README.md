# Setup

* Install Android Studio 4.1.2
* For Windows: install Putty + Putty Keygen

# Tutorials

## Domain Data & Persistance

To understand the persistance, a certain amount of lightSQL, domain-driven-design principles and object-ortientated
architecture will be needed.

We use three major technologies:
- fetchers := retrofit, for fetching http apis
- data access objects := room db, for storing data on device
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