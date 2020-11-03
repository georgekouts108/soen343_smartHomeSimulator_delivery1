package house;
import sample.*;
import utilities.*;

public class UserProfile {

    private static int PROFILE_ID = 1;

    private String type; // 'parent', 'child', 'guest', or 'stranger'
    private boolean loggedIn;
    private int profileID;
    private Room currentLocation;
    private boolean isAdmin;
    private int numberOfTimesHyperlinkClicked;
    private boolean isAway;

    /**
     * UserProfile constructor
     * @param type
     * @param isAdmin
     */
    public UserProfile(String type, boolean isAdmin) {
        this.type = type;
        this.loggedIn = false;
        this.profileID = (PROFILE_ID++);
        this.isAdmin = isAdmin;
        this.numberOfTimesHyperlinkClicked = 0;
        this.isAway = false;
    }

    public boolean isAway() {
        return isAway;
    }

    public void setAway(boolean away) {
        isAway = away;
    }

    /**
     * Access a boolean indicating if a profile is an administrator
     * @return
     */
    public boolean isAdmin() {
        return isAdmin;
    }

    /**
     * Enable or disable a profile's administrator mode
     * @param admin
     */
    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    /**
     * Return a room's profile ID
     * @return
     */
    public static int getstaticProfileId() {
        return PROFILE_ID;
    }

    /**
     * Mutate a room's profile ID
     * @param profileId
     */
    public static void setstaticProfileId(int profileId) {
        PROFILE_ID = profileId;
    }

    /**
     * Return the number of times a profile link was clicked
     * @return
     */
    public int getNumberOfTimesHyperlinkClicked() {
        return numberOfTimesHyperlinkClicked;
    }

    /**
     * Update the number of times a profile link was clicked
     * @param numberOfTimesHyperlinkClicked
     */
    public void setNumberOfTimesHyperlinkClicked(int numberOfTimesHyperlinkClicked) {
        this.numberOfTimesHyperlinkClicked = numberOfTimesHyperlinkClicked;
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
