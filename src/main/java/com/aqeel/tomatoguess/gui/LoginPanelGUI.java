package com.aqeel.tomatoguess.gui;

import com.aqeel.tomatoguess.App;
import com.aqeel.tomatoguess.components.ButtonUI;
import com.aqeel.tomatoguess.components.RoundPasswordFieldUI;
import com.aqeel.tomatoguess.components.RoundTextFieldUI;
import com.aqeel.tomatoguess.components.TextErrorMessage;
import com.aqeel.tomatoguess.database.PlayerHandler;
import com.aqeel.tomatoguess.database.PreferencesHandler;
import com.aqeel.tomatoguess.game.AudioManager;
import com.aqeel.tomatoguess.game.BonusRewarder;
import com.aqeel.tomatoguess.game.TomatoEngine;
import com.aqeel.tomatoguess.resources.*;
import com.aqeel.tomatoguess.utilities.*;
import org.bson.Document;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;

/**
 * The LoginPanelGUI extends JPanel and represents the GUI for the
 * login functionality.
 *
 * It includes components and methods to handle user authentication, input
 * validation, and navigation.
 *
 * @author Aqeel Jabir
 */
public class LoginPanelGUI extends JPanel implements ActionListener {

    // Class Variables.
    App app;
    TomatoEngine tomatoEngine;
    BonusRewarder bonusRewarder;
    AudioManager audioManager;

    // Login-related variables.
    String playerName;

    // UI Components.
    RoundTextFieldUI txtEmail;
    RoundPasswordFieldUI txtPassword;
    ButtonUI btnLogin, btnClose, btnForgotPassword, btnSignUp_Login, btnLoginDisabled;
    JLabel login_err;
    TextErrorMessage email_err, pass_err;
    JPanel signUpLabelPanel;

    /**
     * Constructor for LoginPanelGUI class.
     *
     * @param app The main application instance.
     */
    public LoginPanelGUI(App app) {
        this.app = app;
        this.tomatoEngine = app.getEngine();
        this.bonusRewarder = app.getBonusRewarder();
        this.audioManager = app.getAudioManager();
        initComponents(); // Initialize UI components.
    }

    /**
     * Authenticates email and password provided by the player.
     *
     * @param email         address of the player.
     * @param inputPassword entered by the player.
     * @return true if authentication is successful else false.
     */
    private boolean authenticatePlayer(String email, String inputPassword) {
        Document playerDoc = PlayerHandler.getPlayer(email);
        if (playerDoc != null) {
            this.playerName = playerDoc.getString("playerName");

            String storedSalt = playerDoc.getString("salt");
            String storedHashPassword = playerDoc.getString("hashPassword");
            String inputPasswordHashed = PasswordEncryptor.hashPassword(inputPassword, storedSalt);

            return storedHashPassword.equals(inputPasswordHashed);
        } else {
            System.out.println("User not found.");
            return false;
        }
    }

    /**
     * Sets the error message and visibility for the email error component.
     *
     * @param text       representation of the error message.
     * @param visibility
     */
    private void setEmailError(String text, boolean visibility) {
        email_err.setText(text);
        email_err.setVisible(visibility);
    }

    /**
     * Sets the error message and visibility for the password error component.
     *
     * @param text       The error message text.
     * @param visibility The visibility status of the error message.
     */
    private void setPasswordError(String text, boolean visibility) {
        pass_err.setText(text);
        pass_err.setVisible(visibility);
    }

    /**
     * Sets the error message and visibility for the login error component.
     *
     * @param text       The error message text.
     * @param visibility The visibility status of the error message.
     */
    private void setLoginError(String text, boolean visibility) {
        login_err.setText(text);
        login_err.setVisible(visibility);
    }

