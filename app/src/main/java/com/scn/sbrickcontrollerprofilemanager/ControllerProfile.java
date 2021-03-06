package com.scn.sbrickcontrollerprofilemanager;

import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * SBrick controller profile.
 */
public class ControllerProfile implements Parcelable {

    //
    // Public constants
    //

    public static final String CONTROLLER_ACTION_DPAD_HORIZONTAL = "CONTROLLER_ACTION_DPAD_HORIZONTAL";
    public static final String CONTROLLER_ACTION_DPAD_VERTICAL = "CONTROLLER_ACTION_DPAD_VERTICAL";
    public static final String CONTROLLER_ACTION_LEFT_JOY_HORIZONTAL = "CONTROLLER_ACTION_LEFT_JOY_HORIZONTAL";
    public static final String CONTROLLER_ACTION_LEFT_JOY_VERTICAL = "CONTROLLER_ACTION_LEFT_JOY_VERTICAL";
    public static final String CONTROLLER_ACTION_LEFT_THUMB = "CONTROLLER_ACTION_LEFT_THUMB";
    public static final String CONTROLLER_ACTION_RIGHT_JOY_HORIZONTAL = "CONTROLLER_ACTION_RIGHT_JOY_HORIZONTAL";
    public static final String CONTROLLER_ACTION_RIGHT_JOY_VERTICAL = "CONTROLLER_ACTION_RIGHT_JOY_VERTICAL";
    public static final String CONTROLLER_ACTION_RIGHT_THUMB = "CONTROLLER_ACTION_RIGHT_THUMB";
    public static final String CONTROLLER_ACTION_A = "CONTROLLER_ACTION_A";
    public static final String CONTROLLER_ACTION_B = "CONTROLLER_ACTION_B";
    public static final String CONTROLLER_ACTION_X = "CONTROLLER_ACTION_X";
    public static final String CONTROLLER_ACTION_Y = "CONTROLLER_ACTION_Y";
    public static final String CONTROLLER_ACTION_RIGHT_TRIGGER_BUTTON = "CONTROLLER_ACTION_RIGHT_TRIGGER_BUTTON";
    public static final String CONTROLLER_ACTION_RIGHT_TRIGGER = "CONTROLLER_ACTION_RIGHT_TRIGGER";
    public static final String CONTROLLER_ACTION_LEFT_TRIGGER_BUTTON = "CONTROLLER_ACTION_LEFT_TRIGGER_BUTTON";
    public static final String CONTROLLER_ACTION_LEFT_TRIGGER = "CONTROLLER_ACTION_LEFT_TRIGGER";
    public static final String CONTROLLER_ACTION_START = "CONTROLLER_ACTION_START";
    public static final String CONTROLLER_ACTION_SELECT = "CONTROLLER_ACTION_SELECT";
    public static final String CONTROLLER_ACTION_BUTTON_1 = "CONTROLLER_ACTION_BUTTON_1";
    public static final String CONTROLLER_ACTION_BUTTON_2 = "CONTROLLER_ACTION_BUTTON_2";
    public static final String CONTROLLER_ACTION_BUTTON_3 = "CONTROLLER_ACTION_BUTTON_3";
    public static final String CONTROLLER_ACTION_BUTTON_4 = "CONTROLLER_ACTION_BUTTON_4";
    public static final String CONTROLLER_ACTION_BUTTON_5 = "CONTROLLER_ACTION_BUTTON_5";
    public static final String CONTROLLER_ACTION_BUTTON_6 = "CONTROLLER_ACTION_BUTTON_6";
    public static final String CONTROLLER_ACTION_BUTTON_7 = "CONTROLLER_ACTION_BUTTON_7";
    public static final String CONTROLLER_ACTION_BUTTON_8 = "CONTROLLER_ACTION_BUTTON_8";
    public static final String CONTROLLER_ACTION_BUTTON_9 = "CONTROLLER_ACTION_BUTTON_9";
    public static final String CONTROLLER_ACTION_BUTTON_10 = "CONTROLLER_ACTION_BUTTON_10";
    public static final String CONTROLLER_ACTION_BUTTON_11 = "CONTROLLER_ACTION_BUTTON_11";
    public static final String CONTROLLER_ACTION_BUTTON_12 = "CONTROLLER_ACTION_BUTTON_12";
    public static final String CONTROLLER_ACTION_BUTTON_13 = "CONTROLLER_ACTION_BUTTON_13";
    public static final String CONTROLLER_ACTION_BUTTON_14 = "CONTROLLER_ACTION_BUTTON_14";
    public static final String CONTROLLER_ACTION_BUTTON_15 = "CONTROLLER_ACTION_BUTTON_15";
    public static final String CONTROLLER_ACTION_BUTTON_16 = "CONTROLLER_ACTION_BUTTON_16";

