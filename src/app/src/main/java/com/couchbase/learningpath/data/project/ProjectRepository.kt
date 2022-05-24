package com.couchbase.learningpath.data.project

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.serialization.ExperimentalSerializationApi

import com.couchbase.learningpath.data.Repository
import com.couchbase.learningpath.models.Location
import com.couchbase.learningpath.models.Project

@ExperimentalCoroutinesApi
@ExperimentalSerializationApi
interface ProjectRepository : Repository<Project> {
    val databaseName: String

    suspend fun completeProject(projectId: String)

    suspend fun get(documentId: String): Project

    //required because cblite queryChangeFlow method is marked experimental
    @ExperimentalCoroutinesApi
    fun getDocuments(team: String): Flow<List<Project>>

    suspend fun updateProjectLocation(projectId: String, location: Location)

    suspend fun loadSampleData()
}