    /**
     * Initializes and sets up the UI components for the login panel.
     */
    private void initComponents() {
        setLayout(new BorderLayout());
        setOpaque(false);

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        add(headerPanel, BorderLayout.NORTH);

        // Button component to close application
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

        // Label to display "Login" title.
        JLabel title = new JLabel("Login");
        title.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.BOLD, 24));
        title.setForeground(AppColors.NEUTRAL_COLOUR);
        mainPanel.add(title, gbc);

        gbc.insets = new Insets(5, 0, 30, 0);

        // Label to display sub title.
        JLabel infoLabel = new JLabel("Please enter your Email and Password!");
        infoLabel.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 12));
        infoLabel.setForeground(AppColors.SECONDARY_COLOUR);
        mainPanel.add(infoLabel, gbc);

        gbc.insets = new Insets(0, 0, 5, 0);

        // Label to display login error.
        login_err = new JLabel("Error");
        login_err.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 10));
        login_err.setVisible(false);
        login_err.setForeground(AppColors.DANGER_COLOUR);
        mainPanel.add(login_err, gbc);

        gbc.insets = new Insets(0, 0, 0, 0);

        // Label to indicate email address field
        JLabel lblEmail = new JLabel("Email ID");
        lblEmail.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 12));
        lblEmail.setHorizontalAlignment(SwingConstants.LEFT);
        lblEmail.setBorder(new EmptyBorder(0, 0, 0, 0));
        lblEmail.setPreferredSize(new Dimension(200, 30));
        lblEmail.setForeground(AppColors.SECONDARY_COLOUR);
        mainPanel.add(lblEmail, gbc);

        // TextField for email address input.
        txtEmail = new RoundTextFieldUI(AppColors.TRANSPARENT_COLOUR, AppColors.NEUTRAL_COLOUR,
                AppDimen.TEXTFIELD_SIZE);
        txtEmail.requestFocusInWindow();
        txtEmail.addActionListener(this);
        mainPanel.add(txtEmail, gbc);

        // Custom label to display email error.
        email_err = new TextErrorMessage("Error");
        email_err.setVisible(false);
        mainPanel.add(email_err, gbc);

        gbc.insets = new Insets(10, 0, 0, 0);

        // Label to indicate Password field.
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 12));
        lblPassword.setHorizontalAlignment(SwingConstants.LEFT);
        lblPassword.setBorder(new EmptyBorder(0, 0, 0, 0));
        lblPassword.setPreferredSize(new Dimension(200, 30));
        lblPassword.setForeground(AppColors.SECONDARY_COLOUR);
        mainPanel.add(lblPassword, gbc);

        gbc.insets = new Insets(0, 0, 0, 0);

        // PasswordField for password input.
        txtPassword = new RoundPasswordFieldUI(AppColors.TRANSPARENT_COLOUR, AppColors.NEUTRAL_COLOUR,
                AppDimen.TEXTFIELD_SIZE);
        txtPassword.addActionListener(this);
        mainPanel.add(txtPassword, gbc);

        // Custom label to display password error.
        pass_err = new TextErrorMessage("Error");
        pass_err.setVisible(false);
        mainPanel.add(pass_err, gbc);

        gbc.insets = new Insets(10, 0, 0, 0);

        // Forgot Password Button Panel
        JPanel forgotPassPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        forgotPassPanel.setPreferredSize(new Dimension(200, 20));
        forgotPassPanel.setOpaque(false);
        mainPanel.add(forgotPassPanel, gbc);

        // Button component for forgot password
        btnForgotPassword = new ButtonUI("Forgot Password", AppColors.ACCENT_COLOUR);
        btnForgotPassword.setHorizontalAlignment(SwingConstants.LEFT);
        btnForgotPassword.addActionListener(this);
        forgotPassPanel.add(btnForgotPassword);

        gbc.insets = new Insets(20, 0, 0, 0);

        // Button component for disabled Login.
