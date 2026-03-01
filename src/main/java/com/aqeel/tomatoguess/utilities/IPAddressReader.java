package com.aqeel.tomatoguess.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

/**
 * Utility class for reading IP address.
 *
 * @author Aqeel Jabir
 */
public class IPAddressReader {

    /**
     * Gets the public IP address.
     *
     * Bakkal, Adelec (2010) Getting the ‘external’ IP address in Java, Stack Overflow. Available at:
     * https://stackoverflow.com/questions/2939218/getting-the-external-ip-address-in-java
     * [Accessed: 11 November 2023].
     *
     * @return the public IP address as a string.
     */
    public static String getPublicIpAddress() {
        String ipAddress = "";
        URL url;
        try {
            // Use an external service to retrieve the public IP address.
            url = new URI("http://checkip.amazonaws.com").toURL();
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            ipAddress = in.readLine();

        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        return ipAddress;
    }

    /**
     * Gets the private IP address of the local machine.
     *
     * BillThor (2012) Check whether the ipAddress is in private range, Stack Overflow. Available at:
     * https://stackoverflow.com/questions/9729378/check-whether-the-ipaddress-is-in-private-range
     * [Accessed: 11 November 2023].
     *
     * @return the private IP address as a string.
     */
    public static String getPrivateIpAddress() {
        InetAddress localAddress;
        try {
            // Get the local host address and extract the IP address from the string.
            localAddress = InetAddress.getLocalHost();
            String[] data = localAddress.toString().split("/");
            return data[1];

        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }
}
