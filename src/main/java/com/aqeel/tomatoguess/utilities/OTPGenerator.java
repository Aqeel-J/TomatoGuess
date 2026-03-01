package com.aqeel.tomatoguess.utilities;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Utility class for generating and managing One-Time Passwords (OTP).
 *
 * @author Aqeel Jabir
 */
public class OTPGenerator {

    private static final long EXPIRATION_TIME = 15 * 60 * 1000;

    private static String otp;
    private static ScheduledExecutorService scheduler;

    /**
     * Private constructor to prevent instantiation.
     */
    private OTPGenerator() {

    }

    /**
     * Generates a new One-Time Password (OTP), schedules its expiration and returns
     * the generated One-Time Password (OTP).
     *
     * @return generated One-Time Password (OTP).
     */
    public static String generateOTP() {
        otp = generateRandomOTP();
        scheduleExpiration();
        return otp;
    }

    /**
     * Gets the currently generated One-Time Password (OTP).
     *
     * @return the current One-Time Password (OTP).
     */
    public static String getOTP() {
        return otp;
    }

    /**
     * Schedules an expiration for the current One-Time Password (OTP) after the
     * specified expiration time.
     */
    private static void scheduleExpiration() {
        if (scheduler == null) {
            scheduler = Executors.newScheduledThreadPool(1);
        }

        scheduler.schedule(() -> {
            otpExpired();
        }, EXPIRATION_TIME, TimeUnit.MILLISECONDS);
    }

    /**
     * Handles the expiration of the One-Time Password (OTP) by setting it to null
     * and printing a message.
     */
    private static void otpExpired() {
        otp = null;
        System.out.println("OTP has expired. Please generate a new OTP.");
    }

    /**
     * Generates a random 6-digit One-Time Password (OTP).
     *
     * @return the generated One-Time Password (OTP).
     */
    private static String generateRandomOTP() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }
}
