package com.example.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

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
            applyCheck64Support(target)
        }
    }

    static void applyCheck64Support(Project target) {
        Task mergeNativeTask = null
        target.tasks.forEach {
            if (it.name.startsWith("merge") && it.name.endsWith("NativeLibs")) {
                mergeNativeTask = it
            }
        }
        if (mergeNativeTask == null) {
            println('not have mergeNativeLibsTask')
        }
        println("got mergeNativeLibsTask $mergeNativeTask")
        target.tasks.create("check64Support") {
            group('check64so')
            dependsOn(mergeNativeTask)
            doFirst {
                SoList list = new SoList()
                mergeNativeTask.inputs.files.each {
                    list.addToSoList(it)
                }
                list.checkSo()
            }
        }
    }
}