    //
    // Private members
    //

    private static final String TAG = ControllerProfile.class.getSimpleName();

    private static final String ControllerActionIdCountKey = "controller_action_id_count_key";
    private static final String ControllerActionIdKey = "controller_action_id_key";
    private static final String ControllerActionCountKey = "controller_action_count_key";

    private String name;
    private Map<String, Set<ControllerAction>> controllerActionMap = new HashMap();

    //
    // Constructor
    //

    /**
     * Creates a new instance of the ControllerProfile class with a new unique name.
     */
    public ControllerProfile() {
        Log.i(TAG, "ControllerProfile...");

        name = ControllerProfileManagerHolder.getManager().getUniqueProfileName();
        Log.i(TAG, "  name: " + name);
    }

    /**
     * Creates a new instance of the ControllerProfile class.
     * @param name is the name of the profile.
     */
    public ControllerProfile(String name) {
        Log.i(TAG, "ControllerProfile - " + name);
        this.name = name;
    }

    ControllerProfile(SharedPreferences prefs, String profileName) {
        Log.i(TAG, "ControllerProfile from shared preferences - " + profileName);

        name = profileName;

        int controllerActionIdCount = prefs.getInt(profileName + "_" + ControllerActionIdCountKey, 0);
        for (int controllerActionIdIndex = 0; controllerActionIdIndex < controllerActionIdCount; controllerActionIdIndex++) {

            String controllerActionId = prefs.getString(profileName + "_" + ControllerActionIdKey + "_" + controllerActionIdIndex, "");
            Set<ControllerAction> controllerActions = new HashSet<>();
            controllerActionMap.put(controllerActionId, controllerActions);

            int controllerActionCount = prefs.getInt(profileName + "_" + controllerActionId + "_" + ControllerActionCountKey, 0);
            for (int controllerActionIndex = 0; controllerActionIndex < controllerActionCount; controllerActionIndex++) {

                ControllerAction controllerAction = new ControllerAction(prefs, profileName, controllerActionId, controllerActionIndex);
                controllerActions.add(controllerAction);
            }
        }
    }

    ControllerProfile(Parcel parcel) {
        Log.i(TAG, "ControllerProfile from parcel...");

        name = parcel.readString();
        Log.i(TAG, "  name: " + name);

        int controllerActionIdCount = parcel.readInt();
        for (int controllerActionIdIndex = 0; controllerActionIdIndex < controllerActionIdCount; controllerActionIdIndex++) {

            String controllerActionId = parcel.readString();
            Set<ControllerAction> controllerActions = new HashSet<>();
            controllerActionMap.put(controllerActionId, controllerActions);

            int controllerActionCount = parcel.readInt();
            for (int controllerActionIndex = 0; controllerActionIndex < controllerActionCount; controllerActionIndex++) {

                ControllerAction controllerAction = parcel.readParcelable(ControllerAction.class.getClassLoader());
                controllerActions.add(controllerAction);
            }
        }
    }

    //
    // API
    //

    /**
     * Gets the name of the profile.
     * @return the name.
     */
    public String getName() { return name; }

    /**
     * Gets the controller actions specified by its Id.
     * @param controllerActionId
     * @return Set of controller actions.
     */
    public Set<ControllerAction> getControllerActions(String controllerActionId) {

        if (!controllerActionMap.containsKey(controllerActionId))
            return new HashSet<>();

        return controllerActionMap.get(controllerActionId);
    }

    /**
     * Adds the controller action for the specified ID.
     * @param controllerActionId is the controller action ID.
     * @param controllerAction is the controller action object.
     */
    public void addControllerAction(String controllerActionId, ControllerAction controllerAction) {
        Log.i(TAG, "setControllerAction - " + controllerActionId);

        Set<ControllerAction> controllerActions;
        if (controllerActionMap.containsKey(controllerActionId)) {
            controllerActions = controllerActionMap.get(controllerActionId);
        }
        else {
            controllerActions = new HashSet<>();
            controllerActionMap.put(controllerActionId, controllerActions);
        }

        controllerActions.add(controllerAction);
    }

