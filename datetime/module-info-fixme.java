module tsa.datetime {

    requires spring.context;

    requires kotlin.stdlib;

    exports dev.mieser.tsa.datetime.api;

    exports dev.mieser.tsa.datetime.config;

    opens dev.mieser.tsa.datetime.config;

}
