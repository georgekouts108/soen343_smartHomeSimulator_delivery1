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
    }

    public int getProfileID() {
        return profileID;
    }

    public void setProfileID(int profileID) {
        this.profileID = profileID;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public boolean isLoggedIn() {
        return loggedIn;
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
