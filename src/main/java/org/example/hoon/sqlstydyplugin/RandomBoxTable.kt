package org.example.hoon.sqlstydyplugin

import org.bukkit.entity.Player
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.transactions.transaction

class RandomBoxTable {

    object RandomBoxes : IntIdTable() {
        val name = varchar("name", 255)
    }

    object RandomBoxItems : IntIdTable() {
        val randomBoxId = reference("randomBoxId", RandomBoxes)
        val item = blob("itemId")
    }

    class RandomBox(id: EntityID<Int>) : IntEntity(id) {

        companion object : IntEntityClass<RandomBox>(RandomBoxes)

        var name by RandomBoxes.name

        fun open(player: Player) {
            transaction {
                RandomBoxItem.find {
                    RandomBoxItems.randomBoxId eq this@RandomBox.id.value
                }
                    .toList()
                    .random()
                    .itemStack.let { itemStack ->
                        player.inventory.addItem(itemStack) }
            }
        }
    }

    class RandomBoxItem(id: EntityID<Int>) : IntEntity(id) {
        companion object : IntEntityClass<RandomBoxItem>(RandomBoxItems)

        var randomBoxId by RandomBoxItems.randomBoxId
        var item by RandomBoxItems.item
        val itemStack get() = item.bytes.decode()
    }


}