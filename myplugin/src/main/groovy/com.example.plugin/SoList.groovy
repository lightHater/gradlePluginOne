package com.example.plugin

class SoList {

    List<String> armv7a = new ArrayList()
    List<String> armv8a = new ArrayList()
    def ANSI_RED = "\u001B[31m"
    def ANSI_RESET = "\u001B[0m"

    void addToSoList(File file) {
        if (file == null) {
            return
        }
        if (file.isDirectory()) {
            file.listFiles().each {
                addToSoList(it)
            }
        } else {
            if (!file.absolutePath.endsWith(".so")) {
                return
            }
            println("got so file $file")
            String filePath = file.absolutePath
            if (filePath.contains("armeabi-v7a")) {
                armv7a.add(file.name)
            } else if (filePath.contains("arm64-v8a")) {
                armv8a.add(file.name)
            }
        }
    }

    void checkSo() {
        println('armv7a contains:')
        for (String file : armv7a) {
            println("$file")
        }
        println('armv8a contains:')
        for (String file: armv8a) {
            println("$file")
        }
        println('armv7a have and armv8a not have:')
        for (String file : armv7a) {
            if (!armv8a.contains(file)) {
                println("$ANSI_RED $file $ANSI_RESET")
            }
        }
    }

}