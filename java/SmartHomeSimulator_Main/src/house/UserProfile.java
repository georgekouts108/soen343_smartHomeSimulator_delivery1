package house;
import sample.*;
import utilities.*;

/**
 * User profile class
 */
public class UserProfile {

    private static int PROFILE_ID = 1;
    private String type; // 'parent', 'child', 'guest', or 'stranger'
    private boolean loggedIn;
    private int profileID;
    private Room currentLocation;
    private int TimesHyperlinkClicked;
    private boolean isAway;

    //permission booleans to allow interacting with devices
    //first in a pair is to allow at all times, second permission allows only if you are in the same location (room)
    private boolean permLights;
    private boolean permLightsLocation;

    private boolean permDoors;
    private boolean permDoorsLocation;

    private boolean permWindows;
    private boolean permWindowsLocation;

    private boolean permAC;
    private boolean permACLocation;

    private boolean permSetAway; // pSA
    private boolean permCreateZone; // pCZ
    private boolean permRoomTemp; // pRT
    private boolean permSeasonWeather; // pSW
    /**TODO:
     *  - create a boolean "permSetAway" (permission to turn on Away mode in SHP)
     *  - create a boolean "permCreateZone" (permission to create a new zone in SHH)
     *  - create a boolean "permRoomTemp" (permission to override temperature in specific rooms in SHH)
     *  - create a boolean "permSeasonWeather" (permission to set the default temp for Winter and Summer for away mode in SHH)
     *  - create only getter methods for each of these, and initialize the permissions in the constructor
     *  */

    /**
     * UserProfile constructor
     * @param type
     *
     */
    public UserProfile(String type, boolean pL, boolean pLL, boolean pD,
                       boolean pDL, boolean pW, boolean pWL, boolean pAC,
                       boolean pACL, boolean pSA, boolean pCZ, boolean pRT, boolean pSW) {
        this.type = type;
        this.loggedIn = false;
        this.profileID = (PROFILE_ID++);
        this.TimesHyperlinkClicked = 0;
        this.isAway = false;

        this.permLights = pL;
        this.permLightsLocation = pLL;
        this.permDoors = pD;
        this.permDoorsLocation = pDL;
        this.permWindows = pW;
        this.permWindowsLocation = pWL;
        this.permAC = pAC;
        this.permACLocation = pACL;

        this.permSetAway = pSA;
        this.permCreateZone = pCZ;
        this.permRoomTemp = pRT;
        this.permSeasonWeather = pSW;
    }

    /**
     * return a user permission to open the lights
     * @return
     */
    public boolean getPermLights(){
        return permLights;
    }

    /**
     * return a user permission to open the lights only in its current location
     * @return
     */
    public boolean getPermLightsLocation(){
        return permLightsLocation;
    }

    /**
     * return a user permission to open the doors
     * @return
     */
    public boolean getPermDoors(){
        return permDoors;
    }

    /**
     * return a user permission to open the doors only in its current location
     * @return
     */
    public boolean getPermDoorsLocation(){
        return permDoorsLocation;
    }

    /**
     * return a user permission to open the windows
     * @return
     */
    public boolean getPermWindows(){
        return permWindows;
    }

    /**
     * return a user permission to open the windows only in its current location
     * @return
     */
    public boolean getPermWindowsLocation(){
        return permWindowsLocation;
    }

    /**
     * return a user permission to open the AC
     * @return
     */
    public boolean getPermAC(){
        return permAC;
    }

    /**
     * return a user permission to open the AC only in its current location
     * @return
     */
    public boolean getPermACLocation(){
        return permACLocation;
    }

    public boolean getPermSetAway() {
        return permSetAway;
    }

    public boolean getPermRoomTemp() {
        return permRoomTemp;
    }

    public boolean getPermSeasonWeather() {
        return permSeasonWeather;
    }

    public boolean getPermCreateZone() {
        return permCreateZone;
    }

    /**
     * Check to see if a user is Away from home
     * @return
     */
    public boolean isAway() {
        return isAway;
    }

    /**
     * Set the Away mode for a user
     * @param away
     */
    public void setAway(boolean away) {
        isAway = away;
    }

    /**
     * Return the count for profile ID's
     * @return
     */
    public static int getstaticProfileId() {
        return PROFILE_ID;
    }

    /**
     * Mutate the count for profile ID's
     * @param profileId
     */
    public static void setstaticProfileId(int profileId) {
        PROFILE_ID = profileId;
    }

    /**
     * Return the number of times a profile link was clicked
     * @return
     */
    public int getTimesHyperlinkClicked() {
        return TimesHyperlinkClicked;
    }

    /**
     * Update the number of times a profile link was clicked
     * @param numberOfTimesHyperlinkClicked
     */
    public void setTimesHyperlinkClicked(int numberOfTimesHyperlinkClicked) {
        this.TimesHyperlinkClicked = numberOfTimesHyperlinkClicked;
    }

    /**
     * Return a room's profile ID
     * @return
     */
    public int getProfileID() {
        return this.profileID;
    }

    /**
     * Mutate a room's profile ID
     * @param profileID
     */
    public void setProfileID(int profileID) {
        this.profileID = profileID;
    }

    /**
     * Return a profile's type (Parent, Guest, Child, or Stranger)
     * @return
     */
    public String getType() {
        return this.type;
    }

    /**
     * Change a profile's type
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Return a boolean indicating if a profile is currently logged in
     * @return
     */
    public boolean isLoggedIn() {
        return this.loggedIn;
    }

    /**
     * Update the boolean indicating if a user is logged in
     * @param loggedIn
     */
    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    /**
     * Return the Room object where a profile is currently located
     * @return
     */
    public Room getCurrentLocation() {
        return currentLocation;
    }

    /**
     * Update the Room where a profile is currently located
     * @param currentLocation
     */
    public void setCurrentLocation(Room currentLocation) {
        this.currentLocation = currentLocation;
    }
}
