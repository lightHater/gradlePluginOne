package com.example.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class MyPlugin implements Plugin<Project> {

    @Override
    void apply(Project target) {
        println("MyPlugin apply execute $target")
        target.extensions.create("myPlugin", MyExtension)
        target.android.registerTransform(new MyTransform())
        target.afterEvaluate {
            println('MyPlugin afterEvaluate')
            MyExtension extension = target.extensions.findByName('myPlugin')
            println("MyPlugin extension ${extension.version} ${extension.config}")
        }
    }
}

