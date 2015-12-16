package com.intellij.openapi.util;

import java.util.Locale;

/**
 * Created by momo- on 22/10/2015.
 */
public class SystemInfoRt {
    public static final String OS_NAME = System.getProperty("os.name");
    public static final String OS_VERSION = System.getProperty("os.version").toLowerCase(Locale.US);
    public static final boolean isUnix = !isWindows && !isOS2;
    public static final boolean isFileSystemCaseSensitive = isUnix && !isMac ||
            "true".equalsIgnoreCase(System.getProperty("idea.case.sensitive.fs"));
    private static final String _OS_NAME = OS_NAME.toLowerCase(Locale.US);
    public static final boolean isWindows = _OS_NAME.startsWith("windows");
    public static final boolean isOS2 = _OS_NAME.startsWith("os/2") || _OS_NAME.startsWith("os2");
    public static final boolean isMac = _OS_NAME.startsWith("mac");
    public static final boolean isLinux = _OS_NAME.startsWith("linux");
    public static final boolean isFreeBSD = _OS_NAME.startsWith("freebsd");
    public static final boolean isSolaris = _OS_NAME.startsWith("sunos");
}
