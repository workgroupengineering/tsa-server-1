// currently not in use because of major problems with JPMS in native executables
open module tsa.integration {
    requires spring.context;

    requires spring.beans;

    requires spring.data.commons;

    requires org.apache.commons.codec;

    requires org.slf4j;

    requires tsa.domain;

    requires tsa.signing;

    requires tsa.persistence;

    requires static lombok;

    exports dev.mieser.tsa.integration.api;

    exports dev.mieser.tsa.integration.config;
}
