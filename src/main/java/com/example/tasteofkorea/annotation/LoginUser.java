package com.example.tasteofkorea.annotation;

import com.example.tasteofkorea.entity.User;
import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoginUser {
}
