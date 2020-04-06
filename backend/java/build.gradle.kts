subprojects {

    group = "com.actlem"
    version = "1.0-SNAPSHOT"

    ext {
        set("easyRandomVersion", "4.0.0")
    }

    apply(plugin = "java")

    repositories {
        mavenCentral()
    }

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_13
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

