package pl.indianbartonka.util.http;

import java.util.Arrays;
import pl.indianbartonka.util.annotation.Since;

/**
 * Copyright 2025 Javalin (or its contributors)
 * Code used from:
 * <a href="https://github.com/javalin/javalin/blob/5ddb1def9f36c2731c90afdbd4de6556c0b0102b/javalin/src/main/java/io/javalin/http/ContentType.kt#L4">Javalin Github</a>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the
 * <p>
 * <a href="http://www.apache.org/licenses/LICENSE-2.0">Apache License</a>
 * </p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@Since("0.0.9.5")
public enum ContentType {

    /* Text */
    TEXT_PLAIN("text/plain", true, "txt"),
    TEXT_CSS("text/css", true, "css"),
    TEXT_CSV("text/csv", false, "csv"),
    TEXT_HTML("text/html", true, "html", "htm"),
    TEXT_JS("text/javascript", true, "js", "mjs"),
    TEXT_MARKDOWN("text/markdown", true, "md"),
    TEXT_PROPERTIES("text/x-java-properties", true, "properties"),
    TEXT_XML("text/xml", true, "xml"),

    /* Image */
    IMAGE_AVIF("image/avif", true, "avif"),
    IMAGE_BMP("image/bmp", true, "bmp"),
    IMAGE_GIF("image/gif", true, "gif"),
    IMAGE_ICO("image/vnd.microsoft.icon", true, "ico"),
    IMAGE_JPEG("image/jpeg", true, "jpeg", "jpg"),
    IMAGE_PNG("image/png", true, "png"),
    IMAGE_SVG("image/svg+xml", true, "svg"),
    IMAGE_TIFF("image/tiff", true, "tiff", "tif"),
    IMAGE_WEBP("image/webp", true, "webp"),

    /* Audio */
    AUDIO_AAC("audio/aac", true, "aac"),
    AUDIO_MIDI("audio/midi", true, "mid", "midi"),
    AUDIO_MPEG("audio/mpeg", true, "mp3"),
    AUDIO_OGA("audio/ogg", true, "oga"),
    AUDIO_OPUS("audio/opus", true, "opus"),
    AUDIO_WAV("audio/wav", true, "wav"),
    AUDIO_WEBA("audio/weba", true, "waba"),

    /* Video */
    VIDEO_AVI("video/x-msvideo", true, "avi"),
    VIDEO_MP4("video/mp4", true, "mp4"),
    VIDEO_MPEG("video/mpeg", true, "mpeg"),
    VIDEO_OGG("video/ogg", true, "ogg"),
    VIDEO_WEBM("video/webm", true, "webm"),

    /* Font */
    FONT_OTF("font/otf", false, "otf"),
    FONT_TTF("font/ttf", false, "ttf"),
    FONT_WOFF("font/woff", false, "woff"),
    FONT_WOFF2("font/woff2", false, "woff2"),

    /* Application */
    APPLICATION_OCTET_STREAM("application/octet-stream", false, "bin"),
    APPLICATION_BZ("application/x-bzip", false, "bz"),
    APPLICATION_BZ2("application/x-bzip2", false, "bz2"),
    APPLICATION_CDN("application/cdn", true, "cdn"),
    APPLICATION_DOC("application/msword", false, "doc"),
    APPLICATION_DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document", false, "docx"),
    APPLICATION_EPUB("application/epub+zip", false, "epub"),
    APPLICATION_GZ("application/gzip", false, "gz"),
    APPLICATION_JSON("application/json", true, "json"),
    APPLICATION_MPKG("application/vnd.apple.installer+xml", false, "mpkg"),
    APPLICATION_JAR("application/java-archive", false, "jar"),
    APPLICATION_PDF("application/pdf", true, "pdf"),
    APPLICATION_POM("application/xml", true, "pom"),
    APPLICATION_RAR("application/vnd.rar", false, "rar"),
    APPLICATION_SH("application/x-sh", true, "sh"),
    APPLICATION_SWF("application/x-shockwave-flash", false, "swf"),
    APPLICATION_TAR("application/x-tar", false, "tar"),
    APPLICATION_XHTML("application/xhtml+xml", true, "xhtml"),
    APPLICATION_YAML("application/yaml", true, "yaml", "yml"),
    APPLICATION_ZIP("application/zip", false, "zip"),
    APPLICATION_X_ZIP_COMPRESSED("application/x-zip-compressed", false, "zip"),
    APPLICATION_7Z("application/x-7z-compressed", false, "7z"),

    /* Other */
    MULTIPART_FORM_DATA("multipart/form-data", false, "multipart/form-data");

    private final String mimeType;
    private final boolean isHumanReadable;
    private final String[] extensions;

    ContentType(final String mimeType, final boolean isHumanReadable, final String... extensions) {
        this.mimeType = mimeType;
        this.isHumanReadable = isHumanReadable;
        this.extensions = extensions;
    }

    public static ContentType getContentType(final String mimeType) {
        return Arrays.stream(values())
                .filter(ct -> ct.mimeType.equalsIgnoreCase(mimeType))
                .findFirst()
                .orElse(null);
    }

    public static ContentType getContentTypeByExtension(final String extension) {
        return Arrays.stream(values())
                .filter(ct -> Arrays.stream(ct.extensions).anyMatch(ext -> ext.equalsIgnoreCase(extension)))
                .findFirst()
                .orElse(null);
    }

    public static String getMimeTypeByExtension(final String extension) {
        final ContentType contentType = getContentTypeByExtension(extension);
        return (contentType != null ? contentType.getMimeType() : null);
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public boolean isHumanReadable() {
        return this.isHumanReadable;
    }

    public String[] getExtensions() {
        return this.extensions;
    }

    @Override
    public String toString() {
        return this.mimeType;
    }
}

