package com.aqeel.tomatoguess.gui;

import com.aqeel.tomatoguess.App;
import com.aqeel.tomatoguess.components.ButtonUI;
import com.aqeel.tomatoguess.components.RoundPasswordFieldUI;
import com.aqeel.tomatoguess.components.TextErrorMessage;
import com.aqeel.tomatoguess.database.PlayerHandler;
import com.aqeel.tomatoguess.game.AudioManager;
import com.aqeel.tomatoguess.game.BonusRewarder;
import com.aqeel.tomatoguess.game.TomatoEngine;
import com.aqeel.tomatoguess.resources.AppColors;
import com.aqeel.tomatoguess.resources.AppDimen;
import com.aqeel.tomatoguess.resources.AppIcons;
import com.aqeel.tomatoguess.resources.AppStrings;
import com.aqeel.tomatoguess.utilities.PasswordEncryptor;
import org.bson.Document;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

/**
 * The ResetPasswordGUI represents the GUI for the password reset functionality.
 *
 * This panel allows the user to reset their password by entering a new password
 * and confirming it. It includes input fields for the new password and
 * confirmation,
 * along with error messages for validation feedback.
 *
 * @author Aqeel Jabir
 */
public class ResetPasswordGUI extends JPanel implements ActionListener {

    App app;
    TomatoEngine tomatoEngine;
    BonusRewarder bonusRewarder;
    AudioManager audioManager;

    String email;

    // UI Components
    RoundPasswordFieldUI txtNewPassword, txtConfirmPassword;
    ButtonUI btnResetPassword, btnClose, btnForgotPassword;
    TextErrorMessage password_err, new_pass_err, confirm_pass_err;

    /**
     * Constructs a new instance of ResetPasswordGUI class.
     *
     * @param app The main application instance.
     */
    public ResetPasswordGUI(App app) {
        this.app = app;
        this.tomatoEngine = app.getEngine();
        this.bonusRewarder = app.getBonusRewarder();
        this.audioManager = app.getAudioManager();
        initComponents();
    }

    /**
     * Sets the email for password reset.
     *
     * @param email address associated with the player account.
     */
    public void setResetEmail(String email) {
        this.email = email;
    }

