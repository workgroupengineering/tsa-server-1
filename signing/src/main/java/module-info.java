module tsa.signing {

    requires java.validation;

    requires org.bouncycastle.pkix;

    requires org.bouncycastle.util;

    requires org.bouncycastle.provider;

    requires org.apache.commons.io;

    requires org.apache.commons.lang3;

    requires org.slf4j;

    requires transitive tsa.datetime;

    requires transitive tsa.domain;

    requires static lombok;

    exports dev.mieser.tsa.signing.api;

    exports dev.mieser.tsa.signing.api.exception;

    // API implementation details only required in the config and embedded module

    exports dev.mieser.tsa.signing to tsa.signing.config, tsa.embedded;

    exports dev.mieser.tsa.signing.cert to tsa.signing.config, tsa.embedded;

    exports dev.mieser.tsa.signing.mapper to tsa.signing.config, tsa.embedded;

    exports dev.mieser.tsa.signing.serial to tsa.signing.config, tsa.embedded;

}
