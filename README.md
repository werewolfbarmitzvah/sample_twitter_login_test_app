Sample Android application that redirects the user to a twitter login page

The app uses Espresso for testing. See https://developer.android.com/training/testing/espresso/

The main benefit of Espresso is it's deeply integrated with the code under test and the Android OS.
This means you can (mostly) test things other frameworks cannot and deeply tied into the CI flow
for application development.

Run the tests with:

gradle connectedAndroidTest