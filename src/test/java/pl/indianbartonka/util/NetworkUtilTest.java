package pl.indianbartonka.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.indianbartonka.util.network.Network;
import pl.indianbartonka.util.network.NetworkUtil;

public class NetworkUtilTest {

    @Test
    public void wifiNameTest() {
        final String ssid = NetworkUtil.getWiFiSSID();

        System.out.println("Nazwa sieci: " + ssid);
        Assertions.assertNotEquals("UNKNNOWN", ssid);
    }


    @Test
    public void testIPv4() {
        for (final Network network : NetworkUtil.getIPv4()) {
            System.out.println("Nazwa wyświetlana: " + network.networkInterface().getDisplayName());
            System.out.println("Nazwa: " + network.networkInterface().getName());
            System.out.println("Address IP: " + network.hostAddress());
            System.out.println();
        }
    }


    @Test
    public void testIPv6() {
        for (final Network network : NetworkUtil.getIPv6()) {
            System.out.println("Nazwa wyświetlana: " + network.networkInterface().getDisplayName());
            System.out.println("Nazwa: " + network.networkInterface().getName());
            System.out.println("Address IP: " + network.hostAddress());
            System.out.println();
        }
    }

}
