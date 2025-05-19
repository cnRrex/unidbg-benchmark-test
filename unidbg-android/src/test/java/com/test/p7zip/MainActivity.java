package com.test.p7zip;

import com.github.unidbg.AndroidEmulator;

import com.github.unidbg.LibraryResolver;
import com.github.unidbg.Module;
import com.github.unidbg.arm.backend.DynarmicFactory;
import com.github.unidbg.linux.android.AndroidEmulatorBuilder;
import com.github.unidbg.linux.android.AndroidResolver;
import com.github.unidbg.memory.Memory;
import com.github.unidbg.pointer.UnidbgPointer;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class MainActivity {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        com.test.p7zip.MainActivity mainActivity = new com.test.p7zip.MainActivity();
        System.out.println("load offset=" + (System.currentTimeMillis() - start) + "ms");
        mainActivity.run();
    }

    private final AndroidEmulator emulator;
    private final Module module;
    private Memory memory;

    private MainActivity() {
        emulator = AndroidEmulatorBuilder
                .for64Bit()
                .addBackendFactory(new DynarmicFactory(true)) //dynarmic
                //.addBackendFactory(new Unicorn2Factory(true)) //unicorn2
                .build();
        memory = emulator.getMemory();
        LibraryResolver resolver = new AndroidResolver(23);
        memory.setLibraryResolver(resolver);

        module = emulator.loadLibrary(new File("unidbg-android/src/test/resources/example_binaries/lib7zr/arm64-v8a/lib7zr.so"));

    }


    private void run() {
        String[] args = {"7zr", "b", "-mm=CRC64"};
        String[] envs = {""};
        int argc = args.length;
        UnidbgPointer[] pointers = new UnidbgPointer[argc];
        UnidbgPointer[] env_pointers = new UnidbgPointer[envs.length+1];
        for (int i = 0; i < argc; i++) {
            byte[] data = (args[i] + "\0").getBytes(StandardCharsets.UTF_8);
            UnidbgPointer ptr = memory.malloc(data.length, true).getPointer();
            ptr.write(0, data, 0, data.length);
            pointers[i] = ptr;
        }
        for (int i = 0; i < envs.length; i++) {
            byte[] data = (envs[i] + "\0").getBytes(StandardCharsets.UTF_8);
            UnidbgPointer ptr = memory.malloc(data.length, true).getPointer();
            ptr.write(0, data, 0, data.length);
            env_pointers[i] = ptr;
        }
        UnidbgPointer argvPtr = memory.malloc(argc * emulator.getPointerSize(), true).getPointer();
        for (int i = 0; i < argc; i++) {
            argvPtr.setPointer(i * emulator.getPointerSize(), pointers[i]);
        }
        UnidbgPointer envpPtr = memory.malloc((envs.length+1) * emulator.getPointerSize(), true).getPointer();
        for (int i = 0; i < envs.length+1; i++) {
            envpPtr.setPointer(i * emulator.getPointerSize(), env_pointers[i]);
        }

        long start = System.currentTimeMillis();
        Number result = module.callFunction(emulator, "main", argc, argvPtr, envpPtr);
        System.out.println("Function return: " + result.intValue() + ", off=" + (System.currentTimeMillis() - start) + "ms, =" + (System.currentTimeMillis() - start)/1000 + "s");
    }
}




