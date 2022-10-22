module tsa.datetime.impl {
    requires static lombok;

    requires spring.context;

    requires tsa.datetime.api;

    exports dev.mieser.tsa.datetime.config;

    opens dev.mieser.tsa.datetime.config;
}
