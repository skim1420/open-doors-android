OpenDoors Android client app
==================

OpenDoors is a small project I cooked up to open my pre-war NYC building main door from my mobile phone.

There is a [Raspberry Pi server component](https://github.com/skim1420/open-doors-server) that activates a relay switch to "press the buzzer" on my intercom. It takes signed HTTP requests to enact the switch.

This is the Android client app, which is a single-button app that sends the signed HTTP request. It also has a widget, and I've started experimenting with stuff like geofencing, notifications and Android Wear.

There is also an [iOS client app](https://github.com/skim1420/open-doors-ios).
