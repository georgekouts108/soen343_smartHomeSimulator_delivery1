package house;
import sample.*;
import utilities.*;

public class UserProfile {

    private static int PROFILE_ID = 1;

    private String type; // 'parent', 'child', 'guest', or 'stranger'
    private boolean loggedIn;
    private int profileID;
    private Room currentLocation;
    private int numberOfTimesHyperlinkClicked;
    private boolean isAway;

    /**
     * UserProfile constructor
     * @param type
     *
     */
    public UserProfile(String type) {
        this.type = type;
        this.loggedIn = false;
        this.profileID = (PROFILE_ID++);
        this.numberOfTimesHyperlinkClicked = 0;
        this.isAway = false;
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