    /**
     * Updates the controller action for the specified controller action id.
     * @param controllerActionId is the controller action id.
     * @param originalControllerAction is the original controller action.
     * @param newControllerAction is the new controller action.
     */
    public void updateControllerAction(String controllerActionId, ControllerAction originalControllerAction, ControllerAction newControllerAction) {
        Log.i(TAG, "updateControllerAction...");

        Set<ControllerAction> controllerActions;
        if (controllerActionMap.containsKey(controllerActionId)) {
            controllerActions = controllerActionMap.get(controllerActionId);
        }
        else {
            controllerActions = new HashSet<>();
            controllerActionMap.put(controllerActionId, controllerActions);
        }

        if (controllerActions.contains(originalControllerAction)) {
            controllerActions.remove(originalControllerAction);
        }

        controllerActions.add(newControllerAction);
    }

    /**
     * Removes the controller action for the specified controller action id.
     * @param controllerActionId is the controller action ID.
     * @param controllerAction is the controller action to remove.
     */
    public void removeControllerAction(String controllerActionId, ControllerAction controllerAction) {
        Log.i(TAG, "removeControllerAction...");

        if (!controllerActionMap.containsKey(controllerActionId))
            return;

        Set<ControllerAction> controllerActions = controllerActionMap.get(controllerActionId);
        controllerActions.remove(controllerAction);

        if (controllerActions.size() == 0)
            controllerActionMap.remove(controllerActionId);
    }

    /**
     * Gets all the SBrick addresses exist in any of the controller actions.
     * @return Collection of SBrick addresses.
     */
    public Collection<String> getSBrickAddresses() {
        Log.i(TAG, "getSBrickAddresses...");

        Set<String> addresses = new HashSet<>();

        for (Set<ControllerAction> controllerActions : controllerActionMap.values())
            for (ControllerAction controllerAction : controllerActions)
                addresses.add(controllerAction.getSBrickAddress());

        return addresses;
    }

    /**
     * Gets the user friendly controller action name.
     * @param controllerActionId
     * @return The controller action name.
     */
    public static String getControllerActionName(String controllerActionId) {
        switch (controllerActionId) {
            case CONTROLLER_ACTION_DPAD_HORIZONTAL: return "Dpad horizontal";
            case CONTROLLER_ACTION_DPAD_VERTICAL: return "Dpad vertical";
            case CONTROLLER_ACTION_LEFT_JOY_HORIZONTAL: return "Left joy horizontal";
            case CONTROLLER_ACTION_LEFT_JOY_VERTICAL: return "Left joy vertical";
            case CONTROLLER_ACTION_LEFT_THUMB: return "Left thumb";
            case CONTROLLER_ACTION_RIGHT_JOY_HORIZONTAL: return "Right joy horizontal";
            case CONTROLLER_ACTION_RIGHT_JOY_VERTICAL: return "Right joy vertical";
            case CONTROLLER_ACTION_RIGHT_THUMB: return "Right thumb";
            case CONTROLLER_ACTION_A: return "Button A";
            case CONTROLLER_ACTION_B: return "Button B";
            case CONTROLLER_ACTION_X: return "Button X";
            case CONTROLLER_ACTION_Y: return "Button Y";
            case CONTROLLER_ACTION_RIGHT_TRIGGER_BUTTON: return "Right trigger button";
            case CONTROLLER_ACTION_RIGHT_TRIGGER: return "Right trigger";
            case CONTROLLER_ACTION_LEFT_TRIGGER_BUTTON: return "Left trigger button";
            case CONTROLLER_ACTION_LEFT_TRIGGER: return "Left trigger";
            case CONTROLLER_ACTION_START: return "Start button";
            case CONTROLLER_ACTION_SELECT: return "Select button";
            case CONTROLLER_ACTION_BUTTON_1: return "Button 1";
            case CONTROLLER_ACTION_BUTTON_2: return "Button 2";
            case CONTROLLER_ACTION_BUTTON_3: return "Button 3";
            case CONTROLLER_ACTION_BUTTON_4: return "Button 4";
            case CONTROLLER_ACTION_BUTTON_5: return "Button 5";
            case CONTROLLER_ACTION_BUTTON_6: return "Button 6";
            case CONTROLLER_ACTION_BUTTON_7: return "Button 7";
            case CONTROLLER_ACTION_BUTTON_8: return "Button 8";
            case CONTROLLER_ACTION_BUTTON_9: return "Button 9";
            case CONTROLLER_ACTION_BUTTON_10: return "Button 10";
            case CONTROLLER_ACTION_BUTTON_11: return "Button 11";
            case CONTROLLER_ACTION_BUTTON_12: return "Button 12";
            case CONTROLLER_ACTION_BUTTON_13: return "Button 13";
            case CONTROLLER_ACTION_BUTTON_14: return "Button 14";
            case CONTROLLER_ACTION_BUTTON_15: return "Button 15";
            case CONTROLLER_ACTION_BUTTON_16: return "Button 16";
        }
        return "";
    }

