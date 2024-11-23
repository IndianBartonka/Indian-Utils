package pl.indianbartonka.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
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
    public void testIPv4() throws SocketException {
        for (final Network network : NetworkUtil.getIPv4()) {
            final NetworkInterface networkInterface = network.networkInterface();

            System.out.println("Nazwa wyświetlana: " + networkInterface.getDisplayName());
            System.out.println("Nazwa: " + networkInterface.getName());
            System.out.println("Adres IP: " + network.hostAddress());

            System.out.println("Czy Point-to-Point: " + networkInterface.isPointToPoint());
            System.out.println("Czy Up: " + networkInterface.isUp());
            System.out.println("Czy Loopback: " + networkInterface.isLoopback());
            System.out.println("Czy Virtual: " + networkInterface.isVirtual());
            System.out.println("Czy multicast: " + networkInterface.supportsMulticast());

            System.out.println();
            System.out.println("Adresy przypisane do interfejsu");
            final Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                final InetAddress inetAddress = inetAddresses.nextElement();
                System.out.println("Adres: " + inetAddress.getHostAddress());
                System.out.println("Canonical Host Name: " + inetAddress.getCanonicalHostName());
                System.out.println();
            }

            final int mtu = networkInterface.getMTU();
            System.out.println("MTU: " + mtu + " (" + MathUtil.formatBytesDynamic(mtu, false) + ")");

            System.out.println("Grupy multicast");
            final Enumeration<NetworkInterface> subInterfaces = networkInterface.getSubInterfaces();
            while (subInterfaces.hasMoreElements()) {
                System.out.println("Podinterfejs: " + subInterfaces.nextElement().getDisplayName());
                System.out.println();
            }

            System.out.println("----------------------------");
            System.out.println();
        }
    }

    @Test
    public void testIPv6() throws SocketException {
        for (final Network network : NetworkUtil.getIPv6()) {
            final NetworkInterface networkInterface = network.networkInterface();

            System.out.println("Nazwa wyświetlana: " + networkInterface.getDisplayName());
            System.out.println("Nazwa: " + networkInterface.getName());
            System.out.println("Adres IP: " + network.hostAddress());

            System.out.println("Czy Point-to-Point: " + networkInterface.isPointToPoint());
            System.out.println("Czy Up: " + networkInterface.isUp());
            System.out.println("Czy Loopback: " + networkInterface.isLoopback());
            System.out.println("Czy Virtual: " + networkInterface.isVirtual());
            System.out.println("Czy multicast: " + networkInterface.supportsMulticast());

            System.out.println();
            System.out.println("Adresy przypisane do interfejsu");
            final Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                final InetAddress inetAddress = inetAddresses.nextElement();
                System.out.println("Adres: " + inetAddress.getHostAddress());
                System.out.println("Canonical Host Name: " + inetAddress.getCanonicalHostName());
                System.out.println();
            }

            final int mtu = networkInterface.getMTU();
            System.out.println("MTU: " + mtu + " (" + MathUtil.formatBytesDynamic(mtu, false) + ")");

            System.out.println("Grupy multicast");
            final Enumeration<NetworkInterface> subInterfaces = networkInterface.getSubInterfaces();
            while (subInterfaces.hasMoreElements()) {
                System.out.println("Podinterfejs: " + subInterfaces.nextElement().getDisplayName());
                System.out.println();
            }

            System.out.println("----------------------------");
            System.out.println();
        }
    }
}