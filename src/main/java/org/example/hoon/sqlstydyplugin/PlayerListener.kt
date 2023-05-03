package org.example.hoon.sqlstydyplugin

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.jetbrains.exposed.sql.transactions.transaction

class PlayerListener : Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        transaction {
            if (!User.find { Users.uuid eq event.player.uniqueId }.empty()) {
                return@transaction
            }
            User.new {
                uuid = event.player.uniqueId
                nickname = event.player.name
            }
        }
    }
}