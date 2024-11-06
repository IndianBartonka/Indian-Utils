package pl.indianbartonka.util.argument;

import org.jetbrains.annotations.Nullable;

public record Arg(String name, @Nullable String value) {
}