package com.aqeel.tomatoguess.utilities;


import com.aqeel.tomatoguess.resources.AppStrings;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * This class provides methods to send various types of email notifications such
 * as account verification, device verification and password reset using
 * JavaMail API.
 *
 * Sun, H.B. (2020) Send email with Java and yahoo! mail, CodeProject. Available
 * at: https://www.codeproject.com/Articles/5266074/Send-Email-with-Java-and-Yahoo-Mail
 * [Accessed: 12 November 2023].
 *
 * @author Aqeel Jabir
 */
public class EmailSender {

    private static final String FROM_EMAIL = "muhammad_aqeel12@yahoo.com";
    private static final String EMAIL_PASS = "xxgtlhglduvtsltc";

    /**
     * Sends an email for account verfication with the provided One-Time Password
     * (OTP).
     *
     * @param playerEmail Emaill address of the player.
     * @param otp         One-TimePassword for account verification.
     */
    public static void sendEmailVerification(String playerEmail, String otp) {

        String subject = "Account Verification";
        String msg = "<html><style>" +
                "body { font-family: 'Arial', sans-serif; }" +
                "h1 { color: #007BFF; text-align: center; }" +
                "p { color: #333; }" +
                "</style><body>";

        msg += "<p>Dear Player,</p>";
        msg += "<p>Thank you for joining " + AppStrings.APP_NAME + "! We're excited to have you on board.</p>";
        msg += "<p>To activate your account, please use the following One-Time Password (OTP):</p>";
        msg += "<h1>" + otp + "</h1>";
        msg += "<p>Please enter this OTP in the account activation section of the app to complete the registration process.";
        msg += "If you did not sign up for " + AppStrings.APP_NAME + ", please ignore this email. </p>";
        // msg+= "If you have any questions or need assistance, feel free to contact our
        // support team at support@[yourdomain].com.";
        msg += "<p>Welcome aboard!</p>";
        msg += "<p>Best regards,<br>";
        msg += "The " + AppStrings.APP_NAME + " Team.</p></body></html>";

        sendEmail(subject, msg, playerEmail);
    }

    /**
     * Sends an email for device verification due to an unrecognised login attempt.
     *
     * @param playerName  Name of the player.
     * @param playerEmail Email address of the player.
     * @param otp         One-TIme Password (OTP) for device verification.
     */
    public static void sendDeviceVerification(String playerName, String playerEmail, String otp) {

        String subject = "Security Alert: Unrecognized Device Login Attempt";

        String msg = "<html><style>" +
                "body { font-family: 'Arial', sans-serif; }" +
                "h1 { color: #007BFF; text-align: center; }" +
                "p { color: #333; }" +
                "</style><body>";

        msg += "<p>Dear " + playerName + ",</p>";
        msg += "<p>We hope this email finds you well. We are writing to inform you about a recent login attempt from an unrecognized device on your account. Your account security is our top priority, and we take such incidents seriously.</p>";
        msg += "<p>Login Details:</p>";
        msg += "<p>Date and Time: " + CurrentDateTime.getDateTime("dd MMMM yyyy HH:mm:ss") + "<br>";
        // msg += "Location: [City, Country]<br>";
        // msg += "Device: [Device Type and Model]<br>";
        msg += "IP Address: " + IPAddressReader.getPrivateIpAddress() + "</p>";
        msg += "<p>If this login was initiated by you, you can ignore this email. However, if you did not attempt to log in from the location mentioned above, we recommend taking the following actions to secure your account:</p>";

        msg += "<ul><li>Change Your Password: Immediately change your account password to prevent unauthorized access. Ensure that the new password is unique and not used for any other accounts.</li>";
        msg += "<li>Review Account Activity: Regularly review your account activity and keep an eye on any unusual or unauthorized transactions.</li></ul>";
        // msg += "<li>Enable Two-Factor Authentication (2FA): Consider enabling 2FA for
        // an extra layer of security. This will require a verification code in addition
        // to your password for login.</li></ul>";
        // msg += "<p>Contact Support: If you believe this login attempt is suspicious
        // or if you need further assistance, please contact our support team at
        // [Support Email or Phone Number]</p>.";
        msg += "<p>For your security, we have included a one-time passcode (OTP) below. Use this code to verify your identity and, if necessary, take the recommended actions mentioned above.</p>";
        msg += "<h1>" + otp + "</h1>";
        msg += "<p>This OTP is valid for a limited time, and please do not share it with anyone. Our support team will never ask for your password or OTP.</p>";
        msg += "<p>Thank you for your prompt attention to this matter. We appreciate your commitment to keeping your account secure.</p>";
        msg += "<p>Best regards,<br>";
        msg += AppStrings.APP_NAME + ".</p></body></html>";

        sendEmail(subject, msg, playerEmail);
    }

    /**
     * Sends an email with a One-Time Password (OTP) for password reset.
     *
     * @param playerName  Name of the player.
     * @param playerEmail Email address of the player.
     * @param otp         One-Time Password (OTP) for password reset.
     */
    public static void sendPasswordReset(String playerName, String playerEmail, String otp) {

        String subject = "Subject: Password Reset OTP for Your " + AppStrings.APP_NAME + " Account";

        String msg = "<html><style>" +
                "body { font-family: 'Arial', sans-serif; }" +
                "h1 { color: #007BFF; text-align: center; }" +
                "p { color: #333; }" +
                "</style><body>";

        msg += "<p>Dear " + playerName + ",</p>";
        msg += "<p>We hope this email finds you well. It has come to our attention that you have requested to reset your password for your "
                + AppStrings.APP_NAME
                + " account. To proceed with the password reset process, please use the following One-Time Password (OTP):</p>";
        msg += "<h1>" + otp + "</h1>";
        msg += "<p>Please ensure that you enter this OTP within the next <b>15 minutes</b> to successfully reset your password. If you did not initiate this request or have any concerns regarding the security of your account, please contact our support team immediately.</p>";
        msg += "<p>For security reasons, please do not share this OTP with anyone, and be cautious of phishing attempts. Our system-generated OTP is designed to help protect your account and ensure that only you can access it.</p>";
        msg += "<p>Thank you for choosing " + AppStrings.APP_NAME
                + ". If you have any further questions or require assistance, feel free to reach out to our support team.</p>";
        msg += "<p>Best regards,<br>";
        msg += AppStrings.APP_NAME + " Support Team</p></body></html>";

        sendEmail(subject, msg, playerEmail);
    }

    /**
     * Sends an email using the configured SMTP server with the specified subject,
     * message and recipient email address.
     *
     * @param subject     of the email address.
     * @param msg         content of the email.
     * @param playerEmail recipient's email address.
     */
    private static void sendEmail(String subject, String msg, String playerEmail) {
        // Configuring for Yahoo SMTP server
        String host = "smtp.mail.yahoo.com";

        // Set up properties for email session.
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Establishing a mail session with authentication.
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, EMAIL_PASS);
            }
        });

        session.setDebug(true);
        try {
            // Create a MimeMessage for the email.
            MimeMessage message = new MimeMessage(session);

            // Set the sender's email address.
            try {
                message.setFrom(new InternetAddress(FROM_EMAIL, AppStrings.APP_NAME));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(playerEmail));
            message.setSubject(subject);
            message.setContent(msg, "text/html; charset=utf-8");

            System.out.println("sending...");
            Transport.send(message);
            System.out.println("Sent message successfully....");

        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }
}
