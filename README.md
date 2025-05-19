# unidbg p7zip benchmark tests
This commit makes some test based on unidbg. By using p7zip 16.02 to run benchmark, and compare the result to other arm to x86 binary translator, we can have a basic understanding of the performance of them.

If you have other benchmark tools can be run in unidbg, you can open an issue here.

### Some changes:
- Build the latest dynarmic.dll for windows. 

- Build 7zr 7zr_static lib7zr.so from [https://github.com/han1202012/7-Zip](https://github.com/han1202012/7-Zip) (Added `setvbuf(stdout, 0, _IOLBF, 0);` and `setvbuf(stderr, 0, _IONBF, 0);` to cpp/lib7zr/CPP/7zip/UI/Console/MainAr.cpp to print result in unidbg).

- unidbg-android/src/test/java/com/test/p7zip/MainActivity.java is for running lib7zr benchmark.

- patch `ARM32SyscallHandler.java` and `ARM64SyscallHandler.java` to support running 7zr.

### Some issues/conclusions:
- unidbg is single thread, so all benchmark is using `-mmt1` argument when running 7zr.

- unicorn2 is based on qemu5, so scores form other environment use qemu5 to compare.

- In unidbg, `7zr b -mm=*` will freeze. So I pick some Codecs from `7zr i` list. Including `LZMA:x5:mt1`, `Delta:4`, `BCJ`, `CRC32:1`, `CRC32:4`, `CRC32:8`, `CRC64`, `SHA256`.

- Each benchmark record contains scores of 7zr b rating, and the running time. 

- `unidbg-scores-unicorn2-dynarmic.txt` contains the test results from unidbg, and some estimates. It has comparison of unicorn2, dynarmic.

- `avd-scores-single-thread.txt` contains the test results from AVD. It has comparsion of native, ndk_translation, houdini, qemu10, qemu10-a72, qemu5. Only arm64 target is tested. Newer qemu has bad performance when emulating arm64, so an extra test using `-cpu cortex-a72` is included.

- `mumu-termux-scores-single-thread.txt` contains the test results from mumu emulator's termux environment. To support running hqemu (based on very old qemu), most of the scores is from 7zr_static. It has comparsion of native, hqemu, houdini, qemu5.

### Belows are original README.

# unidbg

Allows you to emulate an Android native library, and an experimental iOS emulation.<br>

This is an educational project to learn more about the ELF/MachO file format and ARM assembly.<br>

Use it at your own risk !

## License
- unidbg uses software libraries from [Apache Software Foundation](http://apache.org). 

Simple tests under src/test directory
- [unidbg-android/src/test/java/com/bytedance/frameworks/core/encrypt/TTEncrypt.java](https://github.com/zhkl0228/unidbg/blob/master/unidbg-android/src/test/java/com/bytedance/frameworks/core/encrypt/TTEncrypt.java)  

![](assets/TTEncrypt.gif)
***
- [unidbg-android/src/test/java/com/sun/jna/JniDispatch32.java](https://github.com/zhkl0228/unidbg/blob/master/unidbg-android/src/test/java/com/sun/jna/JniDispatch32.java)  
![](assets/JniDispatch32.gif)
***
- [unidbg-android/src/test/java/com/sun/jna/JniDispatch64.java](https://github.com/zhkl0228/unidbg/blob/master/unidbg-android/src/test/java/com/sun/jna/JniDispatch64.java)  
![](assets/JniDispatch64.gif)
***
- [unidbg-android/src/test/java/org/telegram/messenger/Utilities32.java](https://github.com/zhkl0228/unidbg/blob/master/unidbg-android/src/test/java/org/telegram/messenger/Utilities32.java)  
![](assets/Utilities32.gif)
***
- [unidbg-android/src/test/java/org/telegram/messenger/Utilities64.java](https://github.com/zhkl0228/unidbg/blob/master/unidbg-android/src/test/java/org/telegram/messenger/Utilities64.java)  
![](assets/Utilities64.gif)

## More tests
- [unidbg-android/src/test/java/com/github/unidbg/android/QDReaderJni.java](https://github.com/zhkl0228/unidbg/blob/master/unidbg-android/src/test/java/com/github/unidbg/android/QDReaderJni.java)
- [unidbg-android/src/test/java/com/anjuke/mobile/sign/SignUtil.java](https://github.com/zhkl0228/unidbg/blob/master/unidbg-android/src/test/java/com/anjuke/mobile/sign/SignUtil.java)

## Features
- Emulation of the JNI Invocation API so JNI_OnLoad can be called.
- Support JavaVM, JNIEnv.
- Emulation of syscalls instruction.
- Support ARM32 and ARM64.
- Inline hook, thanks to [Dobby](https://github.com/jmpews/Dobby).
- Android import hook, thanks to [xHook](https://github.com/iqiyi/xHook).
- iOS [fishhook](https://github.com/facebook/fishhook) and substrate and [whale](https://github.com/asLody/whale) hook.
- [unicorn](https://github.com/zhkl0228/unicorn) backend support simple console debugger, gdb stub, instruction trace, memory read/write trace.
- Support iOS objc and swift runtime.
- Support [dynarmic](https://github.com/MerryMage/dynarmic) fast backend.
- Support Apple M1 hypervisor, the fastest ARM64 backend.
- Support Linux KVM backend with Raspberry Pi B4.

## Thanks
- [unicorn](https://github.com/zhkl0228/unicorn)
- [dynarmic](https://github.com/MerryMage/dynarmic)
- [HookZz](https://github.com/jmpews/Dobby)
- [xHook](https://github.com/iqiyi/xHook)
- [AndroidNativeEmu](https://github.com/AeonLucid/AndroidNativeEmu)
- [usercorn](https://github.com/lunixbochs/usercorn)
- [keystone](https://github.com/keystone-engine/keystone)
- [capstone](https://github.com/aquynh/capstone)
- [idaemu](https://github.com/36hours/idaemu)
- [jelf](https://github.com/fornwall/jelf)
- [whale](https://github.com/asLody/whale)
- [kaitai_struct](https://github.com/kaitai-io/kaitai_struct)
- [fishhook](https://github.com/facebook/fishhook)
- [runtime_class-dump](https://github.com/Tyilo/runtime_class-dump)
- [mman-win32](https://github.com/mcgarrah/mman-win32)

## Stargazers over time

[![Stargazers over time](https://starchart.cc/zhkl0228/unidbg.svg)](https://starchart.cc/zhkl0228/unidbg)

