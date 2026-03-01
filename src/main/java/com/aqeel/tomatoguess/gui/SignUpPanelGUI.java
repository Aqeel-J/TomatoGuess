package com.aqeel.tomatoguess.gui;

import com.aqeel.tomatoguess.App;
import com.aqeel.tomatoguess.components.ButtonUI;
import com.aqeel.tomatoguess.components.RoundPasswordFieldUI;
import com.aqeel.tomatoguess.components.RoundTextFieldUI;
import com.aqeel.tomatoguess.components.TextErrorMessage;
import com.aqeel.tomatoguess.database.PlayerHandler;
import com.aqeel.tomatoguess.game.AudioManager;
import com.aqeel.tomatoguess.game.TomatoEngine;
import com.aqeel.tomatoguess.resources.*;
import com.aqeel.tomatoguess.utilities.EmailSender;
import com.aqeel.tomatoguess.utilities.OTPGenerator;
import org.bson.Document;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The SignUpPanelGUI class represents the GUI for user registration.
 *
 * Users can enter their email and password to create a new account.
 * The class extends JPanel and implements ActionListener to handle button
 * interactions.
 *
 * @author Aqeel Jabir
 */

public class SignUpPanelGUI extends JPanel implements ActionListener {

    App app;
    TomatoEngine tomatoEngine;
    AudioManager audioManager;

    String email;
    char[] password;

    // UI Components.
    RoundTextFieldUI txtNewEmail;
    RoundPasswordFieldUI txtNewPassword;
    ButtonUI btnSignUp, btnClose, btnLogin_SignUp;
    TextErrorMessage email_err, pass_err;
    JLabel info_err;
    JPanel loginLabelPanel;

    /**
     * Constructs a new instance for SignUpPanelGUI class.
     *
     * @param app The main application instance.
     */
    public SignUpPanelGUI(App app) {
        this.app = app;
        this.tomatoEngine = app.getEngine();
        this.audioManager = app.getAudioManager();
        initComponents();
    }

