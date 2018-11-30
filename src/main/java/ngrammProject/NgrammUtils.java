package ngrammProject;

import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

/**
 * Created by OEM on 30.11.2018.
 */
public class NgrammUtils {
    private String getData(TextField text, CheckBox spaceCheck,CheckBox registerCheck,CheckBox punctuationCheck,CheckBox changeSpaceOn_) {
        String result = text.getText();
        result = result.replaceAll("[^А-Яа-я -!\",./:;?]", "");
        if (!spaceCheck.isSelected()) {
            result = result.replaceAll("\\s", "");
        }
        if (!registerCheck.isSelected()) {
            result = result.toLowerCase();
        }
        if (!punctuationCheck.isSelected()) {
            result = result.replaceAll("[-!\",./:;?]", "");
        }
        if (changeSpaceOn_.isSelected()) {
            result = result.replaceAll(" ", "_");
        }
        return result;
    }



}
