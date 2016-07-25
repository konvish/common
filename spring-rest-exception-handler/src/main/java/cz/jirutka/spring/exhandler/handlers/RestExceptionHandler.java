package cz.jirutka.spring.exhandler.handlers;

import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface RestExceptionHandler<E extends Exception, T> {
    ResponseEntity<T> handleException(E var1, HttpServletRequest var2);
}
