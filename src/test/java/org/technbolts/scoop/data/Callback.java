package org.technbolts.scoop.data;

public interface Callback<T> {
    void onSuccess(T value);
    void onError(String message, Throwable error);
}
