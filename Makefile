#Run the gui version of the program
run: 
	mvn exec:java -Dexec.mainClass="com.tylerspaeth.FXUI" 
#Run the cmd line version of the program
cline:
	mvn exec:java -Dexec.mainClass="com.tylerspaeth.App"


