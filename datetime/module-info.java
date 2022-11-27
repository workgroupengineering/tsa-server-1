// currently not in use because of major problems with JPMS in native executables
open module tsa.currenttime {
    requires static lombok;

    requires spring.context;

    exports dev.mieser.tsa.datetime.api;

    exports dev.mieser.tsa.datetime.config;
}
