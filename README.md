Android Activity Recognition API Practice
========================================

Here you will find a functional project for Eclipse and Android Studio demonstrating Android's Activity Recognition APIs.

Setup
-----
0. Choose your development environment, either Eclipse or Android Studio (IntelliJ) would be ok.

1. Make sure you have Google Play Services SDK [correctly installed](http://developer.android.com/google/play-services/setup.html).

2. Enjoy!

Practice
--------

Right now this app is already configured to request and receive activity recognition updates, but it's doing nothing with each update.
Here is where you come in, complete all of the following features:

1. **Add notifications**: Every time the user starts a new activity show a nice system notification.
2. **Be more efficient**: Slow down the updates while the user keeps doing the same thing, for example, if you receive the same detection 5 times in a row, double the requested lapse. 
**Remember to decrease the lapse when the detection changes!**.
3. **Add something else**, something different that you think is a nice addition to the appication.

Notes
-----
0. Feel free to navigate the entire project's code, you may find some interesting functions and tools already developed for you.
1. You can learn more about Activity Recognition APIs [here](http://developer.android.com/training/location/activity-recognition.html).
2. If testing with the emulator, **be sure** to create a AVD with target set to `Google APIs (Level 18)`.
3. If you find a bug, please let me know, or even better submit a pull request with the fix.
