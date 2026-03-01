package com.aqeel.tomatoguess.gui;

import com.aqeel.tomatoguess.App;
import com.aqeel.tomatoguess.components.ButtonUI;
import com.aqeel.tomatoguess.components.RoundTextFieldUI;
import com.aqeel.tomatoguess.database.PreferencesHandler;
import com.aqeel.tomatoguess.game.AudioManager;
import com.aqeel.tomatoguess.game.BonusRewarder;
import com.aqeel.tomatoguess.game.TomatoEngine;
import com.aqeel.tomatoguess.resources.AppColors;
import com.aqeel.tomatoguess.resources.AppDimen;
import com.aqeel.tomatoguess.resources.AppIcons;
import com.aqeel.tomatoguess.resources.AppStrings;
import com.aqeel.tomatoguess.utilities.IPAddressReader;
import com.aqeel.tomatoguess.utilities.OTPGenerator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;

/**
 * The OTPVerificationGUI class represents a GUI for One-Time Password (OTP)
 * verification.
 * It allows users to enter the verification code sent to their email for
 * authentication.
 *
 * @author Aqeel Jabir
 */
public class OTPVerificationGUI extends JPanel implements ActionListener {

    // Class Variables.
    App app;
    TomatoEngine tomatoEngine;
    AudioManager audioManager;
    BonusRewarder bonusRewarder;

    private String email;
    private String panelPath;

    // UI Components.
    RoundTextFieldUI txtOTP1, txtOTP2, txtOTP3, txtOTP4, txtOTP5, txtOTP6;
    GridBagConstraints gbcOtp;
    ButtonUI btnClose, btnVerify;
    JPanel otpPanel;
    JLabel info, otp_err;

    /**
     * Constructs an instance of the OTPVerificationGUI class.
     *
     * @param app The main application instance.
     */
    public OTPVerificationGUI(App app) {
        this.app = app;
        this.tomatoEngine = app.getEngine();
        this.audioManager = app.getAudioManager();
        this.bonusRewarder = app.getBonusRewarder();
        initComponents();
    }

