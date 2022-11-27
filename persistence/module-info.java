// currently not in use because of major problems with JPMS in native executables
open module tsa.persistence {
    requires jakarta.persistence;

    requires jakarta.validation;

    requires java.compiler;

    requires spring.data.jpa;

    requires spring.data.commons;

    requires spring.context;

    requires spring.boot.autoconfigure;

    requires org.mapstruct;

    requires org.apache.commons.codec;

    requires tsa.domain;

    requires tsa.signing;

    requires org.hibernate.orm.core;

    requires static lombok;

    exports dev.mieser.tsa.persistence.api;

    exports dev.mieser.tsa.persistence.config;
}
