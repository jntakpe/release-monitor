package com.github.jntakpe.releasemonitor.repository

import com.github.jntakpe.releasemonitor.model.Application
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface ApplicationRepository : ReactiveMongoRepository<Application, ObjectId>