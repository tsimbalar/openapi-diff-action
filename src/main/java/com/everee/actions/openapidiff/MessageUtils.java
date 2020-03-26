package com.everee.actions.openapidiff;

import lombok.NonNull;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public abstract class MessageUtils {

  public static String getMessage(@NonNull String key, Object... args) {
    var bundle = ResourceBundle.getBundle("messages");
    var pattern = bundle.getString(key);
    return MessageFormat.format(pattern, args);
  }
}
