package io.weesvc.springboot.weesvc.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Place not found")
public class PlaceNotFoundException extends Exception {

}
