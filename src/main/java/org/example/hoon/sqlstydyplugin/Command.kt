package org.example.hoon.sqlstydyplugin

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.jetbrains.exposed.sql.transactions.transaction

class Command : CommandExecutor {
    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>?): Boolean {
        val player = p0 as Player

        transaction {
            RandomBoxTable.RandomBox.all().firstOrNull()?.open(player)
        }

//        transaction {
//            val user = User.find { Users.uuid eq player.uniqueId }.firstOrNull()
//            if (user == null) {
//                player.sendMessage("유저 정보가 없습니다.")
//                return@transaction
//            }
//            user.nickname = "바뀐 닉네임"
//            player.sendMessage("닉네임이 바뀌었습니다.")
//        }
        return true
    }
}