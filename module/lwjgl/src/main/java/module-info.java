/// The LWJGL module of NativeList.
///
/// @since 1.0.0
module io.github.overrun.nativelist.lwjgl {
    exports io.github.overrun.nativelist.lwjgl;

    requires transitive io.github.overrun.nativelist;
    requires static org.jspecify;
    requires org.lwjgl;
}
