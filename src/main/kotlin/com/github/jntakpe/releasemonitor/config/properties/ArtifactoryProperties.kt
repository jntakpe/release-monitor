package com.github.jntakpe.releasemonitor.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotNull

@Component
@Validated
@ConfigurationProperties("artifactory")
class ArtifactoryProperties {

    @NotNull
    lateinit var host: String

    @NotNull
    lateinit var gradleRepository: String

    var credentials: ArtifactoryCredentialsProperties = ArtifactoryCredentialsProperties()

}