    /**
     * Creates a text field for OTP confirguration.
     *
     * @param size  (width and height) of the text field.
     * @param index of the text field.
     * @return the configured OTP text field.
     */
    private RoundTextFieldUI createTextField(Dimension size, int index) {
        RoundTextFieldUI textField = new RoundTextFieldUI(AppColors.TRANSPARENT_COLOUR, AppColors.NEUTRAL_COLOUR, size);
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(createOTPFilter());
        textField.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.BOLD, 12));
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        textField.addFocusListener(createFocusListener());
        textField.addActionListener(this);

        // Add a KeyListener to move focus to the next field after key released
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (!textField.getText().isEmpty() && index < 6) {
                    getNextTextField(index).requestFocusInWindow();
                }
            }
        });

        otpPanel.add(textField, gbcOtp);
        return textField;
    }

    /**
     * Retrieves the next OTP text field based on the current index.
     *
     * @param currentIndex of the text field.
     * @return the next OTP text field.
     */
    private RoundTextFieldUI getNextTextField(int currentIndex) {
        return switch (currentIndex) {
            case 1 -> txtOTP2;
            case 2 -> txtOTP3;
            case 3 -> txtOTP4;
            case 4 -> txtOTP5;
            case 5 -> txtOTP6;
            default -> txtOTP1;
        };
    }

    /**
     * Creates a document filter for the OTP text fields.
     *
     * @return the document filter for the OTP text fields.
     */
    private static DocumentFilter createOTPFilter() {
        return new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {

                if (text.length() <= 1) {
                    String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());

                    if ((currentText.length() - length + text.length()) <= 1) {
                        super.replace(fb, offset, length, text, attrs);
                    }
                }
            }
        };
    }

    /**
     * Creates a focus listener for the OTP text fields.
     *
     * @return the focus listener for the OTP text fields.
     */
    private FocusListener createFocusListener() {
        return new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (e.getSource() instanceof RoundTextFieldUI textField) {
                    textField.selectAll();
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
            }
        };
    }

    /**
     * Handles changes in the system clipboard for pasting OTP codes.
     */
    private void handleClipboardChange() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable transferable = clipboard.getContents(this);

        if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            try {
                String clipboardText = (String) transferable.getTransferData(DataFlavor.stringFlavor);
                setOTPText(clipboardText);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sets the OTP text based on the provided OTP code.
     *
     * @param otp code to set in the text fields.
     */
    private void setOTPText(String otp) {
        if (otp.length() == 6) {
            txtOTP1.setText(String.valueOf(otp.charAt(0)));
            txtOTP2.setText(String.valueOf(otp.charAt(1)));
            txtOTP3.setText(String.valueOf(otp.charAt(2)));
            txtOTP4.setText(String.valueOf(otp.charAt(3)));
            txtOTP5.setText(String.valueOf(otp.charAt(4)));
            txtOTP6.setText(String.valueOf(otp.charAt(5)));
        }
    }

    /**
     * Clears the OTP text in all text fields.
     */
    private void clearOTPText() {
        txtOTP1.setText("");
        txtOTP2.setText("");
        txtOTP3.setText("");
        txtOTP4.setText("");
        txtOTP5.setText("");
        txtOTP6.setText("");
    }

    /**
     * Sets the email address for which the OTP is being verified.
     *
     * @param email address for OTP verification.
     */
    public void setEmail(String email) {
        this.email = email;
        info.setText("<html><center>Please enter the verification code that was sent to <br><b>" + email
                + "</b></center></html>");
    }

    /**
     * Sets the panel path to navigate to after successful OTP verification.
     *
     * @param panelPath for navigation.
     */
    public void panelPath(String panelPath) {
        this.panelPath = panelPath;
    }

    /**
     * Initializes and configures the components of the OTPVerificationGUI.
     */
    private void initComponents() {
        setLayout(new BorderLayout());
        setOpaque(false);

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        add(headerPanel, BorderLayout.NORTH);

        // Button component to close panel.
        btnClose = new ButtonUI(AppIcons.closeIcon);
        btnClose.setBorder(new EmptyBorder(15, 0, 0, 15));
        btnClose.addActionListener(this);
        headerPanel.add(btnClose, BorderLayout.EAST);

        // Main Panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false);
        add(mainPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 5, 0);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Check your email!", SwingConstants.CENTER);
        title.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.BOLD, 20));
        title.setForeground(AppColors.NEUTRAL_COLOUR);
        mainPanel.add(title, gbc);

        gbc.insets = new Insets(0, 0, 0, 0);

        info = new JLabel();
        info.setHorizontalAlignment(SwingConstants.CENTER);
        info.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 12));
        info.setForeground(AppColors.SECONDARY_COLOUR);
        mainPanel.add(info, gbc);

        gbc.insets = new Insets(30, 0, 0, 0);

        // Label indicating verification code textfield.
        JLabel lblVerifyCode = new JLabel("Verification Code");
        lblVerifyCode.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 12));
        lblVerifyCode.setHorizontalAlignment(SwingConstants.LEFT);
        lblVerifyCode.setBorder(new EmptyBorder(0, 7, 0, 0));
        lblVerifyCode.setPreferredSize(new Dimension(200, 30));
        lblVerifyCode.setForeground(AppColors.SECONDARY_COLOUR);
        mainPanel.add(lblVerifyCode, gbc);

        gbc.insets = new Insets(0, 0, 0, 0);

        // OTP panel for OTP textfields.
        otpPanel = new JPanel(new GridBagLayout());
        otpPanel.setOpaque(false);
        mainPanel.add(otpPanel, gbc);

        gbcOtp = new GridBagConstraints();
        gbcOtp.insets = new Insets(0, 7, 0, 7);
        gbcOtp.gridheight = GridBagConstraints.REMAINDER;
        gbcOtp.fill = GridBagConstraints.VERTICAL;

        // Initialize and configure OTP text fields for each digit.
        txtOTP1 = createTextField(AppDimen.TEXTFIELD_OTP_SIZE, 1);
        txtOTP2 = createTextField(AppDimen.TEXTFIELD_OTP_SIZE, 2);
        txtOTP3 = createTextField(AppDimen.TEXTFIELD_OTP_SIZE, 3);
        txtOTP4 = createTextField(AppDimen.TEXTFIELD_OTP_SIZE, 4);
        txtOTP5 = createTextField(AppDimen.TEXTFIELD_OTP_SIZE, 5);
        txtOTP6 = createTextField(AppDimen.TEXTFIELD_OTP_SIZE, 6);

        // Add a KeyListener to txtOTP1 to handle clipboard changes
        txtOTP1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_V && e.isControlDown()) {
                    handleClipboardChange();
                }
            }
        });

        gbc.insets = new Insets(1, 0, 0, 0);

        otp_err = new JLabel("Error");
        otp_err.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 10));
        otp_err.setHorizontalAlignment(SwingConstants.LEFT);
        otp_err.setBorder(new EmptyBorder(0, 12, 0, 0));
        otp_err.setPreferredSize(new Dimension(200, 20));
        otp_err.setVisible(false);
        otp_err.setForeground(AppColors.DANGER_COLOUR);
        mainPanel.add(otp_err, gbc);

        gbc.insets = new Insets(30, 0, 0, 0);

        // Button Panel.
        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        mainPanel.add(btnPanel, gbc);

        btnVerify = new ButtonUI("Verify Code", AppColors.ACCENT_COLOUR, AppDimen.BUTTON_SIZE);
        btnVerify.addActionListener(this);
        btnPanel.add(btnVerify);

        // Toolkit.getDefaultToolkit().getSystemClipboard().addFlavorListener(e ->
        // handleClipboardChange());
    }

    /**
     * Handles actions performed on components, such as button clicks and text field
     * input.
     *
     * @param e The ActionEvent representing the player's action.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnClose)) {
            // Action to close application when the close button is clicked.
            System.exit(0);
        } else if (e.getSource().equals(btnVerify) || e.getSource().equals(txtOTP1) || e.getSource().equals(txtOTP2)
                || e.getSource().equals(txtOTP3) || e.getSource().equals(txtOTP4) || e.getSource().equals(txtOTP5)
                || e.getSource().equals(txtOTP6)) {

            // Concatenate OTP digits intot a single string.
            String otp = txtOTP1.getText().trim()
                    + txtOTP2.getText().trim()
                    + txtOTP3.getText().trim()
                    + txtOTP4.getText().trim()
                    + txtOTP5.getText().trim()
                    + txtOTP6.getText().trim();

            // Check if entered OTP matches the generated OTP.
            if (otp.equals(String.valueOf(OTPGenerator.getOTP()))) {

                // Handles different panel path.
                switch (panelPath) {
                    case AppStrings.PLAYER_NAME_PANEL ->
                        // Navigation to player name panel.
                            app.getPanel(AppStrings.PLAYER_NAME_PANEL);
                    case AppStrings.RESET_PASSWORD_PANEL ->
                        // Navigation to reset password panel.
                            app.getPanel(AppStrings.RESET_PASSWORD_PANEL);
                    case AppStrings.MAIN_MENU_PANEL -> {
                        // Navigation to main menu panel.
                        String ip = IPAddressReader.getPrivateIpAddress();
                        PreferencesHandler.addPreferences(email, ip);

                        // Toggle musics and sound settings.
                        audioManager.setMusicStatus(true);
                        audioManager.setSoundStatus(true);
                        audioManager.toggleBackgroundMusic();

                        // Setup settings panel.
                        app.settingsPanel.setupSettings(tomatoEngine.getPlayerName(), audioManager.isMusicStatus(),
                                audioManager.isSoundStatus());

                        // Navigation to welcome panel.
                        app.getPanel(AppStrings.WELCOME_PANEL);
                        Timer timer = new Timer(1000, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                app.getPanel(AppStrings.MAIN_MENU_PANEL);
                            }
                        });
                        timer.setRepeats(false);
                        timer.start();
                    }
                }

                // Clear OTP text fields and hide error message.
                clearOTPText();
                otp_err.setText("");
                otp_err.setVisible(false);
                System.out.println("OTP Correct!");
            } else {
                // Clear OTP text fields and show error message.
                clearOTPText();
                otp_err.setText("Invalid verification code!");
                otp_err.setVisible(true);
                System.out.println("OTP Incorrect!");
            }
        }
    }
}
