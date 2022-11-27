// currently not in use because of major problems with JPMS in native executables
open module tsa.app {
    requires spring.core;

    requires spring.boot;

    requires spring.beans;

    requires spring.context;

    requires spring.boot.autoconfigure;

    requires tsa.signing;

    requires tsa.web;

    requires static lombok;
}
