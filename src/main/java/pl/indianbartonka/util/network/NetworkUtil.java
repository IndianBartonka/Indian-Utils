package pl.indianbartonka.util.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
import pl.indianbartonka.util.exception.UnsupportedSystemException;
import pl.indianbartonka.util.system.SystemUtil;

@VisibleForTesting
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
                case WINDOWS -> getWindowsWiFiSSID();
                case LINUX, FREE_BSD -> getUnixWiFiSSID();
                case MAC, UNKNOWN ->
                        throw new UnsupportedSystemException("Pozyskiwanie nazwy sieci WiFi dla " + SystemUtil.getFullyOSName() + " nie jest jeszcze wspierane");
            };
        } catch (final Exception ignore) {
        }

        return "UNKNOWN";
    }

    private static String getUnixWiFiSSID() throws IOException {
        final Process process = Runtime.getRuntime().exec("nmcli -t -f name connection show --active");
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String ssid;
            while ((ssid = reader.readLine()) != null) {
                if (!ssid.equalsIgnoreCase("lo") && !ssid.isEmpty()) return ssid;
            }
        }
        return "UNKNOWN";
    }

    private static String getWindowsWiFiSSID() throws IOException {
        final Process process = Runtime.getRuntime().exec("netsh wlan show interfaces");
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().startsWith("SSID")) {
                    return line.split(":")[1].trim();
                }
            }
        }
        return "UNKNOWN";
    }
}

