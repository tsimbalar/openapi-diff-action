package com.everee.actions.openapidiff;

import com.qdesrame.openapi.diff.model.ChangedOpenApi;

public abstract class ChangeTypeUtils {

    public static ChangeType getChangeType(ChangedOpenApi diff) {
        if (!diff.isDiffBackwardCompatible()) {
            return ChangeType.MAJOR;
        } else if (diff.isDiff()) {
            return ChangeType.MINOR;
        } else {
            return ChangeType.PATCH;
        }
    }
}
