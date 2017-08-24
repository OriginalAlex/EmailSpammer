package com.originalalex.github.visual;

import com.originalalex.github.backend.User;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;

public class Controller {


    @FXML
    private TextArea emailAddress;

    @FXML
    private PasswordField emailPass;

    @FXML
    private TextArea targetAddress;

    @FXML
    private TextArea emailSubject;

    @FXML
    private TextArea emailMessage;

    @FXML
    private TextArea numberOfEmails;

    @FXML
    private ListView<String> messages;

    @FXML
    private ImageView bomb; // the image is essentially the "spam" button

    @FXML
    private Text status;

    private User user;
    private boolean isSignedIn;

    @FXML
    public void initialize() {
        bomb.setImage(new Image(getClass().getResourceAsStream("boom.jpg")));
        allowTabToShiftFocus();
    }

    @FXML
    private void login() {
        String email = emailAddress.getText();
        String pass = emailPass.getText();
        user = (user == null) ? user = new User(email, pass) : user; // Only assign the user value if it is null
        boolean success = user.login();
        if (success) {
            isSignedIn = true;
            status.setText("STATUS: LOGGED IN");
        } else {
            status.setText("STATUS: INVALID CREDENTIALS");

        }
    }

    @FXML
    public void logout() {
        user.logout();
    }

    @FXML
    public void addMessageToList() {
        String message = emailMessage.getText().trim();
        if (message.equals("")) {
            status.setText("STATUS: INVALID MESSAGE");
            return;
        }
        if (!messages.getItems().contains(message)) {
            messages.getItems().add(message);
            status.setText("STATUS: ADDED MESSAGE");
            emailMessage.setText("");
        } else {
            status.setText("STATUS: MESSAGE DUPLICATE!");
        }
    }

    @FXML
    public void imageClicked() {
        if (isSignedIn) {
            String target = targetAddress.getText();
            String subject = emailSubject.getText();
            String possibleNumber = numberOfEmails.getText();
            if (!isValidNumber(possibleNumber)) {
                status.setText("STATUS: INVALID NUMBER ENTERED [Must be in range 1-1000]");
                return;
            }
            int number = Integer.parseInt(possibleNumber);
            if (messages.getItems().size() == 0) {
                status.setText("STATUS: MUST SUPPLY MESSAGES TO SEND");
            } else {
                status.setText("STATUS: BOMBING");
                user.sendSpamEmail(messages.getItems(), subject, target, number);
            }
        } else {
            status.setText("STATUS: NOT LOGGED IN");
        }
    }

    private void allowTabToShiftFocus() {
        emailAddress.setOnKeyPressed(event -> { // These are because it is often nicer to press tab to go to the next credential thing than to click on it.
            if (event.getCode() == KeyCode.TAB) {
                event.consume();
                emailPass.requestFocus();
            }
        });
        targetAddress.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB) {
                event.consume();
                emailSubject.requestFocus();
            }
        });
        emailSubject.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB) {
                event.consume();
                numberOfEmails.requestFocus();
            }
        });
        numberOfEmails.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB) {
                event.consume();
                event.consume();
                emailMessage.requestFocus();
            }
        });
    }

    private boolean isValidNumber(String str) {
        try {
            int i = Integer.parseInt(str);
            if (i < 1 || i > 1000) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

}
