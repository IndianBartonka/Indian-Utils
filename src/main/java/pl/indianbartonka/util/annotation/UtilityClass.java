import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) // Adnotacja może być używana tylko na klasach
@Retention(RetentionPolicy.CLASS) // Adnotacja dostępna w czasie kompilacji
public @interface UtilityClass {
}

//TODO: Dodaj tą adnotacje w klasach takich jak ZipUtil.itp aby kod ładniej wygladał
//Dodaj po skończeniu info/todo żeby kiedyś ta adnotacja robiła coś więcej niż wygladala xD
