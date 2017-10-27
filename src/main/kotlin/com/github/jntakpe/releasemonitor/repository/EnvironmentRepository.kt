package com.github.jntakpe.releasemonitor.repository

import com.github.jntakpe.releasemonitor.model.Environment
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface EnvironmentRepository : ReactiveMongoRepository<Environment, ObjectId>