    /**
     * Initializes and configures the GUI components for the sign-up panel.
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

        // Body Panel.
        JPanel bodyPanel = new JPanel(new GridBagLayout());
        bodyPanel.setOpaque(false);
        add(bodyPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.CENTER;

        // Label to display "Create Account" title.
        JLabel title = new JLabel("Create Account");
        title.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.BOLD, 24));
        title.setForeground(AppColors.NEUTRAL_COLOUR);
        bodyPanel.add(title, gbc);

        gbc.insets = new Insets(5, 0, 20, 0);

        JLabel signUpInfo = new JLabel(
                "<html><center><p>Enter your email and password to <br> get a confirmation code</p></center></html>");
        signUpInfo.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 12));
        signUpInfo.setForeground(AppColors.SECONDARY_COLOUR);
        bodyPanel.add(signUpInfo, gbc);

        gbc.insets = new Insets(0, 0, 20, 0);

        info_err = new JLabel("Error/Success");
        info_err.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 10));
        info_err.setHorizontalAlignment(SwingConstants.CENTER);
        info_err.setPreferredSize(new Dimension(200, 20));
        info_err.setVisible(false);
        bodyPanel.add(info_err, gbc);

        gbc.insets = new Insets(0, 0, 0, 0);

        JLabel lblNewEmail = new JLabel("Email ID");
        lblNewEmail.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 12));
        lblNewEmail.setHorizontalAlignment(SwingConstants.LEFT);
        lblNewEmail.setBorder(new EmptyBorder(0, 0, 0, 0));
        lblNewEmail.setPreferredSize(new Dimension(200, 30));
        lblNewEmail.setForeground(AppColors.SECONDARY_COLOUR);
        bodyPanel.add(lblNewEmail, gbc);

        // TextField to get player email.
        txtNewEmail = new RoundTextFieldUI(AppColors.TRANSPARENT_COLOUR, AppColors.NEUTRAL_COLOUR, AppDimen.TEXTFIELD_SIZE);
        txtNewEmail.addActionListener(this);
        bodyPanel.add(txtNewEmail, gbc);

        email_err = new TextErrorMessage("Error");
        email_err.setVisible(false);
        bodyPanel.add(email_err, gbc);

        gbc.insets = new Insets(10, 0, 0, 0);

        JLabel lblNewPassword = new JLabel("Password");
        lblNewPassword.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 12));
        lblNewPassword.setHorizontalAlignment(SwingConstants.LEFT);
        lblNewPassword.setBorder(new EmptyBorder(0, 0, 0, 0));
        lblNewPassword.setPreferredSize(new Dimension(200, 30));
        lblNewPassword.setForeground(AppColors.SECONDARY_COLOUR);
        bodyPanel.add(lblNewPassword, gbc);

        gbc.insets = new Insets(0, 0, 0, 0);

        // TextField to get player password.
        txtNewPassword = new RoundPasswordFieldUI(AppColors.TRANSPARENT_COLOUR, AppColors.NEUTRAL_COLOUR,
                AppDimen.TEXTFIELD_SIZE);
        txtNewPassword.addActionListener(this);
        bodyPanel.add(txtNewPassword, gbc);

        pass_err = new TextErrorMessage("Error");
        pass_err.setVisible(false);
        bodyPanel.add(pass_err, gbc);

        gbc.insets = new Insets(40, 0, 0, 0);

        // Button component for Sign Up
        btnSignUp = new ButtonUI("Sign Up", AppColors.ACCENT_COLOUR, AppDimen.BUTTON_SIZE);
        btnSignUp.setVisible(true);
        btnSignUp.addActionListener(this);
        bodyPanel.add(btnSignUp, gbc);

        gbc.insets = new Insets(5, 0, 0, 0);

        loginLabelPanel = new JPanel(new FlowLayout());
        loginLabelPanel.setOpaque(false);
        bodyPanel.add(loginLabelPanel, gbc);

        JLabel loginLabel = new JLabel("Already have an account?");
        loginLabelPanel.add(loginLabel);

        btnLogin_SignUp = new ButtonUI("Login", AppColors.ACCENT_COLOUR);
        btnLogin_SignUp.addActionListener(this);
        loginLabelPanel.add(btnLogin_SignUp);
    }

    /**
     * Handles action events triggered by buttons in the sign-up panel.
     *
     * @param e The ActionEvent representing the button click.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnClose)) {
            // Action to close application when the close button is clicked.
            audioManager.playButtonSoundEffects();
            System.exit(0);

        } else if (e.getSource().equals(btnLogin_SignUp)) {
            // Navigates to Login Panel.
            audioManager.playButtonSoundEffects();
            app.getPanel(AppStrings.LOGIN_PANEL);

        } else if (e.getSource().equals(btnSignUp) || e.getSource().equals(txtNewEmail)
                || e.getSource().equals(txtNewPassword)) {
            // Processors player registration.
            btnSignUp.setLoading(true);
            audioManager.playButtonSoundEffects();

            boolean emailCheck = false;
            boolean passCheck = false;

            email = txtNewEmail.getText().trim();
            password = txtNewPassword.getPassword();

            String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,6}$";
            Pattern pattern = Pattern.compile(emailRegex);
            Matcher matcher = pattern.matcher(email);

            // Check if email is empty.
            if (email.isEmpty()) {
                email_err.setText("Email ID cannot be blank!");
                email_err.setVisible(true);
                emailCheck = false;
            } else {
                // Check if player email already exists.
                Document playerDoc = PlayerHandler.getPlayer(email);
                if (playerDoc != null) {
                    System.out.println(playerDoc);
                    email_err.setText("Entered email already exists!");
                    email_err.setVisible(true);
                    emailCheck = false;
                } else {
                    // Check if email format is valid.
                    if (!matcher.matches()) {
                        email_err.setText("Invalid email format!");
                        email_err.setVisible(true);
                        emailCheck = false;
                    } else {
                        emailCheck = true;
                        email_err.setText("");
                        email_err.setVisible(false);
                    }
                }
            }

            // Check if password length is not zero.
            if (password.length == 0) {
                pass_err.setText("Password cannot be blank!");
                pass_err.setVisible(true);
                passCheck = false;
            } else {
                // Check if password length is less than 8.
                if (password.length < 8) {
                    pass_err.setText("<html>Password cannot be less than 8<br>characters!</html>");
                    pass_err.setPreferredSize(new Dimension(200, 40));
                    pass_err.setVisible(true);
                    passCheck = false;
                } else {
                    passCheck = true;
                    pass_err.setText("");
                    pass_err.setPreferredSize(new Dimension(200, 20));
                    pass_err.setVisible(false);
                }
            }

            // Proceed if email and password is valid.
            if (emailCheck && passCheck) {
                loginLabelPanel.setVisible(false);

                // Pass the email and password for insertion.
                app.playerNamePanel.setEmailAndPass(email, password);

                // Asynchronously send a verification code via email.
                SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                    @Override
                    protected Boolean doInBackground() throws Exception {
                        EmailSender.sendEmailVerification(email, OTPGenerator.generateOTP());
                        return true;
                    }

                    @Override
                    protected void done() {
                        try {
                            boolean emailStatus = get();

                            if (emailStatus) {
                                app.verificationPanel.panelPath(AppStrings.PLAYER_NAME_PANEL);
                                app.verificationPanel.setEmail(email);
                                app.getPanel(AppStrings.VERIFICATION_PANEL);
                                System.out.println("Email and password entered successfully!");
                            }

                            loginLabelPanel.setVisible(true);

                            txtNewEmail.setText("");
                            txtNewPassword.setText("");

                            email_err.setText("");
                            email_err.setVisible(false);

                            pass_err.setText("");
                            pass_err.setVisible(false);

                        } catch (InterruptedException | ExecutionException e) {
                            e.fillInStackTrace();
                        }
                    }
                };
                worker.execute();

            } else {
                loginLabelPanel.setVisible(true);
                System.out.println("Failed to create account!");
            }
        }
    }
}
