package com.thelastpickle.tlpcluster.containers

import com.github.dockerjava.api.model.AccessMode
import com.thelastpickle.tlpcluster.*
import java.io.File


class CassandraBuildJava8(val context: Context) {

    private val docker = Docker(context)

    val home = File(System.getProperty("user.home"))
    val builds = File(home, ".tlp-cluster/builds/")
    val mavenCache = File(home, ".tlp-cluster/maven-cache/").toString()


    /**
     * name of the build in the end
     */
    fun runBuild(location: String, name: String) : Result<String> {

        return docker
                .addVolume(VolumeMapping(location, "/cassandra", AccessMode.ro))
                .addVolume(VolumeMapping(File(builds, name).toString(), "/builds/", AccessMode.rw))
                .addVolume(VolumeMapping(mavenCache, "/root/.m2/", AccessMode.rw))
                .runContainer(
                Containers.CASSANDRA_BUILD,
                mutableListOf("/bin/sh", "/usr/local/bin/build_cassandra.sh"),
                "/cassandra"
        )
    }
}