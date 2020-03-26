package com.everee.actions.openapidiff.util;

import lombok.NonNull;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public abstract class MessageUtils {

  private static final ResourceBundle bundle = ResourceBundle.getBundle("messages");

  public static String getMessage(@NonNull String key, Object... args) {
    var pattern = bundle.getString(key);
    return MessageFormat.format(pattern, args);
  }
}
