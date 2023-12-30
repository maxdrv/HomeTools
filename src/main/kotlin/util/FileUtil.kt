package util

import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.name

class TFile(val path: Path) {

    init {
        if (!path.toFile().exists()) {
            throw RuntimeException("file $path does not exists")
        }
        if (!path.toFile().isFile) {
            throw RuntimeException("$path is not a file")
        }
    }

    fun read(): ByteArray {
        return Files.readAllBytes(path)
    }

    fun name(): String {
        return path.name
    }

    override fun toString(): String {
        return "$path"
    }
}

class TDir(val path: Path) {

    init {
        if (!path.toFile().exists()) {
            throw RuntimeException("directory $path does not exists")
        }
        if (!path.toFile().isDirectory) {
            throw RuntimeException("$path is not a directory")
        }
    }

    fun list(): List<TFile> {
        return path.toFile().walk()
            .filter { f: File -> f.isFile }
            .map { f: File -> TFile(f.toPath()) }
            .toList()
    }

    override fun toString(): String {
        return "$path"
    }
}

fun createFileIfNotExists(path: Path, content: ByteArray): TFile {
    if (!path.toFile().exists()) {
        val success = path.toFile().createNewFile()
        if (!success) {
            throw RuntimeException("unable to create $path")
        }
        Files.write(path, content)
    }
    return TFile(path)
}