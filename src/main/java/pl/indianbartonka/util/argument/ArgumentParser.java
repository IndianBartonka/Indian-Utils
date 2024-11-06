package pl.indianbartonka.util.argument;

import java.util.LinkedList;
import java.util.List;
import org.jetbrains.annotations.Nullable;

public final class ArgumentParser {

    private final List<Arg> argsList = new LinkedList<>();

    public ArgumentParser(final String[] args) {
        for (final String arg : args) {
            if (arg.startsWith("-")) {
                final String[] splitArg = arg.substring(1).split(":", 2);
                if (splitArg.length == 2) {
                    this.argsList.add(new Arg(splitArg[0], splitArg[1]));
                } else {
                    this.argsList.add(new Arg(splitArg[0], null));
                }
            }
        }
    }

    @Nullable
    public Arg getArgByName(final String name) {
        return this.argsList.stream()
                .filter(arg -> arg.name().equalsIgnoreCase(name))
                .findAny()
                .orElse(null);
    }

    public boolean contains(final String name) {
        return this.argsList.stream().anyMatch(arg -> arg.name().equalsIgnoreCase(name));
    }

    public boolean isAnyArgument() {
        return !this.argsList.isEmpty();
    }

    public List<Arg> getArgsList() {
        return this.argsList;
    }
}