//        btnLoginDisabled = new ButtonUI(AppImages.btnLoaderGif, AppColors.SECONDARY_COLOUR, AppDimen.BUTTON_SIZE);
//        btnLoginDisabled.setEnabled(false);
//        btnLoginDisabled.setVisible(false);
//        mainPanel.add(btnLoginDisabled, gbc);

        gbc.insets = new Insets(40, 0, 0, 0);

        // Button component for Login.
        btnLogin = new ButtonUI("Login", AppColors.ACCENT_COLOUR, AppDimen.BUTTON_SIZE);
        btnLogin.setVisible(true);
        btnLogin.addActionListener(this);
        mainPanel.add(btnLogin, gbc);

        gbc.insets = new Insets(5, 0, 0, 0);

        signUpLabelPanel = new JPanel(new FlowLayout());
        signUpLabelPanel.setOpaque(false);
        signUpLabelPanel.setVisible(true);
        mainPanel.add(signUpLabelPanel, gbc);

        JLabel signUpLabel = new JLabel("Dont have an account?");
        signUpLabelPanel.add(signUpLabel);

        btnSignUp_Login = new ButtonUI("SignUp", AppColors.ACCENT_COLOUR);
        btnSignUp_Login.addActionListener(this);
        signUpLabelPanel.add(btnSignUp_Login);
    }

    /**
     * Handles action events triggered by the components in the login panel.
     *
     * @param e The ActionEvent object representing th event.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnClose)) {
            // Action to close application when close button is clicked.
            System.exit(0);

        } else if (e.getSource().equals(btnSignUp_Login)) {
            // Action to navigate to the sign-up panel.
            audioManager.playButtonSoundEffects();
            app.getPanel(AppStrings.SIGN_UP_PANEL);

        } else if (e.getSource().equals(btnForgotPassword)) {
            // Action to navigate to the forgot password panel.
            audioManager.playButtonSoundEffects();
            app.getPanel(AppStrings.FORGOT_PASSWORD_PANEL);

        } else if (e.getSource().equals(btnLogin) || e.getSource().equals(txtEmail)
                || e.getSource().equals(txtPassword)) {
            // Handles the login process when the login button or enter key is pressed.
            btnLogin.setLoading(true);
            audioManager.playButtonSoundEffects();

            // Extract email and password input.
            String email = txtEmail.getText().trim();
            char[] password = txtPassword.getPassword();
            boolean checkEmail = false;
            boolean checkPass = false;

            // Validate email input.
            if (email.isEmpty()) {
                setEmailError("Please enter your email address!", true);
                checkEmail = false;
            } else {
                setEmailError("", false);
                checkEmail = true;
            }

            // Validate password input.
            if (password.length == 0) {
                setPasswordError("Please enter your password!", true);
                checkPass = false;
            } else {
                setPasswordError("", false);
                checkPass = true;
            }

            // Proceed if both email and password are valid.
            if (checkEmail && checkPass) {
                // Disable login button, show loader and hide sign-up label.
//                btnLogin.setVisible(false);
//                btnLoginDisabled.setVisible(true);
                signUpLabelPanel.setVisible(false);

                // Authenticate the player using email and password.
                if (authenticatePlayer(email, new String(password))) {
                    // Update UI for successful login.
//                    btnLogin.setVisible(false);
//                    btnLoginDisabled.setVisible(true);
                    signUpLabelPanel.setVisible(false);

                    // Set player information in the tomato engine.
                    String ip = IPAddressReader.getPrivateIpAddress();
                    tomatoEngine.setPlayerName(playerName);
                    tomatoEngine.setPlayerEmail(email);

                    // Set player's country based on IP geolocation.
                    GeoLocationReader geolocation = GeoLocationReader.getInstance();
                    tomatoEngine.setCountry(geolocation.getCountry());

                    // Update region in the score board panel.
                    app.scoreBoardPanel.setRegion();

                    // Retrieve player preferences.
                    Document preferDocument = PreferencesHandler.getPreferences(email, ip);
                    if (preferDocument != null) {
                        // Appy preferences for music and sound.
                        audioManager.setMusicStatus(preferDocument.getBoolean("music"));
                        audioManager.setSoundStatus(preferDocument.getBoolean("sound"));
                        audioManager.toggleBackgroundMusic();

                        // Setup settings in the settings panel.
                        app.settingsPanel.setupSettings(playerName, audioManager.isMusicStatus(),
                                audioManager.isSoundStatus());

                        // Display Welcome panel for 1 second and then navigate to the main menu.
                        app.getPanel(AppStrings.WELCOME_PANEL);
                        Timer timer = new Timer(1000, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                app.getPanel(AppStrings.MAIN_MENU_PANEL);
                            }
                        });
                        timer.setRepeats(false);
                        timer.start();

                        // Reset login error.
                        setLoginError("", false);
                        System.out.println("Login Success!");

                    } else {
                        // Send device verification email in the background.
                        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                            @Override
                            protected Boolean doInBackground() throws Exception {
                                EmailSender.sendDeviceVerification(playerName, email, OTPGenerator.generateOTP());
                                return true;
                            }

                            @Override
                            protected void done() {
                                try {
                                    boolean emailStatus = get();

                                    if (emailStatus) {
                                        app.verificationPanel.setEmail(email);
                                        app.verificationPanel.panelPath(AppStrings.MAIN_MENU_PANEL);
                                        app.getPanel(AppStrings.VERIFICATION_PANEL);
                                        System.out.println("Login Success!");
                                    }

                                    setEmailError("", false);
                                    setPasswordError("", false);

                                } catch (InterruptedException | ExecutionException e) {
                                    e.fillInStackTrace();
                                }
                            }
                        };
                        worker.execute();

                    }
                } else {
                    // Handles unsuccessful login attempt.
//                    btnLogin.setVisible(true);
//                    btnLoginDisabled.setVisible(false);
                    signUpLabelPanel.setVisible(true);
                    setLoginError("<html>Login Failed! Please recheck the<br>email and password and try again.</html>",
                            true);
                    System.out.println("Failed to Login!");
                }
            } else {
//                btnLogin.setVisible(true);
//                btnLoginDisabled.setVisible(false);
                signUpLabelPanel.setVisible(true);
            }
        }
    }
}
