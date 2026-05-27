package com.bolivar.bioingenieria.app.sigma_bb.bootstrap.config.open_api;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de OpenAPI / Swagger para la aplicación.
 *
 * Esta clase expone beans que personalizan la información del API (título,
 * descripción, contacto, licencia) y agrega la configuración de seguridad
 * para que la interfaz de Swagger UI pueda ser utilizada con tokens Bearer/JWT.
 *
 * También se exponen grupos de APIs para segmentar la documentación por módulos
 * (por ejemplo: personas).
 */
@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearer-jwt";

    /**
     * Bean principal que personaliza el documento OpenAPI (Info y Security).
     *
     * @return instancia de {@link OpenAPI} con metadata y esquema de seguridad
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sigma BB API")
                        .version("v0.0.1")
                        .description("API REST para la gestión de personas y recursos de Bolivar Bioingeniería")
                        .contact(new Contact().name("Equipo Bioingeniería").email("devops@bolivar.com"))
                        .license(new License().name("MIT").url("https://opensource.org/licenses/MIT"))
                )
                // Define esquema de seguridad Bearer JWT para los endpoints protegidos
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .schemaRequirement(SECURITY_SCHEME_NAME, new SecurityScheme()
                        .name(SECURITY_SCHEME_NAME)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                );
    }

    /**
     * Grupo de documentación para los endpoints relacionados con personas.
     * Permite filtrar las rutas que se muestran en un grupo separado dentro de Swagger UI.
     *
     * @return instancia de {@link GroupedOpenApi} para el grupo "person"
     */
    @Bean
    public GroupedOpenApi personApi() {
        return GroupedOpenApi.builder()
                .group("person")
                .pathsToMatch("/person/**")
                .build();
    }

    /**
     * Grupo de documentación para los endpoints relacionados con ubicaciones geográficas.
     * (países y ciudades).
     *
     * @return instancia de {@link GroupedOpenApi} para el grupo "location"
     */
    @Bean
    public GroupedOpenApi locationApi() {
        return GroupedOpenApi.builder()
                .group("location")
                .pathsToMatch("/v1/api/countries/**", "/v1/api/cities/**")
                .build();
    }

    /**
     * Grupo de documentación para los endpoints relacionados con equipos.
     * (marcas, fabricantes, modelos, tipos de equipo, equipos y verificaciones técnicas).
     *
     * @return instancia de {@link GroupedOpenApi} para el grupo "equipment"
     */
    @Bean
    public GroupedOpenApi equipmentApi() {
        return GroupedOpenApi.builder()
                .group("equipment")
                .pathsToMatch("/v1/api/brands/**", "/v1/api/manufacturers/**", "/v1/api/models/**",
                        "/v1/api/equipment/**", "/v1/api/equipment-types/**",
                        "/v1/api/technical-verifications/**")
                .build();
    }

    /**
     * Grupo de documentación para los endpoints relacionados con clientes.
     * Permite filtrar las rutas que se muestran en un grupo separado dentro de Swagger UI.
     *
     * @return instancia de {@link GroupedOpenApi} para el grupo "client"
     */
    @Bean
    public GroupedOpenApi clientApi() {
        return GroupedOpenApi.builder()
                .group("client")
                .pathsToMatch("/client/**") // Incluye todas las rutas bajo /client/**
                .build();
    }

    /**
     * Grupo de documentación para los endpoints relacionados con sedes.
     * Permite filtrar las rutas que se muestran en un grupo separado dentro de Swagger UI.
     *
     * @return instancia de {@link GroupedOpenApi} para el grupo "headquarter"
     */
    @Bean
    public GroupedOpenApi headquarterApi() {
        return GroupedOpenApi.builder()
                .group("headquarter")
                .pathsToMatch("/headquarter/**") // Incluye todas las rutas bajo /headquarter/**
                .build();
    }

    /**
     * Grupo de documentación para los endpoints relacionados con áreas de servicio.
     * Permite filtrar las rutas que se muestran en un grupo separado dentro de Swagger UI.
     *
     * @return instancia de {@link GroupedOpenApi} para el grupo "service-area"
     */
    @Bean
    public GroupedOpenApi serviceAreaApi() {
        return GroupedOpenApi.builder()
                .group("service-area")
                .pathsToMatch("/service-area/**") // Incluye todas las rutas bajo /service-area/**
                .build();
    }

    /**
     * Grupo de documentación para los endpoints relacionados con los recursos de los clientes.
     * Permite filtrar las rutas que se muestran en un grupo separado dentro de Swagger UI.
     *
     * @return instancia de {@link GroupedOpenApi} para el grupo "client-equipment"
     */
    @Bean
    public GroupedOpenApi clientEquipmentApi() {
        return GroupedOpenApi.builder()
                .group("client-equipment")
                .pathsToMatch("/client-equipment/**") // Incluye todas las rutas bajo /client-equipment/**
                .build();
    }
}

