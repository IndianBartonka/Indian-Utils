//TODO: Dodać HTTP util z agentami i innymi opcjami nagłówków 
// dodać HTTPCode które będzie enum i będzie miało opcję getCode(int) itp

public class HTTPUtil {

    // Wersja przeglądarek
    private static final String VERSION = "91.0.4472.114";

    // Przeglądarki
    public static final String USER_AGENT_CHROME = 
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/" + VERSION + " Safari/537.36";
    public static final String USER_AGENT_FIREFOX = 
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:89.0) Gecko/20100101 Firefox/89.0";
    public static final String USER_AGENT_SAFARI = 
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Version/" + VERSION + " Safari/537.36";
    public static final String USER_AGENT_OPERA = 
        "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/" + VERSION + " OPR/77.0.4054.90";
    public static final String USER_AGENT_EDGE = 
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/" + VERSION + " Safari/537.36 Edg/91.0.864.64";
    public static final String USER_AGENT_TOR = 
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:10.0) Gecko/20100101 Firefox/10.0";

    // Aplikacje mobilne
    public static final String USER_AGENT_ANDROID = 
        "Mozilla/5.0 (Linux; U; Android 4.4.2; en-US; Nexus 5 Build/KOT49H) AppleWebKit/537.36 (KHTML, like Gecko) Version/" + VERSION + " Mobile Safari/537.36";
    public static final String USER_AGENT_WHATSAPP = 
        "WhatsApp/2.21.2.18 A";

    // Narzędzia i biblioteki
    public static final String USER_AGENT_CURL = 
        "curl/7.64.1";
    public static final String USER_AGENT_OKHTTP = 
        "okhttp/3.12.1";

    // Metoda pomocnicza do drukowania wszystkich user-agentów
    public static void printAllUserAgents() {
        System.out.println("Popularne User-Agenty:");
        System.out.println("Chrome: " + USER_AGENT_CHROME);
        System.out.println("Firefox: " + USER_AGENT_FIREFOX);
        System.out.println("Safari: " + USER_AGENT_SAFARI);
        System.out.println("Opera: " + USER_AGENT_OPERA);
        System.out.println("Edge: " + USER_AGENT_EDGE);
        System.out.println("Tor: " + USER_AGENT_TOR);
        System.out.println("Android: " + USER_AGENT_ANDROID);
        System.out.println("WhatsApp: " + USER_AGENT_WHATSAPP);
        System.out.println("cURL: " + USER_AGENT_CURL);
        System.out.println("OkHttp: " + USER_AGENT_OKHTTP);
    }
}

//TODO: Dodać opcje Build UserAgent 
