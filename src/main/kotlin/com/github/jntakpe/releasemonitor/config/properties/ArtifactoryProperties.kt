package com.github.jntakpe.releasemonitor.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@Component
@Validated
@ConfigurationProperties("artifactory")
class ArtifactoryProperties {

    @NotBlank
    var host: String = ""

    @NotBlank
    var gradleRepository: String = ""

    @Valid
    var credentials: ArtifactoryCredentialsProperties = ArtifactoryCredentialsProperties()

}