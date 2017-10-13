package com.github.jntakpe.releasemonitor.config.properties

import javax.validation.constraints.NotBlank

class ArtifactoryCredentialsProperties {

    @NotBlank
    var username: String = ""

    @NotBlank
    var password: String = ""

}