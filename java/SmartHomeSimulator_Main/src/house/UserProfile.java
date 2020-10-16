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

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public UserProfile(String type, boolean isAdmin) {
        this.type = type;
        this.loggedIn = false;
        this.profileID = (PROFILE_ID++);
        this.isAdmin = isAdmin;
        this.numberOfTimesHyperlinkClicked = 0;
    }

    public static int getstaticProfileId() {
        return PROFILE_ID;
    }

    public static void setstaticProfileId(int profileId) {
        PROFILE_ID = profileId;
    }

    public int getNumberOfTimesHyperlinkClicked() {
        return numberOfTimesHyperlinkClicked;
    }

    public void setNumberOfTimesHyperlinkClicked(int numberOfTimesHyperlinkClicked) {
        this.numberOfTimesHyperlinkClicked = numberOfTimesHyperlinkClicked;
    }

    public int getProfileID() {
        return this.profileID;
    }

    public void setProfileID(int profileID) {
        this.profileID = profileID;
    }

    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public boolean isLoggedIn() {
        return this.loggedIn;
    }
    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public Room getCurrentLocation() {
        return currentLocation;
    }
    public void setCurrentLocation(Room currentLocation) {
        this.currentLocation = currentLocation;
    }

    /**TODO: depending on the type of profile, set the appropriate permissions
     * TODO (probably NOT for Delivery #1 ??) */
    public void setPermissions() {
        if (this.getType().equals("Parent")) {

        }
        else if (this.getType().equals("Child")) {

        }
        else if (this.getType().equals("Guest")) {

        }
        else {

        }
    }
}
