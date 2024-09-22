package me.indian.util.discord.embed;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.awt.Color;
import java.util.List;
import me.indian.util.discord.embed.component.Author;
import me.indian.util.discord.embed.component.Field;
import me.indian.util.discord.embed.component.Footer;
import me.indian.util.discord.embed.component.Image;

public class Embed {

    private String title, message, url, timestamp;
    private Author author;
    private boolean tts;
    private List<Field> fields;
    private Color color;
    private Footer footer;
    private Image thumbnail, image;

    public Embed() {
        this.tts = false;
        this.color = Color.GREEN;
        this.footer = new Footer("");
    }

    public JsonObject getEmbedJson() {
        final JsonObject embed = new JsonObject();
        embed.addProperty("title", this.title);
        embed.addProperty("description", this.message);

        if (this.url != null) {
            embed.addProperty("url", this.url);
        }

        if (this.timestamp != null) {
            embed.addProperty("timestamp", this.timestamp);
        }

        if (this.author != null) {
            final JsonObject authorObject = new JsonObject();
            authorObject.addProperty("name", this.author.name());
            authorObject.addProperty("url", this.author.url());
            authorObject.addProperty("icon_url", this.author.iconURL());
            embed.add("author", authorObject);
        }

        if (this.fields != null && !this.fields.isEmpty()) {
            final JsonArray fieldsArray = new JsonArray();
            for (final Field field : this.fields) {
                final JsonObject fieldObject = new JsonObject();
                fieldObject.addProperty("name", field.name());
                fieldObject.addProperty("value", field.value());
                fieldObject.addProperty("inline", field.inline());
                fieldsArray.add(fieldObject);
            }

            embed.add("fields", fieldsArray);
        }

        if (this.thumbnail != null) {
            final JsonObject thumbnailObject = new JsonObject();
            thumbnailObject.addProperty("url", this.thumbnail.url());
            embed.add("thumbnail", thumbnailObject);
        }

        if (this.image != null) {
            final JsonObject imageObject = new JsonObject();
            imageObject.addProperty("url", this.image.url());
            embed.add("image", imageObject);

        }

        final JsonObject footerObject = new JsonObject();
        footerObject.addProperty("text", this.footer.text());
        footerObject.addProperty("icon_url", this.footer.imageURL());

        embed.add("footer", footerObject);
        embed.addProperty("color", (this.color.getRGB() & 0x00FFFFFF));

        return embed;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(final String timestamp) {
        this.timestamp = timestamp;
    }

    public Author getAuthor() {
        return this.author;
    }

    public void setAuthor(final Author author) {
        this.author = author;
    }

    public boolean isTts() {
        return this.tts;
    }

    public void setTts(final boolean tts) {
        this.tts = tts;
    }

    public List<Field> getFields() {
        return this.fields;
    }

    public void setFields(final List<Field> fields) {
        this.fields = fields;
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(final Color color) {
        this.color = color;
    }

    public Footer getFooter() {
        return this.footer;
    }

    public void setFooter(final Footer footer) {
        this.footer = footer;
    }

    public Image getThumbnail() {
        return this.thumbnail;
    }

    public void setThumbnail(final Image thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Image getImage() {
        return this.image;
    }

    public void setImage(final Image image) {
        this.image = image;
    }
}
