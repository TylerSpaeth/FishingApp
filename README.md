# FishingApp

This desktop app provides fly fishermen with the ability to log their catches and gain insight on their fishing success.

## Description

This project is a desktop application designed for people who like to fly fish. The focus point of the application is the catchlog and insertion form that take up 
the majority of the screen. This is how one can create and view information about their catches. A new catch is made up of a few components:  
- Fly type     
- Fly size  
- Water conditions   
- Weather conditions   
- Location on the body of water   
- Date   
- Time (rounded to the nearest hour).
    
Below the log, there are a variety of buttons for users to choose from.
- They can select an item from the catchlog and delete it with the delete button.
- They can choose from one of the sorting methods to reorganize the catchlog
- They can view what features of a catch they have been most successful with by selecting the appropriate button.
- They can enter their zipcode to get a personalized fly recommendation based on the weather conditions in their location.
   
It is worth noting that an internet connection is necessary to use the zipcode recommendation feature.

### Tech Used
- Java
- JavaFX
- SQLite
- [WeatherAPI.com](WeatherAPI.com)
- Google Cloud Functions

## Getting Started

### Dependencies

- JDK 17 is required to run the app.

### Installing

The projects has three options for download:  
 1. Navigate to the releases section and download the latest jar file. It contains all dependencies minus the JDK.
 2. Download the source code from the most recent release.
 3. Clone the repository (Note that while this should always work, functionality may not be as polished as releases).

### Executing program

<b>This app utilizes an embedded SQLite database, and execution of the program generates a .db file. If you are executing via a jar file, it is reccomended to place 
the jar file in a location where you would like the .db file to reside. They must remain in the same directory.</b>

If you downloaded the jar file:
```
java -jar fishingapp.jar
```
- Alternatively, you can click on the jar file to open it.  
  
If you downloaded the source code:
- While any build tools can be used, Maven is reccomended as the project includes a pom.xml file.
- If you choose to use Maven the following command can be used:
```
// To build the program
mvn compile package
```
- When packaging with this method, the jar file will be located in the shade folder.
- The jar can then be run the same is if you downloaded it from the releases.

## Authors

Tyler Spaeth  
tspaeth58@gmail.com

## License

This project is licensed under the MIT License - see the LICENSE.md file for details
