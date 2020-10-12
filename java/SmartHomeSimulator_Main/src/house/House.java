package house;
import sample.*;
import java.io.*;

public class House {

    private int numOfRooms;
    private Room[] roomArray;

    /**TODO: create attributes for the small image icons of utilities (i.e. lightbulb, AC, window, etc.)*/
    /**TODO: create an attribute for a house layout 2D drawing */

    public House(String houseLayoutFileName) {

        /**TODO: using File IO, read a plain text (.txt) file called "houseLayoutFileName"
         * TODO: and assign the values accordingly
         *
         * TODO: in the file, the number of rooms should come first (initialize to "numOfRooms"
         * TODO: and set the length of "roomArray" to this value)
         *
         * TODO: from the text file, read the information about each room (name,
         * TODO: number of doors, windows, lights, etc.) and create a new Room object with these attributes
         * TODO: [HINT == use the setter methods in the "Room" class to do this]
         *
         * TODO: put the new Room object into "roomArray" and repeat the process for the rest of the Rooms in the file
         *
         *
         * TODO: after all rooms have been created, create a house layout 2D drawing (this will be dynamically
         * TODO: changed over time during the simulation (example, the appearance of icons in certain rooms, etc.)
         *
         * */
    }

    /**TODO: implement methods for setting and getting the appropriate attributes,
     * TODO: accessing and modifying the house layout view depending on what happens during the simulation
     * TODO: (for example, if a Light is turned on, determine the Room where the light is, and on the
     * TODO: 2D layout make visible a light bulb icon in that Room)
     *
     * TODO: implement methods for updating information about objects around the house
     * TODO: [for example, if a Light is turned on, determine the Room where the light is, and on the
     * TODO: 2D layout make visible a light bulb icon in that Room]
     *
     *
     * TODO: implement a method "outputMessage(String message, TextArea outputConsole)" that will
     * TODO: print "message" to the main dashboard's output console
     * TODO: [HINT == use the "Main" class to access its main_dashboard scene, and further its output console,
     * TODO: to which you will append "message"]
     * TODO: for example, if a Light is turned on, generate a string with the current Local Time and the room where
     * TODO: the light was turned on, and append this string to the outputConsole
     * */


}
