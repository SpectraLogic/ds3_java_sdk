## Setup

In order to get started developing with the ds3_java_sdk you need to make sure that you have completed the following:

* Install the latest version of the [Java JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) **NOTE:** Make sure to select the correct version for your platform
* Install the latest version of [Git](http://git-scm.com/)
* Download and extract the [Eclipse IDE](https://www.eclipse.org/downloads/).  Download `Eclipse IDE for Java Developers`, it should be the first download option
* Once Eclipse has been extracted to a directory of your choosing, navigate to that directory and start Eclipse.  On Windows you will see `eclipse.exe`
* Accept either the default workspace directory, or select one of your choosing.
* Once eclipse finishing loading, navigate to `Help -> Eclipse Marketplace...`.  In the dialog that pops up in the Search Tab, type in 'Gradle' and hit Enter (or Click `Go`)
* In the results that are returned install `Gralde Integration for Eclipse (4.4) 3.6.2.RELEASE`.  Clicking `Install` will take you to the 'Confirm Selected Features' dialog.  In this dialog de-select the 2 Spring optional checkboxes.  `Gradle IDE` and `org.gradle.toolingapi.feature` should both be selected.  Accept any dialogs that popup.
* After Gradle has been installed into eclipse and git has been installed, you should be able to clone the repo and import that project into eclipse
