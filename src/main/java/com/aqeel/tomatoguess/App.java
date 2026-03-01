package com.aqeel.tomatoguess;

import com.aqeel.tomatoguess.game.*;
import com.aqeel.tomatoguess.gui.*;
import com.aqeel.tomatoguess.resources.AppColors;
import com.aqeel.tomatoguess.resources.AppStrings;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
/**
 * The class extends JFrame and serves as main window container for the
 * application.
 *
 * It is the main entry point and manages different panels using CardLayout for
 * smooth transitions between screens.
 *
 * The class follows the Singleton design pattern, ensuring a single instance of
 * the application.
 *
 * Components include the game engine, bonus rewarder, and audio manager. The UI
 * uses FlatLaf for a modern appearance.
 *
 * The main method initializes the application, sets up GUI components and
 * manages the application's lifecycle.
 *
 * It handles panel transitions and creates instances of various game-related
 * classes.
 *
 * @author Aqeel Jabir
 */
public class App extends JFrame {

    private static App instance = null;

    /**
     * Gets the instance of the App class.
     * Uses a singleton pattern to ensure a single instance of the App is created.
     *
     * @return the App instance.
     */
    public static App getInstance() {
        if (instance == null) {
            instance = new App();
        }
        return instance;
    }

    TomatoEngine tomatoEngine;
    BonusRewarder bonusRewarder;
    AudioManager audioManager;

    JPanel mainPanel = new JPanel();
    CardLayout mainCardLayout = new CardLayout();

    public GameGUI gamePanel;
    public GameOverGUI gameOverPanel;
    public LevelResultGUI levelResultPanel;
    public OTPVerificationGUI verificationPanel;
    public PlayerNameGUI playerNamePanel;
    public ScoreBoardGUI scoreBoardPanel;
    public SettingsGUI settingsPanel;
    public TimesUpGUI timesUpPanel;
    public ResetPasswordGUI resetPasswordPanel;
    public SignUpPanelGUI signUpPanel;
    /**
     * Initializes the components of the main application Window.
     */
    public void initComponents() {
        setSize(500, 500);
        setUndecorated(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15));

        Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/app_icon.png"));
        setIconImage(icon);


        IPlayer player = new Player();
        this.tomatoEngine = TomatoEngine.getInstance(player);
        this.bonusRewarder = BonusRewarder.getInstance();
        this.audioManager = AudioManager.getInstance();

        mainPanel.setLayout(mainCardLayout);
        mainPanel.setBackground(AppColors.PRIMARY_COLOUR);

        // Login Panel
        LoginPanelGUI loginPanel = new LoginPanelGUI(this);
        mainPanel.add(loginPanel, AppStrings.LOGIN_PANEL);

        // Player Name Panel
        playerNamePanel = new PlayerNameGUI(this);
        mainPanel.add(playerNamePanel, AppStrings.PLAYER_NAME_PANEL);

        // Sign Up Panel
        signUpPanel = new SignUpPanelGUI(this);
        mainPanel.add(signUpPanel, AppStrings.SIGN_UP_PANEL);

        // Main Menu Panel
        MainMenuGUI mainMenuPanel = new MainMenuGUI(this);
        mainPanel.add(mainMenuPanel, AppStrings.MAIN_MENU_PANEL);

        // Scoreboard Panel
        scoreBoardPanel = new ScoreBoardGUI(this);
        mainPanel.add(scoreBoardPanel, AppStrings.SCOREBOARD_PANEL);

        // Times Up Panel
        timesUpPanel = new TimesUpGUI(this);
        mainPanel.add(timesUpPanel, AppStrings.TIMES_UP_PANEL);

        // Level Result Panel
        levelResultPanel = new LevelResultGUI(this);
        mainPanel.add(levelResultPanel, AppStrings.LEVEL_RESULT_PANEL);

        // Game Panel
        gamePanel = new GameGUI(this);
        mainPanel.add(gamePanel, AppStrings.GAME_PANEL);

        // Settings Panel
        settingsPanel = new SettingsGUI(this);
        mainPanel.add(settingsPanel, AppStrings.SETTINGS_PANEL);

        // Game Over Panel
        gameOverPanel = new GameOverGUI(this);
        mainPanel.add(gameOverPanel, AppStrings.GAME_OVER_PANEL);

        // Welcome, Panel
        WelcomeGUI welcomePanel = new WelcomeGUI();
        mainPanel.add(welcomePanel, AppStrings.WELCOME_PANEL);

        // Verification Panel
        verificationPanel = new OTPVerificationGUI(this);
        mainPanel.add(verificationPanel, AppStrings.VERIFICATION_PANEL);

        // Forgot Password Panel
        ForgotPasswordGUI forgotPasswordPanel = new ForgotPasswordGUI(this);
        mainPanel.add(forgotPasswordPanel, AppStrings.FORGOT_PASSWORD_PANEL);

        // Reset Password Panel
        resetPasswordPanel = new ResetPasswordGUI(this);
        mainPanel.add(resetPasswordPanel, AppStrings.RESET_PASSWORD_PANEL);

        getContentPane().add(mainPanel);
        mainPanel.repaint();
    }

    /**
     * Sets the visibility of the game window.
     *
     * @param visibility true to make the window visible else false.
     */
    private void gameVisible(boolean visibility) {
        setVisible(visibility);
    }

    /**
     * Shows the specified panel
     *
     * @param panel to be shown.
     */
    public void getPanel(String panel) {
        mainCardLayout.show(mainPanel, panel);
    }

    /**
     * Gets the TomatoEngine instance.
     *
     * @return the TomatoEngine instance.
     */
    public TomatoEngine getEngine() {
        return tomatoEngine;
    }

    /**
     * Gets the BonusRewarder instance.
     *
     * @return the BonusRewarder instance.
     */
    public BonusRewarder getBonusRewarder() {
        return bonusRewarder;
    }

    /**
     * Gets the AudioManager instance.
     *
     * @return the AudioManager instance.
     */
    public AudioManager getAudioManager() {
        return audioManager;
    }

    /**
     * The entry point of the application.
     *
     * @param args the command line arguments.
     */
    public static void main(String[] args) {
        FlatDarkLaf.setup();
        App app = new App();
        app.initComponents();
        app.gameVisible(true);
    }
}
