val jupiterVersion = "5.5.2"
val mockitoVersion = "3.1.0"
val easyRandomVersion : String? by ext

dependencies {
    implementation(project(":commons"))
    implementation("org.jeasy:easy-random-core:${easyRandomVersion}")
    implementation("org.junit.jupiter:junit-jupiter-api:${jupiterVersion}")
    implementation( "org.mockito:mockito-junit-jupiter:${mockitoVersion}")
}
