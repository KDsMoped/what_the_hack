## What_the_Hack

### [Public Page](https://hendrik-schulte.github.io/what_the_hack/)

### Building

### Requirements
- Android Studio
- Android SDK
- - JDK 1.8

### How to build

- Clone the git repo
- Switch to release branch for a stable build
- In Android Studio select `Open an existing Android Studio Project`
- Select your `serious-gaming-mmi/project` folder
- The build system gradle now processes all dependencies
- For a desktop build create a new `Run/Debug` configuration:
    + Main class: `de.hsd.hacking.desktop.DesktopLauncher`
    + Working directory: `serious-gaming-mmi/project/android/assets`
    + Use classpath of module: `desktop`
    + Add new Gradle Task before `Make`
        * Gradle project: `project`
        * Tasks: `texturePacker`
    + If you're using the master branch setup another Gradle Task before `Make` to compile the protobuf file
        * Gradle project: `project:core`
        * Tasks: `generateCoreProto`
- For an android build setup accordingly
- Hit run and enjoy!