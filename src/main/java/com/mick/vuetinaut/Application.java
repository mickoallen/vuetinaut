package com.mick.vuetinaut;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Vuetinaut",
                version = "0.1",
                description = "A template project for prototyping things"
        )
)
public class Application {
    public static void main(String[] args) {
        Micronaut.run(Application.class);
    }
}