package dev.mieser.tsa.web;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.mutable.MutableObject;
import org.springframework.stereotype.Service;

/**
 * Service to retrieve the current version of the application from the Manifest.
 */
@Slf4j
@Service("applicationVersionService")
public class ApplicationVersionService {

    /**
     * Caches the read application version.
     */
    private final MutableObject<String> cachedVersion = new MutableObject<>(null);

    /**
     * @return The current application version.
     */
    public String getApplicationVersion() {
        synchronized (cachedVersion) {
            if (cachedVersion.getValue() == null) {
                cachedVersion.setValue(getClass().getPackage().getImplementationVersion());
            }
        }

        return cachedVersion.getValue();
    }

}
