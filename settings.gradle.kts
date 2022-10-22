rootProject.name = "tsa-server"

include(
        "domain",
        "signing:signing-api",
        "signing:signing-impl",
        "datetime:datetime-api",
        "datetime:datetime-impl",
//        "app",
//        "web",
//        "integration",
//        "persistence",
        "test-util"
)
