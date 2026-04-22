plugins {
    id("com.diffplug.spotless") version libs.versions.spotless
}

repositories {
    mavenCentral()
}

spotless {
    // Java
    java {
        target("services/**/*.java")
        targetExclude("**/build/**")

        googleJavaFormat("1.35.0")
        removeUnusedImports()
        importOrder()
    }

    // YAML
    format("yaml") {
        target("**/*.yml", "**/*.yaml")
        targetExclude("**/build/**", "**/node_modules/**")

        prettier().config(
            mapOf(
                "tabWidth" to 2,
            ),
        )
    }

    // JSON
    format("json") {
        target("**/*.json")
        targetExclude("**/build/**", "**/node_modules/**")

        prettier()
    }

    // Markdown
    format("markdown") {
        target("**/*.md")
        targetExclude("**/node_modules/**")

        prettier()
    }

    // Gradle Kotlin
    kotlinGradle {
        target("*.gradle.kts", "**/*.gradle.kts")

        ktlint()
    }

    // Misc
    format("misc") {
        target("**/*.sh", "**/*.txt")
        targetExclude("**/build/**")

        trimTrailingWhitespace()
        endWithNewline()
    }
}
