package com.intellij.openapi.util;

import java.io.File;
import java.util.Arrays;
import java.util.List;

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
    public static final boolean hasXdgOpen = isUnix && new File("/usr/bin/xdg-open").canExecute();
    public static final boolean isMacSystemMenu = isMac && "true".equals(System.getProperty("apple.laf.useScreenMenuBar"));
    public static final boolean isFileSystemCaseSensitive = !isWindows && !isOS2 && !isMac;
    public static final boolean areSymLinksSupported = isUnix ||
            isWindows && OS_VERSION.compareTo("6.0") >= 0 && isJavaVersionAtLeast("1.7");
    public static final boolean is32Bit = ARCH_DATA_MODEL == null || ARCH_DATA_MODEL.equals("32");
    public static final boolean is64Bit = !is32Bit;
    public static final boolean isAMD64 = "amd64".equals(OS_ARCH);
    public static final boolean isMacIntel64 = isMac && "x86_64".equals(OS_ARCH);

    /**
     * Whether IDEA is running under MacOS X version 10.4 or later.
     *
     * @since 5.0.2
     */
    public static final boolean isMacOSTiger = isTiger();
    /**
     * Whether IDEA is running under MacOS X on an Intel Machine
     *
     * @since 5.0.2
     */
    public static final boolean isIntelMac = isIntelMac();
    /**
     * Running under MacOS X version 10.5 or later;
     *
     * @since 7.0.2
     */
    public static final boolean isMacOSLeopard = isLeopard();
    /**
     * Running under MacOS X version 10.6 or later;
     *
     * @since 9.0
     */
    public static final boolean isMacOSSnowLeopard = isSnowLeopard();
    /**
     * Running under MacOS X version 10.7 or later;
     *
     * @since 11.0
     */
    public static final boolean isMacOSLion = isLion();
    /**
     * Running under MacOS X version 10.8 or later;
     *
     * @since 11.1
     */
    public static final boolean isMacOSMountainLion = isMountainLion();
    private static final String _SUN_DESKTOP = SUN_DESKTOP.toLowerCase();
    public static final boolean isKDE = _SUN_DESKTOP.contains("kde");
    public static final boolean isGnome = _SUN_DESKTOP.contains("gnome");

    public static final String nativeFileManagerName = isMac ? "Finder" :
            isGnome ? "Nautilus" :
                    isKDE ? "Konqueror" :
                            isWindows ? "Explorer" :
                                    "File Manager";
    /**
     * Operating system is supposed to have middle mouse button click occupied by paste action.
     *
     * @since 6.0
     */
    public static boolean X11PasteEnabledSystem = isUnix && !isMac;

    private static boolean isIntelMac() {
        return isMac && "i386".equals(OS_ARCH);
    }

    private static boolean isTiger() {
        return isMac &&
                !OS_VERSION.startsWith("10.0") &&
                !OS_VERSION.startsWith("10.1") &&
                !OS_VERSION.startsWith("10.2") &&
                !OS_VERSION.startsWith("10.3");
    }

    private static boolean isLeopard() {
        return isMac && isTiger() && !OS_VERSION.startsWith("10.4");
    }

    private static boolean isSnowLeopard() {
        return isMac && isLeopard() && !OS_VERSION.startsWith("10.5");
    }

    private static boolean isLion() {
        return isMac && isSnowLeopard() && !OS_VERSION.startsWith("10.6");
    }

    private static boolean isMountainLion() {
        return isMac && isLion() && !OS_VERSION.startsWith("10.7");
    }

    public static String getMacOSMajorVersion() {
        return getMacOSMajorVersion(OS_VERSION);
    }

    public static String getMacOSMajorVersion(String version) {
        int[] parts = getMacOSVersionParts(version);
        return String.format("%d.%d", parts[0], parts[1]);
    }

    public static String getMacOSVersionCode() {
        return getMacOSVersionCode(OS_VERSION);
    }

    public static String getMacOSMajorVersionCode() {
        return getMacOSMajorVersionCode(OS_VERSION);
    }

    public static String getMacOSMinorVersionCode() {
        return getMacOSMinorVersionCode(OS_VERSION);
    }

    public static String getMacOSVersionCode(String version) {
        int[] parts = getMacOSVersionParts(version);
        return String.format("%02d%d%d", parts[0], normalize(parts[1]), normalize(parts[2]));
    }

    public static String getMacOSMajorVersionCode(String version) {
        int[] parts = getMacOSVersionParts(version);
        return String.format("%02d%d%d", parts[0], normalize(parts[1]), 0);
    }

    public static String getMacOSMinorVersionCode(String version) {
        int[] parts = getMacOSVersionParts(version);
        return String.format("%02d%02d", parts[1], parts[2]);
    }

    private static int[] getMacOSVersionParts(String version) {
        List<String> parts = Arrays.asList(version.split("."));
        while (parts.size() < 3) {
            parts.add("0");
        }
        return new int[]{toInt(parts.get(0)), toInt(parts.get(1)), toInt(parts.get(2))};
    }

    private static int normalize(int number) {
        return number > 9 ? 9 : number;
    }

    private static int toInt(String string) {
        try {
            return Integer.valueOf(string);
        }
        catch (NumberFormatException e) {
            return 0;
        }
    }

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

    public static boolean isJavaVersionAtLeast(String v) {
        return compareVersionNumbers(JAVA_RUNTIME_VERSION, v) >= 0;
    }
}
