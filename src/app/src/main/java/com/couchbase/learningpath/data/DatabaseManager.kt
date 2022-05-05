package com.couchbase.learningpath.data

import android.content.Context
import com.couchbase.learningpath.models.User
import com.couchbase.learningpath.util.Singleton
import com.couchbase.lite.*
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

class DatabaseManager private constructor(private val context: Context) {

    var inventoryDatabase: Database? = null
    var locationDatabase: Database? = null

    private val defaultInventoryDatabaseName = "inventory"
    private val locationDatabaseName = "locations"
    private val startingLocationFileName = "startinglocations.zip"
    private val startingLocationDatabaseName = "startinglocations"

    private val typeIndexName = "idxType"
    private val typeAttributeName = "type"

    private val teamIndexName = "idxTeam"
    private val teamAttributeName = "team"

    private val cityIndexName = "idxCityType"
    private val cityAttributeName = "city"

    private val cityCountryIndexName = "idxCityCountryType"
    private val countryAttributeName = "city"

    var currentInventoryDatabaseName = "inventory"

    init {
        //setup couchbase lite
        CouchbaseLite.init(context)

        //turn on uber logging - in production apps this shouldn't be turn on
        Database.log.console.domains = LogDomain.ALL_DOMAINS
        Database.log.console.level = LogLevel.VERBOSE
    }

    fun dispose() {
        inventoryDatabase?.close()
        locationDatabase?.close()
    }

    fun deleteDatabases() {
        try {
            closeDatabases()
            Database.delete(currentInventoryDatabaseName, context.filesDir)
            Database.delete(locationDatabaseName, context.filesDir)
        } catch (e: Exception) {
            android.util.Log.e(e.message, e.stackTraceToString())
        }
    }

    fun closeDatabases() {
        try {
            inventoryDatabase?.close()
            locationDatabase?.close()
        } catch (e: java.lang.Exception) {
            android.util.Log.e(e.message, e.stackTraceToString())
        }
    }

    fun initializeDatabases(
        currentUser: User
    ) {
        try {
            val dbConfig = DatabaseConfigurationFactory.create(context.filesDir.toString())
            // create the locations database if it doesn't already exist
            if (!Database.exists(locationDatabaseName, context.filesDir)) {
                unzip(startingLocationFileName, File(context.filesDir.toString()))

                // copy the location database to the project database
                // never open the database directly as this will cause issues
                // with sync
                val locationDbFile =
                    File(String.format("%s/%s", context.filesDir, ("${startingLocationDatabaseName}.cblite2")))
                Database.copy(locationDbFile, locationDatabaseName, dbConfig)
            }
            locationDatabase = Database(locationDatabaseName, dbConfig)

            // create or open a database to share between team members to store
            // projects, assets, and user profiles
            // calculate database name based on current logged in users team name
            val teamName = (currentUser.team.filterNot { it.isWhitespace() }).lowercase()
            currentInventoryDatabaseName = teamName.plus("_").plus(defaultInventoryDatabaseName)
            inventoryDatabase = Database(currentInventoryDatabaseName, dbConfig)

            createTypeIndex(locationDatabase)
            createTypeIndex(inventoryDatabase)
            createTeamTypeIndex()
            createCityTypeIndex()
            createCityCountryTypeIndex()

        } catch (e: Exception) {
            android.util.Log.e(e.message, e.stackTraceToString())
        }
    }

    private fun createTeamTypeIndex(){
        try {
            inventoryDatabase?.let {  // 1
                if (!it.indexes.contains(teamIndexName)) {
                    // create index for ProjectListView to only return documents with
                    // the type attribute set to project and the team attribute set to the
                    // logged in users team
                    it.createIndex( // 2
                        teamIndexName, // 3
                        IndexBuilder.valueIndex(   // 4
                            ValueIndexItem.property(typeAttributeName), // 5
                            ValueIndexItem.property(teamAttributeName)) // 5
                    )
                }
            }
        } catch (e: Exception){
            android.util.Log.e(e.message, e.stackTraceToString())
        }
    }

    private fun createCityTypeIndex(){
        try {
            inventoryDatabase?.let {  // 1
                if (!it.indexes.contains(cityIndexName)) {
                    // create index for Locations only return documents with
                    // the type attribute set to location and the city attribute filtered
                    // by value sent in using `like` statement
                    it.createIndex( // 3
                        cityIndexName, // 4
                        IndexBuilder.valueIndex(   // 5
                            ValueIndexItem.property(typeAttributeName), // 5
                            ValueIndexItem.property(cityAttributeName)) // 5
                    )
                }
            }
        } catch (e: Exception){
            android.util.Log.e(e.message, e.stackTraceToString())
        }
    }

    private fun createCityCountryTypeIndex(){
        try {
            inventoryDatabase?.let {  // 1
                if (!it.indexes.contains(cityIndexName)) {
                    // create index for Locations only return documents with
                    // the type attribute set to location, the city attribute filtered
                    // by value sent in using `like` statement, and the country attribute filtered
                    // by the value sent in using `like` statement

                    it.createIndex( // 3
                        cityCountryIndexName, // 4
                        IndexBuilder.valueIndex(   // 5
                            ValueIndexItem.property(typeAttributeName), // 5
                            ValueIndexItem.property(cityAttributeName), // 5
                            ValueIndexItem.property(countryAttributeName)) // 5
                    )
                }
            }
        } catch (e: Exception){
            android.util.Log.e(e.message, e.stackTraceToString())
        }
    }

    private fun createTypeIndex(
        database: Database?
    ) {
        // create indexes for document type
        // create index for document type if it doesn't exist
        database?.let {
            if (!it.indexes.contains(typeIndexName)) {
                it.createIndex(
                    typeIndexName, IndexBuilder.valueIndex(
                        ValueIndexItem.expression(
                            Expression.property(typeAttributeName)
                        )
                    )
                )
            }
        }
    }

    private fun unzip(
        file: String,
        destination: File
    ) {
        context.assets.open(file).use { stream ->
            val buffer = ByteArray(1024)
            val zis = ZipInputStream(stream)
            var ze: ZipEntry? = zis.nextEntry
            while (ze != null) {
                val fileName: String = ze.name
                val newFile = File(destination, fileName)
                if (ze.isDirectory) {
                    newFile.mkdirs()
                } else {
                    File(newFile.parent!!).mkdirs()
                    val fos = FileOutputStream(newFile)
                    var len: Int
                    while (zis.read(buffer).also { len = it } > 0) {
                        fos.write(buffer, 0, len)
                    }
                    fos.close()
                }
                ze = zis.nextEntry
            }
            zis.closeEntry()
            zis.close()
            stream.close()
        }
    }

    companion object : Singleton<DatabaseManager, Context>(::DatabaseManager)
}