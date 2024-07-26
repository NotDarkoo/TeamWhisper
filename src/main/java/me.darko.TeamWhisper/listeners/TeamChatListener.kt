package me.darko.TeamWhisper

import me.darko.TeamWhisper.commands.TeamChatCommand
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerChatEvent

class TeamChatListener(private val teamChatCommand: TeamChatCommand, private val playerChatMode: MutableMap<Player, Boolean>) : Listener {

    @EventHandler(priority = EventPriority.HIGH)
    fun onPlayerChat(event: PlayerChatEvent) {
        val player = event.player
        val isInTeamChat = playerChatMode.getOrDefault(player, false)

        val team = TeamManager.getTeamByPlayer(player)

        if (isInTeamChat) {
            if (team == null) {
                playerChatMode[player] = false
                return
            }
            event.isCancelled = true
            team.sendMessage(player.name + ": " + event.message)
        }
    }
}