    /**
     * Initializes and sets up the components of the password reset panel.
     */
    private void initComponents() {
        setLayout(new BorderLayout());
        setOpaque(false);

        // Header Panel.
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

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.CENTER;

        // Display title
        JLabel title = new JLabel("ResetPassword");
        title.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.BOLD, 24));
        title.setForeground(AppColors.NEUTRAL_COLOUR);
        mainPanel.add(title, gbc);

        gbc.insets = new Insets(5, 0, 30, 0);

        JLabel infoLabel = new JLabel("Enter your new password!");
        infoLabel.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 12));
        infoLabel.setForeground(AppColors.SECONDARY_COLOUR);
        mainPanel.add(infoLabel, gbc);

        gbc.insets = new Insets(0, 0, 5, 0);

        password_err = new TextErrorMessage("Error");
        password_err.setVisible(false);
        mainPanel.add(password_err, gbc);

        gbc.insets = new Insets(0, 0, 0, 0);

        JLabel lblNewPassword = new JLabel("New Password");
        lblNewPassword.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 12));
        lblNewPassword.setHorizontalAlignment(SwingConstants.LEFT);
        lblNewPassword.setBorder(new EmptyBorder(0, 0, 0, 0));
        lblNewPassword.setPreferredSize(new Dimension(200, 30));
        lblNewPassword.setForeground(AppColors.SECONDARY_COLOUR);
        mainPanel.add(lblNewPassword, gbc);

        // Password field for New Password
        txtNewPassword = new RoundPasswordFieldUI(AppColors.TRANSPARENT_COLOUR, AppColors.NEUTRAL_COLOUR,
                AppDimen.TEXTFIELD_SIZE);
        txtNewPassword.requestFocusInWindow();
        txtNewPassword.addActionListener(this);
        mainPanel.add(txtNewPassword, gbc);

        new_pass_err = new TextErrorMessage("Error");
        new_pass_err.setVisible(false);
        mainPanel.add(new_pass_err, gbc);

        gbc.insets = new Insets(10, 0, 0, 0);

        JLabel lblConfirmPassword = new JLabel("Confirm New Password");
        lblConfirmPassword.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 12));
        lblConfirmPassword.setHorizontalAlignment(SwingConstants.LEFT);
        lblConfirmPassword.setBorder(new EmptyBorder(0, 0, 0, 0));
        lblConfirmPassword.setPreferredSize(new Dimension(200, 30));
        lblConfirmPassword.setForeground(AppColors.SECONDARY_COLOUR);
        mainPanel.add(lblConfirmPassword, gbc);

        gbc.insets = new Insets(0, 0, 0, 0);

        // PasswordField for Confirm Password
        txtConfirmPassword = new RoundPasswordFieldUI(AppColors.TRANSPARENT_COLOUR, AppColors.NEUTRAL_COLOUR,
                AppDimen.TEXTFIELD_SIZE);
        txtConfirmPassword.addActionListener(this);
        mainPanel.add(txtConfirmPassword, gbc);

        confirm_pass_err = new TextErrorMessage("Error");
        confirm_pass_err.setVisible(false);
        mainPanel.add(confirm_pass_err, gbc);

        gbc.insets = new Insets(20, 0, 0, 0);

        btnResetPassword = new ButtonUI("Reset Password", AppColors.ACCENT_COLOUR, AppDimen.BUTTON_SIZE);
        btnResetPassword.setVisible(true);
        btnResetPassword.addActionListener(this);
        mainPanel.add(btnResetPassword, gbc);
    }

    /**
     * Handles the actions performed when buttons are clicked.
     *
     * @param e The ActionEvent representing the button click.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnClose)) {
            // Action to close application when the close button is clicked.
            audioManager.playButtonSoundEffects();
            System.exit(0);

        } else if (e.getSource().equals(btnResetPassword) || e.getSource().equals(txtNewPassword)
                || e.getSource().equals(txtConfirmPassword)) {
            // Action to update new password when the text field or button is triggered.

            // Play button sound effects.
            audioManager.playButtonSoundEffects();

            // Retrieve entered password.
            char[] newPassword = txtNewPassword.getPassword();
            char[] confirmNewPassword = txtConfirmPassword.getPassword();

            boolean newPassCheck = false;
            boolean confirmPassCheck = false;

            // Check if new password is empty.
            if (new String(newPassword).isEmpty()) {
                newPassCheck = false;
                new_pass_err.setText("Please enter a new password!");
                new_pass_err.setVisible(true);
            } else {
                newPassCheck = true;
                new_pass_err.setText("");
                new_pass_err.setVisible(false);
            }

            // Check if you confirm password matches new password
            if (!Arrays.equals(confirmNewPassword, newPassword)) {
                confirmPassCheck = false;
                confirm_pass_err.setText("Confirm Password did not match!");
                confirm_pass_err.setVisible(true);
            } else {
                confirmPassCheck = true;
                confirm_pass_err.setText("");
                confirm_pass_err.setVisible(false);
            }

            // If both password valites, update player's password.
            if (newPassCheck && confirmPassCheck) {
                Document playerDoc = PlayerHandler.getPlayer(email);
                if (playerDoc != null) {
                    // Generate salt and hash the password.
                    String salt = PasswordEncryptor.generateSalt();
                    String hashPassword = PasswordEncryptor.hashPassword(new String(newPassword), salt);

                    // Updates the player password in the database.
                    Document update = new Document("$set", new Document()
                            .append("salt", salt)
                            .append("hashPassword", hashPassword));
                    PlayerHandler.updatePlayer(email, update);

                    // Navigation to login panel.
                    app.getPanel(AppStrings.LOGIN_PANEL);
                }
            }
        }
    }
}
