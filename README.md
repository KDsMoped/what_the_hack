# Requirements
- Android Studio

# How to build the release branch

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
- For an android build setup accordingly
- Hit run and enjoy!