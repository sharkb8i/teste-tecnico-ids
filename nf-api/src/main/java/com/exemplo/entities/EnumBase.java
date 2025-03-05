package com.exemplo.entities;

public interface EnumBase {
  String getDescricao();

  static <T extends Enum<T> & EnumBase> T fromString(Class<T> enumClass, String value) {
    for (T constant : enumClass.getEnumConstants()) {
      if (constant.name().equalsIgnoreCase(value))
        return constant;
    }
    throw new IllegalArgumentException("Valor inválido para " + enumClass.getSimpleName() + ": " + value + ".");
  }
}