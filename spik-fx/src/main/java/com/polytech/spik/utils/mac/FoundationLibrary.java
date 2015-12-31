package com.polytech.spik.utils.mac;


import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Pointer;

/**
 * Created by mfuntowicz on 30/12/15.
 */
public interface FoundationLibrary extends Library {
    int kCFStringEncodingMacRoman = 0;
    int kCFStringEncodingWindowsLatin1 = 0x0500;
    int kCFStringEncodingISOLatin1 = 0x0201;
    int kCFStringEncodingNextStepLatin = 0x0B01;
    int kCFStringEncodingASCII = 0x0600;
    int kCFStringEncodingUnicode = 0x0100;
    int kCFStringEncodingUTF8 = 0x08000100;
    int kCFStringEncodingNonLossyASCII = 0x0BFF;
    int kCFStringEncodingUTF16 = 0x0100;
    int kCFStringEncodingUTF16BE = 0x10000100;
    int kCFStringEncodingUTF16LE = 0x14000100;
    int kCFStringEncodingUTF32 = 0x0c000100;
    int kCFStringEncodingUTF32BE = 0x18000100;
    int kCFStringEncodingUTF32LE = 0x1c000100;

    void NSLog(Pointer pString, Object thing);

    ID NSFullUserName();

    ID objc_allocateClassPair(ID supercls, String name, int extraBytes);

    void objc_registerClassPair(ID cls);

    ID CFStringCreateWithBytes(Pointer allocator, byte[] bytes, int byteCount, int encoding, byte isExternalRepresentation);

    byte CFStringGetCString(ID theString, byte[] buffer, int bufferSize, int encoding);

    int CFStringGetLength(ID theString);

    long CFStringConvertNSStringEncodingToEncoding(long nsEncoding);

    ID CFStringConvertEncodingToIANACharSetName(long cfEncoding);

    long CFStringConvertIANACharSetNameToEncoding(ID encodingName);

    long CFStringConvertEncodingToNSStringEncoding(long cfEncoding);

    void CFRetain(ID cfTypeRef);

    void CFRelease(ID cfTypeRef);

    int CFGetRetainCount (Pointer cfTypeRef);

    ID objc_getClass(String className);

    ID objc_getProtocol(String name);

    ID class_createInstance(ID pClass, int extraBytes);

    Pointer sel_registerName(String selectorName);

    ID class_replaceMethod(ID cls, Pointer selName, Callback impl, String types);

    ID objc_getMetaClass(String name);

    ID objc_msgSend(ID receiver, Pointer selector, Object... args);

    boolean class_respondsToSelector(ID cls, Pointer selName);

    boolean class_addMethod(ID cls, Pointer selName, Callback imp, String types);

    boolean class_addMethod(ID cls, Pointer selName, ID imp, String types);

    boolean class_addProtocol(ID aClass, ID protocol);

    boolean class_isMetaClass(ID cls);

    ID NSStringFromSelector(Pointer selector);

    Pointer objc_getClass(Pointer clazz);
}
