package pl.indianbartonka.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Kod zaczerpnięty z
 * <a href="https://github.com/justin-eckenweber/BedrockServerQuery/blob/main/src/main/java/me/justin/bedrockserverquery/data/BedrockQuery.java">...</a>
 */
public record BedrockQuery(String serverAddress, String hostAddress, boolean online, long responseTime, String edition,
                           String motd, int protocol,
                           String minecraftVersion,
                           int playerCount,
                           int maxPlayers, String serverId, String mapName, String gameMode, boolean nintendoLimited,
                           int portV4, int portV6) {

    private static final byte ID_UNCONNECTED_PING = 0x01;
    private static final byte[] UNCONNECTED_MESSAGE_SEQUENCE = {
            0x00, (byte) 0xff, (byte) 0xff, 0x00,
            (byte) 0xfe, (byte) 0xfe, (byte) 0xfe, (byte) 0xfe,
            (byte) 0xfd, (byte) 0xfd, (byte) 0xfd, (byte) 0xfd,
            0x12, 0x34, 0x56, 0x78
    };

    private static long dialerId = MathUtil.RANDOM.nextLong();

    public static BedrockQuery create(final String serverAddress, final int port) {
        try {
            final InetAddress address = InetAddress.getByName(serverAddress);

            try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                 final DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                 final DatagramSocket socket = new DatagramSocket()) {

                dataOutputStream.writeByte(ID_UNCONNECTED_PING);
                dataOutputStream.writeLong(System.currentTimeMillis() / 1000);
                dataOutputStream.write(UNCONNECTED_MESSAGE_SEQUENCE);
                dataOutputStream.writeLong(dialerId++);

                final byte[] requestData = outputStream.toByteArray();
                final byte[] responseData = new byte[(4 * 1_048_576)];

                final DatagramPacket requestPacket = new DatagramPacket(requestData, requestData.length, address, port);
                final DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length);

                final long startTime = System.currentTimeMillis();
                socket.send(requestPacket);
                socket.setSoTimeout((int) DateUtil.secondToMillis(5));
                socket.receive(responsePacket);

                final long ping = System.currentTimeMillis() - startTime;

                // MCPE;<motd>;<protocol>;<version>;<players>;<max players>;<id>;<sub motd>;<gamemode>;<not limited>;<port>;<port>
                final String[] splittedData = new String(responsePacket.getData(), 35, responsePacket.getLength()).split(";");

                int portV4 = -1;
                int portV6 = -1;
                final boolean nintendoLimited = (splittedData.length < 10 || !splittedData[9].contains("1"));

                if (splittedData.length >= 12) {
                    portV4 = Integer.parseInt(splittedData[10]);
                    portV6 = Integer.parseInt(splittedData[11]);
                }

                return new BedrockQuery(serverAddress, address.getHostAddress(), true, ping,
                        splittedData[0], splittedData[1], Integer.parseInt(splittedData[2]), splittedData[3],
                        Integer.parseInt(splittedData[4]), Integer.parseInt(splittedData[5]), splittedData[6],
                        splittedData[7], splittedData[8], nintendoLimited, portV4, portV6);
            }
        } catch (final Exception exception) {
            return new BedrockQuery(serverAddress, "", false, -1, "", "", -1, "", 0, 0, "", "", "", false, -1, -1);
        }
    }
}
