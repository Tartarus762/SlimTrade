package com.slimtrade.core;

import java.awt.*;
import java.util.regex.Pattern;

public class References {

    // GitHub Info
    public static final String AUTHOR = "zmilla93";
    public static final String GITHUB_REPO = "SlimTrade";

    // Links
    public static final String GITHUB_URL = "https://github.com/zmilla93/SlimTrade";
    public static final String GITHUB_ISSUES_URL = "https://github.com/zmilla93/SlimTrade/issues/new/choose";
    public static final String DISCORD_INVITE = "https://discord.com/invite/yKdExMe";
    public static final String FAQ_URL = "https://github.com/zmilla93/SlimTrade/wiki/Troubleshooting";
    public static final String PAYPAL_URL = "https://www.paypal.com/paypalme/zmilla93";
    public static final String PATREON_URL = "https://www.patreon.com/SlimTrade";

    // Regex
    public static final String REGEX_CLIENT_DATA = "((?<date>\\d{4}\\/\\d{2}\\/\\d{2}) (?<time>\\d{2}:\\d{2}:\\d{2}))?.*] ";
    public static final String REGEX_MESSAGE_PREFIX = REGEX_CLIENT_DATA + "@(?<messageType>От кого|\\S+) (?<guildName><.+> )?(?<playerName>.+):(\\s+)(?<message>";
    public static final String REGEX_CLIENT_CHAT_PREFIX = REGEX_CLIENT_DATA + "(?<messageType>От кого|[^\\s<>]+) ?(?<guildName><.+> )?(?<playerName>.+):(\\s+)(?<message>.+";
    public static final String REGEX_CLIENT_META_PREFIX = REGEX_CLIENT_DATA + "(: )?(?<message>.+";
    public static final String REGEX_QUICK_PASTE_PREFIX = "@(?<guildName><.+> )?(?<playerName>.+)(\\s+)(?<message>";
    public static final String REGEX_JOINED_AREA_PREFIX = "(.+ : (?<playerName>.+) ";
    public static final String REGEX_ENTERED_AREA_PREFIX = ": ";
    public static final String REGEX_SUFFIX = ")";
    public static final String APP_PREFIX = "SlimTradeApp::";

    public static final Pattern chatPatten = Pattern.compile(REGEX_CLIENT_CHAT_PREFIX + REGEX_SUFFIX);
    public static final Pattern clientMetaPattern = Pattern.compile(REGEX_CLIENT_META_PREFIX + REGEX_SUFFIX);

    public static final Point DEFAULT_MESSAGE_LOCATION = new Point(800, 0);
    public static final Point DEFAULT_MENUBAR_LOCATION = new Point(0, 0);

}
