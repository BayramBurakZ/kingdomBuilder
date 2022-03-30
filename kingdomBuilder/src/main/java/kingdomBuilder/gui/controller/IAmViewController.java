package kingdomBuilder.gui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import kingdomBuilder.KBState;
import kingdomBuilder.gui.SceneLoader;
import kingdomBuilder.gui.util.Util;
import kingdomBuilder.reducers.GameReducer;
import kingdomBuilder.redux.Store;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This class controls the functions of the IAmView.
 */
public class IAmViewController extends Controller implements Initializable {

    /**
     * Represents the vBox.
     */
    @FXML
    private VBox iamView_vbox;

    /**
     * Represents the ComboBox to select the language.
     */
    @FXML
    private ComboBox<Locale> comboBox_language;

    @FXML
    private Button proceed_button;

    /**
     *
     */
    private List<TextField> iAmTextFields;

    /**
     * Sets the store in the {@link Controller}.
     * @param store the store to set.
     */
    public IAmViewController(Store<KBState> store) {
        super.store = store;
        this.iAmTextFields = new ArrayList<>();
    }

    /**
     * Called to initialize this controller after its root element has been completely processed.
     * @param location the location used to resolve relative paths for the root object,
     *                 or null if the location is not known.
     * @param resources the resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addPlayer(null);

        iamView_vbox.setStyle("""
                -fx-background-image: url(kingdomBuilder/gui/textures/Background.png);
                -fx-background-size: cover;
                """);
        setupEventHandler();
        setupCheckBox(resources);
    }

    private void addPlayer(String name) {
        if(iAmTextFields.size() >= 4)
            return;

        VBox parent = iamView_vbox;
        HBox box = new HBox();
        TextField field = new TextField("");
        Button addButton = new Button();
        Button removeButton = new Button();

        field.setPromptText(getPlaceholder("enterHere"));
        field.setMaxWidth(200);
        field.setId("iAmViewTextField");

        if(name != null)
            field.setText(name);

        addButton.setText("+");
        addButton.setOnMouseClicked(ev -> { addPlayer(null); });

        removeButton.setText("-");
        removeButton.setOnMouseClicked(ev -> {
            if(iAmTextFields.size() > 1) {
                parent.getChildren().remove(box);
                iAmTextFields.remove(field);
            }
        });

        box.setAlignment(Pos.CENTER);
        box.getChildren().addAll(field, addButton, removeButton);

        int idx = parent.getChildren().indexOf(proceed_button);
        parent.getChildren().add(idx, box);

        if(iAmTextFields.size() == 0)
            removeButton.setDisable(true);

        iAmTextFields.add(field);
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

        //reload
        sceneLoader.loadViews(comboBox_language.getSelectionModel().getSelectedItem());
        sceneLoader.showIAmView();

        iAmTextFields.clear();
        addPlayer(null);

    }

    /**
     * Sets the functionality for the MainMenu Button.
     * @param event contains the data from the event source.
     */
    @FXML
    private void onButtonMainMenuShow(Event event) {
        setPreferredNames();
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
    private void setupKeyEventHandler() {}

    /**
     * Sets the user's preferred name.
     */
    private void setPreferredNames() {
        List<String> preferredNames = iAmTextFields
            .stream()
            .map(f -> f.getText().trim())
            .toList();

        final boolean allNamesAreValid = preferredNames.stream().allMatch(this::isNameValid);
        if(!allNamesAreValid) {
            Util.showLocalizedPopupMessage("invalidName", (Stage) sceneLoader.getScene().getWindow());
            return;
        }

        store.dispatch(GameReducer.SET_PREFERRED_NAMES, preferredNames);
        sceneLoader.showMenuView();
    }

    /**
     * Checks if the String is a valid name for the server.
     * @param name the String to check.
     * @return whether the string is valid (true) or not (false)
     */
    private boolean isNameValid(String name) {
        if(name.isEmpty()) return false;
        // invalid characters: [ ] ( )
        return !name.matches(".*\\[.*|.*\\].*|.*\\(.*|.*\\).*");
    }

    private String getPlaceholder(String placeholder) {
        ResourceBundle rb = ResourceBundle.getBundle("kingdomBuilder/gui/gui", SceneLoader.getLocale());
        return rb.getString(placeholder);
    }
}
