package me.indian.util.discord.embed;

import java.awt.Color;
import java.util.List;
import me.indian.util.discord.embed.component.Author;
import me.indian.util.discord.embed.component.Field;
import me.indian.util.discord.embed.component.Footer;
import me.indian.util.discord.embed.component.Image;

public class EmbedBuilder {

    private final Embed embed;

    public EmbedBuilder() {
        this.embed = new Embed();
    }

    public EmbedBuilder setTitle(final String title) {
        this.embed.setTitle(title);
        return this;
    }

    public EmbedBuilder setMessage(final String message) {
        this.embed.setMessage(message);
        return this;
    }

    public EmbedBuilder setURL(final String url) {
        this.embed.setUrl(url);
        return this;
    }

    public EmbedBuilder setTimestamp(final String timestamp) {
        this.embed.setTimestamp(timestamp);
        return this;
    }

    public EmbedBuilder setAuthor(final Author author) {
        this.embed.setAuthor(author);
        return this;
    }

    public EmbedBuilder setColor(final Color color) {
        this.embed.setColor(color);
        return this;
    }

    public EmbedBuilder setTTS(final boolean tts) {
        this.embed.setTts(tts);
        return this;
    }

    public EmbedBuilder setFields(final List<Field> fields) {
        this.embed.setFields(fields);
        return this;
    }

    public EmbedBuilder setFooter(final Footer footer) {
        this.embed.setFooter(footer);
        return this;
    }

    public EmbedBuilder setThumbnail(final Image thumbnail) {
        this.embed.setThumbnail(thumbnail);
        return this;
    }

    public EmbedBuilder setImage(final Image image) {
        this.embed.setImage(image);
        return this;
    }

    public Embed build() {
        if (this.embed.getMessage() == null && this.embed.getTitle() == null) {
            throw new IllegalStateException("Embed musi zawierać 'Title' bądź 'Message");
        }

        return this.embed;
    }
}
