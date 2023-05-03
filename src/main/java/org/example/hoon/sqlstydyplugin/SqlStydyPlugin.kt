package org.example.hoon.sqlstydyplugin

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.util.io.BukkitObjectInputStream
import org.bukkit.util.io.BukkitObjectOutputStream
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class SqlStydyPlugin : JavaPlugin() {
    override fun onEnable() {
        Database.connect("jdbc:sqlite:${dataFolder.path}/myDatabase.db", "org.sqlite.JDBC")

        transaction {
            SchemaUtils.drop(Users)
            SchemaUtils.create(Users)

            SchemaUtils.drop(RandomBoxTable.RandomBoxes)
            SchemaUtils.drop(RandomBoxTable.RandomBoxItems)

            SchemaUtils.create(RandomBoxTable.RandomBoxes)
            SchemaUtils.create(RandomBoxTable.RandomBoxItems)

            val randomBox = RandomBoxTable.RandomBox.new { name = "랜덤박스1" }
            val diamond = ItemStack(Material.DIAMOND)
            val iron = ItemStack(Material.IRON_INGOT)
            val gold = ItemStack(Material.GOLD_INGOT)

            RandomBoxTable.RandomBoxItem.new {
                randomBoxId = randomBox.id
                item = ExposedBlob(diamond.encode())
            }
            RandomBoxTable.RandomBoxItem.new {
                randomBoxId = randomBox.id
                item = ExposedBlob(iron.encode())
            }
            RandomBoxTable.RandomBoxItem.new {
                randomBoxId = randomBox.id
                item = ExposedBlob(gold.encode())
            }
        }


        server.pluginManager.registerEvents(PlayerListener(), this)
        getCommand("sql")?.setExecutor(Command())
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}

fun ItemStack.encode(): ByteArray {
    val outputStream = ByteArrayOutputStream()
    val dataOutput = BukkitObjectOutputStream(outputStream)
    dataOutput.writeObject(this)
    dataOutput.close()
    return outputStream.toByteArray()
}

fun ByteArray.decode(): ItemStack {
    val inputStream = ByteArrayInputStream(this)
    val dataInput = BukkitObjectInputStream(inputStream)
    return dataInput.readObject() as ItemStack
}

object Users : IntIdTable() {
    val uuid = uuid("uuid").uniqueIndex()
    val nickname = varchar("nickname", 255)
}


class User(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<User>(Users)

    var uuid by Users.uuid
    var nickname by Users.nickname
}
