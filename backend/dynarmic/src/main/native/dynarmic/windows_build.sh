# build script for windows msys2

JAVA_INC="$JAVA_HOME"/include
JAVA_PLATFORM_INC="$(dirname "$(find "$JAVA_INC" -name jni_md.h)")"

g++ -m64 -o dynarmic.dll -shared -fPIC -std=c++17 -O2 -static -DDYNARMIC_MASTER \
  -I ~/git/dynarmic/src -I ~/git/dynarmic/externals/fmt/include \
  dynarmic.cpp arm_dynarmic_cp15.cpp mman.c \
  -I "$JAVA_INC" -I "$JAVA_PLATFORM_INC" \
  ~/git/dynarmic/build/src/dynarmic/libdynarmic.a \
  ~/git/dynarmic/build/externals/fmt/libfmt.a \
  ~/git/dynarmic/build/externals/mcl/src/libmcl.a \
  ~/git/dynarmic/build/externals/zydis/libZydis.a && \
  mv dynarmic.dll ../../resources/natives/windows_64/
