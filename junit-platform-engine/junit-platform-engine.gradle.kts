plugins {
	id("junitbuild.java-library-conventions")
	id("junitbuild.java-multi-release-sources")
	`java-test-fixtures`
}

description = "JUnit Platform Engine API"

dependencies {
	api(platform(projects.junitBom))
	api(libs.opentest4j)
	api(projects.junitPlatformCommons)

	compileOnlyApi(libs.apiguardian)

	testImplementation(libs.assertj)

	osgiVerification(projects.junitJupiterEngine)
	osgiVerification(projects.junitPlatformLauncher)
}

tasks.jar {
	val release19ClassesDir = project.sourceSets.mainRelease19.get().output.classesDirs.singleFile
	inputs.dir(release19ClassesDir).withPathSensitivity(PathSensitivity.RELATIVE)
	doLast(objects.newInstance(junitbuild.java.ExecJarAction::class).apply {
		javaLauncher.set(project.javaToolchains.launcherFor(java.toolchain))
		args.addAll(
			"--update",
			"--file", archiveFile.get().asFile.absolutePath,
			"--release", "19",
			"-C", release19ClassesDir.absolutePath, "."
		)
	})
}


eclipse {
	classpath {
		sourceSets -= project.sourceSets.mainRelease19.get()
	}
}
