package github.zmilla93.core.poe;

import github.zmilla93.core.enums.ResultStatus;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Results when validating the path to A
 */
public enum PoeClientPath {

    INCORRECT_FOLDER_NAME(ResultStatus.DENY, "Folder name must be '%s'.", true),
    MISSING_POE_FOLDER(ResultStatus.DENY, "The '%s' no longer exists.", true),
    MISSING_LOGS_FOLDER(ResultStatus.INDETERMINATE, "Folder name is correct, but is missing a '" + GameSettings.LOG_FOLDER_NAME + "' folder."),
    MISSING_CLIENT_FILE(ResultStatus.INDETERMINATE, "%s's " + GameSettings.LOG_FOLDER_NAME + " folder is missing a " + GameSettings.CLIENT_TXT_NAME + " file.", true),
    VALID(ResultStatus.APPROVE, "Folder set correctly."),

    DETECTED_SINGLE(ResultStatus.APPROVE, "Folder auto detected."),
    DETECTED_MULTIPLE(ResultStatus.INDETERMINATE, "Multiple folders auto detected."),
    ;

    public final ResultStatus status;
    public final String message;
    public final boolean requiresFormatting;

    PoeClientPath(ResultStatus status, String message) {
        this(status, message, false);
    }

    PoeClientPath(ResultStatus status, String message, boolean requiresFormatting) {
        this.status = status;
        this.message = message;
        this.requiresFormatting = requiresFormatting;
    }

    /// Some messages require formatting
    public String getMessage(Game game) {
        if (requiresFormatting) return String.format(message, game);
        else return message;
    }

    public static boolean isValidInstallFolder(Game game, String pathString) {
        if (pathString == null) return false;
        return isValidInstallFolder(game, Paths.get(pathString));
    }

    public static boolean isValidInstallFolder(Game game, Path path) {
        return validateInstallFolder(game, path).status == ResultStatus.APPROVE;
    }

    public static PoeClientPath validateInstallFolder(Game game, Path poeFolderPath) {
        if (poeFolderPath == null) return MISSING_POE_FOLDER;
        if (!poeFolderPath.endsWith(game.toString())) return INCORRECT_FOLDER_NAME;
        if (!poeFolderPath.toFile().exists()) return MISSING_POE_FOLDER;
        Path logsFolder = poeFolderPath.resolve(GameSettings.LOG_FOLDER_NAME);
        if (!logsFolder.toFile().exists()) return MISSING_LOGS_FOLDER;
        Path clientFile = logsFolder.resolve(GameSettings.CLIENT_TXT_NAME);
        if (!clientFile.toFile().exists()) return MISSING_CLIENT_FILE;
        return VALID;
    }

    // FIXME : Auto path detection

}
