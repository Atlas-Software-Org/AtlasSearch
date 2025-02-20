package de.glowman554.search.utils;

public class Logger {

    private static void print(String format, Object... args) {
        String message = format;
        if (args.length > 0) {
            message = String.format(format, args);
        }

        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StringBuilder newMessage = new StringBuilder();
        for (String line : message.split("\n")) {
            newMessage.append(String.format("[%s::%s at %s:%s] %s\n", stackTraceElements[3].getClassName(), stackTraceElements[3].getMethodName(), stackTraceElements[3].getFileName(), stackTraceElements[3].getLineNumber(), line));
        }

        System.out.println(newMessage.toString().strip());
    }

    public static void log(String format, Object... args) {
        print(format, args);
    }

    public static void exception(Exception e) {
        log("Exception: %s", e.getMessage());
        for (StackTraceElement element : e.getStackTrace()) {
            print("    at %s.%s(%s:%s)", element.getClassName(), element.getMethodName(), element.getFileName(), element.getLineNumber());
        }
    }

}
