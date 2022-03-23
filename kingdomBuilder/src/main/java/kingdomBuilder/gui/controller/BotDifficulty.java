package kingdomBuilder.gui.controller;

import java.util.ResourceBundle;

/**
 * Enum to select the difficulty of a bot.
 */
public enum BotDifficulty {
    /**
     * Represents the difficulty easy for a bot.
     */
    EASY {
        @Override
        public String toString() {
            return ResourceBundle.getBundle("kingdomBuilder/gui/gui",
                    Controller.store.getState().sceneLoader().getLocale()).getString("easy");
        }
    },
    /**
     * Represents the difficulty normal for a bot.
     */
    NORMAL {
        @Override
        public String toString() {
            return ResourceBundle.getBundle("kingdomBuilder/gui/gui",
                    Controller.store.getState().sceneLoader().getLocale()).getString("normal");
        }
    },
    /**
     * Represents the difficulty hard for a bot.
     */
    HARD {
        @Override
        public String toString() {
            return ResourceBundle.getBundle("kingdomBuilder/gui/gui",
                    Controller.store.getState().sceneLoader().getLocale()).getString("hard");
        }
    },
    /**
     * Represents the difficulty expert for a bot.
     */
    EXPERT {
        @Override
        public String toString() {
            return ResourceBundle.getBundle("kingdomBuilder/gui/gui",
                    Controller.store.getState().sceneLoader().getLocale()).getString("expert");
        }
    }
}
