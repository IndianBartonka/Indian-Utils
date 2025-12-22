package pl.indianbartonka.util.network;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.jetbrains.annotations.VisibleForTesting;
import pl.indianbartonka.util.IndianUtils;
import pl.indianbartonka.util.annotation.UtilityClass;
import pl.indianbartonka.util.system.LinuxUtil;
import pl.indianbartonka.util.system.SystemUtil;
import pl.indianbartonka.util.system.WindowsUtil;

@UtilityClass
public final class NetworkUtil {

    private NetworkUtil() {

    }

    public static List<Network> getIPv4() {
        final List<Network> ipList = new ArrayList<>();

        try {
            final Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

            while (networkInterfaces.hasMoreElements()) {
                final NetworkInterface networkInterface = networkInterfaces.nextElement();

                if (networkInterface.isUp()) {
                    final Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();

                    while (inetAddresses.hasMoreElements()) {
                        final InetAddress inetAddress = inetAddresses.nextElement();

                        if (inetAddress instanceof Inet4Address) {
                            ipList.add(new Network(networkInterface, inetAddress.getHostAddress()));
                        }
                    }
                }
            }
        } catch (final SocketException socketException) {
            if (IndianUtils.debug) socketException.printStackTrace();
        }

        return ipList;
    }

    @VisibleForTesting
    public static List<Network> getIPv6() {
        final List<Network> ipList = new ArrayList<>();

        try {
            final Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

            while (networkInterfaces.hasMoreElements()) {
                final NetworkInterface networkInterface = networkInterfaces.nextElement();

                if (networkInterface.isUp()) {
                    final Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();

                    while (inetAddresses.hasMoreElements()) {
                        final InetAddress inetAddress = inetAddresses.nextElement();

                        if (inetAddress instanceof Inet6Address) {
                            ipList.add(new Network(networkInterface, inetAddress.getHostAddress()));
                        }
                    }
                }
            }
        } catch (final SocketException socketException) {
            if (IndianUtils.debug) socketException.printStackTrace();
        }

        return ipList;
    }

    public static String getWiFiSSID() {
        try {
            return switch (SystemUtil.getSystem()) {
                case WINDOWS -> WindowsUtil.getWiFiSSID();
                case LINUX, FREE_BSD -> LinuxUtil.getWiFiSSID();
                case MAC, UNKNOWN ->
                        "Pozyskiwanie nazwy sieci WiFi dla " + SystemUtil.getFullyOSName() + " nie jest jeszcze zaimplementowane";
            };
        } catch (final IOException ioException) {
            if (IndianUtils.debug) ioException.printStackTrace();
        }

        return "UNKNOWN";
    }
}