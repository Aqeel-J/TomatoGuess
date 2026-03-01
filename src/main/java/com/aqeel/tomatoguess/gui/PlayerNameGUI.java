package com.aqeel.tomatoguess.gui;

import com.aqeel.tomatoguess.App;
import com.aqeel.tomatoguess.components.ButtonUI;
import com.aqeel.tomatoguess.components.RoundTextFieldUI;
import com.aqeel.tomatoguess.components.TextErrorMessage;
import com.aqeel.tomatoguess.database.BonusesHandler;
import com.aqeel.tomatoguess.database.PlayerHandler;
import com.aqeel.tomatoguess.database.PreferencesHandler;
import com.aqeel.tomatoguess.game.AudioManager;
import com.aqeel.tomatoguess.game.TomatoEngine;
import com.aqeel.tomatoguess.resources.AppColors;
import com.aqeel.tomatoguess.resources.AppDimen;
import com.aqeel.tomatoguess.resources.AppIcons;
import com.aqeel.tomatoguess.resources.AppStrings;
import com.aqeel.tomatoguess.utilities.IPAddressReader;
import com.aqeel.tomatoguess.utilities.PasswordEncryptor;
import org.bson.Document;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Represents the GUI for collecting player name during account registration.
 *
 * This panel prompts the user to enter a player name and handles the
 * registration process.
 *
 * @author Aqeel Jabir
 */
public class PlayerNameGUI extends JPanel implements ActionListener {

    // Class Variables.
    App app;
    TomatoEngine tomatoEngine;
    AudioManager audioManager;

    // UI Components
    RoundTextFieldUI txtPlayerName;
    ButtonUI btnClose, btnContinue;
    TextErrorMessage name_err;

    private String email;
    private char[] password;

    /**
     * Constructs a new PlayerNameGUI instance.
     * Initializes and sets up the components for collecting player name.
     *
     * @param app The main application instance.
     */
    public PlayerNameGUI(App app) {
        this.app = app;
        this.tomatoEngine = app.getEngine();
        this.audioManager = app.getAudioManager();
        initComponents(); // Initialize UI components.
    }

    /**
     * Set the email and password for the player.
     *
     * @param email    address of the player.
     * @param password entered by the player.
     */
    public void setEmailAndPass(String email, char[] password) {
        this.email = email;
        this.password = password;
    }

    /**
     * Initializes and sets up the components of the player name panel.
     */
    private void initComponents() {
        setLayout(new BorderLayout());
        setOpaque(false);

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        add(headerPanel, BorderLayout.NORTH);

        btnClose = new ButtonUI(AppIcons.closeIcon);
        btnClose.setBorder(new EmptyBorder(15, 0, 0, 15));
        btnClose.addActionListener(this);
        headerPanel.add(btnClose, BorderLayout.EAST);

        // Main Panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false);
        add(mainPanel, BorderLayout.CENTER);

        GridBagConstraints gbcPlayerName = new GridBagConstraints();
        gbcPlayerName.insets = new Insets(0, 0, 0, 0);
        gbcPlayerName.gridwidth = GridBagConstraints.REMAINDER;
        gbcPlayerName.fill = GridBagConstraints.CENTER;

        // Label to display the Title
        JLabel playerTitle = new JLabel("Enter Name");
        playerTitle.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.BOLD, 24));
        playerTitle.setBorder(new EmptyBorder(0, 0, 0, 0));
        playerTitle.setForeground(AppColors.NEUTRAL_COLOUR);
        mainPanel.add(playerTitle, gbcPlayerName);

        gbcPlayerName.insets = new Insets(5, 0, 20, 0);

        JLabel playerInfo = new JLabel("Enter your player name!");
        playerInfo.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 12));
        playerInfo.setForeground(AppColors.SECONDARY_COLOUR);
        mainPanel.add(playerInfo, gbcPlayerName);

        gbcPlayerName.insets = new Insets(0, 0, 0, 0);

        // Label to indicated name
        JLabel lblName = new JLabel("Name");
        lblName.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 12));
        lblName.setHorizontalAlignment(SwingConstants.LEFT);
        lblName.setBorder(new EmptyBorder(0, 0, 0, 0));
        lblName.setPreferredSize(new Dimension(200, 30));
        lblName.setForeground(AppColors.SECONDARY_COLOUR);
        mainPanel.add(lblName, gbcPlayerName);

        // TextField for player name input.
        txtPlayerName = new RoundTextFieldUI(AppColors.TRANSPARENT_COLOUR, AppColors.NEUTRAL_COLOUR,
                AppDimen.TEXTFIELD_SIZE);
        txtPlayerName.addActionListener(this);
        mainPanel.add(txtPlayerName, gbcPlayerName);

        name_err = new TextErrorMessage("Error");
        name_err.setVisible(false);
        mainPanel.add(name_err, gbcPlayerName);

        gbcPlayerName.insets = new Insets(40, 0, 0, 0);

        // Button component to continue registartion.
        btnContinue = new ButtonUI("Continue", AppColors.ACCENT_COLOUR, AppDimen.BUTTON_SIZE);
        btnContinue.addActionListener(this);
        mainPanel.add(btnContinue, gbcPlayerName);
    }

    /**
     * Handles the action events for buttons.
     *
     * @param e The ActionEvent generated by the button.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnClose)) {
            // Close the application if close button is clicked.
            System.exit(0);
        } else if (e.getSource().equals(btnContinue) || e.getSource().equals(txtPlayerName)) {
            audioManager.playButtonSoundEffects();

            // Check if the player name is empty.
            if (txtPlayerName.getText().isEmpty()) {
                name_err.setText("Name cannot be blank!");
                name_err.setVisible(true);
            } else {
                name_err.setText("");
                name_err.setVisible(false);

                String ip = IPAddressReader.getPrivateIpAddress();
                String playerName = txtPlayerName.getText().trim();
                String salt = PasswordEncryptor.generateSalt();
                String hashPassword = PasswordEncryptor.hashPassword(new String(password), salt);

                // Create a document to store player details.
                Document playerDocument = new Document("playerName", playerName)
                        .append("email", email)
                        .append("salt", salt)
                        .append("hashPassword", hashPassword);
                PlayerHandler.addPlayer(playerDocument);
                BonusesHandler.addBonuses(email);
                PreferencesHandler.addPreferences(email, ip);

                // Navigate to login panel after successfully registering.
                app.getPanel(AppStrings.LOGIN_PANEL);
                System.out.println("Account registered successfully!");
            }
        }
    }
}
