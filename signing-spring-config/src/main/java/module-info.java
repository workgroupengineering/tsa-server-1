open module tsa.signing.config {

    requires java.validation;

    requires spring.boot;

    requires spring.context;

    requires spring.boot.autoconfigure;

    requires tsa.domain;

    requires tsa.signing;

    requires tsa.datetime;

    requires tsa.datetime.config;

    requires org.mapstruct;

    requires java.compiler;

    requires static lombok;

    exports dev.mieser.tsa.signing.config;

}
