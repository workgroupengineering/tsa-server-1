module tsa.datetime {

    requires static lombok;

    exports dev.mieser.tsa.datetime.api;

    // API implementation details only required in config and embedded module

    exports dev.mieser.tsa.datetime to tsa.datetime.config, tsa.embedded;

}
