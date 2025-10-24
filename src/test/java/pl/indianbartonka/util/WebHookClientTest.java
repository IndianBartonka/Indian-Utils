package pl.indianbartonka.util;

import java.awt.Color;
import java.io.File;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import pl.indianbartonka.util.discord.WebHookClient;
import pl.indianbartonka.util.discord.embed.Embed;
import pl.indianbartonka.util.discord.embed.EmbedBuilder;
import pl.indianbartonka.util.discord.embed.component.Author;
import pl.indianbartonka.util.discord.embed.component.Field;
import pl.indianbartonka.util.discord.embed.component.Footer;
import pl.indianbartonka.util.discord.embed.component.Image;
import pl.indianbartonka.util.logger.Logger;
import pl.indianbartonka.util.logger.config.LoggerConfiguration;

public class WebHookClientTest {

    private static final LoggerConfiguration loggerConfiguration = LoggerConfiguration.builder()
            .setLogsPath(System.getProperty("user.dir") + File.separator + "logs")
            .setLoggingToFile(true)
            .setDebug(true)
            .build();

    private final Logger logger = new Logger(loggerConfiguration) {
    };

    private final String userName = "Tescior";
    private final String avatarURL = "https://th.bing.com/th/id/OIP.f3tTSSqVRSktMK8uFBqlJQHaIi?w=148&h=180&c=7&r=0&o=5&dpr=1.3&pid=1.7";
    //I don't care :)
    private final String webhookURL = "https://discord.com/api/webhooks/1431293923392880796/2yaI87lcfy5hQEZTzxXRpQl6cJxCgEQ7xvQmyJpoicRhYIj2xyP3ZHzKVka9a34vuISM";
    private final WebHookClient client = new WebHookClient(this.logger, false, this.webhookURL, this.userName, this.avatarURL);

    @Test
    public void sendMessage() {
        this.client.sendMessage("Siema");
    }


    @Test
    public void sendEmbed() {
        final Author author = new Author("Autor", "https://indianbartonka.pl/", "https://indianbartonka.pl/img/favicon.jpg");
        final Footer footer = new Footer("Stopka", "https://th.bing.com/th/id/OIP.V4QmXbKfBya0XHTzLMuhCAHaE8?rs=1&pid=ImgDetMain");
        final Image image = new Image("https://th.bing.com/th/id/OIP._6Wv8EGBcwpJrviyMP7uvgHaEK?rs=1&pid=ImgDetMain");
        final Image thumbnail = new Image("https://th.bing.com/th/id/OIP.FZnr6KbuelFYyegSQxMlLQHaHa?rs=1&pid=ImgDetMain");

        final List<Field> fields = Arrays.asList(
                new Field("Pole 1", "Wartość 1", false),
                new Field("Pole 2", "Wartość 2", true)
        );

        final Embed embed = new EmbedBuilder()
                .setTitle("Tytuł wiadomości")
                .setMessage("Treść wiadomości")
                .setURL("https://example.com")
                .setTimestamp(Instant.now().toString())
                .setAuthor(author)
                .setColor(Color.BLUE)
                .setTTS(false)
                .setFields(fields)
                .setFooter(footer)
                .setThumbnail(thumbnail)
                .setImage(image)
                .build();


        this.client.sendEmbedMessage(embed);
    }
}
