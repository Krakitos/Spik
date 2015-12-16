package com.intellij.openapi.util;

/**
 * Created by momo- on 22/10/2015.
 */
@SuppressWarnings({"HardCodedStringLiteral", "UtilityClassWithoutPrivateConstructor", "UnusedDeclaration"})
public class SystemInfo extends SystemInfoRt {
    public static final String OS_NAME = SystemInfoRt.OS_NAME;
    public static final String OS_VERSION = SystemInfoRt.OS_VERSION;
    public static final String OS_ARCH = System.getProperty("os.arch");
    public static final String JAVA_VERSION = System.getProperty("java.version");
    public static final String JAVA_RUNTIME_VERSION = System.getProperty("java.runtime.version");
    public static final String ARCH_DATA_MODEL = System.getProperty("sun.arch.data.remotes");
    public static final String SUN_DESKTOP = System.getProperty("sun.desktop", "");

    public static final boolean isWindows = SystemInfoRt.isWindows;
    public static final boolean isMac = SystemInfoRt.isMac;
    public static final boolean isOS2 = SystemInfoRt.isOS2;
    public static final boolean isLinux = SystemInfoRt.isLinux;
    public static final boolean isFreeBSD = SystemInfoRt.isFreeBSD;
    public static final boolean isSolaris = SystemInfoRt.isSolaris;
    public static final boolean isUnix = SystemInfoRt.isUnix;

    // version numbers from http://msdn.microsoft.com/en-us/library/windows/desktop/ms724832.aspx
    public static final boolean isWin2kOrNewer = isWindows && isOsVersionAtLeast("5.0");
    public static final boolean isWinVistaOrNewer = isWindows && isOsVersionAtLeast("6.0");
    public static final boolean isWin7OrNewer = isWindows && isOsVersionAtLeast("6.1");
    public static final boolean isWindowsXP = isWindows && (OS_VERSION.equals("5.1") || OS_VERSION.equals("5.2"));

    public static boolean isOsVersionAtLeast(String version) {
        return compareVersionNumbers(OS_VERSION, version) >= 0;
    }

    private static int compareVersionNumbers(String v1, String v2) {
        if (v1 == null && v2 == null) {
            return 0;
        }
        if (v1 == null) {
            return -1;
        }
        if (v2 == null) {
            return 1;
        }

        String[] part1 = v1.split("[\\._\\-]");
        String[] part2 = v2.split("[\\._\\-]");

        int idx = 0;
        for (; idx < part1.length && idx < part2.length; idx++) {
            String p1 = part1[idx];
            String p2 = part2[idx];

            int cmp;
            if (p1.matches("\\d+") && p2.matches("\\d+")) {
                cmp = new Integer(p1).compareTo(new Integer(p2));
            }
            else {
                cmp = part1[idx].compareTo(part2[idx]);
            }
            if (cmp != 0) return cmp;
        }

        if (part1.length == part2.length) {
            return 0;
        }
        else {
            boolean left = part1.length > idx;
            String[] parts = left ? part1 : part2;

            for (; idx < parts.length; idx++) {
                String p = parts[idx];
                int cmp;
                if (p.matches("\\d+")) {
                    cmp = new Integer(p).compareTo(0);
                }
                else {
                    cmp = 1;
                }
                if (cmp != 0) return left ? cmp : -cmp;
            }
            return 0;
        }
    }
}
