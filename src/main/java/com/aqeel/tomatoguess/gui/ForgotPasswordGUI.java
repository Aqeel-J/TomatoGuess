package com.aqeel.tomatoguess.gui;

import com.aqeel.tomatoguess.App;
import com.aqeel.tomatoguess.components.ButtonUI;
import com.aqeel.tomatoguess.components.RoundTextFieldUI;
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

/**
 * The ForgotPasswordGUI class provides a user interface for the "Forgot
 * Password" functionality.
 *
 * Players can enter their email address and a verification code will be sent to
 * reset their password asynchronously.
 *
 * It handles input validation, error display, and communicates with the
 * PlayerHandler and EmailSender classes.
 *
 * This class is part of the Tomato Guess application's GUI and utilizes the
 * SwingWorker for background tasks.
 *
 * @author Aqeel Jabir
 */
public class ForgotPasswordGUI extends JPanel implements ActionListener {

    App app;
    TomatoEngine tomatoEngine;
    AudioManager audioManager;

    RoundTextFieldUI txtCP_Email;
    ButtonUI btnClose, btnGetCode, btnGetCodeDisabled;
    JLabel email_err;

    /**
     * Constructor for ForgotPasswordGUI class.
     *
     * @param app The main application instance.
     */
    public ForgotPasswordGUI(App app) {
        this.app = app;
        this.tomatoEngine = app.getEngine();
        this.audioManager = app.getAudioManager();
        initComponents();
    }

    /**
     * Initializes and sets up the UI components for the forgot password panel.
     */
    private void initComponents() {
        setLayout(new BorderLayout());
        setOpaque(false);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        add(headerPanel, BorderLayout.NORTH);

        btnClose = new ButtonUI(AppIcons.closeIcon);
        btnClose.setBorder(new EmptyBorder(15, 0, 0, 15));
        btnClose.addActionListener(this);
        headerPanel.add(btnClose, BorderLayout.EAST);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false);
        add(mainPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.CENTER;

        JLabel title = new JLabel("Forgot Password?");
        title.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.BOLD, 24));
        title.setBorder(new EmptyBorder(0, 0, 0, 0));
        title.setForeground(AppColors.NEUTRAL_COLOUR);
        mainPanel.add(title, gbc);

        gbc.insets = new Insets(5, 0, 20, 0);

        JLabel infoLabel = new JLabel(
                "<html><center>Enter the email of your acccount and will send <br>you a verification code to reset your password.</center></html>");
        infoLabel.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 12));
        infoLabel.setForeground(AppColors.SECONDARY_COLOUR);
        mainPanel.add(infoLabel, gbc);

        gbc.insets = new Insets(0, 0, 0, 0);

        JLabel lblName = new JLabel("Email ID");
        lblName.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 12));
        lblName.setHorizontalAlignment(SwingConstants.LEFT);
        lblName.setBorder(new EmptyBorder(0, 0, 0, 0));
        lblName.setPreferredSize(new Dimension(200, 30));
        lblName.setForeground(AppColors.SECONDARY_COLOUR);
        mainPanel.add(lblName, gbc);

        txtCP_Email = new RoundTextFieldUI(AppColors.TRANSPARENT_COLOUR, AppColors.NEUTRAL_COLOUR,
                AppDimen.TEXTFIELD_SIZE);
        txtCP_Email.addActionListener(this);
        mainPanel.add(txtCP_Email, gbc);

        email_err = new JLabel("Error");
        email_err.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 10));
        email_err.setHorizontalAlignment(SwingConstants.LEFT);
        email_err.setBorder(new EmptyBorder(0, 5, 0, 0));
        email_err.setPreferredSize(new Dimension(200, 20));
        email_err.setVisible(false);
        email_err.setForeground(AppColors.DANGER_COLOUR);
        mainPanel.add(email_err, gbc);

        gbc.insets = new Insets(40, 0, 0, 0);

        btnGetCode = new ButtonUI("Get Code", AppColors.ACCENT_COLOUR, AppDimen.BUTTON_SIZE);
        btnGetCode.setVisible(true);
        btnGetCode.addActionListener(this);
        mainPanel.add(btnGetCode, gbc);

        btnGetCodeDisabled = new ButtonUI(AppImages.btnLoaderGif, AppColors.ACCENT_COLOUR, AppDimen.BUTTON_SIZE);
        btnGetCodeDisabled.setEnabled(false);
        btnGetCodeDisabled.setVisible(false);
        mainPanel.add(btnGetCodeDisabled, gbc);
    }

    /**
     * Handles action events triggered by the components.
     * Performs actions, such as sending a verification code or closing the
     * application.
     *
     * @param e The ActionEvent generated by the player's interaction with the
     *          components.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnClose)) {
            // Action to close application when the close button is clicked.
            audioManager.playButtonSoundEffects();
            System.exit(0);

        } else if (e.getSource().equals(btnGetCode) || e.getSource().equals(txtCP_Email)) {
            // Action to get One-Time Password (OTP) when the "Get Code" button or the
            // textfield is activated.
            audioManager.playButtonSoundEffects();
            String email = txtCP_Email.getText().trim();

            // Input validation and processing logic.
            if (email.isEmpty()) {
                email_err.setText("Email cannot be blank!");
                email_err.setVisible(true);
            } else {
                Document playerDoc = PlayerHandler.getPlayer(email);

                // Check if the player's email exists.
                if (playerDoc != null) {
                    btnGetCode.setVisible(false);
                    btnGetCodeDisabled.setVisible(true);

                    String playerName = playerDoc.getString("playerName");

                    // Asynchronously send a verification code via email.
                    SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                        @Override
                        protected Boolean doInBackground() throws Exception {
                            EmailSender.sendPasswordReset(playerName, email, OTPGenerator.generateOTP());
                            return true;
                        }

                        @Override
                        protected void done() {
                            try {
                                boolean emailStatus = get();

                                if (emailStatus) {
                                    btnGetCode.setVisible(true);
                                    btnGetCodeDisabled.setVisible(false);

                                    app.verificationPanel.panelPath(AppStrings.RESET_PASSWORD_PANEL);
                                    app.verificationPanel.setEmail(email);
                                    app.resetPasswordPanel.setResetEmail(email);
                                    app.getPanel(AppStrings.VERIFICATION_PANEL);
                                }

                                email_err.setText("");
                                email_err.setVisible(false);

                            } catch (InterruptedException | ExecutionException e) {
                                e.fillInStackTrace();
                            }
                        }
                    };
                    worker.execute();
                    ;
                } else {
                    email_err.setText("Couldn't find your email!");
                    email_err.setVisible(true);
                }
            }
        }
    }
}
