// currently not in use because of major problems with JPMS in native executables
open module tsa.domain {
    requires static lombok;

    exports dev.mieser.tsa.domain;
}
