package pl.indianbartonka.util;

import java.awt.Color;
import org.junit.jupiter.api.Test;
import pl.indianbartonka.util.color.AnsiColor;

public class AnsiColorTest {

    @Test
    public void testColors() {
        System.out.println("&0: " + AnsiColor.convertMinecraftColors("&0Black"));
        System.out.println("&1: " + AnsiColor.convertMinecraftColors("&1Dark Blue"));
        System.out.println("&2: " + AnsiColor.convertMinecraftColors("&2Dark Green"));
        System.out.println("&3: " + AnsiColor.convertMinecraftColors("&3Dark Aqua"));
        System.out.println("&4: " + AnsiColor.convertMinecraftColors("&4Dark Red"));
        System.out.println("&5: " + AnsiColor.convertMinecraftColors("&5Dark Purple"));
        System.out.println("&6: " + AnsiColor.convertMinecraftColors("&6Gold"));
        System.out.println("&7: " + AnsiColor.convertMinecraftColors("&7Gray"));
        System.out.println("&8: " + AnsiColor.convertMinecraftColors("&8Dark Gray"));
        System.out.println("&9: " + AnsiColor.convertMinecraftColors("&9Blue"));
        System.out.println("&a: " + AnsiColor.convertMinecraftColors("&aGreen"));
        System.out.println("&b: " + AnsiColor.convertMinecraftColors("&bAqua"));
        System.out.println("&c: " + AnsiColor.convertMinecraftColors("&cRed"));
        System.out.println("&d: " + AnsiColor.convertMinecraftColors("&dLight Purple"));
        System.out.println("&e: " + AnsiColor.convertMinecraftColors("&eYellow"));
        System.out.println("&f: " + AnsiColor.convertMinecraftColors("&fWhite"));
        System.out.println("&g: " + AnsiColor.convertMinecraftColors("&gMinecoin Gold"));
        System.out.println("&h: " + AnsiColor.convertMinecraftColors("&hMaterial Quartz"));
        System.out.println("&i: " + AnsiColor.convertMinecraftColors("&iMaterial Iron"));
        System.out.println("&j: " + AnsiColor.convertMinecraftColors("&jMaterial Netherite"));
        System.out.println("&p: " + AnsiColor.convertMinecraftColors("&pMaterial Gold"));
        System.out.println("&q: " + AnsiColor.convertMinecraftColors("&qMaterial Emerald"));
        System.out.println("&s: " + AnsiColor.convertMinecraftColors("&sMaterial Diamond"));
        System.out.println("&t: " + AnsiColor.convertMinecraftColors("&tMaterial Lapis"));
        System.out.println("&u: " + AnsiColor.convertMinecraftColors("&uMaterial Amethyst"));

        System.out.println();
        System.out.println();

        System.out.println("&k: " + AnsiColor.convertMinecraftColors("&kObfuscated"));
        System.out.println("&l: " + AnsiColor.convertMinecraftColors("&lBold"));
        System.out.println("&m: " + AnsiColor.convertMinecraftColors("&mStrikethrough"));
        System.out.println("&n: " + AnsiColor.convertMinecraftColors("&nUnderline"));
        System.out.println("&o: " + AnsiColor.convertMinecraftColors("&oItalic"));
        System.out.println("&r: " + AnsiColor.convertMinecraftColors("&rReset"));

        System.out.println();
        System.out.println();
        System.out.println("Special Colors");
        System.out.println("&g " + AnsiColor.convertMinecraftColors("&gMinecoin Gold"));
        System.out.println("&h " + AnsiColor.convertMinecraftColors("&hMaterial Quartz"));
        System.out.println("&i " + AnsiColor.convertMinecraftColors("&iMaterial Iron"));
        System.out.println("&j " + AnsiColor.convertMinecraftColors("&jMaterial Netherite"));
        System.out.println("&p " + AnsiColor.convertMinecraftColors("&pMaterial Gold"));
        System.out.println("&q " + AnsiColor.convertMinecraftColors("&qMaterial Emerald"));
        System.out.println("&s " + AnsiColor.convertMinecraftColors("&sMaterial Diamond"));
        System.out.println("&t " + AnsiColor.convertMinecraftColors("&tMaterial Lapis"));
        System.out.println("&u " + AnsiColor.convertMinecraftColors("&uMaterial Amethyst"));

        System.out.println();
        System.out.println();
        System.out.println("Color Tester");

        //Wymaga to usprawnien
        final Color color = Color.GREEN;

        final String rgbColor = AnsiColor.getMinecraftColor(color.getRed(), color.getBlue(), color.getGreen());

        System.out.println("RGB Color: " + rgbColor + AnsiColor.convertMinecraftColors(rgbColor + "Test"));
    }
}
