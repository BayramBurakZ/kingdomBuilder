package kingdomBuilder.gui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import kingdomBuilder.KBState;
import kingdomBuilder.actions.BetterColorModeAction;
import kingdomBuilder.actions.SetPreferredNameAction;
import kingdomBuilder.redux.Store;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This class controls all functions for the settings menu.
 */
public class SettingsController extends Controller implements Initializable {

    /**
     * Represents the ComboBox to change the language.
     */
    @FXML
    private ComboBox<Locale> comboBox_language;

    /**
     * Represents the textField to change the preferred name.
     */
    @FXML
    private TextField textField_name;

    /**
     * Represents the CheckBox to change the color mode.
     */
    @FXML
    private CheckBox settings_checkBox_colors;

    /**
     * Constructs the Settings View with the given store.
     * @param store The Store for access to the state.
     */
    public SettingsController(Store<KBState> store) {
        this.store = store;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupCheckBox(resources);
        settings_checkBox_colors.setSelected(store.getState().betterColorsActiv);
    }

    /**
     * Setup the Checkbox to change the language.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    private void setupCheckBox(ResourceBundle resources) {
        ObservableList<Locale> options = FXCollections.observableArrayList(Locale.ENGLISH, Locale.GERMAN);
        comboBox_language.setItems(options);
        comboBox_language.setConverter(
                new StringConverter<Locale>() {
                    /**
                     * Converts the object provided into its string form.
                     * Format of the returned string is defined by the specific converter.
                     * @param object the object of type Locale to convert
                     * @return a string representation of the object passed in
                     */
                    @Override
                    public String toString(Locale object) {
                        return object.getDisplayLanguage(object);
                    }

                    /**
                     * Converts the string provided into an object defined by the specific converter.
                     * Format of the string and type of the resulting object is defined by the specific converter.
                     * @param string the String to convert
                     * @return an object representation of the string passed in.
                     */
                    @Override
                    public Locale fromString(String string) {
                        return null;
                    }
                });
        comboBox_language.setCellFactory(p -> new LanguageListCell());
        comboBox_language.getSelectionModel().select(resources.getLocale());
    }

    /**
     * Class that translate the language of a ListCell
     */
    class LanguageListCell extends ListCell<Locale> {
        /**
         * Transform the locales to String in their corresponding language.
         * @param item The local to transform.
         * @param empty If the cell is Empty.
         */
        @Override
        protected void updateItem(Locale item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                setText(item.getDisplayLanguage(item));
            }
        }
    }

    /**
     * Sets the functionality to change the language with the ComboBox.
     * @param event Contains the data from the event source.
     */
    @FXML
    private void onCheckBoxSelectionPressed(Event event) {
        String selectedLanguage = comboBox_language.getSelectionModel().getSelectedItem().getDisplayLanguage();
        System.out.println("Changed Language to: " + selectedLanguage);
        sceneLoader.loadViews(comboBox_language.getSelectionModel().getSelectedItem());
    }

    /**
     * Represents the functionality to apply the changes.
     * @param event Contains the data from the event source.
     */
    @FXML
    private void onApplyButtonPressed(Event event) {
        String preferredName = textField_name.getText().trim();
        if (isNameValid(preferredName)) {
            store.dispatch(new SetPreferredNameAction(preferredName));
        } else {
            // TODO: Error Message if Name is not valid!
        }

        // color mode
        if (store.getState().betterColorsActiv != settings_checkBox_colors.isSelected()) {
            store.dispatch(new BetterColorModeAction(settings_checkBox_colors.isSelected()));
        }

        sceneLoader.showMenuView();
    }

    /**
     * Checks if the String is a valid name for the server.
     * @param name The String to check.
     * @return whether the string is valid (true) or not (false)
     */
    private boolean isNameValid(String name) {
        if (name.isEmpty()) return false;
        // invalid characters: [ ] ( )
        if (name.matches(".*\\[.*|.*\\].*|.*\\(.*|.*\\).*"))
            return false;
        return true;

    }
}