    /**
     * Gets a value indicating if the toggle option is available for the controller action.
     * @param controllerActionId is the controller action id.
     * @return true if toggle is available, false otherwise.
     */
    public static boolean isToggleApplicable(String controllerActionId) {

        switch (controllerActionId) {
            case CONTROLLER_ACTION_DPAD_HORIZONTAL:
            case CONTROLLER_ACTION_DPAD_VERTICAL:
            case CONTROLLER_ACTION_LEFT_JOY_HORIZONTAL:
            case CONTROLLER_ACTION_LEFT_JOY_VERTICAL:
            case CONTROLLER_ACTION_RIGHT_JOY_HORIZONTAL:
            case CONTROLLER_ACTION_RIGHT_JOY_VERTICAL:
            case CONTROLLER_ACTION_RIGHT_TRIGGER:
            case CONTROLLER_ACTION_LEFT_TRIGGER:
                return false;

            case CONTROLLER_ACTION_LEFT_THUMB:
            case CONTROLLER_ACTION_RIGHT_THUMB:
            case CONTROLLER_ACTION_A:
            case CONTROLLER_ACTION_B:
            case CONTROLLER_ACTION_X:
            case CONTROLLER_ACTION_Y:
            case CONTROLLER_ACTION_RIGHT_TRIGGER_BUTTON:
            case CONTROLLER_ACTION_LEFT_TRIGGER_BUTTON:
            case CONTROLLER_ACTION_START:
            case CONTROLLER_ACTION_SELECT:
            case CONTROLLER_ACTION_BUTTON_1:
            case CONTROLLER_ACTION_BUTTON_2:
            case CONTROLLER_ACTION_BUTTON_3:
            case CONTROLLER_ACTION_BUTTON_4:
            case CONTROLLER_ACTION_BUTTON_5:
            case CONTROLLER_ACTION_BUTTON_6:
            case CONTROLLER_ACTION_BUTTON_7:
            case CONTROLLER_ACTION_BUTTON_8:
            case CONTROLLER_ACTION_BUTTON_9:
            case CONTROLLER_ACTION_BUTTON_10:
            case CONTROLLER_ACTION_BUTTON_11:
            case CONTROLLER_ACTION_BUTTON_12:
            case CONTROLLER_ACTION_BUTTON_13:
            case CONTROLLER_ACTION_BUTTON_14:
            case CONTROLLER_ACTION_BUTTON_15:
            case CONTROLLER_ACTION_BUTTON_16:
                return true;
        }

        return false;
    }

    //
    // Internal API
    //

    void setName(String name) { this.name = name; }

    void saveToPreferences(SharedPreferences.Editor editor) {
        Log.i(TAG, "saveToPreferences - " + getName());

        editor.putInt(name + "_" + ControllerActionIdCountKey, controllerActionMap.size());

        int controllerActionIdIndex = 0;
        for (Map.Entry<String, Set<ControllerAction>> kvp : controllerActionMap.entrySet()) {

            String controllerActionId = kvp.getKey();
            Set<ControllerAction> controllerActions = kvp.getValue();

            editor.putString(name + "_" + ControllerActionIdKey + "_" + controllerActionIdIndex, controllerActionId);
            editor.putInt(name + "_" + controllerActionId + "_" + ControllerActionCountKey, controllerActions.size());

            int controllerActionIndex = 0;
            for (ControllerAction controllerAction : controllerActions) {

                controllerAction.saveToPreferences(editor, name, controllerActionId, controllerActionIndex);
                controllerActionIndex++;
            }

            controllerActionIdIndex++;
        }
    }

    //
    // Parcelable overrides
    //

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Log.i(TAG, "writeToParcel - " + name);

        dest.writeString(name);
        dest.writeInt(controllerActionMap.size());

        for (Map.Entry<String, Set<ControllerAction>> kvp : controllerActionMap.entrySet()) {

            String controllerActionId = kvp.getKey();
            Set<ControllerAction> controllerActions = kvp.getValue();

            dest.writeString(controllerActionId);
            dest.writeInt(controllerActions.size());

            for (ControllerAction controllerAction : controllerActions)
                dest.writeParcelable(controllerAction, flags);
        }
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        @Override
        public Object createFromParcel(Parcel source) {
            return new ControllerProfile(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new ControllerProfile[size];
        }
    };

    //
    // Object overrides
    //

    @Override
    public String toString() {
        return getName();
    }
}
