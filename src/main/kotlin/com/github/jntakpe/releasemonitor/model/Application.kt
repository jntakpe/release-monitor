package com.github.jntakpe.releasemonitor.model

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.mapping.Document

@Document
@CompoundIndex(name = "group_name", def = "{'group' : 1, 'name': 1}", unique = true)
class Application(val name: String, val group: String, val id: ObjectId? = null) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Application) return false

        if (name != other.name) return false
        if (group != other.group) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + group.hashCode()
        return result
    }

}
