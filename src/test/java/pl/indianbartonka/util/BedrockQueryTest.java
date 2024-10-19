package pl.indianbartonka.util;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BedrockQueryTest {

    @Test
    void testBedrockQuery() {
        final List<String> ipList = List.of(
                "play.skyblockpe.com",
                "play.inpvp.net",
                "mco.cubecraft.net",
                "geo.hivebedrock.network",
                "51.83.32.139"
        );

        for (final String ip : ipList) {
            final BedrockQuery query = BedrockQuery.create(ip, 19132);

            assertNotNull(query, "Query should not be null for IP: " + ip);
            System.out.println("MOTD: " + query.motd());
            System.out.println("Server Address: " + query.serverAddress());
            System.out.println("Host Address: " + query.hostAddress());
            Assertions. assertTrue(query.online(), "Server should be online for IP: " + ip);
            System.out.println("Response Time: " + query.responseTime());
            System.out.println("Edition: " + query.edition());
            System.out.println("Protocol: " + query.protocol());
            System.out.println("Minecraft Version: " + query.minecraftVersion());
            System.out.println("Player Count: " + query.playerCount());
            System.out.println("Max Players: " + query.maxPlayers());
            System.out.println("Server ID: " + query.serverId());
            System.out.println("Map Name: " + query.mapName());
            System.out.println("Game Mode: " + query.gameMode());
            System.out.println("Nintendo Limited: " + query.nintendoLimited());
            System.out.println("Port V4: " + query.portV4());
            System.out.println("Port V6: " + query.portV6());
            System.out.println();
            System.out.println();
        }
    }
}
