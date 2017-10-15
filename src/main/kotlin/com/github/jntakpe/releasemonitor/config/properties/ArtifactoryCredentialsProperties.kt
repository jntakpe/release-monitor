package com.github.jntakpe.releasemonitor.config.properties

import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotNull

@Validated
class ArtifactoryCredentialsProperties {

    @NotNull
    lateinit var username: String

    @NotNull
    lateinit var password: String

}