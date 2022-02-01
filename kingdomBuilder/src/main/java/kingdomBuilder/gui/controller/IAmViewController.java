package kingdomBuilder.gui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;
import kingdomBuilder.KBState;
import kingdomBuilder.actions.SetPreferredNameAction;
import kingdomBuilder.redux.Store;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This class controls the functions of the IAmView.
 */
public class IAmViewController extends Controller implements Initializable {
    /**
     * Represents the TextField to enter the preferred name.
     */
    @FXML
    private TextField iAmViewTextField;

    /**
     * Represents the ComboBox to select the language.
     */
    @FXML
    private ComboBox<Locale> comboBox_language;

    /**
     * Sets the store in the {@link Controller}.
     * @param store the store to set.
     */
    public IAmViewController(Store<KBState> store) {
        super.store = store;
    }

    /**
     * Called to initialize this controller after its root element has been completely processed.
     * @param location the location used to resolve relative paths for the root object,
     *                 or null if the location is not known.
     * @param resources the resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupEventHandler();
        setupCheckBox(resources);
    }

    /**
     * Setup the Checkbox to change the language.
     * @param resources the resources used to localize the root object, or null if the root object was not localized.
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
         * @param item the local to transform.
         * @param empty if the cell is Empty.
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
     * @param event contains the data from the event source.
     */
    @FXML
    private void onCheckBoxSelectionPressed(Event event) {
        String selectedLanguage = comboBox_language.getSelectionModel().getSelectedItem().getDisplayLanguage();
        System.out.println("Changed Language to: " + selectedLanguage);
        sceneLoader.loadViews(comboBox_language.getSelectionModel().getSelectedItem());
        sceneLoader.showIAmView();
    }

    /**
     * Sets the functionality for the MainMenu Button.
     * @param event contains the data from the event source.
     */
    @FXML
    private void onButtonMainMenuShow(Event event) {
        SetPreferredName();
    }

    /**
     * Setup all connected EventHandler.
     */
    private void setupEventHandler() {
        setupKeyEventHandler();
    }

    /**
     * Creates the EventHandler that is responsible for the Key events.
     */
    private void setupKeyEventHandler() {
        iAmViewTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            /**
             * Invoked when a specific event of the type for which this handler is registered happens.
             * @param event the event which occurred.
             */
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    SetPreferredName();
                }
            }
        });
    }

    /**
     * Sets the user's preferred name.
     */
    private void SetPreferredName() {
        String preferredName = iAmViewTextField.getText().trim();
        if (isNameValid(preferredName)) {
            // TODO: could let dispatch throw in case the name isn't valid
            store.dispatch(new SetPreferredNameAction(preferredName));
            sceneLoader.showMenuView();
        }
    }

    /**
     * Checks if the String is a valid name for the server.
     * @param name the String to check.
     * @return whether the string is valid (true) or not (false)
     */
    private boolean isNameValid(String name) {
        if(name.isEmpty()) return false;
        // invalid characters: [ ] ( )
        if (name.matches(".*\\[.*|.*\\].*|.*\\(.*|.*\\).*"))
            return false;
        return true;
    }
}
