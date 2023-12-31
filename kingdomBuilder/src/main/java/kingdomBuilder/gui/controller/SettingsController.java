package kingdomBuilder.gui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
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
 * This class controls all functions for the settings menu.
 */
public class SettingsController extends Controller implements Initializable {

    @FXML
    private VBox root;

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

    @FXML
    private Button proceed_button;

    /**
     * Stores all dynamically allocated boxes.
     */
    private List<HBox> boxes;

    /**
     * Stores all dynamically allocated input fields.
     */
    private List<TextField> iAmTextFields;

    /**
     * Constructs the Settings View with the given store.
     * @param store the Store for access to the state.
     */
    public SettingsController(Store<KBState> store) {
        this.store = store;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupCheckBox(resources);
        settings_checkBox_colors.setSelected(store.getState().betterColorsActive());
        this.iAmTextFields = new ArrayList<>();
        this.boxes = new ArrayList<>();


        store.subscribe(s -> {
            iAmTextFields.clear();
            var names = s.clientPreferredNames();
            if(names == null || names.isEmpty()) return;

            for(var box: boxes)
                root.getChildren().remove(box);

            for(int it = 0; it < names.size(); ++it) {
                addPlayer(names.get(it));
                System.out.println("Added player!");
            }
        }, "preferredNames");
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
    }

    /**
     * Represents the functionality to apply the changes.
     * @param event contains the data from the event source.
     */
    @FXML
    private void onApplyButtonPressed(Event event) {
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

        // color mode
        if (store.getState().betterColorsActive() != settings_checkBox_colors.isSelected()) {
            store.dispatch(GameReducer.BETTER_COLOR_MODE, settings_checkBox_colors.isSelected());
        }

        sceneLoader.showMenuView();
    }

    /**
     * Checks if the String is a valid name for the server.
     * @param name the String to check.
     * @return whether the string is valid (true) or not (false)
     */
    private boolean isNameValid(String name) {
        if (name.isEmpty()) return false;
        // invalid characters: [ ] ( )
        if (name.matches(".*\\[.*|.*\\].*|.*\\(.*|.*\\).*"))
            return false;
        return true;
    }

    private void addPlayer(String name) {

        if(iAmTextFields.size() >= 4)
            return;

        System.out.println("Added button");

        VBox parent = (VBox) proceed_button.getParent();
        HBox box = new HBox();
        TextField field = new TextField("");
        Button addButton = new Button();
        Button removeButton = new Button();

        field.setPromptText(getPlaceholder("enterHere"));
        field.setMaxWidth(200);

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

        boxes.add(box);
        iAmTextFields.add(field);
    }

    private String getPlaceholder(String placeholder) {
        ResourceBundle rb = ResourceBundle.getBundle("kingdomBuilder/gui/gui", SceneLoader.getLocale());
        return rb.getString(placeholder);
    }
}
