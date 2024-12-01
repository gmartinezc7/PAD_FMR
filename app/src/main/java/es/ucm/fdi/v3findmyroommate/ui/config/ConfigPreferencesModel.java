package es.ucm.fdi.v3findmyroommate.ui.config;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.preference.PreferenceManager;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.ucm.fdi.v3findmyroommate.LocaleUtils;
import es.ucm.fdi.v3findmyroommate.R;

public class ConfigPreferencesModel extends AndroidViewModel {

    private static SharedPreferences userPreferences;
    private static DatabaseReference databaseUserReference;

    public ConfigPreferencesModel(Application application) {
        super(application);
        ConfigPreferencesModel.getUserPreferences(application);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            FirebaseDatabase databaseInstance = FirebaseDatabase.getInstance(application.
                    getApplicationContext().getString(R.string.database_url));

            ConfigPreferencesModel.databaseUserReference = databaseInstance.getReference("users")
                    .child(user.getUid());
        }
        else {
            Log.e("UserPreferences", "Can't find current user");
            throw new NullPointerException("User wasn't found");
        }

    }


    // Singleton method to retrieve the SharedPreferences.
    public static SharedPreferences getUserPreferences(Application application) {
        if (ConfigPreferencesModel.userPreferences == null)
            ConfigPreferencesModel.userPreferences = PreferenceManager.getDefaultSharedPreferences(
                    application.getApplicationContext());
        return ConfigPreferencesModel.userPreferences;
    }


    // Function that loads the initial preferences.
    public static void setInitialPreferences(Application application) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            FirebaseDatabase databaseInstance = FirebaseDatabase.getInstance(application.
                    getApplicationContext().getString(R.string.database_url));
            ConfigPreferencesModel.databaseUserReference = databaseInstance.getReference("users")
                    .child(user.getUid());

            ConfigPreferencesModel.databaseUserReference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();

                    String databaseEmail = user.getEmail();
                    String databaseUsername = user.getDisplayName();
                    String databaseAgeRange = snapshot.child(application
                            .getString(R.string.age_range_db_label)).getValue(String.class);
                    String databaseGender = snapshot.child(application
                            .getString(R.string.gender_db_label)).getValue(String.class);
                    String databaseMaritalStatus = snapshot.child(application
                            .getString(R.string.marital_status_db_label)).getValue(String.class);
                    String databaseOccupation = snapshot.child(application
                            .getString(R.string.occupation_db_label)).getValue(String.class);
                    String databasePropertyType = snapshot.child(application
                            .getString(R.string.property_type_db_label)).getValue(String.class);

                    // Stores the data in SharedPreferences.
                    SharedPreferences.Editor editor = ConfigPreferencesModel.getUserPreferences(application).edit();
                    editor.clear();

                    editor.putString(application.getString(R.string.email_preference_key),
                            databaseEmail);

                    editor.putString(application.getString(R.string.username_preference_key),
                            databaseUsername);

                    editor.putString(application.getString(R.string.age_range_preference_key),
                            databaseAgeRange);

                    // Loads the email preference that's saved in the DB.
                    setEmailPreference(databaseEmail, editor, application);

                    // Loads the username preference that's saved in the DB.
                    setUsernamePreference(databaseUsername, editor, application);

                    // Loads the age range preference that's saved in the DB.
                    setAgeRangePreference(databaseAgeRange, editor, application);

                    // Loads the gender preference that's saved in the DB.
                    setGenderPreference(databaseGender, editor, application);

                    // Loads the marital status preference that's saved in the DB.
                    setMaritalStatusPreference(databaseMaritalStatus, editor, application);

                    // Loads the occupation preference that's saved in the DB.
                    setOccupationPreference(databaseOccupation, editor, application);


                    if (databasePropertyType != null) {
                        setPropertyTypePreference(databasePropertyType, editor, application);

                        if (databasePropertyType.equals(application.getString(
                                R.string.house_property_type_label))) {     // If the user prefers a house.

                            String databaseHouseTypePreference = snapshot.child(application
                                    .getString(R.string.add_house_type_db_label)).getValue(String.class);
                            if (databaseHouseTypePreference != null)
                                setHouseTypePreference(databaseHouseTypePreference, editor, application);

                            String databaseNumRoomsPreference = snapshot.child(application
                                    .getString(R.string.num_rooms_db_label)).getValue(String.class);
                            if (databaseNumRoomsPreference != null)
                                setNumRoomsPreference(databaseNumRoomsPreference, editor, application);

                            String databaseNumBathroomsPreference = snapshot.child(application
                                    .getString(R.string.num_bathrooms_db_label)).getValue(String.class);
                            if (databaseNumBathroomsPreference != null)
                                setNumBathroomsPreference(databaseNumBathroomsPreference, editor, application);

                            String databaseSquareMetersPreference = snapshot.child(application
                                    .getString(R.string.square_meters_db_label)).getValue(String.class);
                            if (databaseSquareMetersPreference != null)
                                setSquareMetersPreference(databaseSquareMetersPreference, editor, application);

                        }

                        else if (databasePropertyType.equals(application.getString(
                                R.string.room_property_type_label))) {     // If the user prefers a room.

                            String databaseMaxNumRoommatesPreference = snapshot.child(application
                                    .getString(R.string.max_num_roommates_db_label)).getValue(String.class);
                            if (databaseMaxNumRoommatesPreference != null)
                                setMaxNumRoommatesPreference(databaseMaxNumRoommatesPreference, editor, application);

                            String databaseRoommateGenderPreference = snapshot.child(application
                                    .getString(R.string.roommate_gender_db_label)).getValue(String.class);
                            if (databaseRoommateGenderPreference != null)
                                setRoommateGenderPreference(databaseRoommateGenderPreference, editor, application);

                            String databaseBathroomTypePreference = snapshot.child(application
                                    .getString(R.string.bathroom_type_db_label)).getValue(String.class);
                            if (databaseBathroomTypePreference != null)
                                setBathroomTypePreference(databaseBathroomTypePreference, editor, application);

                        }

                        // Fields that are common to both property types.
                        String databaseMaxBudgetPreference = snapshot.child(application
                                .getString(R.string.max_budget_db_label)).getValue(String.class);
                        if (databaseMaxBudgetPreference != null)
                            setMaxBudgetPreference(databaseMaxBudgetPreference, editor, application);

                        String databaseOrientationPreference = snapshot.child(application
                                .getString(R.string.orientation_db_label)).getValue(String.class);
                        if (databaseOrientationPreference != null)
                            setOrientationPreference(databaseOrientationPreference, editor, application);

                    }

                    editor.apply();
                }
                else {
                    Log.e("Firebase", "Failed to retrieve user data", task.getException());
                }
            });
        }
        else {
            Log.e("UserPreferences", "Can't find current user");
            throw new NullPointerException("User found to be null");
        }
    }


    // Static method to update the selected preference.
    public static void updateSelectedPreference(String newValue, String preferenceKey, Application application) {
        SharedPreferences.Editor editor = ConfigPreferencesModel.getUserPreferences(application).edit();

        // Updates user's email.
        if (preferenceKey.equals(application.getString(R.string.email_preference_key))) {
            ConfigPreferencesModel.setEmailPreference(newValue, editor, application);
        }

        // Updates user's username.
        else if (preferenceKey.equals(application.getString(R.string.username_preference_key))) {
            ConfigPreferencesModel.setUsernamePreference(newValue, editor, application);
        }

        // Updates user's age range.
        else if (preferenceKey.equals(application.getString(R.string.age_range_preference_key))) {
            ConfigPreferencesModel.setAgeRangePreference(newValue, editor, application);
        }

        // Updates user's gender.
        else if (preferenceKey.equals(application.getString(R.string.gender_preference_key))) {
            ConfigPreferencesModel.setGenderPreference(newValue, editor, application);
        }

        // Updates user's marital status.
        else if (preferenceKey.equals(application.getString(R.string.marital_status_preference_key))) {
            ConfigPreferencesModel.setMaritalStatusPreference(newValue, editor, application);
        }

        // Updates user's occupation.
        else if (preferenceKey.equals(application.getString(R.string.occupation_preference_key))) {
            ConfigPreferencesModel.setOccupationPreference(newValue, editor, application);
        }

        // Updates user's property type preference.
        else if (preferenceKey.equals(application.getString(R.string.property_type_preference_key))) {
            ConfigPreferencesModel.deleteOldPropertyTypeFields(newValue, application);
            ConfigPreferencesModel.setPropertyTypePreference(newValue, editor, application);
        }

        // Updates user's max budget.
        else if (preferenceKey.equals(application.getString(R.string.max_budget_preference_key))) {
            ConfigPreferencesModel.setMaxBudgetPreference(newValue, editor, application);
        }

        // Updates user's house type preference.
        else if (preferenceKey.equals(application.getString(R.string.add_house_type_db_label))) {
            ConfigPreferencesModel.setHouseTypePreference(newValue, editor, application);
        }

        // Updates user's number of rooms preference.
        else if (preferenceKey.equals(application.getString(R.string.num_rooms_preference_key))) {
            ConfigPreferencesModel.setNumRoomsPreference(newValue, editor, application);
        }

        // Updates user's number of bathrooms preference.
        else if (preferenceKey.equals(application.getString(R.string.num_bathrooms_preference_key))) {
            ConfigPreferencesModel.setNumBathroomsPreference(newValue, editor, application);
        }

        // Updates user's square meters preference.
        else if (preferenceKey.equals(application.getString(R.string.square_meters_preference_key))) {
            ConfigPreferencesModel.setSquareMetersPreference(newValue, editor, application);
        }

        // Updates user's max number of roommates preference.
        else if (preferenceKey.equals(application.getString(R.string.max_num_roommates_preference_key))) {
            ConfigPreferencesModel.setMaxNumRoommatesPreference(newValue, editor, application);
        }

        // Updates user's orientation preference.
        else if (preferenceKey.equals(application.getString(R.string.orientation_preference_key))) {
            ConfigPreferencesModel.setOrientationPreference(newValue, editor, application);
        }

        // Updates user's roommates' gender preferences.
        else if (preferenceKey.equals(application.getString(R.string.roommate_gender_preference_key))) {
            ConfigPreferencesModel.setRoommateGenderPreference(newValue, editor, application);
        }

        // Updates user's bathroom type preference.
        else if (preferenceKey.equals(application.getString(R.string.bathroom_type_preference_key))) {
            ConfigPreferencesModel.setBathroomTypePreference(newValue, editor, application);
        }

        editor.apply();
    }


    // Method that saves the email preference.
    private static void setEmailPreference(String email, SharedPreferences.Editor editor,
                                           Application application) {

        editor.putString(application.getString(R.string.email_preference_key), email);
        Log.i("EmailPreference", "Email: " + email);

    }


    // Method that saves the username preference.
    private static void setUsernamePreference(String username, SharedPreferences.Editor editor,
                                           Application application) {

        ConfigPreferencesModel.databaseUserReference.child(
                application.getString(R.string.username_db_label)).setValue(username);
        editor.putString(application.getString(R.string.username_preference_key), username);
        Log.i("UsernamePreference", "Username: " + username);

    }


    // Method that saves the age range preference.
    private static void setAgeRangePreference(String ageRange, SharedPreferences.Editor editor,
                                              Application application) {

        // Since it's a numeric value (albeit in string format), there's no need to check for translations.
        ConfigPreferencesModel.databaseUserReference.child(
                application.getString(R.string.age_range_db_label)).setValue(ageRange);
        editor.putString(application.getString(R.string.age_range_preference_key), ageRange);
        Log.i("AgeRangePreference", "Age range selected: " + ageRange);

    }


    // Method that saves the entered max budget preference.
    private static void setMaxBudgetPreference(String maxBudget, SharedPreferences.Editor editor,
                                               Application application) {

        // Since it's a numeric value (albeit in string format), there's no need to check for translations.
        ConfigPreferencesModel.databaseUserReference.child(
                application.getString(R.string.max_budget_db_label)).setValue(maxBudget);
        editor.putString(application.getString(R.string.max_budget_preference_key), maxBudget);
        Log.i("MaxBudgetPreference", "Max budget: " + maxBudget);

    }


    // Method that saves the selected number of rooms preference.
    private static void setNumRoomsPreference(String numRooms, SharedPreferences.Editor editor,
                                              Application application) {

        // Since it's a numeric value (albeit in string format), there's no need to check for translations.
        ConfigPreferencesModel.databaseUserReference.child(
                application.getString(R.string.num_rooms_db_label)).setValue(numRooms);
        editor.putString(application.getString(R.string.num_rooms_preference_key), numRooms);
        Log.i("NumberOfRoomsPreference", "Number of rooms selected: " + numRooms);

    }


    // Method that saves the selected number of bathrooms preference.
    private static void setNumBathroomsPreference(String numBathrooms, SharedPreferences.Editor editor,
                                                  Application application) {

        // Since it's a numeric value (albeit in string format), there's no need to check for translations.
        ConfigPreferencesModel.databaseUserReference.child(
                application.getString(R.string.num_bathrooms_db_label)).setValue(numBathrooms);
        editor.putString(application.getString(R.string.num_bathrooms_preference_key), numBathrooms);
        Log.i("NumberOfBathroomsPreference", "Number of bathrooms selected: " + numBathrooms);

    }


    // Method that saves the entered square meters preference.
    private static void setSquareMetersPreference(String squareMeters, SharedPreferences.Editor editor,
                                                  Application application) {

        // Since it's a numeric value (albeit in string format), there's no need to check for translations.
        ConfigPreferencesModel.databaseUserReference.child(
                application.getString(R.string.square_meters_db_label)).setValue(squareMeters);
        editor.putString(application.getString(R.string.square_meters_preference_key), squareMeters);
        Log.i("SquareMatersPreference", "Square meters amount: " + squareMeters);

    }


    // Method that saves the selected maximum number of roommates preference.
    private static void setMaxNumRoommatesPreference(String maxNumRoommates, SharedPreferences.Editor editor,
                                                     Application application) {

        // Since it's a numeric value (albeit in string format), there's no need to check for translations.
        ConfigPreferencesModel.databaseUserReference.child(
                application.getString(R.string.max_num_roommates_db_label)).setValue(maxNumRoommates);
        editor.putString(application.getString(R.string.max_num_roommates_preference_key), maxNumRoommates);
        Log.i("MaxNumberOfRoommatesPreference", "Maximum number of roommates selected: " + maxNumRoommates);

    }


    // Method that saves the selected gender preference.
    private static void setGenderPreference(String gender, SharedPreferences.Editor editor,
                                            Application application) {

        String genderDBLanguage = "";
        String genderValueForPreferences = "";

        // Translates to the DB language's equivalent value.
        if (LocaleUtils.doesStringMatchAnyLanguage(application.getApplicationContext(),
                gender, R.string.male_label)) {  // Selects male option.
            genderDBLanguage = LocaleUtils.getValueInDBLocale(application.getApplicationContext(),
                    R.string.male_label);
            genderValueForPreferences = application.getString(R.string.male_label);
        }

        else if (LocaleUtils.doesStringMatchAnyLanguage(application.getApplicationContext(),
                gender, R.string.female_label)) {  // Selects female option.
            genderDBLanguage = LocaleUtils.getValueInDBLocale(application.getApplicationContext(),
                    R.string.female_label);
            genderValueForPreferences = application.getString(R.string.female_label);
        }

        ConfigPreferencesModel.databaseUserReference.child(application.getString(R.string.gender_db_label))
                .setValue(genderDBLanguage);
        editor.putString(application.getString(R.string.gender_preference_key), genderValueForPreferences);
        Log.i("GenderPreference", "Gender selected: " + genderValueForPreferences);
    }


    // Method that saves the selected marital status preference.
    private static void setMaritalStatusPreference(String maritalStatus, SharedPreferences.Editor editor,
                                                   Application application) {

        String maritalStatusDBLanguage = "";
        String maritalStatusValueForPreferences = "";

        // Translates to the DB language's equivalent value.
        if (LocaleUtils.doesStringMatchAnyLanguage(application.getApplicationContext(),
                maritalStatus, R.string.single_status_label)) {  // Selects single option.
            maritalStatusDBLanguage = LocaleUtils.getValueInDBLocale(application.getApplicationContext(),
                    R.string.single_status_label);
            maritalStatusValueForPreferences = application.getString(R.string.single_status_label);
        }

        else if (LocaleUtils.doesStringMatchAnyLanguage(application.getApplicationContext(),
                maritalStatus, R.string.relationship_status_label)) {  // Selects in a relationship option.
            maritalStatusDBLanguage = LocaleUtils.getValueInDBLocale(application.getApplicationContext(),
                    R.string.relationship_status_label);
            maritalStatusValueForPreferences = application.getString(R.string.relationship_status_label);
        }

        else if (LocaleUtils.doesStringMatchAnyLanguage(application.getApplicationContext(),
                maritalStatus, R.string.married_status_label)) {  // Selects married option.
            maritalStatusDBLanguage = LocaleUtils.getValueInDBLocale(application.getApplicationContext(),
                    R.string.married_status_label);
            maritalStatusValueForPreferences = application.getString(R.string.married_status_label);
        }

        else if (LocaleUtils.doesStringMatchAnyLanguage(application.getApplicationContext(),
                maritalStatus, R.string.prefer_not_say_status_label)) {  // Selects prefer not say option.
            maritalStatusDBLanguage = LocaleUtils.getValueInDBLocale(application.getApplicationContext(),
                    R.string.prefer_not_say_status_label);
            maritalStatusValueForPreferences = application.getString(R.string.prefer_not_say_status_label);
        }

        ConfigPreferencesModel.databaseUserReference.child(application.getString(R.string.marital_status_db_label))
                .setValue(maritalStatusDBLanguage);
        editor.putString(application.getString(R.string.marital_status_preference_key), maritalStatusValueForPreferences);
        Log.i("MaritalStatusPreference", "Marital status selected: " + maritalStatusValueForPreferences);

    }


    // Method that saves the selected occupation preference.
    private static void setOccupationPreference(String occupation, SharedPreferences.Editor editor,
                                                Application application) {

        String occupationDBLanguage = "";
        String occupationValueForPreferences = "";

        // Translates to the DB language's equivalent value.
        if (LocaleUtils.doesStringMatchAnyLanguage(application.getApplicationContext(),
                occupation, R.string.employed_status_label)) {  // Selects employed option.
            occupationDBLanguage = LocaleUtils.getValueInDBLocale(application.getApplicationContext(),
                    R.string.employed_status_label);
            occupationValueForPreferences = application.getString(R.string.employed_status_label);
        }

        else if (LocaleUtils.doesStringMatchAnyLanguage(application.getApplicationContext(),
                occupation, R.string.unemployed_status_label)) {  // Selects unemployed option.
            occupationDBLanguage = LocaleUtils.getValueInDBLocale(application.getApplicationContext(),
                    R.string.unemployed_status_label);
            occupationValueForPreferences = application.getString(R.string.unemployed_status_label);
        }

        else if (LocaleUtils.doesStringMatchAnyLanguage(application.getApplicationContext(),
                occupation, R.string.student_status_label)) {  // Selects student option.
            occupationDBLanguage = LocaleUtils.getValueInDBLocale(application.getApplicationContext(),
                    R.string.student_status_label);
            occupationValueForPreferences = application.getString(R.string.student_status_label);
        }

        else if (LocaleUtils.doesStringMatchAnyLanguage(application.getApplicationContext(),
                occupation, R.string.retired_status_label)) {  // Selects retired option.
            occupationDBLanguage = LocaleUtils.getValueInDBLocale(application.getApplicationContext(),
                    R.string.retired_status_label);
            occupationValueForPreferences = application.getString(R.string.retired_status_label);
        }

        ConfigPreferencesModel.databaseUserReference.child(application.getString(R.string.occupation_db_label))
                .setValue(occupationDBLanguage);
        editor.putString(application.getString(R.string.occupation_preference_key), occupationValueForPreferences);
        Log.i("OccupationPreference", "Occupation selected: " + occupationValueForPreferences);

    }


    // Method that saves the selected property type preference.
    private static void setPropertyTypePreference(String propertyType, SharedPreferences.Editor editor,
                                                  Application application) {

        String propertyTypeDBLanguage = "";
        String propertyTypeValueForPreferences = "";

        // Translates to the DB language's equivalent value.
        if (LocaleUtils.doesStringMatchAnyLanguage(application.getApplicationContext(),
                propertyType, R.string.house_property_type_label)) {  // Selects house option.
            propertyTypeDBLanguage = LocaleUtils.getValueInDBLocale(application.getApplicationContext(),
                    R.string.house_property_type_label);
            propertyTypeValueForPreferences = application.getString(R.string.house_property_type_label);
        }

        else if (LocaleUtils.doesStringMatchAnyLanguage(application.getApplicationContext(),
                propertyType, R.string.room_property_type_label)) {  // Selects room option.
            propertyTypeDBLanguage = LocaleUtils.getValueInDBLocale(application.getApplicationContext(),
                    R.string.room_property_type_label);
            propertyTypeValueForPreferences = application.getString(R.string.room_property_type_label);
        }

        ConfigPreferencesModel.databaseUserReference.child(application.getString(R.string.property_type_db_label))
                .setValue(propertyTypeDBLanguage);
        editor.putString(application.getString(R.string.property_type_preference_key), propertyTypeValueForPreferences);
        Log.i("PropertyTypePreference", "Property type selected: " + propertyTypeValueForPreferences);

    }


    // Method that saves the selected house type preference.
    private static void setHouseTypePreference(String houseType, SharedPreferences.Editor editor,
                                               Application application) {

        String houseTypeDBLanguage = "";
        String houseTypeValueForPreferences = "";

        // Translates to the DB language's equivalent value.
        if (LocaleUtils.doesStringMatchAnyLanguage(application.getApplicationContext(),
                houseType, R.string.house_house_type)) {  // Selects house option.
            houseTypeDBLanguage = LocaleUtils.getValueInDBLocale(application.getApplicationContext(),
                    R.string.house_house_type);
            houseTypeValueForPreferences = application.getString(R.string.house_house_type);
        }

        else if (LocaleUtils.doesStringMatchAnyLanguage(application.getApplicationContext(),
                houseType, R.string.apartment_house_type)) {  // Selects apartment option.
            houseTypeDBLanguage = LocaleUtils.getValueInDBLocale(application.getApplicationContext(),
                    R.string.apartment_house_type);
            houseTypeValueForPreferences = application.getString(R.string.apartment_house_type);
        }

        ConfigPreferencesModel.databaseUserReference.child(application.getString(R.string.add_house_type_db_label))
                .setValue(houseTypeDBLanguage);
        editor.putString(application.getString(R.string.house_type_preference_key), houseTypeValueForPreferences);
        Log.i("HouseTypePreference", "House type selected: " + houseTypeValueForPreferences);

    }


    // Method that saves the selected roommates' gender preference.
    private static void setRoommateGenderPreference(String roommateGender, SharedPreferences.Editor editor,
                                                    Application application) {

        String roommateGenderDBLanguage = "";
        String roommateGenderValueForPreferences = "";

        // Translates to the DB language's equivalent value.
        if (LocaleUtils.doesStringMatchAnyLanguage(application.getApplicationContext(),
                roommateGender, R.string.male_label)) {  // Selects male option.
            roommateGenderDBLanguage = LocaleUtils.getValueInDBLocale(application.getApplicationContext(),
                    R.string.male_label);
            roommateGenderValueForPreferences = application.getString(R.string.male_label);
        }

        else if (LocaleUtils.doesStringMatchAnyLanguage(application.getApplicationContext(),
                roommateGender, R.string.female_label)) {  // Selects female option.
            roommateGenderDBLanguage = LocaleUtils.getValueInDBLocale(application.getApplicationContext(),
                    R.string.female_label);
            roommateGenderValueForPreferences = application.getString(R.string.female_label);
        }

        else if (LocaleUtils.doesStringMatchAnyLanguage(application.getApplicationContext(),
                roommateGender, R.string.both_label)) {  // Selects both option.
            roommateGenderDBLanguage = LocaleUtils.getValueInDBLocale(application.getApplicationContext(),
                    R.string.both_label);
            roommateGenderValueForPreferences = application.getString(R.string.both_label);
        }

        ConfigPreferencesModel.databaseUserReference.child(application.getString(R.string.roommate_gender_db_label))
                .setValue(roommateGenderDBLanguage);
        editor.putString(application.getString(R.string.roommate_gender_preference_key), roommateGenderValueForPreferences);
        Log.i("RoommateGenderPreference", "Roommates' gender selected: " + roommateGenderValueForPreferences);

    }


    // Method that saves the selected bathroom type preference.
    private static void setBathroomTypePreference(String bathroomType, SharedPreferences.Editor editor,
                                                  Application application) {

        String bathroomTypeDBLanguage = "";
        String bathroomTypeValueForPreferences = "";

        // Translates to the DB language's equivalent value.
        if (LocaleUtils.doesStringMatchAnyLanguage(application.getApplicationContext(),
                bathroomType, R.string.private_bathroom_type)) {  // Selects private option.
            bathroomTypeDBLanguage = LocaleUtils.getValueInDBLocale(application.getApplicationContext(),
                    R.string.private_bathroom_type);
            bathroomTypeValueForPreferences = application.getString(R.string.private_bathroom_type);
        }

        else if (LocaleUtils.doesStringMatchAnyLanguage(application.getApplicationContext(),
                bathroomType, R.string.shared_bathroom_type)) {  // Selects shared option.
            bathroomTypeDBLanguage = LocaleUtils.getValueInDBLocale(application.getApplicationContext(),
                    R.string.shared_bathroom_type);
            bathroomTypeValueForPreferences = application.getString(R.string.shared_bathroom_type);
        }

        ConfigPreferencesModel.databaseUserReference.child(application.getString(R.string.bathroom_type_db_label))
                .setValue(bathroomTypeDBLanguage);
        editor.putString(application.getString(R.string.bathroom_type_preference_key), bathroomTypeValueForPreferences);
        Log.i("BathroomTypePreference", "Bathroom type selected: " + bathroomTypeValueForPreferences);

    }


    // Method that saves the selected orientation preference.
    private static void setOrientationPreference(String orientation, SharedPreferences.Editor editor,
                                                 Application application) {

        String orientationDBLanguage = "";
        String orientationValueForPreferences = "";

        // Translates to the DB language's equivalent value.
        if (LocaleUtils.doesStringMatchAnyLanguage(application.getApplicationContext(),
                orientation, R.string.interior_orientation)) {  // Selects interior option.
            orientationDBLanguage = LocaleUtils.getValueInDBLocale(application.getApplicationContext(),
                    R.string.interior_orientation);
            orientationValueForPreferences = application.getString(R.string.interior_orientation);
        }

        else if (LocaleUtils.doesStringMatchAnyLanguage(application.getApplicationContext(),
                orientation, R.string.exterior_orientation)) {  // Selects exterior option.
            orientationDBLanguage = LocaleUtils.getValueInDBLocale(application.getApplicationContext(),
                    R.string.exterior_orientation);
            orientationValueForPreferences = application.getString(R.string.exterior_orientation);
        }

        ConfigPreferencesModel.databaseUserReference.child(application.getString(R.string.orientation_db_label))
                .setValue(orientationDBLanguage);
        editor.putString(application.getString(R.string.orientation_preference_key), orientationValueForPreferences);
        Log.i("OrientationPreference", "Orientation selected: " + orientationValueForPreferences);

    }


    // Method that updates those user fields that require re-authentication for security reasons
    // (email and password).
    public static void updateProfile(String itemWritten, String currentPassword, UpdateCredentialsAction action,
                                     Application application) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String userEmail = ConfigPreferencesModel.getUserPreferences(application).getString(application.getString(
                R.string.email_preference_key), "");

        AuthCredential credential = EmailAuthProvider.getCredential(userEmail, currentPassword);
        if (user != null) {
            user.reauthenticate(credential).addOnCompleteListener(task -> {

                Log.d("Reauthentication", "User re-authenticated.");
                // Decides which element of the user's profile to update
                switch(action) {
                    case UPDATE_EMAIL:
                        user.updateEmail(itemWritten).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                String emailPrefKey = application.getString(R.string.email_preference_key);
                                ConfigPreferencesModel.updateSelectedPreference(itemWritten, emailPrefKey, application);
                                Log.d("UserEmail", "User's email successfully updated");
                            }
                        });
                        break;

                    case UPDATE_PASSWORD:
                        user.updatePassword(itemWritten).addOnCompleteListener(task12 -> {
                            if (task12.isSuccessful()) {
                                String passwordPrefKey = application.getString(R.string.password_preference_key);
                                ConfigPreferencesModel.updateSelectedPreference(itemWritten, passwordPrefKey, application);
                                Log.d("UserPassword", "User's password successfully updated");
                            }
                        });
                        break;

                    default:
                        break;
                }
            });
        }
    }


    // Auxiliary function that removes the fields corresponding to the previous property type preference.
    private static void deleteOldPropertyTypeFields(String newValue, Application application) {

        if (LocaleUtils.doesStringMatchAnyLanguage(application.getApplicationContext(),
                newValue, R.string.house_property_type_label)) {  // Si selecciona una casa.
            String housePropertyTypeLabelDefaultLanguage = LocaleUtils.getValueInDBLocale(application
                    .getApplicationContext(), R.string.house_property_type_label);
            ConfigPreferencesModel.databaseUserReference.child(application.getString(R.string.property_type_db_label))
                    .setValue(housePropertyTypeLabelDefaultLanguage);

            // Elimina los campos que no sean de una casa (en el caso de que antes fuese un anuncio de una habitación).
            ConfigPreferencesModel.databaseUserReference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();

                    // Elimina el campo de máximo número de compañeros de piso.
                    String max_num_roomates = snapshot.child(application
                            .getString(R.string.max_num_roommates_db_label)).getValue(String.class);
                    if (max_num_roomates != null)
                        ConfigPreferencesModel.databaseUserReference.child(application.getString(
                                R.string.max_num_roommates_db_label)).removeValue();

                    // Elimina el campo de preferencia de sexo de los compañeros.
                    String roomate_gender = snapshot.child(application
                            .getString(R.string.roommate_gender_db_label)).getValue(String.class);
                    if (roomate_gender != null)
                        ConfigPreferencesModel.databaseUserReference.child(application.getString(
                                R.string.roommate_gender_db_label)).removeValue();

                    // Elimina el campo de tipo de baño.
                    String bathroom_type = snapshot.child(application
                            .getString(R.string.bathroom_type_db_label)).getValue(String.class);
                    if (bathroom_type != null)
                        ConfigPreferencesModel.databaseUserReference.child(application.getString(
                                R.string.bathroom_type_db_label)).removeValue();

                }
            });
        }

        else if (LocaleUtils.doesStringMatchAnyLanguage(application.getApplicationContext(),
                newValue, R.string.room_property_type_label)) {  // Si selecciona una habitación.
            String roomPropertyTypeLabelDefaultLanguage = LocaleUtils.getValueInDBLocale(application
                    .getApplicationContext(), R.string.room_property_type_label);
            ConfigPreferencesModel.databaseUserReference.child(application.getString(R.string.property_type_db_label))
                    .setValue(roomPropertyTypeLabelDefaultLanguage);

            // Elimina los campos que no sean de una habitación (en el caso de que antes fuese un anuncio de una casa).
            ConfigPreferencesModel.databaseUserReference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();

                    // Elimina el campo de tipo de casa.
                    String house_type = snapshot.child(application
                            .getString(R.string.add_house_type_db_label)).getValue(String.class);
                    if (house_type != null)
                        ConfigPreferencesModel.databaseUserReference.child(application.getString(
                                R.string.add_house_type_db_label)).removeValue();

                    // Elimina el campo de número de habitaciones.
                    String num_rooms = snapshot.child(application
                            .getString(R.string.num_rooms_db_label)).getValue(String.class);
                    if (num_rooms != null)
                        ConfigPreferencesModel.databaseUserReference.child(application.getString(
                                R.string.num_rooms_db_label)).removeValue();

                    // Elimina el campo de número de baños.
                    String num_bathroom = snapshot.child(application
                            .getString(R.string.num_bathrooms_db_label)).getValue(String.class);
                    if (num_bathroom != null)
                        ConfigPreferencesModel.databaseUserReference.child(application.getString(
                                R.string.num_bathrooms_db_label)).removeValue();

                    // Elimina el campo de metros cuadrados.
                    String square_meters = snapshot.child(application
                            .getString(R.string.square_meters_db_label)).getValue(String.class);
                    if (square_meters != null)
                        ConfigPreferencesModel.databaseUserReference.child(application.getString(
                                R.string.square_meters_db_label)).removeValue();

                }
            });
        }

    }


}